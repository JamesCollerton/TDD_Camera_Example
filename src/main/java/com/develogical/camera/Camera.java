package com.develogical.camera;

public class Camera implements WriteListener {

  private boolean on;
  private boolean writeComplete;

  private Sensor sensor;
  private MemoryCard memoryCard;

  public Camera(boolean on, Sensor sensor, MemoryCard memoryCard) {
    this.sensor = sensor;
    this.memoryCard = memoryCard;
    this.writeComplete = true;
    if (on) {
      powerOn();
    }
  }

  public void pressShutter() {
    if (on) {
      byte[] data = sensor.readData();
      this.memoryCard.write(data);
      this.writeComplete = false;
    }
  }

  public void powerOn() {
    sensor.powerUp();
    this.on = true;
  }

  public void powerOff() {
    if (on && writeComplete) {
      sensor.powerDown();
    }
    this.on = false;
  }

  @Override
  public void writeComplete() {
    this.writeComplete = true;
  }
}
