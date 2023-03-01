package org.example.broker.mqtt;

import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptPublishMessage;
import org.example.broker.utils.Logging;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;

public class PublisherListener extends AbstractInterceptHandler {

    private static final Logger LOG = Logging.getLogger();

    @Override
    public String getID() {
        return "PublishListener";
    }

    @Override
    public void onPublish(InterceptPublishMessage msg) {

        // Create array for payload
        int readableBytes = msg.getPayload().readableBytes();
        byte[] payload = new byte[readableBytes];

        // Read bytes from payload
        for (int i = 0; i < readableBytes; i++) {
            payload[i] = msg.getPayload().readByte();
        }

        // Create string from payload
        String decodedPayload = new String(payload, StandardCharsets.UTF_8);
        LOG.info("Received on topic: " + msg.getTopicName() + " / Content: " + decodedPayload);
    }
}