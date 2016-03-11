package ltt.sample;

import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

  @Override public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    animateInput(500);
  }

  private void animateInput(final int duration) {
    View input = ButterKnife.findById(this, R.id.input);
    input.setAlpha(0);
    input.setScaleY(2);
    input.setTranslationY(100);
    input.animate()
        .scaleY(1f)
        .alpha(1f)
        .translationY(0)
        .setStartDelay(100)
        .setDuration(duration)
        .setInterpolator(new LinearOutSlowInInterpolator());
  }
}
