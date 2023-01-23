/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.util;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;

import com.mmxlabs.models.lng.schedule.Event;

public class ScheduleSequenceTestUtil {

	private ScheduleSequenceTestUtil() {
	
	}

	/*
	 * Checks that the sequence of classes in expectedSequence is matched by the sequence of instances in events.
	 */
	public static void checkSequenceEventOrder(@NonNull final List<@NonNull Class<? extends Event>> expectedClassSequence, @NonNull final List<Event> events) {
		Assertions.assertEquals(expectedClassSequence.size(), events.size());
		final Iterator<Event> eventIter = events.iterator();
		for (@NonNull final Class<? extends Event> expectedClass : expectedClassSequence) {
			Assertions.assertTrue(expectedClass.isInstance(eventIter.next()));
		}
	}
}
