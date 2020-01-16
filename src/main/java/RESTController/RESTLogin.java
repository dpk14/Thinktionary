package RESTController;

import Model.API.Login.LoginAPI;
import Model.Data.Lib.Paths.DBFileNames;
import Model.Data.Lib.Paths.DBNames;
import Model.Data.Lib.Paths.DBUrls;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;
import RESTController.SerializableModels.ErrorMessage;
import RESTController.SerializableModels.ResponsePair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/login")
public class RESTLogin {
    private final String myDBUsername = "dbUsername";
    private final String myDBPassword = "dbPassword";
    private final String myDBURL = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getMainDbPath());

    @GetMapping("/")
    public ResponseEntity<Integer> login(@RequestParam String username, @RequestParam String password) {
        LoginAPI loginAPI = new LoginAPI(myDBUsername, myDBPassword, myDBURL);
        try {
            int userId = loginAPI.login(username, password);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{userId}")
                    .buildAndExpand(userId)
                    .toUri();

            return ResponseEntity.created(uri).body(userId);
        }
        catch(InvalidLoginException e){
            System.out.print(e.toString());
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/makeAccount")
    public ResponsePair makeAccount(@RequestParam String username, @RequestParam String password) {
        LoginAPI loginAPI = new LoginAPI(myDBUsername, myDBPassword, myDBURL);
        try {
            loginAPI.makeAccount(username, password);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{userId}")
                    .buildAndExpand(userId)
                    .toUri();

            return new ResponsePair(ResponseEntity.created(uri).body(userId),
                    null);
            return null;
        }
        catch(AccountExistsException e){
            return new ErrorMessage(e.toString(), e.getStackTrace());
        }

        Resp
    }

    //makeaccount needs to add /id, the id is another layer validating data

}