package models;

/**
 * Created by Tejas on 5/18/2016.
 */
public class ValidationBean {

    private boolean status=false;
    private String errorMessage="";

    public boolean isStatus() {
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
