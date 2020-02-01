package Controller;

import Model.API.Journal.Journal;
import Model.API.Login.LoginAPI;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

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
    
}