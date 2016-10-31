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

package me.legrange.wattnode.config;

/**
 * MQTT Configuration.
 * @author gideon
 */
public class Mqtt {

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
  
    void validate() throws ConfigurationException { 
        if (broker == null) throw new ConfigurationException("MQTT broker not configured");
        broker.validate();
        if ((topic == null) || topic.equals("")) throw new ConfigurationException("MQTT topic for publishing data not configured");
    }

    private Broker broker;
    private String topic; 
}
