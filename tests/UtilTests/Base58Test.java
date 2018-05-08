package UtilTests;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

import Util.AddressFormatException;
import Util.Base58;
import Util.AddressFormatException.InvalidCharacter;
import Util.AddressFormatException.InvalidChecksum;
import Util.AddressFormatException.InvalidDataLength;

public class Base58Test {

	public void testEncode() throws Exception {
        byte[] testbytes = "Hello World".getBytes();
        assertEquals("JxF12TrwUP45BMd", Base58.encode(testbytes));
        
        BigInteger bi = BigInteger.valueOf(3471844090L);
        assertEquals("16Ho7Hs", Base58.encode(bi.toByteArray()));
        
        byte[] zeroBytes1 = new byte[1];
        assertEquals("1", Base58.encode(zeroBytes1));
        
        byte[] zeroBytes7 = new byte[7];
        assertEquals("1111111", Base58.encode(zeroBytes7));

        // test empty encode
        assertEquals("", Base58.encode(new byte[0]));
    }

    @Test
    public void testEncodeChecked_address() throws Exception {
        String encoded = Base58.encodeChecked(111, new byte[20]);
        assertEquals("mfWxJ45yp2SFn7UciZyNpvDKrzbhyfKrY8", encoded);
    }

    @Test
    public void testEncodeChecked_privateKey() throws Exception {
        String encoded = Base58.encodeChecked(128, new byte[32]);
        assertEquals("5HpHagT65TZzG1PH3CSu63k8DbpvD8s5ip4nEB3kEsreAbuatmU", encoded);
    }

    @Test
    public void testDecode() throws Exception {
        byte[] testbytes = "Hello World".getBytes();
        byte[] actualbytes = Base58.decode("JxF12TrwUP45BMd");
        assertTrue(new String(actualbytes), Arrays.equals(testbytes, actualbytes));
        
        assertTrue("1", Arrays.equals(Base58.decode("1"), new byte[1]));
        assertTrue("1111", Arrays.equals(Base58.decode("1111"), new byte[4]));

        // Test decode of empty String.
        assertEquals(0, Base58.decode("").length);
    }

    @Test(expected = AddressFormatException.class)
    public void testDecode_invalidBase58() {
        Base58.decode("This isn't valid base58");
    }

    @Test
    public void testDecodeChecked() {
//        Base58.decodeChecked("4stwEBjT6FYyVV");

        // Now check we can correctly decode the case where the high bit of the first byte is not zero, so BigInteger
        // sign extends. Fix for a bug that stopped us parsing keys exported using sipas patch.
//        Base58.decodeChecked("93VYUMzRG9DdbRP72uQXjaWibbQwygnvaCu9DumcqDjGybD864T");
    }

    @Test(expected = AddressFormatException.InvalidCharacter.class)
    public void decode_invalidCharacter_notInAlphabet() {
        Base58.decodeChecked("J0F12TrwUP45BMd");
    }

    @Test(expected = AddressFormatException.InvalidChecksum.class)
    public void testDecodeChecked_invalidChecksum() {
        Base58.decodeChecked("4stwEBjT6FYyVW");
    }

    @Test(expected = AddressFormatException.InvalidDataLength.class)
    public void testDecodeChecked_shortInput() {
        Base58.decodeChecked("4s");
    }

    @Test
    public void testDecodeToBigInteger() {
        byte[] input = Base58.decode("129");
        assertEquals(new BigInteger(1, input), Base58.decodeToBigInteger("129"));
    }

}