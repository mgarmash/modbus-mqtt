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

import java.util.logging.Level;
import java.util.logging.Logger;
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
public class MqttThread implements Runnable, MqttCallback {

    public MqttThread(String broker, Service service) {
        this.broker = broker;
        this.service = service;
    }

    @Override
    public void run() {
        try {
            running = true;
            mqtt = new MqttClient(broker, service.getName(), new MemoryPersistence());
            MqttConnectOptions opts = new MqttConnectOptions();
            opts.setCleanSession(true);
            mqtt.connect(opts);
            mqtt.setCallback(this);
        } catch (MqttException ex) {
            Logger.getLogger(MqttThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void connectionLost(Throwable e) {
        say("MQTT connection lost [%s]", e.getMessage());
        long time = 2500;
        int count = 0;
        while (!mqtt.isConnected()) {
            try {
                Thread.sleep(time);
                say("MQTT re-connecting");
                mqtt.connect();
                say("MQTT re-connected");
            } catch (InterruptedException ex) {
                Logger.getLogger(MqttThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MqttException ex) {
                say("MQTT reconnection error [%s]", ex.getMessage());
            }
        }
    }
    
    public void stop() { 
        running = false;
        if (mqtt.isConnected()) { 
            say("MQTT disconnected");
            try {
                mqtt.disconnect();
            } catch (MqttException ex) {
                Logger.getLogger(MqttThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void publish(String topic, String msg) {
        try {   
            mqtt.publish(topic, new MqttMessage(msg.getBytes()));
        } catch (MqttException ex) {
            Logger.getLogger(MqttThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
    }

    private void say(String fmt, Object... args) {
        service.say(fmt, args);
    }

    private boolean running;
    private MqttClient mqtt;
    private final String broker;
    private final Service service;

}
