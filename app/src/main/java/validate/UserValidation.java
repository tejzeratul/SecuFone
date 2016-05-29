package validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tejas on 5/27/2016.
 */
public class UserValidation {

    public Validation isEmailValid(String email) {

        Validation validationResult = new Validation();

        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);
        boolean result = matcher.matches();

        if (result) {
            validationResult.setStatus(true);
            validationResult.setErrorMessage("");
        } else {
            validationResult.setStatus(false);
            validationResult.setErrorMessage("Email Invalid");
        }

        return validationResult;
    }

    public Validation isPasswordValid(String password1, String password2) {

        Validation validationResult = new Validation();

        if (!password1.equals(password2)) {
            validationResult.setStatus(false);
            validationResult.setErrorMessage("Passwords does not match");
        } else {
            validationResult = isPasswordValid(password1);
        }

        return validationResult;
    }

    public Validation isPasswordValid(String password) {


        // TODO: Use unicode regex
        Validation validationResult = new Validation();

        String regexAlphaNumeric = "^.*(?=.*\\d)(?=.*[a-zA-Z]).*$";
        String regexSpaces = "\\w*\\s+\\w*";

        Pattern pattern = Pattern.compile(regexAlphaNumeric);

        Matcher matcher = pattern.matcher(password);
        boolean result = matcher.matches();

        if (password.length() < 5) {
            validationResult.setStatus(false);
            validationResult.setErrorMessage("Password too short");
        } else {
            if (!result) {
                validationResult.setStatus(false);
                validationResult.setErrorMessage("Password should be alphanumeric");
            } else {
                if (password.contains("?")) {
                    validationResult.setStatus(false);
                    validationResult.setErrorMessage("? cannot be part of password");
                } else {
                    if (password.matches(regexSpaces)) {
                        validationResult.setStatus(false);
                        validationResult.setErrorMessage("spaces cannot be part of password");

                    } else {
                        validationResult.setStatus(true);
                        validationResult.setErrorMessage("");
                    }
                }
            }
        }
        return validationResult;
    }


    public Validation isNameValid(String name) {

        Validation validationResult = new Validation();

        if (name.length() < 3) {
            validationResult.setStatus(false);
            validationResult.setErrorMessage("Name too short");
        } else {
            validationResult.setStatus(true);
            validationResult.setErrorMessage("");
        }

        return validationResult;

    }

    private Validation isGeoValid(String country, String city, String state) {

        //TODO: Replace this with your own logic
        Validation validationResult = new Validation();

        validationResult.setStatus(true);
        validationResult.setErrorMessage("");

        return validationResult;
    }


}
