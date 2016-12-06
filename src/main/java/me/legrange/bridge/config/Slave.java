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
package me.legrange.bridge.config;

import java.util.List;

/**
 *
 * @author gideon
 */
public class Slave {

    private String name;
    private int deviceId = 1;
    private int pollInterval = 60;
    private boolean zeroBased = false;
    private List<Register> registers; 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isZeroBased() {
        return zeroBased;
    }

    public void setZeroBased(boolean zeroBased) {
        this.zeroBased = zeroBased;
    }

    public List<Register> getRegisters() {
        return registers;
    }

    public void setRegisters(List<Register> registers) {
        this.registers = registers;
    }
    

}
