package com.bosch.cosmos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bosch.cosmos.utils.RxBus;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import rx.Subscription;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DashboardActivity extends AppCompatActivity {
  @Bind(R.id.pager) ViewPager pager;

  ActionBar toolbar;
  Subscription subscription;
  int currentUnitId = 0;
  int currentFloorId = 0;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
    ButterKnife.bind(this);
    toolbar = getSupportActionBar();
    assert toolbar != null;

    toolbar.setTitle("Dashboard");
    List<Fragment> fragments = initFragments();
    initPager(fragments);
  }

  @Override protected void onStart() {
    super.onStart();
    subscription = subscribeEvent(RxBus.getBus());
  }

  @Override protected void onStop() {
    super.onStop();
    subscription.unsubscribe();
  }

  private Subscription subscribeEvent(RxBus bus) {
    return bus.toObserverable().subscribe(this::handleEvent);
  }

  private void handleEvent(Object v) {
    if (v instanceof UnitFragment.UnitClickEvent) {
      int unitId = ((UnitFragment.UnitClickEvent) v).getUnitId();
      toolbar.setTitle("Unit " + unitId);
      currentUnitId = unitId;
      pager.setCurrentItem(1);
    } else if (v instanceof FloorFragment.FloorClickEvent) {
      int floorId = ((FloorFragment.FloorClickEvent) v).getFloorId();
      String title =
          String.format(Locale.getDefault(), "Unit %d/Floor %d", currentUnitId,
              floorId);
      currentFloorId = floorId;
      toolbar.setTitle(title);
      pager.setCurrentItem(2);
    }
  }

  private void initPager(final List<Fragment> fragments) {
    FragmentPagerAdapter adapter =
        new FragmentPagerAdapter(getSupportFragmentManager()) {
          @Override public Fragment getItem(int position) {
            return fragments.get(position);
          }

          @Override public int getCount() {
            return fragments.size();
          }
        };
    pager.setAdapter(adapter);
    adapter.notifyDataSetChanged();
    pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override public void onPageSelected(int position) {
        super.onPageSelected(position);
        switch (position) {
          case 0:
            toolbar.setTitle("Dashboard");
            break;
          case 1:
            toolbar.setTitle("Unit " + currentUnitId);
            break;
          case 2:
            toolbar.setTitle(
                String.format(Locale.getDefault(), "Unit " + "%d/Floor %d",
                    currentUnitId, currentFloorId));
            break;
          default:
            break;
        }
      }
    });
  }

  private List<Fragment> initFragments() {
    List<Fragment> fragments = new ArrayList<>();
    fragments.add(new UnitFragment());
    fragments.add(new FloorFragment());
    fragments.add(new DetailFragment());
    return fragments;
  }
}
