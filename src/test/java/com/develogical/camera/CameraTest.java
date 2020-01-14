package com.develogical.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

  @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

  private Sensor sensor = context.mock(Sensor.class);
  private MemoryCard memoryCard = context.mock(MemoryCard.class);

  @Test
  public void switchingTheCameraOnPowersUpTheSensor() {
    Camera camera = cameraFactory(false);
    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerUp();
          }
        });
    camera.powerOn();
  }

  @Test
  public void switchingTheCameraOffPowersDownTheSensor() {
    Camera camera = cameraFactory(true);
    context.checking(
        new Expectations() {
          {
            exactly(1).of(sensor).powerDown();
          }
        });
    camera.powerOff();
  }

  @Test
  public void pressShutterWhenPowerOffDoesNothing() {
    Camera camera = cameraFactory(false);
    context.checking(
        new Expectations() {
          {
            never(sensor);
            never(memoryCard);
          }
        });
    camera.pressShutter();
  }

  @Test
  public void pressShutterWhenPowerOnSavesToMemory() {
    Camera camera = cameraFactory(true);
    final byte[] data = new byte[] {};
    context.checking(
        new Expectations() {
          {
            oneOf(sensor).readData();
            will(returnValue(data));

            exactly(1).of(memoryCard).write(data);
          }
        });
    camera.pressShutter();
  }

  @Test
  public void ifDataCurrentlyBeingWrittenTurnOffDoesNotPowerDownSensor() {
    Camera camera = cameraFactory(true);
    final byte[] data = new byte[] {};
    context.checking(
        new Expectations() {
          {
            oneOf(sensor).readData();
            will(returnValue(data));

            exactly(1).of(memoryCard).write(data);
          }
        });
    camera.pressShutter();

    context.checking(
        new Expectations() {
          {
            never(sensor).powerDown();
          }
        });
    camera.powerOff();
  }

  private Camera cameraFactory(boolean on) {
    if (on) {
      context.checking(
          new Expectations() {
            {
              exactly(1).of(sensor).powerUp();
            }
          });
    }
    return new Camera(on, sensor, memoryCard);
  }
}
