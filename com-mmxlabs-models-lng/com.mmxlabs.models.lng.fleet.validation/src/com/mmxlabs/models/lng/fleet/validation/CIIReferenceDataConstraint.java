/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.CIIGradeBoundary;
import com.mmxlabs.models.lng.fleet.CIIReductionFactor;
import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelEmissionReference;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Checks the {@link CIIReferenceData} values
 * 
 * @author FM
 * 
 */
public class CIIReferenceDataConstraint extends AbstractModelMultiConstraint {
	
	private static final FleetPackage fp = FleetPackage.eINSTANCE;
	
	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraValidationContext, @NonNull List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		
		if (target instanceof final CIIReferenceData ciiReferenceData) {
			if (ciiReferenceData.getReductionFactors() == null || ciiReferenceData.getReductionFactors().isEmpty()) {
				statuses.add(DetailConstraintStatusFactory.makeStatus()
						.withMessage("CII reduction factors table is missing values")
						.withObjectAndFeature(ciiReferenceData, fp.getCIIReferenceData_ReductionFactors())
						.make(ctx));
			} else {
				for (final CIIReductionFactor rf : ciiReferenceData.getReductionFactors()) {
					if(rf.getPercentage() < 0 || rf.getPercentage() > 100) {
						statuses.add(DetailConstraintStatusFactory.makeStatus()
								.withFormattedMessage("CII reduction factor for the year %d must be between 0 and 100", rf.getYear())
								.withObjectAndFeature(rf, fp.getCIIReductionFactor_Percentage())
								.make(ctx));
					}
				}
			}
			if (ciiReferenceData.getFuelEmissions() == null || ciiReferenceData.getFuelEmissions().isEmpty()) {
				statuses.add(DetailConstraintStatusFactory.makeStatus()
						.withMessage("CII fuel reference data table is missing values")
						.withObjectAndFeature(ciiReferenceData, fp.getCIIReferenceData_FuelEmissions())
						.make(ctx));
			} else {
				int nameCounter = 0;
				final List<FuelEmissionReference> fers = new ArrayList<>();
				for (final FuelEmissionReference fer : ciiReferenceData.getFuelEmissions()) {
					final String name = fer.getName();
					if (name != null && !name.isBlank()) {
						if (name.toLowerCase().contains("lng")) {
							fers.add(fer);
							nameCounter++;
						}
						if (fer.getCf() <= 0.0 || fer.getCf() > 15.0) {
							statuses.add(DetailConstraintStatusFactory.makeStatus()
									.withFormattedMessage("Fuel reference %s must be between 0.0 and 15.0", name)
									.withObjectAndFeature(fer, fp.getFuelEmissionReference_Cf())
									.make(ctx));
						}
					}
				}
				if (nameCounter != 1) {
					if (nameCounter == 0) {
					statuses.add(DetailConstraintStatusFactory.makeStatus()
							.withMessage("CII fuel reference data table must have one entry with name containing \"LNG\"")
							.withObjectAndFeature(ciiReferenceData, fp.getCIIReferenceData_FuelEmissions())
							.make(ctx));
					}
					if (nameCounter > 1) {
						DetailConstraintStatusFactory.makeStatus()
								.withMessage("CII fuel reference data table must have one entry with name containing \"LNG\"")
								.withObjectAndFeatures(fers.stream()
										.map(e -> Pair.of(e, MMXCorePackage.eINSTANCE.getNamedObject_Name()))
												.toArray(Pair[]::new))
								.make(ctx, statuses);
					}
				}
			}
			if (ciiReferenceData.getCiiGradeBoundaries() == null || ciiReferenceData.getCiiGradeBoundaries().isEmpty()) {
				statuses.add(DetailConstraintStatusFactory.makeStatus()
						.withMessage("CII grade boundary table is missing values")
						.withObjectAndFeature(ciiReferenceData, fp.getCIIReferenceData_CiiGradeBoundaries())
						.make(ctx));
			} else {
				for (final CIIGradeBoundary gb : ciiReferenceData.getCiiGradeBoundaries()) {
					if (gb.getGradeAValue() <= 0.0 || gb.getGradeAValue()> 10.0) {
						statuses.add(DetailConstraintStatusFactory.makeStatus()
								.withMessage("CII grade boundary A must be between 0.0 and 10.0")
								.withObjectAndFeature(gb, fp.getCIIGradeBoundary_GradeAValue())
								.make(ctx));
					}
					if (gb.getGradeBValue() <= 0.0 || gb.getGradeBValue()> 10.0) {
						statuses.add(DetailConstraintStatusFactory.makeStatus()
								.withMessage("CII grade boundary B must be between 0.0 and 10.0")
								.withObjectAndFeature(gb, fp.getCIIGradeBoundary_GradeBValue())
								.make(ctx));
					}
					if (gb.getGradeCValue() <= 0.0 || gb.getGradeCValue()> 10.0) {
						statuses.add(DetailConstraintStatusFactory.makeStatus()
								.withMessage("CII grade boundary C must be between 0.0 and 10.0")
								.withObjectAndFeature(gb, fp.getCIIGradeBoundary_GradeCValue())
								.make(ctx));
					}
					if (gb.getGradeDValue() <= 0.0 || gb.getGradeDValue()> 10.0) {
						statuses.add(DetailConstraintStatusFactory.makeStatus()
								.withMessage("CII grade boundary D must be between 0.0 and 10.0")
								.withObjectAndFeature(gb, fp.getCIIGradeBoundary_GradeDValue())
								.make(ctx));
					}
				}
			}
		}

		
	}
}
