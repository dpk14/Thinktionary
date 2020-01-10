package RESTController;

import Model.API.Login.LoginAPI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RESTLogin {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final String myDBUsername = "dbUsername";
    private final String myDBPassword = "dbPassword";
    private final String myDBURL = "iahe";

    @RequestMapping("/login")
    public void greeting(@RequestParam(value = "username", defaultValue = "") String username,
                             @RequestParam(value = "password", defaultValue = "") String password) {

    }
}