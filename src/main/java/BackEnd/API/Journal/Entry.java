package src.main.java.BackEnd.API.Journal;

import src.main.java.BackEnd.API.Journal.EntryComponents.Date;
import src.main.java.BackEnd.API.Journal.EntryComponents.Topic;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Entry {
    Set<Topic> myTopics;
    Date myCreated;
    Date myModified;
    String myText;
    String myTitle;

    /*
    JournalDBAPI, modular unit of information managed by JournalAPI. Packages outside JournalDBAPI backend package cannot create Entries, but if they have received
    an Entry through JournalAPI methods, they have access to Entry getter methods, which allow them to interpret the contents of the Entry.
    Modifications to Entries must be done through JournalAPI.
     */

    protected Entry(Set<Topic> topics, String title, String text){ //protected so only journals can instance entries
        myCreated = new Date();
        initialize(topics, title, text);
    }

    protected Entry(Set<Topic> topics, String title, String text, Date date){ //protected so only journals can instance entries
        myCreated = date;
        initialize(topics, title, text);
    }

    private void initialize(Set<Topic> topics, String title, String text){
        myTopics = topics;
        myModified = myCreated;
        myText = text;
        myTitle = title;
    }

    /*
    ----------------------------
    Public API (Used by src.main.java.BackEnd.Data module. Do not actually give access to the objects
                    just give access to Strings symbolizing objects):
    ----------------------------
     */

    public String getmyTitle() { return String.valueOf(myTitle); }

    public String getMyModfiedasString() {return myModified.toString();}

    public String getMyCreatedasString(){ return myCreated.toString();}

    public String getmyText() { return myText.toString(); }


    public Map<String, String> getMyTopicsAsMap(){
        Map<String, String> topicToColor = new HashMap<>();
        for(Topic topic : myTopics){
            topicToColor.put(topic.getMyTopic(), topic.getMyColor());
        }
        return topicToColor;
    }

    public static class EntryComparator implements Comparator<Entry> {
        @Override
        public int compare(Entry e1, Entry e2) {
            return e1.getMyCreated().compareTo(e2.getMyCreated());
        }

    }

    /*
    ----------------------------
    Protected API (Used by internal src.main.java.BackEnd methods):
    ----------------------------
     */

    protected boolean youngerThan(Entry e2){
        return getMyCreated().compareTo(e2.getMyCreated()) < 0;
    }

    protected void updateModification(){
        myModified = new Date();
    }

    protected Set<Topic> getMyTopics(){
        return myTopics;
    }

    protected Date getMyCreated(){ return myCreated; }

    /*
    ----------------------------
    Protected Setters (Used by internal src.main.java.BackEnd methods):
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

    protected void setMyCreated(Date date) {
        myCreated = date;
    }


}
