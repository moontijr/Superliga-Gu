package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    /***
     * Teste pentru Lab02 - UVSS
     * Check UVSS/Lab02/Lab02_BBT_TCs_Form for detailed ECPs and BVAs
     */

    public boolean isRegistrationValid(String username, String password) {
        //simulation function

        /*** user pressed the register button

         -dialog popup

         -user enter their first name, last name, email, username & password

         -user presses "submit" */

        return isUsernameValid(username) && isPasswordValid(password);
    }

    private boolean isUsernameValid(String username) {
        return username.length() >= 5 && username.length() <= 25 && !username.contains(" ");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 25 && !password.contains(" ");
    }

    @Test
    public void testValidUsernameAndPassword() {
        //username: 6 - gultig, password: 6 - gultig
        assertTrue(isRegistrationValid("VladP7", "123456"));
    }

    @Test
    public void testUsernameTooShort() {
        //username: 4 - nicht gultig, password: 6 - gultig
        assertFalse(isRegistrationValid("vlad", "123456"));
    }

    @Test
    public void testPasswordTooShort() {
        //username: 25 - gultig, password: 5 - nicht gultig
        assertFalse(isRegistrationValid("pascavladeugen20021017cjn", "12345"));
    }

    @Test
    public void testUsernameTooLong() {
        //username: 26 - nicht gultig, password: 6 - gultig
        assertFalse(isRegistrationValid("pascavladeugen20021017cjnp", "123456"));
    }

    @Test
    public void testPasswordTooLong() {
        //username: 25 - gultig, password: 26 - nicht gultig
        assertFalse(isRegistrationValid("pascavladeugen20021017cjn", "12345678901234567890123456"));
    }

    @Test
    public void testInvalidUsernameAndPassword() {
        //username: 26 - nicht gultig, password: 26 - nicht gultig
        assertFalse(isRegistrationValid("pascavladeugen20021017cjnp", "12345678901234567890123456"));
    }


}