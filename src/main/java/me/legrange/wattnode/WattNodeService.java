package me.legrange.wattnode;

import java.util.Arrays;
import java.util.Date;
import me.legrange.wattnode.modbus.ModbusReader;
import me.legrange.wattnode.config.Configuration;
import me.legrange.wattnode.config.ConfigurationException;
import me.legrange.wattnode.config.Register;
import me.legrange.wattnode.modbus.ModbusListener;

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
        s.configure(args[0]);
        s.start();
        s.run();

    }

    private WattNodeService() {

    }

    private void configure(String fileName) throws ConfigurationException {
        this.config = Configuration.readConfiguration(fileName);
    }

    private void start() throws ServiceException {
        running = true;
        startMqtt();
        startModbus();
        say("service started");
    }

    private void startMqtt() {
        mqtt = new MqttWriter(String.format("tcp://%s:%d", config.getMqtt().getBroker().getHost(), config.getMqtt().getBroker().getPort()), this);
        mqtt.start();
    }

    private void startModbus() throws ServiceException {
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