package com.pte.liquid.relay.camel.tracer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.processor.interceptor.TraceEventHandler;
import org.apache.camel.processor.interceptor.TraceEventMessage;
import org.apache.camel.processor.interceptor.TraceInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pte.liquid.relay.Converter;
import com.pte.liquid.relay.Transport;

public class LiquidRelayTracerImpl implements TraceEventHandler{

    private Transport stompTransport;
    private Converter<TraceEventMessage> camelConverter;
		
	public LiquidRelayTracerImpl(){
		ApplicationContext appCtx = new ClassPathXmlApplicationContext("com/pte/liquid/relay/camel/tracer/application-context.xml");
    	stompTransport = (Transport) appCtx.getBean("relayApiStompTransport");
        camelConverter = (Converter<TraceEventMessage>) appCtx.getBean("relayCamelConverter");
	}
	
	@Override
	public void traceExchange(ProcessorDefinition processorDefinition, Processor processor,
			TraceInterceptor traceInterceptor, Exchange exchange) throws Exception {
		TraceEventMessage message = exchange.getIn(TraceEventMessage.class);		
		
		stompTransport.send(camelConverter.convert(message));
	}

	@Override
	public Object traceExchangeIn(ProcessorDefinition processorDefinition, Processor processor,
			TraceInterceptor traceInterceptor, Exchange exchange) throws Exception {
		
		return null;
	}

	@Override
	public void traceExchangeOut(ProcessorDefinition processorDefinition, Processor processor,
			TraceInterceptor traceInterceptor, Exchange exchange, Object object) throws Exception {


		
	}

}
