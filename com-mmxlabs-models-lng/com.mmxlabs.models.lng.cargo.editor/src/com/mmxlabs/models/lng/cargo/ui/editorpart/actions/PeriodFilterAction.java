package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.mmxlabs.models.lng.cargo.ui.editorpart.StructuredViewerFilterManager;
import com.mmxlabs.models.lng.cargo.ui.filters.TimePeriodFilter;
import com.mmxlabs.models.lng.cargo.util.CargoEditorFilterUtils;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class PeriodFilterAction extends DefaultMenuCreatorAction {
	private static final String TIME_FILTER_ID = "Time";

	private final StructuredViewerFilterManager filterManager;
	private final LocalDate earliest;
	private final LocalDate latest;

	public PeriodFilterAction(StructuredViewerFilterManager filterManager, LocalDate earliest, LocalDate latest) {
		super("Period");
		this.filterManager = filterManager;
		this.earliest = earliest;
		this.latest = latest;
	}

	@Override
	protected void populate(final Menu menu) {
		if (filterManager.filterExists(TIME_FILTER_ID)) {
			final Action clearTimeFilters = new Action("Clear Period Filters") {
				public void run() {
					filterManager.removeFilter(TIME_FILTER_ID);
				}
			};
			addActionToMenu(clearTimeFilters, menu);
		}

		final Action currentAction = new Action("Today onwards") {
			@Override
			public void run() {
				filterManager.addFilter(TIME_FILTER_ID, new TimePeriodFilter(CargoEditorFilterUtils.TimeFilterOption.CURRENT));
			}
		};
		addActionToMenu(currentAction, menu);

		final Action promptAction = new Action("Today +3m") {
			@Override
			public void run() {
				filterManager.addFilter(TIME_FILTER_ID, new TimePeriodFilter(CargoEditorFilterUtils.TimeFilterOption.PROMPT));
			}
		};
		addActionToMenu(promptAction, menu);

		new MenuItem(menu, SWT.SEPARATOR);

		buildMonth(earliest, latest, menu);
	}

	private void buildMonth(final LocalDate start, final LocalDate end, final Menu menu) {
		final YearMonth yms = YearMonth.from(start);
		final int firstYear = yms.getYear();
		final YearMonth yme = YearMonth.from(end);
		int i = 0;

		if (start.plusYears(2).isAfter(end)) {
			while (!yme.isBefore(yms.plusMonths(i))) {
				final YearMonth ymc = yms.plusMonths(i);
				if (i != 0 && ymc.getMonthValue() == 1) {
					new MenuItem(menu, SWT.SEPARATOR);
				}
				final TimePeriodFilter filter = new TimePeriodFilter(CargoEditorFilterUtils.TimeFilterOption.YEARMONTH, ymc);
				Action ymcAction;
				if (!filterManager.containsFilter(TIME_FILTER_ID, filter)) {
					ymcAction = new Action(formatMe(ymc, firstYear != ymc.getYear())) {
						@Override
						public void runWithEvent(Event e) {
							if ((e.stateMask & SWT.CTRL) != 0) {
								filterManager.addFilterAsUnion(TIME_FILTER_ID, filter);
							} else {
								filterManager.addFilter(TIME_FILTER_ID, filter);
							}
						}
					};
				} else {
					ymcAction = new Action(formatMe(ymc, firstYear != ymc.getYear())) {
						@Override 
						public void run() {
							filterManager.removeFilter(TIME_FILTER_ID, filter, true);
						}
					};
					ymcAction.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
				}

				addActionToMenu(ymcAction, menu);
				i++;
			}
		} else {
			List<Action> actionsForYear = new ArrayList<>(12);
			int counter = yms.getMonthValue();
			int currentYear = yms.getYear();
			while (!yme.isBefore(yms.plusMonths(i))) {
				final YearMonth ymc = yms.plusMonths(i);
				if (counter == 13) {
					addListOfActionsToMenu(menu, Integer.toString(currentYear), actionsForYear);
					currentYear++;
					actionsForYear.clear();
					counter = 1;
				}
				final TimePeriodFilter filter = new TimePeriodFilter(CargoEditorFilterUtils.TimeFilterOption.YEARMONTH, ymc);
				Action ymcAction;
				if (!filterManager.containsFilter(TIME_FILTER_ID, filter)) {
					ymcAction = new Action(formatMe(ymc, false)) {
						@Override
						public void runWithEvent(Event e) {
							if ((e.stateMask & SWT.CTRL) != 0) {
								filterManager.addFilterAsUnion(TIME_FILTER_ID, filter);
							} else {
								filterManager.addFilter(TIME_FILTER_ID, filter);
							}
						}
					};
				} else {
					ymcAction = new Action(formatMe(ymc, false)) {
						@Override 
						public void run() {
							filterManager.removeFilter(TIME_FILTER_ID, filter, true);
						}
					};
					ymcAction.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
				}
				actionsForYear.add(ymcAction);
				counter++;
				i++;
			}
			addListOfActionsToMenu(menu, Integer.toString(currentYear), actionsForYear);
		}
	}

	private String formatMe(final YearMonth val, final boolean showYear) {
		String result = String.format("%s", val.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
		if (showYear) {
			result += String.format(" '%d", (val.getYear() % 100));
		}
		return result;
	}

	private void addListOfActionsToMenu(final Menu menu, final String title, final List<Action> collection) {
		final DefaultMenuCreatorAction dmca = new DefaultMenuCreatorAction(title) {
			final List<Action> local = new LinkedList<>(collection);

			@Override
			protected void populate(final Menu menu) {
				for (final Action a : this.local) {
					addActionToMenu(a, menu);
				}
			}
		};
		addActionToMenu(dmca, menu);
		collection.clear();
	}

}
