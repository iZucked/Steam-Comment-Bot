/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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

import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.ColumnFilters;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.cargo.util.CargoEditorFilterUtils;

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
				final Action currentAction = new Action("Today onwards") {
					@Override
					public void run() {
						activeFilters.add(new TimePeriodFilterHandler(CargoEditorFilterUtils.TimeFilterOption.CURRENT));
						viewer.getScenarioViewer().refresh();
					}
				};
				addActionToMenu(currentAction, menu);
				
				final Action promptAction = new Action("Prompt") {
					@Override
					public void run() {
						final Iterator<ITradesBasedFilterHandler> itr = activeFilters.iterator();
						while (itr.hasNext()) {
							final ITradesBasedFilterHandler filter = itr.next();
							if (filter instanceof TimePeriodFilterHandler) {
								itr.remove();
							}
						}
						activeFilters.add(new TimePeriodFilterHandler(CargoEditorFilterUtils.TimeFilterOption.PROMPT, 3));
						viewer.getScenarioViewer().refresh();
					}
				};
				addActionToMenu(promptAction, menu);

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
							activeFilters.add(new TimePeriodFilterHandler(CargoEditorFilterUtils.TimeFilterOption.YEARMONTH, ymc));
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
	
	private class TimePeriodFilterHandler implements ITradesBasedFilterHandler {
		
		private CargoEditorFilterUtils.TimeFilterOption option = CargoEditorFilterUtils.TimeFilterOption.NONE;
		private YearMonth choice = null;
		private int promptMonth = 3;

		public TimePeriodFilterHandler(CargoEditorFilterUtils.TimeFilterOption option) {
			this.option = option;
		}
		
		public TimePeriodFilterHandler(CargoEditorFilterUtils.TimeFilterOption option, YearMonth choice) {
			this.option = option;
			this.choice = choice;
		}
		
		public TimePeriodFilterHandler(CargoEditorFilterUtils.TimeFilterOption option, int promptMonth) {
			this.option = option;
			this.promptMonth = promptMonth;
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
			if (row != null) {
				return CargoEditorFilterUtils.timePeriodFilter(option, row.getLoadSlot(), row.getDischargeSlot(), choice, promptMonth);
			} else {
				return false;
			}
		}

		@Override
		public boolean isDefaultFilter() {
			return false;
		}
	}

}
