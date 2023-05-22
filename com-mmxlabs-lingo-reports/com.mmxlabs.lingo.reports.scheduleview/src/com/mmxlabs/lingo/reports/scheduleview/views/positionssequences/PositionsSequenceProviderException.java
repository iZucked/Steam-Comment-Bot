package com.mmxlabs.lingo.reports.scheduleview.views.positionssequences;

public class PositionsSequenceProviderException extends Exception {

	private static final long serialVersionUID = 4098589416638136553L;
	
	private final String title;
	private final String description;
	
	public PositionsSequenceProviderException(final String title, final String description) {
		this.title = title;
		this.description = description;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
}
