package com.mmxlabs.lingo.reports.emissions.cii.managers;

import java.time.Year;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

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
	
	@Override
	public @Nullable Color getForeground(final CIIGradesData data) {
		final String grade = data.getGrades().get(year);
		if (grade != null && (grade.equals("D") || grade.equals("E"))) {
			return Display.getDefault().getSystemColor(SWT.COLOR_RED);
		}
		return null;
	}
}
