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
package me.legrange.bridge.mqtt;

/**
 *
 * @author gideon
 */
public interface MqttListener {
    
    /** A message was received from the MQTT bus
     * 
     * @param topic The topic for which the message was received. 
     * @param msg The message text. 
     */
    void received(String topic, String msg);
 
}
