package cn.kio.cloud.boot.common.util;

import java.security.MessageDigest;

/**
 * 接口请求 校验和生成逻辑
 *
 * @author kio
 */
public class CheckSumUtils {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * CheckSum
     *
     * @param appSecret Keep the secret key used to generate checksum safe
     * @param nonce     Random number (maximum length 128 characters)
     * @param curTime   The number of seconds (of type String) of the current UTC timestamp from 0:00 0:00 on January 1, 1970, to the present (within 5 minutes before or after the moment the request is made)
     * @return
     */
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    /**
     * CheckSum
     *
     * @param algorithm MessageDigest type
     * @param appSecret Keep the secret key used to generate checksum safe
     * @param nonce     Random number (maximum length 128 characters)
     * @param curTime   The number of seconds (of type String) of the current UTC timestamp from 0:00 0:00 on January 1, 1970, to the present (within 5 minutes before or after the moment the request is made)
     * @return
     */
    public static String getCheckSum(String algorithm, String appSecret, String nonce, String curTime) {
        return encode(algorithm, appSecret + nonce + curTime);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (byte bt : bytes) {
            buf.append(HEX_DIGITS[(bt >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bt & 0x0f]);
        }
        return buf.toString();
    }
}
