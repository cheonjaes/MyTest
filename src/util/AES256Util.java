package com.cbscap.util;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;

import org.apache.commons.codec.binary.Base64;

//import com.cbscap.util.aes256.AES256Util;
//고급 암호화 표준(AES, Advanced Encryption Standard)
//암호화와 복호화 과정에서 동일한 키를 사용하는 대칭 키 알고리즘
public class AES256Util {
 
 private static volatile AES256Util INSTANCE;
 
 
 final static String secretKey   = "12345678901234567890123456789012"; //32bit
 static String IV                = ""; //16bit
  
 public static AES256Util getInstance(){
     if(INSTANCE==null){
         synchronized(AES256Util.class){
             if(INSTANCE==null)
                 INSTANCE=new AES256Util();
         }
     }
     return INSTANCE;
 }
 
 private AES256Util(){
	 IV = secretKey.substring(0,16);
	 //IV = secretKey.substring(0,32);
}
 
 public static String Encode(String str)throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
	 	int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
	   // System.out.println(""+maxKeyLen);
	    AES256Util aes256 = AES256Util.getInstance();	 
	    String enStr = aes256.AES_Encode(str);
	    return enStr;
 }
 
 public static String Decode(String str)throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
	 	int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
	   // System.out.println(""+maxKeyLen);
	    AES256Util aes256 = AES256Util.getInstance();	 
	    String DeStr = aes256.AES_Decode(str);
	    return DeStr;
}
 
  //암호화
  public static String AES_Encode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
      byte[] keyData = secretKey.getBytes();
  
   SecretKey secureKey = new SecretKeySpec(keyData, "AES");
   //System.out.println("666666666666666 : "+IV.getBytes().length);
   
   Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
   //System.out.println("666666666666666 : "+secureKey);
   c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));
   //c.init(Cipher.ENCRYPT_MODE, secureKey);
   //c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(ivBytes));
   
   byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
   String enStr = new String(Base64.encodeBase64(encrypted));
   
   return enStr;
  }
 
  //복호화
  public static String AES_Decode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
    byte[] keyData = secretKey.getBytes();
    //System.out.println(new String(keyData) );
    SecretKey secureKey = new SecretKeySpec(keyData, "AES");
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
   
    byte[] byteStr = Base64.decodeBase64(str.getBytes());
   
    return new String(c.doFinal(byteStr),"UTF-8");
  }
  
  //키생서
  public static byte[] generationAES256_KEY() throws NoSuchAlgorithmException{
   KeyGenerator kgen = KeyGenerator.getInstance("AES");
   kgen.init(256);
   SecretKey key = kgen.generateKey();
   
   return key.getEncoded();
   
  }
}
