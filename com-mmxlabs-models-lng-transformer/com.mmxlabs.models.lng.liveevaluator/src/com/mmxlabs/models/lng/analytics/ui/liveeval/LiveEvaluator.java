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

/**
 * Updates schedule when their dirty bit is set.
 * 
 * @author hinton
 *
 */
public class LiveEvaluator extends MMXAdapterImpl {
	private static final Logger log = LoggerFactory.getLogger(LiveEvaluator.class);
	private IResource resource;
	private Thread evaluatorThread = null;
	public LiveEvaluator(final IResource resource) {
		this.resource = resource;
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
			log.debug("Waiting 2s to evaluate " + resource.getName());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				run();
				return;
			}
			
			final IResourceEvaluator evaluator = AnalyticsEditorPlugin.getPlugin().getResourceEvaluator();
			if (evaluator != null) {
				final MMXRootObject rootObject = (MMXRootObject) resource.getAdapter(MMXRootObject.class);
				if (Thread.interrupted()) {
					run();
					return;
				}
				synchronized (rootObject) {
					log.debug("About to evaluate " + resource.getName());
					evaluator.evaluate(resource);
				}
			} else {
				log.debug("Could not find evaluator when evaluating " + resource.getName());
			}
		}
	}
}
