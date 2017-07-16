package bola.encrypted_message_app.crypt;


public class Permutation {
    public String encryptedData(String data,String key)
    {
        String encryptedText = "";
        while(data.length()!=0)
        {
            String block = null;
            block=data.substring(0,key.length());
            data=data.substring(key.length());
            encryptedText+=encrypt(block,key);
        }
        return encryptedText;
    }

    private String encrypt(String data, String key)
    {
        int keyValue = 0;
        String cipherText = "";
        for(int i=0;i<data.length();i++)
        {
            keyValue=Integer.parseInt(String.valueOf(key.charAt(i)));
            cipherText+=data.charAt(keyValue-1);
        }
        return cipherText;
    }

}