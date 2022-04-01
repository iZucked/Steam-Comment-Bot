/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.editor.commandproviders;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Deletes old distance lines when their ports are being deleted
 * 
 * @author hinton
 * 
 */
public class ContractProfileCommandProvider extends BaseModelCommandProvider<Object> {

	@Override
	protected Command handleDeletion(final EditingDomain editingDomain, final MMXRootObject rootObject, final Collection<Object> deleted, final Map<EObject, EObject> overrides,
			final Set<EObject> editSet) {

		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final ADPModel adpModel = ScenarioModelUtil.getADPModel(lngScenarioModel);
			if (adpModel == null) {
				return null;
			}

			final Set<ContractProfile<?, ?>> dead = new HashSet<>();
			for (final Object object : deleted) {
				if (object instanceof Contract) {
					adpModel.getPurchaseContractProfiles().stream().filter(p -> p.getContract() == object).forEach(dead::add);
					adpModel.getSalesContractProfiles().stream().filter(p -> p.getContract() == object).forEach(dead::add);
				}
			}
			if (!dead.isEmpty()) {
				return DeleteCommand.create(editingDomain, dead);
			}
		}
		return null;
	}
}
