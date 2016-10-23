
import java.nio.ByteBuffer;

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

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class test {

    public static void main(String...args) {
      //  byte b[] = new byte[]{-28, -21, 0x42, 0x47 };
        byte b[] = new byte[]{ 0x42, 0x47, -28, -21 };
        float f = ByteBuffer.wrap(b).getFloat();
        System.out.println(" f =  " + f);
    }
    
}
