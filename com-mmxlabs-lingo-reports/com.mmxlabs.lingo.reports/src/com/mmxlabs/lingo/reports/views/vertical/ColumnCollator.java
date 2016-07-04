/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.vertical.providers.EventProvider;
import com.mmxlabs.lingo.reports.views.vertical.providers.HashMapEventProvider;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;

/**
 * Class to encapsulate the logic for collating sparse vessel columns into as few columns as possible in a vertical report. A single instance of this class will produce a list of EventProvider objects
 * showing which events occur on which days for such vessels.
 * 
 * The object greedily collates active intervals into free space in existing columns, and will leave a vessel plan intact unless the whole plan can be incorporated into one or more other columns.
 * 
 * 
 * @author Simon McGregor
 * 
 */
public abstract class ColumnCollator {
	private final ArrayList<HashMapEventProvider> columns = new ArrayList<>();
	private final ScheduleSequenceData data;
	private final AbstractVerticalReportVisualiser verticalReportVisualiser;

	/**
	 * @param data
	 *            The sequence data for the report.
	 */
	public ColumnCollator(final @NonNull ScheduleSequenceData data, final @NonNull AbstractVerticalReportVisualiser verticalReportVisualiser) {
		this.data = data;
		this.verticalReportVisualiser = verticalReportVisualiser;
	}

	/**
	 * Returns the sequences which are associated with charter-ins.
	 * 
	 * @return
	 */
	private List<@NonNull Sequence> getSequences() {
		final List<@NonNull Sequence> result = new ArrayList<>();
		if (data.vessels != null) {
			for (final Sequence seq : data.vessels) {
				if (includeSequence(seq)) {
					result.add(seq);
				}
			}
		}
		return result;
	}

	/**
	 * Return true is the sequence is a candidate for collating.
	 * 
	 * @param sequence
	 * @return
	 */
	protected abstract boolean includeSequence(@NonNull Sequence sequence);

	/**
	 * Determines if the entire sequence of events for a particular provider can be collated into empty space in the existing event providers for this collator, without breaking events on consecutive
	 * days.
	 * 
	 * TODO: rewrite this so that event continuity is understood correctly (by reference to journeys etc.) instead of simply by consecutive dates
	 * 
	 * @param provider
	 * @return
	 */
	protected boolean canCollate(final @NonNull EventProvider provider) {
		final List<Pair<LocalDate, LocalDate>> intervals = getActiveIntervals(provider);
		for (final Pair<LocalDate, LocalDate> interval : intervals) {
			if (canCollate(interval) == false) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Determines if events from a particular interval can be inserted into an empty space in an existing column event provider.
	 * 
	 * @param interval
	 * @return
	 */
	protected boolean canCollate(final @NonNull Pair<LocalDate, LocalDate> interval) {
		for (final HashMapEventProvider candidate : columns) {
			if (emptyBetween(interval, candidate)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines whether a particular event provider is empty (free of events) between the specified dates, inclusive.
	 * 
	 * @param first
	 * @param second
	 * @param candidate
	 * @return
	 */
	protected boolean emptyBetween(final Pair<LocalDate, LocalDate> interval, final EventProvider candidate) {
		for (final LocalDate date : VerticalReportUtils.getUTCDaysBetween(interval.getFirst(), interval.getSecond())) {
			if (candidate.getEvents(date).length != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the date intervals for a particular event provider during which it has continuous activity.
	 * 
	 * @param provider
	 * @return A list of closed date intervals as X, Y pairs where X represents the first Date in the interval and Y represents the last Date in the interval.
	 */
	List<Pair<LocalDate, LocalDate>> getActiveIntervals(final EventProvider provider) {
		final List<Pair<LocalDate, LocalDate>> result = new ArrayList<>();

		LocalDate start = null;
		LocalDate lastActive = null;

		for (final LocalDate date : VerticalReportUtils.getUTCDaysBetween(data.start, data.end)) {
			// no event on the specified date
			if (provider.getEvents(date).length == 0) {
				// if this is the end of an interval, add it to the list
				if (start != null) {
					result.add(new Pair<>(start, lastActive));
					start = null;
				}
			}
			// one or more events on the specified date
			else {
				// if this is the start of an interval, remember it
				if (start == null) {
					start = date;
				}
				lastActive = date;
			}
		}

		// make sure we mark any trailing intervals at the end of the schedule
		if (start != null) {
			result.add(new Pair<>(start, data.end));
		}

		return result;
	}

	/**
	 * Collates the information from the specified provider into one or more of the existing column event providers.
	 * 
	 * @param provider
	 */
	private void collate(final HashMapEventProvider provider) {
		final List<Pair<LocalDate, LocalDate>> intervals = getActiveIntervals(provider);
		for (final Pair<LocalDate, LocalDate> interval : intervals) {
			collate(interval, provider);
		}

	}

	/**
	 * Collates the information from the specified provider, during the specified interval, into one or more of the existing column event providers.
	 * 
	 * @param interval
	 * @param provider
	 */
	private void collate(final Pair<LocalDate, LocalDate> interval, final HashMapEventProvider provider) {
		for (final HashMapEventProvider candidate : columns) {
			if (emptyBetween(interval, candidate)) {
				for (final LocalDate date : VerticalReportUtils.getUTCDaysBetween(interval.getFirst(), interval.getSecond())) {
					for (final Event event : provider.getEvents(date)) {
						candidate.addEvent(date, event);
					}
				}
			}
		}
	}

	/**
	 * Returns a list of event providers for columns showing charter-in vessel activity. These compress the charter-in vessels into as few non-overlapping columns as possible.
	 * 
	 * @param data
	 * @return
	 */
	public List<HashMapEventProvider> collate() {
		final List<@NonNull Sequence> sequences = getSequences();
		for (final Sequence seq : sequences) {
			// create a provider for this specific charter-in vessel
			@NonNull
			final EventProvider wrapped = createEventProvider(seq);
			final HashMapEventProvider provider = new HashMapEventProvider(data.start, data.end, wrapped);
			// and collate it if possible
			if (canCollate(provider)) {
				collate(provider);
			}
			// otherwise add it as a new column
			else {
				columns.add(provider);
			}
		}

		return columns;
	}

	protected abstract @NonNull EventProvider createEventProvider(@NonNull Sequence sequence);
}
