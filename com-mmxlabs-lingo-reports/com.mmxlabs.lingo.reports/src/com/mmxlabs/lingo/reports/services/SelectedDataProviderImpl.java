/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.ScenarioResult;

public class SelectedDataProviderImpl implements ISelectedDataProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectedDataProviderImpl.class);

	private final @NonNull List<ScenarioResult> allScenarioResults = new LinkedList<>();
	private final @NonNull List<ScenarioResult> otherScenarioResults = new LinkedList<>();
	private final @NonNull Map<EObject, ScenarioResult> scenarioResultMap = new HashMap<>();
	private final @NonNull Map<EObject, Schedule> scheduleMap = new HashMap<>();
	private ScenarioResult pinnedScenarioResult;

	private boolean diffToBase;
	private ChangeSetTableRoot changeSetRoot;
	private ChangeSetTableGroup changeSet;
	private Collection<ChangeSetTableRow> changeSetRows;

	public void addScenario(@NonNull final ScenarioResult scenarioResult) {
		final ScheduleModel scheduleModel = scenarioResult.getTypedResult(ScheduleModel.class);
		addScenario(scenarioResult, scheduleModel.getSchedule(), getChildren(scenarioResult));
	}

	public void addScenario(@NonNull final ScenarioResult scenarioResult, @Nullable final Schedule schedule, @NonNull final Collection<EObject> children) {

		allScenarioResults.add(scenarioResult);
		if (scenarioResult != pinnedScenarioResult) {
			otherScenarioResults.add(scenarioResult);
		}
		for (final EObject e : children) {
			scenarioResultMap.put(e, scenarioResult);
			scheduleMap.put(e, schedule);
		}
	}

	@Override
	public List<ScenarioResult> getAllScenarioResults() {
		return allScenarioResults;
	}

	@Override
	public List<ScenarioResult> getOtherScenarioResults() {
		return otherScenarioResults;
	}

	@Override
	public ScenarioResult getScenarioResult(final EObject eObject) {
		return scenarioResultMap.get(eObject);
	}

	@Override
	public Schedule getSchedule(final EObject eObject) {
		return scheduleMap.get(eObject);
	}

	@Override
	public boolean isPinnedObject(final EObject eObject) {
		return Objects.equals(getScenarioResult(eObject), pinnedScenarioResult);
	}

	@Override
	public ScenarioResult getPinnedScenarioResult() {
		return pinnedScenarioResult;
	}

	public void setPinnedScenarioInstance(final @Nullable ScenarioResult pinnedScenarioResult) {
		this.pinnedScenarioResult = pinnedScenarioResult;
		otherScenarioResults.remove(pinnedScenarioResult);
	}

	

	/**
	 * Find all the elements belonging to this scenario and result.
	 * 
	 * @param scenarioResult
	 * @return
	 */
	public static Collection<EObject> getChildren(final ScenarioResult scenarioResult) {

		final LNGScenarioModel scenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
		final ScheduleModel scheduleModel = scenarioResult.getTypedResult(ScheduleModel.class);

		final Collection<EObject> children = new HashSet<>();
		{
			final TreeIterator<EObject> itr = scenarioModel.eAllContents();
			while (itr.hasNext()) {
				final EObject next = itr.next();
				if (next instanceof ScheduleModel) {
					itr.prune();
				} else {
					children.add(next);
				}
			}
		}
		if (scheduleModel != null) {
			final TreeIterator<EObject> itr = scheduleModel.eAllContents();
			while (itr.hasNext()) {
				final EObject next = itr.next();
				children.add(next);
			}
		}
		children.add(scenarioModel);

		return children;
	}

	@Override
	public boolean inPinDiffMode() {
		return pinnedScenarioResult != null && !otherScenarioResults.isEmpty();
	}

	private final Collection<ISelectionListener> listeners = new ConcurrentLinkedQueue<>();

	@Override
	public void addSelectionChangedListener(final ISelectionListener l) {
		listeners.add(l);
	}

	@Override
	public void removeSelectionChangedListener(final ISelectionListener l) {
		listeners.remove(l);
	}

	private Collection<Object> selectedObjects = new LinkedHashSet<>();

	private Collection<Object> changeSetSelection;
	
	public void setSelectedObjects(final IWorkbenchPart part, final ISelection selection) {
		
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			final Collection<Object> newSelectedObjects = new LinkedHashSet<>();
			ss.forEach(newSelectedObjects::add);
			selectedObjects = newSelectedObjects;
		}
		
		for (final var l : listeners) {
			try {
				l.selectionChanged(part, selection);
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
	
	
	@Override
	public boolean isDiffToBase() {
		return diffToBase;
	}

	public void setDiffToBase(final boolean diffToBase) {
		this.diffToBase = diffToBase;
	}
	
	@Override
	public @Nullable ChangeSetTableGroup getChangeSet() {
		return changeSet;
	}

	public void setChangeSet(final ChangeSetTableGroup changeSet) {
		this.changeSet = changeSet;
	}
	@Override
	public @Nullable ChangeSetTableRoot getChangeSetRoot() {
		return changeSetRoot;
	}

	public void setChangeSetRoot(final ChangeSetTableRoot changeSetRoot) {
		this.changeSetRoot = changeSetRoot;
	}
	
	@Override
	public @Nullable Collection<ChangeSetTableRow> getSelectedChangeSetRows() {
		return changeSetRows;
	}

	public void setChangeSetRows(final Collection<ChangeSetTableRow> changeSetRows) {
		this.changeSetRows = changeSetRows;
	}

	@Override
	public Collection<Object> getSelectedObjects() {
		return selectedObjects;
	}
	
	@Override
	public @Nullable  Collection<Object> getChangeSetSelection() {
		return changeSetSelection;
	}

	public void setChangeSetSelection(List<Object> changeSetSelection) {
		this.changeSetSelection = new LinkedHashSet<>(changeSetSelection);
	}
	
}
