package org.eclipse.nebula.widgets.ganttchart;

public enum EventLabelFontSize {
	SMALL(12),
	MEDIUM(16),
	LARGE(20);

	/*
	 * Preferably should be a number that is divisible by 4
	 */
	private int inPixels;

	EventLabelFontSize(int fontHeightInPixels) {
		this.inPixels = fontHeightInPixels;
	}
	
	public int getFontHeightInPixels() {
		return inPixels;
	}
}