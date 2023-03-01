package org.example.broker.mqtt;

import io.moquette.broker.Server;
import io.moquette.broker.config.ClasspathResourceLoader;
import io.moquette.broker.config.IConfig;
import io.moquette.broker.config.IResourceLoader;
import io.moquette.broker.config.ResourceLoaderConfig;
import io.moquette.interception.InterceptHandler;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.example.broker.utils.Logging;
import org.example.broker.utils.Resources;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Broker {

    private static final Logger LOG = Logging.getLogger();

    private final Server mqttBroker;

    public Broker() {
        mqttBroker = new Server();
    }

    public void startServer() {

        // Load class path for configuration
        IResourceLoader classpathLoader = new ClasspathResourceLoader();
        final IConfig classPathConfig = new ResourceLoaderConfig(classpathLoader);

        // Start MQTT broker
        LOG.info("Start MQTT broker...");
        List<? extends InterceptHandler> userHandlers = Collections.singletonList(new PublisherListener());

        try {
            mqttBroker.startServer(classPathConfig, userHandlers);
        } catch (IOException e) {
            LOG.error("MQTT broker start failed...");
        }

        // Publishing topics
        LOG.info("Pushing topics...");

        List<String> lines = Resources.getLines("config/topics.conf");

        for(String line: lines) {
            pushTopic(line);
        }

        LOG.info("Topics pushed...");
    }

    public void stopServer() {
        mqttBroker.stopServer();
    }

    public void pushTopic(String topic) {

        LOG.info("Push topic: {}", topic);

        MqttPublishMessage message = MqttMessageBuilders.publish()
                .topicName(topic)
                .retained(true)
                .qos(MqttQoS.EXACTLY_ONCE)
                .payload(Unpooled.copiedBuffer("{}".getBytes(UTF_8)))
                .build();

        mqttBroker.internalPublish(message, "INTRLPUB");
    }
}