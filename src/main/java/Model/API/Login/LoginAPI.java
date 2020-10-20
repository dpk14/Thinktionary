package Model.API.Login;

import Model.API.Journal.Journal;
import Model.Data.API.Run.LoginDBAPI;
import Model.ErrorHandling.Exceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.InvalidEmailException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.InvalidLoginException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.UserErrorException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static sun.security.x509.X509CertInfo.SUBJECT;

public class LoginAPI {

    private static final String FROM = "thinktionary.app@gmail.com";
    private static String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
            + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
            + "AWS SDK for Java</a>";

    static final String TEXTBODY = "This email was sent through Amazon SES "
            + "using the AWS SDK for Java.";

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
        loginDBAPI.storeEmailConfirmationKey(email, email);
        try {
            sendEmail(email, emailKey);
        }
        catch (Exception e) {
            loginDBAPI.removeEmailConfirmationKey(email);
            throw new InvalidEmailException();
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

    private static void sendEmail(String to, int emailKey) {
        AmazonSimpleEmailService client =
                AmazonSimpleEmailServiceClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1).build();
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(to))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8").withData(HTMLBODY))
                                .withText(new Content()
                                        .withCharset("UTF-8").withData(Integer.toString(emailKey))))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(SUBJECT)))
                .withSource(FROM);
        client.sendEmail(request);
    }
}

