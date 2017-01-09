/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.Collection;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.IPostExportProcessor;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * {@link IPostExportProcessor} to remove duplicate slots created by the {@link PetronasRedirectionContractTransformer}
 * 
 * @author Simon Goodall
 * 
 */
public class RedirectionPostExportProcessor implements IPostExportProcessor {

	@Inject
	private RedirectionGroupProvider redirectionGroupProvider;

	@Override
	public void postProcess(final EditingDomain ed, final MMXRootObject rootObject, final Schedule scheduleModel, final CompoundCommand commands) {

		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
		
			final CargoModel cargoModel = lngScenarioModel.getCargoModel();
			if (cargoModel == null) {
				return;
			}

			for (final CargoAllocation cargoAllocation : scheduleModel.getCargoAllocations()) {

				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Collection<Slot> group = redirectionGroupProvider.getRedirectionGroup(slotAllocation.getSlot());
					if (group == null) {
						continue;
					}
					// Remove other slots...
					for (final Slot slot : group) {
						if (slot == slotAllocation.getSlot()) {
							// Keep this slot
							// TODO: Make sure extensions are transferred over, or replace existing with selected data
							if (slot instanceof LoadSlot) {
								final LoadSlot loadSlot = (LoadSlot) slot;
								if (loadSlot.isDESPurchase()) {

									if (slotAllocation.getCargoAllocation() != null) {
										DischargeSlot discharge = null;
										for (final SlotAllocation s : slotAllocation.getCargoAllocation().getSlotAllocations()) {
											if (s.getSlot() instanceof DischargeSlot) {
												discharge = (DischargeSlot) s.getSlot();
												break;
											}
										}

										if (discharge != null) {
											commands.append(SetCommand.create(ed, loadSlot, CargoPackage.Literals.SLOT__PORT, discharge.getPort()));
											commands.append(SetCommand.create(ed, loadSlot, CargoPackage.Literals.SLOT__WINDOW_START, discharge.getWindowStart()));
											// TODO: We may need a getSlotOrPortWindowStartTime here
											commands.append(SetCommand.create(ed, loadSlot, CargoPackage.Literals.SLOT__WINDOW_START_TIME, discharge.getWindowStartTime()));
											commands.append(SetCommand.create(ed, loadSlot, CargoPackage.Literals.SLOT__WINDOW_SIZE, discharge.getSlotOrPortWindowSize()));
											commands.append(SetCommand.create(ed, loadSlot, CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS, discharge.getSlotOrPortWindowSizeUnits()));
											commands.append(SetCommand.create(ed, loadSlot, CargoPackage.Literals.SLOT__DURATION, discharge.getSlotOrPortDuration()));

											// TODO: What about volume bounds?

										}
									} else {
										// Unpaired cargo -> restore slot to defaults?
										// DES Purchase @?
									}
								}
							}

							continue;
						}
						if (slot.eContainer() != null) {
							commands.append(DeleteCommand.create(ed, slot));
						}

//						if (slot.getCargo() != null) {
//							final Cargo c = slot.getCargo();
//							// TODO: Need to delete assignment
////							commands.append(AssignmentEditorHelper.unassignElement(ed, assignmentModel, c));
//							// commands.append(DeleteCommand.create(ed, c));
//						}
					}
				}
			}
		}
	}

}