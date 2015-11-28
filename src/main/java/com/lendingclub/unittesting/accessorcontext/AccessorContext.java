package com.lendingclub.unittesting.accessorcontext;

import com.lendingclub.unittesting.domainexamples.DongleAccessor;
import com.lendingclub.unittesting.domainexamples.WidgetAccessor;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Holder for all accessor classes.
 *
 * Accessor classes are stateless classes that contain various methods for accessing objects
 * of a particular type, e.g. WidgetAccessor accesses Widget objects. These were previously static
 * methods on the classes but that made unit testing and mocking difficult.
 *
 * There is a stack of AccessorContexts kept in a ThreadLocal. Under normal circumstances there
 * will be nothing in the ThreadLocal and so a single static instance will be used (it's fine
 * to share instances across threads because the Accessor classes have no state). At the
 * moment the only code that pushes new contexts is tests, but one could imagine cases to do it
 * in production code in the future (though I can't offhand think of what they would be!).
 *
 * To establish a new AccessorContext call AccessorContext.establish(). Be sure to release it
 * when you're done. You can do that in a junit After method or in a finally or as a JDK7
 * AutoCloseable in a try-with.
 *
 * The non-standard getter names (e.g. widget() instead of getWidgetAccessor()) is to make boilerplate
 * calling code more compact, e.g.
 *
 * 	AccessorContext.get().widget().getWidgetById()
 *
 * instead of
 *
 * 	AccessorContext.get().getWidgetAccessor().getWidgetById()
 *
 */
public abstract class AccessorContext implements AutoCloseable {

	private static final AccessorContext DEFAULT = new FinalAccessorContext();
	private static final ThreadLocal<Deque<AccessorContext>> THREAD_LOCAL = new ThreadLocal<>();

	private static class FinalAccessorContext extends AccessorContext {
		private final DongleAccessor dongle = new DongleAccessor();
		private final WidgetAccessor widget = new WidgetAccessor();

		public DongleAccessor dongle() {
			return this.dongle;
		}

		public WidgetAccessor widget() {
			return this.widget;
		}
	}

	/**
	 * @return the current AccessorContext. If one has not been established with the establish() method,
	 * it will return the default context.
	 */
	public static AccessorContext get() {
		Deque<AccessorContext> result = THREAD_LOCAL.get();
		if (result != null && !result.isEmpty()) return result.peek();
		return DEFAULT;
	}

	/**
	 * Push the specified AccessorContext onto the stack and use it as the current one (i.e. the one that get()
	 * will return) in this thread.
	 *
	 * IMPORTANT: be sure to release this context when you're done with it, either in a junit After
	 * method or a finally block or a finally-with.
	 */
	public static AccessorContext establish(AccessorContext context) {
		Deque<AccessorContext> stack = THREAD_LOCAL.get();
		if (stack == null) {
			stack = new LinkedList<>();
			THREAD_LOCAL.set(stack);
		}
		stack.push(context);
		return context;
	}

	/**
	 * Push this AccessorContext onto the stack and use it as the current one (i.e. the one that get()
	 * will return) in this thread.
	 *
	 * IMPORTANT: be sure to release this context when you're done with it, either in a junit After
	 * method or a finally block or a finally-with.
	 */
	public AccessorContext establish() {
		establish(this);
		return this;
	}

	/**
	 * Pop this AccessorContext off the current thread's stack.
	 *
	 * @throws IllegalStateException if this is not the head of the stack
	 */
	public void release() {
		Deque<AccessorContext> stack = THREAD_LOCAL.get();
		if (stack == null || stack.isEmpty()) {
			throw new IllegalStateException("AccessorContext.release() called with no context to release!");
		}
		if (stack.peek() != this) {
			throw new IllegalStateException("this (" + this + ") is not the current context");
		}
		stack.pop();
		if (stack.isEmpty()) THREAD_LOCAL.set(null);
	}

	/**
	 * Implements AutoCloseable.close(). Same as release().
	 */
	public final void close() {
		release();
	}

	// these are all abstract because i want the members to be not-final in MockableAccessorContext
	// and final under normal circumstances. i could override these methods in MockableAccessorContext
	// but then there's the chance that code in here accesses the members directly instead of
	// calling the getters

	/**
	 * Getter for DongleAccessor
	 */
	public abstract DongleAccessor dongle();

	/**
	 * Getter for WidgetAccessor
	 */
	public abstract WidgetAccessor widget();


}

