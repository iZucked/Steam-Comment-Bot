/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

/**
 * Used to handle commands generated by inline editors, and also provide peripheral information.
 * 
 * @author hinton
 *
 */
public interface ICommandHandler {
	/**
	 * Get reference value provider provider for the context of this command handler
	 * 
	 * @return
	 */
	public IReferenceValueProviderProvider getReferenceValueProviderProvider();
	
	/**
	 * Get the editing domain in which commands for this handler should happen
	 * 
	 * @return
	 */
	public EditingDomain getEditingDomain();
	
	/**
	 * Tell the command handler to deal with this command, applied to this feature on the target.
	 * 
	 * @param command
	 * @param target
	 * @param feature
	 */
	public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature);
}
