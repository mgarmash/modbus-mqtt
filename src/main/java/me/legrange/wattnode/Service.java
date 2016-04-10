package me.legrange.wattnode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.legrange.modbus.tiny.SerialException;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class Service {
    
    public static void main(String[] args) throws Exception {
        Service s = new Service();
        s.start();
        s.waitForEnd();
        
    }
    
    private Service() {
        
    }
    
    private void start() throws SerialException { 
        running = true;
        startMqtt();
        startModbus();
        say("service started");
    }
    
    private void startMqtt() {
        mqtt = new MqttThread(broker, this);
        pool.submit(mqtt);
    } 
    
    private void startModbus() throws SerialException {
        mbus = new ModbusThread(port, speed, this);
        pool.submit(mbus);
    }
    
    private void waitForEnd() {
        say("service running");
        while (running) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
        say("service stopping");
    }
    
    void error(String fmt, Object...args) {
        say(fmt, args);
    }
    
    void error(Throwable ex) {
        ex.printStackTrace();
    }
    
    /** talk to the user, log or whatever */
    void say(String fmt, Object...args) { 
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
    private MqttThread mqtt;
    private ModbusThread mbus;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final String broker = "tcp://192.168.1.5:1883";
    private final String port = "/dev/ttyUSB0";
    private final int speed = 9600;
    
}
