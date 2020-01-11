package RESTController.SerializableModels;

public class ErrorMessage {
    String myMessage;
    String myStackTrace;

    public ErrorMessage(String message, String stackTrace){
        myMessage = message;
        myStackTrace = stackTrace;
    }

    public String getMyMessage() {
        return myMessage;
    }

    public String getMyStackTrace() {
        return myStackTrace;
    }
}
