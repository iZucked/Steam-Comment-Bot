/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.ecore;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link AdapterImpl} subclass with a try/catch around the notification call to
 * handle exceptions. By default this will send exceptions to the logger, but
 * this can be overridden.
 * 
 * @author Simon Goodall
 *
 */
public abstract class SafeAdapterImpl extends AdapterImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(SafeAdapterImpl.class);

	@Override
	public final void notifyChanged(final Notification msg) {
		super.notifyChanged(msg);

		try {
			safeNotifyChanged(msg);
		} catch (final Exception e) {
			handleException(e);
		}
	}

	protected abstract void safeNotifyChanged(Notification msg);

	protected void handleException(final Exception e) {
		LOGGER.error(e.getMessage(), e);
	}
}
