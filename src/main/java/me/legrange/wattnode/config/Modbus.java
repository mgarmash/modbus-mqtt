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
