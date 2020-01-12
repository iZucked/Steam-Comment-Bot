/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package summary;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */


import java.time.LocalDate;
import java.util.List;

/**
 * @author josephpallamidessi
 *
 */
public class KSummaryModel {
	List<KSummaryReportModel> summaryData;
	LocalDate adpCalendarStart;
	LocalDate adpCalendarEnd;
	LocalDate yearlyCalendarStart;
	LocalDate yearlyCalendarEnd;
	
	public List<KSummaryReportModel> getSummaryData() {
		return summaryData;
	}
	public void setSummaryData(List<KSummaryReportModel> summaryData) {
		this.summaryData = summaryData;
	}
	public LocalDate getAdpCalendarStart() {
		return adpCalendarStart;
	}
	public void setAdpCalendarStart(LocalDate adpCalendarStart) {
		this.adpCalendarStart = adpCalendarStart;
	}
	public LocalDate getAdpCalendarEnd() {
		return adpCalendarEnd;
	}
	public void setAdpCalendarEnd(LocalDate adpCalendarEnd) {
		this.adpCalendarEnd = adpCalendarEnd;
	}
	public LocalDate getYearlyCalendarStart() {
		return yearlyCalendarStart;
	}
	public void setYearlyCalendarStart(LocalDate yearlyCalendarStart) {
		this.yearlyCalendarStart = yearlyCalendarStart;
	}
	public LocalDate getYearlyCalendarEnd() {
		return yearlyCalendarEnd;
	}
	public void setYearlyCalendarEnd(LocalDate yearlyCalendarEnd) {
		this.yearlyCalendarEnd = yearlyCalendarEnd;
	}
	
}
