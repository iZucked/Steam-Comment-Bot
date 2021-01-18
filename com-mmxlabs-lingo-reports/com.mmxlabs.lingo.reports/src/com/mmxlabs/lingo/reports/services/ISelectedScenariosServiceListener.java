/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;

@NonNullByDefault
public interface ISelectedScenariosServiceListener {

	void selectedDataProviderChanged(ISelectedDataProvider selectedDataProvider, boolean block);

	default void selectedObjectChanged(@Nullable MPart source, ISelection selection) {

	}

	default void diffOptionChanged(EDiffOption option,Object oldValue, Object newValue) {

	}

	// void selectedObjectChanged(MPart source, ISelection selection);

	// void changeSetChanged(@Nullable ChangeSetTableRoot changeSetRoot, @Nullable ChangeSetTableGroup changeSet, @Nullable Collection<ChangeSetTableRow> changeSetRows, boolean diffToBase);
	//
	// void selectionChanged(@NonNull ISelectedDataProvider selectedDataProvider, @Nullable ScenarioResult pinned, @NonNull Collection<@NonNull ScenarioResult> others, boolean block);

}
