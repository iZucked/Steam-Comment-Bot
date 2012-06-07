/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.management.timer.Timer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class CargoImporter extends DefaultClassImporter {
	private static final String ASSIGNMENT = "assignment";

	@Override
	protected boolean shouldFlattenReference(final EReference reference) {
		return super.shouldFlattenReference(reference) || reference == CargoPackage.eINSTANCE.getCargo_LoadSlot() || reference == CargoPackage.eINSTANCE.getCargo_DischargeSlot();
	}

	@Override
	public Collection<EObject> importObject(final EClass eClass, final Map<String, String> row, final IImportContext context) {
		final Collection<EObject> result = super.importObject(eClass, row, context);
		LoadSlot load = null;
		DischargeSlot discharge = null;
		Cargo cargo = null;
		for (final EObject o : result) {
			if (o instanceof Cargo) cargo = (Cargo) o;
			else if (o instanceof LoadSlot) load = (LoadSlot) o;
			else if (o instanceof DischargeSlot) discharge = (DischargeSlot) o;
		}
		
		// fix missing names
		
		
		if (cargo != null && cargo.getName() != null && cargo.getName().trim().isEmpty() == false) {
			final String cargoName = cargo.getName().trim();
			if (load != null && (load.getName() == null || load.getName().trim().isEmpty())) {
				final LoadSlot load2 = load;
				context.doLater(new IDeferment() {
					@Override
					public void run(final IImportContext context) {
						load2.setName("load-" + cargoName);
						context.registerNamedObject(load2);
					}
					
					@Override
					public int getStage() {
						return IImportContext.STAGE_MODIFY_ATTRIBUTES;
					}
				});
			}
			
			if (discharge != null && (discharge.getName() == null || discharge.getName().trim().isEmpty())) {
				final DischargeSlot discharge2 = discharge;
				context.doLater(new IDeferment() {
					@Override
					public void run(final IImportContext context) {
						discharge2.setName("discharge-" + cargoName);
						context.registerNamedObject(discharge2);
					}
					
					@Override
					public int getStage() {
						return IImportContext.STAGE_MODIFY_ATTRIBUTES;
					}
				});
			}
		}
		
		if (row.containsKey(ASSIGNMENT)) {
			final Cargo cargo_ = cargo;
			final String assignedTo = row.get(ASSIGNMENT);
			final IImportProblem noVessel = context.createProblem("Cannot find vessel " + assignedTo, true, true, true);
			context.doLater(new IDeferment() {
				@Override
				public void run(final IImportContext context) {
					final InputModel im = context.getRootObject().getSubModel(InputModel.class);
					if (im != null) {
						for (final Assignment a : im.getAssignments()) {
							if (a.getAssignedObjects().contains(cargo_)) return;
						}
						
						// attempt to find vessel
						NamedObject vessel = context.getNamedObject(assignedTo, FleetPackage.eINSTANCE.getVessel());
						if (vessel instanceof Vessel) {
							// assignment is not present, so use the one from here.
							Assignment match = null;
							for (final Assignment a : im.getAssignments()) {
								if (a.getVessels().contains(vessel)) {
									match = a;
									break;
								}
							}
							if (match == null) {
								match = InputFactory.eINSTANCE.createAssignment();
								match.getVessels().add((Vessel)vessel);
								im.getAssignments().add(match);
							}
							insert(match, cargo_);

						} else {
							context.addProblem(noVessel);
						}
					}
				}
				
				private void insert(Assignment match, Cargo task) {
					int position = 0;
					
					final Date start = getStartDate(task);
					final Date end = getEndDate(task);

					if (start != null && end != null) {
						for (final UUIDObject o : match.getAssignedObjects()) {
							final Date startO = getStartDate(o);
//							final Date endO = getEndDate(o);
							if (end.before(startO)) {
								break;
//							} else if (start.after(endO)) {
//								position++;
//								break;
							} else {
								position++;
							}
						}
					}
					
					if (position == match.getAssignedObjects().size()) {
						match.getAssignedObjects().add(task);
					} else {
						match.getAssignedObjects().add(position, task);
					}
				}

				@Override
				public int getStage() {
					return IImportContext.STAGE_MODIFY_SUBMODELS;
				}
				
				private Date getStartDate(UUIDObject task) {
					if (task instanceof Cargo) {
						return ((Cargo) task).getLoadSlot()
								.getWindowStartWithSlotOrPortTime();
					} else if (task instanceof VesselEvent) {
						return
								((VesselEvent) task).getStartBy();
					} else if (task instanceof Slot) {
						return ((Slot) task).getWindowStartWithSlotOrPortTime();
					} else {
						return null;
					}
				}
				
				private Date getEndDate(UUIDObject task) {
					if (task instanceof Cargo) {
						return ((Cargo) task).getDischargeSlot().getWindowEndWithSlotOrPortTime();
					} else if (task instanceof VesselEvent) {
						return 
								new Date(
								((VesselEvent) task).getStartBy().getTime()
								+ Timer.ONE_DAY * ((VesselEvent)task).getDurationInDays());
					} else if (task instanceof Slot) {
						return ((Slot) task).getWindowEndWithSlotOrPortTime();
					} else {
						return null;
					}
				}
			});
		}
		
		return result;
	}
}
