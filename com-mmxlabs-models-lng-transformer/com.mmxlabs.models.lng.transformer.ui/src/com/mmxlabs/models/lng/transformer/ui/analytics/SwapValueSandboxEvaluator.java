/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.modules.ExporterExtensionsModule;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequences;

public class SwapValueSandboxEvaluator {
	protected static final Logger LOG = LoggerFactory.getLogger(SwapValueSandboxEvaluator.class);

	@Inject
	private Injector injector;

	@Nullable
	public Pair<Schedule, Schedule> evaluate(final ISequences baseSequences, final ISequences swapSequences, final ModelEntityMap modelEntityMap) {
		final Schedule baseSchedule;
		{
			final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();

			final List<Module> modules = new LinkedList<>();
			modules.add(new ExporterExtensionsModule());
			final Injector childInjector = injector.createChildInjector(modules);
			childInjector.injectMembers(exporter);

			final IAnnotatedSolution baseSolution = LNGSchedulerJobUtils.evaluateCurrentState(childInjector, baseSequences).getFirst();
			baseSchedule = exporter.exportAnnotatedSolution(modelEntityMap, baseSolution);
		}

		final Schedule swapSchedule;
		{
			final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();

			final List<Module> modules = new LinkedList<>();
			modules.add(new ExporterExtensionsModule());
			final Injector childInjector = injector.createChildInjector(modules);
			childInjector.injectMembers(exporter);

			final IAnnotatedSolution swapSolution = LNGSchedulerJobUtils.evaluateCurrentState(childInjector, swapSequences).getFirst();
			swapSchedule = exporter.exportAnnotatedSolution(modelEntityMap, swapSolution);
		}

		return Pair.of(baseSchedule, swapSchedule);
	}
}
