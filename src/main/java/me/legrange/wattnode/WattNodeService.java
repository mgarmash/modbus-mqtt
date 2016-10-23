package me.legrange.wattnode;

import java.util.logging.Level;
import java.util.logging.Logger;
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
        } catch (ConfigurationException ex) {
            error("Configuration error: " + ex.getMessage(), ex);
        } catch (ModbusReaderException ex) {
            error("Error connecting to Modbus: " + ex.getMessage(), ex);
        }
    }

    /**
     * Default private constructor
     */
    private WattNodeService() {
    }

    /**
     * Configure the application.
     *
     * @param fileName The configuration file to parse.
     * @throws ConfigurationException Indicates there is a error in the
     * configuration.
     */
    private void configure(String fileName) throws ConfigurationException {
        this.config = Configuration.readConfiguration(fileName);
    }

    /**
     * Start the service.
     *
     * @throws ServiceException
     */
    private void start() throws ServiceException {
        
        running = true;
        startMqtt();
        startModbus();
        info("service started");
    }

    /**
     * Connect to the MQTT broker
     */
    private void startMqtt() {
        mqtt = new MqttWriter(String.format("tcp://%s:%d", config.getMqtt().getBroker().getHost(), config.getMqtt().getBroker().getPort()), this);
        mqtt.start();
    }

    private void startModbus() throws ModbusReaderException {
        mbus = new ModbusReader(config.getModbus().getSerial().getPort(), config.getModbus().getSerial().getSpeed(), config.getModbus().getDeviceId());
        mbus.addListener(new ModbusListener() {

            @Override
            public void received(Register reg, byte bytes[]) {
                double val = reg.decode(bytes);
                mqtt.publish(config.getMqtt().getTopic() + "/" + reg.getName(), Double.toString(val));
            }

            @Override
            public void error(Throwable e) {
                WattNodeService.error(e.getMessage(), e);
            }
        });
        mbus.setPollInterval(config.getModbus().getPollInterval());
        mbus.start();

    }

    private void run() {
        info("service running");
        for (Register reg : config.getRegisters()) {
            mbus.addRegister(reg);
            debug("reg: " + reg.getName());
        }
        while (running) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
            }
        }
        info("service stopping");
    }

    static void debug(String fmt, Object... args) {
        logger.finest(String.format(fmt, args));
    }

    static void info(String fmt, Object... args) {
        logger.info(String.format(fmt, args));
    }

    static void warn(String fmt, Object... args) {
        logger.warning(String.format(fmt, args));
    }

    static void error(String msg, Throwable ex) {
        logger.log(Level.SEVERE, msg, ex);
    }

    String getName() {
        return "wattnode-mqtt";
    }

    private boolean running;
    private MqttWriter mqtt;
    private ModbusReader mbus;
    private Configuration config;
    private static final Logger logger = Logger.getLogger(WattNodeService.class.getName());

}
