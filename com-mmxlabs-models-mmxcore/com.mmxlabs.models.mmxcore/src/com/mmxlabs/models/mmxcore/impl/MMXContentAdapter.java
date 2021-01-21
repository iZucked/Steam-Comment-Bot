/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;

import com.mmxlabs.models.mmxcore.IMMXAdapter;

public abstract class MMXContentAdapter extends EContentAdapter implements IMMXAdapter {
	private final List<Notification> missedNotifications = new LinkedList<>();
	/**
	 * @since 2.2
	 */
	protected final HashSet<EStructuralFeature> ignoredFeatures = new HashSet<>();

	public MMXContentAdapter() {
	}

	@Override
	public synchronized void notifyChanged(final Notification notification) {
		super.notifyChanged(notification);
		if (ignoredFeatures.contains(notification.getFeature())) {
			return;
		}
		if (enabled) {
			reallyNotifyChanged(notification);
		} else {
			missedNotifications.add(notification);
		}
	}

	/**
	 * @since 2.0
	 */
	public abstract void reallyNotifyChanged(final Notification notification);

	/**
	 * @since 2.2
	 */
	protected synchronized void missedNotifications(final List<Notification> missed) {

	}

	boolean enabled = true;

	@Override
	public synchronized void disable() {
		enabled = false;
		missedNotifications.clear();
	}

	@Override
	public synchronized void enable() {
		enable(false);
	}

	/**
	 * @since 2.2
	 */
	public synchronized void enable(final boolean skip) {
		if (missedNotifications.isEmpty() == false) {
			if (!skip) {
				missedNotifications(Collections.unmodifiableList(missedNotifications));
			}
			missedNotifications.clear();
		}
		enabled = true;
	}
}
