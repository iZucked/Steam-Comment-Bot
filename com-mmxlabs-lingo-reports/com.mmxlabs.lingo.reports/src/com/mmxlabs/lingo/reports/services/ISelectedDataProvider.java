/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.ISelectionListener;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ScenarioResult;

public interface ISelectedDataProvider {

	boolean inPinDiffMode();

	// Lookup methods
	@Nullable
	ScenarioResult getScenarioResult(EObject eObject);

	@Nullable
	Schedule getSchedule(EObject eObject);

	boolean isPinnedObject(EObject eObject);

	@NonNull
	List<com.mmxlabs.scenario.service.ScenarioResult> getOtherScenarioResults();

	@NonNull
	List<com.mmxlabs.scenario.service.ScenarioResult> getAllScenarioResults();

	ScenarioResult getPinnedScenarioResult();

	@Nullable
	Collection<ChangeSetTableRow> getSelectedChangeSetRows();

	@Nullable
	ChangeSetTableGroup getChangeSet();

	boolean isDiffToBase();

	@Nullable
	ChangeSetTableRoot getChangeSetRoot();

	void removeSelectionChangedListener(ISelectionListener l);

	void addSelectionChangedListener(ISelectionListener l);

	Collection<Object> getSelectedObjects();

}