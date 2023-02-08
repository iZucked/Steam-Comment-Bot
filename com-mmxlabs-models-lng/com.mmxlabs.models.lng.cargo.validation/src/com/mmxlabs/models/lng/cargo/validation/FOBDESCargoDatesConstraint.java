/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class FOBDESCargoDatesConstraint extends AbstractModelMultiConstraint {

	public static final String KEY_INCOMPATIBLE_WINDOWS = "incompatible-windows";
	public static final String KEY_DUAL_COUNTERPART_WINDOWS = "dual-counterparty-windows";

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject object = ctx.getTarget();
		if (object instanceof final Cargo cargo) {
			if (cargo.getCargoType() == CargoType.FOB || cargo.getCargoType() == CargoType.DES) {

				LoadSlot loadSlot = null;
				DischargeSlot dischargeSlot = null;

				for (final Slot<?> slot : cargo.getSlots()) {

					if (slot instanceof final LoadSlot ls) {
						if (loadSlot != null) {
							// We don't support LLD here
							return;
						}
						loadSlot = ls;
					} else if (slot instanceof final DischargeSlot ds) {
						if (dischargeSlot != null) {
							// We don't support LDD here
							return;
						}
						dischargeSlot = ds;
					}
				}

				if (loadSlot != null && dischargeSlot != null) {
					// Check this is the correct type of DES Purchase and FOB Sale
					if (SlotClassifier.classify(loadSlot) == SlotType.DES_Buy || SlotClassifier.classify(dischargeSlot) == SlotType.FOB_Sale) {

						if (loadSlot.isWindowCounterParty() && dischargeSlot.isWindowCounterParty()) {

							final String message = String.format("[Cargo|%s] Cannot have counter-party windows on both sides of the cargo.", cargo.getLoadName());

							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
							status.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_WindowCounterParty());
							status.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowCounterParty());
							status.setConstraintKey(KEY_DUAL_COUNTERPART_WINDOWS);
							statuses.add(status);
						} else if (loadSlot.getWindowStart() != null && dischargeSlot.getWindowStart() != null) {
							if (!checkDates(getStartLDT(loadSlot), getEndLDT(loadSlot), !loadSlot.isDESPurchase() && loadSlot.isWindowCounterParty(), //
									getStartLDT(dischargeSlot), getEndLDT(dischargeSlot), !dischargeSlot.isFOBSale() && dischargeSlot.isWindowCounterParty())) {

								final String message = String.format("[Cargo|%s] Incompatible slot windows.", cargo.getLoadName());

								final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
								status.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStart());
								status.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStart());
								status.setConstraintKey(KEY_INCOMPATIBLE_WINDOWS);
								statuses.add(status);
							}
						}
					}
				}
			}
		}
	}

	private LocalDateTime getStartLDT(final Slot<?> s) {
		final LocalDate ld = s.getWindowStart();
		return ld.atTime(s.getWindowStartTime(), 0, 0);
	}

	private LocalDateTime getEndLDT(final Slot<?> s) {
		LocalDateTime ldt = getStartLDT(s);
		// Add on window duration
		{
			final int windowSize = (Integer) s.eGetWithDefault(CargoPackage.Literals.SLOT__WINDOW_SIZE);
			if (windowSize != 0) {
				final TimePeriod tp = (TimePeriod) s.eGetWithDefault(CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS);

				switch (tp) {
				case DAYS:
					ldt = ldt.plusDays(windowSize).minusHours(1);
					break;
				case HOURS:
					ldt = ldt.plusHours(windowSize);
					break;
				case MONTHS:
					ldt = ldt.plusMonths(windowSize).minusHours(1);
					break;
				default:
					break;
				}
			}
		}
		// Add on any flex
		{
			final int windowSize = s.getWindowFlex();
			if (windowSize != 0) {
				final TimePeriod tp = s.getWindowFlexUnits();

				switch (tp) {
				case DAYS:
					ldt = ldt.plusDays(windowSize).minusHours(1);
					break;
				case HOURS:
					ldt = ldt.plusHours(windowSize);
					break;
				case MONTHS:
					ldt = ldt.plusMonths(windowSize).minusHours(1);
					break;
				default:
					break;
				}
			}
		}
		return ldt;
	}

	private boolean checkDates(final LocalDateTime loadWindowStart, final LocalDateTime loadWindowEnd, final boolean loadIsCP, final LocalDateTime dischargeWindowStart,
			final LocalDateTime dischargeWindowEnd, final boolean dischargeIsCP) {

		if (loadIsCP && dischargeIsCP) {
			return false;
		}

		if (loadWindowStart == null || loadWindowEnd == null || dischargeWindowStart == null || dischargeWindowEnd == null) {
			return false;
		}

		if (loadWindowEnd.isBefore(dischargeWindowStart)) {
			return false;
		}

		if (loadWindowStart.isAfter(dischargeWindowEnd)) {
			return false;
		}

		// out by one error
		if (loadIsCP) {
			if (dischargeWindowStart.isAfter(loadWindowStart)) {
				return false;
			}
			if (dischargeWindowEnd.isBefore(loadWindowEnd)) {
				return false;
			}
		}
		if (dischargeIsCP) {
			if (loadWindowStart.isAfter(dischargeWindowStart)) {
				return false;
			}
			if (loadWindowEnd.isBefore(dischargeWindowEnd)) {
				return false;
			}
		}

		if ((loadWindowEnd.equals(dischargeWindowStart) && !(loadWindowStart.equals(loadWindowEnd)))) {
			// return false;
		}
		return true;
	}
}
