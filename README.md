# modbus-mqtt
A serial Modbus to MQTT bridge

This application polls registers in serial modbus devices and publishes the values read to MQTT topics. This can be used to publish data from Modbus devices (for example energy meters) to MQTT for consumption by a client (for example OpenHab). 

## Building the application 

The application is a Maven Java project. You can build it by cloning the repository and running `mvn clean install assembly:single 
` in the repository directory. This will create a all inclusive java file in the target directory, for example `modbus-mqtt-1.0-SNAPSHOT-jar-with-dependencies.jar`. 

## Configuring the application 

You need a YAML configuration file. The configuration file contains three important sections:

* modbus - Used to configure the modbus port, speed and other parameters. 
* mqtt - Used to configure the MQTT broker. 
* slaves - Map the modbus slaves with their registers into named variables published to MQTT.

A working example, config.yml, is provided, and how to configure the application is best explained by disecting this example. 

### Configuring the serial modbus interface

The below example configures a Modbus device on port `/dev/ttyUSB0` with a serial speed of 9600. 

```yaml
# Configure serial modbus. 
modbus: {
    serial: { 
        port: /dev/ttyUSB0,
        speed: 9600
    }
}
```

### Configuring the MQTT broker and topic

Here we configure the MQTT broker to with which to communicate. The application will connect to the broker using host `192.168.1.5` and port `1883`. The MQTT topic to which data is published is specified:

* dataTopic - The MQTT topic to which Modbus register values read will be published. 

```yaml
# Setup the MQTT broker 
mqtt: {
    broker: {  
        host: 192.168.1.5,
        port: 1883
    }, 
    dataTopic: modbus-data
}   
```

### Define the modbus slaves and registers to read and publish

A list of modbus slaves from which data will be read is defined. Each slave must contain the following:

* name - The name of the slave that is meant to be human readable and serves as part of the MQTT path. 
* deviceId - The slave's unique modbus device ID. 
* pollInterval - How often registers from the slave must be read. 
* zeroBased - Some Modbus devices expect zero based register numbering, so when reading for example register 1201, the register address read should actually be 1200. For these devices, zeroBase must be set to true.
* registers - A list of registers to read from the slave.


The list of `registers` to poll (read) from the Modbus device must be defined as an array. Each register must contain the following:

* name - The register name that is meant to be human readable and serves as MQTT path. 
* address - The Modbus register address
* length - The Modbus register length in (2 byte) words. 
* transform (optional) - A mathematical expression used to transform the received value, with '_' used to subsitute the value received from Modbus. 
* type - The type of Modbus value received. Currently `int` and `float` are supported.

```yaml

slaves: [
    {
        name: wattnode1,
        deviceId: 1,
        pollInterval: 60,
        zeroBased: true,
        registers: [
            {
                name: energyA,
                address: 1101,
                length: 2,
                type: float    
            },
            {
                name: energyB,
                address: 1103,
                length: 2,
                type: float    
            },
            {
                name: energyC,
                address: 1105,
                length: 2,
                type: float    
            }
        ]
    }
]

```

In the above example, every 60 seconds registers 1101, 1103 and 1105 will be read from modbus device 1 and published as `/wattnode1/energyA`, `/wattnode1/energyB` and `/wattnode1/energyC`. They'll all be decoded as floating point values. Modbus device one requires zero based addressing. 

## Running the application 

The application can be started by executing the jar and passing the name of the configuration file, for example:

```bash 
java -jar modbus-mqtt-1.0-SNAPSHOT-jar-with-dependencies.jar config.yml
```
