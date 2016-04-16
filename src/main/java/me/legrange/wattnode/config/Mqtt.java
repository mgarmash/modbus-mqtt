package me.legrange.wattnode.config;

import static java.lang.String.format;

/**
 *
 * @author gideon
 */
public class Mqtt {

    private Broker broker;

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public static class Broker {

        public Broker() {
        }
        
        

        private int port;

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

        private String host;

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
        
            @Override
    public String toString() {
        return new StringBuilder()
                .append(format("Host: %s", host))
                .append(format("Port: %s", port))
                .toString();
    }


    }
    
        @Override
    public String toString() {
        return new StringBuilder()
                .append(broker).toString();
    }


}
