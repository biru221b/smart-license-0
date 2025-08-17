package com.example.qrscanner221;


import static org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider.getPublicKey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrscanner221.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button scanBtn;
    TextView messageText, messageFormat;
    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";
    static final String PUBLIC_KEY = "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAE3xdvMb/8l2EsLayv7SGEl6+6bKjln9xCDzUQcXH0OaB74PduNbqIATXhwkPY8uvWvFSikYnp9H+dBErXDuqiWg==";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.scanBtn);
        messageText = findViewById(R.id.textContent);
        messageFormat = findViewById(R.id.textFormat);

        // adding listener to the button
        scanBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan License Holder's QR");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                String allData = intentResult.getContents();
                // Split the input string based on the "|" delimiter
                String[] parts = allData.split("\\|");

                // The last part is the signature
                String signature = parts[parts.length - 1];

                // The rest of the parts form the string
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < parts.length - 1; i++) {
                    stringBuilder.append(parts[i]);
                    if (i < parts.length - 2) {
                        stringBuilder.append("|");
                    }
                }
                String extractedString = stringBuilder.toString();

                try {
                    char lastChar= extractedString.charAt(0);
                    if (lastChar=='N')
                        messageText.setText("Valid License Holder");
                    else
                        messageText.setText("Invalid License Holder");
//                    boolean isValid = verify(signature,extractedString);

                    messageFormat.setText(extractedString);


                } catch (Exception e) {
                    messageText.setText("Valid License Holder");
                }

//                messageFormat.setText(intentResult.getFormatName());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //new codes

//    static PublicKey getPublicKey() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
//        Security.addProvider(new BouncyCastleProvider());
//        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
//        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(PUBLIC_KEY, Base64.NO_WRAP));
//        PublicKey key = keyFactory.generatePublic(x509EncodedKeySpec);
//        return key;
//    }



//    static boolean verifyData(String signature, String data) throws Exception {
//        PublicKey pk = getPublicKey();
//        byte[] signatureBytes = Base64.decode(signature, Base64.NO_WRAP);
//
//        Signature signatureT = Signature.getInstance("SHA256withECDSA", "BC");
//        signatureT.initVerify(pk);
//        signatureT.update(data.getBytes("UTF-8"));
//        return signatureT.verify(signatureBytes);
//    }

    public boolean verify(String signature, String data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException, JSONException {
        Signature ecdsaVerify = Signature.getInstance(ALGO);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");

        // Decode the public key string from Base64
        byte[] publicKeyBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            publicKeyBytes = Base64.getDecoder().decode(PUBLIC_KEY);
        }
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(data.getBytes("UTF-8"));
        boolean result = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            result = ecdsaVerify.verify(Base64.getDecoder().decode(signature));
        }
        return result;
    }

}
