/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IMemento;

import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.IScheduleEventLabelElementGenerator;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;
import com.mmxlabs.widgets.schedulechart.draw.RelativeDrawableElement;
import com.mmxlabs.widgets.schedulechart.providers.IDrawableScheduleEventLabelProvider;

public class NinetyDayDrawableEventLabelProvider implements IDrawableScheduleEventLabelProvider {
	
	Map<ScheduleEvent, NavigableMap<Integer, List<RelativeDrawableElement>>> cache = new HashMap<>();
	List<List<IScheduleEventLabelElementGenerator>> eventLabelGenerators = Collections.emptyList();

	private TogglableLabel showDays;
	private TogglableLabel showDestinationLabels;
	private TogglableLabel showCanals;
	private IScheduleChartSettings settings;

	private List<ILabellingOption> knownLabels = new ArrayList<>();
	private Map<ILabellingOption, Action> applyActions = new HashMap<>();
	
	public NinetyDayDrawableEventLabelProvider(IMemento memento, IScheduleChartSettings settings) {
		this.showCanals = new TogglableLabel(NinetyDayScheduleViewConstants.SHOW_CANALS, memento);
		this.showDestinationLabels = new TogglableLabel(NinetyDayScheduleViewConstants.SHOW_DESTINATION_LABELS, memento);
		this.showDays = new TogglableLabel(NinetyDayScheduleViewConstants.SHOW_DAYS, memento);
		this.settings = settings;
		applyActions.put(showCanals, new Action() {
			@Override
			public void run() {
				eventLabelGenerators = NinetyDayLabelFactory.buildCanalLabels(settings);
			}
		});

		applyActions.put(showDestinationLabels, new Action() {
			@Override
			public void run() {
				eventLabelGenerators = NinetyDayLabelFactory.buildDestinationLabels(settings);
			}
		});
		applyActions.put(showDays, new Action() {
			@Override
			public void run() {
				eventLabelGenerators = NinetyDayLabelFactory.buildShowDaysLabels(settings);
			}
		});

		knownLabels.add(showCanals);
		knownLabels.add(showDestinationLabels);
		knownLabels.add(showDays);

		final List<ILabellingOption> activeLabels = knownLabels.stream() //
				.filter(ILabellingOption::isShowing) //
				.toList();
		final int numActiveLabels = activeLabels.size();
		if (numActiveLabels == 1) {
			applyActions.get(activeLabels.get(0)).run();
		} else if (numActiveLabels > 1) {
			// Bad state - reset everything
			activeLabels.forEach(ILabellingOption::reset);
		}

		clearLabelCache();
	}

	@Override
	public List<RelativeDrawableElement> getEventLabels(DrawableScheduleEvent dse, DrawerQueryResolver r) {
		final Entry<Integer, List<RelativeDrawableElement>> lowerEntry = cache.computeIfAbsent(dse.getScheduleEvent(), se -> {
			final NavigableMap<Integer, List<RelativeDrawableElement>> map = new TreeMap<>();
			int lastMinWidth = Integer.MAX_VALUE;
			for (final List<IScheduleEventLabelElementGenerator> generators : eventLabelGenerators) {
				final List<RelativeDrawableElement> generatedLabels = new ArrayList<>();
				int currMinWidth = 0;
				for (final IScheduleEventLabelElementGenerator generator: generators) {
					final var label = generator.generate(se, dse);
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
		return Collections.emptyList();
	}
	
	private void clearLabelCache() {
		cache.clear();
	}

	private void toggleTogglable(final @NonNull TogglableLabel togglableLabel) {
		if (togglableLabel.isShowing()) {
			eventLabelGenerators = Collections.emptyList();
		}
		togglableLabel.toggle();
		if (togglableLabel.isShowing()) {
			applyActions.get(togglableLabel).run();
		}
		resetLabelStates(togglableLabel);
		clearLabelCache();
	}

	public void toggleShowDays() {
		toggleTogglable(showDays);
	}

	public void toggleShowDestinationLabels() {
		toggleTogglable(showDestinationLabels);
	}

	public void toggleShowCanals() {
		toggleTogglable(showCanals);
	}

	private void resetLabelStates(final @NonNull ILabellingOption toIgnore) {
		knownLabels.stream().filter(label -> label != toIgnore).forEach(ILabellingOption::reset);
	}

	public boolean showCanals() {
		return showCanals.isShowing();
	}

	public boolean showDestinationLabels() {
		return showDestinationLabels.isShowing();
	}

	public boolean showDays() {
		return showDays.isShowing();
	}
	
	public TogglableLabel getToggledOption() {
		if(showCanals())
			return showCanals;
		else if(showDestinationLabels())
			return showDestinationLabels;
		else if(showDays())
			return showDays;
		return null;
	}
	
	public void reToggleLabel() {
		TogglableLabel label = getToggledOption();
		
		if(label != null) {
			applyActions.get(label).run();
		}
	}
}
