package RESTController;

import Model.API.Journal.Journal;
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
public class RESTLogin {
    private final String myDBUsername = "dbUsername";
    private final String myDBPassword = "dbPassword";
    private final String myDBURL = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getMainDbPath());

    @GetMapping("/")
    public ResponseEntity<Journal> login(@RequestParam(value="user") String username, @RequestParam(value = "pwd") String password) {
        try {
            Journal journal = new LoginAPI().login(username, password);
            return ResponseEntity.ok(journal);
        }
        catch(InvalidLoginException e){
            System.out.print(e.toString());
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/users")
    public ResponsePair makeAccount(@RequestParam(value="user") String username, @RequestParam(value = "pwd") String password) {
        try {
            int userId = new LoginAPI().makeAccount(username, password);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{username}/{password}/{userID}")
                    .buildAndExpand(username, password, userId)
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