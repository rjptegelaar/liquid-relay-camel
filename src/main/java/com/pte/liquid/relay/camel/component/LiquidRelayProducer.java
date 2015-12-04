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

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pte.liquid.relay.Constants;
import com.pte.liquid.relay.Converter;
import com.pte.liquid.relay.Transport;
import com.pte.liquid.relay.model.Message;

/**
 * The LiquidRelay producer.
 */
public class LiquidRelayProducer extends DefaultProducer {
    private static final transient Logger LOG = LoggerFactory.getLogger(LiquidRelayProducer.class);
    private LiquidRelayEndpoint endpoint;
    private Transport transport;
    private Converter<Exchange> converter;
    
    public LiquidRelayProducer(LiquidRelayEndpoint endpoint, Transport transport, Converter<Exchange> converter) {
        super(endpoint);               
        this.converter = converter;
        this.transport = transport;
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
    	try{
        	Message preMsg = converter.convert(exchange);  
        	String correlationID = determineCorrelation(exchange);
        	String parentId = determineParent(exchange);
        	int order = determineOrder(exchange);
        	String messageID = preMsg.getId();
        	
        	setCorrelationID(correlationID, exchange);
        	preMsg.setCorrelationID(correlationID);
        	
        	setParentID(messageID, exchange);
        	preMsg.setParentID(parentId);
        	
        	setOrder(order, exchange);
        	preMsg.setOrder(order);	
        	
        	   	    	    	    	  	   
        	transport.send(preMsg);    	      		
    	} catch (Exception e) {
			//Empty by design
		}
    }
    
    private String determineCorrelation(Exchange exchange){
		String correlationId = "";	
		if(exchange!=null){
			if(exchange.getIn().getHeader(Constants.CORRELATION_ID_PROPERTY_NAME, String.class)!=null){
				correlationId = exchange.getIn().getHeader(Constants.CORRELATION_ID_PROPERTY_NAME, String.class);
			} else {
				correlationId = UUID.randomUUID().toString();
			}
		}	
		return correlationId;
	}
	
	private int determineOrder(Exchange exchange){
		int order = 0;	
		if(exchange!=null){
			if(exchange.getIn().getHeader(Constants.ORDER_PROPERTY_NAME, Integer.class)!=null){
				order = exchange.getIn().getHeader(Constants.ORDER_PROPERTY_NAME, Integer.class);
			}			
		}	
		return order;
	}
	
	private String determineParent(Exchange exchange){
		String parentID = "";	
		if(exchange!=null){
			if(exchange.getIn().getHeader(Constants.PARENT_ID_PROPERTY_NAME, String.class)!=null){
				parentID = exchange.getIn().getHeader(Constants.PARENT_ID_PROPERTY_NAME, String.class);
			}			
		}	
		return parentID;
	}
	
	private void setCorrelationID(String correlationID, Exchange exchange){		
		exchange.getIn().setHeader(Constants.CORRELATION_ID_PROPERTY_NAME, correlationID);		
	}
	
	private void setOrder(int order, Exchange exchange){		
		exchange.getIn().setHeader(Constants.ORDER_PROPERTY_NAME, order + 1);		
	}
	
	private void setParentID(String parentID,Exchange exchange){		
		exchange.getIn().setHeader(Constants.PARENT_ID_PROPERTY_NAME, parentID);		
	}

}
