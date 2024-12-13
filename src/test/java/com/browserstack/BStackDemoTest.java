package com.browserstack;

import browserstack.shaded.commons.codec.binary.Base32;
import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Hex;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

public class BStackDemoTest extends SeleniumTest {

    public String generateTOTP(String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
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

    @Test
    public void LoginIntoSystem() throws Exception {
        driver.get("https://authenticationtest.com/totpChallenge/");

        // Input username
        WebElement emailField = driver.findElement(By.xpath("//input[@id='email']"));
        emailField.sendKeys("totp@authenticationtest.com");

        // Input password
        WebElement passwordField = driver.findElement(By.xpath("//input[@id='password']"));
        passwordField.sendKeys("pa$$w0rd");

        // Generate the TOTP code
        String secretKey = "I65VU7K5ZQL7WB4E"; // Replace with your secret key
        String totpCode = generateTOTP(secretKey);

        // Input the TOTP code
        WebElement mfaField = driver.findElement(By.xpath("//input[@id='totpmfa']"));
        mfaField.sendKeys(totpCode);

        // Click the login button
        WebElement loginButton = driver.findElement(By.xpath("//input[@type='submit']"));
        loginButton.click();

        // Wait for the login success message
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Login Success']")));

        // Print the login message
        System.out.println("Login Message: " + loginMessage.getText());
    }
}
