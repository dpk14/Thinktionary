package RESTController.SerializableModels;

import org.springframework.http.ResponseEntity;

public class ResponsePair {
    ResponseEntity myResponseEntity;
    ErrorMessage myErrorMesage;

    public ResponsePair(ResponseEntity re, ErrorMessage em){
        myResponseEntity = re;
        myErrorMesage = em;
    }

    public ErrorMessage getMyErrorMesage() {
        return myErrorMesage;
    }

    public ResponseEntity getMyResponseEntity() {
        return myResponseEntity;
    }
}
