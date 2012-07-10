/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.NamedObject;
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
						ElementAssignment existing = null;
						for (final ElementAssignment ea : im.getElementAssignments()) {
							if (ea.getAssignedObject() == cargo_) {
								existing = ea;
								break;
							}
						}
						if (existing == null) {
							existing = InputFactory.eINSTANCE.createElementAssignment();
							im.getElementAssignments().add(existing);
							existing.setAssignedObject(cargo_);
						}
						
						// attempt to find vessel
						NamedObject vessel = context.getNamedObject(assignedTo, FleetPackage.eINSTANCE.getVessel());
						if (vessel instanceof Vessel) {
							existing.setAssignment((AVesselSet) vessel);
						} else {
							context.addProblem(noVessel);
						}
					}
				}

				@Override
				public int getStage() {
					return IImportContext.STAGE_MODIFY_SUBMODELS + 10;
				}
			});
		}
		
		return result;
	}
}
