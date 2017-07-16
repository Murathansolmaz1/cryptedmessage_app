package bola.encrypted_message_app.crypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Crypt {
    bola.encrypted_message_app.crypt.Permutation permutationObject = new bola.encrypted_message_app.crypt.Permutation();
    bola.encrypted_message_app.crypt.Caesar caesarObject = new bola.encrypted_message_app.crypt.Caesar();

    public String encrypt(String data) {
        String permutationKey = "743682519";
        String encryptedText = "";

        try {
            if (data.length()!=0) {
                String unSpacedplainText = data.replaceAll("\\s+","€");
                Random rand = new Random();
                int n = rand.nextInt(9) + 1;
                int m = rand.nextInt(9) + 1;
                unSpacedplainText = n + unSpacedplainText + m;
                int mod = (permutationKey.length() - (unSpacedplainText.length() % permutationKey.length())) % permutationKey.length();

                while (mod!=0) {
                    unSpacedplainText += "'";
                    mod -= 1;
                }
                String encryptedText1 = permutationObject.encryptedData(unSpacedplainText,permutationKey);
                encryptedText1 = encryptedText1.toLowerCase();
                encryptedText = caesarObject.encryption(encryptedText1, 12);
            }
        } catch (Exception e) {}

        return encryptedText;
    }

    public String decrypt(String data) {
        String permutationDeKey = "863274159";
        String decryptedText = "";
        try {
            decryptedText = caesarObject.encryption(data,caesarObject.alphabet.length()-12);
            decryptedText = permutationObject.encryptedData(decryptedText, permutationDeKey);
            decryptedText = decryptedText.replace("€"," ");
            decryptedText = decryptedText.replace("'","");
            decryptedText.trim();
            if (tryParseInt(decryptedText.substring(0,1))&& tryParseInt(decryptedText.substring(decryptedText.length()-1))) {
                decryptedText = decryptedText.substring(1);
                decryptedText = decryptedText.substring(0,decryptedText.length()-1);
            } else
                decryptedText = "";
        } catch(Exception e){
            decryptedText = "";
        }
        return decryptedText;
    }

    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    public String md5Coder (String textToHash) {
        String generatedPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(textToHash.getBytes());

            StringBuilder sb = new StringBuilder();
            for (int i = 0;i<bytes.length;i++)
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100,16).substring(1));

            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {}
        return generatedPassword;
    }

}
