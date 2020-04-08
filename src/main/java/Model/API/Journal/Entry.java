package Model.API.Journal;

import Model.API.Journal.EntryComponents.Date;
import Model.API.Journal.EntryComponents.Topic;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Entry {
    private String myTitle;
    private String myText;
    private LocalDateTime myCreated;
    private LocalDateTime myModified;
    private Set<Topic> myTopics;

    /*
    JournalDBAPI, modular unit of information managed by Journal. Packages outside JournalDBAPI backend package cannot create Entries, but if they have received
    an Entry through Journal methods, they have access to Entry getter methods, which allow them to interpret the contents of the Entry.
    Modifications to Entries must be done through Journal.
     */

    public Entry(){

    }

    public Entry(String myTitle, String myText, Set<Topic> myTopics){ //protected so only journals can instance entries
        myCreated = LocalDateTime.now();
        initialize(myTopics, myTitle, myText);
    }

    public Entry(String myTitle, String myText, LocalDateTime myCreated, Set<Topic> myTopics){ //protected so only journals can instance entries
        this.myCreated = myCreated;
        initialize(myTopics, myTitle, myText);
    }

    private void initialize(Set<Topic> topics, String title, String text){
        myTopics = topics;
        myModified = myCreated;
        myText = text;
        myTitle = title;
    }

    /*
    ----------------------------
    Public Run (Used by src.main.java.Model.Data module. Do not actually give access to the objects
                    just give access to RESTStrings symbolizing objects):
    ----------------------------
     */

    public static class EntryComparator implements Comparator<Entry> {
        @Override
        public int compare(Entry e1, Entry e2) {
            return e1.getMyCreatedDate().compareTo(e2.getMyCreatedDate());
        }

    }

    /*
    ----------------------------
    Protected Run (Used by internal src.main.java.Model methods):
    ----------------------------
     */

    protected boolean youngerThan(Entry e2){
        return getMyCreatedDate().compareTo(e2.getMyCreatedDate()) < 0;
    }

    protected void updateModification(){
        myModified = LocalDateTime.now();
    }

    protected LocalDateTime getMyCreatedDate(){ return myCreated; }

    /*
    ----------------------------
    Protected Setters (Used by internal src.main.java.Model methods):
    ----------------------------
     */

    protected void setMyTitle(String myTitle) {
        this.myTitle = myTitle;
    }

    protected void setMyTopics(Set<Topic> topics){
        myTopics = topics;
    }

    protected void setText(String text) {
        myText = text;
    }

    protected void setMyCreated(LocalDateTime date) {
        myCreated = date;
    }

    //Getters:

    public String getmyTitle() { return String.valueOf(myTitle); }

    public String getmyText() { return myText.toString(); }

    public String getMyCreated(){ return Date.LocalDateTimetoString(myCreated);}

    public String getMyModfied() {return Date.LocalDateTimetoString(myModified);}

    public Set<Topic> getMyTopics(){
        return myTopics;
    }

    public Map<String, String> myTopicsAsMap(){
        Map<String, String> topicToColor = new HashMap<>();
        for(Topic topic : myTopics){
            topicToColor.put(topic.getMyTopic(), topic.getMyColor());
        }
        return topicToColor;
    }

}
