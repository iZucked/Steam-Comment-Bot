/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.IElementComparer;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.MultiEvent;

public class EquivalentsManager {
	
	private final Map<Object, Object> equivalents = new HashMap<>();
	private final Set<Object> contents = new HashSet<>();
	
	public void setInputEquivalents(final Object input, final Collection<Object> objectEquivalents) {
		for (final Object o : objectEquivalents) {
			equivalents.put(o, input);
		}
		contents.add(input);
	}

	public void clearInputEquivalents() {
		equivalents.clear();
		contents.clear();
	}
	
	public IElementComparer getComparer() {
		return new IElementComparer() {

			@Override
			public int hashCode(final Object element) {
				return element.hashCode();
			}

			@Override
			public boolean equals(Object a, Object b) {
				if (!contents.contains(a) && equivalents.containsKey(a)) {
					a = equivalents.get(a);
				}
				if (!contents.contains(b) && equivalents.containsKey(b)) {
					b = equivalents.get(b);
				}
				return Objects.equals(a, b);
			}
		};
	}

	public List<Object> expandSelection(final Collection<Object> selectedObjects) {
		final Set<Object> newSelection = new HashSet<>(selectedObjects.size());
		for (final Object o : selectedObjects) {
			newSelection.add(o);
			if (o instanceof final Slot<?> slot) {
				final Object object = equivalents.get(slot);
				if (object instanceof final SlotVisit slotVisit) {
					newSelection.add(slotVisit);

					final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
					if (slotAllocation == null) continue;
					newSelection.add(slotAllocation);

					final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
					if (cargoAllocation == null) continue;
					newSelection.add(cargoAllocation);
					newSelection.addAll(cargoAllocation.getEvents());

				} else if (object instanceof final OpenSlotAllocation openSlotAllocation) {
					newSelection.add(openSlotAllocation);

				} else if (object instanceof final MultiEvent multiEvent) {
					newSelection.add(multiEvent);
					newSelection.addAll(multiEvent.getElements());

				} else if (object instanceof final @NonNull NonShippedSlotVisit slotVisit && !newSelection.contains(slotVisit)) {
					newSelection.addAll(getNonShippedExtraEvents(slotVisit));

				}
			} else if (equivalents.containsKey(o)) {
				newSelection.add(equivalents.get(o));
			}
		}
		newSelection.retainAll(contents);
		return new ArrayList<>(newSelection);
	}
	
	public void collectEquivalents(Object event, Function<Object, List<Object>> equivalentsGenerator) {
		if (event instanceof final MultiEvent me) {
			final List<Object> localEquivalents = new ArrayList<>();
			for (final var e : me.getElements()) {
				localEquivalents.addAll(equivalentsGenerator.apply(e));
			}
			localEquivalents.addAll(me.getElements());
			setInputEquivalents(event, localEquivalents);
		} else {
			setInputEquivalents(event, equivalentsGenerator.apply(event));
		}
	}
	
	private static List<Event> getNonShippedExtraEvents(final @NonNull NonShippedSlotVisit slotVisit) {
		final List<Event> events = new LinkedList<>();
		final Cargo commonCargo = slotVisit.getSlot().getCargo();
		assert commonCargo != null;
		final NonShippedSlotVisit firstLoadVisit = getFirstCommonCargoNonShippedSlotVisit(slotVisit);
		Event currentEvent = firstLoadVisit;
		while (currentEvent != null) {
			if (currentEvent instanceof NonShippedSlotVisit currentSlotVisit && currentSlotVisit.getSlot().getCargo() != commonCargo) {
				break;
			}
			events.add(currentEvent);
			currentEvent = currentEvent.getNextEvent();
		}
		return events;
	}

	private static NonShippedSlotVisit getFirstCommonCargoNonShippedSlotVisit(final @NonNull NonShippedSlotVisit slotVisit) {
		final Cargo commonCargo = slotVisit.getSlot().getCargo();
		NonShippedSlotVisit currentFirst = slotVisit;
		Event currentEvent = slotVisit.getPreviousEvent();
		while (currentEvent != null) {
			if (currentEvent instanceof NonShippedSlotVisit currentSlotVisit) {
				if (currentSlotVisit.getSlot().getCargo() == commonCargo) {
					currentFirst = currentSlotVisit;
				} else {
					break;
				}
			}
			currentEvent = currentEvent.getPreviousEvent();
		}
		return currentFirst;
	}
}
