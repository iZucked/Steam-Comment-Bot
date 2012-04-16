/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.evaluation.impl;

import java.util.WeakHashMap;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.evaluation.IEvaluationService;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;

/**
 * A simple implementation of {@link IEvaluationService}. Uses a weak hashmap to track which
 * scenarios are being evaluated at the moment.
 * 
 * TODO fix locking race condition around transformer.
 * 
 * @author hinton
 *
 */
public class EvaluationService implements IEvaluationService {
	private final WeakHashMap<MMXRootObject, Thread> evaluators = new WeakHashMap<MMXRootObject, Thread>();
	
	@Override
	public synchronized void evaluate(final MMXRootObject scenario, final IProgressMonitor monitor) {
		final Thread existingThread = evaluators.get(scenario);
		
		final Thread newThread = new Thread(new Evaluator(existingThread, scenario));
		evaluators.put(scenario, newThread);
		newThread.start();
	}
	
	private class Evaluator implements Runnable {
		private Thread head;
		private MMXRootObject rootObject;

		public Evaluator(final Thread head, final MMXRootObject rootObject) {
			this.head = head;
			this.rootObject = rootObject;
		}
		
		@Override
		public void run() {
			try {
				if (head != null) {
					head.interrupt();
					head.join();
				}
				//TODO fix race in here; submitted object may be modified by editor during the beginning of evaluation,
				//	   which would be less than ideal.
				//     this is where the scenario service would be useful.
				evaluate();
			} catch (InterruptedException e) {
				return;
			}
		}
		
		protected void evaluate() {
			LNGTransformer transformer = new LNGTransformer(rootObject);

			IOptimisationData data = transformer.getOptimisationData();
			
			final OptimisationTransformer ot = transformer.getOptimisationTransformer();
			final Pair<IOptimisationContext, LocalSearchOptimiser> optAndContext = ot.createOptimiserAndContext(data, transformer.getEntities());

			final IOptimisationContext context = optAndContext.getFirst();
			final LocalSearchOptimiser optimiser = optAndContext.getSecond();

			optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());

			optimiser.init();
			optimiser.start(context);

			final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
			exporter.addPlatformExporterExtensions();
			final Schedule schedule = exporter.exportAnnotatedSolution(rootObject, transformer.getEntities(), optimiser.getBestSolution(true));

			final ScheduleModel scheduleModel = rootObject.getSubModel(ScheduleModel.class);
			scheduleModel.setInitialSchedule(schedule);
		}
	}
}
