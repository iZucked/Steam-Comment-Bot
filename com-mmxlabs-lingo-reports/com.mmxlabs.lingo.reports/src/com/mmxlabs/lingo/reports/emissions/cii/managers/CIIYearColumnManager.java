package com.mmxlabs.lingo.reports.emissions.cii.managers;

import java.time.Year;

import com.mmxlabs.lingo.reports.emissions.cii.CIIGradesData;

public class CIIYearColumnManager extends AbstractCIIColumnManager {
	
	private final Year year;

	public CIIYearColumnManager(Year year) {
		super(year.toString());
		this.year = year;
	}

	@Override
	public String getColumnText(final CIIGradesData data) {
		final String grade = data.getGrades().get(year);
		if (grade == null) {
			return "-";
		}
		return grade;
	}
}
