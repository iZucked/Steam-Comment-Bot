/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.mmxcore.IMMXAdapter;

public abstract class SafeMMXContentAdapter extends EContentAdapter implements IMMXAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SafeMMXContentAdapter.class);

	private final List<Notification> missedNotifications = new LinkedList<>();
	/**
	 * @since 2.2
	 */
	protected final HashSet<EStructuralFeature> ignoredFeatures = new HashSet<>();

	public SafeMMXContentAdapter() {
	}

	@Override
	public synchronized void notifyChanged(final Notification notification) {
		super.notifyChanged(notification);
		if (ignoredFeatures.contains(notification.getFeature())) {
			return;
		}
		if (enabled) {
			try {
				reallyNotifyChanged(notification);
			} catch (Exception e) {
				handleException(e);
			}
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

	public synchronized void enable(final boolean skip) {
		if (!missedNotifications.isEmpty()) {
			if (!skip) {
				try {
					missedNotifications(Collections.unmodifiableList(missedNotifications));
				} catch (Exception e) {
					handleException(e);
				}
			}
			missedNotifications.clear();
		}
		enabled = true;
	}

	protected void handleException(final Exception e) {
		LOGGER.error(e.getMessage(), e);
	}
}
