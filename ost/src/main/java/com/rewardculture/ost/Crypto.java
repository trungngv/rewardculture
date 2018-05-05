package com.rewardculture.ost;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    public static final String ALGORITHM = "RawBytes";
    public static final String HMAC_SHA_256 = "HmacSHA256";
    public static final String UTF_8 = "UTF-8";

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String signToHex(String secretKey, String s) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA_256);
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(UTF_8), ALGORITHM);
            mac.init(key);
            return bytesToHex(mac.doFinal(s.getBytes(UTF_8)));
        } catch (Exception e) {
            return null;
        }
    }

}
