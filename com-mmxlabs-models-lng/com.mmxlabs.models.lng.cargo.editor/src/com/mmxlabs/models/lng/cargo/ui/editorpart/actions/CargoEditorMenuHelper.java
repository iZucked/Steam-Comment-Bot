/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchActionConstants;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Days;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.ui.util.CargoTransferUtil;
import com.mmxlabs.models.lng.cargo.util.CargoTravelTimeUtils;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 */
public class CargoEditorMenuHelper {

	private static final String ALLOW_HEDGING = "Allow hedging";

	private static final String NO_HEDGING = "No hedging";

	private static final String ALLOW_EXPOSURES = "Allow exposures";

	private static final String NO_EXPOSURES = "No exposures";

	private final IScenarioEditingLocation scenarioEditingLocation;

	private final CargoEditingCommands cec;
	private final CargoEditingHelper helper;
	private final LNGScenarioModel scenarioModel;

	private static final boolean enableSTSMenus = LicenseFeatures.isPermitted(KnownFeatures.FEATURE_SHIP_TO_SHIP);

	/**
	 */
	public CargoEditorMenuHelper(final Shell shell, final IScenarioEditingLocation scenarioEditingLocation, final LNGScenarioModel scenarioModel) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.scenarioModel = scenarioModel;
		this.helper = new CargoEditingHelper(scenarioEditingLocation.getEditingDomain(), scenarioModel);
		this.cec = new CargoEditingCommands(scenarioEditingLocation.getEditingDomain(), scenarioModel, //
				ScenarioModelUtil.getCargoModel(scenarioModel), //
				ScenarioModelUtil.getCommercialModel(scenarioModel), //
				Activator.getDefault().getModelFactoryRegistry());
	}

	private final class EditAction extends Action {
		private final EObject target;

		private EditAction(final String text, final EObject target) {
			super(text);
			this.target = target;
		}

		@Override
		public void run() {
			DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, target);
		}
	}

	/**
<<<<<<< HEAD
	 * For a given transfer agreement creates an action
	 * which allows creation of the new transfer record
	 * for a list of slots
=======
	 * For a given transfer agreement creates an action which allows creation of the new transfer record
	 * 
>>>>>>> origin/master
	 * @author FM
	 *
	 */
	private final class CreateAddTransferRecordsAction extends Action {
		private final List<Slot<?>> slots;
		private final EObject transferAgreement;

		private CreateAddTransferRecordsAction(final String text, final List<Slot<?>> slots, final EObject transferAgreement) {
			super(text);
			this.slots = slots;
			this.transferAgreement = transferAgreement;
		}

		@Override
		public void run() {

			if (slots != null && !slots.isEmpty()) {
				helper.transferCargoesBetweenEntities(scenarioEditingLocation, "Create transfer record", slots, transferAgreement);
			}

		}
	}

	private final class CreateEditTransferRecordAction extends Action {
		private final Slot<?> slot;

		private CreateEditTransferRecordAction(final Slot<?> slot) {
			super("Edit transfer record");
			this.slot = slot;
		}

		@Override
		public void run() {

			if (slot != null) {
				helper.editTransferRecord(scenarioEditingLocation, slot);
			}

		}
	}

	private void addSetToSubMenu(final IMenuManager manager, final String name, final Slot<?> source, final boolean sourceIsLoad, final Set<Slot<?>> targetSet, final boolean includeContract,
			final boolean includePort) {
		for (final Slot<?> target : targetSet) {
			createWireAction(manager, source, target, sourceIsLoad, includeContract, includePort);
		}
	}

	private void buildSubMenu(final IMenuManager manager, final String name, final Slot<?> source, final boolean sourceIsLoad, final Map<String, Set<Slot<?>>> targets, final boolean includeContract,
			final boolean includePort) {
		final MenuManager subMenu = new MenuManager(name, null);

		// For single item sub menus, skip the sub menu and add item directly
		if (targets.size() == 1) {
			for (final Map.Entry<String, Set<Slot<?>>> e : targets.entrySet()) {
				for (final Slot<?> target : e.getValue()) {
					createWireAction(subMenu, source, target, sourceIsLoad, includeContract, includePort);
				}
			}

		} else {
			for (final Map.Entry<String, Set<Slot<?>>> e : targets.entrySet()) {
				final MenuManager subSubMenu = new MenuManager(e.getKey(), null);
				for (final Slot<?> target : e.getValue()) {
					createWireAction(subSubMenu, source, target, sourceIsLoad, includeContract, includePort);
				}
				subMenu.add(subSubMenu);
			}
		}

		manager.add(subMenu);
	}

	private void buildSwapMenu(final IMenuManager manager, final String name, final Slot<?> source, final Map<String, Set<Slot<?>>> targets, final boolean isLoad, final boolean includeContract,
			final boolean includePort) {
		final MenuManager subMenu = new MenuManager(name, null);

		// For single item sub menus, skip the sub menu and add item directly
		if (targets.size() == 1) {
			for (final Map.Entry<String, Set<Slot<?>>> e : targets.entrySet()) {
				for (final Slot<?> target : e.getValue()) {
					createSwapAction(subMenu, source, target, isLoad, includeContract, includePort);
				}
			}

		} else {
			for (final Map.Entry<String, Set<Slot<?>>> e : targets.entrySet()) {
				final MenuManager subSubMenu = new MenuManager(e.getKey(), null);
				for (final Slot<?> target : e.getValue()) {
					createSwapAction(subSubMenu, source, target, isLoad, includeContract, includePort);
				}
				subMenu.add(subSubMenu);
			}

		}

		manager.add(subMenu);
	}

	private void createDeleteSlotMenu(final IMenuManager newMenuManager, final Slot<?> slot) {
		final Action deleteAction = new Action("Delete") {
			@Override
			public void run() {

				final CompoundCommand currentWiringCommand = new CompoundCommand("Delete slot");
				currentWiringCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), slot));
				Cargo cargo = null;
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					cargo = loadSlot.getCargo();
				}
				if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					cargo = dischargeSlot.getCargo();
				}
				if (cargo != null) {
					currentWiringCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), cargo));
				}
				scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand);
				{
					cec.verifyCargoModel(((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getCargoModel());
				}
			}
		};
		newMenuManager.add(new Separator());
		newMenuManager.add(deleteAction);

	}

	public IMenuListener createDischargeSlotMenuListener(final List<DischargeSlot> dischargeSlots, final int index) {
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		return new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {

				final DischargeSlot dischargeSlot = dischargeSlots.get(index);
				if (dischargeSlot.getCargo() == null) {
					if (LicenseFeatures.isPermitted("features:menu-item-markto")) {
						final MenuManager markToMenuManager = new MenuManager("Mark to...", null);
						if (!dischargeSlot.isFOBSale()) {
							createSpotMarketMenu(markToMenuManager, SpotType.DES_PURCHASE, dischargeSlot, "");
						}
						createSpotMarketMenu(markToMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, "");
						manager.add(markToMenuManager);
					}
				}

				final MenuManager newMenuManager = new MenuManager("Pair to new...", null);
				manager.add(newMenuManager);
				createNewSlotMenu(newMenuManager, dischargeSlot);
				createMenus(manager, dischargeSlot, dischargeSlot.getCargo(), filterSlotsByCompatibility(dischargeSlot, cargoModel.getLoadSlots()), false);
				if (!dischargeSlot.isFOBSale()) {
					createSpotMarketMenu(newMenuManager, SpotType.DES_PURCHASE, dischargeSlot, " market");
				}
				createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, " market");
				createEditMenu(manager, dischargeSlot, dischargeSlot.getContract(), dischargeSlot.getCargo());
				createDeleteSlotMenu(manager, dischargeSlot);
<<<<<<< HEAD
				
				createAddTransferRecordsMenu(manager, Collections.singletonList(dischargeSlot));
=======

				createAddTransferRecordMenu(manager, dischargeSlot);
>>>>>>> origin/master
				createEditTransferRecordMenu(manager, dischargeSlot);
				if (dischargeSlot.isFOBSale()) {
					createAssignmentMenus(manager, dischargeSlot);
					// createPanamaAssignmentMenus(manager, dischargeSlot);
				} else if (dischargeSlot.getCargo() != null) {

					boolean foundDESPurchase = false;
					for (final Slot<?> s : dischargeSlot.getCargo().getSlots()) {
						if (s instanceof LoadSlot && ((LoadSlot) s).isDESPurchase()) {
							createAssignmentMenus(manager, s);
							foundDESPurchase = true;
						}
					}
					if (!foundDESPurchase) {
						createAssignmentMenus(manager, dischargeSlot.getCargo());
					}
				}

				{
					final Cargo cargo = dischargeSlot.getCargo();
					if (cargo != null) {
						if (cargo.getCargoType() == CargoType.FOB || cargo.getCargoType() == CargoType.DES) {
							if (!cargo.isAllowRewiring()) {
								manager.add(new RunnableAction("Unlock", () -> helper.unlockCargoAssignment("Unlock assignment", cargo)));
							} else if (cargo.isAllowRewiring()) {
								manager.add(new RunnableAction("Lock", () -> helper.lockCargoAssignment("Lock assignment", cargo)));
							}
						}
						manager.add(new RunnableAction("Unpair", () -> helper.unpairCargoes("Unpair", Collections.singleton(cargo))));
					}
				}

				final Contract contract = dischargeSlot.getContract();
				if (contract == null || contract.getContractType() == ContractType.BOTH) {
					createFOBDESSwitchMenu(manager, dischargeSlot);
				}

				if (cargoModel.getCanalBookings() != null) {
					// panamaAssignmentMenu(manager, dischargeSlot);
				}
			}

		};
	}

	public void createLightEditorMenuListener(final IMenuManager manager, final Set<Cargo> cargoes) {
		final CargoModel cm = scenarioModel.getCargoModel();
		if (cargoes.size() == 1) {
			cargoes.forEach(c -> {
				if (cm.getCargoesForExposures().contains(c)) {
					manager.add(new RunnableAction(NO_EXPOSURES, () -> helper.setExposuresForCargoAssignment(NO_EXPOSURES, cm, false, cargoes)));
				} else {
					manager.add(new RunnableAction(ALLOW_EXPOSURES, () -> helper.setExposuresForCargoAssignment(ALLOW_EXPOSURES, cm, true, cargoes)));
				}
				if (cm.getCargoesForHedging().contains(c)) {
					manager.add(new RunnableAction(NO_HEDGING, () -> helper.setHedgingForCargoAssignment(NO_HEDGING, cm, false, cargoes)));
				} else {
					manager.add(new RunnableAction(ALLOW_HEDGING, () -> helper.setHedgingForCargoAssignment(ALLOW_HEDGING, cm, true, cargoes)));
				}
			});
		} else if (cargoes.size() > 1) {
			manager.add(new RunnableAction(ALLOW_EXPOSURES, () -> helper.setExposuresForCargoAssignment(ALLOW_EXPOSURES, cm, true, cargoes)));
			manager.add(new RunnableAction(ALLOW_HEDGING, () -> helper.setHedgingForCargoAssignment(ALLOW_HEDGING, cm, true, cargoes)));
			manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			manager.add(new RunnableAction(NO_EXPOSURES, () -> helper.setExposuresForCargoAssignment(NO_EXPOSURES, cm, false, cargoes)));
			manager.add(new RunnableAction(NO_HEDGING, () -> helper.setHedgingForCargoAssignment(NO_HEDGING, cm, false, cargoes)));
		}
	}

	private void createEditMenu(final IMenuManager newMenuManager, final Slot<?> slot, final Contract contract, final Cargo cargo) {
		newMenuManager.add(new Separator());
		newMenuManager.add(new EditAction("Edit Slot", slot));
		if (contract != null) {
			newMenuManager.add(new EditAction("Edit Contract", contract));
		}
		if (cargo != null) {
			newMenuManager.add(new EditAction("Edit Cargo", cargo));
		}
	}

	private void createAssignmentMenus(final IMenuManager menuManager, final Slot<?> slot) {
		menuManager.add(new Separator());

		if (slot != null) {
			final MenuManager reassignMenuManager = new MenuManager("Assign to...", null);
			menuManager.add(reassignMenuManager);

			class AssignAction extends Action {
				private final Vessel vessel;

				public AssignAction(final String label, final Vessel vessel) {
					super(label);
					this.vessel = vessel;
				}

				@Override
				public void run() {
					helper.assignNominatedVessel(String.format("Assign to %s (%dk)", vessel.getName(), vessel.getVesselOrDelegateCapacity() / 1000), slot, vessel);
				}
			}

			final IReferenceValueProviderFactory valueProviderFactory = Activator.getDefault()
					.getReferenceValueProviderFactoryRegistry()
					.getValueProviderFactory(CargoPackage.eINSTANCE.getSlot(), CargoPackage.eINSTANCE.getSlot_NominatedVessel());

			final IReferenceValueProvider valueProvider = valueProviderFactory.createReferenceValueProvider(CargoPackage.eINSTANCE.getSlot(), CargoPackage.eINSTANCE.getSlot_NominatedVessel(),
					scenarioModel);
			final List<Pair<String, EObject>> allowedValues = valueProvider.getAllowedValues(slot, CargoPackage.eINSTANCE.getSlot_NominatedVessel());
			if (allowedValues.size() > 15) {
				int counter = 0;
				MenuManager m = new MenuManager("...", null);
				String firstEntry = null;
				String lastEntry = null;
				for (final Pair<String, EObject> p : allowedValues) {
					if (p.getSecond() == null) {
						continue;
					}
					if (p.getSecond() == slot.getNominatedVessel()) {
						continue;
					}
					final int capacity = ((Vessel) p.getSecond()).getVesselOrDelegateCapacity();
					m.add(new AssignAction(String.format("%s (%dk)", p.getFirst(), capacity / 1000), (Vessel) p.getSecond()));
					counter++;
					if (firstEntry == null) {
						firstEntry = p.getFirst();
					}
					lastEntry = p.getFirst();

					if (counter == 15) {
						final String title = String.format("%s ... %s", firstEntry, lastEntry);
						m.setMenuText(title);
						reassignMenuManager.add(m);
						m = new MenuManager("...", null);
						counter = 0;
						firstEntry = null;
						lastEntry = null;
					}
				}
				if (counter > 0) {
					final String title = String.format("%s ... %s", firstEntry, lastEntry);
					m.setMenuText(title);
					reassignMenuManager.add(m);
				}
			} else {
				for (final Pair<String, EObject> p : allowedValues) {
					if (p.getSecond() != slot.getNominatedVessel()) {
						final Vessel vessel = (Vessel) p.getSecond();
						if (vessel != null) {
							final int capacity = vessel.getVesselOrDelegateCapacity();
							reassignMenuManager.add(new AssignAction(String.format("%s (%dk)", p.getFirst(), capacity / 1000), vessel));
						}
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
				menuManager.add(action);
			}

		}

	}

	private void createAssignmentMenus(final IMenuManager menuManager, final Cargo cargo) {
		menuManager.add(new Separator());

		if (cargo != null && cargo.getCargoType() == CargoType.FLEET) {
			final MenuManager reassignMenuManager = new MenuManager("Assign to...", null);
			menuManager.add(reassignMenuManager);

			final MenuManager marketMenu = new MenuManager("Market...", null);
			final MenuManager nominalMenu = new MenuManager("Nominal...", null);
			boolean marketMenuUsed = false;
			boolean nominalMenuUsed = false;

			final IReferenceValueProviderFactory valueProviderFactory = Activator.getDefault()
					.getReferenceValueProviderFactoryRegistry()
					.getValueProviderFactory(CargoPackage.eINSTANCE.getAssignableElement(), CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType());
			final IReferenceValueProvider valueProvider = valueProviderFactory.createReferenceValueProvider(CargoPackage.eINSTANCE.getAssignableElement(),
					CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType(), scenarioModel);

			final List<Pair<String, EObject>> vesselCharterOptions = new LinkedList<>();

			for (final Pair<String, EObject> p : valueProvider.getAllowedValues(cargo, CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType())) {
				final EObject assignmentOption = p.getSecond();
				if (assignmentOption == null) {
					continue;
				}

				if (assignmentOption instanceof CharterInMarket) {
					final CharterInMarket charterInMarket = (CharterInMarket) assignmentOption;
					final Vessel vessel = charterInMarket.getVessel();
					final int capacity = vessel == null ? 0 : vessel.getVesselOrDelegateCapacity();
					if (charterInMarket.isNominal()) {
						nominalMenuUsed = true;
						nominalMenu.add(new RunnableAction(String.format("%s (%dk)", charterInMarket.getName(), capacity / 1000),
								() -> helper.assignCargoToSpotCharterIn(String.format("Assign to %s", charterInMarket.getName()), cargo, charterInMarket, -1)));
					}
					if (charterInMarket.isEnabled() && charterInMarket.getSpotCharterCount() > 0) {

						if (charterInMarket.getSpotCharterCount() == 1) {
							marketMenu.add(new RunnableAction(String.format("%s (%dk)", charterInMarket.getName(), capacity / 1000),
									() -> helper.assignCargoToSpotCharterIn(String.format("Assign to %s", charterInMarket.getName()), cargo, charterInMarket, 0)));
							marketMenuUsed = true;
						} else {

							final MenuManager marketOptionMenu = new MenuManager(String.format("%s...", charterInMarket.getName()), null);

							for (int i = 0; i < charterInMarket.getSpotCharterCount(); ++i) {
								final int spotIndex = i;
								marketOptionMenu.add(new RunnableAction(String.format("Option %d ", i + 1),
										() -> helper.assignCargoToSpotCharterIn(String.format("Assign to %s", charterInMarket.getName()), cargo, charterInMarket, spotIndex)));
							}

							marketMenu.add(marketOptionMenu);
							marketMenuUsed = true;
						}
					}
				} else if (assignmentOption instanceof VesselCharter) {
					final VesselCharter vesselCharter = (VesselCharter) assignmentOption;

					if (vesselCharter != cargo.getVesselAssignmentType()) {
						vesselCharterOptions.add(p);

						// reassignMenuManager.add(new RunnableAction(p.getFirst(), () -> helper.assignCargoToVesselCharter(String.format("Assign to %s", p.getFirst()), cargo,
						// vesselCharter)));
					}
				} else {
					assert false;
				}
			}
			{
				if (vesselCharterOptions.size() > 15) {
					int counter = 0;
					MenuManager m = new MenuManager("", null);
					String firstEntry = null;
					String lastEntry = null;
					for (final Pair<String, EObject> p : vesselCharterOptions) {
						if (p.getSecond() == null) {
							continue;
						}
						final VesselCharter vesselCharter = (VesselCharter) p.getSecond();
						final Vessel vessel = vesselCharter.getVessel();
						final int capacity = vessel == null ? 0 : vessel.getVesselOrDelegateCapacity();

						m.add(new RunnableAction(String.format("%s (%dk)", p.getFirst(), capacity / 1000),
								() -> helper.assignCargoToVesselCharter(String.format("Assign to %s", p.getFirst()), cargo, (VesselCharter) p.getSecond())));
						counter++;
						if (firstEntry == null) {
							firstEntry = p.getFirst();
						}
						lastEntry = p.getFirst();

						if (counter == 15) {
							final String title = String.format("%s ... %s", firstEntry, lastEntry);
							m.setMenuText(title);
							reassignMenuManager.add(m);
							m = new MenuManager("", null);
							counter = 0;
							firstEntry = null;
							lastEntry = null;
						}
					}
					if (counter > 0) {
						final String title = String.format("%s ... %s", firstEntry, lastEntry);
						m.setMenuText(title);
						reassignMenuManager.add(m);
					}

					// Carve up menu
				} else {
					for (final Pair<String, EObject> p : vesselCharterOptions) {
						final VesselCharter vesselCharter = (VesselCharter) p.getSecond();
						final Vessel vessel = vesselCharter.getVessel();
						final int capacity = vessel == null ? 0 : vessel.getVesselOrDelegateCapacity();

						reassignMenuManager.add(new RunnableAction(String.format("%s (%dk)", p.getFirst(), capacity / 1000),
								() -> helper.assignCargoToVesselCharter(String.format("Assign to %s", p.getFirst()), cargo, (VesselCharter) p.getSecond())));
					}
				}
			}

			if (nominalMenuUsed) {
				if (LicenseFeatures.isPermitted("features:nominals")) {
					reassignMenuManager.add(nominalMenu);
				}
			}
			if (marketMenuUsed) {
				reassignMenuManager.add(marketMenu);
			}
			{
				menuManager.add(new RunnableAction("Unassign", () -> helper.unassignCargoAssignment("Unassign", cargo)));
			}

			{

				if (cargo.getVesselAssignmentType() != null) {
					if (cargo.isLocked() && !cargo.isAllowRewiring()) {
						menuManager.add(new RunnableAction("Unlock", () -> helper.unlockCargoAssignment("Unlock assignment", cargo)));
					} else if (!cargo.isLocked() && cargo.isAllowRewiring()) {
						menuManager.add(new RunnableAction("Lock", () -> helper.lockCargoAssignment("Lock assignment", cargo)));
					}
				} else {
					if (cargo.getCargoType() == CargoType.FOB || cargo.getCargoType() == CargoType.DES) {
						if (!cargo.isAllowRewiring()) {
							menuManager.add(new RunnableAction("Unlock", () -> helper.unlockCargoAssignment("Unlock assignment", cargo)));
						} else if (cargo.isAllowRewiring()) {
							menuManager.add(new RunnableAction("Lock", () -> helper.lockCargoAssignment("Lock assignment", cargo)));
						}
					}
				}
			}

		}
	}

	private void createPanamaAssignmentMenus(final IMenuManager menuManager, final Slot<?> slot) {
		menuManager.add(new Separator());

		if (slot != null) {
			final MenuManager reassignMenuManager = new MenuManager("Panama Assignment", null);
			menuManager.add(reassignMenuManager);
		}
	}

	public IMenuListener createMultipleSelectionMenuListener(final Set<Cargo> cargoes, final Set<LoadSlot> loads, final Set<DischargeSlot> discharges, boolean createLoadSpecificOptions,
			boolean createDischargeSpecificOptions, boolean createVesselSpecificOptions) {

		return manager -> {

			boolean anyLocked = false;
			boolean anyUnlocked = false;

			boolean complexCargo = false;
			for (final Cargo cargo : cargoes) {
				if (cargo.isLocked()) {
					anyLocked = true;
				} else {
					anyUnlocked = true;
				}
				complexCargo |= cargo.getSlots().size() != 2;
			}
			// Can only swap slots on a pair of LD cargoes.
			boolean canSwap = !complexCargo && cargoes.size() == 2;

			if (!loads.isEmpty()) {
				final MenuManager newMenuManager = new MenuManager("Pair to new...", null);
				manager.add(newMenuManager);
				final boolean hasDESPurchase = loads.stream().anyMatch(LoadSlot::isDESPurchase);
				createBulkSpotMarketMenu(newMenuManager, SpotType.DES_SALE, (Collection) loads, " market");
				if (!hasDESPurchase) {
					createBulkSpotMarketMenu(newMenuManager, SpotType.FOB_SALE, (Collection) loads, " market");
				}
				newMenuManager.add(new Separator());
				createAddTransferRecordsMenu(manager, new ArrayList<>(loads));
			} else if (!discharges.isEmpty()) {
				final MenuManager newMenuManager = new MenuManager("Pair to new...", null);
				manager.add(newMenuManager);
				final boolean hasFOBSale = discharges.stream().anyMatch(DischargeSlot::isFOBSale);
				createBulkSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, (Collection) discharges, " market");
				if (!hasFOBSale) {
					createBulkSpotMarketMenu(newMenuManager, SpotType.DES_PURCHASE, (Collection) discharges, " market");
				}
				newMenuManager.add(new Separator());
				createAddTransferRecordsMenu(manager, new ArrayList<>(discharges));
			}
			if (anyLocked) {
				manager.add(new RunnableAction("Unlock", () -> helper.unlockCargoesAssignment("Unlock assignments", cargoes)));
			}

			if (anyUnlocked) {
				manager.add(new RunnableAction("Lock", () -> helper.lockCargoesAssignment("Lock assignments", cargoes)));
			}
			if (!cargoes.isEmpty()) {
				manager.add(new RunnableAction("Unpair", () -> helper.unpairCargoes("Unpair", cargoes)));
			}
			if (canSwap) {
				if (createVesselSpecificOptions) {
					boolean hasDESPurchase = cargoes.stream().filter(c -> c.getCargoType() == CargoType.DES).count() > 0;
					boolean hasFOBSale = cargoes.stream().filter(c -> c.getCargoType() == CargoType.FOB).count() > 0;

					if (!hasDESPurchase && !hasFOBSale) {
						manager.add(new RunnableAction("Swap vessels", () -> helper.swapCargoVessels("Swap vessels", new ArrayList<>(cargoes))));
					}
				}
				if (createLoadSpecificOptions) {
					boolean hasDESPurchase = cargoes.stream().filter(c -> c.getCargoType() == CargoType.DES).count() > 0;
					if (!hasDESPurchase) {
						manager.add(new RunnableAction("Swap loads", () -> helper.swapCargoLoads("Swap loads", new ArrayList<>(cargoes))));
					}
				}
				if (createDischargeSpecificOptions) {
					boolean hasFOBSale = cargoes.stream().filter(c -> c.getCargoType() == CargoType.FOB).count() > 0;
					if (!hasFOBSale) {
						manager.add(new RunnableAction("Swap discharges", () -> helper.swapCargoDischarges("Swap discharges", new ArrayList<>(cargoes))));
					}
				}
			}
		};
	}

	public IMenuListener createLoadSlotMenuListener(final List<LoadSlot> loadSlots, final int index) {
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		return manager -> {
			final LoadSlot loadSlot = loadSlots.get(index);

			if (loadSlot.getCargo() == null) {
				if (LicenseFeatures.isPermitted("features:menu-item-markto")) {
					final MenuManager markToMenuManager = new MenuManager("Mark to...", null);
					createSpotMarketMenu(markToMenuManager, SpotType.DES_SALE, loadSlot, "");
					if (!loadSlot.isDESPurchase()) {
						createSpotMarketMenu(markToMenuManager, SpotType.FOB_SALE, loadSlot, "");
					}
					manager.add(markToMenuManager);
				}
			}

			final MenuManager newMenuManager = new MenuManager("Pair to new...", null);
			manager.add(newMenuManager);
			createNewSlotMenu(newMenuManager, loadSlot);
			createMenus(manager, loadSlot, loadSlot.getCargo(), filterSlotsByCompatibility(loadSlot, cargoModel.getDischargeSlots()), true);
			createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot, " market");
			if (!loadSlot.isDESPurchase()) {
				createSpotMarketMenu(newMenuManager, SpotType.FOB_SALE, loadSlot, " market");
			}

			createEditMenu(manager, loadSlot, loadSlot.getContract(), loadSlot.getCargo());
			createDeleteSlotMenu(manager, loadSlot);
<<<<<<< HEAD
			
			createAddTransferRecordsMenu(manager, Collections.singletonList(loadSlot));
=======

			createAddTransferRecordMenu(manager, loadSlot);
>>>>>>> origin/master
			createEditTransferRecordMenu(manager, loadSlot);

			if (loadSlot.isDESPurchase()) {
				createAssignmentMenus(manager, loadSlot);
				// createPanamaAssignmentMenus(manager, loadSlot);
			} else if (loadSlot.getCargo() != null) {
				boolean foundFobSale = false;
				for (final Slot<?> s : loadSlot.getCargo().getSlots()) {
					if (s instanceof DischargeSlot && ((DischargeSlot) s).isFOBSale()) {
						createAssignmentMenus(manager, s);
						foundFobSale = true;
					}
				}
				if (!foundFobSale) {
					createAssignmentMenus(manager, loadSlot.getCargo());
				}
			}

			{
				final Cargo cargo = loadSlot.getCargo();
				if (cargo != null) {
					if (cargo.getCargoType() == CargoType.FOB || cargo.getCargoType() == CargoType.DES) {
						if (!cargo.isAllowRewiring()) {
							manager.add(new RunnableAction("Unlock", () -> helper.unlockCargoAssignment("Unlock assignment", cargo)));
						} else if (cargo.isAllowRewiring()) {
							manager.add(new RunnableAction("Lock", () -> helper.lockCargoAssignment("Lock assignment", cargo)));
						}
					}
					manager.add(new RunnableAction("Unpair", () -> helper.unpairCargoes("Unpair", Collections.singleton(cargo))));
				}
			}

			final Contract contract = loadSlot.getContract();
			if (contract == null || contract.getContractType() == ContractType.BOTH) {
				createFOBDESSwitchMenu(manager, loadSlot);
			}
			if (cargoModel.getCanalBookings() != null) {
				// panamaAssignmentMenu(manager, loadSlot);
			}

		};
	}
	
	private void createAddTransferRecordsMenu(IMenuManager manager, final List<Slot<?>> slots) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TRANSFER_MODEL)) {
			manager.add(new Separator());
			final MenuManager transferMenuManager = new MenuManager("Transfer...", null);
			for (final NamedObject ta : CargoTransferUtil.getTransferAgreementsForMenu(scenarioModel)) {
				transferMenuManager.add(new CreateAddTransferRecordsAction(ta.getName(), slots, ta));
			}
			manager.add(transferMenuManager);
		}
	}

	private void createEditTransferRecordMenu(IMenuManager manager, final Slot<?> slot) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TRANSFER_MODEL) && //
				CargoTransferUtil.isSlotReferencedByTransferRecord(slot, scenarioModel)) {
			manager.add(new CreateEditTransferRecordAction(slot));
		}
	}

	/**
	 */
	public IMenuListener createSwapSlotsMenuListener(final List<Slot<?>> slots, final int index) {
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		return manager -> {
			final Slot<?> slot = slots.get(index);
			final MenuManager newMenuManager = new MenuManager("New...", null);
			manager.add(newMenuManager);
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (!loadSlot.isDESPurchase()) {
					final List<Slot<?>> filteredSlots = new LinkedList<>();
					for (final LoadSlot s : cargoModel.getLoadSlots()) {
						if (!s.isDESPurchase()) {
							filteredSlots.add(s);
						}
					}

					createSwapWithMenus(manager, loadSlot, filteredSlots, true);
				}

			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				if (!dischargeSlot.isFOBSale()) {
					final List<Slot<?>> filteredSlots = new LinkedList<>();
					for (final DischargeSlot s : cargoModel.getDischargeSlots()) {
						if (!s.isFOBSale()) {
							filteredSlots.add(s);
						}
					}
					createSwapWithMenus(manager, dischargeSlot, filteredSlots, false);
				}
			}
		};
	}

	private void createSwapWithMenus(final IMenuManager manager, final Slot<?> source, final List<? extends Slot<?>> possibleTargets, final boolean sourceIsLoad) {

		final Map<String, Set<Slot<?>>> slotsByDate = new TreeMap<>();
		final Map<String, Set<Slot<?>>> slotsByContract = new TreeMap<>();
		final Map<String, Set<Slot<?>>> slotsByPort = new TreeMap<>();

		for (final Slot<?> target : possibleTargets) {

			// Perform some filtering on the possible targets
			if (source.getCargo().getSlots().contains(target)) {
				continue;
			}

			final Contract contract = target.getContract();
			if (contract != null) {
				addTargetByDateToSortedSet(target, contract.getName(), slotsByContract);
			}
			final Port port = target.getPort();
			if (port != null) {
				addTargetByDateToSortedSet(target, port.getName(), slotsByPort);
			}
			addTargetByDateToSortedSet(target, "Any", slotsByDate);
		}
		{
			buildSwapMenu(manager, "Swap Slots By Contract", source, slotsByContract, sourceIsLoad, false, true);
			buildSwapMenu(manager, "Swap Slots By Port", source, slotsByPort, sourceIsLoad, true, false);
		}
	}

	/**
	 * Returns a list of slots among the specified possible targets which are compatible with the specified source slot. FOB purchases may be paired with DES sales or with FOB sales (which must be at
	 * the same port unless the sale is divertible), while DES purchases may be paired only with DES sales (which must be at the same port unless the purchase is divertible).
	 * 
	 * @param source
	 * @param possibleTargets
	 * @return
	 */
	private List<Slot<?>> filterSlotsByCompatibility(final Slot<?> source, final List<? extends Slot<?>> possibleTargets) {

		final List<Slot<?>> filteredSlots = new LinkedList<>();

		for (final Slot<?> slot : possibleTargets) {
			// Check restrictions on both slots
			if (!areUnsortedSlotsCompatible(source, slot)) {
				continue;
			}
			if (!checkSourceContractConstraints(source, slot) || !checkSourceContractConstraints(slot, source)) {
				continue;
			}
			filteredSlots.add(slot);
		}
		return filteredSlots;
	}

	private boolean areSlotWindowsCompatible(final LoadSlot load, final DischargeSlot discharge) {
		final ZonedDateTime loadStart = load.getSchedulingTimeWindow().getStart();
		final ZonedDateTime loadEnd = load.getSchedulingTimeWindow().getEndWithFlex();
		final ZonedDateTime dischargeStart = discharge.getSchedulingTimeWindow().getStart();
		final ZonedDateTime dischargeEnd = discharge.getSchedulingTimeWindow().getEndWithFlex();

		// slots with unknown time windows are incompatible
		if (loadStart == null || dischargeStart == null || loadEnd == null || dischargeEnd == null) {
			return false;
		}

		// can never load before discharging
		if (loadStart.isAfter(dischargeEnd)) {
			return false;
		}

		final boolean overlap = (dischargeStart.isBefore(loadEnd));

		// TODO: Check the change in rounding - does this round down as the previous code did?
		final int daysDifference = Days.between(loadStart, dischargeEnd);

		// DES load
		if (load.isDESPurchase()) {
			// divertible DES - discharge time should be within shipping restriction window for load slot
			if (load.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
				final int restriction = load.getSlotOrDelegateShippingDaysRestriction();
				return (daysDifference <= restriction);
			}
			// regular DES - windows should overlap
			else {
				return overlap;
			}
		}
		// FOB load
		else {
			// FOB sale - windows should overlap
			if (discharge.isFOBSale()) {
				if (discharge.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
					final int restriction = discharge.getSlotOrDelegateShippingDaysRestriction();
					return (daysDifference <= restriction);
				}

				return overlap;
			}
			// shipped cargo
			else {
				return true;
			}
		}

	}

	private boolean areSlotsCompatible(final LoadSlot load, final DischargeSlot discharge) {
		// DES purchase not compatible with FOB sale
		if (load.isDESPurchase() && discharge.isFOBSale()) {
			return false;
		}

		// check that window timings are compatible
		if (!areSlotWindowsCompatible(load, discharge)) {
			return false;
		}

		// DES purchase
		if (load.isDESPurchase()) {
			// FOB sale - incompatible
			if (discharge.isFOBSale()) {
				return false;
			}
			// DES sale - only at the same port or divertible
			else {
				return load.getSlotOrDelegateDESPurchaseDealType() != DESPurchaseDealType.DEST_ONLY || (load.getPort() == discharge.getPort());
			}
		}
		// FOB purchase
		else {
			// FOB sale - only at the same port or divertible
			if (discharge.isFOBSale()) {
				return discharge.getSlotOrDelegateFOBSaleDealType() != FOBSaleDealType.SOURCE_ONLY || (load.getPort() == discharge.getPort());
			}
			// DES sale - compatible
			else {
				return true;
			}
		}

	}

	/**
	 * Decides whether two slots are compatible.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean areUnsortedSlotsCompatible(final Slot<?> a, final Slot<?> b) {
		// need load / discharge or discharge / load
		if (a instanceof LoadSlot && b instanceof DischargeSlot) {
			return areSlotsCompatible((LoadSlot) a, (DischargeSlot) b);
		} else if (a instanceof DischargeSlot && b instanceof LoadSlot) {
			return areSlotsCompatible((LoadSlot) b, (DischargeSlot) a);
		} else {
			return false;
		}
	}

	/**
	 * Given a source slot, check that the target slot is compatible with the source slot contract restrictions.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	private boolean checkSourceContractConstraints(final Slot<?> source, final Slot<?> target) {
		final List<APortSet<Port>> restrictedPorts = source.getSlotOrDelegatePortRestrictions();
		final boolean areRestrictedPortsListsPermissive = source.getSlotOrDelegatePortRestrictionsArePermissive();

		if (restrictedPorts.contains(target.getPort()) != areRestrictedPortsListsPermissive) {
			return false;
		}

		final List<? extends Contract> restrictedContracts = source.getSlotOrDelegateContractRestrictions();
		final boolean areRestrictedContractListsPermissive = source.getSlotOrDelegateContractRestrictionsArePermissive();
		if (restrictedContracts.contains(target.getContract()) != areRestrictedContractListsPermissive) {
			return false;
		}

		return true;
	}

	private void createMenus(final IMenuManager manager, final Slot<?> source, final Cargo sourceCargo, final List<? extends Slot<?>> possibleTargets, final boolean sourceIsLoad) {

		final Map<String, Set<Slot<?>>> unusedSlotsByDate = new TreeMap<>();
		final Set<Slot<?>> nearSlotsByDate = createSlotTreeSet();
		final Map<String, Set<Slot<?>>> slotsByDate = new TreeMap<>();
		final Map<String, Set<Slot<?>>> slotsByContract = new TreeMap<>();
		final Map<String, Set<Slot<?>>> slotsByPort = new TreeMap<>();

		for (final Slot<?> target : possibleTargets) {

			final Cargo targetCargo;
			final int daysDifference;
			// Filter out some of the possible targets
			{
				final LoadSlot loadSlot;
				final DischargeSlot dischargeSlot;
				if (sourceIsLoad) {
					loadSlot = (LoadSlot) source;
					dischargeSlot = (DischargeSlot) target;
					targetCargo = dischargeSlot.getCargo();
				} else {
					loadSlot = (LoadSlot) target;
					dischargeSlot = (DischargeSlot) source;
					targetCargo = loadSlot.getCargo();
				}
				// Filter out current pairing
				if (sourceCargo != null && sourceCargo == targetCargo) {
					continue;
				}
				// TODO: Check the change in rounding - does this round down as the previous code did?
				final ZonedDateTime a = loadSlot.getSchedulingTimeWindow().getStart();
				final ZonedDateTime b = dischargeSlot.getSchedulingTimeWindow().getStart();
				if (a != null && b != null) {
					daysDifference = Days.between(a, b);
				} else {
					daysDifference = -1;
				}
			}

			if (targetCargo == null) {
				addTargetByDateToSortedSet(target, "Unused", unusedSlotsByDate);
			}

			final Contract contract = target.getContract();
			if (contract != null) {
				addTargetByDateToSortedSet(target, contract.getName(), slotsByContract);
			}
			final Port port = target.getPort();
			if (port != null) {
				addTargetByDateToSortedSet(target, port.getName(), slotsByPort);
			}
			if (daysDifference == -1) {

			} else if (daysDifference <= 60) {
				// addTargetByDateToSortedSet(target, "Less than 30 Days", slotsByDate);
				nearSlotsByDate.add(target);
			} else if (daysDifference > 60 && daysDifference <= 90) {
				addTargetByDateToSortedSet(target, "[>60 Days]", slotsByDate);
			}
		}
		{
			buildSubMenu(manager, sourceIsLoad ? "Shorts" : "Longs", source, sourceIsLoad, unusedSlotsByDate, false, true);
			final MenuManager allMenu = new MenuManager("All", null);
			manager.add(allMenu);
			addSetToSubMenu(allMenu, "Close dates", source, sourceIsLoad, nearSlotsByDate, true, true);
			buildSubMenu(allMenu, "More...", source, sourceIsLoad, slotsByDate, true, true);
			allMenu.add(new Separator());
			buildSubMenu(allMenu, "By contract", source, sourceIsLoad, slotsByContract, false, true);
			buildSubMenu(allMenu, "By port", source, sourceIsLoad, slotsByPort, true, true);
		}
	}

	void createNewSlotMenu(final IMenuManager menuManager, final Slot<?> source) {

		final List<Port> transferPorts = new LinkedList<>();
		for (final Port p : scenarioModel.getReferenceModel().getPortModel().getPorts()) {
			if (p.getCapabilities().contains(PortCapability.TRANSFER)) {
				transferPorts.add(p);
			}
		} // Sort by name
		Collections.sort(transferPorts, new Comparator<Port>() {

			@Override
			public int compare(final Port o1, final Port o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		if (source instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) source;
			if (loadSlot.isDESPurchase()) {
				// Only create new Discharge
				menuManager.add(new CreateSlotAction("DES Sale", Collections.singletonList(source), null, false));
			} else {
				//
				menuManager.add(new CreateSlotAction("DES Sale", Collections.singletonList(source), null, false));
				menuManager.add(new CreateSlotAction("FOB Sale", Collections.singletonList(source), null, true));

				if (enableSTSMenus) {
					if (loadSlot.getTransferFrom() == null) {
						if (!transferPorts.isEmpty()) {
							final MenuManager subMenu = new MenuManager("Ship to Ship", null);

							for (final Port p : transferPorts) {
								subMenu.add(new CreateSTSSlotAction(p.getName(), source, null, false, p));
							}
							menuManager.add(subMenu);
						}
					}
				}
			}
		} else {
			final DischargeSlot dischargeSlot = (DischargeSlot) source;
			menuManager.add(new CreateSlotAction("FOB Purchase", Collections.singletonList(source), null, false));
			if (!dischargeSlot.isFOBSale()) {
				menuManager.add(new CreateSlotAction("DES Purchase", Collections.singletonList(source), null, true));
			}
			if (enableSTSMenus) {

				if (dischargeSlot.getTransferTo() == null) {
					if (!transferPorts.isEmpty()) {
						final MenuManager subMenu = new MenuManager("Ship to Ship", null);

						for (final Port p : transferPorts) {
							subMenu.add(new CreateSTSSlotAction(p.getName(), source, null, false, p));
						}
						menuManager.add(subMenu);
					}
				}
			}
		}
	}

	private TreeSet<Slot<?>> createSlotTreeSet() {
		return new TreeSet<>((o1, o2) -> {
			if (o1.getWindowStart() == null) {
				return -1;
			} else if (o2.getWindowStart() == null) {
				return 1;
			}
			return o1.getWindowStart().compareTo(o2.getWindowStart());
		});

	}

	void createSpotMarketMenu(final IMenuManager manager, final SpotType spotType, final Slot<?> source, final String marketMenuSuffix) {
		if (source instanceof SpotSlot) {
			return;
		}
		final SpotMarketsModel pricingModel = scenarioModel.getReferenceModel().getSpotMarketsModel();
		final Collection<SpotMarket> validMarkets = new LinkedList<>();
		String menuName = "";
		boolean isSpecial = false;
		if (spotType == SpotType.DES_PURCHASE) {
			menuName = "DES";
			final SpotMarketGroup group = pricingModel.getDesPurchaseSpotMarket();
			for (final SpotMarket market : group.getMarkets()) {
				final Set<Port> ports = SetUtils.getObjects(((DESPurchaseMarket) market).getDestinationPorts());
				if (ports.contains(source.getPort())) {
					validMarkets.add(market);
				}
			}
			isSpecial = true;
		} else if (spotType == SpotType.DES_SALE) {
			menuName = "DES";
			validMarkets.addAll(pricingModel.getDesSalesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_PURCHASE) {
			menuName = "FOB";
			validMarkets.addAll(pricingModel.getFobPurchasesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_SALE) {
			menuName = "FOB";
			final SpotMarketGroup group = pricingModel.getFobSalesSpotMarket();
			for (final SpotMarket market : group.getMarkets()) {
				final Set<Port> originPorts = SetUtils.getObjects(((FOBSalesMarket) market).getOriginPorts());
				if (originPorts != null && originPorts.contains(source.getPort())) {
					validMarkets.add(market);
				}
			}
			isSpecial = true;
		}
		final MenuManager subMenu = new MenuManager(menuName + marketMenuSuffix, null);

		if (source.getWindowStart() != null) {
			for (final SpotMarket market : validMarkets) {
				subMenu.add(new CreateSlotAction(market.getName(), Collections.singletonList(source), market, isSpecial));
			}
		}

		manager.add(subMenu);

	}

	void createBulkSpotMarketMenu(final IMenuManager manager, final SpotType spotType, final Collection<Slot<?>> sourceSlots, final String marketMenuSuffix) {
		for (final Slot<?> slot : sourceSlots) {
			if (slot instanceof SpotSlot)
				return;
		}
		final SpotMarketsModel pricingModel = scenarioModel.getReferenceModel().getSpotMarketsModel();
		final Collection<SpotMarket> validMarkets = new LinkedList<>();
		String menuName = "";
		boolean isSpecial = false;
		if (spotType == SpotType.DES_PURCHASE) {
			menuName = "DES";
			final SpotMarketGroup group = pricingModel.getDesPurchaseSpotMarket();
			for (final SpotMarket market : group.getMarkets()) {
				final Set<Port> ports = SetUtils.getObjects(((DESPurchaseMarket) market).getDestinationPorts());
				if (ports.isEmpty()) {
					validMarkets.add(market);
				} else {
					boolean valid = true;
					for (final Slot<?> source : sourceSlots) {
						if (!ports.contains(source.getPort())) {
							valid = false;
							break;
						}
					}
					if (valid) {
						validMarkets.add(market);
					}
				}
			}
			isSpecial = true;
		} else if (spotType == SpotType.DES_SALE) {
			menuName = "DES";
			validMarkets.addAll(pricingModel.getDesSalesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_PURCHASE) {
			menuName = "FOB";
			validMarkets.addAll(pricingModel.getFobPurchasesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_SALE) {
			menuName = "FOB";
			final SpotMarketGroup group = pricingModel.getFobSalesSpotMarket();
			for (final SpotMarket market : group.getMarkets()) {
				final Set<Port> originPorts = SetUtils.getObjects(((FOBSalesMarket) market).getOriginPorts());
				if (originPorts.isEmpty()) {
					validMarkets.add(market);
				} else {
					boolean valid = true;
					for (final Slot<?> source : sourceSlots) {
						if (!originPorts.contains(source.getPort())) {
							valid = false;
							break;
						}
					}
					if (valid) {
						validMarkets.add(market);
					}
				}
			}
			isSpecial = true;
		}
		final MenuManager subMenu = new MenuManager(menuName + marketMenuSuffix, null);

		for (final SpotMarket market : validMarkets) {
			subMenu.add(new CreateSlotAction(market.getName(), sourceSlots, market, isSpecial));
		}

		manager.add(subMenu);

	}

	private void createWireAction(final IMenuManager subMenu, final Slot<?> source, final Slot<?> target, final boolean sourceIsLoad, final boolean includeContract, final boolean includePort) {
		final String name = getActionName(target, !sourceIsLoad, includeContract, includePort);
		if (sourceIsLoad) {
			subMenu.add(new RunnableAction(name, () -> helper.pairSlotsIntoCargo("Rewire Cargoes", (LoadSlot) source, (DischargeSlot) target)));
		} else {
			subMenu.add(new RunnableAction(name, () -> helper.pairSlotsIntoCargo("Rewire Cargoes", (LoadSlot) target, (DischargeSlot) source)));
		}
	}

	private void createSwapAction(final MenuManager subMenu, final Slot<?> source, final Slot<?> target, final boolean isLoad, final boolean includeContract, final boolean includePort) {
		final String name = getActionName(target, isLoad, includeContract, includePort);
		subMenu.add(new SwapAction(name, source, target));
	}

	private String getActionName(final Slot<?> slot, final boolean isLoad, final boolean includeContract, final boolean includePort) {
		final StringBuilder sb = new StringBuilder();

		sb.append(formatDate(slot.getWindowStart()));
		if (includePort && slot.getPort() != null) {
			sb.append(", " + slot.getPort().getName());
		}
		if (slot instanceof SpotSlot) {
			sb.append(", " + ((SpotSlot) slot).getMarket().getName());
		} else {
			sb.append(", " + slot.getName());
		}

		return sb.toString();
	}

	private void addTargetByDateToSortedSet(final Slot<?> target, final String group, final Map<String, Set<Slot<?>>> targets) {
		Set<Slot<?>> targetGroupSlots;
		if (targets.containsKey(group)) {
			targetGroupSlots = targets.get(group);
		} else {
			targetGroupSlots = createSlotTreeSet();
			targets.put(group, targetGroupSlots);
		}
		targetGroupSlots.add(target);
	}

	class CreateSTSSlotAction extends Action {

		private final Slot<?> source;
		private final SpotMarket market;
		private final boolean sourceIsLoad;
		private final boolean isDesPurchaseOrFobSale;
		private final Port shipToShipPort;

		public CreateSTSSlotAction(final String name, final Slot<?> source, final SpotMarket market, final boolean isDesPurchaseOrFobSale, final Port shipToShipPort) {
			super(name);
			this.source = source;
			this.market = market;
			this.sourceIsLoad = source instanceof LoadSlot;
			this.shipToShipPort = shipToShipPort;
			this.isDesPurchaseOrFobSale = isDesPurchaseOrFobSale;
		}

		@Override
		public void run() {
			final CargoModel cargoModel = scenarioModel.getCargoModel();

			final List<Command> setCommands = new LinkedList<>();
			final List<EObject> deleteObjects = new LinkedList<>();

			// when we create a ship to ship linked slot, we don't rewire the cargoes
			if (shipToShipPort != null) {
				cec.insertShipToShipSlots(setCommands, source, cargoModel, shipToShipPort);
			}
			// when we create another slot, we rewire the cargoes
			else {
				LoadSlot loadSlot;
				DischargeSlot dischargeSlot;
				if (sourceIsLoad) {
					loadSlot = (LoadSlot) source;
					if (market == null) {
						dischargeSlot = cec.createNewDischarge(setCommands, cargoModel, isDesPurchaseOrFobSale);
						dischargeSlot.setWindowStart(source.getWindowStart());
					} else {
						dischargeSlot = cec.createNewSpotDischarge(setCommands, cargoModel, market);
						// Get start of month and create full sized window
						ZonedDateTime cal = source.getSchedulingTimeWindow().getStart();
						// Take into account travel time
						if (loadSlot.isDESPurchase() && loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
							final int travelTime = getTravelTime(loadSlot, dischargeSlot, loadSlot.getNominatedVessel(), scenarioEditingLocation.getScenarioDataProvider());
							cal = cal.plusHours(travelTime);
							cal = cal.plusHours(loadSlot.getSchedulingTimeWindow().getDuration());
						} else if (!loadSlot.isDESPurchase() && !dischargeSlot.isFOBSale()) {

							Vessel assignedVessel = null;
							if (loadSlot.getCargo() != null) {
								final VesselAssignmentType vesselAssignmentType = loadSlot.getCargo().getVesselAssignmentType();
								if (vesselAssignmentType instanceof VesselCharter) {
									assignedVessel = ((VesselCharter) vesselAssignmentType).getVessel();
								} else if (vesselAssignmentType instanceof CharterInMarket) {
									assignedVessel = ((CharterInMarket) vesselAssignmentType).getVessel();
								} else if (vesselAssignmentType instanceof CharterInMarketOverride) {
									assignedVessel = ((CharterInMarketOverride) vesselAssignmentType).getCharterInMarket().getVessel();
								}
							}
							final int travelTime = getTravelTime(loadSlot, dischargeSlot, assignedVessel, scenarioEditingLocation.getScenarioDataProvider());
							if (travelTime == Integer.MAX_VALUE) {
								final String message = String.format("Can not determine travel time between %s and %s. \n Travel time can not be %d hours.", loadSlot.getPort().getName(),
										dischargeSlot.getPort().getName(), travelTime);
								throw new RuntimeException(message);
							}
							cal = cal.plusHours(travelTime);
							cal = cal.plusHours(loadSlot.getSchedulingTimeWindow().getDuration());
						}

						// Get existing names
						final Set<String> usedIDStrings = new HashSet<>();
						for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
							usedIDStrings.add(slot.getName());
						}

						// Set back to start of month
						cal = cal.withDayOfMonth(1).withHour(0);
						final LocalDate dishargeCal = cal.toLocalDate();
						final String yearMonthString = getKeyForDate(dishargeCal);
						dischargeSlot.setWindowStart(dishargeCal);
						dischargeSlot.setWindowStartTime(0);

						dischargeSlot.setWindowSize(1);
						dischargeSlot.setWindowSizeUnits(TimePeriod.MONTHS);

						final String idPrefix = market.getName() + "-" + yearMonthString + "-";
						int i = 0;
						String id = idPrefix + (i++);
						while (usedIDStrings.contains(id)) {
							id = idPrefix + (i++);
						}
						dischargeSlot.setName(id);

					}
					if (dischargeSlot.isFOBSale()) {
						dischargeSlot.setPort(source.getPort());
					}
				} else {
					dischargeSlot = (DischargeSlot) source;
					if (market == null) {
						loadSlot = cec.createNewLoad(setCommands, cargoModel, isDesPurchaseOrFobSale);
						loadSlot.setWindowStart(source.getWindowStart());
					} else {
						loadSlot = cec.createNewSpotLoad(setCommands, cargoModel, isDesPurchaseOrFobSale, market);
						// Get start of month and create full sized window
						ZonedDateTime cal = source.getSchedulingTimeWindow().getStart();
						// Take into account travel time
						if (!dischargeSlot.isFOBSale() && !loadSlot.isDESPurchase()) {

							Vessel assignedVessel = null;
							if (loadSlot.getCargo() != null) {
								final VesselAssignmentType vesselAssignmentType = loadSlot.getCargo().getVesselAssignmentType();
								if (vesselAssignmentType instanceof VesselCharter) {
									assignedVessel = ((VesselCharter) vesselAssignmentType).getVessel();
								} else if (vesselAssignmentType instanceof CharterInMarket) {
									assignedVessel = ((CharterInMarket) vesselAssignmentType).getVessel();
								} else if (vesselAssignmentType instanceof CharterInMarketOverride) {
									assignedVessel = ((CharterInMarketOverride) vesselAssignmentType).getCharterInMarket().getVessel();
								}
							}
							final int travelTime = getTravelTime(loadSlot, dischargeSlot, assignedVessel, scenarioEditingLocation.getScenarioDataProvider());
							if (travelTime == Integer.MAX_VALUE) {
								final String message = String.format("Can not determine travel time between %s and %s. \n Travel time can not be %d hours.", loadSlot.getPort().getName(),
										dischargeSlot.getPort().getName(), travelTime);
								throw new RuntimeException(message);
							}
							cal = cal.minusHours(travelTime);
							cal = cal.minusHours(loadSlot.getSchedulingTimeWindow().getDuration());
						}

						// Set back to start of month
						cal = cal.withDayOfMonth(1).withHour(0);
						final LocalDate loadCal = cal.toLocalDate();
						final String yearMonthString = getKeyForDate(loadCal);
						loadSlot.setWindowStart(loadCal);
						loadSlot.setWindowStartTime(0);
						// Subtract 1 hour to limit within calendar month
						loadSlot.setWindowSize(1);
						loadSlot.setWindowSizeUnits(TimePeriod.MONTHS);

						// Get existing names
						final Set<String> usedIDStrings = new HashSet<>();
						for (final LoadSlot slot : cargoModel.getLoadSlots()) {
							usedIDStrings.add(slot.getName());
						}

						final String idPrefix = market.getName() + "-" + yearMonthString + "-";
						int i = 0;
						String id = idPrefix + (i++);
						while (usedIDStrings.contains(id)) {
							id = idPrefix + (i++);
						}
						loadSlot.setName(id);

					}
					if (loadSlot.isDESPurchase()) {
						loadSlot.setPort(source.getPort());
					}
				}
				cec.runWiringUpdate(setCommands, deleteObjects, loadSlot, dischargeSlot);
			}

			// make a compound command which adds and sets values before deleting anything

			final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");
			// Process set before delete
			for (final Command c : setCommands) {
				currentWiringCommand.append(c);
			}
			if (!deleteObjects.isEmpty()) {
				currentWiringCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), deleteObjects));
			}

			scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand);
			{
				cec.verifyCargoModel(ScenarioModelUtil.getCargoModel((LNGScenarioModel) scenarioEditingLocation.getRootObject()));
			}

		}
	}

	public static String getKeyForDate(final LocalDate date) {
		return String.format("%04d-%02d", date.getYear(), date.getMonthValue());
	}

	class CreateSlotAction extends Action {

		private final Collection<Slot<?>> sourceSlots;
		private final SpotMarket market;
		private final boolean isDesPurchaseOrFobSale;

		public CreateSlotAction(final String name, final Collection<Slot<?>> sourceSlots, final SpotMarket market, final boolean isDesPurchaseOrFobSale) {
			super(name);
			this.sourceSlots = sourceSlots;
			this.market = market;
			this.isDesPurchaseOrFobSale = isDesPurchaseOrFobSale;
		}

		@Override
		public void run() {
			final CargoModel cargoModel = scenarioModel.getCargoModel();

			final List<Command> setCommands = new LinkedList<>();
			final List<EObject> deleteObjects = new LinkedList<>();

			// Get existing names
			final Set<String> usedDischargeIDStrings = new HashSet<>();
			for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
				usedDischargeIDStrings.add(slot.getName());
			}
			final Set<String> usedLoadIDStrings = new HashSet<>();
			for (final LoadSlot slot : cargoModel.getLoadSlots()) {
				usedLoadIDStrings.add(slot.getName());
			}

			// when we create another slot, we rewire the cargoes
			for (final Slot<?> source : sourceSlots) {
				final boolean sourceIsLoad = source instanceof LoadSlot;
				LoadSlot loadSlot;
				DischargeSlot dischargeSlot;
				if (sourceIsLoad) {
					loadSlot = (LoadSlot) source;
					if (market == null) {
						dischargeSlot = cec.createNewDischarge(setCommands, cargoModel, isDesPurchaseOrFobSale);
						dischargeSlot.setWindowStart(loadSlot.getWindowStart());
					} else {
						dischargeSlot = cec.createNewSpotDischarge(setCommands, cargoModel, market);
						makeUpDischargeSlot(usedDischargeIDStrings, loadSlot, dischargeSlot, market, scenarioEditingLocation.getScenarioDataProvider(), getAssignedVessel(loadSlot));
					}
				} else {
					dischargeSlot = (DischargeSlot) source;
					if (market == null) {
						loadSlot = cec.createNewLoad(setCommands, cargoModel, isDesPurchaseOrFobSale);
						loadSlot.setWindowStart(source.getWindowStart());
					} else {
						loadSlot = cec.createNewSpotLoad(setCommands, cargoModel, isDesPurchaseOrFobSale, market);
						makeUpLoadSlot(usedLoadIDStrings, loadSlot, dischargeSlot, market, scenarioEditingLocation.getScenarioDataProvider(), getAssignedVessel(loadSlot));
					}
				}
				cec.runWiringUpdate(setCommands, deleteObjects, loadSlot, dischargeSlot);
			}

			// make a compound command which adds and sets values before deleting anything

			final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");
			// Process set before delete
			for (final Command c : setCommands) {
				currentWiringCommand.append(c);
			}
			if (!deleteObjects.isEmpty()) {
				currentWiringCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), deleteObjects));
			}
			if (!currentWiringCommand.isEmpty()) {
				scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand);
				{
					cec.verifyCargoModel(ScenarioModelUtil.getCargoModel((LNGScenarioModel) scenarioEditingLocation.getRootObject()));
				}
			}
		}

		private Vessel getAssignedVessel(final LoadSlot loadSlot) {
			Vessel assignedVessel = null;
			if (loadSlot.getCargo() != null) {
				final VesselAssignmentType vesselAssignmentType = loadSlot.getCargo().getVesselAssignmentType();
				if (vesselAssignmentType instanceof VesselCharter) {
					assignedVessel = ((VesselCharter) vesselAssignmentType).getVessel();
				} else if (vesselAssignmentType instanceof CharterInMarket) {
					assignedVessel = ((CharterInMarket) vesselAssignmentType).getVessel();
				} else if (vesselAssignmentType instanceof CharterInMarketOverride) {
					assignedVessel = ((CharterInMarketOverride) vesselAssignmentType).getCharterInMarket().getVessel();
				}
			}
			return assignedVessel;
		}
	}

	public static void makeUpLoadSlot(final Set<String> usedLoadIDStrings, final LoadSlot loadSlot, final DischargeSlot dischargeSlot, final SpotMarket market, final IScenarioDataProvider sdp,
			final Vessel assignedVessel) {
		// Get start of month and create full sized window
		ZonedDateTime cal = dischargeSlot.getSchedulingTimeWindow().getStart();
		// Take into account travel time
		if (dischargeSlot.isFOBSale() && dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
			final int travelTime = getTravelTime(loadSlot, dischargeSlot, dischargeSlot.getNominatedVessel(), sdp);
			cal = cal.minusHours(travelTime);
			cal = cal.minusHours(loadSlot.getSchedulingTimeWindow().getDuration());
		} else if (!dischargeSlot.isFOBSale() && !loadSlot.isDESPurchase()) {

			final int travelTime = getTravelTime(loadSlot, dischargeSlot, assignedVessel, sdp);
			if (travelTime == Integer.MAX_VALUE) {
				final String message = String.format("Can not determine travel time between %s and %s. \n Travel time can not be %d hours.", loadSlot.getPort().getName(),
						dischargeSlot.getPort().getName(), travelTime);
				throw new RuntimeException(message);
			}
			cal = cal.minusHours(travelTime);
			cal = cal.minusHours(loadSlot.getSchedulingTimeWindow().getDuration());
		}

		// Set back to start of month
		cal = cal.withDayOfMonth(1).withHour(0);
		final LocalDate loadCal = cal.toLocalDate();
		final String yearMonthString = getKeyForDate(loadCal);
		loadSlot.setWindowStart(loadCal);
		loadSlot.setWindowStartTime(0);
		// Subtract 1 hour to limit within calendar month
		loadSlot.setWindowSize(1);
		loadSlot.setWindowSizeUnits(TimePeriod.MONTHS);

		final String idPrefix = market.getName() + "-" + yearMonthString + "-";
		int i = 0;
		String id = idPrefix + (i++);
		while (usedLoadIDStrings.contains(id)) {
			id = idPrefix + (i++);
		}

		loadSlot.setName(id);
		usedLoadIDStrings.add(id);
		if (loadSlot.isDESPurchase()) {
			loadSlot.setPort(dischargeSlot.getPort());
		}
	}

	public static void makeUpDischargeSlot(final Set<String> usedDischargeIDStrings, final LoadSlot loadSlot, final DischargeSlot dischargeSlot, final SpotMarket market,
			final IScenarioDataProvider sdp, final Vessel assignedVessel) {
		// Get start of month and create full sized window
		ZonedDateTime cal = loadSlot.getSchedulingTimeWindow().getStart();
		// Take into account travel time
		if (loadSlot.isDESPurchase() && loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
			final int travelTime = getTravelTime(loadSlot, dischargeSlot, loadSlot.getNominatedVessel(), sdp);
			cal = cal.plusHours(travelTime);
			cal = cal.plusHours(loadSlot.getSchedulingTimeWindow().getDuration());
		} else if (!loadSlot.isDESPurchase() && !dischargeSlot.isFOBSale()) {

			final int travelTime = getTravelTime(loadSlot, dischargeSlot, assignedVessel, sdp);
			if (travelTime == Integer.MAX_VALUE) {
				final String message = String.format("Can not determine travel time between %s and %s. \n Travel time can not be %d hours.", loadSlot.getPort().getName(),
						dischargeSlot.getPort().getName(), travelTime);
				throw new RuntimeException(message);
			}
			cal = cal.plusHours(travelTime);
			cal = cal.plusHours(loadSlot.getSchedulingTimeWindow().getDuration());
		}

		// Set back to start of month
		cal = cal.withDayOfMonth(1).withHour(0);
		final LocalDate dishargeCal = cal.toLocalDate();
		final String yearMonthString = getKeyForDate(dishargeCal);
		dischargeSlot.setWindowStart(dishargeCal);
		dischargeSlot.setWindowStartTime(0);

		dischargeSlot.setWindowSize(1);
		dischargeSlot.setWindowSizeUnits(TimePeriod.MONTHS);

		final String idPrefix = market.getName() + "-" + yearMonthString + "-";
		int i = 0;
		String id = idPrefix + (i++);
		while (usedDischargeIDStrings.contains(id)) {
			id = idPrefix + (i++);
		}
		dischargeSlot.setName(id);
		usedDischargeIDStrings.add(id);
		if (dischargeSlot.isFOBSale()) {
			dischargeSlot.setPort(loadSlot.getPort());
		}
	}

	class SwapAction extends Action {

		private final Slot<?> source;
		private final Slot<?> target;

		public SwapAction(final String text, final Slot<?> source, final Slot<?> target) {
			super(text);
			this.source = source;
			this.target = target;
		}

		@Override
		public void run() {

			final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");

			final Cargo targetCargo = target.getCargo();
			final Cargo c = source.getCargo();

			currentWiringCommand.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), source, CargoPackage.eINSTANCE.getSlot_Cargo(), null));
			currentWiringCommand.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), target, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
			if (targetCargo != null && targetCargo != c && (targetCargo.getSlots().size() - 1) < 2) {
				currentWiringCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), targetCargo));
			}

			// Quick hacky bit to change cargo ID to match new defining load slot id
			if (target instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) target;
				if (loadSlot.getWindowStart() != null && !c.getSlots().isEmpty()) {
					final List<Slot<?>> sortedSlots = c.getSortedSlots();
					final Iterator<Slot<?>> iterator = sortedSlots.iterator();
					while (iterator.hasNext()) {
						final Slot<?> slot = iterator.next();
						// This is the slot we are replacing!
						if (slot == source) {
							continue;
						}

						final ZonedDateTime slotDate = slot.getSchedulingTimeWindow().getStart();
						if (slotDate == null || target.getSchedulingTimeWindow().getEnd().isBefore(slotDate)) {
							currentWiringCommand.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, MMXCorePackage.eINSTANCE.getNamedObject_Name(), target.getName()));
						} else {
							break;
						}
					}
				}
			}

			scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand);
			{
				cec.verifyCargoModel(((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getCargoModel());
			}
		}
	}

	private void createFOBDESSwitchMenu(final IMenuManager manager, final Slot<?> slot) {

		final Contract contract = slot.getContract();
		assert (contract == null || contract.getContractType() == ContractType.BOTH);

		if (slot instanceof SpotSlot) {
			return;
		}
		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (SlotClassifier.classify(loadSlot) == SlotType.FOB_Buy) {
				manager.add(new RunnableAction("Convert to DES Purchase", () -> helper.convertToDESPurchase("Convert to DES Purchase", loadSlot)));
			} else if (SlotClassifier.classify(loadSlot) == SlotType.DES_Buy_AnyDisPort) {
				manager.add(new RunnableAction("Convert to FOB Purchase", () -> helper.convertToFOBPurchase("Convert to FOB Purchase", loadSlot)));
			}
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			if (SlotClassifier.classify(dischargeSlot) == SlotType.FOB_Sale_AnyLoadPort) {
				manager.add(new RunnableAction("Convert to DES Sale", () -> helper.convertToDESSale("Convert to DES Sale", dischargeSlot)));
			} else if (SlotClassifier.classify(dischargeSlot) == SlotType.DES_Sale) {
				manager.add(new RunnableAction("Convert to FOB Sale", () -> helper.convertToFOBSale("Convert to FOB Sale", dischargeSlot)));
			}
		}
	}

	public static int getTravelTime(final Slot<?> from, final Slot<?> to, final @Nullable Vessel vessel, final @NonNull IScenarioDataProvider scenarioDataProvider) {

		double maxSpeed = 19.0;
		if (vessel != null) {
			maxSpeed = vessel.getVesselOrDelegateMaxSpeed();
		}
		final LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(scenarioDataProvider);
		for (final Route route : scenarioModel.getReferenceModel().getPortModel().getRoutes()) {
			if (route.getRouteOption() == RouteOption.DIRECT) {
				return CargoTravelTimeUtils.getTimeForRoute(vessel, maxSpeed, route.getRouteOption(), from, to,
						scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class));
			}
		}

		return Integer.MAX_VALUE;
	}

	private String formatDate(final LocalDate localDate) {
		if (localDate == null) {
			return "<no date>";
		}
		return String.format("%02d %s %04d", localDate.getDayOfMonth(), localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()), localDate.getYear());
	}

}