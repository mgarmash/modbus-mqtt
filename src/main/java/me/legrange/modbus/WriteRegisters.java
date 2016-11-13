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
 * A Modbus request frame (function 4).
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class WriteRegisters extends ModbusFrame {

    /**
     * Create a new request frame to write a sequence of words to the given
     * device at the register and of the given size.
     *
     * @param deviceId The device (slave) to talk to.
     * @param register The register to start writing.
     * @param size The number of words to read.
     * @throws CrcException
     */
    public WriteRegisters(int deviceId, int register, int size, int... data) throws CrcException {
        super(FrameUtil.withCrc(
                FrameUtil.concat(new byte[]{
            (byte) deviceId,
            (byte) 16, // function code
            (byte) ((register & 0xff00) >> 8),
            (byte) (register & 0xff),
            (byte) ((size & 0xff00) >> 8),
            (byte) (size & 0xff)}, FrameUtil.toWords(data))));
    }

}
