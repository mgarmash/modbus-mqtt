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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import me.legrange.wattnode.config.Register;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
class RegisterList {
    
    synchronized void addRegister(Register reg) {
        boolean added = false;
        for (int g = 0; g < groups.size(); ++g) {
            Group group = groups.get(g);
            if (group.canAdd(reg)) {
                group.add(reg);
                if (g < (groups.size()-1)) { // we're trying to fill in the gaps.
                    Group next = groups.get(g+1);
                    if (group.canAdd(next)) {
                        group.add(next);
                        groups.remove(g+1);
                    }
                }
                added = true;
            }
        }
        if (!added) {
            groups.add(new Group(reg));
        }
        Collections.sort(groups, (Group o1, Group o2) -> o1.address - o2.address);
//        dump();
    }
    
    List<Group> getGroups() {
        return groups;
    }
    
    private void dump() { 
        System.out.println("#");
        for (Group group : groups) { 
            System.out.printf("address = %d, size = %d, count = %d\n", group.address, group.size, group.registers.size());
        }
    }
    
    private List<Group> groups = new LinkedList<>();

    private class Group { 
        
        private Group(Register reg) {
            address = reg.getAddress();
            size = reg.getLength();
            registers.add(reg);
        }
        
        private void add(Register reg) {
            if (reg.getAddress() > address) {
                registers.add(reg);
            }
            else {
                registers.add(0, reg);
                address = reg.getAddress();
            }
            size = size + reg.getLength();
        }
        
        private void add(Group group) {
            for (Register reg : group.registers) {
                add(reg);
            }
        }
        
        private int getAddress() {
            return address;
        }

        private int getSize() {
            return size;
        }
        
        private boolean canAdd(Register reg) {
            return (reg.getAddress() == address + size) || (reg.getAddress() + reg.getLength() == address);
        }
        
        private boolean canAdd(Group group) {
            return (group.address == address + size);
        }
        
        private List<Register> registers = new LinkedList<>();
        private int address;
        private int size = 0;
    }
    
}
