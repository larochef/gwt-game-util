/**
 * 
 */
package com.sfeir.canvas.activities.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.sfeir.canvas.activities.client.event.PageChangeEvent;
import com.sfeir.canvas.activities.client.event.PageChangeEventHandler;

/**
 * Simple manager for Canvas Activities.<br>
 * It will manage the different {@link CanvasActivity} registered on it.<br>
 * To navigate from a {@link CanvasActivity} to another, you juste have to fire
 * a {@link PageChangeEvent} on the bus, and the manager will do the rest,
 * depending on the state given by the {@link CanvasNavigationManager}.
 * 
 * @author François LAROCHE
 */
public class CanvasActivityManager {
	/**
	 * Key for the default activity
	 */
	private static final String DEFAULT_ACTIVITY = "default";

	/**
	 * key in map for the current activity
	 */
	private String currentActivityKey;
	/**
	 * Current activity
	 */
	private CanvasActivity currentActivity;
	/**
	 * Workflow manager, to handle navigation
	 */
	private final CanvasNavigationManager manager;
	/**
	 * the bus on which to receive events
	 */
	private final EventBus bus;
	/**
	 * the different activities configured
	 */
	private final Map<String, CanvasActivity> activities;
	/**
	 * the view containing the canvas
	 */
	private final CanvasView view;

	/**
	 * Constructor initializing this {@link CanvasActivityManager}
	 * 
	 * @param bus
	 *            the event bus from which to receive events
	 * @param manager
	 *            the navigation manager
	 * @param view
	 *            the view containing the canvas on which to write
	 */
	public CanvasActivityManager(EventBus bus, CanvasNavigationManager manager, CanvasView view) {
		this.currentActivityKey = DEFAULT_ACTIVITY;
		this.manager = manager;
		this.view = view;
		this.bus = bus;
		this.activities = new HashMap<String, CanvasActivity>();
		this.bus.addHandler(PageChangeEvent.TYPE, new PageChangeEventHandler() {
			@Override
			public void handlePageChange(PageChangeEvent event) {
				changePage(event.getRequest(), event.getContext());
			}
		});
	}

	/**
	 * Change the page
	 * 
	 * @param request
	 *            the request (next, previous, and so on)
	 * @param context
	 *            a map with objects passed from an activity to another
	 */
	private void changePage(String request, Map<String, Object> context) {
		if (this.currentActivity != null) {
			this.currentActivity.stop();
		}
		if (this.currentActivityKey == null || "".equals(this.currentActivityKey)) {
			this.currentActivityKey = DEFAULT_ACTIVITY;
		}
		this.currentActivityKey = this.manager.computeNextState(this.currentActivityKey, request);
		this.currentActivity = this.activities.get(this.currentActivityKey);
		if (currentActivity != null) {
			this.setEvents();
			this.currentActivity.init(context);
			this.currentActivity.start(this.view.getCanvas(), this.bus);
		}
	}

	/**
	 * Register an activity
	 * 
	 * @param key
	 *            the key under which to register the activity
	 * @param activity
	 *            the activity to register
	 */
	public void registerActivity(String key, CanvasActivity activity) {
		this.activities.put(key, activity);
	}

	/**
	 * Sets the default activity
	 * 
	 * @param activity
	 *            the activity to use as default
	 */
	public void setDefaultActivity(CanvasActivity activity) {
		this.activities.put(DEFAULT_ACTIVITY, activity);
	}

	/**
	 * Start this CanvasActivityManager and display the default activity
	 */
	public void start() {
		this.currentActivity = this.activities.get(this.currentActivityKey);
		if (this.currentActivity != null) {
			this.currentActivity.start(this.view.getCanvas(), this.bus);
		}
	}

	/**
	 * set events of the canvas to the current activity
	 */
	private void setEvents() {
		final Canvas canvas = this.view.getCanvas();
		canvas.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				currentActivity.onClick(event);
			}
		});
		canvas.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				currentActivity.onFocus(event);
			}
		});
		canvas.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				currentActivity.onBlur(event);
				canvas.setFocus(true);
			}
		});
		canvas.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				currentActivity.onKeyPress(event);
			}
		});
		canvas.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				currentActivity.onKeyDown(event);
			}
		});

		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				currentActivity.onMouseMove(event);
			}
		});
	}
}
