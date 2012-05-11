/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.notify.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.analytics.presentation.AnalyticsEditorPlugin;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
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
	private ScenarioInstance instance;
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

	private void queueEvaluate() { 
		if (evaluatorThread == null || !evaluatorThread.isAlive()) {
			evaluatorThread = new Thread(new Evaluator());
			evaluatorThread.start();
		} else {
			evaluatorThread.interrupt();			
		}
	}
	
	private class Evaluator implements Runnable {
		@Override
		public void run() {
			log.debug("Waiting 2s to evaluate " + instance.getName());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				run();
				return;
			}
			
			final IScenarioInstanceEvaluator evaluator = AnalyticsEditorPlugin.getPlugin().getResourceEvaluator();
			if (evaluator != null) {
				if (Thread.interrupted()) {
					run();
					return;
				}
				synchronized (instance) {
					log.debug("About to evaluate " + instance.getName());
					evaluator.evaluate(instance);
				}
			} else {
				log.debug("Could not find evaluator when evaluating " + instance.getName());
			}
		}
	}
}
