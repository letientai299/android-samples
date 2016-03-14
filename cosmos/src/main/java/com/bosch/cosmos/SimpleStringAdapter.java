package com.bosch.cosmos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.List;

/**
 * Created by letientai299 on 3/15/16.
 */
public class SimpleStringAdapter
    extends RecyclerView.Adapter<SimpleStringAdapter.ViewHolder> {
  private final List<String> strings;
  private View.OnClickListener clickListener;

  public SimpleStringAdapter(List<String> units,
      View.OnClickListener onClickListener) {
    this.clickListener = onClickListener;
    this.strings = units;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View inflate = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.view_simple, parent, false);
    inflate.setOnClickListener(clickListener);
    return new ViewHolder(inflate);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.name.setText(strings.get(position));
  }

  @Override public int getItemCount() {
    return strings.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.name) TextView name;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
