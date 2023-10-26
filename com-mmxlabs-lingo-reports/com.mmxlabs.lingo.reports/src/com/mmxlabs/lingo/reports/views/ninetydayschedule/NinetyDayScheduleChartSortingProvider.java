/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.Comparator;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.InventoryEvents;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.util.CombinedSequence;
import com.mmxlabs.models.lng.schedule.util.PositionsSequence;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRowPriorityType;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleChartSortingProvider;

public class NinetyDayScheduleChartSortingProvider implements IScheduleChartSortingProvider {

	public enum Mode {
		STACK("Stack", "Rows are grouped by scenario"), //
		INTERLEAVE("Interleave", "Row are grouped by name");

		private final String displayName;
		private final String tooltip;

		private Mode(final String displayName, String tooltip) {
			this.displayName = displayName;
			this.tooltip = tooltip;

		}

		public final String getDisplayName() {
			return displayName;
		}

		public final String getTooltip() {
			return tooltip;
		}
	}

	public enum Category {
		BY_NAME("By name", "Rows are sorted by name, grouped by non-shipped, fleet and market"), //
		BY_SIZE("By size", "Rows are sorted by capacity then name, grouped by non-shipped, fleet and market");

		private final String displayName;
		private final String tooltip;

		private Category(final String displayName, String tooltip) {
			this.displayName = displayName;
			this.tooltip = tooltip;
		}

		public final String getDisplayName() {
			return displayName;
		}

		public final String getTooltip() {
			return tooltip;
		}
	}
	
	private enum Type {
		INVENTORY, DES, FOB, FLEET, CHARTER
	}
	
	private Mode mode = Mode.INTERLEAVE;
	private Category category = Category.BY_NAME;
	
	
	private Type getSequenceType(final Object obj) {
		if (obj instanceof CombinedSequence) {
			return Type.FLEET;
		} else if (obj instanceof final Sequence s) {
			if (s.isFleetVessel()) {
				return Type.FLEET;
			} else if (s.isSetCharterInMarket()) {
				return Type.CHARTER;
			} else if (s.getName().contains("DES")) {
				return Type.DES;
			} else if (s.getName().contains("FOB")) {
				return Type.FOB;
			}
		} else if (obj instanceof InventoryEvents) {
			return Type.INVENTORY;
		} else if (obj instanceof PositionsSequence p) {
			return Type.DES;
//			return p.isBuy() ? Type.DES : Type.FOB;
		}
		return Type.FLEET;
	}
	
	private int getSequenceCapacity(final Object obj) {
		if (obj instanceof final CombinedSequence combinedSequence) {
			final Vessel vessel = combinedSequence.getVessel();
			if (vessel != null) {
				return vessel.getVesselOrDelegateCapacity();
			}
		} else if (obj instanceof final Sequence s) {
			Vessel vessel = null;
			if (s.getVesselCharter() != null) {
				vessel = s.getVesselCharter().getVessel();
			} else if (s.getCharterInMarket() != null) {
				vessel = s.getCharterInMarket().getVessel();
			}

			if (vessel != null) {
				return vessel.getVesselOrDelegateCapacity();
			}

		}
		return 0;
	}
	
	/**
	 * Compares chart rows to decide which row should come first on the chart.
	 * 
	 * @implNote it might be a good idea to compare by scenario name first
	 * in case of a pin diff mode. Otherwise rows will not be grouped by scenario. not implemented yet.
	 */
	@Override
	public Comparator<ScheduleChartRow> getComparator() {
		return (firstRow, secondRow) -> {
			
			if(mode == Mode.STACK) {
				final String scenario1 = firstRow.getScenarioName();
				final String scenario2 = secondRow.getScenarioName();
				
				// Group By scenario
				if(!scenario1.equals(scenario2)) {
					return scenario1.compareTo(scenario2);
				}
				
				// Group by fleet/spot
				final Type s1Type = getSequenceType(firstRow.getKey().getData());
				final Type s2Type = getSequenceType(secondRow.getKey().getData());

				if (s1Type != s2Type) {
					return s1Type.ordinal() - s2Type.ordinal();
				}
				
				if (category == Category.BY_SIZE) {

					// Sort by capacity
					final int s1Value = getSequenceCapacity(firstRow.getKey().getData());
					final int s2Value = getSequenceCapacity(secondRow.getKey().getData());

					final int c = -Integer.compare(s1Value, s2Value);
					if (c != 0) {
						return c;
					}

				}

				if (category == Category.BY_NAME)
				{

					// Sort by name
					final String s1Name = firstRow.getName();
					final String s2Name = secondRow.getName();

					final int c = s1Name.compareTo(s2Name);
					if (c != 0) {
						return c;
					}

				}
			} else if (mode == Mode.INTERLEAVE) {
				// Group by fleet/spot
				final Type s1Type = getSequenceType(firstRow.getKey().getData());
				final Type s2Type = getSequenceType(secondRow.getKey().getData());

				if (s1Type != s2Type) {
					return s1Type.ordinal() - s2Type.ordinal();
				}
				
				if (category == Category.BY_SIZE) {

					// Sort by capacity
					final int s1Value = getSequenceCapacity(firstRow.getKey().getData());
					final int s2Value = getSequenceCapacity(secondRow.getKey().getData());

					final int c = -Integer.compare(s1Value, s2Value);
					if (c != 0) {
						return c;
					}

				}

				if (category == Category.BY_NAME)
				{

					// Sort by name
					final String s1Name;
					if(!firstRow.getRowType().equals(ScheduleChartRowPriorityType.REGULAR_ROWS)) {
						s1Name = "";
					} else {
						s1Name = firstRow.getName();
					}
					 
					final String s2Name;
					if(!secondRow.getRowType().equals(ScheduleChartRowPriorityType.REGULAR_ROWS)) {
						s2Name = "";
					} else {
						s2Name = secondRow.getName();
					}

					final int c = s1Name.compareTo(s2Name);
					if (c != 0) {
						return c;
					}

				}
				
				final String scenario1 = firstRow.getScenarioName();
				final String scenario2 = secondRow.getScenarioName();
				
				// Group By scenario
				if(!scenario1.equals(scenario2)) {
					return scenario1.compareTo(scenario2);
				} 
				
				
			}
			// Sort positions sequences by partition description first and buy/sell later (pairs buys and sells)
			if (firstRow.getKey().getData() instanceof PositionsSequence ps1 && secondRow.getKey().getData() instanceof PositionsSequence ps2) {
				int result = Boolean.compare(!ps1.isBuy(), !ps2.isBuy());
				
				if(result != 0) {
					return result;
				}
			}
			
			
			final int priorityComparison = 
					Integer.compare(firstRow.getRowType().getPriority(), secondRow.getRowType().getPriority());
			
			if (priorityComparison != 0) {
				return priorityComparison;
			}
			
			return firstRow.getName().compareTo(secondRow.getName());
		};
	}
	
	public Mode getMode() {
		return mode;
	}


	public void setMode(Mode mode) {
		this.mode = mode;
	}


	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}

}
