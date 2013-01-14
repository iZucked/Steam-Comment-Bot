/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Adds a set command to the setter for cargo names
 * 
 * @author Tom Hinton, Simon Goodall
 * 
 */
public class SlotNameUpdatingCommandProvider implements IModelCommandProvider {

	@Override
	public Command provideAdditionalCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {

		final Set<EObject> seenObjects = getSeenObjects();

		if (commandClass == SetCommand.class) {
			if (parameter.getEStructuralFeature() == MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
				if (!seenObjects.add(parameter.getEOwner())) {
					return null;
				}
				if (parameter.getEOwner() instanceof Cargo) {
					final Cargo cargo = (Cargo) parameter.getEOwner();
					final Slot load = cargo.getLoadSlot();
					final Slot discharge = cargo.getDischargeSlot();
					final CompoundCommand fixer = new CompoundCommand("Slot Name Update");
					if (load != null) {
						// Only update if changed.
						if (load.getName() == null || !load.getName().equals(parameter.getValue())) {
							fixer.append(SetCommand.create(editingDomain, load, MMXCorePackage.eINSTANCE.getNamedObject_Name(), parameter.getValue()));
							seenObjects.add(load);
						}
					}

					if (discharge != null) {
						// Only update if the previous value matched correctly - otherwise it could be user specified.
						if (discharge.getName() == null || discharge.getName().isEmpty() || discharge.getName().equals("d-" + cargo.getName())) {
							fixer.append(SetCommand.create(editingDomain, discharge, MMXCorePackage.eINSTANCE.getNamedObject_Name(), "d-" + parameter.getValue()));
							seenObjects.add(discharge);
						}
					}
					if (fixer.isEmpty()) {
						return null;
					}
					return fixer;

				} else if (parameter.getEOwner() instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) parameter.getEOwner();
					final Cargo cargo = loadSlot.getCargo();
					final CompoundCommand fixer = new CompoundCommand("Slot Name Update");
					if (cargo != null) {
						// Update cargo Name only if different
						if (cargo.getName() == null || !cargo.getName().equals(parameter.getValue())) {
							fixer.append(SetCommand.create(editingDomain, cargo, MMXCorePackage.eINSTANCE.getNamedObject_Name(), parameter.getValue()));
							seenObjects.add(cargo);
						}
					}

					if (fixer.isEmpty()) {
						return null;
					}
					return fixer;

				}
			} else if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getCargo_LoadSlot()) {
				final LoadSlot loadSlot = (LoadSlot) parameter.getEValue();
				final Cargo cargo = (Cargo) parameter.getEOwner();
				final CompoundCommand fixer = new CompoundCommand("Slot Name Update");
				if (cargo != null && loadSlot != null) {
					// Update cargo Name only if different
					if (cargo.getName() == null || !cargo.getName().equals(loadSlot.getName())) {
						fixer.append(SetCommand.create(editingDomain, cargo, MMXCorePackage.eINSTANCE.getNamedObject_Name(), loadSlot.getName()));
						seenObjects.add(cargo);
					}
				}

				if (fixer.isEmpty()) {
					return null;
				}
				return fixer;
			} else if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getCargo_DischargeSlot()) {
				final DischargeSlot dischargeSlot = (DischargeSlot) parameter.getEValue();
				final Cargo cargo = (Cargo) parameter.getEOwner();
				final CompoundCommand fixer = new CompoundCommand("Slot Name Update");
				if (cargo != null && dischargeSlot != null) {

					// Only update if the previous value matched correctly - otherwise it could be user specified.
					if (dischargeSlot.getName() == null || dischargeSlot.getName().isEmpty()) {
						fixer.append(SetCommand.create(editingDomain, dischargeSlot, MMXCorePackage.eINSTANCE.getNamedObject_Name(), "d-" + cargo.getName()));
						seenObjects.add(dischargeSlot);
					}
				}

				if (fixer.isEmpty()) {
					return null;
				}
				return fixer;

			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Set<EObject> getSeenObjects() {

		Object object = getContext();
		if (object == null) {
			object = new HashSet<EObject>();
			setContext(object);
		}
		return (Set<EObject>) object;
	}

	private final ThreadLocal<AtomicInteger> provisionStack = new ThreadLocal<AtomicInteger>();
	private final ThreadLocal<Object> provisionContext = new ThreadLocal<Object>();

	protected void setContext(final Object context) {
		provisionContext.set(context);
	}

	protected Object getContext() {
		return provisionContext.get();
	}

	protected int getProvisionDepth() {
		return provisionStack.get().get();
	}

	@Override
	public void startCommandProvision() {
		if (provisionStack.get() == null) {
			provisionStack.set(new AtomicInteger(0));
		}
		if (provisionStack.get().getAndIncrement() == 0) {
			provisionContext.set(null);
		}
	}

	@Override
	public void endCommandProvision() {
		if (provisionStack.get().decrementAndGet() == 0) {
			provisionContext.set(null);
		}
	}
}
