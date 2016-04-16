package me.legrange.wattnode.config;

import static java.text.MessageFormat.format;

/**
 *
 * @author gideon
 */
public class Modbus {
    
    private int serialSpeed;

    /**
     * Get the value of serialSpeed
     *
     * @return the value of serialSpeed
     */
    public int getSerialSpeed() {
        return serialSpeed;
    }

    /**
     * Set the value of serialSpeed
     *
     * @param serialSpeed new value of serialSpeed
     */
    public void setSerialSpeed(int serialSpeed) {
        this.serialSpeed = serialSpeed;
    }

    private String serialPort;

    /**
     * Get the value of serialPort
     *
     * @return the value of serialPort
     */
    public String getSerialPort() {
        return serialPort;
    }

    /**
     * Set the value of serialPort
     *
     * @param serialPort new value of serialPort
     */
    public void setSerialPort(String serialPort) {
        this.serialPort = serialPort;
    }
    @Override
    public String toString() {
        return new StringBuilder()
                .append(format("Serial port: %s", serialPort))
                .append(format("Serial speed: %d", serialSpeed))
                .toString();
    }

}
