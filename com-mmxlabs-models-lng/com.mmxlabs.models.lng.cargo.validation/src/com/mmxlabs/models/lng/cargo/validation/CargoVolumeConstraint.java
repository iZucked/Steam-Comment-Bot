/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A constraint which checks that the load and discharge quantities for a cargo are compatible, so min discharge volume < max load volume
 * 
 * @author Tom Hinton
 * 
 */
public class CargoVolumeConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(IValidationContext ctx, final IExtraValidationContext extraContext, List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof Cargo) {

			final Cargo cargo = (Cargo) object;

			int loadMinVolume = 0;
			int loadMaxVolume = 0;
			int dischargeMinVolume = 0;
			int dischargeMaxVolume = 0;
			int numberOfSlots = cargo.getSlots().size();
			VolumeUnits dischargeUnits = null;
			VolumeUnits loadUnits = null;
			double cv = 0;
			for (final Slot slot : cargo.getSlots()) {
				if (slot instanceof LoadSlot) {
					loadMinVolume += slot.getSlotOrContractMinQuantity();
					loadMaxVolume += slot.getSlotOrContractMaxQuantity();
					loadUnits = slot.getSlotOrContractVolumeLimitsUnit();
					cv = ((LoadSlot) slot).getSlotOrDelegatedCV();
				} else if (slot instanceof DischargeSlot) {
					dischargeMinVolume += slot.getSlotOrContractMinQuantity();
					dischargeMaxVolume += slot.getSlotOrContractMaxQuantity();
					if (dischargeUnits == null) {
						dischargeUnits = slot.getSlotOrContractVolumeLimitsUnit();
					}
					if (numberOfSlots > 2) {
						checkComplexDischargeLimitsSet(ctx, failures, cargo, slot);
						checkComplexDischargeLimitsIdentical(ctx, failures, cargo, slot);
						checkComplexDischargeLimitUnitsIdentical(ctx, failures, cargo, dischargeUnits, slot);
					}
				}
			}
			if (loadUnits != dischargeUnits && !(cv > 0)) {
				// no cv value, can't convert
				return Activator.PLUGIN_ID;
			}
			if (loadUnits != VolumeUnits.MMBTU) {
				loadMinVolume = (int) (loadMinVolume * cv);
				loadMaxVolume = (int) (loadMaxVolume * cv);
			}
			if (dischargeUnits != VolumeUnits.MMBTU) {
				dischargeMinVolume = (int) (dischargeMinVolume * cv);
				dischargeMaxVolume = (int) (dischargeMaxVolume * cv);
			}
			String unitsWarning = getUnitsWarningString(dischargeUnits, loadUnits);
			checkMinAndMaxVolumes(ctx, failures, cargo, loadMinVolume, loadMaxVolume, dischargeMinVolume, dischargeMaxVolume, unitsWarning);
			checkMinAndMaxVolumesAgainstVesselCapacity(ctx, failures, cargo, loadMinVolume, loadMaxVolume, dischargeMinVolume, dischargeMaxVolume, cv, unitsWarning);
		}

		return Activator.PLUGIN_ID;
	}

	private String getUnitsWarningString(VolumeUnits dischargeUnits, VolumeUnits loadUnits) {
		String unitsWarning = "";
		if (loadUnits != dischargeUnits) {
			unitsWarning = String.format(". Note that load units [%s] are different to discharge units [%s]", loadUnits, dischargeUnits);
		}
		return unitsWarning;
	}

	private void checkMinAndMaxVolumes(IValidationContext ctx, List<IStatus> failures, final Cargo cargo, int loadMinVolume, int loadMaxVolume, int dischargeMinVolume, int dischargeMaxVolume,
			String unitsWarning) {
		if (loadMaxVolume < dischargeMinVolume) {
			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Cargo|" + cargo.getLoadName()
					+ "] Max load volume less than min discharge" + unitsWarning + ")."), IStatus.WARNING);

			for (final Slot slot : cargo.getSlots()) {
				if (slot instanceof LoadSlot) {
					status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
				} else if (slot instanceof DischargeSlot) {
					status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());

				}
			}

			failures.add(status);
		}
		if (loadMinVolume > dischargeMaxVolume) {
			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Cargo|" + cargo.getLoadName()
					+ "] Min load volume greater than max discharge" + unitsWarning + ")."), IStatus.WARNING);
			for (final Slot slot : cargo.getSlots()) {
				if (slot instanceof LoadSlot) {
					status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
				} else if (slot instanceof DischargeSlot) {
					status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());

				}
			}

			failures.add(status);
		}
	}

	private int getVesselCapacity(Cargo cargo) {
		VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
		if (vesselAssignmentType == null) {
			if (cargo.getSortedSlots().size() > 0) {
				if (cargo.getSortedSlots().get(0) instanceof LoadSlot) {
					LoadSlot loadSlot = (LoadSlot) cargo.getSortedSlots().get(0);
					if (loadSlot.isDESPurchase()) {
						Vessel vessel = loadSlot.getNominatedVessel();
						if (vessel != null) {
							// get the capacity directly
							return vessel.getVesselOrVesselClassCapacity();
						}
					}
				}
			}
			return -1;
		}

		int capacity = -1;
		if (vesselAssignmentType instanceof VesselAvailability) {
			VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;

			Vessel vessel = vesselAvailability.getVessel();
			if (vessel != null) {
				capacity = vessel.getVesselOrVesselClassCapacity();
			}
		} else if (vesselAssignmentType instanceof CharterInMarket) {
			CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
			final VesselClass vesselClass = charterInMarket.getVesselClass();
			if (vesselClass != null) {
				capacity = vesselClass.getCapacity();
			}
		} else {
			// Can't do much here, no capacity...
			return -1;
		}
		return capacity;
	}

	private void checkMinAndMaxVolumesAgainstVesselCapacity(IValidationContext ctx, List<IStatus> failures, final Cargo cargo, int loadMinVolume, int loadMaxVolume, int dischargeMinVolume,
			int dischargeMaxVolume, double cv, String unitsWarning) {
		int vesselCapacity = getVesselCapacity(cargo);
		if (vesselCapacity > -1 && (cv > 0)) {
			// convert to MMBTU
			int convertedVesselCapacity = (int) (vesselCapacity * cv);
			if (loadMinVolume > convertedVesselCapacity) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Cargo|" + cargo.getLoadName()
						+ "] minimum load volume is greater than the capacity of current vessel assignment."), IStatus.WARNING);

				for (final Slot slot : cargo.getSlots()) {
					if (slot instanceof LoadSlot) {
						status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					}
				}
				failures.add(status);
			}
			if (dischargeMinVolume > convertedVesselCapacity) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Cargo|" + cargo.getLoadName()
						+ "] minimum discharge volume is greater than the capacity of current vessel assignment."), IStatus.WARNING);
				for (final Slot slot : cargo.getSlots()) {
					if (slot instanceof DischargeSlot) {
						status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					}
				}

				failures.add(status);
			}
		}
	}

	private void checkComplexDischargeLimitUnitsIdentical(IValidationContext ctx, List<IStatus> failures, final Cargo cargo, VolumeUnits dischargeUnits, final Slot slot) {
		if (slot.getSlotOrContractVolumeLimitsUnit() != dischargeUnits) {
			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Complex Cargo|" + cargo.getLoadName()
					+ " requires volume limit units to be identical"));
			status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit());
			failures.add(status);
		}
	}

	private void checkComplexDischargeLimitsIdentical(IValidationContext ctx, List<IStatus> failures, final Cargo cargo, final Slot slot) {
		// Check fields are the same
		if (slot.isSetMaxQuantity() && slot.isSetMinQuantity()) {
			if (slot.getMinQuantity() != slot.getMaxQuantity()) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Complex Cargo|" + cargo.getLoadName()
						+ " requires min and max discharge volumes to be specified and identical"));

				status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
				status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
				failures.add(status);
			}
		}
	}

	private void checkComplexDischargeLimitsSet(IValidationContext ctx, List<IStatus> failures, final Cargo cargo, final Slot slot) {
		// Check fields are set
		if (!slot.isSetMaxQuantity() || !slot.isSetMinQuantity()) {
			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Complex Cargo|" + cargo.getLoadName()
					+ " requires min and max discharge volumes to be specified and identical"));

			if (!slot.isSetMaxQuantity()) {
				status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
			} else if (!slot.isSetMinQuantity()) {
				status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
			}
			failures.add(status);
		}
	}
}
