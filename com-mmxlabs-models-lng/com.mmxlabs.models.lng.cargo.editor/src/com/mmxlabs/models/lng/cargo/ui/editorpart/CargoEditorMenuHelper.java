/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.shiro.SecurityUtils;
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

import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.editor.editors.ldd.ComplexCargoEditor;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.dates.LocalDateUtil;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * @since 3.0
 */
public class CargoEditorMenuHelper {

	private final Shell shell;

	private final IScenarioEditingLocation scenarioEditingLocation;

	private final LNGPortfolioModel portfolioModel;

	private final CargoEditingCommands cec;

	private final LNGScenarioModel scenarioModel;

	private static final boolean enableSTSMenus = SecurityUtils.getSubject().isPermitted("features:shiptoship");

	/**
	 * @since 4.0
	 */
	public CargoEditorMenuHelper(final Shell shell, final IScenarioEditingLocation scenarioEditingLocation, final LNGScenarioModel scenarioModel, final LNGPortfolioModel portfolioModel) {
		this.shell = shell;
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.scenarioModel = scenarioModel;
		this.portfolioModel = portfolioModel;
		cec = new CargoEditingCommands(scenarioEditingLocation.getEditingDomain(), scenarioModel, portfolioModel);
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
			ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
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
				editLDDCargo((Cargo) target);
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
			}
		};
		newMenuManager.add(new Separator());
		newMenuManager.add(deleteAction);

	}

	IMenuListener createDischargeSlotMenuListener(final List<DischargeSlot> dischargeSlots, final int index) {
		final CargoModel cargoModel = portfolioModel.getCargoModel();
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {

				final DischargeSlot dischargeSlot = dischargeSlots.get(index);

				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				if (dischargeSlot.isFOBSale()) {
					createNewSlotMenu(newMenuManager, dischargeSlot);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot);
				} else {
					createNewSlotMenu(newMenuManager, dischargeSlot);
					createMenus(manager, dischargeSlot, dischargeSlot.getCargo(), filterSlotsByCompatibility(dischargeSlot, cargoModel.getLoadSlots()), false);
					createSpotMarketMenu(newMenuManager, SpotType.DES_PURCHASE, dischargeSlot);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot);
				}
				createEditMenu(manager, dischargeSlot, dischargeSlot.getContract(), dischargeSlot.getCargo());
				createDeleteSlotMenu(manager, dischargeSlot);
				if (dischargeSlot.getCargo() != null) {
					final ElementAssignment elementAssignment = AssignmentEditorHelper.getElementAssignment(portfolioModel.getAssignmentModel(), dischargeSlot.getCargo());
					createAssignmentMenus(manager, dischargeSlot.getCargo(), elementAssignment);
				}
			}

		};
		return l;

	}

	private void createEditMenu(final IMenuManager newMenuManager, final Slot slot, final Contract contract, final Cargo cargo) {
		newMenuManager.add(new Separator());
		newMenuManager.add(new EditAction("Edit Slot", slot));
		if (contract != null) {
			newMenuManager.add(new EditAction("Edit Contract", contract));
		}
		if (cargo != null) {
			if (cargo.getSlots().size() > 2) {
				newMenuManager.add(new EditLDDAction("Edit Complex Cargo", cargo));
			}
			// newMenuManager.add(new EditAction("Edit Cargo", cargo));
		}
	}

	private void createAssignmentMenus(final IMenuManager menuManager, final Cargo cargo, final ElementAssignment elementAssignment) {
		menuManager.add(new Separator());

		{
			final MenuManager reassignMenuManager = new MenuManager("Assign to...", null);
			menuManager.add(reassignMenuManager);

			final ScenarioFleetModel fleetModel = scenarioModel.getPortfolioModel().getScenarioFleetModel();
			class AssignAction extends Action {
				private final Vessel vessel;

				public AssignAction(final Vessel vessel) {
					super(vessel.getName());
					this.vessel = vessel;
				}

				public void run() {
					final Command cmd = AssignmentEditorHelper.reassignElement(scenarioEditingLocation.getEditingDomain(), vessel, elementAssignment);
					scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
				}
			}
			if (elementAssignment != null) {
				for (final VesselAvailability vesselAvailability : fleetModel.getVesselAvailabilities()) {
					final Vessel vessel = vesselAvailability.getVessel();
					if (vessel != elementAssignment.getAssignment()) {
						reassignMenuManager.add(new AssignAction(vessel));
					}
				}
			}
		}

		if (elementAssignment != null && elementAssignment.getAssignment() != null) {
			if (elementAssignment.isLocked()) {
				final Action action = new Action("Unlock") {
					@Override
					public void run() {
						final Command cmd = AssignmentEditorHelper.unlockElement(scenarioEditingLocation.getEditingDomain(), elementAssignment);
						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				menuManager.add(action);
			} else {
				final Action action = new Action("Lock") {
					@Override
					public void run() {
						final Command cmd = AssignmentEditorHelper.lockElement(scenarioEditingLocation.getEditingDomain(), elementAssignment);
						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				menuManager.add(action);
			}
			{
				final Action action = new Action("Unassign") {
					@Override
					public void run() {
						final Command cmd = AssignmentEditorHelper.unassignElement(scenarioEditingLocation.getEditingDomain(), elementAssignment);
						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				menuManager.add(action);
			}

		}

	}

	public IMenuListener createLoadSlotMenuListener(final List<LoadSlot> loadSlots, final int index) {
		final CargoModel cargoModel = portfolioModel.getCargoModel();
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				final LoadSlot loadSlot = loadSlots.get(index);
				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				if (loadSlot.isDESPurchase()) {
					createNewSlotMenu(newMenuManager, loadSlot);
					createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot);
				} else {
					createNewSlotMenu(newMenuManager, loadSlot);
					createMenus(manager, loadSlot, loadSlot.getCargo(), filterSlotsByCompatibility(loadSlot, cargoModel.getDischargeSlots()), true);
					createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_SALE, loadSlot);
				}
				createEditMenu(manager, loadSlot, loadSlot.getContract(), loadSlot.getCargo());
				createDeleteSlotMenu(manager, loadSlot);
				if (loadSlot.getCargo() != null) {
					final ElementAssignment elementAssignment = AssignmentEditorHelper.getElementAssignment(portfolioModel.getAssignmentModel(), loadSlot.getCargo());
					createAssignmentMenus(manager, loadSlot.getCargo(), elementAssignment);
				}
			}
		};
		return l;

	}

	/**
	 * @since 4.0
	 */
	public IMenuListener createSwapSlotsMenuListener(final List<Slot> slots, final int index) {
		final CargoModel cargoModel = portfolioModel.getCargoModel();
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				final Slot slot = slots.get(index);
				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					if (loadSlot.isDESPurchase()) {
						// createNewSlotMenu(newMenuManager, loadSlot, true);
						// createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot, true);
					} else {
						// createNewSlotMenu(newMenuManager, loadSlot, true);
						final List<Slot> filteredSlots = new LinkedList<Slot>();
						for (final LoadSlot s : cargoModel.getLoadSlots()) {
							if (!s.isDESPurchase()) {
								filteredSlots.add(s);
							}
						}

						createSwapWithMenus(manager, loadSlot, filteredSlots, true);
						// createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot, true);
						// createSpotMarketMenu(newMenuManager, SpotType.FOB_SALE, loadSlot, true);
					}

				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					if (dischargeSlot.isFOBSale()) {
						// createNewSlotMenu(newMenuManager, dischargeSlot, false);
						// createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, false);
					} else {
						final List<Slot> filteredSlots = new LinkedList<Slot>();
						for (final DischargeSlot s : cargoModel.getDischargeSlots()) {
							if (!s.isFOBSale()) {
								filteredSlots.add(s);
							}
						}
						// createNewSlotMenu(newMenuManager, dischargeSlot, false);
						createSwapWithMenus(manager, dischargeSlot, filteredSlots, false);
						// createSpotMarketMenu(newMenuManager, SpotType.DES_PURCHASE, dischargeSlot, false);
						// createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, false);
					}
				}

				// createEditMenu(manager, slot, slot.getCargo());
				// createEditContractMenu(manager, slot, slot.getContract());
				// createDeleteSlotMenu(manager, slot);
			}
		};
		return l;

	}

	private void createSwapWithMenus(final IMenuManager manager, final Slot source, final List<? extends Slot> possibleTargets, final boolean sourceIsLoad) {

		final Map<String, Set<Slot>> slotsByDate = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByContract = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByPort = new TreeMap<String, Set<Slot>>();

		for (final Slot target : possibleTargets) {

			final int daysDifference;
			// Perform some filtering on the possible targets
			{
				// final Slot otherSlot;
				// final DischargeSlot dischargeSlot;
				// if (sourceIsLoad) {
				// loadSlot = (LoadSlot) source;
				// dischargeSlot = (DischargeSlot) target;
				// } else {
				// loadSlot = (LoadSlot) target;
				// dischargeSlot = (DischargeSlot) source;
				// }
				// Filter out current pairing
				if (source.getCargo().getSlots().contains(target)) {
					continue;
				}

				// // Filter backwards pairings
				// if (loadSlot.getWindowStart() == null) {
				// continue;
				// }
				// if (dischargeSlot.getWindowStart() == null) {
				// continue;
				// }
				// if (loadSlot.getWindowStart().after(dischargeSlot.getWindowStart())) {
				// continue;
				// }
				// final long diff = dischargeSlot.getWindowStart().getTime() - loadSlot.getWindowStart().getTime();
				// daysDifference = (int) (diff / 1000 / 60 / 60 / 24);
			}

			final Contract contract = target.getContract();
			if (contract != null) {
				addTargetByDateToSortedSet(target, contract.getName(), slotsByContract);
			}
			final Port port = target.getPort();
			if (port != null) {
				addTargetByDateToSortedSet(target, port.getName(), slotsByPort);
			}

			// if (daysDifference < 5) {
			// addSlotToTargets(target, "Less than 5 Days", slotsByDate);
			// }
			// if (daysDifference < 10) {
			// addSlotToTargets(target, "Less than 10 Days", slotsByDate);
			// }
			// if (daysDifference < 20) {
			// addSlotToTargets(target, "Less than 20 Days", slotsByDate);
			// }
			// if (daysDifference < 30) {
			// addSlotToTargets(target, "Less than 30 Days", slotsByDate);
			// }
			// if (daysDifference < 60) {
			// addSlotToTargets(target, "Less than 60 Days", slotsByDate);
			// }
			addTargetByDateToSortedSet(target, "Any", slotsByDate);

		}
		{
			buildSwapMenu(manager, "Swap Slots By Contract", source, slotsByContract, sourceIsLoad, false, true);
			// buildSubMenu(manager, "Slots By Date", source, sourceIsLoad, slotsByDate, true, true);
			buildSwapMenu(manager, "Swap Slots By Port", source, slotsByPort, sourceIsLoad, true, false);
		}
	}

	/**
	 * 
	 * @param source
	 * @param possibleTargets
	 * @return
	 */
	private List<Slot> filterSlotsByCompatibility(final Slot source, final List<? extends Slot> possibleTargets) {

		final List<Slot> filteredSlots = new LinkedList<Slot>();
		for (final Slot slot : possibleTargets) {
			// Check restrictions on both slots
			if (slot instanceof LoadSlot) {
				if (((LoadSlot) slot).isDESPurchase()) {
					if (!(slot.getPort() == source.getPort())) {
						continue;
					}
				}
			}
			if (slot instanceof DischargeSlot) {
				if (((DischargeSlot) slot).isFOBSale()) {
					continue;
				}
			}
			if (!checkSourceContractConstraints(source, slot) || !checkSourceContractConstraints(slot, source)) {
				continue;
			}
			filteredSlots.add(slot);
		}
		return filteredSlots;
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
				// Filter null windows and backwards pairings
				if (loadSlot.getWindowStart() == null) {
					continue;
				}
				if (dischargeSlot.getWindowStart() == null) {
					continue;
				}
				if (loadSlot.getWindowStart().after(dischargeSlot.getWindowStart())) {
					continue;
				}
				final long diff = dischargeSlot.getWindowStart().getTime() - loadSlot.getWindowStart().getTime();
				daysDifference = (int) (diff / 1000 / 60 / 60 / 24);
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

			// if (daysDifference < 5) {
			// addTargetByDateToSortedSet(target, "Less than 5 Days", slotsByDate);
			// // addTargetByDateToSortedSet(target, "near", nearSlotsByDate);
			// nearSlotsByDate.add(target);
			// }
			// if (daysDifference < 10) {
			// addTargetByDateToSortedSet(target, "Less than 10 Days", slotsByDate);
			// nearSlotsByDate.add(target);
			// }
			// if (daysDifference < 20) {
			// addTargetByDateToSortedSet(target, "Less than 20 Days", slotsByDate);
			// nearSlotsByDate.add(target);
			// }
			if (daysDifference <= 60) {
				// addTargetByDateToSortedSet(target, "Less than 30 Days", slotsByDate);
				nearSlotsByDate.add(target);
			}
			if (daysDifference > 60 && daysDifference <= 90) {
				addTargetByDateToSortedSet(target, "[>60 Days]", slotsByDate);
			}
			// if (daysDifference < 60) {
			// addTargetByDateToSortedSet(target, "Less than 60 Days", slotsByDate);
			// }
			// addTargetByDateToSortedSet(target, "Any", slotsByDate);
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
		for (final Port p : scenarioModel.getPortModel().getPorts()) {
			if (p.getCapabilities().contains(PortCapability.TRANSFER)) {
				transferPorts.add(p);
			}
		}// Sort by name
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
				menuManager.add(new CreateSlotAction("Discharge", source, null, false, null));
			} else {
				//
				menuManager.add(new CreateSlotAction("Discharge", source, null, false, null));
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
			menuManager.add(new CreateSlotAction("Load", source, null, false, null));
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
		final SpotMarketsModel pricingModel = scenarioModel.getSpotMarketsModel();
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

		for (final SpotMarket market : validMarkets) {
			subMenu.add(new CreateSlotAction("Create " + market.getName() + " slot", source, market, isSpecial, null));
		}

		manager.add(subMenu);

	}

	private void createWireAction(final IMenuManager subMenu, final Slot source, final Slot target, final boolean sourceIsLoad, final boolean includeContract, final boolean includePort) {
		final String name = getActionName(target, !sourceIsLoad, includeContract, includePort);
		if (sourceIsLoad) {
			subMenu.add(new WireAction(name, (LoadSlot) source, (DischargeSlot) target));
		} else {
			subMenu.add(new WireAction(name, (LoadSlot) target, (DischargeSlot) source));
		}
	}

	private void createSwapAction(final MenuManager subMenu, final Slot source, final Slot target, final boolean isLoad, final boolean includeContract, final boolean includePort) {
		final String name = getActionName(target, isLoad, includeContract, includePort);
		subMenu.add(new SwapAction(name, source, target));
	}

	private String getActionName(final Slot slot, final boolean isLoad, final boolean includeContract, final boolean includePort) {
		final StringBuilder sb = new StringBuilder();

		{
			final SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
			df.applyPattern("dd MMM yy");
			if (slot.getPort() != null) {
				final TimeZone zone = LocalDateUtil.getTimeZone(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
				df.setTimeZone(zone);
			}
			sb.append(df.format(slot.getWindowStart()));
		}
		{
			// sb.append(" '"+ slot.getName()+ "'");
		}
		// if (slot instanceof LoadSlot) {
		// if (((LoadSlot) slot).isDESPurchase()) {
		// sb.append(", DES");
		// }
		// c = ((LoadSlot) slot).getCargo();
		// }
		// if (slot instanceof DischargeSlot) {
		// if (((DischargeSlot) slot).isFOBSale()) {
		// sb.append(", FOB ");
		// }
		// c = ((DischargeSlot) slot).getCargo();
		// }
		if (includePort && slot.getPort() != null) {
			sb.append(", " + slot.getPort().getName());
		}
		if (slot instanceof SpotSlot) {
			sb.append(", " + ((SpotSlot) slot).getMarket().getName());
		}
		// if (includeContract && slot.getContract() != null) {
		// sb.append(slot.getContract().getName());
		// sb.append(", ");
		// }
		// sb.append(" | ");
		final Cargo c = isLoad ? ((LoadSlot) slot).getCargo() : ((DischargeSlot) slot).getCargo();
		if (c != null) {
			sb.append(" -- ");
			sb.append("cargo '" + c.getName() + "'");
		} else {
			// sb.append(isLoad ? "Long" : "Short");
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
			final CargoModel cargoModel = portfolioModel.getCargoModel();

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
					} else {
						dischargeSlot = cec.createNewSpotDischarge(setCommands, cargoModel, isDesPurchaseOrFobSale, market);
					}
					dischargeSlot.setWindowStart(source.getWindowStart());
					if (loadSlot.isDESPurchase()) {
						dischargeSlot.setPort(source.getPort());
					}
				} else {
					dischargeSlot = (DischargeSlot) source;
					if (market == null) {
						loadSlot = cec.createNewLoad(setCommands, cargoModel, isDesPurchaseOrFobSale);
						loadSlot.setWindowStart(source.getWindowStart());
					} else {
						loadSlot = cec.createNewSpotLoad(setCommands, cargoModel, isDesPurchaseOrFobSale, market);
						loadSlot.setWindowStart(source.getWindowStart());
					}
					if (dischargeSlot.isFOBSale()) {
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

		}
	}

	class WireAction extends Action {

		final private LoadSlot loadSlot;
		final private DischargeSlot dischargeSlot;

		public WireAction(final String text, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
			super(text);
			this.loadSlot = loadSlot;
			this.dischargeSlot = dischargeSlot;
		}

		@Override
		public void run() {

			final List<Command> setCommands = new LinkedList<Command>();
			final List<Command> deleteCommands = new LinkedList<Command>();

			cec.runWiringUpdate(setCommands, deleteCommands, loadSlot, dischargeSlot);

			final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");
			// Process set before delete
			for (final Command c : setCommands) {
				currentWiringCommand.append(c);
			}
			for (final Command c : deleteCommands) {
				currentWiringCommand.append(c);
			}

			scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand);
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

						// if (slot instanceof DischargeSlot) {
						// continue;
						// }
						final Date slotDate = slot.getWindowStartWithSlotOrPortTime();
						if (slotDate == null || target.getWindowEndWithSlotOrPortTime().before(slotDate)) {
							currentWiringCommand.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, MMXCorePackage.eINSTANCE.getNamedObject_Name(), target.getName()));
						} else {
							break;
						}
					}
				}
			}

			scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand);
		}
	}

	/**
	 * @since 4.0
	 */
	public void editLDDCargo(final Cargo cargo) {
		final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
		try {
			editorLock.claim();
			scenarioEditingLocation.setDisableUpdates(true);

			final ComplexCargoEditor editor = new ComplexCargoEditor(shell, scenarioEditingLocation);
			// editor.setBlockOnOpen(true);

			final int ret = editor.open(cargo);
			final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
			if (ret == Window.OK) {
				final CargoModel cargomodel = portfolioModel.getCargoModel();

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
}