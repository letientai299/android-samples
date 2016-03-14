package com.bosch.cosmos;

import android.view.View;
import com.bosch.cosmos.utils.RxBus;

public class UnitFragment extends SimpleListFragment {
  @Override public void onClick(View v) {
    int childAdapterPosition = rv.getChildAdapterPosition(v);
    RxBus.getBus().send(new UnitClickEvent(childAdapterPosition));
  }

  @Override String getFormatString() {
    return "Unit %d";
  }

  class UnitClickEvent {
    private final int unitId;

    UnitClickEvent(int id) {
      this.unitId = id;
    }

    public int getUnitId() {
      return unitId;
    }
  }
}
