package com.bosch.cosmos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class SimpleListFragment extends Fragment
    implements View.OnClickListener {
  @Bind(R.id.unitList) RecyclerView rv;

  @Nullable @Override public View onCreateView(LayoutInflater inflater,
      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.frag_unit, container, false);
    ButterKnife.bind(this, rootView);
    initRecyclerView();
    return rootView;
  }

  private void initRecyclerView() {
    List<String> units = initUnitList();
    SimpleStringAdapter adapter = new SimpleStringAdapter(units, this);
    rv.setAdapter(adapter);
    rv.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
            false));
    adapter.notifyDataSetChanged();
  }

  private List<String> initUnitList() {
    List<String> units = new ArrayList<>();
    for (int i = 0; i < 40; i++) {
      units.add(String.format(Locale.getDefault(), getFormatString(), i));
    }
    return units;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  abstract String getFormatString();
}
