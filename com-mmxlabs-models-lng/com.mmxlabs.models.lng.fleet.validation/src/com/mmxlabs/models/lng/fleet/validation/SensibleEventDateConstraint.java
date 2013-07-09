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
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class SensibleEventDateConstraint  extends AbstractModelMultiConstraint {

	private Date earliestDate = new GregorianCalendar(2000,0,1).getTime();
	private EStructuralFeature [] eventDateFields = { 
			FleetPackage.Literals.VESSEL_EVENT__START_AFTER,
			FleetPackage.Literals.VESSEL_EVENT__START_BY,
	};
	
	/**
	 * Impose sensible date cutoffs for vessel availabilities and events
	 */
	@Override
	public String validate(final IValidationContext ctx, List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) object;
			for (EStructuralFeature feature: eventDateFields) {
				final Date date = (Date) object.eGet(feature);
				if (date.before(earliestDate)) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(event.getName(), feature.getName(), earliestDate.toString()));
					status.addEObjectAndFeature(object, feature);
					failures.add(status);				
				}
			}

		}

		return Activator.PLUGIN_ID;
	}

}
