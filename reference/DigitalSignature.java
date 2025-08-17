/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.niraj.licenseecdsa;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.zxing.BarcodeFormat;  
import com.google.zxing.EncodeHintType;  
import com.google.zxing.MultiFormatWriter;  
import com.google.zxing.NotFoundException;  
import com.google.zxing.WriterException;  
import com.google.zxing.client.j2se.MatrixToImageWriter;  
import com.google.zxing.common.BitMatrix;  
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;  
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Niraj Khadka
 */
public class DigitalSignature {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";
    public void generateKeyPair() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException{
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        KeyPair keypair = g.generateKeyPair();
        publicKey = keypair.getPublic();
        privateKey = keypair.getPrivate(); 
        String pubkeyAsString = new BigInteger(publicKey.getEncoded()).toString(64);
        System.out.println("publickey"+pubkeyAsString);
        String prvkeyAsString = new BigInteger(privateKey.getEncoded()).toString(64);
        
    } 
    
    public JSONObject signMessage(String message) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException, JSONException, WriterException, IOException{
        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(message.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String sig = Base64.getEncoder().encodeToString(signature);
        System.out.println(sig);
        System.out.println(pub);

        JSONObject obj = new JSONObject();
        String qrText = message+"|"+sig;
        String path = "C:\\Users\\Niraj\\Pictures\\QR.png";  
        //Encoding charset to be used  
        String charset = "UTF-8";  
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();  
        //generates QR code with Low level(L) error correction capability  
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  
        generateQRcode(qrText, path, charset, hashMap, 400, 400);
        obj.put("message", message);
        obj.put("signature", sig);
        

        return obj; 
    }
    
    public boolean verify(JSONObject obj) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException, JSONException {

        Signature ecdsaVerify = Signature.getInstance(ALGO);
        KeyFactory kf = KeyFactory.getInstance("EC");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(obj.getString("message").getBytes("UTF-8"));
        boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(obj.getString("signature")));

        return result;
    }
    
    //static function that creates QR Code  
    public static void generateQRcode(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException  
    {  
    //the BitMatrix class represents the 2D matrix of bits  
    //MultiFormatWriter is a factory class that finds the appropriate Writer subclass for the BarcodeFormat requested and encodes the barcode with the supplied contents.  
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);  
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));  
    }  
    
    public static void main(String[] args) throws JSONException, WriterException, IOException{
        try {
            DigitalSignature digiSig = new DigitalSignature();
            digiSig.generateKeyPair();
            JSONObject obj = digiSig.signMessage("Niraj Khadka|Shankharapur-9, Kathmandu, Bagmati|1988-04-30|2063-08-12|2083-08-11|43237|A");
            //obj.put("message", "Biraj Khadka|Shankharapur-9, Kathmandu, Bagmati|1988-04-30|2063-08-12|2083-08-11|43237|A");
            boolean result = digiSig.verify(obj);
            System.out.println(result);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
