package me.legrange.wattnode.config;

import static java.lang.String.format;

/**
 *
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
        if (topic == null) throw new ConfigurationException("MQTT topic for publishing data not configured");
    }

    private Broker broker;
    private String topic; 
}
