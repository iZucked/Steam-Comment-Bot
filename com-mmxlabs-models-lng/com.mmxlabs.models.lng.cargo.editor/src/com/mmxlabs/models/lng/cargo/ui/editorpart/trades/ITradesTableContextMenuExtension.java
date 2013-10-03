package com.mmxlabs.models.lng.cargo.ui.editorpart.trades;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;

import com.mmxlabs.models.lng.cargo.Slot;

/**
 * @since 8.0
 */
public interface ITradesTableContextMenuExtension {

	void contributeToMenu(@NonNull final EditingDomain domain, @NonNull Slot slot, @NonNull MenuManager menuManager);
}
