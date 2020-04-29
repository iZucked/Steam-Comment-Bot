package com.mmxlabs.rcp.common.ecore;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link EContentAdapter} subclass with a try/catch around the notification
 * call to handle exceptions. By default this will send exceptions to the
 * logger, but this can be overridden. Note, super.notifyChanged is called by
 * this call and sub classes do not need to do this.
 * 
 * @author Simon Goodall
 *
 */
public abstract class SafeEContentAdapter extends EContentAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SafeEContentAdapter.class);

	@Override
	public final void notifyChanged(final Notification msg) {
		super.notifyChanged(msg);

		try {
			safeNotifyChanged(msg);
		} catch (final Exception e) {
			handleException(e);
		}
	}

	/**
	 * Note, super.notifyChanged has already been called before this method is
	 * invoked.
	 * 
	 * @param msg
	 */
	protected abstract void safeNotifyChanged(Notification msg);

	protected void handleException(final Exception e) {
		LOGGER.error(e.getMessage(), e);
	}
}
