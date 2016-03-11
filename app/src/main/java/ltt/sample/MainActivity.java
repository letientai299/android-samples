package ltt.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = ButterKnife.findById(this, R.id.fab);
    if (fab != null) {
      fab.setOnClickListener(
          view -> Snackbar.make(view, "Replace with your own action",
              Snackbar.LENGTH_LONG).setAction("Action", null).show());
    }
  }
}
