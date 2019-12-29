package BackEnd.Main.Components;

public class Topic {
    String myTopic;
    String myColor;

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
}
