package com.lendingclub.unittesting.accessorcontext;

import com.lendingclub.unittesting.domainexamples.DongleAccessor;
import com.lendingclub.unittesting.domainexamples.WidgetAccessor;

/**
 * Implementation of AccessorContext for testing purposes that allows
 * you to change the various Accessors.
 */
public class MockableAccessorContext extends AccessorContext {
	private WidgetAccessor widget = new WidgetAccessor();
	private DongleAccessor dongle = new DongleAccessor();

	public WidgetAccessor widget() {
		return this.widget;
	}

	public DongleAccessor dongle() {
		return this.dongle;
	}

	// setters
	
	public DongleAccessor setDongle(DongleAccessor dongle) {
		this.dongle = dongle;
		return dongle;
	}
	
	public WidgetAccessor setWidget(WidgetAccessor widget) {
		this.widget = widget;
		return widget;
	}
	
	/**
	 * Push this AccessorContext onto the stack and use it as the current one (i.e. the one that get()
	 * will return) in this thread.
	 * 
	 * IMPORTANT: be sure to release this context when you're done with it, either in a junit After
	 * method or a finally block or a finally-with.
	 */
	public MockableAccessorContext establish() {
		establish(this);
		return this;
	}
	
	/**
	 * Push a new MockableAccessorContext onto the stack and use it as the current one (i.e. the one that get()
	 * will return) in this thread.
	 * 
	 * IMPORTANT: be sure to release this context when you're done with it, either in a junit After
	 * method or a finally block or a try-with.
	 */
	public static MockableAccessorContext establishMockable() {
		MockableAccessorContext context = new MockableAccessorContext();
		establish(context);
		return context;
	}
	
	/**
	 * @return the current AccessorContext. 
	 * @throws IllegalStateException if the current context is not a MockableAccessorContext
	 */
	public static MockableAccessorContext get() {
		AccessorContext context = AccessorContext.get();
		if (!(context instanceof MockableAccessorContext)) throw new IllegalStateException("no MockableAccessorContext established");
		return (MockableAccessorContext) context;
	}
	
}