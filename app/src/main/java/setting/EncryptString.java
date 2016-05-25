package setting;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Tejas on 4/20/2016.
 */
public class EncryptString {

    public String doEncryption(String data) {

        String output = null;
        byte[] digest = null;


        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes("UTF-8"));


            if (md != null) {
                digest = md.digest();

            }
            output = String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return output;

    }
}
