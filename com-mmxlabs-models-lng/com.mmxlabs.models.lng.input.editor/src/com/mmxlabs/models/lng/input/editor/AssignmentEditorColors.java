package com.mmxlabs.models.lng.input.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class AssignmentEditorColors {
	public final Color selectedTaskGradientTop;
	public final Color selectedTaskGradientBottom;
	public final Color taskGradientTop;
	public final Color taskGradientBottom;
	public final Color backgroundColor;
	public final Color taskLabelTextColor;
	public final Color resourceLabelTextColor;
	public final Color dividerColor;

	public final Color lockedTaskGradientTop;
	public final Color lockedTaskGradientBottom;
	public final Color rowColorOne;
	public final Color rowColorTwo;
	
	public AssignmentEditorColors(final Display display) {
		rowColorTwo = new Color(display, 230,239,249);
		rowColorOne = new Color(display, 255,255,255);

		backgroundColor = rowColorOne;//display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		taskLabelTextColor = display.getSystemColor(SWT.COLOR_WHITE);
		resourceLabelTextColor = display.getSystemColor(SWT.COLOR_BLACK);
		dividerColor = display.getSystemColor(SWT.COLOR_BLACK);

		selectedTaskGradientBottom = display.getSystemColor(SWT.COLOR_DARK_GREEN);
		selectedTaskGradientTop = display.getSystemColor(SWT.COLOR_GREEN);

		taskGradientBottom = display.getSystemColor(SWT.COLOR_DARK_BLUE);
		taskGradientTop = display.getSystemColor(SWT.COLOR_BLUE);

		lockedTaskGradientBottom = display.getSystemColor(SWT.COLOR_DARK_RED);
		lockedTaskGradientTop = display.getSystemColor(SWT.COLOR_RED);
	}
	
	public void dispose() {
		selectedTaskGradientTop.dispose();
		selectedTaskGradientBottom.dispose();
		taskGradientTop.dispose();
		taskGradientBottom.dispose();
		backgroundColor.dispose();
		taskLabelTextColor.dispose();
		resourceLabelTextColor.dispose();
		dividerColor.dispose();

		lockedTaskGradientTop.dispose();
		lockedTaskGradientBottom.dispose();
		
		rowColorOne.dispose();
		rowColorTwo.dispose();
	}
}
