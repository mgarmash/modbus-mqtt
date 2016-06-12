package me.legrange.wattnode;

import me.legrange.wattnode.modbus.ModbusReader;
import me.legrange.wattnode.config.Configuration;
import me.legrange.wattnode.config.ConfigurationException;
import me.legrange.wattnode.config.Register;
import me.legrange.wattnode.modbus.ModbusListener;
import me.legrange.wattnode.modbus.ModbusReaderException;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class WattNodeService {

    public static void main(String[] args) throws Exception {
        WattNodeService s = new WattNodeService();
        if (args.length != 1) {
            System.out.println("Confiugration file name required");
            System.exit(-1);
        }
        try {
            s.configure(args[0]);
            s.start();
            s.run();
        }
        catch (ConfigurationException ex) {
            System.out.printf("Configuration error: %s\n", ex.getMessage());
        }
        catch (ModbusReaderException ex) {
            System.out.printf("Error connecting to Modbus: %s\n", ex.getMessage());
        }
    }

    /** 
     * Default private constructor
     */
    private WattNodeService() {
    }

    /** 
     * Configure the application. 
     * @param fileName The configuration file to parse.
     * @throws ConfigurationException Indicates there is a error in the configuration. 
     */
    private void configure(String fileName) throws ConfigurationException {
        this.config = Configuration.readConfiguration(fileName);
    }

    /** 
     * Start the service. 
     * @throws ServiceException 
     */
    private void start() throws ServiceException {
        running = true;
        startMqtt();
        startModbus();
        say("service started");
    }

    /** Connect to the MQTT broker */
    private void startMqtt() {
        mqtt = new MqttWriter(String.format("tcp://%s:%d", config.getMqtt().getBroker().getHost(), config.getMqtt().getBroker().getPort()), this);
        mqtt.start();
    }

    private void startModbus() throws ModbusReaderException {
        mbus = new ModbusReader(config.getModbus().getSerial().getPort(), config.getModbus().getSerial().getSpeed(), config.getModbus().getDeviceId());
        mbus.addListener(new ModbusListener() {

            @Override
            public void received(Register reg, int words[]) {
                double val = reg.decode(words);
                mqtt.publish(config.getMqtt().getTopic() + "/" + reg.getName(), Double.toString(val));
            }

            @Override
            public void error(Throwable e) {
                WattNodeService.this.error(e);
            }
        });
        mbus.setPollInterval(config.getModbus().getPollInterval());
        mbus.start();
       
    }

    private void run() {
        say("service running");
        for (Register reg : config.getRegisters()) {
            mbus.addRegister(reg);
            say("reg: " + reg.getName());
        }
        while (running) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
            }
        }
        say("service stopping");
    }

    void error(String fmt, Object... args) {
        say(fmt, args);
    }

    void error(Throwable ex) {
        ex.printStackTrace();
    }

    /**
     * talk to the user, log or whatever
     */
    void say(String fmt, Object... args) {
        String msg = String.format(getName() + " " + fmt, args);
        if (!msg.endsWith("\n")) {
            msg = msg + "\n";
        }
        System.out.print(msg);
    }

    String getName() {
        return "wattnode-mqtt";
    }

    private boolean running;
    private MqttWriter mqtt;
    private ModbusReader mbus;
    private Configuration config;
    
}