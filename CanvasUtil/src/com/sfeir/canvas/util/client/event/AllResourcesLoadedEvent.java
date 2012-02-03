/**
 * 
 */
package com.sfeir.canvas.util.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author François LAROCHE
 *
 */
public class AllResourcesLoadedEvent extends GwtEvent<AllResourcesLoadedHandler> {

	public static final Type<AllResourcesLoadedHandler> TYPE = new Type<AllResourcesLoadedHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AllResourcesLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AllResourcesLoadedHandler handler) {
		handler.onAllResourcesLoaded(this);
	}

}
