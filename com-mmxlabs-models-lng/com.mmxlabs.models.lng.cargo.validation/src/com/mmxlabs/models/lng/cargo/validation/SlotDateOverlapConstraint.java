/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import static com.mmxlabs.models.lng.cargo.util.SlotClassifier.classify;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SlotDateOverlapConstraint extends AbstractModelMultiConstraint {

	private static class PortSlotCounter {

		private final Map<Port, Map<@NonNull String, @NonNull Collection<@NonNull Slot>>> countingMap = new HashMap<>();

		private final DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

		/**
		 * Returns a modifiable {@link Collection} of {@link Slot} objects which overlap the given {@link Slot}.
		 * 
		 * @param slot
		 * @return
		 */
		public Collection<Slot> slotOverlaps(final Slot slot) {

			final ZonedDateTime windowStart = slot.getWindowStartWithSlotOrPortTime();
			if (windowStart == null) {
				return Collections.emptySet();
			}
			@NonNull
			ZonedDateTime cal = windowStart;
			int windowSize = slot.getWindowSize();

			final Set<@NonNull Slot> overlappingSlots = new LinkedHashSet<>();
			final SlotType slotType = classify(slot);
			do {
				final String dateKey = dateToString(cal);
				final Collection<@NonNull Slot> potentialOverlaps = getOverlappingSlots(slot, dateKey);
				final Iterator<@NonNull Slot> ii = potentialOverlaps.iterator();
				while (ii.hasNext()) {
					final Slot overlapSlot = ii.next();
					final SlotType overlapSlotType = classify(overlapSlot);
					// The combinations below are OK, let them pass
					if (overlapSlotType == SlotType.DES_Buy && slotType == SlotType.DES_Sale || (overlapSlotType == SlotType.DES_Sale && slotType == SlotType.DES_Buy)
							|| (overlapSlotType == SlotType.FOB_Sale && slotType == SlotType.FOB_Buy) || (overlapSlotType == SlotType.FOB_Buy && slotType == SlotType.FOB_Sale)) {
						ii.remove();
						continue;
					}

					final ZonedDateTime slotStart = slot.getWindowStartWithSlotOrPortTimeWithFlex();
					final ZonedDateTime overlapSlotStart = overlapSlot.getWindowStartWithSlotOrPortTimeWithFlex();
					final int slotDur = slot.getDuration();
					final int overlapSlotDur = overlapSlot.getDuration();
					final ZonedDateTime olEnd = overlapSlot.getWindowEndWithSlotOrPortTimeWithFlex();
					final ZonedDateTime slotEnd = slot.getWindowEndWithSlotOrPortTimeWithFlex();

					// if slot start + duration is before the end of the overlapSlot window, it can be OK so let them pass
					final ZonedDateTime slotFinish = slotStart.plusHours(slotDur);
					if (slotFinish.isBefore(olEnd)) {
						ii.remove();
						continue;
					}

					final ZonedDateTime overlapSlotFinish = overlapSlotStart.plusHours(overlapSlotDur);
					if (overlapSlotFinish.isBefore(slotEnd)) {
						ii.remove();
						continue;
					}
				}
				// final boolean overlaps = slots.contains(slot) ? slots.size() > 1 : slots.size() > 0;
				// if (overlaps) {
				overlappingSlots.addAll(potentialOverlaps);
				// overlappingSlots.remove(slot);
				// }
				windowSize -= 24;
				cal = cal.plusMonths(1);
			} while (windowSize > 0);

			return overlappingSlots;
		}

		public void addSlot(final Slot slot) {

			// if (slot instanceof LoadSlot) {
			// final LoadSlot load = (LoadSlot) slot;
			// if (load.isDESPurchase()) {
			// if (!slot.getPort().getCapabilities().contains(PortCapability.DISCHARGE)){
			// return;
			// }
			// }
			// } else if (slot instanceof DischargeSlot) {
			// final DischargeSlot disch = (DischargeSlot) slot;
			// if (disch.isFOBSale()) {
			// return;
			// }
			// }

			if (slot instanceof SpotSlot) {
				return;
			}

			final ZonedDateTime windowStart = slot.getWindowStartWithSlotOrPortTimeWithFlex();
			if (windowStart == null) {
				return;
			}
			@NonNull
			ZonedDateTime cal = windowStart;
			int windowPlusDurationSize = slot.getWindowSize();// + slot.getDuration();
			do {
				final String dateKey = dateToString(cal);
				final Collection<Slot> slots = getOverlappingSlots(slot, dateKey);
				slots.add(slot);
				// if(Calendar.get(Calendar.HOUR_OF_DAY, slot.getWindowStartWithSlotOrPortTime()) == 0)
				windowPlusDurationSize -= 24;
				cal = cal.plusMonths(1);
			} while (windowPlusDurationSize > 0);
		}

		private @NonNull Collection<@NonNull Slot> getOverlappingSlots(final @NonNull Slot slot, final @NonNull String dateKey) {

			final Port port = slot.getPort();
			Map<@NonNull String, @NonNull Collection<@NonNull Slot>> map;
			if (countingMap.containsKey(port)) {
				map = countingMap.get(port);
			} else {
				map = new HashMap<>();
				countingMap.put(port, map);
			}
			@NonNull
			Collection<@NonNull Slot> col;
			if (map.containsKey(dateKey)) {
				col = map.get(dateKey);
			} else {
				col = new LinkedList<>();
				map.put(dateKey, col);
			}
			return col;

		}

		private @NonNull String dateToString(final @NonNull ZonedDateTime date) {
			return date.toLocalDate().format(df);
		}
	}

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		if (LicenseFeatures.isPermitted("features:disable-slot-overlap-checks")) {
			return Activator.PLUGIN_ID;
		}

		final EObject object = ctx.getTarget();
		final EObject original = extraContext.getOriginal(object);
		final EObject replacement = extraContext.getReplacement(object);

		if (object instanceof Slot) {

			final Slot slot = (Slot) object;
			if (slot instanceof LoadSlot) {
				final LoadSlot load = (LoadSlot) slot;
				// if (load.isDESPurchase()) {
				// return ctx.createSuccessStatus();
				// }
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot disch = (DischargeSlot) slot;
				// if (disch.isFOBSale()) {
				// return ctx.createSuccessStatus();
				// }
			}
			if (slot instanceof SpotSlot) {
				return Activator.PLUGIN_ID;
			}

			final Object currentConstraintData = ctx.getCurrentConstraintData();
			PortSlotCounter psc;
			if (currentConstraintData == null || !(currentConstraintData instanceof PortSlotCounter)) {
				psc = buildConstraintData(ctx, extraContext);
				ctx.putCurrentConstraintData(psc);
			} else {
				psc = (PortSlotCounter) currentConstraintData;
			}

			final Collection<Slot> slotOverlaps = psc.slotOverlaps(slot);
			// Remove "this" slot to avoid conflicts
			slotOverlaps.remove(original);
			slotOverlaps.remove(slot);
			slotOverlaps.remove(replacement);
			if (slot instanceof LoadSlot) {
				final DischargeSlot transferSlot = ((LoadSlot) slot).getTransferFrom();
				slotOverlaps.remove(extraContext.getReplacement(transferSlot));
				slotOverlaps.remove(extraContext.getOriginal(transferSlot));
				slotOverlaps.remove(transferSlot);
			} else if (slot instanceof DischargeSlot) {
				final LoadSlot transferSlot = ((DischargeSlot) slot).getTransferTo();
				slotOverlaps.remove(extraContext.getReplacement(transferSlot));
				slotOverlaps.remove(extraContext.getOriginal(transferSlot));
				slotOverlaps.remove(transferSlot);
			}

			assert slotOverlaps.contains(slot) == false;
			if (slotOverlaps.isEmpty()) {
				return Activator.PLUGIN_ID;
			}

			boolean first = true;
			final StringBuilder sb = new StringBuilder();
			for (final Slot s : slotOverlaps) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(s.getName());
			}
			final String slotStr = sb.toString();

			final String message;
			message = String.format("[Slot|'%s'] Overlaps with slot(s) '%s'", slot.getName() == null ? "(no ID)" : slot.getName(), slotStr);

			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
			dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
			statuses.add(dsd);
		}

		return Activator.PLUGIN_ID;
	}

	private PortSlotCounter buildConstraintData(final IValidationContext ctx, final IExtraValidationContext extraContext) {

		final PortSlotCounter psc = new PortSlotCounter();

		final MMXRootObject rootObject = extraContext.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final CargoModel cargoModel = ((LNGScenarioModel) rootObject).getCargoModel();

			for (final LoadSlot slot : cargoModel.getLoadSlots()) {
				if (slot instanceof SpotSlot) {
					final SpotSlot spotSlot = (SpotSlot) slot;
					if (spotSlot.getMarket() != null) {
						continue;
					}
				}
				psc.addSlot(slot);
			}
			for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
				if (slot instanceof SpotSlot) {
					final SpotSlot spotSlot = (SpotSlot) slot;
					if (spotSlot.getMarket() != null) {
						continue;
					}
				}
				psc.addSlot(slot);
			}
		}
		return psc;
	}
}
