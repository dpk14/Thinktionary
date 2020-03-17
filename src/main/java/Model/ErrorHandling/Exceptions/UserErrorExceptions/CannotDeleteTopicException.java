package Model.ErrorHandling.Exceptions.UserErrorExceptions;

public class CannotDeleteTopicException extends Exception {
    private final String MESSAGE = "Topic cannot be deleted from the bank; it's used by other entries!";
    String myTopic;
    public CannotDeleteTopicException(String topicName) {
        myTopic = topicName;
    }

    @Override
    public String toString() {
        return "TopicIsUsedException{" +
                "MESSAGE='" + MESSAGE + '\'' +
                ", myTopic='" + myTopic + '\'' +
                '}';
    }
}
