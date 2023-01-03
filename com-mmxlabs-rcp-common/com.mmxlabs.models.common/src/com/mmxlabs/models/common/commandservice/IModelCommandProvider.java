/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.common.commandservice;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * An interface for things which override / extend commands
 * 
 * @author hinton
 *
 */
public interface IModelCommandProvider {
	/**
	 * Given the input command, return an extra command which should happen before the input has executed.
	 * @param input
	 * @return
	 */
	@Nullable Command provideAdditionalBeforeCommand(
			final EditingDomain editingDomain, 
			final MMXRootObject rootObject,
			final Map<EObject, EObject> overrides,
			final Set<EObject> editSet,
			final Class<? extends Command> commandClass, 
			final CommandParameter parameter, 
			final Command input);
	
	
	/**
	 * Given the input command, return an extra command which should happen after the input has executed.
	 * @param input
	 * @return
	 */
	@Nullable Command provideAdditionalAfterCommand(
			final EditingDomain editingDomain, 
			final MMXRootObject rootObject,
			final Map<EObject, EObject> overrides,
			final Set<EObject> editSet,
			final Class<? extends Command> commandClass, 
			final CommandParameter parameter, 
			final Command input);
	
	default void startCommandProvision() {
		
	}
	
	default void endCommandProvision() {
		
	}
}
