package RESTController.SerializableModels;

import org.springframework.http.ResponseEntity;

public class ResponsePair<T> {
    T myReturnObject;
    ErrorMessage myErrorMesage;

    public ResponsePair(ErrorMessage em, T returnObject){
        myErrorMesage = em;
        myReturnObject = returnObject;
    }

    public ErrorMessage getMyErrorMesage() {
        return myErrorMesage;
    }

    public T getMyReturnObject() {
        return myReturnObject;
    }
}
