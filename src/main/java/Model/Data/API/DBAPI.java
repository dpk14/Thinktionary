package Model.Data.API;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class DBAPI {

    protected String myDBUrl;
    protected String myDBUsername;
    protected String myDBPassword;
    protected String myDBRelFilename;
    protected String myDBAbsFilename;

    public DBAPI() {
        try {
            URI dbUri = new URI(System.getenv("DATABASE_URL"));

            myDBUsername = dbUri.getUserInfo().split(":")[0];
            myDBPassword = dbUri.getUserInfo().split(":")[1];
            myDBUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
