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
 * Broker configuration.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class Broker {

    /**
     * Get the value of port
     *
     * @return the value of port
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the value of port
     *
     * @param port new value of port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Get the value of host
     *
     * @return the value of host
     */
    public String getHost() {
        return host;
    }

    /**
     * Set the value of host
     *
     * @param host new value of host
     */
    public void setHost(String host) {
        this.host = host;
    }

    @Numeric(min=1, max=65535)
    private int port;
    @NotBlank
    private String host;

}
