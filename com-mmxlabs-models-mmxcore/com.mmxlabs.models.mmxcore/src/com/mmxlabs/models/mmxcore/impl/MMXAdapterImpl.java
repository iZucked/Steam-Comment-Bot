/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.mmxcore.IMMXAdapter;

public abstract class MMXAdapterImpl extends AdapterImpl implements IMMXAdapter {

	private List<Notification> missedNotifications = new LinkedList<>();

	/**
	 * @since 2.2
	 */
	protected final HashSet<EStructuralFeature> ignoredFeatures = new HashSet<>();

	private boolean enabled = true;

	@Override
	public void notifyChanged(final Notification notification) {
		if (ignoredFeatures.contains(notification.getFeature())) {
			return;
		}
		if (enabled) {
			reallyNotifyChanged(notification);
		} else {
			synchronized (missedNotifications) {
				missedNotifications.add(notification);
			}
		}
	}

	public abstract void reallyNotifyChanged(final Notification notification);

	/**
	 * @since 2.2
	 */
	protected void missedNotifications(final List<Notification> missed) {

	}

	@Override
	public void disable() {
		synchronized (missedNotifications) {
			missedNotifications.clear();
		}
		enabled = false;
	}

	@Override
	public void enable() {
		enable(false);
	}

	public void enable(final boolean skip) {
		final List<Notification> copy;
		synchronized (missedNotifications) {
			if (!skip) {
				copy = new ArrayList<>(missedNotifications);
			} else {
				copy = Collections.emptyList();
			}
			missedNotifications.clear();
		}
		if (!copy.isEmpty()) {
			if (!skip) {
				missedNotifications(copy);
			}
		}
		enabled = true;
	}
}
