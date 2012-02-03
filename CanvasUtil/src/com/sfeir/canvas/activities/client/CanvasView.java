/**
 * 
 */
package com.sfeir.canvas.activities.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author François LAROCHE
 *
 */
public interface CanvasView extends IsWidget {

	/**
	 * Gets the Canvas on which to write
	 * 
	 * @return the Canvas associated to this view
	 */
	public Canvas getCanvas();
}
