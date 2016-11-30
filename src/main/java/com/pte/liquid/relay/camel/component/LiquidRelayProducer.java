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

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

import com.pte.liquid.relay.Converter;
import com.pte.liquid.relay.Transport;
import com.pte.liquid.relay.camel.util.LiquidRelayCamelUtil;
import com.pte.liquid.relay.model.Message;

/**
 * The LiquidRelay producer.
 */
public class LiquidRelayProducer extends DefaultProducer {

    private LiquidRelayEndpoint endpoint;
    private Transport transport;
    private Converter<Exchange> converter;
    private boolean enabled;
    
    public LiquidRelayProducer(LiquidRelayEndpoint endpoint, Transport transport, Converter<Exchange> converter, boolean enabled) {
        super(endpoint);               
        this.converter = converter;
        this.transport = transport;
        this.endpoint = endpoint;
        this.enabled = enabled;
    }

    public void process(Exchange exchange) throws Exception {
    	try{
    		if(enabled){
    			    		
	        	Message preMsg = converter.convert(exchange);  
	        	String correlationID = LiquidRelayCamelUtil.determineCorrelation(exchange);
	        	String parentId = LiquidRelayCamelUtil.determineParent(exchange);
	        	int order = LiquidRelayCamelUtil.determineOrder(exchange);
	        	String messageID = preMsg.getId();
	        	
	        	LiquidRelayCamelUtil.setCorrelationID(correlationID, exchange);
	        	preMsg.setCorrelationID(correlationID);
	        	
	        	LiquidRelayCamelUtil.setParentID(messageID, exchange);
	        	preMsg.setParentID(parentId);
	        	
	        	LiquidRelayCamelUtil.setOrder(order, exchange);
	        	preMsg.setOrder(order);	
	        	
	        	   	    	    	    	  	   
	        	transport.send(preMsg);
    		}
    	} catch (Exception e) {
    		transport.destroy();
		}
    }
    


    
   

}
