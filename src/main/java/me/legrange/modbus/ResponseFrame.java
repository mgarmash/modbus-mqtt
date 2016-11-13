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
 * A Modbus response frame received from a Modbus slave. 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class ResponseFrame extends ModbusFrame {

    /** Create a new frame from the supplied raw Modbus data. 
     * 
     * @param frame The raw data. 
     * @throws CrcException Thrown if the frame fails the CRC check. 
     */
    ResponseFrame(byte[] frame) throws CrcException {
        super(frame);
    }

    /** Check if the frame represents a Modbus error. 
     * 
     * @return True if the frame is an error. 
     */
    public boolean isError() {
        return (getFunction() & 0x80) != 0; 
    }
    
    /** 
     * Return the number of data words in the response. 
     * @return The number of words.
     */
    public int getWordCount() {
        return getByteCount()/2;
    }
    
    /** 
     * Return the number of data bytes in the response. 
     * @return The number of bytes. 
     */
    private int getByteCount() { 
        return frame[2];
    }

    /** 
     * Return the word at the given index. 
     * @param index The index.
     * @return The data word. 
     */
    public int getWord(int index) {
        byte h = frame[index*2+3];
        byte l = frame[index*2+4];
        return (((int)h) << 8) | ((l < 0) ? 256+l : l); 
    }

    /** 
     * Return all the words.
     * @return The words.
     */
    public int[] getWords() {
        int count = getWordCount();
        int words[] = new int[count];
        for (int i = 0; i < count; ++i) {
            words[i] = getWord(i);
        }
        return words;
    }
    
    public byte[] getBytes()  {
        byte buf[] = new byte[getByteCount()];
        System.arraycopy(frame, 3, buf, 0, buf.length);
        return buf;
    }
    
  
}
