package lekt02_aktiviteter;


import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import dk.nordfalk.android.elementer.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class HovedmenuTest {

  @Rule
  public ActivityTestRule<Hovedmenu_akt> testRule = new ActivityTestRule<>(Hovedmenu_akt.class);

  @Test
  public void hovedmenuTest() throws InterruptedException {
    ViewInteraction knap1 = onView(withId(R.id.knap1));
    ViewInteraction knap2 = onView(withId(R.id.knap2));
    ViewInteraction knap3 = onView(withId(R.id.knap3));

    // Det er ikke så smart at bruge Thread.sleep() da det får testen til at køre langsomt.
    // Se https://developer.android.com/training/testing/espresso/idling-resource for løsninger
    boolean langsomt = true;
    if (langsomt) Thread.sleep(1000);

    knap1.perform(click());
    if (langsomt) Thread.sleep(1000);

    knap1.check(doesNotExist());
    if (langsomt) Thread.sleep(1000);

    Espresso.pressBack();
    if (langsomt) Thread.sleep(1000);

    knap2.check(matches(isDisplayed()));
    knap2.perform(click());
    if (langsomt) Thread.sleep(1000);

    onView(withText("En underskærm")).perform(click());
    if (langsomt) Thread.sleep(1000);

    Espresso.pressBack();
    Espresso.pressBack();
    if (langsomt) Thread.sleep(1000);

    knap3.perform(click());
    if (langsomt) Thread.sleep(1000);
/*
    ViewInteraction editText = onView(withText("Skriv et bogstav her."));
    editText.check(matches(isDisplayed()));
    editText.perform(replaceText("f"), closeSoftKeyboard());
    if (langsomt) Thread.sleep(1000);

    onView(withText("Gæt")).perform(click());
    if (langsomt) Thread.sleep(1000);
*/

    //Log.d("AFPRØVNING", "Aktiviteten har på knap3 teksten: "+akt.knap3.getText());
  }
}
