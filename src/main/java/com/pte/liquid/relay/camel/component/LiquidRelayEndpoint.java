//Copyright 2015 Paul Tegelaar
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
package com.pte.liquid.relay.camel.component;

import java.util.Properties;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pte.liquid.relay.Converter;
import com.pte.liquid.relay.Transport;

/**
 * Represents a LiquidRelay endpoint.
 */
public class LiquidRelayEndpoint extends DefaultEndpoint{

    private Transport transport;
    private Converter<Exchange> camelConverter;
    private String destination;
    private String hostname;
    private String port;
    private boolean enabled = true;
       

    public LiquidRelayEndpoint(String uri, LiquidRelayComponent component) {
    	super(uri, component);

    }

    public Producer createProducer() throws Exception {

        Properties properties = new Properties();
        
        if(destination!=null){
        	properties.put("relay_destination", destination);
        }
        if(hostname!=null){
        	properties.put("relay_stomp_hostname", hostname);
        }
        if(port!=null){
        	properties.put("relay_stomp_port", port);
        }

    	ApplicationContext appCtx = new ClassPathXmlApplicationContext("com/pte/liquid/relay/camel/component/application-context.xml");

     	transport = (Transport) appCtx.getBean("relayApiStompTransport");     	     	
     	transport.setProperties(properties);   
        camelConverter = (Converter<Exchange>) appCtx.getBean("relayCamelConverter");        
        
        
        
        return new LiquidRelayProducer(this, transport, camelConverter, enabled);
    }

    public boolean isSingleton() {
        return true;
    }
  



    public Consumer createConsumer(Processor processor) throws Exception {
        return new LiquidRelayConsumer(this, processor);
    }


	public Transport getTransport() {
		return transport;
	}


	public void setTransport(Transport transport) {
		this.transport = transport;
	}


	public Converter<Exchange> getCamelConverter() {
		return camelConverter;
	}


	public void setCamelConverter(Converter<Exchange> camelConverter) {
		this.camelConverter = camelConverter;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {				
		this.destination = destination;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}




	
	

	



    

    
}
