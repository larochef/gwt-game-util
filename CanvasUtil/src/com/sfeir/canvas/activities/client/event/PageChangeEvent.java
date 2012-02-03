/**
 * 
 */
package com.sfeir.canvas.activities.client.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event used to change page
 * 
 * @author François LAROCHE
 */
public class PageChangeEvent extends GwtEvent<PageChangeEventHandler> {
	/**
	 * Type used for this event
	 */
	public static final Type<PageChangeEventHandler> TYPE = new Type<PageChangeEventHandler>();
	
	/**
	 * Event to go back
	 */
	public static final String BACK = "back";
	/**
	 * Cancel event
	 */
	public static final String CANCEL = "cancel";
	/**
	 * next event
	 */
	public static final String NEXT = "next";

	private final Map<String, Object> context;
	private final String request;
	
	public PageChangeEvent(String request, Map<String, Object> context) {
		Map<String, Object> notNullContext = context == null ? new HashMap<String, Object>() : context;
		this.context = Collections.unmodifiableMap(notNullContext);
		this.request = request;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public String getRequest() {
		return request;
	}

	@Override
	public Type<PageChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PageChangeEventHandler handler) {
		handler.handlePageChange(this);
	}

}
