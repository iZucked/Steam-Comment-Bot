/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
import com.mmxlabs.models.ui.registries.IModelFactoryRegistry;

/**
 */
public class CargoEditingCommands {

	private final @NonNull EditingDomain editingDomain;

	// private final @NonNull LNGScenarioModel scenarioModel;

	private final @NonNull IModelFactoryRegistry modelFactoryRegistry;

	private final @Nullable BaseLegalEntity defaultEntity;
	private final @NonNull CargoModel cargoModel;
	private final @NonNull CommercialModel commercialModel;

	private @NonNull MMXRootObject rootObject;

	public CargoEditingCommands(final @NonNull EditingDomain editingDomain, final @NonNull MMXRootObject rootObject, @NonNull CargoModel cargoModel, @NonNull CommercialModel commercialModel,
			final @NonNull IModelFactoryRegistry modelFactoryRegistry) {
		this.editingDomain = editingDomain;
		this.rootObject = rootObject;
		this.cargoModel = cargoModel;
		this.commercialModel = commercialModel;
		this.modelFactoryRegistry = modelFactoryRegistry;

		// No need for listener to update on change to count as users cannot edit number of entities.
		if (commercialModel.getEntities().size() == 1) {
			defaultEntity = commercialModel.getEntities().get(0);
		} else {
			defaultEntity = null;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T createObject(final EClass clz, final EReference reference, final EObject container) {
		final List<IModelFactory> factories = modelFactoryRegistry.getModelFactories(clz);

		// TODO: Pre-generate and link to UI
		// TODO: Add FOB/DES etc as explicit slot types.
		final IModelFactory factory = factories.get(0);
		final Collection<? extends ISetting> settings = factory.createInstance(rootObject, container, reference, null);
		if (settings.isEmpty() == false) {

			for (final ISetting setting : settings) {

				return (T) setting.getInstance();
			}
		}
		return (T) null;
	}

	public Cargo createNewCargo(final List<Command> setCommands, final CargoModel cargoModel) {
		// Create a cargo
		final Cargo newCargo = createObject(CargoPackage.eINSTANCE.getCargo(), CargoPackage.eINSTANCE.getCargoModel_Cargoes(), cargoModel);
		newCargo.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());

		// Factory creates new slots by default - remove them
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
		if (market instanceof FOBPurchasesMarket) {
			final FOBPurchasesMarket fobPurchasesMarket = (FOBPurchasesMarket) market;
			newLoad.setPort((Port) fobPurchasesMarket.getNotionalPort());
		}
		// newLoad.setContract((Contract) market.getContract());
		newLoad.setOptional(true);
		newLoad.setName("");

		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

		return newLoad;
	}

	/**
	 * @param transferPort
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
		if (!(newLoad instanceof SpotSlot) && defaultEntity != null) {
			newLoad.setEntity(defaultEntity);
		}
		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));
		return newLoad;
	}

	public DischargeSlot createNewDischarge(final List<Command> setCommands, final CargoModel cargoModel, final boolean isFOBSale) {

		final DischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		newDischarge.setFOBSale(isFOBSale);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newDischarge.setName("");

		if (!(newDischarge instanceof SpotSlot) && defaultEntity != null) {
			newDischarge.setEntity(defaultEntity);
		}

		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
		return newDischarge;
	}

	public SpotDischargeSlot createNewSpotDischarge(final List<Command> setCommands, final CargoModel cargoModel, final SpotMarket market) {

		final SpotDischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getSpotDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newDischarge.setMarket(market);
		newDischarge.setName("");
		if (market instanceof DESSalesMarket) {
			final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
			newDischarge.setPort((Port) desSalesMarket.getNotionalPort());
			newDischarge.setFOBSale(false);
		} else if (market instanceof FOBSalesMarket) {
			newDischarge.setFOBSale(true);
		} else {
			assert false;
		}
		newDischarge.setOptional(true);

		setCommands.add(AddCommand.create(editingDomain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
		return newDischarge;
	}

	public void runWiringUpdate(final List<Command> setCommands, final List<Command> deleteCommands, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {

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

			if (cargo != null && dischargeSlot.isFOBSale()) {
				// Cargo assignments should be removed.
				setCommands.add(SetCommand.create(editingDomain, cargo, CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType(), SetCommand.UNSET_VALUE));
				setCommands.add(SetCommand.create(editingDomain, cargo, CargoPackage.eINSTANCE.getAssignableElement_Locked(), Boolean.FALSE));
				setCommands.add(SetCommand.create(editingDomain, cargo, CargoPackage.eINSTANCE.getAssignableElement_SequenceHint(), SetCommand.UNSET_VALUE));
				setCommands.add(SetCommand.create(editingDomain, cargo, CargoPackage.eINSTANCE.getAssignableElement_SpotIndex(), SetCommand.UNSET_VALUE));
			}

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
			setCommands.add(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), cargo));
			setCommands.add(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), cargo));
		}
	}

	/**
	 * Method to check the state of the data model after command execution
	 * 
	 * @param cargoModel
	 */
	public void verifyCargoModel(@NonNull final CargoModel cargoModel) {

		cargoModel.getLoadSlots().forEach(s -> {
			final Cargo c = s.getCargo();
			if (c != null) {
				if (!cargoModel.getCargoes().contains(c)) {
					throw new IllegalStateException(String.format("Inconsistent data model - slot %s has uncontained cargo reference", s.getName()));
				}
			}
		});

		cargoModel.getDischargeSlots().forEach(s -> {
			final Cargo c = s.getCargo();
			if (c != null) {
				if (!cargoModel.getCargoes().contains(c)) {
					throw new IllegalStateException(String.format("Inconsistent data model - slot %s has uncontained cargo reference", s.getName()));
				}
			}
		});

		cargoModel.getCargoes().forEach(c -> {
			c.getSlots().forEach(s -> {
				if (!(cargoModel.getLoadSlots().contains(s) || cargoModel.getDischargeSlots().contains(s))) {
					throw new IllegalStateException(String.format("Inconsistent data model - cargo %s has uncontained slot reference %s", c.getLoadName(), s.getName()));
				}
			});
			if (c.getSlots().size() < 2) {
				throw new IllegalStateException(String.format("Inconsistent data model - cargo %s has less than 2 slots (%d in total)", c.getLoadName(), c.getSlots().size()));
			}
		});
	}
}
