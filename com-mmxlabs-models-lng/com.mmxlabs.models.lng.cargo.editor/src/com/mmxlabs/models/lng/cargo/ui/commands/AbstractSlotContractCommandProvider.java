/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.common.commandservice.AbstractModelCommandProvider;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Abstract class used to create slot parameters
 * 
 * @author sg
 *
 * @param
 * 			<P>
 * @param <C>
 */
public abstract class AbstractSlotContractCommandProvider<P extends LNGPriceCalculatorParameters, C extends SlotContractParams> extends AbstractModelCommandProvider<Object> {

	private final @NonNull Class<P> pricingParamsClass;
	private final @NonNull Class<C> slotContractParamsClass;
	private final @NonNull EClass slotContractParamsEClass;

	private final @NonNull EFactory factory;
	private final @NonNull SlotFilter filter;

	@FunctionalInterface
	public static interface SlotFilter {

		boolean includeSlot(@NonNull Slot slot, @NonNull CommandParameter parameter);
	}

	protected static @NonNull SlotFilter FILTER_LoadSlotFilter = (s, p) -> {
		return s instanceof LoadSlot;
	};

	protected static @NonNull SlotFilter FILTER_DischargeSlotFilter = (s, p) -> {
		return s instanceof DischargeSlot;
	};

	protected AbstractSlotContractCommandProvider(final @NonNull Class<P> pricingParamsClass, @NonNull final Class<C> slotContractParamsClass, @NonNull final EClass slotContractParamsEClass,
			final @NonNull EFactory factory, final @NonNull SlotFilter filter) {
		this.pricingParamsClass = pricingParamsClass;
		this.slotContractParamsClass = slotContractParamsClass;
		this.slotContractParamsEClass = slotContractParamsEClass;
		this.factory = factory;
		this.filter = filter;
	}

	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		return null;
	}

	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {

		if (rootObject instanceof LNGScenarioModel) {

			if (parameter.getOwner() instanceof Slot) {
				final Slot slot = (Slot) parameter.getOwner();

				// Check for contract change
				if (parameter.getFeature() == CargoPackage.eINSTANCE.getSlot_Contract()) {
					if (parameter.getEValue() != null) {

						if (!filter.includeSlot(slot, parameter)) {
							return null;
						}

						final Contract contract = (Contract) parameter.getEValue();
						final LNGPriceCalculatorParameters priceCalculatorParameters = contract.getPriceInfo();
						if (pricingParamsClass.isInstance(priceCalculatorParameters)) {

							for (final EObject ext : slot.getExtensions()) {
								if (slotContractParamsClass.isInstance(ext)) {
									// Already present
									return null;
								}
							}
							// No params found, create new instance.
							final EObject slotContractParams = factory.create(slotContractParamsEClass);
							return AddCommand.create(editingDomain, slot, MMXCorePackage.eINSTANCE.getMMXObject_Extensions(), slotContractParams);
						}
					}
				}
			}
		}

		return null;
	}
}
