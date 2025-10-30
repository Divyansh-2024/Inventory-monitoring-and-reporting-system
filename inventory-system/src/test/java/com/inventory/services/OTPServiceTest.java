package com.inventory.services;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class OTPServiceTest {

    private final String testEmail = "test@example.com";

    @BeforeEach
    void clearStore() {
        OTPService.otpStore.clear();
    }

    @Test
    void testGenerateOTP_ShouldReturn6DigitNumber() {
        String otp = OTPService.generateOTP(testEmail);

        assertTrue(OTPService.otpStore.containsKey(testEmail));
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"));
    }

    @Test
    void testValidateOTP_ShouldReturnTrueForCorrectOTP() {
        String otp = OTPService.generateOTP(testEmail);

        boolean isValid = OTPService.validateOTP(testEmail, otp);
        assertTrue(isValid);
        assertFalse(OTPService.otpStore.containsKey(testEmail));
    }

    @Test
    void testValidateOTP_ShouldReturnFalseForIncorrectOTP() {
        OTPService.generateOTP(testEmail);

        boolean isValid = OTPService.validateOTP(testEmail, "123456");
        assertFalse(isValid);
        assertTrue(OTPService.otpStore.containsKey(testEmail));
    }

    @Test
    void testValidateOTP_ShouldReturnFalseIfNoOTPGenerated() {
        // No OTP generated for the email
        boolean isValid = OTPService.validateOTP(testEmail, "000000");

        assertFalse(isValid);
    }
}
