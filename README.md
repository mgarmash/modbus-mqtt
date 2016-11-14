# modbus-mqtt
A serial Modbus to MQTT bridge

This application polls registers in a serial modbus device and publishes the values read to an MQTT topic. This can be used to publish data from a Modbus device (for example an energy meter) to MQTT for consumption by a client (for example OpenHab). 

## Building the application 

The application is a Maven Java project. You can build it by cloning the repository and running `mvn clean install assembly:single 
` in the repository directory. This will create a all inclusive java file in the target directory, for example `modbus-mqtt-1.0-SNAPSHOT-jar-with-dependencies.jar`. 

## Configuring the application 

You need a YAML configuration file. The configuration file contains three important sections:

* modbus - Used to configure the modbus port, speed and other parameters. 
* mqtt - Used to configure the MQTT broker. 
* registers - Map the modbus registers into named variables published to MQTT.

A working example, [config.yml], is provided, and how to configure the application is best examplained by disecting this example. 

### Configuring the serial modbus interface

The below example configures a Modbus device on port `/dev/ttyUSB0` with a serial speed of 9600. The Modbus slave device ID is `1`, and the device will be polled every `10` seconds. 

Some Modbus devices expect zero based register numbering, so when reading for example register 1201, the register address read should actually be 1200. For these devices, `zeroBase` must be set to `true`.

```yaml
# Configure serial modbus. 
modbus: {
    serial: { 
        port: /dev/ttyUSB0,
        speed: 9600
    }, 
    deviceId: 1,
    pollInterval: 10,
    zeroBased: true
}
```

### Configuring the MQTT broker and topic

Here we configure the MQTT broker to with which to communicate. The application will connect to the broker using host `192.168.1.5` and port `1883`. Two MQTT topics are specified:

* dataTopic - The MQTT topic to which Modbus register values read will be published. 
* commandTopic - The MQTT topic from which commands will be received. 

```yaml
# Setup the MQTT broker 
mqtt: {
    broker: {  
        host: 192.168.1.5,
        port: 1883
    }, 
    dataTopic: wattnode1-data,
    commandTopic: wattnode1-cmd
}   
```

### Define the modbus registers to read and publish

A list of registers to poll (read) from the Modbus device each defined. Each register must contain the following:

* name - The register name that is meant to be human readable and serves as MQTT path 
* address - The Modbus register address
* length - The Modbus register length in (2 byte) words. 
* transform (optional) - A mathematical expression used to transform the received value, with '_' used to subsitute the value received from Modbus. 
* type - The type of Modbus value received. Currently `int` and `float` are supported.

```yaml
registers: [
    { 
        name: frequency,
        address: 1033,
        length: 2,
        type: float
    },
    {
        name: voltage,
        address: 1019,
        length: 2,
        type: float    
    },
    {
        name: energy,
        address: 1001,
        length: 2,
        type: float    
    }
]
```

## Running the application 

The application can be started by executing the jar and passing the name of the configuration file, for example:

```bash 
java -jar modbus-mqtt-1.0-SNAPSHOT-jar-with-dependencies.jar config.yml
```
