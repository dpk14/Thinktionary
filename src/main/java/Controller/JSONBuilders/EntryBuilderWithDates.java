package Controller.JSONBuilders;

import Model.API.Journal.Entry;
import Model.API.Journal.EntryComponents.Topic;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;

public class EntryBuilderWithDates {
    Entry myEntry;

    @JsonCreator
    public EntryBuilderWithDates(@JsonProperty("myCreated") LocalDateTime myCreated, @JsonProperty("myModfied") LocalDateTime myModified, @JsonProperty("myTitle") String myTitle, @JsonProperty("myText") String myText, @JsonProperty("myTopics") Set<Topic> myTopics){ //protected so only journals can instance entries
        myEntry = new Entry(myTitle, myText, myCreated, myModified, myTopics);
    }

    public Entry getMyEntry() {
        return myEntry;
    }
}
