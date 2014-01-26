
package com.pte.liquid.relay.camel.component;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pte.liquid.relay.Constants;
import com.pte.liquid.relay.Converter;
import com.pte.liquid.relay.Transport;
import com.pte.liquid.relay.client.jms.JmsTransport;
import com.pte.liquid.relay.model.Message;

/**
 * The LiquidRelay producer.
 */
public class LiquidRelayProducer extends DefaultProducer {
    private static final transient Logger LOG = LoggerFactory.getLogger(LiquidRelayProducer.class);
    private LiquidRelayEndpoint endpoint;
    private Transport transport;
    private Converter<Exchange> camelConverter;
    
    public LiquidRelayProducer(LiquidRelayEndpoint endpoint, Transport transport, Converter<Exchange> camelConverter) {
        super(endpoint);               
        this.camelConverter = camelConverter;
        this.transport = transport;
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
    	LOG.info("Start send");
    	
    	String correlationID = exchange.getIn().getHeader(Constants.CORRELATION_ID_PROPERTY_NAME, String.class);
    	String bcID = exchange.getIn().getHeader("breadcrumbId", String.class);
    	LOG.info("Preconvert");	
    	Message msg = camelConverter.convert(exchange);  
    	LOG.info("Created message");
    	if(correlationID!=null && !"".equals(correlationID)){
    		msg.setCorrelationID(correlationID);	
    	}else if(bcID!=null && !"".equals(bcID)){
    		msg.setCorrelationID(bcID);
    		exchange.getIn().setHeader(Constants.CORRELATION_ID_PROPERTY_NAME, bcID);
    	}else{
    		String newID = UUID.randomUUID().toString();
    		msg.setCorrelationID(newID);
    		exchange.getIn().setHeader(Constants.CORRELATION_ID_PROPERTY_NAME, newID);    		
    	}
    	LOG.info("Set corid");    	    	    	    	  	   
    	transport.send(msg);    	
    	
    	LOG.info("End send");    
    }

}
