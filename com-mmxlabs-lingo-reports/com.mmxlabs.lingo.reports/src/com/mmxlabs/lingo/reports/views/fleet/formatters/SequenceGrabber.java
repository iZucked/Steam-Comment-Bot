/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.List;
import java.util.function.ToIntFunction;

import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Sequence;

/**
 * Utility class to find the various places a sequence is obtained from the expected input data from the Vessel Summary report.
 * 
 * @author Simon Goodall
 *
 */
public class SequenceGrabber {
	private SequenceGrabber() {

	}

	public static int applyToSequences(final Object object, final ToIntFunction<Sequence> action) {
		int value = 0;

		if (object instanceof Row) {
			final Row row = ((Row) object);
			final Sequence s = row.getSequence();
			if (s != null) {
				value += action.applyAsInt(s);
			}
		} else if (object instanceof Sequence) {
			final Sequence sequence = (Sequence) object;
			value += action.applyAsInt(sequence);
		} else if (object instanceof List) {
			final List<?> objects = (List<?>) object;
			if (!objects.isEmpty()) {
				for (final Object o : objects) {
					if (o instanceof Row) {
						final Row row = ((Row) o);
						final Sequence s = row.getSequence();
						if (s != null) {
							value += action.applyAsInt(s);
						}
					} else if (o instanceof Sequence) {
						final Sequence sequence = (Sequence) o;
						value += action.applyAsInt(sequence);
					}
				}
			}
		}
		return value;
	}
}
