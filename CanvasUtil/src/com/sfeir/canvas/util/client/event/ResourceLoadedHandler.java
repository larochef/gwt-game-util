/**
 * 
 */
package com.sfeir.canvas.util.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author François LAROCHE
 *
 */
public interface ResourceLoadedHandler extends EventHandler {

	public void onResourcesLoaded(ResourceLoadedEvent event);
}
