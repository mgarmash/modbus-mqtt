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
package me.legrange.bridge;

import me.legrange.bridge.modbus.ModbusReader;
import me.legrange.bridge.config.Configuration;
import me.legrange.bridge.config.Register;
import me.legrange.bridge.config.Slave;
import me.legrange.bridge.modbus.ModbusListener;
import me.legrange.bridge.modbus.ModbusReaderException;
import me.legrange.bridge.modbus.ModbusRegister;
import me.legrange.modbus.SerialException;
import me.legrange.modbus.SerialModbusPort;
import me.legrange.mqtt.service.MqttService;
import me.legrange.simple.app.SimpleApplicationException;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class ModbusMqttService extends MqttService<Configuration> {

    public static void main(String...args) {
        MqttService.main(args);
    }

    @Override
    public String getName() {
        return "modbus-mqtt";
    }


    /**
     * Start the service.
     *
     * @throws ServiceException
     */
    @Override
    public void start() throws SimpleApplicationException {
        super.start();
        try {
            startModbus();
        } catch (ModbusReaderException | SerialException ex) {
            error(ex.getMessage(),ex);
        }
    }

    @Override
    public void stop() {
        try {
            stopModbus();
        } catch (SerialException ex) {
            error(ex.getMessage(),ex);
        }
        super.stop();
    }


    private void startModbus() throws ModbusReaderException, SerialException {
        Configuration config = getConfiguration();
        port = SerialModbusPort.open(config.getModbus().getSerial().getPort(),
                config.getModbus().getSerial().getSpeed());
        for (Slave slave : config.getSlaves()) {
            ModbusReader mbus = new ModbusReader(port, slave.getName(),
                    slave.getDeviceId(),
                    slave.isZeroBased());
            mbus.addListener(new ModbusListener() {

                @Override
                public void received(ModbusRegister reg, byte bytes[]) {
                    double val = ModbusRegister.decode(reg, bytes);
                    publish(config.getMqtt().getPublishTopic()+ "/" + slave.getName() + "/" + reg.getName(), Double.toString(val));
                }

                @Override
                public void error(Throwable e) {
                    ModbusMqttService.this.error(e.getMessage(), e);
                }
            });
            for (Register reg : slave.getRegisters()) {
                mbus.addRegister(makeRegister(reg));
                debug("reg: " + reg.getName());
            }
            mbus.setPollInterval(slave.getPollInterval());
            mbus.start();
        }

    }

    private void stopModbus() throws SerialException {
        port.close();
    }

    private ModbusRegister makeRegister(final Register reg) {
        return new ModbusRegister() {
            @Override
            public String getName() {
                return reg.getName();
            }

            @Override
            public int getAddress() {
                return reg.getAddress();
            }

            @Override
            public int getLength() {
                return reg.getLength();
            }

            @Override
            public Expression getTransform() {
                Expression transform = new ExpressionBuilder(reg.getTransform()).variables("_").build().setVariable("_", 0);
                ValidationResult val = transform.validate();
                if (!val.isValid()) {
                    throw new RuntimeException(String.format("Invalid transform '%s': %s", reg.getTransform(), val.getErrors()));
                }
                return transform;
            }

            @Override
            public ModbusRegister.Type getType() {
                switch (reg.getType()) {
                    case "float":
                        return Type.FLOAT;
                    case "int":
                        return Type.INT;
                    default:
                        throw new RuntimeException("Unknown register type '" + reg.getType() + "'");
                }
            }
        };
    }

    private SerialModbusPort port;

    @Override
    protected Class<Configuration> getConfigurationClass() {
        return Configuration.class;
    }

}
