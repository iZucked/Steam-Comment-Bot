/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

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

	private final ExecutorService executor;

	private boolean enabled = true;

	private IScenarioInstanceEvaluator scenarioInstanceEvaluator;

	public LiveEvaluator(final ScenarioInstance instance, final ExecutorService executor) {
		this.instance = instance;
		this.executor = executor;
	}

	@Override
	public void reallyNotifyChanged(final Notification notification) {
		if (notification.getFeature() == SchedulePackage.eINSTANCE.getScheduleModel_Dirty()) {
			if (notification.getEventType() == Notification.SET && notification.getNewBooleanValue()) {
				queueEvaluate();
				// } else if (notification.getEventType() == Notification.SET && notification.getNewBooleanValue() == false) {
				// dequeueEvaluate();
			}
		} else if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_ValidationStatusCode()) {
			// If error validation improves from an error status, queue a validation as previously it would not have been queued.
			if (notification.getOldIntValue() == IStatus.ERROR && notification.getNewIntValue() != IStatus.ERROR) {
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
		if (!enabled) {
			return;
		}

		// Do not bother to evaluate if there is an error.
		if (instance.getValidationStatusCode() == IStatus.ERROR) {
			return;
		}

		if (evaluatorThread == null || !evaluatorThread.isAlive()) {
			evaluatorThread = new Thread(evaluator, "Live Evaluator [" + instance.getName() + "]");
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

					if (scenarioInstanceEvaluator != null) {

						// Perform some checks here - retry again after lock is claimed just in case....

						if (!enabled) {
							return;
						}
						// Do not bother to evaluate if there is an error.
						if (instance.getValidationStatusCode() == IStatus.ERROR) {
							return;
						}

						final ScenarioLock evaluatorLock = instance.getLock(ScenarioLock.EVALUATOR);
						try (final ModelReference modelReference = instance.getReference("LiveEvaluator")) {
							final LNGScenarioModel root = (LNGScenarioModel) modelReference.getInstance();
							if (root == null) {
								// No data model, skip
								return;
							}
							if (evaluatorLock.claim()) {
								// Submit request to queue
								executor.submit(new Callable<Object>() {
									@Override
									public Object call() throws Exception {
										log.debug("Checking evaluation is still required");
										// Check enabled state
										if (!enabled) {
											return null;
										}
										// Do not bother to evaluate if there is an error.
										if (instance.getValidationStatusCode() == IStatus.ERROR) {
											return null;
										}
										final ScheduleModel scheduleModel = root.getScheduleModel();
										if (!scheduleModel.isDirty()) {
											return null;
										}
										log.debug("About to evaluate " + instance.getName());
										scenarioInstanceEvaluator.evaluate(instance);

										return null;
									}
								});
							} else {
								// log.debug("Didn't get lock, spinning");
								spinLock = true;
							}
						} catch (final Throwable th) {

						} finally {
							evaluatorLock.release();
						}
					} else {
						log.debug("Could not find evaluator when evaluating " + instance.getName());
					}
				} catch (final InterruptedException e) {
					if (kill) {
						spinLock = false;
					} else {
						spinLock = true;
					}
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
			final LNGScenarioModel root = (LNGScenarioModel) instance.getInstance();
			if (root != null) {
				final ScheduleModel scheduleModel = root.getScheduleModel();
				if (scheduleModel.isDirty()) {
					queueEvaluate();
				}
			}
		}
	}

	public IScenarioInstanceEvaluator getScenarioInstanceEvaluator() {
		return scenarioInstanceEvaluator;
	}

	public void setScenarioInstanceEvaluator(final IScenarioInstanceEvaluator scenarioInstanceEvaluator) {
		this.scenarioInstanceEvaluator = scenarioInstanceEvaluator;
	}
}
