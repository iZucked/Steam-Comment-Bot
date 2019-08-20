/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.ColumnFilters;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;

public class TimePeriodTradesBasedFilterHandler implements ITradesBasedFilterHandler {

	public TimePeriodTradesBasedFilterHandler(final LocalDate start, final LocalDate end) {
		super();
		this.start = start;
		this.end = end;
	}
	
	private final LocalDate start;
	private final LocalDate end;
	
	@Override
	public Action activateAction(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer) {
		DefaultMenuCreatorAction action = new DefaultMenuCreatorAction("Months") {

			@Override
			protected void populate(final Menu menu) {
				
				final Action fooAction = new Action("Prompt") {
					@Override
					public void run() {
						final Iterator<ITradesBasedFilterHandler> itr = activeFilters.iterator();
						while (itr.hasNext()) {
							final ITradesBasedFilterHandler filter = itr.next();
							if (filter instanceof TimePeriodFilterHandler) {
								itr.remove();
							}
						}
						activeFilters.add(new TimePeriodFilterHandler(TimeFilterType.PROMPT, 3));
						viewer.getScenarioViewer().refresh();
					}
				};
				addActionToMenu(fooAction, menu);

				buildMonth(start, end, menu);
			}

			private void buildMonth(final LocalDate start, final LocalDate end, final Menu menu) {
				final YearMonth yms = YearMonth.from(start);
				final int firstYear = yms.getYear();
				final YearMonth yme = YearMonth.from(end);
				int i = 0;

				while (!yme.isBefore(yms.plusMonths(i))) {
					final YearMonth ymc = yms.plusMonths(i);
					final Action fooAction = new Action(formatMe(ymc, firstYear != ymc.getYear())) {
						@Override
						public void run() {
							final Iterator<ITradesBasedFilterHandler> itr = activeFilters.iterator();
							while (itr.hasNext()) {
								final ITradesBasedFilterHandler filter = itr.next();
								if (filter instanceof TimePeriodFilterHandler) {
									itr.remove();
								}
							}
							activeFilters.add(new TimePeriodFilterHandler(TimeFilterType.YEARMONTH, ymc));
							viewer.getScenarioViewer().refresh();
						}
					};

					addActionToMenu(fooAction, menu);
					i++;
				}
			}

			private String formatMe(final YearMonth val, final boolean showYear) {
				String result = String.format("%s", val.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
				if (showYear) {
					result += String.format(" %d", (val.getYear() % 100));
				}
				return result;
			}
		};
		return action;
	}

	@Override
	public Action deactivateAction(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activate(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRowVisible(Row row) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isDefaultFilter() {
		// TODO Auto-generated method stub
		return true;
	}
	
	private enum TimeFilterType {
		NONE, YEARMONTH, PROMPT
	}
	
	private class TimePeriodFilterHandler implements ITradesBasedFilterHandler {
		
		private TimeFilterType type = TimeFilterType.NONE;
		private YearMonth choice = null;
		private int promptMonth = 3;

		public TimePeriodFilterHandler(TimeFilterType type) {
			this.type = type;
		}
		
		public TimePeriodFilterHandler(TimeFilterType type, YearMonth choice) {
			this.type = type;
			this.choice = choice;
		}
		
		public TimePeriodFilterHandler(TimeFilterType type, int promtMonth) {
			this.type = type;
			this.promptMonth = promtMonth;
		}

		@Override
		public Action activateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
			return null;
		}

		@Override
		public Action deactivateAction(ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer) {
			return null;
		}

		@Override
		public void activate(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters) {

		}

		@Override
		public void deactivate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters) {
			activeFilters.remove(this);
		}

		@Override
		public boolean isRowVisible(Row row) {
			if (type == TimeFilterType.NONE)
				return true;
			if (row != null) {
				switch (type) {
				case YEARMONTH:
					return checkYearMonth(row, choice);
				case PROMPT:
					return checkPrompt(row, promptMonth);
				}
			}
			return false;
		}

		@Override
		public boolean isDefaultFilter() {
			return false;
		}
		
		private boolean checkYearMonth(final Row row, final YearMonth choice) {
			LocalDate start = null;
			LocalDate end = null;
			final LoadSlot ls = row.getLoadSlot();
			if (ls != null) {
				start = ls.getSchedulingTimeWindow().getStart().toLocalDate();
				end = ls.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
			if ((start != null) && (end != null)) {
				final YearMonth yms = YearMonth.from(start);
				final YearMonth yme = YearMonth.from(end);
				if ((yms.equals(choice)) || (yme.equals(choice))) {
					return true;
				}
			}
			final DischargeSlot ds = row.getDischargeSlot();
			if (ds != null) {
				start = ds.getSchedulingTimeWindow().getStart().toLocalDate();
				end = ds.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
			if ((start != null) && (end != null)) {
				final YearMonth yms = YearMonth.from(start);
				final YearMonth yme = YearMonth.from(end);
				if ((yms.equals(choice)) || (yme.equals(choice))) {
					return true;
				}
			}
			return false;
		}

		private boolean checkPrompt(final Row row, final int month) {
			final boolean result = false;
			LocalDate start = null;
			LocalDate end = null;
			final LocalDate today = LocalDate.now();
			final LocalDate prompt = today.plusMonths(month);
			final LoadSlot ls = row.getLoadSlot();
			if (ls != null) {
				start = ls.getSchedulingTimeWindow().getStart().toLocalDate();
				end = ls.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
			if (start != null && end != null) {
				if (start.isAfter(today) && start.isBefore(prompt)) {
					return true;
				}
				if (end.isAfter(today) && end.isBefore(prompt)) {
					return true;
				}
			}
			final DischargeSlot ds = row.getDischargeSlot();
			if (ds != null) {
				start = ds.getSchedulingTimeWindow().getStart().toLocalDate();
				end = ds.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
			if (start != null && end != null) {
				if (start.isAfter(today) && start.isBefore(prompt)) {
					return true;
				}
				if (end.isAfter(today) && end.isBefore(prompt)) {
					return true;
				}
			}

			return result;
		}

	}

}
