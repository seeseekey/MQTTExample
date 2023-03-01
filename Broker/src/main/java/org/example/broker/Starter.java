package org.example.broker;

import org.example.broker.utils.Logging;
import org.example.broker.mqtt.Broker;
import org.slf4j.Logger;

public class Starter {

    private static final Logger LOG = Logging.getLogger();

    public static void main(String[] args) {

        // Init and start broker
        LOG.info("Init broker...");

        Broker broker = new Broker();
        broker.startServer();

        // Bind a shutdown hook
        LOG.info("Bind shutdown hook...");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Stopping broker...");
            broker.stopServer();
        }));
    }
}
