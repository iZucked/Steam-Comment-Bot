/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import static com.mmxlabs.models.lng.cargo.util.SlotClassifier.classify;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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

		private final Map<Port, Map<String, Collection<Slot>>> countingMap = new HashMap<Port, Map<String, Collection<Slot>>>();

		private final DateFormat df = DateFormat.getDateInstance();

		/**
		 * Returns a modifiable {@link Collection} of {@link Slot} objects which overlap the given {@link Slot}.
		 * 
		 * @param slot
		 * @return
		 */
		public Collection<Slot> slotOverlaps(final Slot slot) {

			final Date windowStart = slot.getWindowStartWithSlotOrPortTime();
			if (windowStart == null) {
				return Collections.emptySet();
			}
			final Calendar cal = Calendar.getInstance();
			cal.setTime(windowStart);
			int windowSize = slot.getWindowSize();

			final Set<Slot> overlappingSlots = new LinkedHashSet<Slot>();
			final SlotType slotType = classify(slot);
			do {
				final String dateKey = dateToString(cal.getTime());
				final Collection<Slot> potentialOverlaps = getOverlappingSlots(slot, dateKey);
				final Iterator<Slot> ii = potentialOverlaps.iterator();
				while (ii.hasNext()) {
					final Slot overlapSlot = ii.next();
					final SlotType overlapSlotType = classify(overlapSlot);
					// The combinations below are OK, let them pass
					if (overlapSlotType == SlotType.DES_Buy && slotType == SlotType.DES_Sale || (overlapSlotType == SlotType.DES_Sale && slotType == SlotType.DES_Buy)
							|| (overlapSlotType == SlotType.FOB_Sale && slotType == SlotType.FOB_Buy) || (overlapSlotType == SlotType.FOB_Buy && slotType == SlotType.FOB_Sale)) {
						ii.remove();
						continue;
					}

					final Date slotStart = slot.getWindowStartWithSlotOrPortTime();
					final Date overlapSlotStart = overlapSlot.getWindowStartWithSlotOrPortTime();
					final int slotDur = slot.getDuration();
					final int overlapSlotDur = overlapSlot.getDuration();
					final Date olEnd = overlapSlot.getWindowEndWithSlotOrPortTime();
					final Date slotEnd = slot.getWindowEndWithSlotOrPortTime();

					// if slot start + duration is before the end of the overlapSlot window, it can be OK so let them pass
					final Calendar slotCal = Calendar.getInstance();
					slotCal.setTime(slotStart);
					slotCal.add(Calendar.HOUR_OF_DAY, slotDur);
					final Date slotFinish = slotCal.getTime();
					if (slotFinish.before(olEnd)) {
						ii.remove();
						continue;
					}

					final Calendar overlapSlotCal = Calendar.getInstance();
					overlapSlotCal.setTime(overlapSlotStart);
					overlapSlotCal.add(Calendar.HOUR_OF_DAY, overlapSlotDur);
					final Date overlapSlotFinish = overlapSlotCal.getTime();
					if (overlapSlotFinish.before(slotEnd)) {
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
				cal.add(Calendar.DAY_OF_MONTH, 1);
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

			final Date windowStart = slot.getWindowStartWithSlotOrPortTime();
			if (windowStart == null) {
				return;
			}
			final Calendar cal = Calendar.getInstance();
			cal.setTime(windowStart);
			int windowPlusDurationSize = slot.getWindowSize();// + slot.getDuration();
			do {
				final String dateKey = dateToString(cal.getTime());
				final Collection<Slot> slots = getOverlappingSlots(slot, dateKey);
				slots.add(slot);
				// if(Calendar.get(Calendar.HOUR_OF_DAY, slot.getWindowStartWithSlotOrPortTime()) == 0)
				windowPlusDurationSize -= 24;
				cal.add(Calendar.DAY_OF_MONTH, 1);
			} while (windowPlusDurationSize > 0);
		}

		private Collection<Slot> getOverlappingSlots(final Slot slot, final String dateKey) {

			final Port port = slot.getPort();
			Map<String, Collection<Slot>> map;
			if (countingMap.containsKey(port)) {
				map = countingMap.get(port);
			} else {
				map = new HashMap<String, Collection<Slot>>();
				countingMap.put(port, map);
			}
			Collection<Slot> col;
			if (map.containsKey(dateKey)) {
				col = map.get(dateKey);
			} else {
				col = new LinkedList<Slot>();
				map.put(dateKey, col);
			}
			return col;

		}

		private String dateToString(final Date date) {
			return df.format(date);
		}
	}

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
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
			final CargoModel cargoModel = ((LNGScenarioModel) rootObject).getPortfolioModel().getCargoModel();

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
