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
package me.legrange.wattnode;

import static java.lang.String.format;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public final class Configuration {

    private Date released;
    private String version;
    private List< String> protocols;
    private Map< String, String> users;

    public Date getReleased() {
        return released;
    }

    public String getVersion() {
        return version;
    }

    public void setReleased(Date released) {
        this.released = released;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List< String> getProtocols() {
        return protocols;
    }

    public void setProtocols(List< String> protocols) {
        this.protocols = protocols;
    }

    public Map< String, String> getUsers() {
        return users;
    }

    public void setUsers(Map< String, String> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(format("Version: %s\n", version))
                .append(format("Released: %s\n", released))
                .append(format("Supported protocols: %s\n", protocols))
                .append(format("Users: %s\n", users))
                .toString();
    }
}
