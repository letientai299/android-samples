package ltt.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class LandingActivity extends AppCompatActivity {

  public static final float MAXIMIZE = 1.7f;
  private final static int TIMEOUT_MILLIS = 1500;
  private static final float ORIGINAL = 1f;

  @Bind(R.id.landingBrand) TextView brand;
  @Bind(R.id.landingProgress) ProgressBar progress;

  private Interpolator interpolator = new FastOutSlowInInterpolator();

  @Override public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_landing);
    ButterKnife.bind(this);
    startProgress(TIMEOUT_MILLIS);
    startBrandAnimation(brand, TIMEOUT_MILLIS);
  }

  private void startBrandAnimation(View view, int duration) {
    view.setScaleX(MAXIMIZE);
    view.setScaleY(MAXIMIZE);
    int delay = 600;
    AnimatorSet animationSet = new AnimatorSet();
    animationSet.setDuration(duration - delay).
        playTogether(
            ObjectAnimator.ofFloat(view, View.SCALE_X, MAXIMIZE, ORIGINAL),
            ObjectAnimator.ofFloat(view, View.SCALE_Y, MAXIMIZE, ORIGINAL));
    animationSet.setInterpolator(interpolator);
    animationSet.setStartDelay(delay);
    animationSet.start();
  }

  private void startProgress(final int timeout) {
    progress.setMax(timeout);
    progress.setProgress(0);
    ObjectAnimator animator =
        ObjectAnimator.ofInt(progress, "progress", 0, timeout);
    animator.setInterpolator(interpolator);
    animator.setDuration(timeout);
    animator.start();
    animator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(final Animator animation) {
        super.onAnimationEnd(animation);
        startActivity(LoginActivity.class);
      }
    });
  }

  private void startActivity(final Class<LoginActivity> cls) {
    startActivity(new Intent(this, cls));
    finish();
    overridePendingTransition(0, 0);
  }
}

