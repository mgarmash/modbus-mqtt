package me.legrange.wattnode.config;

/**
 * Modbus configuration
 *
 * @author gideon
 */
public class Modbus {


    /**
     * Get the value of serial
     *
     * @return the value of serial
     */
    public Serial getSerial() {
        return serial;
    }

    /**
     * Set the value of serial
     *
     * @param serial new value of serial
     */
    public void setSerial(Serial serial) {
        this.serial = serial;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(int pollInterval) {
        this.pollInterval = pollInterval;
    }

    
    void validate() throws ConfigurationException { 
        if (serial == null) throw new ConfigurationException("No serial configuration under modbus");
        serial.validate();
    }
    

    private Serial serial;
    private int deviceId = 1;
    private int pollInterval = 60;
}
