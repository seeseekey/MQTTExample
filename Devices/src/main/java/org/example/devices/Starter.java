package org.example.devices;

import org.example.devices.devices.Device;
import org.example.devices.devices.Multisensor;
import org.example.devices.devices.Switch;
import org.example.devices.utils.Logging;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Starter {

    private static final Logger LOG = Logging.getLogger();
    public static void main(String[] args) throws InterruptedException {

        LOG.info("Init Dummy device simulator...");

        // Create list of Dummy devices
        List<Device> devices = new ArrayList<>();

        devices.add(new Multisensor("multisensor/bewegung", "multisensor/temperatur"));
        devices.add(new Switch("schalter1/status"));
        devices.add(new Switch("schalter2/status"));
        devices.add(new Switch("schalter3/status"));

        while(true) {

            LOG.info("Send dummy data...");

            for(Device device: devices) {
                device.sendData();
            }

            // Sleep 15 seconds
            Thread.sleep(15000);
        }
    }
}
