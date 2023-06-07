package org.eclipse.nebula.widgets.ganttchart.label;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.ganttchart.GanttChartParameters;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.label.internal.GeneratedEventText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

@NonNullByDefault
public class PreComputedEventsLabelManager implements IEventsLabelManager {

	private final Map<GanttEvent, @Nullable NavigableMap<Integer, @Nullable List<GeneratedEventText>>> cache = new HashMap<>();
	private final List<List<IEventTextPropertiesGenerator>> sortedTextPropertiesGenerators;
	private final IEventsLabelCalculator labelCalculator;
	private final Image temporaryImage;
	private final GC temporaryGC;
	@Nullable
	private Font temporaryFont;

	public PreComputedEventsLabelManager(final List<List<IEventTextPropertiesGenerator>> sortedTextPropertiesGenerator, final IEventsLabelCalculator labelCalculator) {
		this.sortedTextPropertiesGenerators = sortedTextPropertiesGenerator;
		this.labelCalculator = labelCalculator;
		this.temporaryImage = new Image(Display.getDefault(), 2, 2);
		this.temporaryGC = new GC(temporaryImage);
		this.temporaryFont = GanttChartParameters.getStandardFont();
		temporaryGC.setFont(temporaryFont);
	}

	@Override
	public @Nullable List<GeneratedEventText> getEventTexts(final GanttEvent event, final int eventWidth) {
		final Entry<Integer, @Nullable List<GeneratedEventText>> lowerEntry = cache.computeIfAbsent(event, evt -> {
			final NavigableMap<Integer, @Nullable List<GeneratedEventText>> map = new TreeMap<>();
			int lastMinWidth = Integer.MAX_VALUE;
			for (final List<IEventTextPropertiesGenerator> generators : sortedTextPropertiesGenerators) {
				final List<GeneratedEventText> generatedEventTexts = new LinkedList<>();
				for (final IEventTextPropertiesGenerator generator : generators) {
					final String text = generator.generateText(event);
					if (!text.isBlank()) {
						generatedEventTexts.add(new GeneratedEventText(text, generator.getAlignment(), temporaryGC.stringExtent(text)));
					}
				}
				final int currentMinWidth = labelCalculator.calculateMinimumWidth(event, generatedEventTexts);
				if (lastMinWidth > currentMinWidth) {
					map.put(currentMinWidth, generatedEventTexts);
					lastMinWidth = currentMinWidth;
				}
			}
			return map;
		}).lowerEntry(eventWidth);
		if (lowerEntry != null) {
			return lowerEntry.getValue();
		}
		return null;
	}

	@Override
	public void dispose() {
		temporaryGC.dispose();
		temporaryImage.dispose();
		if (temporaryFont != null) {
			temporaryFont.dispose();
			temporaryFont = null;
		}
	}

	@Override
	public void reset() {
		if (temporaryFont != null) {
			temporaryFont.dispose();
		}
		this.temporaryFont = GanttChartParameters.getStandardFont();
		temporaryGC.setFont(temporaryFont);
		cache.clear();
	}

}
