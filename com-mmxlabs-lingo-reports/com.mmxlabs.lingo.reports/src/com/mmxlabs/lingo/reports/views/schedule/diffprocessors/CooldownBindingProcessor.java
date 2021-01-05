/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

/**
 * If a cooldown is created (or removed) bind the previous event.
 * 
 * @author Simon Goodall
 *
 */
public class CooldownBindingProcessor implements IDiffProcessor {

	@Override
	public void processSchedule(final Schedule schedule, final boolean isPinned) {
	}

	@Override
	public void runDiffProcess(final Table table, final List<EObject> referenceElements, final List<EObject> uniqueElements, final Map<EObject, Set<EObject>> equivalancesMap,
			final Map<EObject, Row> elementToRowMap) {

		for (final EObject referenceElement : referenceElements) {

			if (!(referenceElement instanceof SlotAllocation)) {
				continue;
			}
			final SlotAllocation ref_Allocation = (SlotAllocation) referenceElement;
			final SlotVisit ref_slotVisit = ref_Allocation.getSlotVisit();

			final boolean ref_hasCooldownViolation = ref_slotVisit.getViolations().containsKey(CapacityViolationType.FORCED_COOLDOWN);

			final Collection<EObject> equivalents = equivalancesMap.get(referenceElement);
			if (equivalents == null || equivalents.isEmpty()) {
				continue;
			}

			for (final EObject equivalent : equivalents) {
				if (!(equivalent instanceof SlotAllocation)) {
					continue;
				}
				final SlotAllocation other_Allocation = (SlotAllocation) equivalent;
				final SlotVisit other_slotVisit = other_Allocation.getSlotVisit();
				final boolean other_hasCooldownViolation = other_slotVisit.getViolations().containsKey(CapacityViolationType.FORCED_COOLDOWN);

				if (ref_hasCooldownViolation != other_hasCooldownViolation) {
					// Bind reference together.
					{

						Event evt = ref_slotVisit.getPreviousEvent();
						while (evt != null && !(evt instanceof PortVisit)) {
							evt = evt.getPreviousEvent();
						}
						if (evt != null) {
							final Row previousRow = elementToRowMap.get(evt);
							for (final EObject element : equivalents) {
								final Row elementRow = elementToRowMap.get(element);
								if (elementRow != null) {
									final Row referenceRow = elementToRowMap.get(referenceElement);
									final CycleGroup cycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, referenceRow);
									referenceRow.setVisible(true);
									elementRow.setVisible(true);
									CycleGroupUtils.addToOrMergeCycleGroup(table, previousRow, cycleGroup);
								}
							}
						}
					}
					// Bind together equivalent
					{

						Event evt = other_slotVisit.getPreviousEvent();
						while (evt != null && !(evt instanceof PortVisit)) {
							evt = evt.getPreviousEvent();
						}
						if (evt != null) {
							final Row previousRow = elementToRowMap.get(evt);
							for (final EObject element : equivalents) {
								final Row elementRow = elementToRowMap.get(element);
								if (elementRow != null) {
									final Row equivalentRow = elementToRowMap.get(equivalent);
									final CycleGroup cycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, equivalentRow);
									equivalentRow.setVisible(true);
									elementRow.setVisible(true);
									CycleGroupUtils.addToOrMergeCycleGroup(table, previousRow, cycleGroup);
								}
							}
						}
					}

				}
			}

		}

	}
}
