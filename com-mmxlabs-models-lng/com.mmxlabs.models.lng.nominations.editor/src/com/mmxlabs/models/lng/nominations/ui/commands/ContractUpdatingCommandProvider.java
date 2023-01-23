/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.commands;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class ContractUpdatingCommandProvider extends AbstractCommandProvider {

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		if (rootObject instanceof LNGScenarioModel) {
			final CompoundCommand cc = new CompoundCommand();
			if (commandClass == SetCommand.class) {
				//Check if contract has been renamed.
				if (parameter.getEOwner() instanceof Contract) {
					final Contract contract = (Contract)parameter.getEOwner();
					if (parameter.getEStructuralFeature() == MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
						final String oldName = contract.getName();
						final String newName = (String)parameter.getValue();

						if (!Objects.equals(oldName, newName)) {
							//update all nominations with refererId = contract name.
							final LNGScenarioModel sm = (LNGScenarioModel)rootObject;
							final NominationsModel nm = sm.getNominationsModel();
							nm.getNominationSpecs().forEach(sns -> {
								if (Objects.equals(sns.getRefererId(), oldName)) {
									cc.append(SetCommand.create(editingDomain, sns, NominationsPackage.eINSTANCE.getAbstractNominationSpec_RefererId(), newName));
								}
							});
						}
					}
				}
			}
			else if (commandClass == DeleteCommand.class) {
				//Check if contract has been deleted.
				for (final Object object : getChangedValues(parameter)) {
					if (object instanceof Contract) {
						final Contract contract = (Contract)object;
						final String name = contract.getName();
						//delete all nominations with refererId = contract name.
						final LNGScenarioModel sm = (LNGScenarioModel)rootObject;
						final NominationsModel nm = sm.getNominationsModel();
						nm.getNominationSpecs().forEach(sns -> {
							if (Objects.equals(sns.getRefererId(), name)) {
								cc.append(DeleteCommand.create(editingDomain, sns));
							}
						});
					}
				}
			}
			if (!cc.isEmpty()) {
				return cc;
			}
		}
		return null;
	}
}
