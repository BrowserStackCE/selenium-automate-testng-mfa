package com.browserstack;

import browserstack.shaded.commons.codec.binary.Base32;
import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Hex;

public class TOTPGenerator {
    public static String generateTOTP(String secretKey) {
        try
        {
            // Decode the Base32 encoded secret key
            Base32 base32 = new Base32();
            byte[] bytes = base32.decode(secretKey);
            String hexKey = Hex.encodeHexString(bytes);

            // Generate the TOTP code using the hex key
            return TOTP.getOTP(hexKey);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error generating TOTP", e);
        }
    }

    public static void main(String[] args) {
        String secretKey = "I65VU7K5ZQL7WB4E"; // Replace with your secret key
        String code = generateTOTP(secretKey);
        System.out.println("Generated TOTP: " + code);
    }
}
