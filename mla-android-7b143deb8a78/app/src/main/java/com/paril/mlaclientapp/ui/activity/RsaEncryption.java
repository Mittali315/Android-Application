package com.paril.mlaclientapp.ui.activity;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;

public class RsaEncryption {

    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String RSA_KEY_ALIAS = "SharedPreferenceEncryption";
    //private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    private static final String KEY_TYPE = "RSA";

    public static PublicKey loadpairKeys(Context ctx, String RSA_KEY_ALIAS) {
        try {
            KeyStore keyStore;

            keyStore = KeyStore.getInstance(AndroidKeyStore);
            keyStore.load(null);

            if (!keyStore.containsAlias(RSA_KEY_ALIAS)) {

                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 30);

                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(ctx)
                        .setAlias(RSA_KEY_ALIAS)
                        .setSubject(new X500Principal("CN=" + RSA_KEY_ALIAS))
                        .setSerialNumber(BigInteger.TEN)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();

                KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_TYPE, AndroidKeyStore);
                kpg.initialize(spec);
                KeyPair keypair = kpg.generateKeyPair();
                return keypair.getPublic();
            }

            KeyStore.PrivateKeyEntry rsaKeys = (KeyStore.PrivateKeyEntry) keyStore.getEntry(RSA_KEY_ALIAS, null);
            return rsaKeys.getCertificate().getPublicKey();

        } catch (Exception e) {
            throw new RuntimeException("Unable to generate keys", e);
        }
    }

    public static String getRandomKey(){
        try{
            byte[] aesKey = new byte[16];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(aesKey);
            byte[] generated = secureRandom.generateSeed(12);
            String gp = Base64.encodeToString(generated, Base64.DEFAULT);

            return gp;
        }catch(Exception e){
            e.printStackTrace();
            return "not generated";
        }
    }


    public static String getSessionkey(){
        try{
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();
            byte[] encoded = secretKey.getEncoded();
            String sess = Base64.encodeToString(encoded, Base64.DEFAULT);
            return sess;
        }catch(Exception e){
            e.printStackTrace();
            return "not generated";
        }
    }

    public static PrivateKey retrievePrivateKey() {
        try
        {
            KeyStore keyStore = KeyStore.getInstance(AndroidKeyStore);
            keyStore.load(null);
            if(keyStore.containsAlias(RSA_KEY_ALIAS)) {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(RSA_KEY_ALIAS, null);
                return privateKeyEntry.getPrivateKey();
            }
            return null;
        }
        catch(KeyStoreException | IOException |NoSuchAlgorithmException | UnrecoverableEntryException | CertificateException e)
        {
            throw new RuntimeException("Unable to get pk", e);
        }
    }

    public static String generateDigitalsignature(String Msg) {
        String digitalSignature = " ";
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            PrivateKey pk = RsaEncryption.retrievePrivateKey();

            if(pk != null) {
                sign.initSign(pk);
                byte[] Msgbytes = Msg.getBytes();
                sign.update(Msgbytes);
                byte[] signature = sign.sign();

                digitalSignature = new String(Base64.encode(signature, 0));
            }
            return digitalSignature;

        } catch(NoSuchAlgorithmException | InvalidKeyException | SignatureException e)
        {
            throw new RuntimeException("Error ", e) ;
        }
    }

        }