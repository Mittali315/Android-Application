package com.paril.mlaclientapp.ui.activity;
import android.content.Context;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;

public class Cryptography {

    private static final String ANDROID_KEY_STORE_NAME = "AndroidKeyStore";
    private static final String AES_MODE = "AES/GCM/NoPadding";

    private static final String KEY_ALIAS = "YOUR-KeyAliasForEncryption";

    private static final byte[] FIXED_IV = new byte[]{ 55, 54, 53, 52, 51, 50,
            49, 48, 47,
            46, 45, 44 };
    private static final String CHARSET_NAME = "UTF-8";

    private static final String LOG_TAG = Cryptography.class.getName();

    private final Context mContext;

    private final static Object s_keyInitLock = new Object();

    public Cryptography(Context context) {
        mContext = context;
    }


    private void initKeys() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException, InvalidAlgorithmParameterException, UnrecoverableEntryException, NoSuchPaddingException, InvalidKeyException {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME);
        keyStore.load(null);
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            initValidKeys();
        }
        else {
            try {
                    KeyStore.Entry keyEntry = keyStore.getEntry(KEY_ALIAS, null);
                   // String secretKey = getSecretKeyFromSharedPreferences();
            } catch (NullPointerException | UnrecoverableKeyException e) {
                Log.e(LOG_TAG, "Failed ", e);
            }
        }

    }

    private void initValidKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CertificateException, UnrecoverableEntryException, NoSuchPaddingException, KeyStoreException, InvalidKeyException, IOException {
       synchronized (s_keyInitLock) {
                generateKeys();
       }
    }


    protected void generateKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyGenerator keyGenerator;
        keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE_NAME);
        keyGenerator.init(

                new KeyGenParameterSpec.Builder(KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setRandomizedEncryptionRequired(false)
                        .build());
        keyGenerator.generateKey();

    }


    public String encryptData(String stringDataToEncrypt) {
        try {

            initKeys();
            Cipher cipher;

                cipher = Cipher.getInstance(AES_MODE);
                cipher.init(Cipher.ENCRYPT_MODE, getSecretKeyAPI(),
                        new GCMParameterSpec(128, FIXED_IV));
            byte[] encodedBytes = cipher.doFinal(stringDataToEncrypt.getBytes(CHARSET_NAME));
            String encryptedBase64Encoded = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
            return encryptedBase64Encoded;

        } catch (Exception e) {
            throw new RuntimeException("Unable to encrypt ", e);
        }
    }


    public String decryptData(String encryptedData) {
        try {

            initKeys();

            if (encryptedData == null) {
                throw new IllegalArgumentException("Data to be decrypted must be non null");
            }

            byte[] encryptedDecodedData = Base64.decode(encryptedData, Base64.DEFAULT);

            Cipher c;
            try {

                    c = Cipher.getInstance(AES_MODE);
                    c.init(Cipher.DECRYPT_MODE, getSecretKeyAPI(), new GCMParameterSpec(128, FIXED_IV));

            } catch (InvalidKeyException | IOException e) {


                throw e;
            }

            byte[] decodedBytes = c.doFinal(encryptedDecodedData);
            return new String(decodedBytes, CHARSET_NAME);

        } catch (Exception e) {
            throw new RuntimeException("Unable to generate keys", e);
        }
    }

    private Key getSecretKeyAPI() throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME);
        keyStore.load(null);
        return keyStore.getKey(KEY_ALIAS, null);

    }
}