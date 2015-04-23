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

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pte.liquid.relay.Converter;
import com.pte.liquid.relay.Transport;

/**
 * Represents the component that manages {@link LiquidRelayEndpoint}.
 */
public class LiquidRelayComponent extends DefaultComponent {

    private Transport stompTransport;
	
    private Converter<Exchange> camelConverter;
	
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
    	ApplicationContext appCtx = new ClassPathXmlApplicationContext("com/pte/liquid/relay/camel/component/application-context.xml");
    	stompTransport = (Transport) appCtx.getBean("relayApiStompTransport");
        camelConverter = (Converter<Exchange>) appCtx.getBean("relayCamelConverter");
    	
        LiquidRelayEndpoint endpoint = new LiquidRelayEndpoint(uri, this);
        endpoint.setTransport(stompTransport);
        endpoint.setCamelConverter(camelConverter);
        
        setProperties(endpoint, parameters);
        
       
        
        
        
        return endpoint;
    }



	public Transport getStompTransport() {
		return stompTransport;
	}



	public void setStompTransport(Transport stompTransport) {
		this.stompTransport = stompTransport;
	}



	public Converter<Exchange> getCamelConverter() {
		return camelConverter;
	}

	public void setCamelConverter(Converter<Exchange> camelConverter) {
		this.camelConverter = camelConverter;
	}
    
    
}
