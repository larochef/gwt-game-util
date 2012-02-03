/**
 * 
 */
package com.sfeir.canvas.activities.client;

import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Activity for Canvas elements
 * 
 * @author François LAROCHE
 */
public interface CanvasActivity extends CanvasPresenter {

	/**
	 * Start this activity
	 * 
	 * @param ctx the context on which to write
	 * @param bus the bus on which to send or receive events
	 */
	public void start(Canvas canvas, EventBus bus);
	
	/**
	 * called when the activity is unloaded
	 */
	public void stop();
	
	/**
	 * Initialize the activity with the data given from previous ones
	 * 
	 * @param context parameters provided from the old activity
	 */
	public void init(Map<String, Object>  context);

}
