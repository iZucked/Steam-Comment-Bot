/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.importer.CargoImporter;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

/**
 * Extended {@link ImportAction} implementation for {@link Cargo}es. The Cargo import imports {@link Cargo}, {@link LoadSlot} and {@link DischargeSlot} instances. The {@link Cargo} object additionally
 * encode the wiring. This import action merges the cargoes created from the {@link CargoImporter} with the existing cargoes. The {@link CargoImportAction} checks that as slots are moved between
 * cargoes, invalid cargo objects are removed (those with less than two slots). Unlike standard {@link ImportAction} sub-classes, the original model {@link EObject} instance is reused where possible -
 * normally the newly imported objects replace their original counterpart.
 * 
 * @author Simon Goodall
 * @since 3.0
 * 
 */
public final class CargoImportAction extends SimpleImportAction {
	public CargoImportAction(final IScenarioEditingLocation part, final ScenarioTableViewer viewer) {
		super(part, viewer);
	}

	public CargoImportAction(final ImportHooksProvider iph, final FieldInfoProvider fip) {
		super(iph, fip);
	}

	/**
	 * Merge the imported list of objects with the existing {@link Cargo}es and {@link Slot}s in the {@link CargoModel}.
	 */
	@Override
	public Command mergeImports(final EObject container, final EReference containment, final Collection<EObject> imports) {

		final EditingDomain domain = importHooksProvider.getEditingDomain();

		final List<EObject> cargoes = new ArrayList<EObject>();
		final List<EObject> loads = new ArrayList<EObject>();
		final List<EObject> discharges = new ArrayList<EObject>();
		final CompoundCommand mergeAll = new CompoundCommand();

		// Create maps of Cargo/Slot ID to Object instance
		final CargoModel cargoModel = (CargoModel) container;
		final Map<String, Cargo> nameToCargo = new HashMap<String, Cargo>();
		final Map<String, LoadSlot> nameToLoad = new HashMap<String, LoadSlot>();
		final Map<String, DischargeSlot> nameToDischarge = new HashMap<String, DischargeSlot>();

		{
			// Build up original names map
			for (final Cargo c : cargoModel.getCargoes()) {
				nameToCargo.put(c.getName(), c);
			}
			for (final LoadSlot s : cargoModel.getLoadSlots()) {
				nameToLoad.put(s.getName(), s);
			}
			for (final DischargeSlot s : cargoModel.getDischargeSlots()) {
				nameToDischarge.put(s.getName(), s);
			}
		}

		// Parallel list of original to updated import equivalent.
		final List<EObject> originals = new ArrayList<EObject>();
		final List<EObject> copies = new ArrayList<EObject>();

		// New Cargoes will need references to existing slots re-written. Objects which are new (i.e. have no original) are added to the relevant container.
		// UUID's of the new object are updated to match the original
		final List<EObject> newCargoes = new ArrayList<EObject>();
		for (final EObject o : imports) {
			if (o instanceof Cargo) {
				final Cargo cargo = (Cargo) o;
				// Filter out broken cargoes - those with less than two slots
				if (cargo.getSlots().size() < 2) {
					for (final Slot slot : cargo.getSlots()) {
						slot.setCargo(null);
					}
					// EOpposite should have already done this - but just to be sure
					cargo.getSlots().clear();
				} else {
					cargoes.add(cargo);
					// Overwrite UUID field with original
					if (nameToCargo.containsKey(cargo.getName())) {
						cargo.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), nameToCargo.get(cargo.getName()).getUuid());
						originals.add(nameToCargo.get(cargo.getName()));
						copies.add(cargo);
					} else {
						mergeAll.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), cargo));
						newCargoes.add(cargo);
					}
				}

			} else if (o instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) o;
				loads.add(o);
				// Overwrite UUID field with original
				if (nameToLoad.containsKey(loadSlot.getName())) {
					loadSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), nameToLoad.get(loadSlot.getName()).getUuid());
					originals.add(nameToLoad.get(loadSlot.getName()));
					copies.add(loadSlot);
				} else {
					mergeAll.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), loadSlot));

				}
			} else if (o instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) o;
				discharges.add(o);
				// Overwrite UUID field with original
				if (nameToDischarge.containsKey(dischargeSlot.getName())) {
					dischargeSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), nameToDischarge.get(dischargeSlot.getName()).getUuid());
					originals.add(nameToDischarge.get(dischargeSlot.getName()));
					copies.add(dischargeSlot);
				} else {
					mergeAll.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), dischargeSlot));

				}
			}
		}

		// Use a ChangeRecorder to record the differences between the original objects and the new imports. The ChangeRecorder can then be used to create a ChangeDescription object which in turn can
		// be wrapped in a Command object.
		{

			// For all the imports with an original, use a change recorder to capture the changes.
			// To avoid changing the original, we apply the original value onto the copy - then we reverse the change to get back to the original state.
			final ChangeRecorder changeRecorder = new ChangeRecorder(copies);
			for (int i = 0; i < originals.size(); ++i) {
				final EObject original = originals.get(i);
				final EObject copy = copies.get(i);

				for (final EStructuralFeature feature : original.eClass().getEAllStructuralFeatures()) {

					// Skip certain features
					if (!needsToBeReplaced(feature)) {
						continue;
					}

					// Set or unset fields as appropriate
					if (original.eIsSet(feature)) {
						copy.eSet(feature, original.eGet(feature));
					} else {
						copy.eUnset(feature);
					}

				}
			}

			// Get the recorded set of changes;
			final ChangeDescription changeDescription = changeRecorder.endRecording();

			// Sanity Check
			for (final EObject e : changeDescription.getObjectChanges().keySet()) {
				if (originals.contains(e) == true) {
					throw new IllegalStateException();
				}
				if (copies.contains(e) == false) {
					throw new IllegalStateException();
				}
			}

			// Map change description from copies to originals
			rewriteReferences(changeDescription, originals, copies);

			// Sanity Check
			for (final EObject e : changeDescription.getObjectChanges().keySet()) {
				if (originals.contains(e) == false) {
					throw new IllegalStateException();
				}

				if (copies.contains(e) == true) {
					throw new IllegalStateException();
				}
			}

			// Create a command out of it!
			mergeAll.append(new ChangeDescriptionCommand(changeDescription, originals));
		}

		// Handle re-wiring of cargoes
		mergeAll.append(rewireCargoes(cargoModel, cargoes, loads, discharges));

		return mergeAll;
	}

	/**
	 * This method handles the Cargo update. This includes merging Cargo record updates, re-wiring of slots and removing cargoes which are no longer valid
	 * 
	 * @param container
	 * @param containment
	 * @param cargoes
	 * @param loads
	 * @param discharges
	 * @return
	 */
	private Command rewireCargoes(final CargoModel cargoModel, final List<EObject> newCargoes, final List<EObject> newLoads, final List<EObject> newDischarges) {
		final EditingDomain domain = importHooksProvider.getEditingDomain();
		final CompoundCommand mergeCommand = new CompoundCommand();
		// Add Identity command incase there is no other command added here
		mergeCommand.append(IdentityCommand.INSTANCE);

		final Map<String, Cargo> nameToCargo = new HashMap<String, Cargo>();
		final Map<String, LoadSlot> nameToLoad = new HashMap<String, LoadSlot>();
		final Map<String, DischargeSlot> nameToDischarge = new HashMap<String, DischargeSlot>();

		{
			// Build up original names map
			for (final Cargo c : cargoModel.getCargoes()) {
				nameToCargo.put(c.getName(), c);
			}
			for (final LoadSlot s : cargoModel.getLoadSlots()) {
				nameToLoad.put(s.getName(), s);
			}
			for (final DischargeSlot s : cargoModel.getDischargeSlots()) {
				nameToDischarge.put(s.getName(), s);
			}
		}

		// Build up a count of the number of slots each cargo has - this is used to determine whether or not to remove a cargo. Build a map of slot ID to cargo
		final Map<Cargo, Integer> cargoSlotCount = new HashMap<Cargo, Integer>();
		final Map<String, Cargo> existingLoadSlotMap = new HashMap<String, Cargo>();
		final Map<String, Cargo> existingCargoMap = new HashMap<String, Cargo>();
		final Map<String, Cargo> existingDischargeSlotMap = new HashMap<String, Cargo>();
		for (final Cargo c : cargoModel.getCargoes()) {

			existingCargoMap.put(c.getName(), c);

			final int count = c.getSlots().size();
			for (final Slot slot : c.getSlots()) {
				if (slot instanceof LoadSlot) {
					existingLoadSlotMap.put(slot.getName(), c);
				} else if (slot instanceof DischargeSlot) {
					existingDischargeSlotMap.put(slot.getName(), c);
				} else {
					throw new ClassCastException("Slot is not a Load or Discharge");
				}
			}
			cargoSlotCount.put(c, count);
		}

		// Loop over all new cargoes and update wiring
		for (final EObject newECargo : newCargoes) {
			final Cargo newCargo = (Cargo) newECargo;

			// Existing cargo to be updated
			if (existingCargoMap.containsKey(newCargo.getName())) {
				final Cargo existingCargo = existingCargoMap.get(newCargo.getName());

				for (final Slot newSlot : newCargo.getSlots()) {

					if (newSlot instanceof LoadSlot) {
						// Existing load slot? Then update reference counting
						if (existingLoadSlotMap.containsKey(newSlot.getName())) {
							final Cargo oldCargo = existingLoadSlotMap.get(newSlot.getName());
							if (!oldCargo.getName().equals(newCargo.getName())) {
								// Re-wired!
								{
									// Decrement existing count
									final int count = cargoSlotCount.get(oldCargo);
									cargoSlotCount.put(oldCargo, count - 1);
								}
								{
									// Increment current cargo counter
									final int count = cargoSlotCount.get(existingCargo);
									cargoSlotCount.put(existingCargo, count + 1);
								}
							}
						}
					} else if (newSlot instanceof DischargeSlot) {
						// Existing discharge slot? Then update reference counting
						if (existingDischargeSlotMap.containsKey(newSlot.getName())) {
							final Cargo oldCargo = existingDischargeSlotMap.get(newSlot.getName());
							if (!oldCargo.getName().equals(newCargo.getName())) {
								// Re-wired!
								{
									// Decrement existing count
									final int count = cargoSlotCount.get(oldCargo);
									cargoSlotCount.put(oldCargo, count - 1);
								}
								{
									// Increment current cargo counter
									final int count = cargoSlotCount.get(existingCargo);
									cargoSlotCount.put(existingCargo, count + 1);
								}
							}
						}
					} else {
						throw new ClassCastException("Slot is not a Load or Discharge");
					}
				}
			} else {
				// New Cargo, but check for re-wiring of slots
				mergeCommand.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), newCargo));

				// Check re-wiring
				for (final Slot newSlot : newCargo.getSlots()) {
					if (newSlot instanceof LoadSlot) {
						if (existingLoadSlotMap.containsKey(newSlot.getName())) {

							final Cargo oldCargo = existingLoadSlotMap.get(newSlot.getName());
							// Decrement existing count
							final int count = cargoSlotCount.get(oldCargo);
							cargoSlotCount.put(oldCargo, count - 1);
						}

					} else if (newSlot instanceof DischargeSlot) {
						if (existingDischargeSlotMap.containsKey(newSlot.getName())) {
							final Cargo oldCargo = existingDischargeSlotMap.get(newSlot.getName());
							// Decrement existing count
							final int count = cargoSlotCount.get(oldCargo);
							cargoSlotCount.put(oldCargo, count - 1);
						}
					} else {
						throw new ClassCastException("Slot is not a Load or Discharge");
					}
				}
			}

			// We've now handled the existing slot decrements - now handle the new wiring.
			// Note - some of these commands may be redundant - the slot may already be wired to the cargo.

			// Look up original cargo if is exists.
			final Cargo cargo = nameToCargo.containsKey(newCargo.getName()) ? nameToCargo.get(newCargo.getName()) : newCargo;
			for (final Slot newSlot : newCargo.getSlots()) {
				if (newSlot instanceof LoadSlot) {

					if (nameToLoad.containsKey(newSlot.getName())) {
						final Slot oldSlot = nameToLoad.get(newSlot.getName());
						if (cargo.getSlots().contains(newSlot)) {
							mergeCommand.append(ReplaceCommand.create(domain, cargo, CargoPackage.eINSTANCE.getCargo_Slots(), newSlot, Collections.singleton(oldSlot)));
							continue;
						}
					}
					// Look up original slot, unless this is a new slot
					final LoadSlot slot = nameToLoad.containsKey(newSlot.getName()) ? nameToLoad.get(newSlot.getName()) : (LoadSlot) newSlot;
					if (cargo.getSlots().contains(slot) == false) {
						mergeCommand.append(SetCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_Cargo(), cargo));
					}
				} else if (newSlot instanceof DischargeSlot) {
					if (nameToDischarge.containsKey(newSlot.getName())) {
						final Slot oldSlot = nameToDischarge.get(newSlot.getName());
						if (cargo.getSlots().contains(newSlot)) {
							mergeCommand.append(ReplaceCommand.create(domain, cargo, CargoPackage.eINSTANCE.getCargo_Slots(), newSlot, Collections.singleton(oldSlot)));
							continue;
						}
					}
					// Look up original slot, unless this is a new slot
					final DischargeSlot slot = nameToDischarge.containsKey(newSlot.getName()) ? nameToDischarge.get(newSlot.getName()) : (DischargeSlot) newSlot;
					if (cargo.getSlots().contains(slot) == false) {
						mergeCommand.append(SetCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_Cargo(), cargo));
					}
				} else {
					throw new ClassCastException("Slot is not a Load or Discharge");
				}
			}
		}

		for (final Map.Entry<Cargo, Integer> e : cargoSlotCount.entrySet()) {
			final int count = e.getValue();
			if (count < 2) {
				mergeCommand.append(DeleteCommand.create(domain, e.getKey()));
			}
			if (count > 2) {
				throw new RuntimeException("Cargo has more than two slots!");
			}
		}

		return mergeCommand;
	}

	/**
	 * For the given object, find all references to objects in the copies list and replace with the equivalent in the original list. This assumes both the copies and originals list are the same size
	 * and the same index maps between the original and copy.
	 * 
	 * @param object
	 * @param originals
	 * @param copies
	 */
	private void rewriteReferences(final EObject object, final List<EObject> originals, final List<EObject> copies) {
		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(copies, object);

		for (int i = 0; i < originals.size(); ++i) {
			final EObject original = originals.get(i);
			final EObject copy = copies.get(i);

			final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(copy);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					if (needsToBeReplaced(setting)) {
						EcoreUtil.replace(setting, copy, original);
					}
				}
			}
		}
	}

	private boolean needsToBeReplaced(final EStructuralFeature.Setting setting) {
		final EStructuralFeature feature = setting.getEStructuralFeature();

		return needsToBeReplaced(feature);
	}

	/**
	 * See if a particular reference is valid for reference re-write. Derived features should not be changed. We also ignore Cargo <-> Slot references as we maintain this ourselves in the
	 * {@link #rewireCargoes(CargoModel, List, List, List)} method.
	 * 
	 * @param feature
	 * @return
	 */
	private boolean needsToBeReplaced(final EStructuralFeature feature) {
		if (feature == CargoPackage.eINSTANCE.getCargo_Slots()) {
			return false;
		}
		if (feature == CargoPackage.eINSTANCE.getSlot_Cargo()) {
			return false;
		}

		return !feature.isDerived() && feature.isChangeable();
	}

	/**
	 * {@link Command} to wrap a {@link ChangeDescription}
	 * 
	 */
	class ChangeDescriptionCommand extends AbstractCommand {
		private ChangeDescription changeDescription;
		private Collection<?> affectedObjects;

		public ChangeDescriptionCommand(final ChangeDescription changeDescription, final Collection<?> affectedObjects) {
			this.changeDescription = changeDescription;
			this.affectedObjects = affectedObjects;
		}

		@Override
		public Collection<?> getAffectedObjects() {
			return affectedObjects == null ? Collections.emptyList() : affectedObjects;
		}

		@Override
		public void dispose() {
			changeDescription = null;
			affectedObjects = null;
			super.dispose();
		}

		@Override
		protected boolean prepare() {
			return true;
		}

		@Override
		public boolean canExecute() {
			return true;
		}

		@Override
		public void execute() {
			changeDescription.applyAndReverse();
		}

		@Override
		public void undo() {
			changeDescription.applyAndReverse();
		}

		@Override
		public void redo() {
			changeDescription.applyAndReverse();
		}
	}

}