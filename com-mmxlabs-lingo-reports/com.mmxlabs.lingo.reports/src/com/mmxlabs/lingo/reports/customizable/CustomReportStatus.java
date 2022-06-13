package com.mmxlabs.lingo.reports.customizable;

public class CustomReportStatus {
	public StoreType storeType;

	public CustomReportDefinition report;

	public CustomReportDefinition checkpoint;

	public boolean newReport;
	public boolean saved;

	private CustomReportStatus() {
	}

	public void createCheckpoint() {
		checkpoint = report.copy();
	}

	public void revert() {
		report = checkpoint.copy();
	}

	/**
	 * Create a change record when definitions are first loaded from disk
	 * 
	 * @param storeType
	 * @param rpt
	 * @return
	 */
	public static CustomReportStatus clean(StoreType storeType, CustomReportDefinition rpt) {
		CustomReportStatus c = new CustomReportStatus();
		c.storeType = storeType;
		c.report = rpt;
		c.newReport = false;
		c.saved = true;
		
		c.createCheckpoint();
		return c;
	}

	/**
	 * Create a change record when a definition is first created
	 * 
	 * @param storeType
	 * @param rpt
	 * @return
	 */
	public static CustomReportStatus newReport(StoreType storeType, CustomReportDefinition rpt) {
		CustomReportStatus c = new CustomReportStatus();
		c.storeType = storeType;
		c.report = rpt;
		c.newReport = true;
		c.saved = false;
		
		c.createCheckpoint();
		
		return c;

	}

	/**
	 * Create a new change record when a definition is changed
	 * 
	 * @param storeType
	 * @param rpt
	 * @return
	 */
	public static CustomReportStatus change(CustomReportDefinition rpt, CustomReportStatus existing) {
		assert rpt == existing.report;
		CustomReportStatus c = new CustomReportStatus();
		c.storeType = existing.storeType;
		c.report = rpt;
		c.newReport = existing.newReport;
		c.saved = false;
		return c;
	}
}