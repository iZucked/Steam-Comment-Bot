/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class DealSetUpdatingCommandProvider implements IModelCommandProvider {

	@Override
	public @Nullable Command provideAdditionalBeforeCommand(EditingDomain editingDomain, MMXRootObject rootObject, Map<EObject, EObject> overrides, Set<EObject> editSet,
			Class<? extends Command> commandClass, CommandParameter parameter, Command input) {
		return null;
	}
	
	private static final CargoPackage cp = CargoPackage.eINSTANCE;

	@Override
	public @Nullable Command provideAdditionalAfterCommand(EditingDomain editingDomain, MMXRootObject rootObject, Map<EObject, EObject> overrides, Set<EObject> editSet,
			Class<? extends Command> commandClass, CommandParameter parameter, Command input) {
		if ((commandClass == SetCommand.class) && parameter.getEOwner() instanceof final DealSet dealSet) {
			if (parameter.getEStructuralFeature() == cp.getDealSet_AllowExposure()//
					|| parameter.getEStructuralFeature() == cp.getDealSet_AllowHedging()) {
				
				Boolean aExpo = null;
				Boolean aHedge = null;
				
				if (parameter.getEStructuralFeature() == cp.getDealSet_AllowExposure()) {
					aExpo = (boolean) parameter.getValue();
				} else if (parameter.getEStructuralFeature() == cp.getDealSet_AllowHedging()) {
					aHedge = (boolean) parameter.getValue();
				}
				
				if (Boolean.TRUE.equals(aHedge)) {
					return SetCommand.create(editingDomain, dealSet, cp.getDealSet_AllowExposure(), Boolean.TRUE);
				}
				if (Boolean.FALSE.equals(aExpo)) {
					return SetCommand.create(editingDomain, dealSet, cp.getDealSet_AllowHedging(), Boolean.FALSE);
				}
			}
		}
		return null;
	}

}
