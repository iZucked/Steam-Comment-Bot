/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A constraint which checks that the load and discharge quantities for a cargo are compatible, so min discharge volume < max load volume
 * 
 * @author Tom Hinton
 * 
 */
public class CargoVolumeConstraint extends AbstractModelMultiConstraint {

	private static final String COMPLEX_CARGO = "Complex cargo";

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof Cargo) {

			final Cargo cargo = (Cargo) object;

			final DetailConstraintStatusFactory factoryBase = DetailConstraintStatusFactory.makeStatus()//
					.withTypedName("Cargo", cargo.getLoadName());

			int loadMinVolume = 0;
			int loadMaxVolume = 0;
			int dischargeMinVolume = 0;
			int dischargeMaxVolume = 0;
			final int numberOfSlots = cargo.getSlots().size();
			VolumeUnits dischargeUnits = null;
			VolumeUnits loadUnits = null;
			double cv = 0;

			// If false, we have hit an unbounded limit, so skip max checks.
			boolean maxLoadValid = true;
			boolean maxDischargeValid = true;
			LoadSlot loadSlot = null;
			for (final Slot<?> slot : cargo.getSlots()) {
				if (slot instanceof LoadSlot) {
					loadSlot = (LoadSlot) slot;

					loadMinVolume += slot.getSlotOrDelegateMinQuantity();
					loadMaxVolume += slot.getSlotOrDelegateMaxQuantity();
					loadUnits = slot.getSlotOrDelegateVolumeLimitsUnit();
					cv = ((LoadSlot) slot).getSlotOrDelegateCV();

					if (slot.getSlotOrDelegateMaxQuantity() == Integer.MAX_VALUE) {
						maxLoadValid = false;
					} else if (slot.getSlotOrDelegateMaxQuantity() == 0) {
						maxLoadValid = false;
					}
				} else if (slot instanceof DischargeSlot) {
					dischargeMinVolume += slot.getSlotOrDelegateMinQuantity();
					dischargeMaxVolume += slot.getSlotOrDelegateMaxQuantity();
					if (dischargeUnits == null) {
						dischargeUnits = slot.getSlotOrDelegateVolumeLimitsUnit();
					}
					if (numberOfSlots > 2) {
						checkComplexDischargeLimitsSet(factoryBase, ctx, failures, cargo, slot);
						checkComplexDischargeLimitsIdentical(factoryBase, ctx, failures, cargo, slot);
						checkComplexDischargeLimitUnitsIdentical(factoryBase, ctx, failures, cargo, dischargeUnits, slot);
					}
					if (slot.getSlotOrDelegateMaxQuantity() == Integer.MAX_VALUE) {
						maxDischargeValid = false;
					} else if (slot.getSlotOrDelegateMaxQuantity() == 0) {
						maxDischargeValid = false;
					}
				}
			}
			if (loadUnits != dischargeUnits && (cv <= 0)) {
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
			final String unitsWarning = getUnitsWarningString(dischargeUnits, loadUnits);
			checkMinAndMaxVolumes(factoryBase, ctx, failures, cargo, loadSlot, loadMinVolume, loadMaxVolume, dischargeMinVolume, dischargeMaxVolume, unitsWarning, maxLoadValid, maxDischargeValid);
			checkMinAndMaxVolumesAgainstVesselCapacity(factoryBase, ctx, failures, cargo, loadSlot, loadMinVolume, loadMaxVolume, dischargeMinVolume, dischargeMaxVolume, cv, unitsWarning,
					maxLoadValid);
		}

		return Activator.PLUGIN_ID;
	}

	private String getUnitsWarningString(final VolumeUnits dischargeUnits, final VolumeUnits loadUnits) {
		String unitsWarning = "";
		if (loadUnits != dischargeUnits) {
			unitsWarning = String.format(". Note that load units [%s] are different to discharge units [%s]", loadUnits, dischargeUnits);
		}
		return unitsWarning;
	}

	private void checkMinAndMaxVolumes(final DetailConstraintStatusFactory factoryBase, final IValidationContext ctx, final List<IStatus> failures, final Cargo cargo, LoadSlot loadSlot,
			final int loadMinVolume, final int loadMaxVolume, final int dischargeMinVolume, final int dischargeMaxVolume, final String unitsWarning, final boolean maxLoadValid,
			final boolean maxDischargeValid) {

		if (loadSlot != null && loadSlot.isDESPurchase() && loadSlot.isVolumeCounterParty()) {
			// Special counterparty volume only code path.
			if (maxLoadValid && maxDischargeValid) {
				boolean minMismatch = loadMinVolume < dischargeMinVolume;
				boolean maxMismatch = loadMaxVolume > dischargeMaxVolume;

				if (maxMismatch || minMismatch) {

					final DetailConstraintStatusFactory factory = factoryBase.copyName() //
							.withTag(ValidationConstants.TAG_VOLUME_MISMATCH) //
							.withSeverity(IStatus.ERROR);

					if (minMismatch && maxMismatch) {
						factory.withFormattedMessage("Counterparty volume mismatch");
					} else if (maxMismatch) {
						factory.withFormattedMessage("Counterparty max volume mismatch");
					} else {
						assert minMismatch;
						factory.withFormattedMessage("Counterparty min volume mismatch");
					}

					for (final Slot<?> slot : cargo.getSlots()) {
						if (maxMismatch) {
							factory.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
						}
						if (minMismatch) {
							factory.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
						}
					}
					factory.make(ctx, failures);

				}

				return;
			}
		}

		if (maxLoadValid && loadMaxVolume < dischargeMinVolume) {
			final DetailConstraintStatusFactory factory = factoryBase.copyName() //
					.withFormattedMessage("Max load volume less than min discharge %s", unitsWarning) //
					.withSeverity(IStatus.WARNING);

			for (final Slot<?> slot : cargo.getSlots()) {
				if (slot instanceof LoadSlot) {
					factory.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
				} else if (slot instanceof DischargeSlot) {
					factory.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());

				}
			}
			factory.make(ctx, failures);
		}
		if (maxDischargeValid && loadMinVolume > dischargeMaxVolume) {
			// Always consider non-shipped, or shipped if we have not enabled heel roll over
			// TODO: Make this more customisable for client rules
			if (cargo.getCargoType() != CargoType.FLEET //
					|| !LicenseFeatures.isPermitted("features:heelrollover")) {

				final DetailConstraintStatusFactory factory = factoryBase //
						.copyName() //
						.withTag(ValidationConstants.TAG_VOLUME_MISMATCH) //
						.withFormattedMessage("Min load volume greater than max discharge %s", unitsWarning) //
						.withSeverity(IStatus.WARNING);

				for (final Slot<?> slot : cargo.getSlots()) {
					if (slot instanceof LoadSlot) {
						factory.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					} else if (slot instanceof DischargeSlot) {
						factory.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
					}
				}
				factory.make(ctx, failures);
			}
		}
	}

	private int getVesselCapacity(final Cargo cargo) {
		final VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
		if (vesselAssignmentType == null) {
			if (!cargo.getSortedSlots().isEmpty()) {
				if (cargo.getSortedSlots().get(0) instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) cargo.getSortedSlots().get(0);
					if (loadSlot.isDESPurchase()) {
						final Vessel vessel = loadSlot.getNominatedVessel();
						if (vessel != null) {
							// get the capacity directly
							return (int) (vessel.getVesselOrDelegateCapacity() * vessel.getVesselOrDelegateFillCapacity());
						}
					}
				}
			}
			return -1;
		}

		int capacity = -1;
		double fillCapacity = 0.985;
		if (vesselAssignmentType instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;

			final Vessel vessel = vesselAvailability.getVessel();
			if (vessel != null) {
				capacity = vessel.getVesselOrDelegateCapacity();
				fillCapacity = vessel.getVesselOrDelegateFillCapacity();
			}
		} else if (vesselAssignmentType instanceof CharterInMarket) {
			final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
			final Vessel vessel = charterInMarket.getVessel();
			if (vessel != null) {
				capacity = vessel.getVesselOrDelegateCapacity();
				fillCapacity = vessel.getVesselOrDelegateFillCapacity();
			}
		} else if (vesselAssignmentType instanceof CharterInMarketOverride) {
			final CharterInMarketOverride charterInMarketOverride = (CharterInMarketOverride) vesselAssignmentType;
			final CharterInMarket charterInMarket = charterInMarketOverride.getCharterInMarket();
			final Vessel vessel = charterInMarket.getVessel();
			if (vessel != null) {
				capacity = vessel.getVesselOrDelegateCapacity();
				fillCapacity = vessel.getVesselOrDelegateFillCapacity();
			}
		} else {
			// Can't do much here, no capacity...
			return -1;
		}
		return (int) (capacity * fillCapacity);
	}

	private void checkMinAndMaxVolumesAgainstVesselCapacity(DetailConstraintStatusFactory factoryBase, final IValidationContext ctx, final List<IStatus> failures, final Cargo cargo, LoadSlot loadSlot,
			final int loadMinVolume, final int loadMaxVolume, final int dischargeMinVolume, final int dischargeMaxVolume, final double cv, final String unitsWarning, final boolean maxLoadValid) {
		final int vesselCapacity = getVesselCapacity(cargo);
		if (vesselCapacity > -1 && (cv > 0)) {
			// convert to MMBTU
			final int convertedVesselCapacity = (int) (vesselCapacity * cv);
			if (loadMinVolume > convertedVesselCapacity) {

				final DetailConstraintStatusFactory factory = factoryBase//
						.copyName() //
						.withMessage("Minimum load volume is greater than the capacity of current vessel assignment") //
						.withSeverity(IStatus.WARNING);

				for (final Slot<?> slot : cargo.getSlots()) {
					if (slot instanceof LoadSlot) {
						factory.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					}
				}
				factory.make(ctx, failures);
			}
			if (dischargeMinVolume > convertedVesselCapacity) {

				final DetailConstraintStatusFactory factory = factoryBase//
						.copyName() //
						.withMessage("Minimum discharge volume is greater than the capacity of current vessel assignment") //
						.withSeverity(IStatus.WARNING);

				for (final Slot<?> slot : cargo.getSlots()) {
					if (slot instanceof DischargeSlot) {
						factory.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					}
				}
				factory.make(ctx, failures);
			}
			if (loadSlot != null && loadSlot.isDESPurchase() && loadSlot.isVolumeCounterParty()) {
				// Special counterparty volume only code path.
				if (maxLoadValid) {
					boolean maxMismatch = loadMaxVolume > convertedVesselCapacity;

					if (maxMismatch) {

						final DetailConstraintStatusFactory factory = factoryBase.copyName() //
								.withTag(ValidationConstants.TAG_VOLUME_MISMATCH) //
								.withSeverity(IStatus.ERROR);

						factory.withFormattedMessage("Counterparty volume is greater than the capacity of current vessel assignment");
						factory.withObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
						factory.make(ctx, failures);
					}
				}
			}
		}
	}

	private void checkComplexDischargeLimitUnitsIdentical(final DetailConstraintStatusFactory factoryBase, final IValidationContext ctx, final List<IStatus> failures, final Cargo cargo,
			final VolumeUnits dischargeUnits, final Slot slot) {
		if (slot.getSlotOrDelegateVolumeLimitsUnit() != dischargeUnits) {
			factoryBase.copyName() //
					.withTypedName(COMPLEX_CARGO, cargo.getLoadName()) //
					.withMessage("requires volume limit units to be identical") //
					.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit()) //
					.make(ctx, failures);
		}
	}

	private void checkComplexDischargeLimitsIdentical(final DetailConstraintStatusFactory factoryBase, final IValidationContext ctx, final List<IStatus> failures, final Cargo cargo, final Slot slot) {
		// Check fields are the same
		if (slot.isSetMaxQuantity() && slot.isSetMinQuantity()) {
			if (slot.getMinQuantity() != slot.getMaxQuantity()) {
				factoryBase.copyName() //
						.withTypedName(COMPLEX_CARGO, cargo.getLoadName()) //
						.withMessage("requires min and max discharge volumes to be specified and identical") //
						.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity()) //
						.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity()) //
						.make(ctx, failures);
			}
		}
	}

	private void checkComplexDischargeLimitsSet(final DetailConstraintStatusFactory factoryBase, final IValidationContext ctx, final List<IStatus> failures, final Cargo cargo, final Slot slot) {
		// Check fields are set
		if (!slot.isSetMaxQuantity() || !slot.isSetMinQuantity()) {

			final DetailConstraintStatusFactory factory = factoryBase.copyName() //
					.withTypedName(COMPLEX_CARGO, cargo.getLoadName()) //
					.withMessage("requires min and max discharge volumes to be specified and identical");

			if (!slot.isSetMaxQuantity()) {
				factory.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
			} else if (!slot.isSetMinQuantity()) {
				factory.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
			}

			factory.make(ctx, failures);
		}
	}
}
