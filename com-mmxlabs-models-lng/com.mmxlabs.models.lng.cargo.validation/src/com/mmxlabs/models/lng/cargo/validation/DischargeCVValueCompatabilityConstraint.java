/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
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
								final double cv = loadSlot.getSlotOrDelegatedCV();
								final String format = "[Cargo|%s] Purchase CV %.2f is %s than the %s CV (%.2f) for %s '%s'.";
								if (dischargeSlot.isSetMinCvValue() || (salesContract != null && salesContract.isSetMinCvValue())) {
									final Double minCvValue = dischargeSlot.getSlotOrContractMinCv();
									if (minCvValue != null && cv < minCvValue) {
										DetailConstraintStatusDecorator dsd;
										String constraintName, constraintInstance;
										final ArrayList<Pair<EObject, EStructuralFeature>> detailsDecoratorData = new ArrayList<Pair<EObject,EStructuralFeature>>();
										if (dischargeSlot.isSetMinCvValue()) {
											detailsDecoratorData.add(new Pair<EObject, EStructuralFeature>(dischargeSlot, CargoPackage.eINSTANCE.getDischargeSlot_MinCvValue()));
											constraintName = "discharge slot";
											constraintInstance = dischargeSlot.getName();
										} else { // sales contract
											constraintName = "sales constract";
											constraintInstance = contract.getName();
											detailsDecoratorData.add(new Pair<EObject, EStructuralFeature>(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract()));
										}
										dsd = addDetailConstraintStatusDecorator(constraintName, constraintInstance, cargo.getName(), dischargeSlot, loadSlot, true, cv, (double) minCvValue, detailsDecoratorData, format, ctx);
										failures.add(dsd);
									}
								}
								
								if (dischargeSlot.isSetMaxCvValue() || (salesContract != null && salesContract.isSetMaxCvValue())) {
									final Double maxCvValue = dischargeSlot.getSlotOrContractMaxCv();
									if (maxCvValue != null && cv > maxCvValue) {
										DetailConstraintStatusDecorator dsd;
										String constraintName, constraintInstance;
										final ArrayList<Pair<EObject, EStructuralFeature>> detailsDecoratorData = new ArrayList<Pair<EObject,EStructuralFeature>>();
										if (dischargeSlot.isSetMaxCvValue() ) {
											detailsDecoratorData.add(new Pair<EObject, EStructuralFeature>(dischargeSlot, CargoPackage.eINSTANCE.getDischargeSlot_MaxCvValue()));
											constraintName = "discharge slot";
											constraintInstance = dischargeSlot.getName();
										} else { // sales contract
											constraintName = "sales constract";
											constraintInstance = contract.getName();
											detailsDecoratorData.add(new Pair<EObject, EStructuralFeature>(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Contract()));
										}
										dsd = addDetailConstraintStatusDecorator(constraintName, constraintInstance, cargo.getName(), dischargeSlot, loadSlot, false, cv, (double) maxCvValue, detailsDecoratorData, format, ctx);
										failures.add(dsd);
									}
								}
								checkPortBoundsMin(port, failures, dischargeSlot, loadSlot, cargo, cv, format, ctx);
								checkPortBoundsMax(port, failures, dischargeSlot, loadSlot, cargo, cv, format, ctx);
							}
						}
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	/*
	 * Check port cv bounds are met
	 */
	private void checkPortBoundsMin(final Port port, final List<IStatus> failures, final DischargeSlot dischargeSlot, final LoadSlot loadSlot, final Cargo cargo, final double cv, final String format, final IValidationContext ctx) {
		if (port.isSetMinCvValue()) {
			final Double minCvValue = port.getMinCvValue();
			if (minCvValue != null && cv < minCvValue) {
				final ArrayList<Pair<EObject, EStructuralFeature>> detailsDecoratorData = new ArrayList<Pair<EObject,EStructuralFeature>>(){
					{
						add(new Pair<EObject, EStructuralFeature>(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port()));
						add(new Pair<EObject, EStructuralFeature>(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_CargoCV()));

					}
				};
				
				failures.add(addDetailConstraintStatusDecorator("port",port.getName(), cargo.getName(), dischargeSlot, loadSlot, true, cv, minCvValue, detailsDecoratorData, format, ctx));
			}
		}
	}

	private void checkPortBoundsMax(final Port port, final List<IStatus> failures, final DischargeSlot dischargeSlot, final LoadSlot loadSlot, final Cargo cargo, final double cv, final String format, final IValidationContext ctx) {
		if (port.isSetMaxCvValue()) {
			final Double maxCvValue = port.getMaxCvValue();
			if (maxCvValue != null && cv > maxCvValue) {
				failures.add(addPortDetailConstraintStatusDecorator(port.getName(), cargo.getName(), dischargeSlot, loadSlot, false, cv, maxCvValue, format, ctx));
			}
		}
	}

	private DetailConstraintStatusDecorator addPortDetailConstraintStatusDecorator(final String portName, final String cargoName, final DischargeSlot dischargeSlot, final LoadSlot loadSlot, final boolean isMin, final double loadCV,
			final double portCV, final String format, final IValidationContext ctx) {
		String operator, bound = "";
		if (isMin) {
			operator = "less";
			bound = "minimum";
		} else {
			operator = "more";
			bound = "maximum";
		}
		final String failureMessage = String.format(format, cargoName, loadCV, operator, bound, portCV, "port", portName);
		final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
		dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port());
		dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
		return dsd;
	}

	private DetailConstraintStatusDecorator addDetailConstraintStatusDecorator(final String constraintName, final String constraintInstance, final String cargoName, final DischargeSlot dischargeSlot, final LoadSlot loadSlot, final boolean isMin, final double loadCV,
			final double dischargeCV, final ArrayList<Pair<EObject, EStructuralFeature>> objectsAndFeatures, final String format, final IValidationContext ctx) {
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
		for (final Pair<EObject, EStructuralFeature> pair:objectsAndFeatures){
			dsd.addEObjectAndFeature(pair.getFirst(), pair.getSecond());
		}
		return dsd;
	}

}
