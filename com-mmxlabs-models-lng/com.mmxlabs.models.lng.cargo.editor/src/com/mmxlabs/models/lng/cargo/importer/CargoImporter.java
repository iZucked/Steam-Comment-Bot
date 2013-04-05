/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.google.common.collect.Sets;
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
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * @since 2.0
 */
public class CargoImporter extends DefaultClassImporter {
	private static final String ASSIGNMENT = "assignment";

	// List of column names to filter out of export. UUID is confusing to the user, othernames is not used by cargo or slots. Fixed Price is deprecated.
	private static final Set<String> filteredColumns = Sets.newHashSet("uuid", "otherNames", "loadSlot.uuid", "loadSlot.fixedPrice", "loadSlot.otherNames", "dischargeSlot.uuid",
			"dischargeSlot.fixedPrice", "dischargeSlot.otherNames");

	@Override
	protected boolean shouldFlattenReference(final EReference reference) {
		return super.shouldFlattenReference(reference) || reference == CargoPackage.eINSTANCE.getCargo_LoadSlot() || reference == CargoPackage.eINSTANCE.getCargo_DischargeSlot();
	}

	public Collection<Map<String, String>> exportObjects(final Collection<Cargo> cargoes, final Collection<LoadSlot> loadSlots, final Collection<DischargeSlot> dischargeSlots, final MMXRootObject root) {

		final List<Map<String, String>> data = new LinkedList<Map<String, String>>();

		data.addAll(super.exportObjects(cargoes, root));
		{
			for (final LoadSlot slot : loadSlots) {
				if (slot.getCargo() == null) {
					final Map<String, String> result = new LinkedHashMap<String, String>();
					final Map<String, String> subMap = super.exportObjects(Collections.singleton(slot), root).iterator().next();
					for (final Map.Entry<String, String> e : subMap.entrySet()) {
						result.put(CargoPackage.eINSTANCE.getCargo_LoadSlot().getName() + DefaultClassImporter.DOT + e.getKey(), e.getValue());
					}
					data.add(result);
				}
			}
		}
		{
			for (final DischargeSlot slot : dischargeSlots) {
				if (slot.getCargo() == null) {
					final Map<String, String> result = new LinkedHashMap<String, String>();
					final Map<String, String> subMap = super.exportObjects(Collections.singleton(slot), root).iterator().next();
					for (final Map.Entry<String, String> e : subMap.entrySet()) {
						result.put(CargoPackage.eINSTANCE.getCargo_DischargeSlot().getName() + DefaultClassImporter.DOT + e.getKey(), e.getValue());
					}
					data.add(result);
				}
			}
		}

		// Filter Data
		for (final Map<String, String> map : data) {
			// Remove any matching columns
			for (final String filteredKey : filteredColumns) {
				map.remove(filteredKey);
			}
		}

		return data;
	}

	@Override
	public Collection<EObject> importObject(final EClass eClass, final Map<String, String> row, final IImportContext context) {
		final Collection<EObject> result = super.importObject(eClass, row, context);
		LoadSlot load = null;
		DischargeSlot discharge = null;
		Cargo cargo = null;
		for (final EObject o : result) {
			if (o instanceof Cargo)
				cargo = (Cargo) o;
			else if (o instanceof LoadSlot)
				load = (LoadSlot) o;
			else if (o instanceof DischargeSlot)
				discharge = (DischargeSlot) o;
		}

		// fix missing names

		final List<EObject> newResults = new ArrayList<EObject>(3);
		boolean keepCargo = true;
		if (load.getWindowStart() == null) {
			keepCargo = false;
		} else {
			newResults.add(load);
		}
		if (discharge.getWindowStart() == null) {
			keepCargo = false;
		} else {
			newResults.add(discharge);
		}
		if (!keepCargo) {
			cargo.setLoadSlot(null);
			cargo.setDischargeSlot(null);
		} else {
			newResults.add(cargo);
		}

		if (cargo != null && cargo.getName() != null && cargo.getName().trim().isEmpty() == false) {
			final String cargoName = cargo.getName().trim();
			if (load != null && (load.getName() == null || load.getName().trim().isEmpty())) {
				final LoadSlot load2 = load;
				context.doLater(new IDeferment() {
					@Override
					public void run(final IImportContext context) {
						load2.setName(cargoName);
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
						discharge2.setName("d-" + cargoName);
						context.registerNamedObject(discharge2);
					}

					@Override
					public int getStage() {
						return IImportContext.STAGE_MODIFY_ATTRIBUTES;
					}
				});
			}
		}

		if (keepCargo && row.containsKey(ASSIGNMENT)) {
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
						final NamedObject vessel = context.getNamedObject(assignedTo, FleetPackage.eINSTANCE.getVessel());
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

		return newResults;
	}
}
