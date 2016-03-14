package com.bosch.cosmos;

/**
 * Created by letientai299 on 3/15/16.
 */
public class Sensor {
  int id;
  double max;
  double min;
  double value;
  SensorType type;

  public Sensor(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public double getMax() {
    return max;
  }

  public void setMax(double max) {
    this.max = max;
  }

  public double getMin() {
    return min;
  }

  public void setMin(double min) {
    this.min = min;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public SensorType getType() {
    return type;
  }

  public void setType(SensorType type) {
    this.type = type;
  }

  enum SensorType {
    MOISTURE, TEMPERATURE;
  }
}
