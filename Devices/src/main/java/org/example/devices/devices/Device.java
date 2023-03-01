package org.example.devices.devices;

import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient;

public interface Device {

    void sendData();
}
