/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A version of the {@link NameUniquenessConstraint} for Slots. We do not want
 * duplicate ids, but we can allow duplicates for spot slots.
 * 
 */
public class VesselCharterUniquenessConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof VesselCharter vesselCharter) {
			Vessel vessel = vesselCharter.getVessel();
			if (vessel == null) {
				return;
			}

			final EObject container = extraContext.getContainer(target);
			if (container instanceof CargoModel cargoModel) {

				Map<Vessel, Set<Integer>> existingOptions = (Map<Vessel, Set<Integer>>) ctx.getCurrentConstraintData();
				if (existingOptions == null) {
					existingOptions = new HashMap<>();
					ctx.putCurrentConstraintData(existingOptions);
				}

				// STILL A PROBLEM
				// 1). We only generate only slot type!
				// 2). Not easy to do all the tyes as

				// for (LoadSlot )

				{
					Set<Integer> bad = existingOptions.get(vessel);
					if (bad == null) {
						bad = new HashSet<>();
						existingOptions.put(vessel, bad);
						final List<EObject> objects = extraContext.getSiblings(target);

						final Set<Integer> temp = new HashSet<>();
						for (final EObject no : objects) {
							if (no instanceof VesselCharter va2) {
								if (va2.getVessel() == vessel) {
									final Integer n = va2.getCharterNumber();
									if (temp.contains(n)) {
										bad.add(n);
									}
									temp.add(n);
								}
							}
						}
					}

					final Integer charterNumber = vesselCharter.getCharterNumber();
					if (bad.contains(charterNumber)) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(target.eClass().getName() + " has non-unique charter number " + charterNumber));
						dsd.addEObjectAndFeature(target, CargoPackage.Literals.VESSEL_CHARTER__CHARTER_NUMBER);
						statuses.add(dsd);
					}
				}
			}
		}
	}

}
