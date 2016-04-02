package ltt.bare;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ProgressBar;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class) public class MainActivityTest
    extends ActivityInstrumentationTestCase2<MainActivity> {

  @Rule public ActivityTestRule<MainActivity> rule;

  public MainActivityTest() {
    super(MainActivity.class);
    rule = new ActivityTestRule<>(MainActivity.class);
  }

  @Before public void setUp() throws Exception {
    super.setUp();
    injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    Context context = getInstrumentation().getTargetContext();
    PreferenceManager.getDefaultSharedPreferences(context)
        .edit()
        .clear()
        .apply();
  }

  @After public void tearDown() throws Exception {
    System.out.println("Clean up resource");
    SystemClock.sleep(1000);
    super.tearDown();
  }

  @Test public void showHello() {
    String hello = rule.getActivity().getString(R.string.hello);
    onView(withText(hello)).check(matches(isDisplayed()));
  }

  @Test public void resumeProgressOnRotate() {
    // Click the fab button
    onView(withId(R.id.fab)).perform(click());
    final int DELAY_TIME = 1000;
    SystemClock.sleep(DELAY_TIME);

    rotateScreen();

    // Now check the progress, it should be greeter than DELAY_TIME
    onView(withId(R.id.progressBar)).check((view, noViewFoundException) -> {
      if (view instanceof ProgressBar) {
        ProgressBar progressBar = (ProgressBar) view;
        int currentProgress = progressBar.getProgress();
        // This s not the right way to test this case, of course.
        if (currentProgress < DELAY_TIME) {
          System.out.println("Expected to larger than: " + DELAY_TIME);
          System.out.println("But actual value is is: " + currentProgress);
          Assert.fail();
        }
      }
    });
  }

  private void rotateScreen() {
    Activity activity = rule.getActivity();
    Context context = activity.getApplicationContext();
    int orientation = context.getResources().getConfiguration().orientation;
    int nextOrientation = Configuration.ORIENTATION_LANDSCAPE == orientation
        ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    activity.setRequestedOrientation(nextOrientation);
  }

  @Test public void testFirstTime() {
    // This is for searching the snackbar
    onView(allOf(withId(android.support.design.R.id.snackbar_text),
        withText(containsString("welcome")))).check(matches(isDisplayed()));
  }

  @Test public void testNotFirstTime() {
    onView(withText(containsString("welcome"))).check(doesNotExist());
  }
}
