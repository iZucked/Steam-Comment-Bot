/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.commands;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.commandservice.IModelCommandProvider;

/**
 * A command provider which resets cost matrix lines when the cost matrix has changed
 * 
 * @author hinton
 *
 */
public class UnitCostMatrixCommandProvider implements IModelCommandProvider {

	@Override
	public Command provideAdditionalCommand(EditingDomain editingDomain, MMXRootObject rootObject, Class<? extends Command> commandClass, CommandParameter parameter, Command input) {
		if (commandClass.equals(SetCommand.class)) {
			if (parameter.getEOwner() instanceof UnitCostMatrix) {
				return RemoveCommand.create(editingDomain, parameter.getEOwner(), AnalyticsPackage.eINSTANCE.getUnitCostMatrix_CostLines(), ((UnitCostMatrix) (parameter.getEOwner())).getCostLines());
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
