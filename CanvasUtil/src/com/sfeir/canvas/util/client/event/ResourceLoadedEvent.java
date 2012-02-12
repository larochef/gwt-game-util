/**
 * 
 */
package com.sfeir.canvas.util.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author François LAROCHE
 *
 */
public class ResourceLoadedEvent extends GwtEvent<ResourceLoadedHandler> {

	public static final Type<ResourceLoadedHandler> TYPE = new Type<ResourceLoadedHandler>();
	
	private final float percentage;
	
	public ResourceLoadedEvent(float percent, Object source) {
		setSource(source);
		this.percentage = percent;
		
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ResourceLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ResourceLoadedHandler handler) {
		handler.onResourcesLoaded(this);
	}

	public float getPercentage() {
		return percentage;
	}
	
}
