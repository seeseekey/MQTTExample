package org.example.devices.devices;

import com.google.gson.Gson;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import org.example.devices.models.Motion;
import org.example.devices.models.Temperature;
import org.example.devices.utils.RandomNumberGenerator;

import java.util.UUID;

public class Multisensor implements Device {

    private Mqtt3BlockingClient client;

    private final String motionTopic;

    private final String temperatureTopic;

    private String getMotionPayload() {

        boolean randomMotionValue = RandomNumberGenerator.getRandomBoolean();
        Motion motion = new Motion(randomMotionValue);
        return new Gson().toJson(motion);
    }

    private String getTemperaturePayload() {

        int randomTemperatureValue = RandomNumberGenerator.getRandomNumberFromRange(-40, 40);
        Temperature temperature = new Temperature(randomTemperatureValue, "°C");
        return new Gson().toJson(temperature);
    }

    public Multisensor(String motionTopic, String temperatureTopic) {
        this.motionTopic = motionTopic;
        this.temperatureTopic = temperatureTopic;
    }

    @Override
    public void sendData() {

        if (client == null) {
            // Create MQTT client
            client = Mqtt3Client.builder()
                    .identifier(UUID.randomUUID().toString())
                    .serverHost("localhost")
                    .buildBlocking();

            client.connect();
        }

        client.publishWith()
                .topic(motionTopic)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(getMotionPayload().getBytes())
                .send();

        client.publishWith()
                .topic(temperatureTopic)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(getTemperaturePayload().getBytes())
                .send();
    }
}
