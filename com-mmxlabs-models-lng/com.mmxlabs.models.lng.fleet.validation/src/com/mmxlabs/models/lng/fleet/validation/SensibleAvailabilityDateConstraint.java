package com.mmxlabs.models.lng.fleet.validation;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class SensibleAvailabilityDateConstraint  extends AbstractModelMultiConstraint {

	private Date earliestDate = new GregorianCalendar(2000,0,1).getTime();
	private EStructuralFeature [] availabilityDateFields = { 
			FleetPackage.Literals.VESSEL_AVAILABILITY__END_AFTER,
			FleetPackage.Literals.VESSEL_AVAILABILITY__END_BY,
			FleetPackage.Literals.VESSEL_AVAILABILITY__START_AFTER,
			FleetPackage.Literals.VESSEL_AVAILABILITY__START_BY
	};
	
	/**
	 * Impose sensible date cutoffs for vessel availabilities and events
	 */
	@Override
	public String validate(final IValidationContext ctx, List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) object;
			for (EStructuralFeature feature: availabilityDateFields) {
				final Date date = (Date) object.eGet(feature);
				if (date.before(earliestDate)) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(vesselAvailability.getVessel().getName(), feature.getName(), earliestDate.toString()));
					status.addEObjectAndFeature(object, feature);
					failures.add(status);				
				}
			}

		}

		return Activator.PLUGIN_ID;
	}

}
