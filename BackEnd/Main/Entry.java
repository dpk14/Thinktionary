package BackEnd.Main;

import BackEnd.Main.Components.Date;
import BackEnd.Main.Components.Topic;

import java.util.HashSet;
import java.util.Set;

public class Entry {
    Set<Topic> myTopics;
    Date myCreated;
    Date myModified;
    String myText;
    String myTitle;
    String myColor;

    /*
    Main, modular unit of information managed by Journal. Packages outside Main backend package cannot create Entries, but if they have received
    an Entry through Journal methods, they have access to Entry getter methods, which allow them to interpret the contents of the Entry.
    Modifications to Entries must be done through Journal.
     */

    protected Entry(String title, Set<Topic> topics, String text, String color){ //protected so only journals can instance entries
        myCreated = new Date();
        initialize(title, topics, text, color);
    }

    protected Entry(String title, Set<Topic> topics, String text, String color, Date date){ //protected so only journals can instance entries
        myCreated = date;
        initialize(title, topics, text, color);
    }

    private void initialize(String title, Set<Topic> topics, String text, String color){
        myTopics = topics;
        myModified = myCreated;
        myText = text;
        myTitle = title;
        myColor = color;
    }

    /*
    ----------------------------
    Public API:
    ----------------------------
     */

    protected Date getMyCreated(){
        return myCreated;
    }

    public String getMyCreatedasString(){
        return myCreated.toString();
    }

    public String getMyText(){
        return myText;
    }

    public void setMyTitle(String myTitle) {
        this.myTitle = myTitle;
    }

    public String getMyColor() {
        return myColor;
    }

    public Set<String> getMyTopicStrings(){
        Set<String> topcs = new HashSet();
        for(Topic topic : myTopics){
            topcs.add(topic.getMyTopic());
        }
        return topcs;
    }

    /*
    ----------------------------
    Protected API (Used by internal BackEnd methods):
    ----------------------------
     */

    protected boolean youngerThan(Entry e2){
        return getMyCreated().compareTo(e2.getMyCreated()) < 0;
    }

    protected Set<Topic> getMyTopics(){
        return myTopics;
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

    protected void updateModification(){
        myModified = new Date();
    }
}
