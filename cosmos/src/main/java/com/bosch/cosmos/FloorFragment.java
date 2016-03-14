package com.bosch.cosmos;

import android.view.View;
import com.bosch.cosmos.utils.RxBus;

public class FloorFragment extends SimpleListFragment {
  @Override String getFormatString() {
    return "Floor %d";
  }

  @Override public void onClick(View v) {
    RxBus.getBus().send(new FloorClickEvent(rv.getChildAdapterPosition(v)));
  }

  class FloorClickEvent {
    private int floorId;

    public FloorClickEvent(int floorId) {
      this.floorId = floorId;
    }

    public int getFloorId() {
      return floorId;
    }
  }
}
