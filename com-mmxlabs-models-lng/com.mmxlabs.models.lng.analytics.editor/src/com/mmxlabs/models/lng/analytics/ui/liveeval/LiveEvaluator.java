/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.analytics.presentation.AnalyticsEditorPlugin;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Updates schedule when their dirty bit is set.
 * 
 * @author hinton
 * 
 */
public class LiveEvaluator extends MMXAdapterImpl {
	private static final Logger log = LoggerFactory.getLogger(LiveEvaluator.class);
	private Thread evaluatorThread = null;
	private final ScenarioInstance instance;

	public LiveEvaluator(final ScenarioInstance instance) {
		this.instance = instance;
	}

	@Override
	public void reallyNotifyChanged(final Notification notification) {
		if (notification.getFeature() == SchedulePackage.eINSTANCE.getScheduleModel_Dirty()) {
			if (notification.getEventType() == Notification.SET && notification.getNewBooleanValue()) {
				queueEvaluate();
			}
		}
	}

	@Override
	protected void missedNotifications(final List<Notification> missed) {
		// Re-process missed notifications
		final List<Notification> copied = new ArrayList<Notification>(missed);
		for (final Notification n : copied) {
			reallyNotifyChanged(n);
		}
	}

	private void queueEvaluate() {
		if (evaluatorThread == null || !evaluatorThread.isAlive()) {
			evaluatorThread = new Thread(new Evaluator(), "Live Evaluator");
			evaluatorThread.start();
		} else {
			evaluatorThread.interrupt();
		}
	}

	private class Evaluator implements Runnable {
		@Override
		public void run() {
			log.debug("Waiting 2s to evaluate " + instance.getName());
			boolean spinLock = true;
			while (spinLock) {
				try {
					spinLock = false;
					Thread.sleep(2000);

					final IScenarioInstanceEvaluator evaluator = AnalyticsEditorPlugin.getPlugin().getResourceEvaluator();
					if (evaluator != null) {
						synchronized (instance) {
							log.debug("About to evaluate " + instance.getName());
							evaluator.evaluate(instance);
						}
					} else {
						log.debug("Could not find evaluator when evaluating " + instance.getName());
					}
				} catch (final InterruptedException e) {
					spinLock = true;
				}
			}
		}
	}
}
