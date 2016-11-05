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

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public interface ModbusListener {
    
    /** A Modbus response was received
     * 
     * @param reg The register form which the response was received.
     * @param bytes The response value as byte array
     */
    void received(ModbusRegister reg, byte bytes[]);
    
    /** An error occurred while doing a Modbus request 
     * 
     * @param e The exception that occurred. 
     */
    void error(Throwable e);

}
