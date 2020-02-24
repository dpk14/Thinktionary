package Controller;

import Controller.Exceptions.NotLoggedInException;
import Model.API.Journal.Journal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SessionManager {
    Map<Integer, Journal> myUsers;
    public SessionManager(){
        myUsers = new HashMap<>();
    }

    public void addUser(int id, Journal journal){
        myUsers.put(id, journal);
    }

    public void removeUser(int id){
        myUsers.remove(id);
    }

    public Journal getSessionInfo(int id) throws NotLoggedInException {
        if(!myUsers.containsKey(id)) throw new NotLoggedInException();
        return myUsers.get(id);
    }
}
