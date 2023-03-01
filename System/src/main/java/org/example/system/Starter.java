package org.example.system;

import com.google.gson.Gson;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import org.example.system.models.Lamp;
import org.example.system.models.Motion;
import org.example.system.models.Switch;
import org.example.system.utils.Logging;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Starter {

    private static final Logger LOG = Logging.getLogger();

    private static Mqtt3BlockingClient client;

    public static void main(String[] args) throws InterruptedException {

        LOG.info("Init smart home system...");

        // Create client
        client = Mqtt3Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost("localhost")
                .buildBlocking();

        // Connect to MQTT server
        client.connect();

        // Subscribe to topics
        client.toAsync().subscribeWith()
                .topicFilter("schalter1/status")
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback(Starter::switchMessageReceived)
                .send();

        client.toAsync().subscribeWith()
                .topicFilter("schalter2/status")
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback(Starter::switchMessageReceived)
                .send();

        client.toAsync().subscribeWith()
                .topicFilter("schalter3/status")
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback(Starter::switchMessageReceived)
                .send();

        client.toAsync().subscribeWith()
                .topicFilter("multisensor/bewegung")
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback(Starter::multisensorMotionMessageReceived)
                .send();
    }

    private static String getPayloadAsString(ByteBuffer buffer) {

        byte[] payload = new byte[buffer.remaining()];
        buffer.get(payload);
        return new String(payload, StandardCharsets.UTF_8);
    }

    private static String getLampPayload(boolean lampEnabled) {

        Lamp lamp = new Lamp(lampEnabled);
        return new Gson().toJson(lamp);
    }

    private static void switchMessageReceived(Mqtt3Publish mqtt3Publish) {

        LOG.info("Receive message: {}", mqtt3Publish);

        String payload = getPayloadAsString(mqtt3Publish.getPayload().get());
        LOG.info("Payload: {}", payload);

        if (payload.length() <= 2) {// Ignore empty JSONs
            return;
        }

        Switch switchStatus = new Gson().fromJson(payload, Switch.class);

        // Define link between switch and lamp
        String targetTopic;

        switch (mqtt3Publish.getTopic().toString()) {
            case "schalter1/status" -> {
                targetTopic = "bad/deckenlampe";
            }
            case "schalter2/status" -> {
                targetTopic = "kueche/deckenlampe";
            }
            case "schalter3/status" -> {
                targetTopic = "wohnzimmer/deckenlampe";
            }
            default -> {
                LOG.info("Ignore unknown topic...");
                return;
            }
        }

        client.publishWith()
                .topic(targetTopic)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(getLampPayload(switchStatus.enabled).getBytes())
                .send();
    }

    private static void multisensorMotionMessageReceived(Mqtt3Publish mqtt3Publish) {

        LOG.info("Receive message: {}", mqtt3Publish);

        String payload = getPayloadAsString(mqtt3Publish.getPayload().get());
        LOG.info("Payload: {}", payload);

        if (payload.length() <= 2) { // Ignore empty JSONs, from publishing topic
            return;
        }

        Motion motion = new Gson().fromJson(payload, Motion.class);

        client.publishWith()
                .topic("bad/deckenlampe")
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(getLampPayload(motion.motion).getBytes())
                .send();
    }
}
