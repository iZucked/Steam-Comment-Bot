package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
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

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.editor.editors.ldd.LDDEditor;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.dates.LocalDateUtil;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;

public class CargoEditorMenuHelper {

	private final Shell shell;

	private final class EditAction extends Action {
		private final EObject target;

		private EditAction(final String text, final EObject target) {
			super(text);
			this.target = target;
		}

		@Override
		public void run() {

			final DetailCompositeDialog dcd = new DetailCompositeDialog(shell, scenarioEditingLocation.getDefaultCommandHandler());
			try {
				scenarioEditingLocation.getEditorLock().claim();
				scenarioEditingLocation.setDisableUpdates(true);

				dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), Collections.<EObject> singletonList(target), scenarioEditingLocation.isLocked());
			} finally {
				scenarioEditingLocation.setDisableUpdates(false);
				scenarioEditingLocation.getEditorLock().release();
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

	private final IScenarioEditingLocation scenarioEditingLocation;
	private final CargoEditingCommands cec;

	public CargoEditorMenuHelper(final Shell shell, final IScenarioEditingLocation scenarioEditingLocation) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		cec = new CargoEditingCommands(scenarioEditingLocation);
		this.shell = shell;
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

	private void buildSwapMenu(final IMenuManager manager, final String name, final Slot source, final Map<String, Set<Slot>> targets, final boolean includeContract, final boolean includePort) {
		final MenuManager subMenu = new MenuManager(name, null);

		// For single item sub menus, skip the sub menu and add item directly
		if (targets.size() == 1) {
			for (final Map.Entry<String, Set<Slot>> e : targets.entrySet()) {
				for (final Slot target : e.getValue()) {
					createSwapAction(subMenu, source, target, includeContract, includePort);
				}
			}

		} else {
			for (final Map.Entry<String, Set<Slot>> e : targets.entrySet()) {
				final MenuManager subSubMenu = new MenuManager(e.getKey(), null);
				for (final Slot target : e.getValue()) {
					createSwapAction(subSubMenu, source, target, includeContract, includePort);
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
		final CargoModel cargoModel = scenarioEditingLocation.getRootObject().getSubModel(CargoModel.class);
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {

				final DischargeSlot dischargeSlot = dischargeSlots.get(index);

				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				if (dischargeSlot.isFOBSale()) {
					createNewSlotMenu(newMenuManager, dischargeSlot, false);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, false);
				} else {
					createNewSlotMenu(newMenuManager, dischargeSlot, false);
					createMenus(manager, dischargeSlot, cargoModel.getLoadSlots(), false);
					createSpotMarketMenu(newMenuManager, SpotType.DES_PURCHASE, dischargeSlot, false);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, false);
				}
				createEditMenu(manager, dischargeSlot, dischargeSlot.getCargo());
				createEditContractMenu(manager, dischargeSlot, dischargeSlot.getContract());
				createDeleteSlotMenu(manager, dischargeSlot);
			}

		};
		return l;

	}

	private void createEditContractMenu(final IMenuManager newMenuManager, final Slot slot, final Contract contract) {
		newMenuManager.add(new Separator());
		newMenuManager.add(new EditAction("Edit Contract", contract));
	}

	private void createEditMenu(final IMenuManager newMenuManager, final Slot slot, final Cargo cargo) {
		newMenuManager.add(new Separator());
		newMenuManager.add(new EditAction("Edit Slot", slot));
		if (cargo != null) {
			if (cargo.getSlots().size() > 2) {
				newMenuManager.add(new EditLDDAction("Edit Complex Cargo", cargo));
			}
			newMenuManager.add(new EditAction("Edit Cargo", cargo));
		}
	}

	public IMenuListener createLoadSlotMenuListener(final List<LoadSlot> loadSlots, final int index) {
		final CargoModel cargoModel = scenarioEditingLocation.getRootObject().getSubModel(CargoModel.class);
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				final LoadSlot loadSlot = loadSlots.get(index);
				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				if (loadSlot.isDESPurchase()) {
					createNewSlotMenu(newMenuManager, loadSlot, true);
					createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot, true);
				} else {
					createNewSlotMenu(newMenuManager, loadSlot, true);
					createMenus(manager, loadSlot, cargoModel.getDischargeSlots(), true);
					createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot, true);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_SALE, loadSlot, true);
				}
				createEditMenu(manager, loadSlot, loadSlot.getCargo());
				createEditContractMenu(manager, loadSlot, loadSlot.getContract());
				createDeleteSlotMenu(manager, loadSlot);
			}
		};
		return l;

	}

	public IMenuListener createSwapSlotsMenuListener(final List<Slot> slots, final int index) {
		final CargoModel cargoModel = scenarioEditingLocation.getRootObject().getSubModel(CargoModel.class);
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
						createSwapWithMenus(manager, loadSlot, cargoModel.getLoadSlots(), true);
						// createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot, true);
						// createSpotMarketMenu(newMenuManager, SpotType.FOB_SALE, loadSlot, true);
					}

				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					if (dischargeSlot.isFOBSale()) {
						// createNewSlotMenu(newMenuManager, dischargeSlot, false);
						// createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, false);
					} else {
						// createNewSlotMenu(newMenuManager, dischargeSlot, false);
						createSwapWithMenus(manager, dischargeSlot, cargoModel.getDischargeSlots(), false);
						// createSpotMarketMenu(newMenuManager, SpotType.DES_PURCHASE, dischargeSlot, false);
						// createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, false);
					}
				}

				createEditMenu(manager, slot, slot.getCargo());
				createEditContractMenu(manager, slot, slot.getContract());
				createDeleteSlotMenu(manager, slot);
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
				addSlotToTargets(target, contract.getName(), slotsByContract);
			}
			final Port port = target.getPort();
			if (port != null) {
				addSlotToTargets(target, port.getName(), slotsByPort);
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
			addSlotToTargets(target, "Any", slotsByDate);

		}
		{
			buildSwapMenu(manager, "Swap Slots By Contract", source, slotsByContract, false, true);
			// buildSubMenu(manager, "Slots By Date", source, sourceIsLoad, slotsByDate, true, true);
			buildSwapMenu(manager, "Swap Slots By Port", source, slotsByPort, true, false);
		}
	}

	private void createMenus(final IMenuManager manager, final Slot source, final List<? extends Slot> possibleTargets, final boolean sourceIsLoad) {

		final Map<String, Set<Slot>> slotsByDate = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByContract = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByPort = new TreeMap<String, Set<Slot>>();

		for (final Slot target : possibleTargets) {

			final int daysDifference;
			// Perform some filtering on the possible targets
			{
				final LoadSlot loadSlot;
				final DischargeSlot dischargeSlot;
				if (sourceIsLoad) {
					loadSlot = (LoadSlot) source;
					dischargeSlot = (DischargeSlot) target;
				} else {
					loadSlot = (LoadSlot) target;
					dischargeSlot = (DischargeSlot) source;
				}
				// Filter out current pairing
				if (loadSlot.getCargo() != null && loadSlot.getCargo() == dischargeSlot.getCargo()) {
					continue;
				}

				// Filter backwards pairings
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

			final Contract contract = target.getContract();
			if (contract != null) {
				addSlotToTargets(target, contract.getName(), slotsByContract);
			}
			final Port port = target.getPort();
			if (port != null) {
				addSlotToTargets(target, port.getName(), slotsByPort);
			}

			if (daysDifference < 5) {
				addSlotToTargets(target, "Less than 5 Days", slotsByDate);
			}
			if (daysDifference < 10) {
				addSlotToTargets(target, "Less than 10 Days", slotsByDate);
			}
			if (daysDifference < 20) {
				addSlotToTargets(target, "Less than 20 Days", slotsByDate);
			}
			if (daysDifference < 30) {
				addSlotToTargets(target, "Less than 30 Days", slotsByDate);
			}
			if (daysDifference < 60) {
				addSlotToTargets(target, "Less than 60 Days", slotsByDate);
			}
			addSlotToTargets(target, "Any", slotsByDate);

		}
		{
			buildSubMenu(manager, "Slots By Contract", source, sourceIsLoad, slotsByContract, false, true);
			buildSubMenu(manager, "Slots By Date", source, sourceIsLoad, slotsByDate, true, true);
			buildSubMenu(manager, "Slots By Port", source, sourceIsLoad, slotsByPort, true, false);
		}
	}

	void createNewSlotMenu(final IMenuManager menuManager, final Slot source, final boolean sourceIsLoad) {

		if (sourceIsLoad) {
			final LoadSlot loadSlot = (LoadSlot) source;
			if (loadSlot.isDESPurchase()) {
				// Only create new Discharge
				menuManager.add(new CreateSlotAction("Discharge", source, null, sourceIsLoad, false));
			} else {
				//
				menuManager.add(new CreateSlotAction("Discharge", source, null, sourceIsLoad, false));
				menuManager.add(new CreateSlotAction("FOB Sale", source, null, sourceIsLoad, true));
			}
		} else {
			final DischargeSlot dischargeSlot = (DischargeSlot) source;
			if (dischargeSlot.isFOBSale()) {
				// only load
				menuManager.add(new CreateSlotAction("Load", source, null, sourceIsLoad, false));
			} else {
				menuManager.add(new CreateSlotAction("Load", source, null, sourceIsLoad, false));
				menuManager.add(new CreateSlotAction("DES Purchase", source, null, sourceIsLoad, true));
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

	void createSpotMarketMenu(final IMenuManager manager, final SpotType spotType, final Slot source, final boolean sourceIsLoad) {
		final SpotMarketsModel pricingModel = scenarioEditingLocation.getRootObject().getSubModel(SpotMarketsModel.class);
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
				final Port loadPort = ((FOBSalesMarket) market).getLoadPort();
				if (loadPort == source.getPort()) {
					validMarkets.add(market);
				}
			}
			isSpecial = true;
		}
		final MenuManager subMenu = new MenuManager("New " + menuName + " Market Slot", null);

		for (final SpotMarket market : validMarkets) {
			subMenu.add(new CreateSlotAction("Create " + market.getName() + " slot", source, market, sourceIsLoad, isSpecial));
		}

		manager.add(subMenu);

	}

	private void createWireAction(final MenuManager subMenu, final Slot source, final Slot target, final boolean sourceIsLoad, final boolean includeContract, final boolean includePort) {
		final String name = getActionName(target, includeContract, includePort);
		if (sourceIsLoad) {
			subMenu.add(new WireAction(name, (LoadSlot) source, (DischargeSlot) target));
		} else {
			subMenu.add(new WireAction(name, (LoadSlot) target, (DischargeSlot) source));
		}
	}

	private void createSwapAction(final MenuManager subMenu, final Slot source, final Slot target, final boolean includeContract, final boolean includePort) {
		final String name = getActionName(target, includeContract, includePort);
		subMenu.add(new SwapAction(name, source, target));
	}

	private String getActionName(final Slot slot, final boolean includePort, final boolean includeContract) {
		final StringBuilder sb = new StringBuilder();

		if (includeContract && slot.getContract() != null) {
			sb.append(slot.getContract().getName());
			sb.append(" - ");
		}

		if (includePort && slot.getPort() != null) {
			sb.append(slot.getPort().getName());
			sb.append(" - ");
		}
		{
			final DateFormat df = DateFormat.getDateInstance();
			if (slot.getPort() != null) {
				final TimeZone zone = LocalDateUtil.getTimeZone(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
				df.setTimeZone(zone);
			}
			sb.append(df.format(slot.getWindowStart()));
			sb.append(" - ");
		}
		{
			sb.append(slot.getName());
			sb.append(" - ");

		}
		Cargo c = null;
		if (slot instanceof LoadSlot) {
			if (((LoadSlot) slot).isDESPurchase()) {
				sb.append("(DES Purchase) ");
			}
			c = ((LoadSlot) slot).getCargo();
		}
		if (slot instanceof DischargeSlot) {
			if (((DischargeSlot) slot).isFOBSale()) {
				sb.append("(FOB Sale) ");
			}
			c = ((DischargeSlot) slot).getCargo();
		}
		if (slot instanceof SpotSlot) {
			sb.append(((SpotSlot) slot).getMarket().getName());
			sb.append(" - ");

		}
		if (c != null) {
			sb.append(" (Cargo " + c.getName() + ")");
		} else {
			sb.append("(unused)");
		}
		return sb.toString();
	}

	private void addSlotToTargets(final Slot target, final String group, final Map<String, Set<Slot>> targets) {
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
		private final boolean isSpecial;

		public CreateSlotAction(final String name, final Slot source, final SpotMarket market, final boolean sourceIsLoad, final boolean isSpecial) {
			super(name);
			this.source = source;
			this.market = market;
			this.sourceIsLoad = sourceIsLoad;
			this.isSpecial = isSpecial;
		}

		@Override
		public void run() {
			final CargoModel cargoModel = scenarioEditingLocation.getRootObject().getSubModel(CargoModel.class);

			final List<Command> setCommands = new LinkedList<Command>();
			final List<Command> deleteCommands = new LinkedList<Command>();

			LoadSlot loadSlot;
			DischargeSlot dischargeSlot;
			if (sourceIsLoad) {
				loadSlot = (LoadSlot) source;
				if (market == null) {
					dischargeSlot = cec.createNewDischarge(setCommands, cargoModel, isSpecial);
				} else {
					dischargeSlot = cec.createNewSpotDischarge(setCommands, cargoModel, isSpecial, market);
				}
			} else {
				if (market == null) {
					loadSlot = cec.createNewLoad(setCommands, cargoModel, isSpecial);
				} else {
					loadSlot = cec.createNewSpotLoad(setCommands, cargoModel, isSpecial, market);
				}
				dischargeSlot = (DischargeSlot) source;
			}
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
			final Cargo c = source.getCargo();
			currentWiringCommand.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), source, CargoPackage.eINSTANCE.getSlot_Cargo(), null));
			currentWiringCommand.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), target, CargoPackage.eINSTANCE.getSlot_Cargo(), c));

			scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand);
		}

	}

	public void editLDDCargo(final Cargo cargo) {
		try {
			scenarioEditingLocation.getEditorLock().claim();
			scenarioEditingLocation.setDisableUpdates(true);

			final LDDEditor editor = new LDDEditor(shell, scenarioEditingLocation);
			// editor.setBlockOnOpen(true);

			final int ret = editor.open(cargo);
			final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
			if (ret == Window.OK) {
				final CargoModel cargomodel = scenarioEditingLocation.getRootObject().getSubModel(CargoModel.class);

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
				Iterator<Command> itr = new LinkedList<Command>(editor.getExecutedCommands()).descendingIterator();
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
			scenarioEditingLocation.getEditorLock().release();
		}
	}
}