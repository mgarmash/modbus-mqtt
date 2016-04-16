package me.legrange.wattnode.config;

import static java.lang.String.format;

/**
 *
 * @author gideon
 */
public class Configuration {

    private Mqtt mqtt;

    public Mqtt getMqtt() {
        return mqtt;
    }

    public void setMqtt(Mqtt mqtt) {
        this.mqtt = mqtt;
    }
    
    

    private Modbus modbus;

    /**
     * Get the value of modbus
     *
     * @return the value of modbus
     */
    public Modbus getModbus() {
        return modbus;
    }

    /**
     * Set the value of modbus
     *
     * @param modbus new value of modbus
     */
    public void setModbus(Modbus modbus) {
        this.modbus = modbus;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(modbus)
                .append(mqtt)
                .toString();
    }
}
