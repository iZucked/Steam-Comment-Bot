/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
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

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
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
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.dates.LocalDateUtil;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 */
public class CargoEditorMenuHelper {

	private final Shell shell;

	private final IScenarioEditingLocation scenarioEditingLocation;

	private final LNGPortfolioModel portfolioModel;

	private final CargoEditingCommands cec;

	private final LNGScenarioModel scenarioModel;

	private static final boolean enableSTSMenus = SecurityUtils.getSubject().isPermitted("features:shiptoship");

	/**
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
					createAssignmentMenus(manager, dischargeSlot.getCargo());
				}
				final Contract contract = dischargeSlot.getContract();
				if (contract != null && contract.getContractType() == ContractType.BOTH) {
					createFOBDESSwitchMenu(manager, dischargeSlot);
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

	private void createAssignmentMenus(final IMenuManager menuManager, final AssignableElement assignableElement) {
		menuManager.add(new Separator());

		if (assignableElement != null) {
			final MenuManager reassignMenuManager = new MenuManager("Assign to...", null);
			menuManager.add(reassignMenuManager);

			class AssignAction extends Action {
				private final AVesselSet<Vessel> vessel;

				public AssignAction(final String label, final AVesselSet<Vessel> vessel) {
					super(label);
					this.vessel = vessel;
				}

				public void run() {

					final Object value = vessel == null ? SetCommand.UNSET_VALUE : vessel;
					final Command cmd = SetCommand.create(scenarioEditingLocation.getEditingDomain(), assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, value);
					scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
				}
			}

			final IReferenceValueProviderFactory valueProviderFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry()
					.getValueProviderFactory(CargoPackage.eINSTANCE.getAssignableElement(), CargoPackage.eINSTANCE.getAssignableElement_Assignment());
			final IReferenceValueProvider valueProvider = valueProviderFactory.createReferenceValueProvider(CargoPackage.eINSTANCE.getAssignableElement(),
					CargoPackage.eINSTANCE.getAssignableElement_Assignment(), scenarioModel);

			for (final Pair<String, EObject> p : valueProvider.getAllowedValues(assignableElement, CargoPackage.eINSTANCE.getAssignableElement_Assignment())) {
				if (p.getSecond() != assignableElement.getAssignment()) {
					reassignMenuManager.add(new AssignAction(p.getFirst(), (AVesselSet<Vessel>) p.getSecond()));
				}
			}
			if (assignableElement instanceof Cargo || assignableElement instanceof VesselEvent) {
				if (assignableElement.getAssignment() != null) {
					if (assignableElement.isLocked()) {
						final Action action = new Action("Unlock") {
							@Override
							public void run() {
								final CompoundCommand cc = new CompoundCommand("Unlock assignment");
								cc.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.FALSE));
								if (assignableElement instanceof Cargo) {
									cc.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), assignableElement, CargoPackage.Literals.CARGO__ALLOW_REWIRING, Boolean.TRUE));
								}
								scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cc);
							}
						};
						menuManager.add(action);
					} else {
						final Action action = new Action("Lock") {
							@Override
							public void run() {
								final CompoundCommand cc = new CompoundCommand("Lock assignment");
								cc.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.TRUE));
								if (assignableElement instanceof Cargo) {
									cc.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), assignableElement, CargoPackage.Literals.CARGO__ALLOW_REWIRING, Boolean.FALSE));
								}
								scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cc);
							}
						};
						menuManager.add(action);
					}
				}
			}
			{
				final Action action = new Action("Unassign") {
					@Override
					public void run() {
						final CompoundCommand cc = new CompoundCommand("Unassign");
						cc.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, SetCommand.UNSET_VALUE));
						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cc);
					}
				};
				menuManager.add(action);
			}

		}

	}

	public IMenuListener createMultipleSelectionMenuListener(final Set<Cargo> cargoes) {
		
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				boolean anyLocked = false;
				boolean anyUnlocked = false;
				
				for (Cargo cargo: cargoes) {
					if (cargo.getCargoType() == CargoType.FLEET) {
						if (cargo.isLocked()) {
							anyLocked = true;
						}
						else {
							anyUnlocked = true;
						}
					}
				}
				
				if (anyLocked) {
					final Action action = new Action("Unlock") {
						@Override
						public void run() {
							final CompoundCommand cc = new CompoundCommand("Unlock assignments");
							for (Cargo cargo: cargoes) {
								cc.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.FALSE));
								cc.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.CARGO__ALLOW_REWIRING, Boolean.TRUE));
							}
							scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cc);
						}
					};
					manager.add(action);					
				}
				
				if (anyUnlocked) {
					final Action action = new Action("Lock") {
						@Override
						public void run() {
							final CompoundCommand cc = new CompoundCommand("Lock assignments");
							for (Cargo cargo: cargoes) {
								cc.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.TRUE));
								cc.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.CARGO__ALLOW_REWIRING, Boolean.FALSE));
							}
							scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cc);
						}
					};
					manager.add(action);					
				}
			}

		};
		return l;
	}	
	
	public IMenuListener createLoadSlotMenuListener(final List<LoadSlot> loadSlots, final int index) {
		final CargoModel cargoModel = portfolioModel.getCargoModel();
		final IMenuListener l = new IMenuListener() {

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
					createAssignmentMenus(manager, loadSlot.getCargo());
				}
				final Contract contract = loadSlot.getContract();
				if (contract != null && contract.getContractType() == ContractType.BOTH) {
					createFOBDESSwitchMenu(manager, loadSlot);
				}
			}

		};
		return l;

	}

	/**
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
	 * Returns a list of slots among the specified possible targets which are compatible with the specified source slot.
	 * FOB purchases may be paired with DES sales or with FOB sales (which must be at the same port unless the sale
	 * is divertible), while DES purchases may be paired only with DES sales (which must be at the same port unless the 
	 * purchase is divertible). 
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
		Date loadStart = load.getWindowStartWithSlotOrPortTime();
		Date loadEnd = load.getWindowEndWithSlotOrPortTime();
		Date dischargeStart = discharge.getWindowStartWithSlotOrPortTime();
		Date dischargeEnd = discharge.getWindowEndWithSlotOrPortTime();

		// slots with unknown time windows are incompatible
		if (loadStart == null || dischargeStart == null || loadEnd == null || dischargeEnd == null) {
			return false;
		}

		// can never load before discharging 
		if (loadStart.after(dischargeEnd)) {
			return false;
		}
		
		boolean overlap = (dischargeStart.before(loadEnd));
		
		final long diff = dischargeEnd.getTime() - loadStart.getTime();
		int daysDifference = (int) (diff / 1000 / 60 / 60 / 24);
		
		// DES load
		if (load.isDESPurchase()) {
			// divertible DES - discharge time should be within shipping restriction window for load slot
			if (load.isDivertible()) {
				int restriction = load.getShippingDaysRestriction();
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
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean areUnsortedSlotsCompatible(final Slot a, final Slot b) {
		// need load / discharge or discharge / load
		if (a instanceof LoadSlot && b instanceof DischargeSlot) {
			return areSlotsCompatible((LoadSlot) a, (DischargeSlot) b);
		}
		else if (a instanceof DischargeSlot && b instanceof LoadSlot) {
			return areSlotsCompatible((LoadSlot) b, (DischargeSlot) a);
		}
		else {
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
				// Filter null windows and backwards pairings
				// no - this should be done in filterSlots...
				/*
				if (loadSlot.getWindowStart() == null) {
					continue;
				}
				if (dischargeSlot.getWindowStart() == null) {
					continue;
				}
				if (loadSlot.getWindowStart().after(dischargeSlot.getWindowStart())) {
					continue;
				}
				*/
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

		private String getKeyForDate(final Date date) {
			final String key = new SimpleDateFormat("yyyy-MM").format(date);
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
						dischargeSlot.setWindowStart(source.getWindowStart());
					} else {
						dischargeSlot = cec.createNewSpotDischarge(setCommands, cargoModel, isDesPurchaseOrFobSale, market);

						// Get start of month and create full sized window
						final Calendar cal = Calendar.getInstance();
						cal.setTimeZone(TimeZone.getTimeZone(source.getPort().getTimeZone()));
						cal.setTime(source.getWindowStartWithSlotOrPortTime());

						// Take into account travel time
						if (loadSlot.isDESPurchase() && loadSlot.isDivertible()) {
							final int travelTime = getTravelTime(loadSlot.getPort(), dischargeSlot.getPort(), loadSlot.getAssignment());
							cal.add(Calendar.HOUR_OF_DAY, travelTime);
							cal.add(Calendar.HOUR_OF_DAY, loadSlot.getSlotOrPortDuration());
						} else if (!loadSlot.isDESPurchase()) {

							AVesselSet<? extends Vessel> assignedVessel = null;
							if (loadSlot.getCargo() != null) {
								assignedVessel = loadSlot.getCargo().getAssignment();
							}
							final int travelTime = getTravelTime(loadSlot.getPort(), dischargeSlot.getPort(), assignedVessel);
							cal.add(Calendar.HOUR_OF_DAY, travelTime);
							cal.add(Calendar.HOUR_OF_DAY, loadSlot.getSlotOrPortDuration());
						}

						cal.set(Calendar.DAY_OF_MONTH, 1);
						cal.set(Calendar.DAY_OF_MONTH, 1);
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);
						final Date startDate = cal.getTime();
						final String yearMonthString = getKeyForDate(cal.getTime());
						dischargeSlot.setWindowStart(startDate);
						dischargeSlot.setWindowStartTime(0);
						cal.add(Calendar.MONTH, 1);
						final Date endDate = cal.getTime();
						dischargeSlot.setWindowSize((int) ((endDate.getTime() - startDate.getTime()) / 1000l / 60l / 60l));

						// Get existing names
						final Set<String> usedIDStrings = new HashSet<>();
						for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
							usedIDStrings.add(slot.getName());
						}

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
						final Calendar cal = Calendar.getInstance();
						cal.setTimeZone(TimeZone.getTimeZone(source.getPort().getTimeZone()));
						cal.setTime(source.getWindowStartWithSlotOrPortTime());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						cal.set(Calendar.DAY_OF_MONTH, 1);
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);
						final Date startDate = cal.getTime();

						final String yearMonthString = getKeyForDate(cal.getTime());

						loadSlot.setWindowStart(startDate);
						loadSlot.setWindowStartTime(0);
						cal.add(Calendar.MONTH, 1);
						final Date endDate = cal.getTime();
						loadSlot.setWindowSize((int) ((endDate.getTime() - startDate.getTime()) / 1000l / 60l / 60l));

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

	private void createFOBDESSwitchMenu(final IMenuManager manager, final Slot slot) {

		final Contract contract = slot.getContract();
		assert (contract != null && contract.getContractType() == ContractType.BOTH);

		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (SlotClassifier.classify(loadSlot) == SlotType.FOB_Buy) {
				manager.add(new FOBDESSwitchAction("Convert to DES Purchase", slot));
			} else if (SlotClassifier.classify(loadSlot) == SlotType.DES_Buy_AnyDisPort) {
				manager.add(new FOBDESSwitchAction("Convert to FOB Purchase", slot));
			}
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			if (SlotClassifier.classify(dischargeSlot) == SlotType.FOB_Sale_AnyLoadPort) {
				manager.add(new FOBDESSwitchAction("Convert to DES Sale", slot));
			} else if (SlotClassifier.classify(dischargeSlot) == SlotType.DES_Sale) {
				manager.add(new FOBDESSwitchAction("Convert to FOB Sale", slot));
			}
		}
	}

	private class FOBDESSwitchAction extends Action {
		private final Slot slot;

		public FOBDESSwitchAction(final String label, final Slot slot) {
			super(label);
			this.slot = slot;
		}

		@Override
		public void run() {

			final CompoundCommand cmd = new CompoundCommand(getText());
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (SlotClassifier.classify(loadSlot) != SlotType.DES_Buy) {
					cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSlot, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE, !loadSlot.isDESPurchase()));
					if (loadSlot.isDESPurchase()) {
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSlot, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, SetCommand.UNSET_VALUE));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSlot, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE));
					} else {
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSlot, CargoPackage.Literals.SLOT__DIVERTIBLE, Boolean.TRUE));

						final Cargo cargo = loadSlot.getCargo();
						if (cargo != null) {
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, SetCommand.UNSET_VALUE));
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE));
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.FALSE));
						}
					}
				}
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				if (SlotClassifier.classify(dischargeSlot) != SlotType.FOB_Sale) {
					cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSlot, CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE, !dischargeSlot.isFOBSale()));
					if (dischargeSlot.isFOBSale()) {
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSlot, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, SetCommand.UNSET_VALUE));
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSlot, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE));
					} else {
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSlot, CargoPackage.Literals.SLOT__DIVERTIBLE, Boolean.TRUE));
						final Cargo cargo = dischargeSlot.getCargo();
						if (cargo != null) {
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, SetCommand.UNSET_VALUE));
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE));
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.FALSE));
						}
					}
				}
			}

			if (cmd.canExecute()) {
				scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
			}

		}
	}

	/**
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
		LOOP_ROUTES: for (final Route route : scenarioModel.getPortModel().getRoutes()) {
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


}