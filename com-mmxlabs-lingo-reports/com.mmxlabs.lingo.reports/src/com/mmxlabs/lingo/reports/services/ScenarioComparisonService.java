/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.PlatformUI;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.lingo.reports.components.ReportComponentModule;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandler;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandlerExtension;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioComparisonService {

	@Inject(optional = true)
	private Iterable<ICustomRelatedSlotHandlerExtension> customRelatedSlotHandlerExtensions;

	private static final Logger log = LoggerFactory.getLogger(ScenarioComparisonService.class);

	private SelectedScenariosService selectedScenarioService;
	private final Set<IScenarioComparisonServiceListener> listeners = new HashSet<>();

	private final ScheduleDiffUtils scheduleDiffUtils = new ScheduleDiffUtils();

	private DiffOptions diffOptions = ScheduleReportFactory.eINSTANCE.createDiffOptions();

	/**
	 * Special counter to try and avoid multiple update requests happening at once. TODO: What happens if we hit Integer.MAX_VALUE?
	 */
	private AtomicInteger counter = new AtomicInteger();

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioInstance pinned, final Collection<ScenarioInstance> others, final boolean block) {
			scheduleRebuildCompare(selectedDataProvider, pinned, others, block);
		}
	};

	private ScenarioComparisonServiceTransformer.TransformResult lastResult;

	// FIXME: This can retain elements from unloaded scenarios.
	private Collection<Object> selectedElements = new LinkedList<>();

	public ScenarioComparisonService() {
		final Injector injector = Guice.createInjector(Peaberry.osgiModule(Activator.getDefault().getBundle().getBundleContext(), EclipseRegistry.eclipseRegistry()), new ReportComponentModule());
		injector.injectMembers(this);
	}

	public void bindSelectedScenariosService(final SelectedScenariosService selectedScenariosService) {
		if (this.selectedScenarioService != null) {
			unbindSelectedScenariosService(this.selectedScenarioService);
		}
		this.selectedScenarioService = selectedScenariosService;

		if (this.selectedScenarioService != null) {
			selectedScenariosService.addListener(selectedScenariosServiceListener);
		}
	}

	public void unbindSelectedScenariosService(final SelectedScenariosService selectedScenariosService) {
		if (this.selectedScenarioService == null) {
			return;
		}
		if (this.selectedScenarioService == selectedScenariosService) {
			selectedScenariosService.removeListener(selectedScenariosServiceListener);
			this.selectedScenarioService = null;
		}
	}

	public void dispose() {
		unbindSelectedScenariosService(this.selectedScenarioService);

	}

	private void scheduleRebuildCompare(@NonNull final ISelectedDataProvider selectedDataProvider, @Nullable final ScenarioInstance pinned, @NonNull final Collection<ScenarioInstance> others,
			final boolean block) {
		lastResult = null;
		final int value = counter.incrementAndGet();
		if (PlatformUI.isWorkbenchRunning()) {

			Runnable r = new Runnable() {

				@Override
				public void run() {
					// Mismatch, assume pending job
					if (value != counter.get()) {
						return;
					}
					doRebuildCompare(value, selectedDataProvider, pinned, others);
				}
			};
			RunnerHelper.exec(r, block);
		}

	}

	private void doRebuildCompare(final int value, @NonNull final ISelectedDataProvider selectedDataProvider, @Nullable final ScenarioInstance pinned,
			@NonNull final Collection<ScenarioInstance> others) {

		final List<ICustomRelatedSlotHandler> customRelatedSlotHandlers = new LinkedList<>();
		if (customRelatedSlotHandlerExtensions != null) {
			for (final ICustomRelatedSlotHandlerExtension h : customRelatedSlotHandlerExtensions) {
				customRelatedSlotHandlers.add(h.getInstance());
			}
		}

		ScenarioComparisonServiceTransformer.TransformResult result = ScenarioComparisonServiceTransformer.transform(pinned, others, selectedDataProvider, scheduleDiffUtils, customRelatedSlotHandlers);
		result.selectedDataProvider = selectedDataProvider;
		final Table table = result.table;

		// Take a copy of current diff options
		table.setOptions(EcoreUtil.copy(diffOptions));

		for (final IScenarioComparisonServiceListener l : listeners) {
			try {
				if (pinned != null) {
					l.compareDataUpdate(selectedDataProvider, pinned, others.iterator().next(), table, result.rootObjects, result.equivalancesMap);
				} else {
					l.multiDataUpdate(selectedDataProvider, others, table, result.rootObjects);
				}
			} catch (final Exception e) {
				// Counter mismatch? data probably been changed, ignore exceptions in this case. However if counter matches, log the exception.
				if (value == counter.get()) {
					log.error(e.getMessage(), e);
				}
			}
		}

		lastResult = result;
	}

	public void addListener(final IScenarioComparisonServiceListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(final IScenarioComparisonServiceListener listener) {
		this.listeners.remove(listener);
	}

	public void triggerListener(IScenarioComparisonServiceListener l) {
		if (lastResult != null) {
			try {
				if (lastResult.pinned != null) {
					l.compareDataUpdate(lastResult.selectedDataProvider, lastResult.pinned, lastResult.others.iterator().next(), lastResult.table, lastResult.rootObjects, lastResult.equivalancesMap);
				} else {
					l.multiDataUpdate(lastResult.selectedDataProvider, lastResult.others, lastResult.table, lastResult.rootObjects);
				}
			} catch (final Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public void toggleDiffOption(EDiffOption option) {
		switch (option) {
		case FILTER_SCHEDULE_CHART_BY_SELECTION:
			diffOptions.setFilterSelectedSequences(!diffOptions.isFilterSelectedSequences());
			break;
		case FILTER_SCHEDULE_SUMMARY_BY_SELECTION:
			diffOptions.setFilterSelectedElements(!diffOptions.isFilterSelectedElements());
			break;
		}
		if (lastResult != null) {
			scheduleRebuildCompare(lastResult.selectedDataProvider, lastResult.pinned, lastResult.others, false);
		} else {
			// Invalidate any existing jobs
			counter.getAndIncrement();
		}
	}

	@Deprecated
	public DiffOptions getDiffOptions() {
		return diffOptions;
	}

	public void setSelectedElements(Collection<Object> selectedElements) {
		this.selectedElements.clear();
		this.selectedElements.addAll(selectedElements);
	}

	public Collection<Object> getSelectedElements() {
		return selectedElements;
	}
}
