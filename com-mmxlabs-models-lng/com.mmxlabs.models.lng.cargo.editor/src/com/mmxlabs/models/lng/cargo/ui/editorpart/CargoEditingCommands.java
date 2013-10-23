/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.ui.util.SpotSlotHelper;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;

/**
 * @since 3.0
 */
public class CargoEditingCommands {

	private final LNGPortfolioModel portfolioModel;

	private final EditingDomain editingDomain;

	private final LNGScenarioModel rootObject;

	/**
	 * @since 4.0
	 */
	public CargoEditingCommands(final EditingDomain editingDomain, final LNGScenarioModel rootObject, final LNGPortfolioModel portfolioModel) {
		this.editingDomain = editingDomain;
		this.rootObject = rootObject;
		this.portfolioModel = portfolioModel;
	}

	@SuppressWarnings("unchecked")
	public <T> T createObject(final EClass clz, final EReference reference, final EObject container) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(clz);

		// TODO: Pre-generate and link to UI
		// TODO: Add FOB/DES etc as explicit slot types.
		final IModelFactory factory = factories.get(0);
		final Collection<? extends ISetting> settings = factory.createInstance(rootObject, container, reference, StructuredSelection.EMPTY);
		if (settings.isEmpty() == false) {

			for (final ISetting setting : settings) {

				return (T) setting.getInstance();
			}
		}
		return null;
	}

	public Cargo createNewCargo(final List<Command> setCommands, final CargoModel cargoModel) {
		// Create a cargo
		final Cargo newCargo = createObject(CargoPackage.eINSTANCE.getCargo(), CargoPackage.eINSTANCE.getCargoModel_Cargoes(), cargoModel);
		newCargo.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());

		// Factory create new slots by default - remove them
		newCargo.getSlots().clear();
		// Allow re-wiring
		newCargo.setAllowRewiring(true);

		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), newCargo));
		return newCargo;
	}

	public SpotLoadSlot createNewSpotLoad(final List<Command> setCommands, final CargoModel cargoModel, final boolean isDESPurchase, final SpotMarket market) {

		final SpotLoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getSpotLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
		newLoad.setDESPurchase(isDESPurchase);
		newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newLoad.setMarket(market);
		// newLoad.setContract((Contract) market.getContract());
		newLoad.setOptional(true);
		newLoad.setName("");
		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

		return newLoad;
	}

	/**
	 * @param transferPort
	 * @since 4.0
	 */
	public void insertShipToShipSlots(final List<Command> setCommands, final Slot sourceSlot, final CargoModel cargoModel, final Port transferPort) {

		final Cargo sourceCargo = sourceSlot.getCargo();

		// when adding a STS transfer, we detach the original slot and connect it to a new STS transfer which replaces the source slot in the original cargo (if any)

		// Create STS pair
		final LoadSlot transferLoad = createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
		transferLoad.eSet(MMXCorePackage.Literals.UUID_OBJECT__UUID, EcoreUtil.generateUUID());
		transferLoad.setWindowStart(sourceSlot.getWindowStart());
		transferLoad.setPriceExpression("0");
		transferLoad.setPort(transferPort);
		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS, transferLoad));

		final DischargeSlot transferDischarge = createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		transferDischarge.eSet(MMXCorePackage.Literals.UUID_OBJECT__UUID, EcoreUtil.generateUUID());
		transferDischarge.setWindowStart(sourceSlot.getWindowStart());
		transferDischarge.setPriceExpression("0");
		transferDischarge.setPort(transferPort);
		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS, transferDischarge));

		// Bind STS Slots
		transferLoad.setTransferFrom(transferDischarge);

		final boolean sourceIsLoad;
		final Slot newCargoLoad;
		final Slot newCargoDischarge;
		final Slot sourceReplacementSlot;

		final String sourceSlotName = sourceSlot.getName();
		final String sourceRelationName; // string describing the transfer relation to the source slot

		if (sourceSlot instanceof LoadSlot) {
			sourceIsLoad = true;
			sourceRelationName = "from";
			newCargoLoad = sourceSlot;
			newCargoDischarge = transferDischarge;
			sourceReplacementSlot = transferLoad;

		} else if (sourceSlot instanceof DischargeSlot) {
			sourceIsLoad = false;
			sourceRelationName = "to";
			newCargoLoad = transferLoad;
			newCargoDischarge = sourceSlot;
			sourceReplacementSlot = transferDischarge;
		} else {
			// ?
			throw new ClassCastException("Unexpected slot type");
		}

		// give the newly created slots sensible default names
		if (!"".equals(sourceSlotName)) {
			transferLoad.setName(String.format("load-%s-%s", sourceRelationName, sourceSlotName));
			transferDischarge.setName(String.format("discharge-%s-%s", sourceRelationName, sourceSlotName));
		}

		// create a new cargo for the target slot and bind it to the appropriate transfer slot
		final Cargo newCargo = createObject(CargoPackage.eINSTANCE.getCargo(), CargoPackage.eINSTANCE.getCargoModel_Cargoes(), cargoModel);
		newCargo.getSlots().clear();
		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.CARGO_MODEL__CARGOES, newCargo));
		// add the new load and discharge slots to the cargo
		setCommands.add(SetCommand.create(editingDomain, newCargoLoad, CargoPackage.Literals.SLOT__CARGO, newCargo));
		setCommands.add(SetCommand.create(editingDomain, newCargoDischarge, CargoPackage.Literals.SLOT__CARGO, newCargo));
		// Link cargo ID to slot ID
		setCommands.add(SetCommand.create(editingDomain, newCargo, MMXCorePackage.Literals.NAMED_OBJECT__NAME, newCargoLoad.getName()));

		// if the original source slot had a cargo already, we need its replacement to be attached to the cargo
		if (sourceCargo != null) {
			// replace the target slot in its original cargo with the corresponding transfer slot
			setCommands.add(SetCommand.create(editingDomain, sourceReplacementSlot, CargoPackage.Literals.SLOT__CARGO, sourceCargo));
		}
	}

	public LoadSlot createNewLoad(final List<Command> setCommands, final CargoModel cargoModel, final boolean isDESPurchase) {

		final LoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
		newLoad.setDESPurchase(isDESPurchase);
		newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newLoad.setName("");
		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

		return newLoad;
	}

	public DischargeSlot createNewDischarge(final List<Command> setCommands, final CargoModel cargoModel, final boolean isFOBSale) {

		final DischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		newDischarge.setFOBSale(isFOBSale);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newDischarge.setName("");
		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
		return newDischarge;
	}

	public SpotDischargeSlot createNewSpotDischarge(final List<Command> setCommands, final CargoModel cargoModel, final boolean isFOBSale, final SpotMarket market) {

		final SpotDischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getSpotDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		newDischarge.setFOBSale(isFOBSale);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newDischarge.setMarket(market);
		// newDischarge.setContract((Contract) market.getContract());
		newDischarge.setName("");
		if (market instanceof DESSalesMarket) {

			final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
			newDischarge.setPort((Port) desSalesMarket.getNotionalPort());
		}
		newDischarge.setOptional(true);
		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
		return newDischarge;
	}

	/**
	 * @since 4.0
	 */
	public void appendFOBDESCommands(final List<Command> setCommands, final List<Command> deleteCommands, final EditingDomain editingDomain, final AssignmentModel assignmentModel, final Cargo cargo,
			final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {

		if (loadSlot.isDESPurchase()) {
			deleteCommands.add(AssignmentEditorHelper.unassignElement(editingDomain, assignmentModel, cargo));

			setCommands.add(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getLoadSlot_ArriveCold(), false));
			setCommands.add(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
			setCommands.add(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Port(), dischargeSlot.getPort()));
			if (loadSlot instanceof SpotSlot) {
				final CompoundCommand cmd = new CompoundCommand();

				SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, loadSlot, dischargeSlot, cmd);
				if (!cmd.isEmpty()) {
					setCommands.add(cmd);
				}
			} else {
				setCommands.add(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), dischargeSlot.getWindowStart()));
				if (dischargeSlot.isSetWindowStartTime()) {
					setCommands.add(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), dischargeSlot.getWindowStartTime()));
				} else {
					setCommands.add(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), SetCommand.UNSET_VALUE));
				}
			}
		} else if (dischargeSlot.isFOBSale()) {
			deleteCommands.add(AssignmentEditorHelper.unassignElement(editingDomain, assignmentModel, cargo));
			setCommands.add(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
			setCommands.add(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port(), loadSlot.getPort()));
			if (dischargeSlot instanceof SpotSlot) {
				final CompoundCommand cmd = new CompoundCommand();
				SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, dischargeSlot, loadSlot, cmd);
				if (!cmd.isEmpty()) {
					setCommands.add(cmd);
				}
			} else {
				setCommands.add(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), loadSlot.getWindowStart()));
				if (loadSlot.isSetWindowStartTime()) {
					setCommands.add(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), loadSlot.getWindowStartTime()));
				} else {
					setCommands.add(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), SetCommand.UNSET_VALUE));
				}
			}
		}
	}

	public void runWiringUpdate(final List<Command> setCommands, final List<Command> deleteCommands, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
		final CargoModel cargoModel = portfolioModel.getCargoModel();
		final AssignmentModel assignmentModel = portfolioModel.getAssignmentModel();

		// Discharge has an existing slot, so remove the cargo & wiring
		if (dischargeSlot.getCargo() != null) {
			deleteCommands.add(DeleteCommand.create(editingDomain, dischargeSlot.getCargo()));

			// Optional market slots can be removed.
			for (final Slot s : dischargeSlot.getCargo().getSlots()) {
				if (s instanceof LoadSlot) {
					final LoadSlot oldSlot = (LoadSlot) s;
					if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
						deleteCommands.add(DeleteCommand.create(editingDomain, oldSlot));
					}
				}
			}
		}

		// Do we need to create a new cargo or re-wire and existing one.
		Cargo cargo = loadSlot.getCargo();
		if (cargo != null) {

			// Clear existing discharge slots
			for (final Slot slot : cargo.getSlots()) {
				if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot2 = (DischargeSlot) slot;
					setCommands.add(SetCommand.create(editingDomain, dischargeSlot2, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
				}
			}
			setCommands.add(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), cargo));
			// Force allow re-wiring on a change
			setCommands.add(SetCommand.create(editingDomain, cargo, CargoPackage.eINSTANCE.getCargo_AllowRewiring(), Boolean.TRUE));

			// Optional market slots can be removed.
			for (final Slot s : cargo.getSlots()) {
				if (s instanceof DischargeSlot) {
					final DischargeSlot oldSlot = (DischargeSlot) s;
					if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
						deleteCommands.add(DeleteCommand.create(editingDomain, oldSlot));
					}
				}
			}
		} else {
			cargo = createNewCargo(setCommands, cargoModel);
			cargo.setName(loadSlot.getName());
			setCommands.add(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), cargo));
			setCommands.add(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), cargo));
		}

		appendFOBDESCommands(setCommands, deleteCommands, editingDomain, assignmentModel, cargo, loadSlot, dischargeSlot);

	}

}
