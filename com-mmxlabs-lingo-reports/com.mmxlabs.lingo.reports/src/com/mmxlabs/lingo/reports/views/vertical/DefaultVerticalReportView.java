/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.views.vertical.labellers.EventLabelProvider;
import com.mmxlabs.lingo.reports.views.vertical.providers.SequenceEventProvider;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.util.CombinedSequence;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;

public class DefaultVerticalReportView extends AbstractVerticalCalendarReportView {

	public DefaultVerticalReportView() {
		super(new DefaultVerticalReportVisualiser());
	}

	@Override
	protected ReportNebulaGridManager createContentProvider() {
		ReportNebulaGridManager manager = super.createContentProvider();
		return manager;
	}

	@Override
	protected List<CalendarColumn> createCalendarCols(final ScheduleSequenceData data) {
		final List<CalendarColumn> result = new LinkedList<>();
		// add a FOB / DES column
		final Sequence[] fobDesSequences = new Sequence[] { data.desPurchases, data.fobSales };
		final CalendarColumn fobDesColumn = new CalendarColumn(verticalReportVisualiser.createDateFormat(), new SequenceEventProvider(fobDesSequences, null, verticalReportVisualiser),
				new EventLabelProvider(verticalReportVisualiser), "FOB/DES", null);
		result.add(fobDesColumn);

		if (data.vessels != null) {
			 List<CombinedSequence> combinedSequences =
			 CombinedSequence.createCombinedSequences(Lists.newArrayList(data.vessels));

			// add a column for each vessel in the scenario
			if (!combinedSequences.isEmpty()) {
				for (final CombinedSequence seq : combinedSequences) {
					if (seq != null) {
						final CalendarColumn column = new CalendarColumn(verticalReportVisualiser.createDateFormat(),
								new SequenceEventProvider(seq.getSequences().toArray(new Sequence[seq.getSequences().size()]), null, verticalReportVisualiser),
								new EventLabelProvider(verticalReportVisualiser), seq.getVessel().getName(), null);
						result.add(column);
					}
				}
			}
		}
		// charter ins
		if (data.vessels != null) {
			for (final Sequence seq : data.vessels) {
				if (seq.isSpotVessel()) {
					CombinedSequence cs = new CombinedSequence(null);
					cs.getSequences().add(seq);
					final CalendarColumn column = new CalendarColumn(verticalReportVisualiser.createDateFormat(),
							new SequenceEventProvider(cs.getSequences().toArray(new Sequence[cs.getSequences().size()]), null, verticalReportVisualiser),
							new EventLabelProvider(verticalReportVisualiser), seq.getName(), null);
					result.add(column);
				}
			}
		}

		return result;
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IReportContents.class.isAssignableFrom(adapter)) {

			final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(gridViewer.getGrid(), false, true);
			util.setRowHeadersIncluded(true);
			util.setShowBackgroundColours(true);
			final String contents = util.convert();
			return (T) new IReportContents() {

				@Override
				public String getStringContents() {
					return contents;
				}
			};

		}
		return super.getAdapter(adapter);
	}
}
