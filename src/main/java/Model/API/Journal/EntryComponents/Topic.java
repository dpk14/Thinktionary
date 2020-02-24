package Model.API.Journal.EntryComponents;

public class Topic {
    String myTopic;
    String myColor;

    public Topic(){}

    public Topic(String topic, String color){
        myTopic = topic;
        myColor = color;
    }

    public String getMyTopic() {
        return myTopic;
    }

    public String getMyColor() {
        return myColor;
    }

    @Override
    public boolean equals(Object topic){
        return myTopic.equals((Topic) topic);
    }
}
