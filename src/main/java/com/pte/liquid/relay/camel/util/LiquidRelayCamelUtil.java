package com.pte.liquid.relay.camel.util;

import java.util.UUID;

import org.apache.camel.Exchange;

import com.pte.liquid.relay.Constants;

public class LiquidRelayCamelUtil {

	 public static String determineCorrelation(Exchange exchange){
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
		
		public static int determineOrder(Exchange exchange){
			int order = 0;	
			if(exchange!=null){
				if(exchange.getIn().getHeader(Constants.ORDER_PROPERTY_NAME, Integer.class)!=null){
					order = exchange.getIn().getHeader(Constants.ORDER_PROPERTY_NAME, Integer.class);
				}			
			}	
			return order;
		}
		
		public static String determineParent(Exchange exchange){
			String parentID = "";	
			if(exchange!=null){
				if(exchange.getIn().getHeader(Constants.PARENT_ID_PROPERTY_NAME, String.class)!=null){
					parentID = exchange.getIn().getHeader(Constants.PARENT_ID_PROPERTY_NAME, String.class);
				}			
			}	
			return parentID;
		}
		
		public static void setCorrelationID(String correlationID, Exchange exchange){		
			exchange.getIn().setHeader(Constants.CORRELATION_ID_PROPERTY_NAME, correlationID);		
		}
		
		public static void setOrder(int order, Exchange exchange){		
			exchange.getIn().setHeader(Constants.ORDER_PROPERTY_NAME, order + 1);		
		}
		
		public static void setParentID(String parentID,Exchange exchange){		
			exchange.getIn().setHeader(Constants.PARENT_ID_PROPERTY_NAME, parentID);		
		}

}
