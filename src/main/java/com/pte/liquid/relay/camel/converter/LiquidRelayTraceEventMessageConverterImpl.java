package com.pte.liquid.relay.camel.converter;

import org.apache.camel.processor.interceptor.TraceEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pte.liquid.relay.Constants;
import com.pte.liquid.relay.Converter;
import com.pte.liquid.relay.exception.RelayException;
import com.pte.liquid.relay.model.Message;

public class LiquidRelayTraceEventMessageConverterImpl  implements Converter<TraceEventMessage>{

	private static final transient Logger LOG = LoggerFactory.getLogger(LiquidRelayTraceEventMessageConverterImpl.class);

	private final static String ESB_TYPE_PROPERTY_VALUE = "APACHE_CAMEL_TRACER";
	
	//CamelCorrelationId
	
	@Override
	public Message convert(TraceEventMessage traceEventMessage) throws RelayException {
		Message newMsg = new Message();
		
		processHeaderString("IN_", newMsg, traceEventMessage.getHeaders());
		processHeaderString("OUT_", newMsg, traceEventMessage.getOutHeaders());
		processPropertiesString(newMsg, traceEventMessage.getProperties());
		
		newMsg.setSnapshotTime(traceEventMessage.getTimestamp());
		newMsg.setCorrelationID(getCorrelationID(traceEventMessage.getHeaders(), traceEventMessage.getShortExchangeId()));
		newMsg.setLocation(getLocation(traceEventMessage.getExchangePattern(), traceEventMessage.getFromEndpointUri(), traceEventMessage.getPreviousNode(), traceEventMessage.getPreviousNode(), traceEventMessage.getExchangeId(), traceEventMessage.getRouteId()));
		
		newMsg.createPart("EXCHANGE_IN", traceEventMessage.getBody());
		newMsg.setSystemHeader("EXCHANGE_IN_TYPE", traceEventMessage.getBodyType());
		
		newMsg.createPart("EXCHANGE_OUT", traceEventMessage.getOutBody());
		newMsg.setSystemHeader("EXCHANGE_OUT_TYPE", traceEventMessage.getOutBodyType());
		
		newMsg.setSystemHeader(Constants.ESB_TYPE_PROPERTY_NAME, ESB_TYPE_PROPERTY_VALUE);
		
		
		//traceEventMessage.
		
		return newMsg;
	}
	
	
	private void processHeaderString(String prefix, Message msg, String headers){
		
		//Do magic
				
	}
	
	private void processPropertiesString(Message msg, String properties){
		
		//Do magic
				
	}
	
	private String getCorrelationID(String headers, String shortExchangeId){		
		
		//Do magic
		
		return "";
	}
	
	private String getLocation(String exchangePattern, String fromEndpointUri, String previousNodeName, String toNodeName, String exchangeId, String routeId){
		
		//Do magic
		
		return "";
	}
	
	

}
