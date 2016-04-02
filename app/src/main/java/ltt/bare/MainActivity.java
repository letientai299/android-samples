package ltt.bare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.promegu.xlog.base.XLog;

public class MainActivity extends AppCompatActivity {

  public static final String CURRENT_PLAY_TIME = "CURRENT_PLAY_TIME";
  private static final int TIMEOUT_MILLIS = 5000;
  static final String IS_FIRST_TIME = "is_first_time";
  ObjectAnimator progressAnimator;
  @Bind(R.id.progressBar) ContentLoadingProgressBar progressBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
    setSupportActionBar(toolbar);

    if (savedInstanceState != null) // activity recreated
    {
      int currentProgress = savedInstanceState.getInt(CURRENT_PLAY_TIME, -1);
      if (currentProgress != -1) {
        startProgress(currentProgress);
      }
    }

    checkAndShowWelcome();
  }

  private void checkAndShowWelcome() {
    SharedPreferences preferences =
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    boolean isFirstTime = preferences.getBoolean(IS_FIRST_TIME, true);
    showWelcome(isFirstTime);
    preferences.edit().putBoolean(IS_FIRST_TIME, false).apply();
  }

  @XLog private void showWelcome(boolean isFirstTime) {
    if (isFirstTime) {
      Snackbar.make(findViewById(android.R.id.content),
          "Hello friend, welcome to my app", Snackbar.LENGTH_SHORT).show();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    return id == R.id.action_settings || super.onOptionsItemSelected(item);
  }

  /**
   * Start the timer for {@link #TIMEOUT_MILLIS}.
   *
   * The {@link #progressBar} will be update during timer running.
   */
  @OnClick(R.id.fab) void onFabClick() {
    startProgress(0);
  }

  @XLog private void startProgress(int initValue) {
    progressBar.setMax(TIMEOUT_MILLIS);
    progressBar.show();
    progressBar.setProgress(initValue);

    notify("Start the progress");
    progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", initValue,
        TIMEOUT_MILLIS);
    progressAnimator.setDuration(TIMEOUT_MILLIS - initValue);
    progressAnimator.setInterpolator(new LinearInterpolator());
    progressAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(final Animator animation) {
        progressBar.setProgress(0);
        MainActivity.this.notify("Done");
        progressBar.hide();
      }
    });

    progressAnimator.start();
  }

  @XLog private void notify(final String s) {
    Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_SHORT)
        .show();
  }

  @Override protected void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);
    if (progressAnimator != null) {
      int currentPlayTime = (int) progressAnimator.getAnimatedValue();
      outState.putInt(CURRENT_PLAY_TIME, currentPlayTime);
    }
  }

  @Override protected void onStop() {
    super.onStop();
    if (progressAnimator != null) progressAnimator.cancel();
  }
}

