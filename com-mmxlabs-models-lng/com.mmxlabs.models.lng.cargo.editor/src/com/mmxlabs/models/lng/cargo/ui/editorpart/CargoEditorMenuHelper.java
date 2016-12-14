/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
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
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Days;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.editor.editors.ldd.ComplexCargoEditor;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 */
public class CargoEditorMenuHelper {

	private final Shell shell;

	private final IScenarioEditingLocation scenarioEditingLocation;

	private final CargoEditingCommands cec;
	private final CargoEditingHelper helper;
	private final LNGScenarioModel scenarioModel;

	private static final boolean enableSTSMenus = LicenseFeatures.isPermitted("features:shiptoship");

	/**
	 */
	public CargoEditorMenuHelper(final Shell shell, final IScenarioEditingLocation scenarioEditingLocation, final LNGScenarioModel scenarioModel) {
		this.shell = shell;
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.scenarioModel = scenarioModel;
		this.helper = new CargoEditingHelper(scenarioEditingLocation.getEditingDomain(), scenarioModel);
		cec = new CargoEditingCommands(scenarioEditingLocation.getEditingDomain(), scenarioModel, ScenarioModelUtil.getCargoModel(scenarioModel), ScenarioModelUtil.getCommercialModel(scenarioModel),
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

			final DetailCompositeDialog dcd = new DetailCompositeDialog(shell, scenarioEditingLocation.getDefaultCommandHandler());
			final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
			try {
				editorLock.claim();
				scenarioEditingLocation.setDisableUpdates(true);

				dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), Collections.<EObject> singletonList(target), scenarioEditingLocation.isLocked());
			} finally {
				scenarioEditingLocation.setDisableUpdates(false);
				editorLock.release();
			}
		}
	}

	private final class EditLDDAction extends Action {
		private final EObject target;

		private EditLDDAction(final String text, final EObject target) {
			super(text);
			this.target = target;
		}

		@Override
		public void run() {

			if (target instanceof Cargo) {
				editLDDCargo((Cargo) target, false);
			}

		}
	}

	private void addSetToSubMenu(final IMenuManager manager, final String name, final Slot source, final boolean sourceIsLoad, final Set<Slot> targetSet, final boolean includeContract,
			final boolean includePort) {
		for (final Slot target : targetSet) {
			createWireAction(manager, source, target, sourceIsLoad, includeContract, includePort);
		}
	}

	private void buildSubMenu(final IMenuManager manager, final String name, final Slot source, final boolean sourceIsLoad, final Map<String, Set<Slot>> targets, final boolean includeContract,
			final boolean includePort) {
		final MenuManager subMenu = new MenuManager(name, null);

		// For single item sub menus, skip the sub menu and add item directly
		if (targets.size() == 1) {
			for (final Map.Entry<String, Set<Slot>> e : targets.entrySet()) {
				for (final Slot target : e.getValue()) {
					createWireAction(subMenu, source, target, sourceIsLoad, includeContract, includePort);
				}
			}

		} else {
			for (final Map.Entry<String, Set<Slot>> e : targets.entrySet()) {
				final MenuManager subSubMenu = new MenuManager(e.getKey(), null);
				for (final Slot target : e.getValue()) {
					createWireAction(subSubMenu, source, target, sourceIsLoad, includeContract, includePort);
				}
				subMenu.add(subSubMenu);
			}
		}

		manager.add(subMenu);
	}

	private void buildSwapMenu(final IMenuManager manager, final String name, final Slot source, final Map<String, Set<Slot>> targets, final boolean isLoad, final boolean includeContract,
			final boolean includePort) {
		final MenuManager subMenu = new MenuManager(name, null);

		// For single item sub menus, skip the sub menu and add item directly
		if (targets.size() == 1) {
			for (final Map.Entry<String, Set<Slot>> e : targets.entrySet()) {
				for (final Slot target : e.getValue()) {
					createSwapAction(subMenu, source, target, isLoad, includeContract, includePort);
				}
			}

		} else {
			for (final Map.Entry<String, Set<Slot>> e : targets.entrySet()) {
				final MenuManager subSubMenu = new MenuManager(e.getKey(), null);
				for (final Slot target : e.getValue()) {
					createSwapAction(subSubMenu, source, target, isLoad, includeContract, includePort);
				}
				subMenu.add(subSubMenu);
			}

		}

		manager.add(subMenu);
	}

	private void createDeleteSlotMenu(final IMenuManager newMenuManager, final Slot slot) {
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

	IMenuListener createDischargeSlotMenuListener(final List<DischargeSlot> dischargeSlots, final int index) {
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		return new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {

				final DischargeSlot dischargeSlot = dischargeSlots.get(index);

				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				createNewSlotMenu(newMenuManager, dischargeSlot);
				createMenus(manager, dischargeSlot, dischargeSlot.getCargo(), filterSlotsByCompatibility(dischargeSlot, cargoModel.getLoadSlots()), false);
				if (dischargeSlot.isFOBSale() == false) {
					createSpotMarketMenu(newMenuManager, SpotType.DES_PURCHASE, dischargeSlot);
				}
				createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot);
				createEditMenu(manager, dischargeSlot, dischargeSlot.getContract(), dischargeSlot.getCargo());
				createDeleteSlotMenu(manager, dischargeSlot);
				if (dischargeSlot.isFOBSale()) {
					createAssignmentMenus(manager, dischargeSlot);
				} else if (dischargeSlot.getCargo() != null) {

					boolean foundDESPurchase = false;
					for (final Slot s : dischargeSlot.getCargo().getSlots()) {
						if (s instanceof LoadSlot && ((LoadSlot) s).isDESPurchase()) {
							createAssignmentMenus(manager, s);
							foundDESPurchase = true;
						}
					}
					if (!foundDESPurchase) {
						createAssignmentMenus(manager, dischargeSlot.getCargo());
					}
				}
				final Contract contract = dischargeSlot.getContract();
				if (contract == null || contract.getContractType() == ContractType.BOTH) {
					createFOBDESSwitchMenu(manager, dischargeSlot);
				}
			}

		};
	}

	private void createEditMenu(final IMenuManager newMenuManager, final Slot slot, final Contract contract, final Cargo cargo) {
		newMenuManager.add(new Separator());
		newMenuManager.add(new EditAction("Edit Slot", slot));
		if (contract != null) {
			newMenuManager.add(new EditAction("Edit Contract", contract));
		}
		if (cargo != null) {
			if (cargo.getSlots().size() > 2) {
				if (LicenseFeatures.isPermitted("features:complex-cargo")) {
					newMenuManager.add(new EditLDDAction("Edit Complex Cargo", cargo));
				}
			}
			// newMenuManager.add(new EditAction("Edit Cargo", cargo));
		}
	}

	private void createAssignmentMenus(final IMenuManager menuManager, final Slot slot) {
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
					m.add(new AssignAction(p.getFirst(), (Vessel) p.getSecond()));
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
						reassignMenuManager.add(new AssignAction(p.getFirst(), (Vessel) p.getSecond()));
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

			final IReferenceValueProviderFactory valueProviderFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry()
					.getValueProviderFactory(CargoPackage.eINSTANCE.getAssignableElement(), CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType());
			final IReferenceValueProvider valueProvider = valueProviderFactory.createReferenceValueProvider(CargoPackage.eINSTANCE.getAssignableElement(),
					CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType(), scenarioModel);

			final List<Pair<String, EObject>> vesselAvailabilityOptions = new LinkedList<>();

			for (final Pair<String, EObject> p : valueProvider.getAllowedValues(cargo, CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType())) {
				final EObject assignmentOption = p.getSecond();
				if (assignmentOption == null) {
					continue;
				}

				if (assignmentOption instanceof CharterInMarket) {
					final CharterInMarket charterInMarket = (CharterInMarket) assignmentOption;

					nominalMenuUsed = true;
					nominalMenu.add(new RunnableAction(String.format("%s", charterInMarket.getName()),
							() -> helper.assignCargoToSpotCharterIn(String.format("Assign to %s", charterInMarket.getName()), cargo, charterInMarket, -1)));

					if (charterInMarket.isEnabled() && charterInMarket.getSpotCharterCount() > 0) {

						if (charterInMarket.getSpotCharterCount() == 1) {
							marketMenu.add(new RunnableAction(String.format("%s", charterInMarket.getName()),
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
				} else if (assignmentOption instanceof VesselAvailability) {
					final VesselAvailability vesselAvailability = (VesselAvailability) assignmentOption;
					if (vesselAvailability != cargo.getVesselAssignmentType()) {
						vesselAvailabilityOptions.add(p);

						// reassignMenuManager.add(new RunnableAction(p.getFirst(), () -> helper.assignCargoToVesselAvailability(String.format("Assign to %s", p.getFirst()), cargo,
						// vesselAvailability)));
					}
				} else {
					assert false;
				}
			}
			{
				if (vesselAvailabilityOptions.size() > 15) {
					int counter = 0;
					MenuManager m = new MenuManager("", null);
					String firstEntry = null;
					String lastEntry = null;
					for (final Pair<String, EObject> p : vesselAvailabilityOptions) {
						if (p.getSecond() == null) {
							continue;
						}

						m.add(new RunnableAction(p.getFirst(), () -> helper.assignCargoToVesselAvailability(String.format("Assign to %s", p.getFirst()), cargo, (VesselAvailability) p.getSecond())));
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
					for (final Pair<String, EObject> p : vesselAvailabilityOptions) {
						reassignMenuManager.add(
								new RunnableAction(p.getFirst(), () -> helper.assignCargoToVesselAvailability(String.format("Assign to %s", p.getFirst()), cargo, (VesselAvailability) p.getSecond())));
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
					if (cargo.isLocked()) {

						menuManager.add(new RunnableAction("Unlock", () -> helper.unlockCargoAssignment("Unlock assignment", cargo)));
					} else {
						menuManager.add(new RunnableAction("Lock", () -> helper.lockCargoAssignment("Lock assignment", cargo)));
					}
				}
			}

		}

	}

	public IMenuListener createMultipleSelectionMenuListener(final Set<Cargo> cargoes) {

		return new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				boolean anyLocked = false;
				boolean anyUnlocked = false;

				for (final Cargo cargo : cargoes) {
					if (cargo.getCargoType() == CargoType.FLEET) {
						if (cargo.isLocked()) {
							anyLocked = true;
						} else {
							anyUnlocked = true;
						}
					}
				}

				if (anyLocked) {
					manager.add(new RunnableAction("Unlock", () -> helper.unlockCargoesAssignment("Unlock assignments", cargoes)));
				}

				if (anyUnlocked) {
					manager.add(new RunnableAction("Lock", () -> helper.lockCargoesAssignment("Lock assignments", cargoes)));
				}
			}

		};
	}

	public IMenuListener createLoadSlotMenuListener(final List<LoadSlot> loadSlots, final int index) {
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		return new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				final LoadSlot loadSlot = loadSlots.get(index);
				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				createNewSlotMenu(newMenuManager, loadSlot);
				createMenus(manager, loadSlot, loadSlot.getCargo(), filterSlotsByCompatibility(loadSlot, cargoModel.getDischargeSlots()), true);
				createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot);
				if (loadSlot.isDESPurchase() == false) {
					createSpotMarketMenu(newMenuManager, SpotType.FOB_SALE, loadSlot);
				}
				createEditMenu(manager, loadSlot, loadSlot.getContract(), loadSlot.getCargo());
				createDeleteSlotMenu(manager, loadSlot);
				if (loadSlot.isDESPurchase()) {
					createAssignmentMenus(manager, loadSlot);
				} else if (loadSlot.getCargo() != null) {
					boolean foundFobSale = false;
					for (final Slot s : loadSlot.getCargo().getSlots()) {
						if (s instanceof DischargeSlot && ((DischargeSlot) s).isFOBSale()) {
							createAssignmentMenus(manager, s);
							foundFobSale = true;
						}
					}
					if (!foundFobSale) {
						createAssignmentMenus(manager, loadSlot.getCargo());

					}
				}
				final Contract contract = loadSlot.getContract();
				if (contract == null || contract.getContractType() == ContractType.BOTH) {
					createFOBDESSwitchMenu(manager, loadSlot);
				}
			}

		};
	}

	/**
	 */
	public IMenuListener createSwapSlotsMenuListener(final List<Slot> slots, final int index) {
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		return new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				final Slot slot = slots.get(index);
				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					if (!loadSlot.isDESPurchase()) {
						final List<Slot> filteredSlots = new LinkedList<Slot>();
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
						final List<Slot> filteredSlots = new LinkedList<Slot>();
						for (final DischargeSlot s : cargoModel.getDischargeSlots()) {
							if (!s.isFOBSale()) {
								filteredSlots.add(s);
							}
						}
						createSwapWithMenus(manager, dischargeSlot, filteredSlots, false);
					}
				}
			}
		};
	}

	private void createSwapWithMenus(final IMenuManager manager, final Slot source, final List<? extends Slot> possibleTargets, final boolean sourceIsLoad) {

		final Map<String, Set<Slot>> slotsByDate = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByContract = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByPort = new TreeMap<String, Set<Slot>>();

		for (final Slot target : possibleTargets) {

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
	private List<Slot> filterSlotsByCompatibility(final Slot source, final List<? extends Slot> possibleTargets) {

		final List<Slot> filteredSlots = new LinkedList<Slot>();

		for (final Slot slot : possibleTargets) {
			// Check restrictions on both slots
			if (areUnsortedSlotsCompatible(source, slot) == false) {
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
		final ZonedDateTime loadStart = load.getWindowStartWithSlotOrPortTimeWithFlex();
		final ZonedDateTime loadEnd = load.getWindowEndWithSlotOrPortTimeWithFlex();
		final ZonedDateTime dischargeStart = discharge.getWindowStartWithSlotOrPortTimeWithFlex();
		final ZonedDateTime dischargeEnd = discharge.getWindowEndWithSlotOrPortTimeWithFlex();

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
			if (load.isDivertible()) {
				final int restriction = load.getShippingDaysRestriction();
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
		if (areSlotWindowsCompatible(load, discharge) == false) {
			return false;
		}

		// DES purchase
		if (load.isDESPurchase() == true) {
			// FOB sale - incompatible
			if (discharge.isFOBSale() == true) {
				return false;
			}
			// DES sale - only at the same port or divertible
			else {
				return load.isDivertible() || (load.getPort() == discharge.getPort());
			}
		}
		// FOB purchase
		else {
			// FOB sale - only at the same port or divertible
			if (discharge.isFOBSale() == true) {
				return discharge.isDivertible() || (load.getPort() == discharge.getPort());
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
	private boolean areUnsortedSlotsCompatible(final Slot a, final Slot b) {
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
	private boolean checkSourceContractConstraints(final Slot source, final Slot target) {
		final EList<Port> restrictedPorts = source.getRestrictedPorts();
		final EList<Contract> restrictedContracts = source.getRestrictedContracts();
		final boolean areRestrictedListsPermissive = source.getSlotOrContractRestrictedListsArePermissive();

		if (restrictedContracts != null) {
			if (restrictedContracts.contains(target.getContract()) != areRestrictedListsPermissive) {
				return false;
			}
		}

		if (restrictedPorts != null) {
			if (restrictedPorts.contains(target.getPort()) != areRestrictedListsPermissive) {
				return false;
			}
		}

		return true;
	}

	private void createMenus(final IMenuManager manager, final Slot source, final Cargo sourceCargo, final List<? extends Slot> possibleTargets, final boolean sourceIsLoad) {

		final Map<String, Set<Slot>> unusedSlotsByDate = new TreeMap<String, Set<Slot>>();
		final Set<Slot> nearSlotsByDate = createSlotTreeSet();
		final Map<String, Set<Slot>> slotsByDate = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByContract = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByPort = new TreeMap<String, Set<Slot>>();

		for (final Slot target : possibleTargets) {

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
				final ZonedDateTime a = loadSlot.getWindowStartWithSlotOrPortTime();
				final ZonedDateTime b = dischargeSlot.getWindowStartWithSlotOrPortTime();
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

	void createNewSlotMenu(final IMenuManager menuManager, final Slot source) {

		final List<Port> transferPorts = new LinkedList<Port>();
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
				menuManager.add(new CreateSlotAction("DES Sale", source, null, false, null));
			} else {
				//
				menuManager.add(new CreateSlotAction("DES Sale", source, null, false, null));
				menuManager.add(new CreateSlotAction("FOB Sale", source, null, true, null));

				if (enableSTSMenus) {
					if (loadSlot.getTransferFrom() == null) {
						if (!transferPorts.isEmpty()) {
							final MenuManager subMenu = new MenuManager("Ship to Ship", null);

							for (final Port p : transferPorts) {
								subMenu.add(new CreateSlotAction(p.getName(), source, null, false, p));
							}
							menuManager.add(subMenu);
						}
					}
				}
			}
		} else {
			final DischargeSlot dischargeSlot = (DischargeSlot) source;
			menuManager.add(new CreateSlotAction("FOB Purchase", source, null, false, null));
			if (!dischargeSlot.isFOBSale()) {
				menuManager.add(new CreateSlotAction("DES Purchase", source, null, true, null));
			}
			if (enableSTSMenus) {

				if (dischargeSlot.getTransferTo() == null) {
					if (!transferPorts.isEmpty()) {
						final MenuManager subMenu = new MenuManager("Ship to Ship", null);

						for (final Port p : transferPorts) {
							subMenu.add(new CreateSlotAction(p.getName(), source, null, false, p));
						}
						menuManager.add(subMenu);
					}
				}
			}
		}
	}

	private TreeSet<Slot> createSlotTreeSet() {
		final TreeSet<Slot> slotsByDate = new TreeSet<Slot>(new Comparator<Slot>() {

			@Override
			public int compare(final Slot o1, final Slot o2) {
				if (o1.getWindowStart() == null) {
					return -1;
				} else if (o2.getWindowStart() == null) {
					return 1;
				}
				return o1.getWindowStart().compareTo(o2.getWindowStart());
			}
		});
		return slotsByDate;
	}

	void createSpotMarketMenu(final IMenuManager manager, final SpotType spotType, final Slot source) {
		final SpotMarketsModel pricingModel = scenarioModel.getReferenceModel().getSpotMarketsModel();
		final Collection<SpotMarket> validMarkets = new LinkedList<SpotMarket>();
		String menuName = "";
		boolean isSpecial = false;
		if (spotType == SpotType.DES_PURCHASE) {
			menuName = "DES Purchase";
			final SpotMarketGroup group = pricingModel.getDesPurchaseSpotMarket();
			for (final SpotMarket market : group.getMarkets()) {
				final Set<Port> ports = SetUtils.getObjects(((DESPurchaseMarket) market).getDestinationPorts());
				if (ports.contains(source.getPort())) {
					validMarkets.add(market);
				}
			}
			isSpecial = true;
		} else if (spotType == SpotType.DES_SALE) {
			menuName = "DES Sale";
			validMarkets.addAll(pricingModel.getDesSalesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_PURCHASE) {
			menuName = "FOB Purchase";
			validMarkets.addAll(pricingModel.getFobPurchasesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_SALE) {
			menuName = "FOB Sale";
			final SpotMarketGroup group = pricingModel.getFobSalesSpotMarket();
			for (final SpotMarket market : group.getMarkets()) {
				final Set<Port> originPorts = SetUtils.getObjects(((FOBSalesMarket) market).getOriginPorts());
				if (originPorts != null && originPorts.contains(source.getPort())) {
					validMarkets.add(market);
				}
			}
			isSpecial = true;
		}
		final MenuManager subMenu = new MenuManager("New " + menuName + " Market Slot", null);

		if (source.getWindowStart() != null) {
			for (final SpotMarket market : validMarkets) {
				subMenu.add(new CreateSlotAction("Create " + market.getName() + " slot", source, market, isSpecial, null));
			}
		}

		manager.add(subMenu);

	}

	private void createWireAction(final IMenuManager subMenu, final Slot source, final Slot target, final boolean sourceIsLoad, final boolean includeContract, final boolean includePort) {
		final String name = getActionName(target, !sourceIsLoad, includeContract, includePort);
		if (sourceIsLoad) {
			subMenu.add(new RunnableAction(name, () -> helper.pairSlotsIntoCargo("Rewire Cargoes", (LoadSlot) source, (DischargeSlot) target)));
		} else {
			subMenu.add(new RunnableAction(name, () -> helper.pairSlotsIntoCargo("Rewire Cargoes", (LoadSlot) source, (DischargeSlot) target)));
		}
	}

	private void createSwapAction(final MenuManager subMenu, final Slot source, final Slot target, final boolean isLoad, final boolean includeContract, final boolean includePort) {
		final String name = getActionName(target, isLoad, includeContract, includePort);
		subMenu.add(new SwapAction(name, source, target));
	}

	private String getActionName(final Slot slot, final boolean isLoad, final boolean includeContract, final boolean includePort) {
		final StringBuilder sb = new StringBuilder();

		sb.append(formatDate(slot.getWindowStart()));
		if (includePort && slot.getPort() != null) {
			sb.append(", " + slot.getPort().getName());
		}
		if (slot instanceof SpotSlot) {
			sb.append(", " + ((SpotSlot) slot).getMarket().getName());
		}
		final Cargo c = isLoad ? ((LoadSlot) slot).getCargo() : ((DischargeSlot) slot).getCargo();
		if (c != null) {
			sb.append(" -- ");
			sb.append("cargo '" + c.getLoadName() + "'");
		}
		return sb.toString();
	}

	private void addTargetByDateToSortedSet(final Slot target, final String group, final Map<String, Set<Slot>> targets) {
		Set<Slot> targetGroupSlots;
		if (targets.containsKey(group)) {
			targetGroupSlots = targets.get(group);
		} else {
			targetGroupSlots = createSlotTreeSet();
			targets.put(group, targetGroupSlots);
		}
		targetGroupSlots.add(target);
	}

	class CreateSlotAction extends Action {

		private final Slot source;
		private final SpotMarket market;
		private final boolean sourceIsLoad;
		private final boolean isDesPurchaseOrFobSale;
		private final Port shipToShipPort;

		private String getKeyForDate(final LocalDate date) {
			final String key = String.format("%04d-%02d", date.getYear(), date.getMonthValue());
			return key;
		}

		public CreateSlotAction(final String name, final Slot source, final SpotMarket market, final boolean isDesPurchaseOrFobSale, final Port shipToShipPort) {
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

			final List<Command> setCommands = new LinkedList<Command>();
			final List<Command> deleteCommands = new LinkedList<Command>();

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
						ZonedDateTime cal = source.getWindowStartWithSlotOrPortTime();
						// Take into account travel time
						if (loadSlot.isDESPurchase() && loadSlot.isDivertible()) {
							final int travelTime = getTravelTime(loadSlot.getPort(), dischargeSlot.getPort(), loadSlot.getNominatedVessel());
							cal = cal.plusHours(travelTime);
							cal = cal.plusHours(loadSlot.getSlotOrPortDuration());
						} else if (!loadSlot.isDESPurchase()) {

							AVesselSet<? extends Vessel> assignedVessel = null;
							if (loadSlot.getCargo() != null) {
								final VesselAssignmentType vesselAssignmentType = loadSlot.getCargo().getVesselAssignmentType();
								if (vesselAssignmentType instanceof VesselAvailability) {
									assignedVessel = ((VesselAvailability) vesselAssignmentType).getVessel();
								} else if (vesselAssignmentType instanceof CharterInMarket) {
									assignedVessel = ((CharterInMarket) vesselAssignmentType).getVesselClass();
								}
							}
							final int travelTime = getTravelTime(loadSlot.getPort(), dischargeSlot.getPort(), assignedVessel);
							cal = cal.plusHours(travelTime);
							cal = cal.plusHours(loadSlot.getSlotOrPortDuration());
						}

						// Get existing names
						final Set<String> usedIDStrings = new HashSet<>();
						for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
							usedIDStrings.add(slot.getName());
						}

						if (dischargeSlot.isFOBSale()) {
							dischargeSlot.setPort(source.getPort());
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

				} else {
					dischargeSlot = (DischargeSlot) source;
					if (market == null) {
						loadSlot = cec.createNewLoad(setCommands, cargoModel, isDesPurchaseOrFobSale);
						loadSlot.setWindowStart(source.getWindowStart());
					} else {
						loadSlot = cec.createNewSpotLoad(setCommands, cargoModel, isDesPurchaseOrFobSale, market);
						// Get start of month and create full sized window
						ZonedDateTime cal = source.getWindowStartWithSlotOrPortTime();
						// Take into account travel time
						if (!dischargeSlot.isFOBSale()) {

							AVesselSet<? extends Vessel> assignedVessel = null;
							if (loadSlot.getCargo() != null) {
								final VesselAssignmentType vesselAssignmentType = loadSlot.getCargo().getVesselAssignmentType();
								if (vesselAssignmentType instanceof VesselAvailability) {
									assignedVessel = ((VesselAvailability) vesselAssignmentType).getVessel();
								} else if (vesselAssignmentType instanceof CharterInMarket) {
									assignedVessel = ((CharterInMarket) vesselAssignmentType).getVesselClass();
								}
							}
							final int travelTime = getTravelTime(loadSlot.getPort(), dischargeSlot.getPort(), assignedVessel);
							cal = cal.minusHours(travelTime);
							cal = cal.minusHours(loadSlot.getSlotOrPortDuration());
						}


						if (loadSlot.isDESPurchase()) {
							loadSlot.setPort(source.getPort());
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
				cec.runWiringUpdate(setCommands, deleteCommands, loadSlot, dischargeSlot);
			}

			// make a compound command which adds and sets values before deleting anything

			final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");
			// Process set before delete
			for (final Command c : setCommands) {
				currentWiringCommand.append(c);
			}
			for (final Command c : deleteCommands) {
				currentWiringCommand.append(c);
			}

			scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand);
			{
				cec.verifyCargoModel(((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getCargoModel());
			}

		}
	}

	class SwapAction extends Action {

		final private Slot source;
		final private Slot target;

		public SwapAction(final String text, final Slot source, final Slot target) {
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
					final EList<Slot> sortedSlots = c.getSortedSlots();
					final Iterator<Slot> iterator = sortedSlots.iterator();
					while (iterator.hasNext()) {
						final Slot slot = iterator.next();
						// This is the slot we are replacing!
						if (slot == source) {
							continue;
						}

						final ZonedDateTime slotDate = slot.getWindowStartWithSlotOrPortTime();
						if (slotDate == null || target.getWindowEndWithSlotOrPortTime().isBefore(slotDate)) {
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

	private void createFOBDESSwitchMenu(final IMenuManager manager, final Slot slot) {

		final Contract contract = slot.getContract();
		assert (contract == null || contract.getContractType() == ContractType.BOTH);

		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (SlotClassifier.classify(loadSlot) == SlotType.FOB_Buy) {
				manager.add(new RunnableAction("Convert to DES Purchase", () -> helper.convertToDESPurchase("Convert to DES Purchase", loadSlot)));
			} else if (SlotClassifier.classify(loadSlot) == SlotType.DES_Buy_AnyDisPort) {
				manager.add(new RunnableAction("Convert to FOB Purchase", () -> helper.convertToDESPurchase("Convert to FOB Purchase", loadSlot)));
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

	/**
	 */
	public void editLDDCargo(final Cargo cargo, final boolean isNew) {
		final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
		try {
			editorLock.claim();
			scenarioEditingLocation.setDisableUpdates(true);

			final ComplexCargoEditor editor = new ComplexCargoEditor(shell, scenarioEditingLocation, false);
			// editor.setBlockOnOpen(true);

			final int ret = editor.open(cargo);
			final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
			if (ret == Window.OK) {
				final CargoModel cargomodel = scenarioModel.getCargoModel();

				final CompoundCommand cmd = new CompoundCommand("Edit LDD Cargo");
				if (cargo.eContainer() == null) {
					cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargomodel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), Collections.singleton(cargo)));
				}
				for (final Slot s : cargo.getSlots()) {

					if (s.eContainer() == null) {

						if (s instanceof LoadSlot) {
							cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargomodel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), Collections.singleton(s)));
						} else {
							cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargomodel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), Collections.singleton(s)));
						}
					}
				}

				commandStack.execute(cmd);

			} else {
				final Iterator<Command> itr = new LinkedList<Command>(editor.getExecutedCommands()).descendingIterator();
				while (itr.hasNext()) {
					final Command cmd = itr.next();
					if (commandStack.getUndoCommand() == cmd) {
						commandStack.undo();
					} else {
						throw new IllegalStateException("Unable to cancel edit - command stack history is corrupt");
					}
				}
			}
		} finally {
			scenarioEditingLocation.setDisableUpdates(false);
			editorLock.release();
		}
	}

	private int getTravelTime(final Port from, final Port to, final AVesselSet<? extends Vessel> assignedVessel) {

		double maxSpeed = 19.0;

		if (assignedVessel instanceof Vessel) {
			final Vessel vessel = (Vessel) assignedVessel;
			final VesselClass vesselClass = vessel.getVesselClass();
			if (vesselClass != null) {
				maxSpeed = vesselClass.getMaxSpeed();
			}
		}

		int distance = 0;
		LOOP_ROUTES: for (final Route route : scenarioModel.getReferenceModel().getPortModel().getRoutes()) {
			if (route.isCanal() == false) {
				for (final RouteLine dl : route.getLines()) {
					if (dl.getFrom().equals(from) && dl.getTo().equals(to)) {
						distance = dl.getDistance();
						break LOOP_ROUTES;
					}
				}

			}
		}

		final int travelTime = (int) Math.round((double) distance / maxSpeed);

		return travelTime;
	}

	private String formatDate(final LocalDate localDate) {
		if (localDate == null) {
			return "<no date>";
		}
		return String.format("%02d %s %04d", localDate.getDayOfMonth(), localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()), localDate.getYear());
	}

}