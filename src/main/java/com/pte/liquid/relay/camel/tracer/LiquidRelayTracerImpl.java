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
