/**
 *
 * Copyright (c) 2013 Paul Tegelaar. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.pte.liquid.relay.camel.converter;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pte.liquid.relay.Constants;
import com.pte.liquid.relay.Converter;
import com.pte.liquid.relay.exception.RelayException;
import com.pte.liquid.relay.model.Message;

public class LiquidRelayConverterImpl implements Converter<Exchange>{

	private static final transient Logger LOG = LoggerFactory.getLogger(LiquidRelayConverterImpl.class);
	private final static String PARENT_ID_PROPERTY_NAME = "liquid_parent_id";
	private final static String ORDER_PROPERTY_NAME = "liquid_message_order";
	private final static String ESB_TYPE_PROPERTY_NAME = "liquid_esb_type";
	private final static String ESB_TYPE_PROPERTY_VALUE = "APACHE_CAMEL";
	
	
	@Override
	public Message convert(Exchange exchange) throws RelayException {		
		return convertExchange(exchange);
	}
	
	private Message convertExchange(Exchange exchange){			
		Message newMsg = new Message();
						
		newMsg.setSnapshotTime(new Date());
		newMsg.setLocation(createLocationName(exchange.getFromRouteId(), exchange.getFromEndpoint().getEndpointKey(), exchange.getContext().getName(), Constants.LOCATION_SEPERATOR));		
		Map<String, Object> exchangeProperties = exchange.getProperties();
		if(exchangeProperties!=null){
					
			Set<String> propNames = exchangeProperties.keySet();
					
			for (String propName : propNames) {			
				newMsg.setHeader(propName, exchange.getProperty(propName, String.class));				
			}		
		}
											
		Map<String, Object> exchangeHeaders = exchange.getIn().getHeaders();
		
		if(exchangeHeaders!=null){
		
			Set<String> headerNames = exchangeHeaders.keySet();
			
			for (String headerName : headerNames) {		
				newMsg.setHeader(headerName, exchange.getIn().getHeader(headerName, String.class));
			}		
		}
		
		if(exchange.getIn().getBody(String.class)!=null){
			newMsg.createPart("EXCHANGE_IN", exchange.getIn().getBody(String.class));	
		}
		
		return newMsg;
	}
	
	private String createLocationName(String fromRouteID, String fromEndPointID, String contextName, String locationSeperator){
				
		StringBuffer sb = new StringBuffer();
		
		sb.append(fromRouteID);
		sb.append(locationSeperator);
		sb.append(fromEndPointID);
		sb.append(locationSeperator);
		sb.append(contextName);
		
		return sb.toString();
	}
	

}
