/**
 * 
 */
package com.sfeir.canvas.activities.client;

import java.util.Map;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;

/**
 * Abstract activity, extending it prevents from having to redefine all handlers, 
 * only override the ones you need.
 * 
 * @author François LAROCHE
 *
 */
public abstract class AbstractCanvasActivity implements CanvasActivity {

	@Override
	public void onClick(ClickEvent event) {}

	@Override
	public void onFocus(FocusEvent event) {}

	@Override
	public void onBlur(BlurEvent event) {}

	@Override
	public void onKeyPress(KeyPressEvent event) {}

	@Override
	public void onKeyDown(KeyDownEvent event) {}

	@Override
	public void onMouseMove(MouseMoveEvent event) {}

	@Override
	public void stop() {}

	@Override
	public void init(Map<String, Object> context) {}

}
