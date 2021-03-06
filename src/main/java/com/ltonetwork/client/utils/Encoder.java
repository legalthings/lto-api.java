package com.ltonetwork.client.utils;

import com.ltonetwork.client.types.Encoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

public class Encoder {

    public static String base58Encode(String input, Charset charset) {
        return Base58.encode(input.getBytes(charset));
    }

    public static String base58Encode(String input) {
        return base58Encode(input, StandardCharsets.UTF_8);
    }

    public static String base58Encode(byte[] input) {
        return Base58.encode(input);
    }

    public static String base58Decode(String input, Charset charset) {
        return new String(Base58.decode(input), charset);
    }

    public static String base58Decode(byte[] input, Charset charset) {
        return new String(Base58.decode(new String(input, charset)));
    }

    public static byte[] base58Decode(byte[] input) {
        return Base58.decode(new String(input));
    }

    public static byte[] base58Decode(String input) {
        return Base58.decode(input);
    }

    public static String base64Encode(String input) {
        try {
            return new String(Base64.getEncoder().encode(input.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String base64Encode(byte[] input) {
        try {
            return new String(Base64.getEncoder().encode(input));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] base64Decode(String input) {
        return Base64.getDecoder().decode(input);
    }

    public static byte[] base64Decode(byte[] input) {
        return Base64.getDecoder().decode(input);
    }

    public static String base64Decode(String input, Charset charset) {
        return new String(Base64.getDecoder().decode(input), charset);
    }

    public static String base64Decode(byte[] input, Charset charset) {
        return new String(Base64.getDecoder().decode(input), charset);
    }

    public static String hexEncode(String input) {
        return Hex.encodeHexString(input.getBytes());
    }

    public static String hexEncode(byte[] input) {
        return Hex.encodeHexString(input);
    }

    public static String hexDecode(String input, Charset charset) {
        try {
            return new String(Hex.decodeHex(input));
        } catch (DecoderException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String hexDecode(byte[] input, Charset charset) {
        try {
            return new String(Hex.decodeHex(new String(input)));
        } catch (DecoderException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] hexDecode(String input) {
        try {
            return Hex.decodeHex(input);
        } catch (DecoderException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] hexDecode(byte[] input) {
        try {
            return Hex.decodeHex(new String(input));
        } catch (DecoderException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decode(String input, Encoding encoding) {
        String ret;
        switch (encoding) {
            case BASE58:
                ret = base58Decode(input, StandardCharsets.UTF_8);
                break;
            case BASE64:
                ret = base64Decode(input, StandardCharsets.UTF_8);
                break;
            case RAW:
                ret = input;
                break;
            case HEX:
                ret = hexDecode(input, StandardCharsets.UTF_8);
                break;
            default:
                ret = null;
        }

        return ret;
    }

    public static String encode(String input, Encoding encoding) {
        String ret;

        switch (encoding) {
            case BASE58:
                ret = base58Encode(input);
                break;
            case BASE64:
                ret = base64Encode(input);
                break;
            case RAW:
                ret = input;
                break;
            case HEX:
                ret = hexEncode(input);
                break;
            default:
                ret = null;
        }

        return ret;
    }

    public static boolean isBase58Encoded(String input) {
        return Pattern.matches("^[1-9A-HJ-NP-Za-km-z]+$", input);
    }

    public static boolean isBase64Encoded(String input) {
        return Pattern.matches("^[A-Za-z0-9+/]+={0,2}$", input);
    }

    static byte[] packN(int value) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(value).array();
        bytes = toPositiveByteArray(bytes);
        return bytes;
    }

    static int unpackN(byte[] value) {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.put(value);
        buf.flip();
        return buf.getInt();
    }

    // converts a byte[] like [0,0,19,-2] to [0,0,19,254]
    public static byte[] toPositiveByteArray(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] < 0 ? bytes[i] + 256 : bytes[i]);
        }
        return bytes;
    }
}
