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

import java.nio.ByteBuffer;
import net.objecthunter.exp4j.Expression;

/**
 *
 * @author gideon
 */
public interface ModbusRegister {
    
    public enum Type { FLOAT, INT; }
    
    public String getName();
    
    public int getAddress();
    public int getLength();

    public Expression getTransform();
 
    public Type getType();

    
    public static double decode(ModbusRegister reg, byte bytes[]) {
        switch (reg.getType()) {
            case FLOAT :
                return decodeFloat(reg, bytes);
            default : return 0.0;
        }
    }
    
    static double decodeFloat(ModbusRegister reg, byte bytes[]) {
        float f = ByteBuffer.wrap(new byte[]{bytes[2], bytes[3], bytes[0], bytes[1]}).getFloat();
        return reg.getTransform().setVariable("_", f).evaluate();
    }
    
    static double decodeInt(ModbusRegister reg, int words[]) {
        long lval = 0;
        for (int i = 0; i < words.length; ++i) {
            lval = (lval << 8) | words[i];
        }
        return reg.getTransform().setVariable("_", lval).evaluate();
    }

    
      
}
