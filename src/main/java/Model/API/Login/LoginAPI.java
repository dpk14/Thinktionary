package Model.API.Login;

import Model.API.Journal.Journal;
import Model.Data.API.Run.LoginDBAPI;
import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.TableNames;
import Utils.ErrorHandling.Exceptions.UserErrorExceptions.*;
import Utils.Security.Encryptor;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LoginAPI {

    LoginDBAPI loginDBAPI;

    public LoginAPI(LoginDBAPI loginDBAPI) {
        this.loginDBAPI = loginDBAPI;
    }

    private static final String FROM = "thinktionary.app@gmail.com";
    private static String VERIFICATION_SUBJECT = "Register for Thinktionary";
    private static String VERIFICATION_HTMLBODY = "<p>Hello %s,</p>"
            + "<p>We're stoked to have you onboard!</p> "
            + "<p>You're almost there. Just enter your 24 hour verification key %s to start journaling!</p>"
            + "<p>All the Best,<br>Thinktionary.app</p>";

    private static String VERIFICATION_TEXTBODY = ">Hello %s,\n"
            + "We're pleased to have you onboard! Just enter the verification key %s to start journaling!\n"
            + "Best,\nThe Thinktionary Team";

    private static String PWD_RESET_SUBJECT = "Reset Password for Thinktionary";
    private static String PWD_RESET_HTMLBODY = "<p>Hello %s,</p>"
            + "<p>Yeah, those pesky passwords can be a nuisance to keep track of.</p> "
            + "<p>Enter the 24 hour verification key %s along with your new password to get back to journaling!</p>"
            + "<p>All the Best,<br>Thinktionary.app</p>";

    private static String PWD_RESET_TEXTBODY = "Hello %s,"
            + "\nYeah, those pesky passwords can be a nuisance to keep track of. "
            + "Enter the 24 hour verification key %s along with your new password to get back to journaling!"
            + "\nAll the Best,\nThinktionary.app";

    public Journal login(String username, String password) throws InvalidLoginException {
        List<Map<String, Object>> userInfo = this.loginDBAPI.login(username, password);
        int userID = LoginDBParser.getUserID(userInfo);
        String email = LoginDBParser.getEmail(userInfo);
        return new Journal(userID, username, email);
    }

    public int makeAccount(String username, String password, String email, String verifyKey) throws UserErrorException { //second exception has two inheritances, password exists and username exists;
        this.loginDBAPI.verifyConfirmationKey(verifyKey, email);
        List<Map<String, Object>> userInfo = this.loginDBAPI.createUser(username, password, email);
        return LoginDBParser.getUserID(userInfo);
    }

    public int deleteAccount(int userId) throws UserErrorException { //second exception has two inheritances, password exists and username exists;
        return this.loginDBAPI.deleteUser(userId);
    }

    public void verifyAccountDoesNotExistAndGenerateEmailConfirmation(String username, String email) throws UserErrorException { //second exception has two inheritances, password exists and username exists
        if (this.loginDBAPI.tableEntryExists(TableNames.getUserInfo(), ColumnInfo.getUSERNAME(), username)) {
            throw new AccountExistsException();
        } else if (this.loginDBAPI.tableEntryExists(TableNames.getUserInfo(), ColumnInfo.getEMAIL(), email)) {
            throw new EmailExistsException();
        }
        int emailKey = generateEmailConfirmationKey();
        this.loginDBAPI.storeEmailConfirmationKey(email, Encryptor.encryptMD5(Integer.toString(emailKey)));
        try {
            sendVerificationEmail(email, emailKey, username);
            //sendVerificationText(email, emailKey, username);
        } catch (Exception e) {
            this.loginDBAPI.removeEmailConfirmationKey(email);
            throw new EmailDeliveryFailure(e);
        }
    }

    public void verifyAccountExists(String username) throws UserErrorException { //second exception has two inheritances, password exists and username exists
        Map<String, String> colToVal = new HashMap<>();
        colToVal.put(ColumnInfo.getUSERNAME(), username);
        if (!this.loginDBAPI.tableEntryExists(TableNames.getUserInfo(), colToVal)) {
            throw new AccountNotRegisteredException();
        }
    }

    public void generateAndStoreEmailConfirmation(String email, String username) throws UserErrorException {
        int emailKey = generateEmailConfirmationKey();
        this.loginDBAPI.storeEmailConfirmationKey(email, Encryptor.encryptMD5(Integer.toString(emailKey)));
        try {
            sendPWDResetEmail(email, emailKey, username);
            //sendPWDResetText(email, emailKey, username);
        }
        catch (Exception e) {
            this.loginDBAPI.removeEmailConfirmationKey(email);
            throw new EmailDeliveryFailure(e);
        }
    }

    public void resetEmail(String email, String username, String verifyKey) throws UserErrorException {
        this.loginDBAPI.verifyConfirmationKey(verifyKey, email);
        this.loginDBAPI.changeUserInfo(username, ColumnInfo.getEMAIL(), email);
    }

    public void resetPassword(String username, String newPwd, String email, String verifyKey) throws UserErrorException {
        this.loginDBAPI.verifyConfirmationKey(verifyKey, email);
        this.loginDBAPI.changeUserInfo(username, ColumnInfo.getPASSWORD(), newPwd);
    }

    //Helpers:

    private static int generateEmailConfirmationKey() {
        Random rnd = new Random();
        return 100000 + rnd.nextInt(900000);
    }

    private static void sendVerificationEmail(String to, int emailKey, String username) {
        sendEmail(to, emailKey, username, VERIFICATION_HTMLBODY, VERIFICATION_TEXTBODY, VERIFICATION_SUBJECT);
    }

    private static void sendPWDResetEmail(String to, int emailKey, String username) {
        sendEmail(to, emailKey, username, PWD_RESET_HTMLBODY, PWD_RESET_HTMLBODY, PWD_RESET_SUBJECT);
    }

    private static void sendVerificationText(String to, int emailKey, String username) {
        sendText(to, emailKey, username, VERIFICATION_TEXTBODY);
    }

    private void sendPWDResetText(String to, int emailKey, String username) {
        sendText(to, emailKey, username, PWD_RESET_TEXTBODY);
    }


    private static void sendEmail(String to, int emailKey, String username, String htmlBody, String textBody, String subject) {
        AmazonSimpleEmailService client =
                AmazonSimpleEmailServiceClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1)
                        .withCredentials(new DefaultAWSCredentialsProviderChain())
                        .build();
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(to))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8").withData(String.format(htmlBody, username, emailKey)))
                                .withText(new Content()
                                        .withCharset("UTF-8").withData(textBody)))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(subject)))
                .withSource(FROM);
        client.sendEmail(request);
    }

    private static PublishResult sendText(String phoneNum, int emailKey, String username, String message) {
        AmazonSNS snsClient = AmazonSNSClient.builder()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

        return snsClient.publish(new PublishRequest()
                .withMessage(String.format(message, username, emailKey))
                .withPhoneNumber(phoneNum));
    }

}

