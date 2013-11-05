
package com.pte.liquid.relay.camel.component;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
    private Transport jmsTransport;
    private Converter<Exchange> camelConverter;
    
    public LiquidRelayProducer(LiquidRelayEndpoint endpoint) {
        super(endpoint);
        
        ApplicationContext appCtx = new ClassPathXmlApplicationContext("com/pte/liquid/relay/camel/component/application-context.xml");
        
        jmsTransport = (Transport) appCtx.getBean("relayApiJmsTransport");
        camelConverter = (Converter<Exchange>) appCtx.getBean("relayCamelConverter");
        
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
    	LOG.info("Start send");
    	
    	String correlationID = exchange.getIn().getHeader(Constants.CORRELATION_ID_PROPERTY_NAME, String.class);
    	String bcID = exchange.getIn().getHeader("breadcrumbId", String.class);
    	    	
    	Message msg = camelConverter.convert(exchange);  
    	
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
    	    	    	    	    	  	   
    	jmsTransport.send(msg);    	
    	
    	LOG.info("End send");    
    }

}
