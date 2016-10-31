/*
 * Copyright 2016 gideon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.legrange.wattnode;

import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
class MqttConnector implements Runnable, MqttCallback {

    MqttConnector(String broker, WattNodeService service) {
        this.broker = broker;
        this.service = service;
    }

    @Override
    public void run() {
        try {
            mqtt = new MqttClient(broker, service.getName(), new MemoryPersistence());
            MqttConnectOptions opts = new MqttConnectOptions();
            opts.setCleanSession(true);
            mqtt.connect(opts);
            mqtt.setCallback(this);
        } catch (MqttException ex) {
            WattNodeService.error(ex.getMessage(), ex);
        }

    }

    @Override
    public void connectionLost(Throwable e) {
        WattNodeService.warn("MQTT connection lost [%s]", e.getMessage());
        long time = 2500;
        while (!mqtt.isConnected()) {
            try {
                TimeUnit.MILLISECONDS.sleep(time);
                WattNodeService.info("MQTT re-connecting");
                mqtt.connect();
                WattNodeService.info("MQTT re-connected");
            } catch (InterruptedException ex) {
                WattNodeService.error("MQTT interruption error: " + ex.getMessage(), ex);
            } catch (MqttException ex) {
                WattNodeService.error("MQTT reconnection error: " + ex.getMessage(), ex);
            }
        }
    }

    void start() {
        Thread t = new Thread(this, "MQTT updater");
        t.setDaemon(true);
        t.start();
    }

    void stop() {
        if (mqtt.isConnected()) {
            try {
                mqtt.disconnect();
            } catch (MqttException ex) {
                WattNodeService.error(ex.getMessage(), ex);
            } finally {
                WattNodeService.info("MQTT disconnected");
            }
        }
    }

    public void publish(String topic, String msg) {
        try {
            mqtt.publish(topic, new MqttMessage(msg.getBytes()));
        } catch (MqttException ex) {
            WattNodeService.error(ex.getMessage(), ex);
        }
    }
    
    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage mm) throws Exception {
    }

    private MqttClient mqtt;
    private final String broker;
    private final WattNodeService service;


}
