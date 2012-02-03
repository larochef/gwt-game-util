/**
 * 
 */
package com.sfeir.canvas.activities.client;

import java.util.HashMap;
import java.util.Map;
/**
 * Navigation manager for canvas
 * 
 * @author François LAROCHE
 */
public class CanvasNavigationManager {

	/**
	 * Map holding all the navigation cases
	 */
	private final Map<String, String> workflows;

	/**
	 * Default constructor
	 */
	public CanvasNavigationManager() {
		this.workflows = new HashMap<String, String>();
	}
	
	/**
	 * Register a navigation rule
	 * 
	 * @param from the code of the "current" activity
	 * @param action the action made from the activity
	 * @param to the view to redirect to
	 */
	public void registerNavigation(String from, String action, String to) {
		this.workflows.put(from + ":" + action, to);
	}

	/**
	 * Computes the key of the next view.
	 * <br>
	 * If no view can be resolved, the default view is returned
	 * 
	 * @param origin the view that asked for a view change
	 * @param request the request made by the view
	 * @return the key for the next view
	 */
	public String computeNextState(String origin, String request) {
		String result = this.workflows.get(origin + ":" + request);
		if(result != null) {
			return result;
		}
		// if no state could be resolved stay on origin
		return origin;
	}
}
