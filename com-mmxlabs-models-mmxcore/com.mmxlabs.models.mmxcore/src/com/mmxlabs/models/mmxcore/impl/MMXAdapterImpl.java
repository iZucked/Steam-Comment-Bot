/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import com.mmxlabs.models.mmxcore.IMMXAdapter;

public abstract class MMXAdapterImpl extends AdapterImpl implements IMMXAdapter {
	private boolean missed = false;
	@Override
	public void notifyChanged(final Notification notification) {
		if (enabled) reallyNotifyChanged(notification);
		else missed = true;
	}
	
	public abstract void reallyNotifyChanged(final Notification notification);
	
	protected void missedNotification() {
		
	}
	
	boolean enabled = true;
	@Override
	public void disable() {
		missed = false;
		enabled = false;
	}

	@Override
	public void enable() {
		if (missed) {
			missedNotification();
		}
		missed = false;
		enabled = true;
	}
}
