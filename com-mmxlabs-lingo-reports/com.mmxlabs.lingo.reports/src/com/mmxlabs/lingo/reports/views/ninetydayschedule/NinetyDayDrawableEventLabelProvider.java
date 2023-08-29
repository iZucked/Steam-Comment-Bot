package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.mmxlabs.widgets.schedulechart.IScheduleEventLabelElementGenerator;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;
import com.mmxlabs.widgets.schedulechart.draw.RelativeDrawableElement;
import com.mmxlabs.widgets.schedulechart.providers.IDrawableScheduleEventLabelProvider;

public class NinetyDayDrawableEventLabelProvider implements IDrawableScheduleEventLabelProvider {
	
	Map<ScheduleEvent, NavigableMap<Integer, List<RelativeDrawableElement>>> cache = new HashMap<>();
	List<List<IScheduleEventLabelElementGenerator>> eventLabelGenerators = NinetyDayLabelFactory.buildShowDaysLabels();
	
	@Override
	public List<RelativeDrawableElement> getEventLabels(DrawableScheduleEvent dse, DrawerQueryResolver r) {
		final Entry<Integer, List<RelativeDrawableElement>> lowerEntry = cache.computeIfAbsent(dse.getScheduleEvent(), se -> {
			final NavigableMap<Integer, List<RelativeDrawableElement>> map = new TreeMap<>();
			int lastMinWidth = Integer.MAX_VALUE;
			for (final List<IScheduleEventLabelElementGenerator> generators : eventLabelGenerators) {
				final List<RelativeDrawableElement> generatedLabels = new ArrayList<>();
				int currMinWidth = 0;
				for (final IScheduleEventLabelElementGenerator generator: generators) {
					final var label = generator.generate(se, dse.getLabelTextColour(), dse.getBackgroundColour());
					final int labelWidth = label.getLabelWidth(r);
					if (labelWidth > 0) {
						generatedLabels.add(label);
						currMinWidth += labelWidth;
					}
				}
				
				if (lastMinWidth > currMinWidth) {
					map.put(currMinWidth, generatedLabels);
					lastMinWidth = currMinWidth;
				}
			}
			return map;
		}).lowerEntry(dse.getBounds().width);
		if (lowerEntry != null) {
			lowerEntry.getValue().forEach(rel -> rel.setBoundsFromRelative(dse));
			return lowerEntry.getValue();
		}
		return List.of();
	}

}
