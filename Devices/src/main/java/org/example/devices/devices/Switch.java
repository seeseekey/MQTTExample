package org.example.devices.devices;

import com.google.gson.Gson;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import org.example.devices.utils.RandomNumberGenerator;

import java.util.UUID;

public class Switch implements Device {

    private Mqtt3BlockingClient client;

    private final String topic;

    private String getSwitchPayload() {

        boolean randomSwitchStatus = RandomNumberGenerator.getRandomBoolean();
        return getSwitchPayload(randomSwitchStatus);
    }

    private String getSwitchPayload(boolean payload) {

        org.example.devices.models.Switch switchValue = new org.example.devices.models.Switch(payload);
        return new Gson().toJson(switchValue);
    }

    public Switch(String topic) {
        this.topic = topic;
    }

    @Override
    public void sendData() {

        if (client == null) {
            // Create MQTT client
            client = Mqtt3Client.builder()
                    .identifier(UUID.randomUUID().toString())
                    .serverHost("localhost")

                    // Last will
                    .willPublish()
                    .topic(topic)
                    .payload(getSwitchPayload(false).getBytes())
                    .applyWillPublish()

                    .buildBlocking();

            client.connect();
        }

        client.publishWith()
                .topic(topic)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(getSwitchPayload().getBytes())
                .send();
    }
}
