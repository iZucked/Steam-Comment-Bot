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
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Validation constraint to check vessel capacity exceeds minimum amount of LNG required for slot.
 */
public class SlotPermissiveVesselListVolumeConstraint extends AbstractModelMultiConstraint {

	/** Extreme CV values min=20 to max=25, that can use if no CV set. */
	private static final double minDefaultCV = 20.0;
	private static final double maxDefaultCV = 25.0;
	
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof Slot) {
			final Slot<?> slot = (Slot<?>) target;
			double cv = 0.0;
			
			if (slot instanceof LoadSlot) {
				cv = ((LoadSlot)slot).getSlotOrDelegateCV();
			}
			
			//Check permitted vessels list.
			if (slot.isRestrictedVesselsArePermissive()) {
				EList<AVesselSet<Vessel>> permittedVessels = slot.getRestrictedVessels();
				
				for (final Vessel vessel : SetUtils.getObjects(permittedVessels)) {
					
					//Capacity is in m^3
					double capacity = vessel.getVesselOrDelegateFillCapacity() * vessel.getCapacity();
					
					double minQuantity = slot.getSlotOrDelegateMinQuantity();
					
					if (slot.getVolumeLimitsUnit() != VolumeUnits.MMBTU) {
						minQuantity = this.convertToMMBTU(minQuantity, (cv > 0 ? cv : maxDefaultCV));
					}
					
					capacity = this.convertToMMBTU(capacity, (cv > 0 ? cv : minDefaultCV));
					
					if (capacity < minQuantity) {
						IStatus status = createInvalidPermittedVesselStatus(ctx, vessel, slot, minQuantity, capacity);
						statuses.add(status);
					}
				}
			}
		}
	
		return Activator.PLUGIN_ID;
	}

	private double convertToMMBTU(double volumeInM3, double cv) {
		return volumeInM3 * cv;
	}
	
	@NonNull
	private DetailConstraintStatusDecorator createInvalidPermittedVesselStatus(final IValidationContext ctx, final Vessel vessel, final Slot slot, double minRequiredVolume, double fillCapacity) {
		final String slotType = (slot instanceof LoadSlot) ? "load" : "discharge";
		final String message = String.format("Permitted vessel %s can not service %s slot %s as vessel capacity (%d mmbtu) is less than min volume for slot (%d mmbtu).", 
				vessel.getName(), slotType, slot.getName(), Math.round(fillCapacity), Math.round(minRequiredVolume));
		final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
		dcsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
		return dcsd;
	}
}
