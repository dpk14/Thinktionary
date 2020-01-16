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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RESTLogin {

    @GetMapping("/")
    public ResponseEntity login(HttpServletRequest httpServletRequest,
                                @RequestParam(value="user") String username, @RequestParam(value = "pwd") String password) {
        try {
            Journal journal = new LoginAPI().login(username, password);
            httpServletRequest.getSession().setAttribute(RESTStrings.getJournalAttribute(), journal);
            return ResponseEntity.ok(journal);
        }
        catch(InvalidLoginException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }

    }

    @PostMapping("/users")
    public ResponseEntity makeAccount(@RequestParam(value="user") String username, @RequestParam(value = "pwd") String password) {
        try {
            int userId = new LoginAPI().makeAccount(username, password);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{username}/{password}/{userID}")
                    .buildAndExpand(username, password, userId)
                    .toUri();

            return ResponseEntity.created(uri).body(userId);
        }
        catch(AccountExistsException e){
            System.out.print(e.toString());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
    }

    //makeaccount needs to add /id, the id is another layer validating data

}