/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * @author Farukh Mukhamedov
 * 
 */
public class FOBPurchaseMarketUpdatingCommandProvider implements IModelCommandProvider {
	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		return null;
	}

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		if (commandClass == SetCommand.class) {
			if (parameter.getOwner() instanceof FOBPurchasesMarket) {
				final FOBPurchasesMarket market = (FOBPurchasesMarket) parameter.getOwner();
				if (parameter.getEStructuralFeature() == SpotMarketsPackage.eINSTANCE.getFOBPurchasesMarket_NotionalPort()) {
					if (parameter.getValue() instanceof Port) {
						final Port port = (Port) parameter.getValue();
						final double cv = port.getCvValue();
						return SetCommand.create(editingDomain, market, SpotMarketsPackage.eINSTANCE.getFOBPurchasesMarket_Cv(), cv);
					}
				}
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
