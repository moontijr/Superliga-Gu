package com.example.myapplication;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.rule.ActivityTestRule;

import com.example.myapplication.activity.MainActivity;

@RunWith(AndroidJUnit4.class)
public class RegistrationInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testRegisterButtonOpensDialog() {
        // Klicken auf den Registrierungsbutton, um das Dialogfeld anzuzeigen
        onView(withId(R.id.registerAction)).perform(click());

        // Überprüfen, ob das Dialogfeld angezeigt wird
        onView(withId(R.id.popupDialog)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterWithValidCredentials() {
        // Klicken auf den Registrierungsbutton, um das Dialogfeld anzuzeigen
        onView(withId(R.id.registerAction)).perform(click());

        // Eingabe gültiger Benutzerdaten im Dialogfeld
        onView(withId(R.id.Username)).perform(typeText("ValidUser"));
        onView(withId(R.id.Password)).perform(typeText("ValidPassword123"));
        onView(withId(R.id.NumeFamilie)).perform(typeText("ValidFamilyName"));
        onView(withId(R.id.Prenume)).perform(typeText("ValidGivenName"));
        onView(withId(R.id.Email)).perform(typeText("valid@example.com"));

        // Klicken auf die Registrierungsschaltfläche im Dialogfeld
        onView(withId(R.id.registerAction)).perform(click());

        // Überprüfen, ob der Benutzer erfolgreich registriert wurde (Anzeige der Hauptaktivität)
        onView(withText("Bine ați venit!")).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterWithInvalidCredentials() {
        // Klicken auf den Registrierungsbutton, um das Dialogfeld anzuzeigen
        onView(withId(R.id.registerAction)).perform(click());

        // Eingabe ungültiger Benutzerdaten im Dialogfeld
        onView(withId(R.id.Username)).perform(typeText("Short")); // Ungültiger Benutzername
        onView(withId(R.id.Password)).perform(typeText("Short")); // Ungültiges Passwort
        onView(withId(R.id.Email)).perform(typeText("invalid")); // Ungültige E-Mail-Adresse

        // Klicken auf die Registrierungsschaltfläche im Dialogfeld
        onView(withId(R.id.registerAction)).perform(click());

        // Überprüfen, ob Fehlermeldungen angezeigt werden
        onView(withText("Username too short")).check(matches(isDisplayed()));
        onView(withText("Password too short")).check(matches(isDisplayed()));
        onView(withText("Enter a valid email address")).check(matches(isDisplayed()));
    }
}
