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

package me.legrange.bridge.config;

import me.legrange.yaml.app.config.annotation.NotBlank;
import me.legrange.yaml.app.config.annotation.Numeric;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class Register {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getTransform() {
        return transform;
    }

    public String getType() {
        return type;
    }

    public void setType(String type)  {
        this.type = type;
    }
    
      
    public void setTransform(String expr) {
        this.transform = expr;
    }

    @NotBlank
    private String name;
    @Numeric(min=1, max=65535)
    private int address;
    @Numeric(min=1, max=8)
    private int length;
    @NotBlank
    private String type;
    @NotBlank
    private String transform = "_";

}
