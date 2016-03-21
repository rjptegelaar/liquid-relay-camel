package com.pte.liquid.relay.camel.processor;

import java.util.Properties;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pte.liquid.relay.Converter;
import com.pte.liquid.relay.Marshaller;
import com.pte.liquid.relay.Transport;
import com.pte.liquid.relay.camel.converter.LiquidRelayExchangeConverterImpl;
import com.pte.liquid.relay.camel.util.LiquidRelayCamelUtil;
import com.pte.liquid.relay.client.stomp.StompTransport;
import com.pte.liquid.relay.marshaller.json.JsonMarshaller;
import com.pte.liquid.relay.model.Message;

public class LiquidRelayProcessor implements Processor{
	private static final transient Logger logger = LoggerFactory.getLogger(LiquidRelayProcessor.class);
	
	
    private Transport transport;
	private Converter<Exchange> converter;
	private Marshaller marshaller;
		
    private boolean enabled;

    private static LiquidRelayProcessor liquidRelayProcessor = null;
    
    public synchronized static LiquidRelayProcessor getInstance(boolean enabled, String destination, String hostname, String port) {
    	   if(liquidRelayProcessor == null) {
    		   liquidRelayProcessor = new LiquidRelayProcessor(enabled, destination, hostname, port);
    	   }
    	   logger.info("Created LiquidRelayProcessor.");
    	   return liquidRelayProcessor;
    	}
	
    protected LiquidRelayProcessor(boolean enabled, String destination, String hostname, String port){    	
    	this.enabled = enabled;
    	
    	marshaller = new JsonMarshaller();
    	
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
    	
    	transport = new StompTransport();
    	transport.setProperties(properties);
    	transport.setMarshaller(marshaller);
    	converter = new LiquidRelayExchangeConverterImpl();
    	
    }
    
    
	@Override
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
    		}else{
    			if(logger.isDebugEnabled()){
    				logger.debug("Skipping message because liquid is disabled");
    			}
    		}
    	} catch (Exception e) {
			//Empty by design
		}
		
	}


}
