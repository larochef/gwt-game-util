/**
 * 
 */
package com.sfeir.canvas.activities.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;

/**
 * Activity presenter, holding the different handlers for events
 * 
 * @author François LAROCHE
 */
public interface CanvasPresenter {

	/**
	 * Action called on clicks from the canvas
	 * 
	 * @param event the click event from the canvas
	 */
	public void onClick(ClickEvent event);

	/**
	 * Action called on the focus event
	 * 
	 * @param event the event generated from the canvas
	 */
	public void onFocus(FocusEvent event);

	/**
	 * Action called on the blur event
	 * 
	 * @param event the event generated from the canvas
	 */
	public void onBlur(BlurEvent event);

	/**
	 * Event called when a key is pressed
	 * 
	 * @param event the event generated from the canvas
	 */
	public void onKeyPress(KeyPressEvent event);

	/**
	 * Event called on the key down event
	 * 
	 * @param event the event generated from the canvas
	 */
	public void onKeyDown(KeyDownEvent event);

	/**
	 * Event called when the mouse is moved
	 * 
	 * @param event the event generated from the canvas
	 */
	public void onMouseMove(MouseMoveEvent event);
}
