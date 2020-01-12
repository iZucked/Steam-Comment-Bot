/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.mmxcore.MMXRootObject;

abstract public class AbstractCommandProvider implements IModelCommandProvider {
	
	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		return null;
	}
	
	@Override
	public void startCommandProvision() {

	}

	@Override
	public void endCommandProvision() {

	}
	
	protected Collection<?> getChangedValues(CommandParameter cp) {
		if (cp.value != null) {
			return Collections.singleton(cp.value);
		}
		else if (cp.collection != null) {
			return cp.collection;
		}
		return Collections.EMPTY_SET;
	}
}
