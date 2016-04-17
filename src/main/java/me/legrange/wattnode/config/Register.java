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

package me.legrange.wattnode.config;

import java.util.ArrayList;
import java.util.Arrays;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

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
        return transform.toString();
    }

 
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
      
    public void setTransform(String expr) throws ConfigurationException {
        transform = new ExpressionBuilder(expr).variables("_").build().setVariable("_", 0);
        ValidationResult val = transform.validate();
        if (!val.isValid()) {
            throw new ConfigurationException(String.format("Invalid transform '%s': %s", expr, val.getErrors()));
        }
    }
    
        static String hexString(int data[]) {
        StringBuilder buf = new StringBuilder();
        for (int b : data) {
            buf.append(String.format("%04x ", b));
        }
        return buf.toString();
    }

    public double decode(int words[]) {
        long lval = 0;
        for (int i = 0; i < words.length; ++i) {
            lval = (lval << 8) | words[i];
        }
        transform.setVariable("_", lval);
        return transform.evaluate();
    }


    void validate() throws ConfigurationException {
        if (name == null) throw new ConfigurationException("Register name not defined");
        if (address <= 0) throw new ConfigurationException("Register address not defined");
        if (length <= 0) throw new ConfigurationException("Register length not defined");
    }
    
    private String name;
    private int address;
    private int length;
    private String type;
    private Expression transform = new ExpressionBuilder("_").variables("_").build().setVariable("_", 0);

}
