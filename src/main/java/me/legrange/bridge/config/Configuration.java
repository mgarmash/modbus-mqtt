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
import me.legrange.mqtt.service.config.MqttServiceConfig;
import me.legrange.yaml.app.config.annotation.NotEmpty;
import me.legrange.yaml.app.config.annotation.NotNull;

/**
 * Configuration generated from YAML config file.
 *
 * @author gideon
 */
public class Configuration extends MqttServiceConfig {

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

    public List<Slave> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<Slave> slaves) {
        this.slaves = slaves;
    }


    @NotNull
    private Modbus modbus;
    @NotEmpty
    private List<Slave> slaves;

}
