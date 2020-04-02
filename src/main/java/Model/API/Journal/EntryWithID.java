package Model.API.Journal;

import Model.API.Journal.EntryComponents.Topic;

import java.time.LocalDateTime;
import java.util.Set;

public class EntryWithID extends Entry{

    private int myID;

    public EntryWithID(){

    }

    public EntryWithID(String myTitle, String myText, Set<Topic> myTopics, int id){ //protected so only journals can instance entries
        super(myTitle, myText, myTopics);
        myID = id;
    }

    public EntryWithID(String myTitle, String myText, LocalDateTime myCreated, Set<Topic> myTopics, int id){ //protected so only journals can instance entries
        super(myTitle, myText, myCreated, myTopics);
        myID = id;
    }

    public EntryWithID(Entry entry, int id){
        super(entry.getmyTitle(), entry.getmyText(), entry.getMyCreatedDate(), entry.getMyTopics());
                myID = id;
    }

    public int getMyID() {
        return myID;
    }
}
