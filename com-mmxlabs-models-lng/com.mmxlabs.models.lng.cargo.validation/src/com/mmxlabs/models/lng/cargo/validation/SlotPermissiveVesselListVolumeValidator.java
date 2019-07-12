package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SlotPermissiveVesselListVolumeValidator extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof Slot) {
			final Slot<?> slot = (Slot<?>) target;
			
			//Check permitted vessels list.
			if (slot.isRestrictedVesselsArePermissive()) {
				EList<AVesselSet<Vessel>> permittedVessels = slot.getRestrictedVessels();
				
				for (final Vessel vessel : SetUtils.getObjects(permittedVessels)) {
					
					//TODO: make sure vessel capacity units same as volume units, so can compare, using cargoCV.
					//[See CargoVolumeConstraint for how to do].
					//if (vessel.getCapacity() < slot.getMinQuantity()) {
					//	
					//}
				}
			}
		}
	
		return Activator.PLUGIN_ID;
	}
	
	@NonNull
	private DetailConstraintStatusDecorator createInvalidPermittedVesselStatus(final IValidationContext ctx, final Vessel vessel, final Slot slot, int volume, int capacity) {
		final String slotType = (slot instanceof LoadSlot) ? "load" : "discharge";
		final String message = String.format("Permitted vessel %s can not service % slot %s as vessel capacity (%d) is less than min volume (%d).", 
				vessel.getName(), slotType, slot.getName(), capacity, volume);
		final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
		dcsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
		return dcsd;
	}
}
