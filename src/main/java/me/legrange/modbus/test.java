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
package me.legrange.modbus;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class test {

    public static void main(String... args) throws Exception {
        test t = new test();
        try {
        t.start();
//        t.write(1604, 33);
//          t.write(1618, 0);
          t.poll(1618, 2);
  //      t.write(1619, 1);
//        t.write(1620, 1);
       }
        finally {
        t.stop();
        }
    }

    private void start() throws SerialException {
        System.out.println("[Opening port]");
        modbus = SerialModbusPort.open("/dev/tty.usbserial", 9600);

    }

    private void stop() throws SerialException {
        if (modbus!=null)
            modbus.close();
        System.out.println("Done");

    }

    private int[] poll(int register, int size) throws CrcException, ModbusException {
        ResponseFrame poll = modbus.poll(new ReadInputRegisters(1, register, size));
        if (poll.isError()) {
            throw new ModbusException(String.format("Error: %s", ModbusError.valueOf(poll.getFunction())));
        } else {
            int c = poll.getWordCount();
            int res[] = new int[poll.getWordCount()];
            for (int i = 0; i < c; ++i) {
                int word = poll.getWord(i);
                res[i] = word;
            }
            return res;
        }
    }

    private void write(int register, int data) throws ModbusException {
        ResponseFrame poll = modbus.write(new WriteRegister(1, register, data));
        System.out.println("[Received frame]");
        if (poll.isError()) {
            System.out.printf("Error: %s\n ", ModbusError.valueOf(poll.getFunction()));
        } else {
            int c = poll.getWordCount();
            System.out.printf("%d words received\n", c);
        }
    }

    private SerialModbusPort modbus;

}
