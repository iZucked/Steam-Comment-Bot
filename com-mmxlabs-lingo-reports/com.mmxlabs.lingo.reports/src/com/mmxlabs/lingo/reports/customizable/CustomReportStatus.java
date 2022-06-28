package com.mmxlabs.lingo.reports.customizable;

public class CustomReportStatus {
	private StoreType storeType;

	private CustomReportDefinition report;

	private CustomReportDefinition checkpoint;

	private boolean newReport;
	private boolean saved;

	private CustomReportStatus() {
	}

	public void createCheckpoint() {
		checkpoint = getReport().copy();
	}

	public void revert() {
		setReport(checkpoint.copy());
	}

	/**
	 * Create a change record when definitions are first loaded from disk
	 * 
	 * @param storeType
	 * @param rpt
	 * @return
	 */
	public static CustomReportStatus clean(final StoreType storeType, final CustomReportDefinition rpt) {
		final CustomReportStatus c = new CustomReportStatus();
		c.setStoreType(storeType);
		c.setReport(rpt);
		c.setNewReport(false);
		c.setSaved(true);
		
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
	public static CustomReportStatus forNewReport(final StoreType storeType, final CustomReportDefinition rpt) {
		final CustomReportStatus c = new CustomReportStatus();
		c.setStoreType(storeType);
		c.setReport(rpt);
		c.setNewReport(true);
		c.setSaved(false);
		
		c.createCheckpoint();
		
		return c;

	}

	public StoreType getStoreType() {
		return storeType;
	}

	public void setStoreType(final StoreType storeType) {
		this.storeType = storeType;
	}

	public boolean isNewReport() {
		return newReport;
	}

	public void setNewReport(final boolean newReport) {
		this.newReport = newReport;
	}

	public CustomReportDefinition getReport() {
		return report;
	}

	public void setReport(final CustomReportDefinition report) {
		this.report = report;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(final boolean saved) {
		this.saved = saved;
	}
}