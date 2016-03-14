package com.bosch.cosmos;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DetailFragment extends Fragment
    implements TableDataClickListener<Sensor> {
  @Bind(R.id.sensorTable) SortableTableView<Sensor> sensorTableView;

  @Nullable @Override

  public View onCreateView(LayoutInflater inflater,
      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.frag_detail, container, false);
    ButterKnife.bind(this, rootView);

    List<Sensor> sensors = fetchSensorData();

    initSensorTable(sensors);
    return rootView;
  }

  private void initSensorTable(List<Sensor> sensors) {
    int numColumn = 5;
    sensorTableView.setColumnCount(numColumn);
    sensorTableView.setDataAdapter(new SensorTableDataAdapter(sensors));

    String[] headers = { "ID", "Min", "Value", "Max", "Type" };
    SimpleTableHeaderAdapter headerAdapter =
        new SimpleTableHeaderAdapter(getContext(), headers);
    headerAdapter.setPaddings(5, 10, 10, 5);
    sensorTableView.setHeaderAdapter(headerAdapter);
    sensorTableView.setHeaderElevation(10);
    sensorTableView.setHeaderSortStateViewProvider(
        SortStateViewProviders.brightArrows());

    sensorTableView.setColumnWeight(0, 1);
    sensorTableView.setColumnWeight(1, 1);
    sensorTableView.setColumnWeight(2, 1);
    sensorTableView.setColumnWeight(3, 1);
    sensorTableView.setColumnWeight(4, 2);

    sensorTableView.setColumnComparator(0,
        (lhs, rhs) -> ((Integer) lhs.getId()).compareTo(rhs.getId()));

    sensorTableView.setColumnComparator(1,
        (l, r) -> ((Double) l.getMin()).compareTo(r.getMin()));
    sensorTableView.setColumnComparator(2,
        (l, r) -> ((Double) l.getValue()).compareTo(r.getValue()));
    sensorTableView.setColumnComparator(3,
        (l, r) -> ((Double) l.getMax()).compareTo(r.getMax()));
    sensorTableView.setColumnComparator(4,
        (l, r) -> l.getType().compareTo(r.getType()));

    sensorTableView.addDataClickListener(this);
  }

  private List<Sensor> fetchSensorData() {
    List<Sensor> sensors = new ArrayList<>();
    Random random = new Random(SystemClock.currentThreadTimeMillis());
    int numGenerate = random.nextInt(20) + 15;
    for (int i = 0; i < numGenerate; i++) {
      Sensor sensor = new Sensor(i);
      sensor.setMin(random.nextDouble() * 100);
      sensor.setMax(random.nextDouble() * 100 + sensor.getMin());
      sensor.setValue(random.nextDouble() * (sensor.getMax() - sensor.getMin())
          + sensor.getMin()
          + random.nextGaussian() * 20);
      sensor.setType(
          ((random.nextInt(100) % 2) == 1 ? Sensor.SensorType.MOISTURE
              : Sensor.SensorType.TEMPERATURE));
      sensors.add(sensor);
    }
    return sensors;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onDataClicked(int rowIndex, Sensor clickedData) {

  }

  private class SensorTableDataAdapter extends TableDataAdapter<Sensor> {
    private final List<Sensor> sensors;

    public SensorTableDataAdapter(List<Sensor> sensors) {
      super(DetailFragment.this.getContext(), sensors);
      this.sensors = sensors;
    }

    @Override public View getCellView(int rowIndex, int columnIndex,
        ViewGroup parentView) {
      Sensor sensor = sensors.get(rowIndex);
      TextView textView = (TextView) LayoutInflater.from(getContext())
          .inflate(R.layout.view_simple_textview, parentView, false);
      textView.setPadding(20, 10, 20, 10);
      String doubleFormat = "%.3f";
      switch (columnIndex) {
        case 0:
          textView.setText(String.valueOf(sensor.getId()));
          break;
        case 1:
          textView.setText(String.format(doubleFormat, sensor.getMin()));
          break;
        case 2:
          textView.setText(String.format(doubleFormat, sensor.getValue()));
          textView.setBackgroundColor(pickColor(sensor));
          break;
        case 3:
          textView.setText(String.format(doubleFormat, sensor.getMax()));
          break;
        case 4:
          textView.setText(String.valueOf(sensor.getType()));
          break;
        default:
          break;
      }
      return textView;
    }

    private int pickColor(Sensor sensor) {
      int colorId;
      double alertRange = 5.0;
      if (isInAlertRange(sensor.getValue(), sensor.getMin(), alertRange)
          || isInAlertRange(sensor.getValue(), sensor.getMax(), alertRange)) {
        colorId = R.color.colorAlert;
      } else {
        if (sensor.getValue() > (sensor.getMin() + alertRange)
            && (sensor.getValue() < sensor.getMax() - alertRange)) {
          colorId = R.color.colorNormal;
        } else {
          colorId = R.color.colorDanger;
        }
      }
      return ContextCompat.getColor(getContext(), colorId);
    }

    private boolean isInAlertRange(double value, double offset,
        double alertRange) {
      return (offset - alertRange <= value) && (offset + alertRange) >= value;
    }
  }
}
