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
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

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
	private final Evaluator evaluator = new Evaluator();

	private boolean enabled = true;
	
	public LiveEvaluator(final ScenarioInstance instance) {
		this.instance = instance;
	}

	@Override
	public void reallyNotifyChanged(final Notification notification) {
		if (notification.getFeature() == SchedulePackage.eINSTANCE.getScheduleModel_Dirty()) {
			if (notification.getEventType() == Notification.SET && notification.getNewBooleanValue()) {
				queueEvaluate();
//			} else if (notification.getEventType() == Notification.SET && notification.getNewBooleanValue() == false) {
//				dequeueEvaluate();
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
		if (!enabled) {
			return;
		}
		
		if (evaluatorThread == null || !evaluatorThread.isAlive()) {
			evaluatorThread = new Thread(evaluator , "Live Evaluator [" + instance.getName() + "]");
			evaluatorThread.start();
		} else {
			evaluatorThread.interrupt();
		}
	}
	
	private void dequeueEvaluate() {
		if (evaluatorThread != null && evaluatorThread.isAlive()) {
			evaluator.kill();
			evaluatorThread.interrupt();
		}
	}

	private class Evaluator implements Runnable {
		private boolean kill = false;
		public void kill() {
			kill = true;
		}
		
		@Override
		public void run() {
			log.debug("Waiting 2s to evaluate " + instance.getName());
			boolean spinLock = true;
			while (spinLock && enabled) {
				try {
					spinLock = false;
					Thread.sleep(2000);
					
					final IScenarioInstanceEvaluator evaluator = AnalyticsEditorPlugin.getPlugin().getResourceEvaluator();
					if (evaluator != null) {
						if (instance.getLock(ScenarioLock.EVALUATOR).claim()) {
							try {
								log.debug("Checking dirty flag is still set");
								final MMXRootObject root = (MMXRootObject) instance.getScenarioService().load(instance);
								final ScheduleModel subModel = root.getSubModel(ScheduleModel.class);
								if (!subModel.isDirty()) return;
								log.debug("About to evaluate " + instance.getName());
								evaluator.evaluate(instance);								
							} catch (final Throwable th) {
								
							} finally {
								instance.getLock(ScenarioLock.EVALUATOR).release();
							}
						} else {
							log.debug("Didn't get lock, spinning");
							spinLock = true;
						}
					} else {
						log.debug("Could not find evaluator when evaluating " + instance.getName());
					}
				} catch (final InterruptedException e) {
					if (kill) {
						spinLock = false;
					}
					else spinLock = true;
				}
			}
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			// We we re-enable check the dirty status and queue an evaluation if it is dirty 
			final MMXRootObject root = (MMXRootObject) instance.getInstance();
			if (root != null) {
				final ScheduleModel subModel = root.getSubModel(ScheduleModel.class);
				if (subModel.isDirty()) {
					queueEvaluate();
				}
			}
		}
	}
}
