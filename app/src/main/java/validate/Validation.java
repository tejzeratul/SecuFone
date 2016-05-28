package validate;

/**
 * Created by Tejas on 5/18/2016.
 */
public class Validation {

    private boolean status=false;
    private String errorMessage="Error";

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
