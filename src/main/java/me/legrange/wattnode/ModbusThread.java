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

import me.legrange.modbus.ModbusException;
import me.legrange.modbus.ModbusListener;
import me.legrange.modbus.SerialModbus;
import me.legrange.modbus.tiny.SerialException;
import me.legrange.modbus.tiny.TinyMaster;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class ModbusThread implements Runnable, ModbusListener {

    public ModbusThread(String port, int speed, Service service) throws SerialException {
        this.port = port;
        this.speed = speed;
        this.service = service;
        try {
            initModbus();
        }
        catch (SerialException e) {
            throw new SerialException(e.getMessage(), e);
        }
    }
    
    @Override
    public void receive(int addr, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void error(Throwable e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        running = true;
        while (running) {
        }

    }

    public void queueRequest(int slaveId, int reg, int size) throws ModbusException {
        modbus.reqeust(slaveId, reg, size);
    }

    private void initModbus() throws SerialException {
        modbus = new TinyMaster(port, speed);
        modbus.addListener(this);
    }

    private final String port;
    private final int speed;
    private boolean running;
    private SerialModbus modbus;
    private final Service service;

}
