package Controller.JSONBuilders;

import Model.API.Journal.Entry;
import Model.API.Journal.EntryComponents.Topic;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class EntryBuilder {
    Entry myEntry;

    @JsonCreator
    public EntryBuilder(@JsonProperty("myTitle") String myTitle, @JsonProperty("myText") String myText, @JsonProperty("myTopics") Set<Topic> myTopics){ //protected so only journals can instance entries
        myEntry = new Entry(myTitle, myText, myTopics);
    }

    /*
    @JsonCreator
    public EntryBuilder(@JsonProperty("myCreated") LocalDateTime myCreated, @JsonProperty("myTitle") String myTitle, @JsonProperty("myText") String myText, @JsonProperty("myTopics") Set<Topic> myTopics){ //protected so only journals can instance entries
        myEntry = new Entry(myTitle, myText, myCreated, myTopics);
    }
     */

    public Entry getMyEntry() {
        return myEntry;
    }
}
