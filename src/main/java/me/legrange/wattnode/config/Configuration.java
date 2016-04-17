package me.legrange.wattnode.config;

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

    public List<Register> getRegisters() {
        return registers;
    }

    public void setRegisters(List<Register> registers) {
        this.registers = registers;
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
        if (registers == null) {
            throw new ConfigurationException("Registers not defined");
        }
        for (Register reg : registers) {
            reg.validate();
        }
    }
    private Mqtt mqtt;
    private Modbus modbus;
    private List<Register> registers;

}
