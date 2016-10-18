/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.common.Pair;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.rcp.common.menus.SubLocalMenuHelper;

/**
 */
public class AssignToMenuHelper {

	private final Shell shell;
	private final CargoEditingHelper helper;
	private final LNGScenarioModel scenarioModel;

	/**
	 */
	public AssignToMenuHelper(final Shell shell, final IScenarioEditingLocation scenarioEditingLocation, final LNGScenarioModel scenarioModel) {
		this.shell = shell;
		this.scenarioModel = scenarioModel;
		this.helper = new CargoEditingHelper(scenarioEditingLocation.getEditingDomain(), scenarioModel);
	}

	public void createAssignmentMenus(final LocalMenuHelper reassignMenuManager, final @NonNull Slot slot) {

		// final LocalMenuHelper reassignMenuManager = new LocalMenuHelper(shell);
		reassignMenuManager.setTitle("Assign to...");

		class AssignAction extends Action {
			private final Vessel vessel;

			public AssignAction(final String label, final Vessel vessel) {
				super(label);
				this.vessel = vessel;
			}

			public void run() {
				helper.assignNominatedVessel(String.format("Assign to %s", vessel.getName()), slot, vessel);
			}
		}

		final IReferenceValueProviderFactory valueProviderFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(CargoPackage.eINSTANCE.getSlot(),
				CargoPackage.eINSTANCE.getSlot_NominatedVessel());

		final IReferenceValueProvider valueProvider = valueProviderFactory.createReferenceValueProvider(CargoPackage.eINSTANCE.getSlot(), CargoPackage.eINSTANCE.getSlot_NominatedVessel(),
				scenarioModel);

		final List<Pair<String, EObject>> allowedValues = valueProvider.getAllowedValues(slot, CargoPackage.eINSTANCE.getSlot_NominatedVessel());
		if (allowedValues.size() > 15) {
			int counter = 0;
			SubLocalMenuHelper m = new SubLocalMenuHelper("");
			String firstEntry = null;
			String lastEntry = null;
			for (final Pair<String, EObject> p : allowedValues) {
				if (p.getSecond() == null) {
					continue;
				}
				if (p.getSecond() == slot.getNominatedVessel()) {
					continue;
				}
				m.addAction(new AssignAction(p.getFirst(), (Vessel) p.getSecond()));
				counter++;
				if (firstEntry == null) {
					firstEntry = p.getFirst();
				}
				lastEntry = p.getFirst();

				if (counter == 15) {
					final String title = String.format("%s ... %s", firstEntry, lastEntry);
					m.setTitle(title);
					reassignMenuManager.addSubMenu(m);
					m = new SubLocalMenuHelper("");
					counter = 0;
					firstEntry = null;
					lastEntry = null;
				}
			}
			if (counter > 0) {
				final String title = String.format("%s ... %s", firstEntry, lastEntry);
				m.setTitle(title);
				reassignMenuManager.addSubMenu(m);
			}

			// Carve up menu
		} else {
			for (final Pair<String, EObject> p : allowedValues) {
				if (p.getSecond() != slot.getNominatedVessel()) {
					reassignMenuManager.addAction(new AssignAction(p.getFirst(), (Vessel) p.getSecond()));
				}
			}
		}
		{
			final Action action = new Action("Unassign") {
				@Override
				public void run() {
					helper.assignNominatedVessel(String.format("Unassign"), slot, null);
				}
			};
			reassignMenuManager.addAction(action);
		}

	}

	public void createAssignmentMenus(final LocalMenuHelper reassignMenuManager, final @NonNull Cargo cargo) {
		// final LocalMenuHelper reassignMenuManager = new LocalMenuHelper(shell);
		reassignMenuManager.setTitle("Assign to...");

		final SubLocalMenuHelper marketMenu = new SubLocalMenuHelper("Market...");
		final SubLocalMenuHelper nominalMenu = new SubLocalMenuHelper("Nominal...");
		boolean marketMenuUsed = false;
		boolean nominalMenuUsed = false;

		final IReferenceValueProviderFactory valueProviderFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry()
				.getValueProviderFactory(CargoPackage.eINSTANCE.getAssignableElement(), CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType());
		final IReferenceValueProvider valueProvider = valueProviderFactory.createReferenceValueProvider(CargoPackage.eINSTANCE.getAssignableElement(),
				CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType(), scenarioModel);

		List<Pair<String, EObject>> vesselAvailabilityOptions = new LinkedList<>();
		for (final Pair<String, EObject> p : valueProvider.getAllowedValues(cargo, CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType())) {
			final EObject assignmentOption = p.getSecond();
			if (assignmentOption == null) {
				continue;
			}
			if (assignmentOption instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) assignmentOption;

				nominalMenuUsed = true;
				nominalMenu.addAction(new RunnableAction(String.format("%s", charterInMarket.getName()),
						() -> helper.assignCargoToSpotCharterIn(String.format("Assign to %s", charterInMarket.getName()), cargo, charterInMarket, -1)));

				if (charterInMarket.isEnabled() && charterInMarket.getSpotCharterCount() > 0) {

					if (charterInMarket.getSpotCharterCount() == 1) {
						marketMenu.addAction(new RunnableAction(String.format("%s", charterInMarket.getName()),
								() -> helper.assignCargoToSpotCharterIn(String.format("Assign to %s", charterInMarket.getName()), cargo, charterInMarket, 0)));
						marketMenuUsed = true;
					} else {

						final SubLocalMenuHelper marketOptionMenu = new SubLocalMenuHelper(String.format("%s...", charterInMarket.getName()));

						for (int i = 0; i < charterInMarket.getSpotCharterCount(); ++i) {
							final int spotIndex = i;
							marketOptionMenu.addAction(new RunnableAction(String.format("Option %d ", i + 1),
									() -> helper.assignCargoToSpotCharterIn(String.format("Assign to %s", charterInMarket.getName()), cargo, charterInMarket, spotIndex)));
						}

						marketMenu.addSubMenu(marketOptionMenu);
						marketMenuUsed = true;
					}
				}
			} else if (assignmentOption instanceof VesselAvailability) {
				final VesselAvailability vesselAvailability = (VesselAvailability) assignmentOption;
				if (assignmentOption != cargo.getVesselAssignmentType()) {
					vesselAvailabilityOptions.add(p);
				}

			} else {
				assert false;
			}
		}

		{
			if (vesselAvailabilityOptions.size() > 15) {
				int counter = 0;
				SubLocalMenuHelper m = new SubLocalMenuHelper("");
				String firstEntry = null;
				String lastEntry = null;
				for (final Pair<String, EObject> p : vesselAvailabilityOptions) {
					if (p.getSecond() == null) {
						continue;
					}

					m.addAction(new RunnableAction(p.getFirst(), () -> helper.assignCargoToVesselAvailability(String.format("Assign to %s", p.getFirst()), cargo, (VesselAvailability) p.getSecond())));
					counter++;
					if (firstEntry == null) {
						firstEntry = p.getFirst();
					}
					lastEntry = p.getFirst();

					if (counter == 15) {
						final String title = String.format("%s ... %s", firstEntry, lastEntry);
						m.setTitle(title);
						reassignMenuManager.addSubMenu(m);
						m = new SubLocalMenuHelper("");
						counter = 0;
						firstEntry = null;
						lastEntry = null;
					}
				}
				if (counter > 0) {
					final String title = String.format("%s ... %s", firstEntry, lastEntry);
					m.setTitle(title);
					reassignMenuManager.addSubMenu(m);
				}

				// Carve up menu
			} else {
				for (Pair<String, EObject> p : vesselAvailabilityOptions) {
					reassignMenuManager.addAction(
							new RunnableAction(p.getFirst(), () -> helper.assignCargoToVesselAvailability(String.format("Assign to %s", p.getFirst()), cargo, (VesselAvailability) p.getSecond())));
				}
			}
		}

		if (nominalMenuUsed) {
			if (LicenseFeatures.isPermitted("features:nominals")) {
				reassignMenuManager.addSubMenu(nominalMenu);
			}
		}
		if (marketMenuUsed) {
			reassignMenuManager.addSubMenu(marketMenu);
		}
		{
			reassignMenuManager.addAction(new RunnableAction("Unassign", () -> helper.unassignCargoAssignment("Unassign", cargo)));
		}
	}
}