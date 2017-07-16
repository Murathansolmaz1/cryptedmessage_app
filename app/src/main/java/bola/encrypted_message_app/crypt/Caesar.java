package bola.encrypted_message_app.crypt;

public class Caesar {
    static String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890+*=%_!@#$/&()-':;.,?<€>|{}[]^~”“é";

    public String encryption(String plainText,int shift){
        String cipherText = "";
        for (int i=0;i<plainText.length();i++){
            if(plainText.charAt(i)==' '){
                cipherText += " ";
                continue;
            }
            if (alphabet.indexOf(plainText.charAt(i))<=alphabet.length()-shift-1)
                cipherText += alphabet.charAt(alphabet.indexOf(plainText.charAt(i))+shift);
            else
                cipherText += alphabet.charAt(alphabet.indexOf(plainText.charAt(i))+shift-alphabet.length());
        }
        return cipherText;
    }
}