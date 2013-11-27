package com.mmxlabs.models.lng.cargo.ui.editorpart.trades;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

/**
 * @since 8.0
 */
public interface ITradesTableContextMenuExtension {

	void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull Slot slot, @NonNull MenuManager menuManager);
}
