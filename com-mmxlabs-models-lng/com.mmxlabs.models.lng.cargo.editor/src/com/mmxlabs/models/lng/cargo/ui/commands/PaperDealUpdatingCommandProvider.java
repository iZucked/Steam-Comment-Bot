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

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * @author Farukh Mukhamedov
 * 
 */
public class PaperDealUpdatingCommandProvider implements IModelCommandProvider {
	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		return null;
	}
	
	private static final CargoPackage cp = CargoPackage.eINSTANCE;

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		
		if ((commandClass == SetCommand.class) && parameter.getEOwner() instanceof final PaperDeal paperDeal) {
			if (parameter.getEStructuralFeature() == cp.getPaperDeal_PricingMonth()//
					|| parameter.getEStructuralFeature() == cp.getPaperDeal_Instrument()) {
				return PaperDealUpdatingCommandUtil.createCommands(paperDeal, editingDomain, parameter.getValue(), null, null);
			}
		}
		return null;
	}
	
	

	@Override
	public void startCommandProvision() {

	}

	@Override
	public void endCommandProvision() {

	}
}
