package com.huanmengz.zhanjian2;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Crypt
{
    protected static final String MY_KEY="486513594438666233LJHMCZYWLLWC2333333";
    public static byte[] encryptAES(byte[] content, String password)throws Throwable {  
            
    /**
    Do the AES encrypt
    @parameter content things to encrypt.
    @parameter password key*/
            KeyGenerator kgen = KeyGenerator.getInstance("AES");  
            kgen.init(128, new SecureRandom(password.getBytes()));  
            SecretKey secretKey = kgen.generateKey();  
            byte[] enCodeFormat = secretKey.getEncoded();  
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content;  
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);  
            return result;
       }
    public static byte[] decryptAES(byte[] content, String password) throws Throwable{  
            KeyGenerator kgen = KeyGenerator.getInstance("AES");  
            kgen.init(128, new SecureRandom(password.getBytes()));  
            SecretKey secretKey = kgen.generateKey();  
            byte[] enCodeFormat = secretKey.getEncoded();  
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);  
            return result; 
       }  
    public static String SHA1Encrypt(byte[] bt)
    {
        //parameter br is a byte[] will be encrypted,
        MessageDigest md=null;
        String strDes=null;
        try
        {
            String encName="SHA-1";

            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest());  //to HexString
        }
        catch (NoSuchAlgorithmException e)
        {
            MainActivity.showERROR(e);
            return null;
        }
        return strDes;
    }
    public static String bytes2Hex(byte[]bts)
    {
        String des="";
        String tmp=null;
        for (int i=0;i < bts.length;i++)
        {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1)
            {
                des += "0";
            }
            des += tmp;
        }
        return des;
        }
}
