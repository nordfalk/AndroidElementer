package lekt01_views;


import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import dk.nordfalk.android.elementer.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(RobolectricTestRunner.class)
public class BenytKnapperDeklarativRobolectricTest {

  @Rule
  public ActivityTestRule<BenytKnapperDeklarativ> testRule = new ActivityTestRule(BenytKnapperDeklarativ.class);

  @Test
  public void trykKnapperTest() throws InterruptedException {
    ViewInteraction knap1 = onView(withId(R.id.knap1));
    ViewInteraction knap2 = onView(withId(R.id.knap2));
    ViewInteraction knap3 = onView(withId(R.id.knap3));

    // Det er ikke så smart at bruge Thread.sleep() da det får testen til at køre langsomt.
    // Se https://developer.android.com/training/testing/espresso/idling-resource for løsninger
    boolean langsomt = false;
    if (langsomt) Thread.sleep(1000);
    knap1.check(matches(withText("En knap")));
    knap2.check(matches(withText("En anden knap")));
    knap3.check(matches(withText("En tredje knap")));

    knap1.perform(click());
    if (langsomt) Thread.sleep(1000);
    knap1.check(matches(not(withText("En knap"))));
    knap2.check(matches(withText("En anden knap")));
    knap3.check(matches(withText("En tredje knap")));

    knap3.perform(click());
    if (langsomt) Thread.sleep(1000);
    knap1.check(matches(not(withText("En knap"))));
    knap2.check(matches(withSubstring("Hey, hvis der skal trykkes")));
    knap3.check(matches(withText("En tredje knap")));

    knap2.perform(click());

    BenytKnapperDeklarativ akt = testRule.getActivity();
    Log.d("AFPRØVNING", "Aktiviteten har på knap3 teksten: "+akt.knap3.getText());
  }
}
