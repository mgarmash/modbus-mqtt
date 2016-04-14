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
package me.legrange.wattnode;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.modbus.ModbusException;
import me.legrange.modbus.RequestFrame;
import me.legrange.modbus.ResponseFrame;
import me.legrange.modbus.SerialModbusPort;



/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class ModbusThread implements Runnable {

    public ModbusThread(String port, int speed, Service service) throws ServiceException {
        this.port = port;
        this.speed = speed;
        this.service = service;
        try {
            initModbus();
        }
        catch (ModbusException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
    

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                RequestFrame req = queue.take();
                ResponseFrame res = modbus.poll(req);
                if (!res.isError()) {
                }
            } catch (ModbusException ex) {
                Logger.getLogger(ModbusThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ModbusThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void queueRequest(int slaveId, int reg, int size) throws ModbusException {
       queue.add(new RequestFrame(slaveId, reg, size));
    }

    private void initModbus() throws ModbusException {
        modbus = SerialModbusPort.open(port, speed);
    }

    private final String port;
    private final int speed;
    private boolean running;
    private SerialModbusPort modbus;
    private final Service service;
    private final LinkedBlockingQueue<RequestFrame> queue = new LinkedBlockingQueue<>();


}
