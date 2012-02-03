package com.sfeir.canvas.util.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.CanPlayThroughEvent;
import com.google.gwt.event.dom.client.CanPlayThroughHandler;
import com.google.gwt.event.dom.client.EndedEvent;
import com.google.gwt.event.dom.client.EndedHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.sfeir.canvas.util.client.event.AllResourcesLoadedEvent;
import com.sfeir.canvas.util.client.event.ResourceLoadedEvent;

/**
 * Class used to manage resources. <br>
 * 
 * It will be used to store all resources, load them asynchronously, send events
 * when loading and, at last, make these resources available to other resources
 * in the application <br>
 * <br>
 * <span style="color: red;font-decoration: underline; font-weight: bold;">
 * Important note on audio : in order to have the loading percentage work
 * correctly, you MUST give the browser sources that are compatible with it, so
 * don't forget to supply sources in the different formats to be compatible with
 * all. </span>
 * 
 * @author François LAROCHE
 */
public class ResourceManager {

	/**
	 * Class defined to load a single resource and know when it is loaded
	 * 
	 * @author François LAROCHE
	 * @param <T>
	 *            the kind of widget needing to be loaded, for instance Image,
	 *            Audio, Video
	 */
	private abstract class ResourceLoader<T extends Widget> {

		/**
		 * The size of the resource
		 */
		private final int size;

		/**
		 * Constructor initializing the size of the resource
		 * 
		 * @param size
		 *            the size of the resource
		 */
		public ResourceLoader(int size) {
			this.size = size;
		}

		/**
		 * Abstract function used to get the widget associated with this
		 * resource, to be loaded asynchronously
		 * 
		 * @return the widget corresponding to this resource
		 */
		protected abstract T getElement();

		/**
		 * Function called after the widget associated with this resource has
		 * been added to the DOM of the page, and is beginning to be loaded
		 */
		protected abstract void afterLoad();

		/**
		 * Loads the resource, adding it to the DOM of the page
		 */
		public void load() {
			loaderPanel.add(getElement());
			afterLoad();
		}

		/**
		 * Returns the size of this resource
		 * 
		 * @return the size of the resource, as configured by the user
		 */
		public int getSize() {
			return size;
		}
	}

	/**
	 * Implementation of the {@link ResourceLoader} for images
	 * 
	 * @author François LAROCHE
	 */
	private class ImageResourceLoader extends ResourceLoader<Image> {
		/**
		 * The Image widget associated with this loader
		 */
		private final Image image;
		/**
		 * the URL of the image
		 */
		private final String url;

		/**
		 * Constructor initializing fields
		 * 
		 * @param url
		 *            the url of the image
		 * @param size
		 *            the size of the image
		 */
		public ImageResourceLoader(String url, int size) {
			super(size);
			this.image = new Image();
			this.image.addLoadHandler(new LoadHandler() {
				@Override
				public void onLoad(LoadEvent event) {
					addLoadedSize(getSize());
					bus.fireEvent(new ResourceLoadedEvent(getLoadedPercentage(), image));
				}
			});
			this.url = url;
		}

		@Override
		protected void afterLoad() {
			this.image.setUrl(this.url);
		}

		@Override
		protected Image getElement() {
			return image;
		}
	}

	/**
	 * Implementation of the {@link ResourceLoader} for audio. <br>
	 * The specificity of audio elements is that there can be several resources
	 * for the same audio.
	 * 
	 * @author François LAROCHE
	 */
	private class AudioResourceLoader extends ResourceLoader<Audio> {
		/**
		 * The audio widget managed by this loader
		 */
		private final Audio audio;
		/**
		 * the list of sources corresponding to this audio
		 */
		private List<String> urls;

		/**
		 * Constructor initializing the size
		 * 
		 * @param size
		 *            the size of the audio
		 */
		public AudioResourceLoader(int size) {
			super(size);
			this.urls = new ArrayList<String>();
			this.audio = Audio.createIfSupported();
			this.audio.addCanPlayThroughHandler(new CanPlayThroughHandler() {

				@Override
				public void onCanPlayThrough(CanPlayThroughEvent event) {
					// Do not wait for audio, some browsers can't handle it
					addLoadedSize(getSize());
				}
			});
			this.audio.addEndedHandler(new EndedHandler() {
				@Override
				public void onEnded(EndedEvent event) {
					audio.load();
				}
			});
		}

		@Override
		protected void afterLoad() {
			for (String url : this.urls) {
				this.audio.addSource(url);
			}
			this.audio.load();
		}

		@Override
		protected Audio getElement() {
			return audio;
		}

		/**
		 * Adds a source for this audio element
		 * 
		 * @param url
		 *            the url of the source to add
		 */
		public void addUrl(String url) {
			this.urls.add(url);
		}
	}

	/**
	 * Loaders for images
	 */
	private final Map<String, ImageResourceLoader> imageLoaders;
	/**
	 * Loaders for audio
	 */
	private final Map<String, AudioResourceLoader> audioLoaders;
	/**
	 * Panel that will be used to store all the elements. The elements need to
	 * be attached to the DOM to begin loading
	 */
	private final HasWidgets loaderPanel;
	/**
	 * the size of the resources currently loaded
	 */
	private int loadedSize;
	/**
	 * the total size of the resources
	 */
	private int totalSize;
	/**
	 * the bus on which to throw events
	 */
	private final EventBus bus;
	/**
	 * whether this manager has already fired the final event or not
	 */
	private boolean hasFired;

	/**
	 * Constructor initializing the fields
	 * 
	 * @param loaderPanel
	 *            the panel in which to load the data
	 * @param bus
	 *            the bus on which to fire the events
	 */
	public ResourceManager(HasWidgets loaderPanel, EventBus bus) {
		this.loaderPanel = loaderPanel;
		this.bus = bus;
		this.hasFired = false;
		this.imageLoaders = new HashMap<String, ImageResourceLoader>();
		this.audioLoaders = new HashMap<String, AudioResourceLoader>();
	}

	/**
	 * register an image in this {@link ResourceManager}
	 * 
	 * @param key
	 *            the key of the image in this {@link ResourceManager}
	 * @param url
	 *            the url of the image
	 * @param size
	 *            the size of the image. This size can be arbitrary. For
	 *            example, you can decide that all resources have a size of "1",
	 *            or have all of them relative to the real sizes
	 */
	public void registerImage(String key, String url, int size) {
		this.imageLoaders.put(key, new ImageResourceLoader(url, size));
	}

	/**
	 * Register an audio or a new source for an existing audio in this
	 * {@link ResourceManager}
	 * 
	 * @param key
	 *            the key of the audio in this {@link ResourceManager}
	 * @param url
	 *            the url of the source
	 * @param size
	 *            the size of audio. This size can be arbitrary. For example,
	 *            you can decide that all resources have a size of "1", or have
	 *            all of them relative to the real sizes
	 */
	public void registerAudio(String key, String url, int size) {
		if (!this.audioLoaders.containsKey(key)) {
			this.audioLoaders.put(key, new AudioResourceLoader(size));
		}
		AudioResourceLoader loader = this.audioLoaders.get(key);
		loader.addUrl(url);
	}

	/**
	 * gets an Image that has been registered
	 * 
	 * @param key
	 *            the key the wanted image is registered under
	 * @return the Image associated to the key
	 */
	public Image getImage(String key) {
		ResourceLoader<Image> image = this.imageLoaders.get(key);
		if (image != null) {
			return image.getElement();
		}
		return null;
	}

	/**
	 * get an Audio that has been registered
	 * 
	 * @param key
	 *            the key under which the audio has been registered
	 * @return the audio corresponding to the key
	 */
	public Audio getAudio(String key) {
		ResourceLoader<Audio> audio = this.audioLoaders.get(key);
		if (audio != null) {
			return audio.getElement();
		}
		return null;
	}

	/**
	 * Pre-load the resources
	 */
	public void preloadResources() {
		this.loadedSize = 0;
		this.totalSize = 0;
		this.hasFired = false;
		this.loaderPanel.clear();

		for (ResourceLoader<Image> loader : this.imageLoaders.values()) {
			loader.load();
			this.totalSize += loader.getSize();
		}
		if (Audio.isSupported()) {
			for (ResourceLoader<Audio> loader : this.audioLoaders.values()) {
				loader.load();
				this.totalSize += loader.getSize();
			}
		}
	}

	private void addLoadedSize(int size) {
		this.loadedSize += size;
		if (this.loadedSize >= totalSize && !this.hasFired) {
			this.hasFired = true;
			this.bus.fireEvent(new AllResourcesLoadedEvent());
		}
	}

	/**
	 * Gets the loading percentage. This percentage is between 0 and 1.
	 * 
	 * @return the loading percentage
	 */
	public float getLoadedPercentage() {
		if (this.totalSize == 0) {
			return 0;
		}
		return (float) this.loadedSize / (float) this.totalSize;
	}
}
