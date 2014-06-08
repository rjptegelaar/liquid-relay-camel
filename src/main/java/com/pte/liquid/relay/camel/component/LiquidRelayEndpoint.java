//Copyright 2014 Paul Tegelaar
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

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

import com.pte.liquid.relay.Converter;
import com.pte.liquid.relay.Transport;
import com.pte.liquid.relay.client.jms.JmsTransport;

/**
 * Represents a LiquidRelay endpoint.
 */
public class LiquidRelayEndpoint extends DefaultEndpoint{

    private Transport transport;
    private Converter<Exchange> camelConverter;
	

    public LiquidRelayEndpoint(String uri, LiquidRelayComponent component) {
    	 super(uri, component);
    }

    
    public LiquidRelayEndpoint(String endpointUri) {
    	super(endpointUri);
    }    

    public Producer createProducer() throws Exception {
        return new LiquidRelayProducer(this, transport, camelConverter);
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




    

    
}
