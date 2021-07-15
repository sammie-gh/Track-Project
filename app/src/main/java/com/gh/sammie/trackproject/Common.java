package com.gh.sammie.trackproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.gh.sammie.trackproject.model.User;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Common {

    public static User currentUser;

    private static byte[] encrypt(byte[] data, byte[] key, byte[] ivs) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            byte[] finalIvs = new byte[16];
            int len = ivs.length > 16 ? 16 : ivs.length;
            System.arraycopy(ivs, 0, finalIvs, 0, len);
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivps);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getEncryptedString(String value) {
        try {
            byte[] key = new byte[16];
            byte[] input = new byte[16];
            return Base64.encodeToString(encrypt(value.getBytes("UTF-8"), key, input), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String priceToString(Double price) {
        String toShow = priceWithoutDecimal(price);
        if (toShow.indexOf(".") > 0) {
            return priceWithDecimal(price);
        } else {
            return priceWithoutDecimal(price);
        }
    }

    private static String priceWithDecimal(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(price);
    }

    public static String priceWithoutDecimal(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        return formatter.format(price);
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }

        return false;
    }
}
