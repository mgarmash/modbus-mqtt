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
package me.legrange.bridge.modbus;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A modbus poll operation. 
 * @author gideon
 */
class Poll {
 
    private final int address;
    private int size;
    private final Map<Integer, ModbusRegister> registers = new HashMap<>();
    
    private Poll(ModbusRegister reg) {
        this.address = reg.getAddress();
        this.size = reg.getLength();
        registers.put(0, reg);
    }
    
    private void add(ModbusRegister reg) { 
        size = size + reg.getLength();
        registers.put(reg.getAddress() - address, reg);
    }
    
    int getAddress() {
        return address;
    }

    int getSize() {
        return size;
    };
    
    /** call listener.received() for each register and it's subset of bytes in the response received 
     * 
     * @param bytes The bytes received for all registers polled. 
     * @param listener The listener to call. 
     */
    void applyBytes(byte bytes[], ModbusListener listener) {
        registers.keySet().stream().map((offset) -> registers.get(offset)).forEach((ModbusRegister reg) -> {
            byte buf[] = new byte[reg.getLength()*2];
            System.arraycopy(bytes, (reg.getAddress() - address)*2, buf, 0, reg.getLength()*2);
            listener.received(reg, buf);
        });
    }
    
    static List<Poll> generatePolls(List<ModbusRegister> registers) {
        Collections.sort(registers, (ModbusRegister o1, ModbusRegister o2) -> o1.getAddress() - o2.getAddress());
        Poll poll = null;
        List<Poll> result = new LinkedList<>();
        for (ModbusRegister reg : registers) {
            if ((poll != null) && ((poll.address + poll.size) == reg.getAddress())) { 
                poll.add(reg);
            }
            else {
                poll = new Poll(reg);
                result.add(poll);
            }
        }
        return result;
    }
    
}
