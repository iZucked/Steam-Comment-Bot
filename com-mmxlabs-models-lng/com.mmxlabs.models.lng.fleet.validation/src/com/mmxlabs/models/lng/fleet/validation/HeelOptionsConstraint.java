/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * A constraint which checks {@link HeelOptions}
 * 
 * @author Simon Goodall
 */
public class HeelOptionsConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(IValidationContext ctx) {

		final List<IStatus> failures = new LinkedList<IStatus>();
		final EObject object = ctx.getTarget();
		if (object instanceof HeelOptions) {
			HeelOptions heelOptions = (HeelOptions) object;

			if (heelOptions.isSetVolumeAvailable()) {

				if (heelOptions.getVolumeAvailable() < 1) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A volume should be specified"));
					dsd.addEObjectAndFeature(heelOptions, FleetPackage.eINSTANCE.getHeelOptions_VolumeAvailable());
					failures.add(dsd);
				}
				if (heelOptions.getCvValue() < 0.001) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A CV value should be specified"));
					dsd.addEObjectAndFeature(heelOptions, FleetPackage.eINSTANCE.getHeelOptions_CvValue());
					failures.add(dsd);
				}
				if (heelOptions.getCvValue() > 40.0) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("CV value is too high"));
					dsd.addEObjectAndFeature(heelOptions, FleetPackage.eINSTANCE.getHeelOptions_CvValue());
					failures.add(dsd);
				}
				if (heelOptions.getPricePerMMBTU() < 0.0001) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("No gas price specified -  must be non-zero"));
					deco.addEObjectAndFeature(heelOptions, FleetPackage.eINSTANCE.getHeelOptions_PricePerMMBTU());
					failures.add(deco);
				}
				if (heelOptions.getPricePerMMBTU() > 70.0) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Gas price is too high."));
					deco.addEObjectAndFeature(heelOptions, FleetPackage.eINSTANCE.getHeelOptions_PricePerMMBTU());
					failures.add(deco);
				}
			}
		}
		if (failures.isEmpty()) {
			return ctx.createSuccessStatus();
		} else if (failures.size() == 1) {
			return failures.get(0);
		} else {
			final MultiStatus multi = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, null, null);
			for (final IStatus s : failures) {
				multi.add(s);
			}
			return multi;
		}
	}
}
