package org.eclipse.nebula.widgets.ganttchart.label;

import org.eclipse.swt.SWT;

public enum EEventLabelAlignment {
	LEFT(SWT.LEFT), CENTRE(SWT.CENTER), RIGHT(SWT.RIGHT);

	private final int swtConstant;

	private EEventLabelAlignment(final int swtConstant) {
		this.swtConstant = swtConstant;
	}

	public int getSwtConstant() {
		return swtConstant;
	}
}
