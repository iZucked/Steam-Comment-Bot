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
					
					//Capacity is always in m^3
					double capacityInM3 = vessel.getCapacity();
					double fillCapacity = (vessel.getFillCapacity() > 0.0 ? vessel.getFillCapacity() : 1.0);
					double minQuantity = slot.getSlotOrDelegateMinQuantity();

					//Convert minQuantity to m^3 if neccessary.
					if (slot.getVolumeLimitsUnit() == VolumeUnits.MMBTU) {
						minQuantity = this.convertToM3(minQuantity, (cv > 0.0 ? cv : maxDefaultCV));
					}
					
					int minCapacityReqInM3 = (int)Math.floor(minQuantity / fillCapacity);
										
					if (capacityInM3 < minCapacityReqInM3) {
						IStatus status = createInvalidPermittedVesselStatus(ctx, vessel, slot, minCapacityReqInM3);
						statuses.add(status);
					}
				}
			}
		}
	
		return Activator.PLUGIN_ID;
	}

	private double convertToM3(double volumeInMMBTU, double cv) {
		if (cv > 0.0) {
			return volumeInMMBTU / cv;
		}
		else {
			//Shouldn't happen, but just to be safe.
			return 0.0;
		}
	}
	
	private double convertToMMBTU(double volumeInM3, double cv) {
		return volumeInM3 * cv;
	}
	
	@NonNull
	private DetailConstraintStatusDecorator createInvalidPermittedVesselStatus(final IValidationContext ctx, final Vessel vessel, final Slot slot, int minRequiredVolumeM3) {
		final String slotType = (slot instanceof LoadSlot) ? "load" : "discharge";
		final String message = String.format("Permitted vessel %s can not service %s slot %s as vessel capacity %d m3 is less than required min volume for slot of %d m3", 
				vessel.getName(), slotType, slot.getName(), vessel.getCapacity(), minRequiredVolumeM3);
		final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
		dcsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
		return dcsd;
	}
}
