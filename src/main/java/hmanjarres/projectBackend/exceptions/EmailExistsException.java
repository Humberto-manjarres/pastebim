package hmanjarres.projectBackend.exceptions;

public class EmailExistsException extends RuntimeException{

    public EmailExistsException(String message){
        /**super() significa que llamamos constructor de la clase que heredamos*/
        super(message);
    }
}
