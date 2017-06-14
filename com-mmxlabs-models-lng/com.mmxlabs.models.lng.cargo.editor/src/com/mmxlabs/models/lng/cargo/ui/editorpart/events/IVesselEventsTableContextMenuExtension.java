/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.events;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

/**
 */
public interface IVesselEventsTableContextMenuExtension {

	void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull VesselEvent vesselEvent, @NonNull MenuManager menuManager);

	void contributeToMenu(@NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull IStructuredSelection selection, @NonNull MenuManager menuManager);
}
