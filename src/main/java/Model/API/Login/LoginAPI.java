package Model.API.Login;

import Model.API.Journal.Journal;
import Model.Data.API.Run.LoginDBAPI;
import Model.ErrorHandling.Exceptions.ServerExceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.EmailDeliveryFailure;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.InvalidLoginException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.UserErrorException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class LoginAPI {

    private static final String FROM = "thinktionary.app@gmail.com";
    private static String SUBJECT = "Register for Thinktionary";
    private static String HTMLBODY = "<p>Hello %s,</p>"
            + "<p>We're stoked to have you onboard!</p> "
            + "<p>You're almost there. Just enter the verification key %s to start journaling!</p>"
            + "<p>All the Best,<br>Thinktionary.app</p>";

    private static String TEXTBODY = ">Hello %s,\n"
            + "We're pleased to have you onboard! Just enter the verification key %s to start journaling!\n"
            + "Best,\nThe Thinktionary Team";

    public static Journal login(String username, String password) throws InvalidLoginException, LoadPropertiesException {
        List<Map<String, Object>> userInfo = new LoginDBAPI().login(username, password);
        int userID = LoginDBParser.getUserID(userInfo);
        return new Journal(userID, username, password);
    }

    public static int makeAccount(String username, String password, String email, String verifyKey) throws UserErrorException, LoadPropertiesException { //second exception has two inheritances, password exists and username exists
        LoginDBAPI loginDBAPI = new LoginDBAPI();
        loginDBAPI.verifyConfirmationKey(verifyKey, email);
        List<Map<String, Object>> userInfo = new LoginDBAPI().createUser(username, password, email);
        return LoginDBParser.getUserID(userInfo);
    }

    public static void verifyAccountDoesNotExistAndGenerateEmailConfirmation(String username, String password, String email) throws UserErrorException, LoadPropertiesException { //second exception has two inheritances, password exists and username exists
        LoginDBAPI loginDBAPI = new LoginDBAPI();
        loginDBAPI.throwExceptionIfUserInfoExists(username, password, email);
        int emailKey = generateEmailConfirmationKey();
        loginDBAPI.storeEmailConfirmationKey(email, emailKey);
        try {
            sendEmail(email, emailKey, username);
        }
        catch (Exception e) {
            loginDBAPI.removeEmailConfirmationKey(email);
            throw new EmailDeliveryFailure(e);
        }
    }

    //Testing:

    public static Map<Integer, User> loadUserInfoMap() throws LoadPropertiesException {
        List<Map<String, Object>> userInfoTable = new LoginDBAPI().loadUserInfoTable();
        return LoginDBParser.parseUserInfoMap(userInfoTable);
    }


    //Helpers:

    private static int generateEmailConfirmationKey() {
        Random rnd = new Random();
        return 100000 + rnd.nextInt(900000);
    }

    private static void sendEmail(String to, int emailKey, String username) {
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
                                        .withCharset("UTF-8").withData(String.format(HTMLBODY, username, emailKey)))
                                .withText(new Content()
                                        .withCharset("UTF-8").withData(TEXTBODY)))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(SUBJECT)))
                .withSource(FROM);
        client.sendEmail(request);
    }
}

