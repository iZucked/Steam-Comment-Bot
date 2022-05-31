/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class SandboxOpportunityCommandProvider implements IModelCommandProvider {
	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		return null;
	}

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		if (commandClass == SetCommand.class) {
			if (parameter.getOwner() instanceof BuyOpportunity op) {
				if (parameter.getEStructuralFeature() == AnalyticsPackage.eINSTANCE.getBuyOpportunity_MinVolume()) {
					if (op.getVolumeMode() == VolumeMode.FIXED) {
						return SetCommand.create(editingDomain, op, AnalyticsPackage.eINSTANCE.getBuyOpportunity_MaxVolume(), parameter.getValue());
					}
				}
				if (parameter.getEStructuralFeature() == AnalyticsPackage.eINSTANCE.getBuyOpportunity_VolumeMode()) {
					if (parameter.getValue() == VolumeMode.FIXED) {
						return SetCommand.create(editingDomain, op, AnalyticsPackage.eINSTANCE.getBuyOpportunity_MaxVolume(), op.getMinVolume());
					}
				}
			}
			if (parameter.getOwner() instanceof SellOpportunity op) {
				if (parameter.getEStructuralFeature() == AnalyticsPackage.eINSTANCE.getSellOpportunity_MinVolume()) {
					if (op.getVolumeMode() == VolumeMode.FIXED) {
						return SetCommand.create(editingDomain, op, AnalyticsPackage.eINSTANCE.getSellOpportunity_MaxVolume(), parameter.getValue());
					}
				}
				if (parameter.getEStructuralFeature() == AnalyticsPackage.eINSTANCE.getSellOpportunity_VolumeMode()) {
					if (parameter.getValue() == VolumeMode.FIXED) {
						return SetCommand.create(editingDomain, op, AnalyticsPackage.eINSTANCE.getSellOpportunity_MaxVolume(), op.getMinVolume());
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
