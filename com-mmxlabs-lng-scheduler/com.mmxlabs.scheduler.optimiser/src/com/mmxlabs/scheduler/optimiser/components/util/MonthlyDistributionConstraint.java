package com.mmxlabs.scheduler.optimiser.components.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MonthlyDistributionConstraint {
	public class Row {
		private Set<Integer> months = new HashSet<>();
		private Integer min;
		private Integer max;
		
		public Row(Collection<Integer> months, Integer min, Integer max) {
			this.min = min;
			this.max = max;
			this.months.addAll(months);
		}

		public Integer getMin() {
			return min;
		}

		public Integer getMax() {
			return max;
		}

		public void setMax(Integer max) {
			this.max = max;
		}

		public List<Integer> getMonths() {
			return months.stream()
					.sorted()
					.collect(Collectors.toList());
		}

	}
	
	List<Row> rows = new LinkedList<>();
	
	public void addRow(Collection<Integer> months, Integer min, Integer max) {
		rows.add(new Row(months, min, max));
	}
	
	public List<Row> getRows() {
		return rows;
	}
}