package org.eclipse.nebula.widgets.ganttchart.label;

import java.util.Iterator;
import java.util.List;
import java.util.function.ToIntFunction;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.label.internal.GeneratedEventText;

@NonNullByDefault
public class DefaultEventsLabelCalculator implements IEventsLabelCalculator {

	private final ToIntFunction<GanttEvent> sideMarginCalculator;
	private final int betweenLabelSpacing;

	public DefaultEventsLabelCalculator(final ToIntFunction<GanttEvent> sideMarginCalculator, final int betweenLabelSpacing) {
		this.sideMarginCalculator = sideMarginCalculator;
		this.betweenLabelSpacing = betweenLabelSpacing;
	}

	@Override
	public boolean fitsInWidth(final GanttEvent event, final List<GeneratedEventText> generatedEventTexts, final int eventWidth) {
		return eventWidth >= calculateMinimumWidth(event, generatedEventTexts);
	}

	@Override
	public int calculateMinimumWidth(final GanttEvent event, final List<GeneratedEventText> generatedEventTexts) {
		if (generatedEventTexts.isEmpty()) {
			return 0;
		}
		if (generatedEventTexts.size() == 1) {
			final int textWidth = generatedEventTexts.get(0).size().x;
			return textWidth + sideMarginCalculator.applyAsInt(event)*2;
		}
		int numGroups = 1;
		int largestGroupSize = 0;
		final Iterator<GeneratedEventText> iterGeneratedEventText = generatedEventTexts.iterator();
		final GeneratedEventText firstGeneratedEventText = iterGeneratedEventText.next();
		final int firstSize = firstGeneratedEventText.size().x;
		int currentGroupSize;
		if (firstGeneratedEventText.alignment() == EEventLabelAlignment.CENTRE) {
			currentGroupSize = firstSize/2;
			largestGroupSize = currentGroupSize;
			++numGroups;
		} else {
			currentGroupSize = firstSize;
		}
		while (iterGeneratedEventText.hasNext()) {
			final GeneratedEventText currentGeneratedEventText = iterGeneratedEventText.next();
			final int currentSize = currentGeneratedEventText.size().x;
			currentGroupSize += betweenLabelSpacing;
			if (currentGeneratedEventText.alignment() == EEventLabelAlignment.CENTRE) {
				final int halfSize = currentSize/2;
				currentGroupSize += halfSize;
				if (currentGroupSize > largestGroupSize) {
					largestGroupSize = currentGroupSize;
				}
				++numGroups;
				// Rounding could cause issues here
				currentGroupSize = halfSize;
			} else {
				currentGroupSize += currentSize;
			}
		}
		if (currentGroupSize > largestGroupSize) {
			largestGroupSize = currentGroupSize;
		}
		final int nonMarginWidth = numGroups*largestGroupSize;
		return nonMarginWidth + sideMarginCalculator.applyAsInt(event)*2;
	}

}
