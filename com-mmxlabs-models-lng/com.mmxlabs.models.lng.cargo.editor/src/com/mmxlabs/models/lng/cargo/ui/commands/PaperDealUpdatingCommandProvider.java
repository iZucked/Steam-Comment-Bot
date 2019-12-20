/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
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

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		if (commandClass == SetCommand.class) {
			if (parameter.getEOwner() instanceof PaperDeal) {
				final PaperDeal paper = (PaperDeal) parameter.getEOwner();
				final PaperPricingType ppt = paper.getPricingType();
				if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getPaperDeal_PricingMonth()) {
					if (ppt != PaperPricingType.PERIOD_AVG) {
						final YearMonth month = (YearMonth) parameter.getValue();
						final LocalDate start = LocalDate.of(month.getYear(), month.getMonthValue(), 1);
						final LocalDate end = LocalDate.of(month.getYear(), month.getMonthValue(), month.lengthOfMonth());
						CompoundCommand temp = new CompoundCommand();
						temp.append(SetCommand.create(editingDomain, paper, CargoPackage.eINSTANCE.getPaperDeal_StartDate(), start));
						temp.append(SetCommand.create(editingDomain, paper, CargoPackage.eINSTANCE.getPaperDeal_EndDate(), end));
						return temp;
					}
				} else if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getPaperDeal_StartDate()) {
					if (ppt == PaperPricingType.PERIOD_AVG) {
						final LocalDate start = (LocalDate) parameter.getValue();
						final YearMonth month = YearMonth.of(start.getYear(), start.getMonthValue());
						return SetCommand.create(editingDomain, paper, CargoPackage.eINSTANCE.getPaperDeal_PricingMonth(), month);
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
