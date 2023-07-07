/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.pricing.InstrumentPeriod;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class SettleStrategyUpdatingCommandProvider implements IModelCommandProvider {

	@Override
	public @Nullable Command provideAdditionalBeforeCommand(EditingDomain editingDomain, MMXRootObject rootObject, Map<EObject, EObject> overrides, Set<EObject> editSet,
			Class<? extends Command> commandClass, CommandParameter parameter, Command input) {
		// nothing
		return null;
	}

	@Override
	public @Nullable Command provideAdditionalAfterCommand(EditingDomain editingDomain, MMXRootObject rootObject, Map<EObject, EObject> overrides, Set<EObject> editSet,
			Class<? extends Command> commandClass, CommandParameter parameter, Command input) {
		if ((commandClass == SetCommand.class)) {
			if (rootObject instanceof LNGScenarioModel scenarioModel) {
				InstrumentPeriod instrumentPeriod = null;
				SettleStrategy settleStrategy = null;
				if (parameter.getEOwner() instanceof final InstrumentPeriod ip) {
					if (ip.eContainer() instanceof final SettleStrategy ss) {
						settleStrategy = ss;
						instrumentPeriod = ip;
					}
				} else if (parameter.getEOwner() instanceof final SettleStrategy ss) {
					settleStrategy = ss;
				}
				if (settleStrategy != null) {
					final SettleStrategy fSS = settleStrategy;
					final InstrumentPeriod fIP = instrumentPeriod;
					final CompoundCommand cc = new CompoundCommand();
					final CargoModel cm = ScenarioModelUtil.getCargoModel(scenarioModel);
					cm.getPaperDeals().forEach( pd -> {
						
						if (pd.getInstrument() != null && fSS.getName().equalsIgnoreCase(pd.getInstrument().getName())) {
							if (fIP != null) {
								cc.append(PaperDealUpdatingCommandUtil.createCommands(pd, editingDomain, fIP, parameter.getEStructuralFeature(), parameter.getValue()));
							} else {
								cc.append(PaperDealUpdatingCommandUtil.createCommands(pd, editingDomain, fSS, parameter.getEStructuralFeature(), parameter.getValue()));
							}
						}
					});
					if (!cc.isEmpty()) {
						return cc;
					}
				}
			}
		}
		return null;
	}



}
