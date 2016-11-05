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
package me.legrange.wattnode.modbus;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.legrange.modbus.ModbusError;
import me.legrange.modbus.ModbusException;
import me.legrange.modbus.ReadInputRegisters;
import me.legrange.modbus.ResponseFrame;
import me.legrange.modbus.SerialModbusPort;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class ModbusReader implements Runnable {

    public ModbusReader(String port, int speed, int deviceId, boolean zeroBased) throws ModbusReaderException {
        this.deviceId = deviceId;
        this.zeroBased = zeroBased;
        try {
            initModbus(port, speed);
        } catch (ModbusException e) {
            throw new ModbusReaderException(e.getMessage(), e);
        }
    }

    public void setPollInterval(int interval) {
        this.pollInterval = interval * 1000;
    }

    public void addRegister(ModbusRegister reg) {
        registers.add(reg);
        polls = null;
    }

    public void addListener(ModbusListener listener) {
        listeners.add(listener);
    }

    public void start() {
        Thread t = new Thread(this, "Modbus poller");
        t.setDaemon(true);
        t.start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            long start = System.currentTimeMillis();
            List<Poll> polls = getPolls();
            for (Poll poll : polls) {
                try {
                    int addr = (zeroBased ? poll.getAddress() - 1 : poll.getAddress());
                    ReadInputRegisters req = new ReadInputRegisters(deviceId, addr, poll.getSize());
                    ResponseFrame res = modbus.poll(req);
                    for (ModbusListener l : listeners) {
                        if (!res.isError()) {
                            poll.applyBytes(res.getBytes(), l);
                        } else {
                            l.error(new ModbusReaderException(String.format("Modbus error: %s", ModbusError.valueOf(res.getFunction()))));
                        }
                    }

                } catch (ModbusException ex) {
                    for (ModbusListener l : listeners) {
                        l.error(new ModbusReaderException(ex.getMessage(), ex));
                    }
                }
            }
            long stop = System.currentTimeMillis();
            try {
                TimeUnit.MILLISECONDS.sleep(pollInterval - (stop - start));
            } catch (InterruptedException ex) {
            }
        }

    }

    private List<Poll> getPolls() {
        if (polls == null) {
            polls = Poll.generatePolls(registers);
        }
        return polls;
    }

    private void initModbus(String port, int speed) throws ModbusException {
        modbus = SerialModbusPort.open(port, speed);
    }

    private boolean running;
    private long pollInterval = 60000;
    private int deviceId;
    private SerialModbusPort modbus;
    private final List<ModbusListener> listeners = new LinkedList<>();
    private final List<ModbusRegister> registers = new LinkedList<>();
    private List<Poll> polls = null;
    private final boolean zeroBased;
}
