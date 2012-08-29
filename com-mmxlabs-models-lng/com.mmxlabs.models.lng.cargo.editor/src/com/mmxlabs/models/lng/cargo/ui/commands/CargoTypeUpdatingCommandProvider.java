/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.dates.LocalDateUtil;

/**
 * 
 * A {@link IModelCommandProvider} implementation to map DES/FOB details between slots
 * 
 * @author Simon Goodall
 * 
 */
public class CargoTypeUpdatingCommandProvider implements IModelCommandProvider {
	@Override
	public Command provideAdditionalCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Class<? extends Command> commandClass,
			final CommandParameter parameter, final Command input) {
		if (commandClass == SetCommand.class) {
			InputModel inputModel = rootObject.getSubModel(InputModel.class);
			if (overrides.containsKey(inputModel)) {
				inputModel = (InputModel) overrides.get(inputModel);
			}
			if (parameter.getEOwner() instanceof LoadSlot) {
				final LoadSlot slot = (LoadSlot) parameter.getEOwner();

				if (slot.getCargo() != null) {
					final Cargo cargo = slot.getCargo();
					final DischargeSlot dischargeSlot = cargo.getDischargeSlot();
					if (dischargeSlot != null) {

						if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase()) {
							final boolean desPurchase = (Boolean) parameter.getValue();
							if (desPurchase) {

								final CompoundCommand cmd = new CompoundCommand("Convert to DES Purchase");
								cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getLoadSlot_ArriveCold(), false));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Port(), dischargeSlot.getPort()));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));

								if (slot instanceof SpotSlot) {
									setSpotSlotTimeWindow(editingDomain, slot, dischargeSlot, cmd);
								} else {
									cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), dischargeSlot.getWindowStart()));
									cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), dischargeSlot.getWindowStartTime()));
								}
								return cmd;
							}
						} else if (dischargeSlot.isFOBSale()) {
							if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Port() || parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_WindowStart()) {
								final CompoundCommand cmd = new CompoundCommand("FOB Sale update");

								if (dischargeSlot instanceof SpotSlot) {
									setSpotSlotTimeWindow(editingDomain, dischargeSlot, slot, (Date) parameter.getValue(), cmd);
								} else {
									cmd.append(SetCommand.create(editingDomain, dischargeSlot, parameter.getEStructuralFeature(), parameter.getValue()));
								}

								return cmd;
							}
						}
					}
				}
			}
			if (parameter.getEOwner() instanceof DischargeSlot) {
				final DischargeSlot slot = (DischargeSlot) parameter.getEOwner();
				if (slot.getCargo() != null) {
					final Cargo cargo = slot.getCargo();
					final LoadSlot loadSlot = cargo.getLoadSlot();
					if (loadSlot != null) {

						if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getDischargeSlot_FOBSale()) {
							final boolean fobSale = (Boolean) parameter.getValue();
							if (fobSale) {

								final CompoundCommand cmd = new CompoundCommand("Convert to FOB Sale");
								cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Port(), loadSlot.getPort()));
								cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));

								if (slot instanceof SpotSlot) {
									setSpotSlotTimeWindow(editingDomain, slot, loadSlot, cmd);
								} else {
									cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), loadSlot.getWindowStart()));
									cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), loadSlot.getWindowStartTime()));
								}
								return cmd;
							}
						} else if (loadSlot.isDESPurchase()) {
							if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Port() || parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_WindowStart()) {
								final CompoundCommand cmd = new CompoundCommand("DES Purchase update");

								if (loadSlot instanceof SpotSlot) {
									setSpotSlotTimeWindow(editingDomain, loadSlot, slot, (Date) parameter.getValue(), cmd);
								} else {
									cmd.append(SetCommand.create(editingDomain, loadSlot, parameter.getEStructuralFeature(), parameter.getValue()));
								}
								return cmd;
							}
						}

					}
				}
			}
		}
		return null;
	}

	private void setSpotSlotTimeWindow(final EditingDomain editingDomain, final Slot slot, final Slot otherSlot, final CompoundCommand cmd) {
		// Spot market - make a month range.
		final Calendar cal = Calendar.getInstance();
		final TimeZone zone = LocalDateUtil.getTimeZone(otherSlot.getPort(), PortPackage.eINSTANCE.getPort_TimeZone());
		cal.setTimeZone(zone);
		cal.setTime(otherSlot.getWindowStart());
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		final Date start = cal.getTime();
		final long startMillis = cal.getTimeInMillis();
		cal.add(Calendar.MONTH, 1);
		final long endMillis = cal.getTimeInMillis();
		final int windowSize = (int) ((endMillis - startMillis) / 1000 / 60 / 60);

		cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowSize(), windowSize));
		cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), start));
		cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), 0));
	}

	private void setSpotSlotTimeWindow(final EditingDomain editingDomain, final Slot slot, Slot otherSlot, Date newDate, final CompoundCommand cmd) {
		// Spot market - make a month range.
		final Calendar cal = Calendar.getInstance();
		final TimeZone zone = LocalDateUtil.getTimeZone(otherSlot.getPort(), PortPackage.eINSTANCE.getPort_TimeZone());
		cal.setTimeZone(zone);
		cal.setTime(newDate);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		DateFormat df = DateFormat.getDateTimeInstance();
		df.setTimeZone(zone);
		System.out.println(df.format(newDate));
		System.out.println(df.format(cal.getTime()));
		final Date start = cal.getTime();
		final long startMillis = cal.getTimeInMillis();
		cal.add(Calendar.MONTH, 1);
		final long endMillis = cal.getTimeInMillis();
		final int windowSize = (int) ((endMillis - startMillis) / 1000 / 60 / 60);

		cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowSize(), windowSize));
		cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), start));
		cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), 0));
	}

	@Override
	public void startCommandProvision() {

	}

	@Override
	public void endCommandProvision() {

	}
}
