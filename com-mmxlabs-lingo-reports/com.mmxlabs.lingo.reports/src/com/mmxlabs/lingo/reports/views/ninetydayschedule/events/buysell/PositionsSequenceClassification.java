/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell;

import com.mmxlabs.models.lng.schedule.util.PositionsSequence;

/**
 * Tuple for all flags and enums that know what {@link PositionsSequence} events are represented
 * by the object
 * 
 * @author Andre
 *
 */
public record PositionsSequenceClassification( //
		PositionStateType positionStateType, //
		PositionType positionType, //
		boolean isBuy ,//
		boolean isMulti //
) {

}
