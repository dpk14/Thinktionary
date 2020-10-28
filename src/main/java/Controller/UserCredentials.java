package Controller;

import Utils.Security.Encryptor;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCredentials {
    private String username;
    private String pwd;
    private String email;
    private String confKey;

    @JsonCreator
    public UserCredentials(@JsonProperty("username") String username,
                           @JsonProperty("pwd") String pwd,
                           @JsonProperty("email") String email,
                           @JsonProperty("confKey") String confKey) {
        this.username = username;
        if (pwd != null) {
            System.out.println(pwd);
            this.pwd = Encryptor.encryptMD5(pwd);
        }
        this.email = email;
        if (confKey != null) {
            this.confKey = Encryptor.encryptMD5(confKey);
        }
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }

    public String getUsername() {
        return username;
    }

    public String getConfKey() {
        return confKey;
    }
}
