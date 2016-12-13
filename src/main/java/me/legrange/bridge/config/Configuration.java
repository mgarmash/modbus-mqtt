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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.yaml.snakeyaml.Yaml;

/**
 * Configuration generated from YAML config file.
 *
 * @author gideon
 */
public class Configuration {

    /**
     * Read the configuration file and return a configuration object.
     *
     * @param fileName The file to read.
     * @return The configuration object.
     * @throws ConfigurationException Thrown if there is a problem reading or
     * parsing the configuration.
     */
    public static Configuration readConfiguration(String fileName) throws ConfigurationException {
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(Paths.get(fileName))) {
            Configuration conf = yaml.loadAs(in, Configuration.class);
            conf.validate();
            return conf;
        } catch (IOException ex) {
            throw new ConfigurationException(String.format("Error reading configuraion file '%s': %s", fileName, ex.getMessage()), ex);
        }
    }

    public Mqtt getMqtt() {
        return mqtt;
    }

    public void setMqtt(Mqtt mqtt) {
        this.mqtt = mqtt;
    }

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

    void validate() throws ConfigurationException {
        if (modbus == null) {
            throw new ConfigurationException("Modbus not configured");
        }
        modbus.validate();
        if (mqtt == null) {
            throw new ConfigurationException("Mqtt not configured");

        }
        mqtt.validate();
        if (slaves == null) {
            throw new ConfigurationException("Slaves not defined");
        }
        for (Slave slave : slaves) {
            slave.validate();
        } 
    }
    
    private Mqtt mqtt;
    private Modbus modbus;
    private List<Slave> slaves;

}
