package com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell;

import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.MultiEvent;
import com.mmxlabs.models.lng.schedule.util.PositionsSequence;

/**
 * Wrapper around a single position sequence element.
 * Used for distinguishing position sequence {@link SlotVisit} from {@link OpenSlotAllocation} and {@link MultiEvent}.
 * 
 * @author AP
 */
public class PositionsSequenceElement {
	
	private final PositionsSequence parentSequence;
	private final Object element;
	private final boolean isBuy;
	
	private PositionsSequenceElement(final Object openSlotAllocationOrSlotVisitForBuySell, final boolean isBuy, PositionsSequence parentSequence) {
		this.isBuy = isBuy;
		this.element = openSlotAllocationOrSlotVisitForBuySell;
		this.parentSequence = parentSequence;
	}

	/**
	 * Nicer constructor
	 */
	public static PositionsSequenceElement of(Object element, final boolean isBuyRowElement, PositionsSequence parentSequence) {
		return new PositionsSequenceElement(element, isBuyRowElement, parentSequence);
	}

	public Object getElement() {
		return element;
	}
	
	public boolean isBuyRow() {
		return isBuy;
	}

	public boolean isMulti() {
		return element instanceof MultiEvent;
	}

	public PositionsSequence getPositionsSequence() {
		return parentSequence;
	}

}
