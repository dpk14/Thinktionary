package Model.Data.Utils;

public class AWSCredentials {
    private static final String ACCESS_SYSTEM_KEY = "aws.accessKeyId";
    private static final String SECRET_SYSTEM_KEY = "aws.secretKey";


    private String awsAccessKey;
    private String awsSecretKey;

    public AWSCredentials (String awsAccessKey, String awsSecretKey) {
        this.awsAccessKey = awsAccessKey;
        this.awsSecretKey = awsSecretKey;
        System.out.println(awsAccessKey);
        System.out.println(awsSecretKey);
    }

    public void apply() {
        System.setProperty(ACCESS_SYSTEM_KEY, awsAccessKey);
        System.setProperty(SECRET_SYSTEM_KEY, awsSecretKey);
    }
}
