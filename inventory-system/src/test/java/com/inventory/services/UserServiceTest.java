package com.inventory.services;

import com.inventory.dao.UserDAO;
import com.inventory.exceptions.InvalidCredentialsException;
import com.inventory.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDAO mockUserDao;

    @InjectMocks
    private UserService service; // Mockito will inject mockUserDao here

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // initializes @Mock & @InjectMocks
    }

    // ✅ TEST 1 — Register New User
    @Test
    void testRegister_NewUser_ShouldAddUser() throws Exception {
        when(mockUserDao.getUserByUsername("divyansh")).thenReturn(null);

        boolean result = service.register("divyansh", "1234", "admin", "div@example.com", false);

        assertTrue(result);
        verify(mockUserDao, times(1)).addUser(any(User.class));
    }

    // ✅ TEST 2 — Register Existing User
    @Test
    void testRegister_ExistingUser_ShouldReturnFalse() throws Exception {
        when(mockUserDao.getUserByUsername("divyansh"))
                .thenReturn(new User(1, "divyansh", "1234", "admin", "div@example.com", true));

        boolean result = service.register("divyansh", "1234", "admin", "div@example.com", false);

        assertFalse(result);
        verify(mockUserDao, never()).addUser(any());
    }

    // ✅ TEST 3 — Send OTP to Unverified User
    @Test
    void testSendVerificationOTP_ShouldGenerateAndSendOTP() throws SQLException {
        User user = new User(1, "div", "pass", "user", "div@gmail.com", false);
        when(mockUserDao.getUserByEmail("div@gmail.com")).thenReturn(user);

        try (MockedStatic<OTPService> mockOtp = mockStatic(OTPService.class);
             MockedStatic<EmailService> mockEmail = mockStatic(EmailService.class)) {

            mockOtp.when(() -> OTPService.generateOTP("div@gmail.com")).thenReturn("123456");

            service.sendVerificationOTP("div@gmail.com");

            mockOtp.verify(() -> OTPService.generateOTP("div@gmail.com"), times(1));
            mockEmail.verify(() -> EmailService.sendOTP("div@gmail.com", "123456"), times(1));
        }
    }

    // ✅ TEST 4 — Verify Email With Correct OTP
    @Test
    void testVerifyEmailWithOTP_ValidOTP_ShouldUpdateDatabase() throws SQLException {
        try (MockedStatic<OTPService> mockOtp = mockStatic(OTPService.class)) {
            mockOtp.when(() -> OTPService.validateOTP("div@gmail.com", "123456")).thenReturn(true);
            when(mockUserDao.verifyEmail("div@gmail.com")).thenReturn(true);

            boolean result = service.verifyEmailWithOTP("div@gmail.com", "123456");

            assertTrue(result);
            mockOtp.verify(() -> OTPService.validateOTP("div@gmail.com", "123456"), times(1));
            verify(mockUserDao, times(1)).verifyEmail("div@gmail.com");
        }
    }

    // ✅ TEST 5 — Verify Email With Wrong OTP
    @Test
    void testVerifyEmailWithOTP_InvalidOTP_ShouldReturnFalse() throws SQLException {
        try (MockedStatic<OTPService> mockOtp = mockStatic(OTPService.class)) {
            mockOtp.when(() -> OTPService.validateOTP("div@gmail.com", "111111")).thenReturn(false);

            boolean result = service.verifyEmailWithOTP("div@gmail.com", "111111");

            assertFalse(result);
            verify(mockUserDao, never()).verifyEmail(anyString());
        }
    }

    // ✅ TEST 6 — Login Successful
    @Test
    void testLogin_ValidCredentials_ShouldReturnUser() throws SQLException, InvalidCredentialsException {
        User mockUser = new User(1, "div", "1234", "admin", "div@example.com", true);
        when(mockUserDao.getUserByUsername("div")).thenReturn(mockUser);

        User result = service.login("div", "1234");

        assertNotNull(result);
        assertEquals("div", result.getUserName());
    }

    // ✅ TEST 7 — Login Invalid Password
    @Test
    void testLogin_InvalidPassword_ShouldReturnNull() throws SQLException, InvalidCredentialsException {
        User mockUser = new User(1, "div", "abcd", "user", "div@example.com", true);
        when(mockUserDao.getUserByUsername("div")).thenReturn(mockUser);

        User result = service.login("div", "wrongpass");

        assertNull(result);
    }

    // ✅ TEST 8 — Login User Not Found
    @Test
    void testLogin_UserNotFound_ShouldThrowException() throws SQLException {
        when(mockUserDao.getUserByUsername("div")).thenReturn(null);

        assertThrows(InvalidCredentialsException.class,
                () -> service.login("div", "1234"));
    }
}
