/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class DischargeCVValueCompatabilityConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;

			for (final Slot slot : cargo.getSlots()) {
				if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;

					final Contract contract = dischargeSlot.getContract();
					final Port port = dischargeSlot.getPort();

					if (contract instanceof SalesContract || dischargeSlot.isSetPriceExpression()) {
						for (final Slot slot2 : cargo.getSlots()) {
							if (slot2 instanceof LoadSlot) {

								final LoadSlot loadSlot = (LoadSlot) slot2;
								final SalesContract salesContract = (SalesContract) contract;
								final double loadCV = loadSlot.getSlotOrDelegatedCV();
								final String format = "[Cargo|%s] Purchase CV %.2f is %s than the %s CV (%.2f) for %s '%s'.";

								// Do four bounds checks
								checkDischargeAndContractMin(failures, salesContract, dischargeSlot, loadSlot, cargo, loadCV, format, ctx);
								checkDischargeAndContractMax(failures, salesContract, dischargeSlot, loadSlot, cargo, loadCV, format, ctx);
								checkPortBoundsMin(port, failures, dischargeSlot, loadSlot, cargo, loadCV, format, ctx);
								checkPortBoundsMax(port, failures, dischargeSlot, loadSlot, cargo, loadCV, format, ctx);
							}
						}
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	/*
	 * Check Discharge Slot or Contract CV bounds are met if set
	 */
	private void checkDischargeAndContractMin(final List<IStatus> failures, final SalesContract salesContract, final DischargeSlot dischargeSlot, final LoadSlot loadSlot, final Cargo cargo,
			final double loadCV, final String format, final IValidationContext ctx) {
		if (dischargeSlot.isSetMinCvValue() || (salesContract != null && salesContract.isSetMinCvValue())) {
			final Double minCvValue = dischargeSlot.getSlotOrContractMinCv();
			if (minCvValue != null && loadCV < minCvValue) {
				if (dischargeSlot.isSetMinCvValue()) {
					final ArrayList<Pair<EObject, EStructuralFeature>> detailsDecoratorData = new ArrayList<Pair<EObject, EStructuralFeature>>() {
						{
							add(new Pair<EObject, EStructuralFeature>(dischargeSlot, CargoPackage.eINSTANCE.getDischargeSlot_MinCvValue()));
						}
					};
					failures.add(addDetailConstraintStatusDecorator("discharge slot", dischargeSlot.getName(), cargo.getLoadName(), dischargeSlot, loadSlot, true, loadCV, (double) minCvValue,
							detailsDecoratorData, format, ctx));
				} else { // sales contract
					addContractError(true, failures, salesContract, dischargeSlot, loadSlot, cargo, loadCV, minCvValue, format, ctx);
				}
			}
		}
	}

	private void checkDischargeAndContractMax(final List<IStatus> failures, final SalesContract salesContract, final DischargeSlot dischargeSlot, final LoadSlot loadSlot, final Cargo cargo,
			final double loadCV, final String format, final IValidationContext ctx) {
		if (dischargeSlot.isSetMaxCvValue() || (salesContract != null && salesContract.isSetMaxCvValue())) {
			final Double maxCvValue = dischargeSlot.getSlotOrContractMaxCv();
			if (maxCvValue != null && loadCV > maxCvValue) {
				if (dischargeSlot.isSetMaxCvValue()) {
					final ArrayList<Pair<EObject, EStructuralFeature>> detailsDecoratorData = new ArrayList<Pair<EObject, EStructuralFeature>>() {
						{
							add(new Pair<EObject, EStructuralFeature>(dischargeSlot, CargoPackage.eINSTANCE.getDischargeSlot_MaxCvValue()));
						}
					};
					failures.add(addDetailConstraintStatusDecorator("discharge slot", dischargeSlot.getName(), cargo.getLoadName(), dischargeSlot, loadSlot, true, loadCV, (double) maxCvValue,
							detailsDecoratorData, format, ctx));
				} else { // sales contract
					addContractError(false, failures, salesContract, dischargeSlot, loadSlot, cargo, loadCV, maxCvValue, format, ctx);
				}
			}
		}
	}

	private void addContractError(final boolean isMin, final List<IStatus> failures, final SalesContract salesContract, final DischargeSlot dischargeSlot, final LoadSlot loadSlot, final Cargo cargo,
			final double loadCV, final double dischargeCV, final String format, final IValidationContext ctx) {
		final ArrayList<Pair<EObject, EStructuralFeature>> detailsDecoratorData = new ArrayList<Pair<EObject, EStructuralFeature>>() {
			{
				add(new Pair<EObject, EStructuralFeature>(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract()));
			}
		};
		detailsDecoratorData.add(new Pair<EObject, EStructuralFeature>(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract()));
		failures.add(addDetailConstraintStatusDecorator("sales contract", salesContract.getName(), cargo.getLoadName(), dischargeSlot, loadSlot, isMin, loadCV, (double) dischargeCV, detailsDecoratorData,
				format, ctx));
	}

	/*
	 * Check Port CV bounds are met
	 */
	private void checkPortBoundsMin(final Port port, final List<IStatus> failures, final DischargeSlot dischargeSlot, final LoadSlot loadSlot, final Cargo cargo, final double loadCV,
			final String format, final IValidationContext ctx) {
		if (port != null && port.isSetMinCvValue()) {
			final Double minCvValue = port.getMinCvValue();
			if (minCvValue != null && loadCV < minCvValue) {
				addPortBoundsError(true, port, failures, dischargeSlot, loadSlot, cargo, loadCV, minCvValue, format, ctx);
			}
		}
	}

	private void checkPortBoundsMax(final Port port, final List<IStatus> failures, final DischargeSlot dischargeSlot, final LoadSlot loadSlot, final Cargo cargo, final double loadCV,
			final String format, final IValidationContext ctx) {
		if (port != null && port.isSetMaxCvValue()) {
			final Double maxCvValue = port.getMaxCvValue();
			if (maxCvValue != null && loadCV > maxCvValue) {
				addPortBoundsError(true, port, failures, dischargeSlot, loadSlot, cargo, loadCV, maxCvValue, format, ctx);
			}
		}
	}

	private void addPortBoundsError(final boolean isMin, final Port port, final List<IStatus> failures, final DischargeSlot dischargeSlot, final LoadSlot loadSlot, final Cargo cargo,
			final double loadCV, final double dischargeCV, final String format, final IValidationContext ctx) {
		final ArrayList<Pair<EObject, EStructuralFeature>> detailsDecoratorData = new ArrayList<Pair<EObject, EStructuralFeature>>() {
			{
				add(new Pair<EObject, EStructuralFeature>(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port()));
				add(new Pair<EObject, EStructuralFeature>(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_CargoCV()));
			}
		};
		failures.add(addDetailConstraintStatusDecorator("port", port.getName(), cargo.getLoadName(), dischargeSlot, loadSlot, isMin, loadCV, dischargeCV, detailsDecoratorData, format, ctx));
	}

	/*
	 * Creates a DetailConstraintStatusDecorator, with the appropriate error message
	 */
	private DetailConstraintStatusDecorator addDetailConstraintStatusDecorator(final String constraintName, final String constraintInstance, final String cargoName, final DischargeSlot dischargeSlot,
			final LoadSlot loadSlot, final boolean isMin, final double loadCV, final double dischargeCV, final ArrayList<Pair<EObject, EStructuralFeature>> objectsAndFeatures, final String format,
			final IValidationContext ctx) {
		String operator, bound = "";
		if (isMin) {
			operator = "less";
			bound = "minimum";
		} else {
			operator = "more";
			bound = "maximum";
		}
		final String failureMessage = String.format(format, cargoName, loadCV, operator, bound, dischargeCV, constraintName, constraintInstance);
		final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
		for (final Pair<EObject, EStructuralFeature> pair : objectsAndFeatures) {
			dsd.addEObjectAndFeature(pair.getFirst(), pair.getSecond());
		}
		return dsd;
	}

}
