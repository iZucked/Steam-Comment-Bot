/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * Used to handle commands generated by inline editors, and also provide peripheral information.
 * 
 * @author hinton
 *
 */

@NonNullByDefault
public interface ICommandHandler {
	/**
	 * Get reference value provider provider for the context of this command handler
	 * 
	 * @return
	 */
	IReferenceValueProviderProvider getReferenceValueProviderProvider();

	/**
	 * Get the editing domain in which commands for this handler should happen
	 * 
	 * @return
	 */
	EditingDomain getEditingDomain();

	ModelReference getModelReference();

	/**
	 * Tell the command handler to deal with this command, applied to this feature on the target.
	 * 
	 * @param command
	 * @param target
	 * @param feature
	 */
	void handleCommand(Command command, @Nullable EObject target, @Nullable EStructuralFeature feature);

	default void handleCommand(Command command) {
		handleCommand(command, null, null);
	}

}
