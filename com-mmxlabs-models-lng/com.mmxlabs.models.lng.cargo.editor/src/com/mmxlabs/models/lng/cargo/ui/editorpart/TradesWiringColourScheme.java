package com.mmxlabs.models.lng.cargo.ui.editorpart;

import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;

public class TradesWiringColourScheme {
	public TradesWiringColourScheme(LocalResourceManager lrm) {;
		darkGrey = lrm.createColor(DARK_GREY_RGB);
		green = lrm.createColor(GREEN_RGB);
		lightGreen = lrm.createColor(LIGHT_GREEN_RGB);
		lightGrey = lrm.createColor(LIGHT_GREY_RGB);
		grey = lrm.createColor(GREY_RGB);
		rewirableColour = getDarkGrey();
		validTerminalColour = getLightGreen();
	}
	
	
	private final Color invalidTerminalColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private final Color invalidWireColour = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
	private final Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private final Color fixedWireColour = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	private final Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	
	private Color darkGrey;
	private Color green;
	private Color lightGreen;
	private Color lightGrey;
	private Color grey;
	
	private static final RGB DARK_GREY_RGB = new RGB(64, 64, 64);
	private static final RGB GREEN_RGB = new RGB(0, 180, 50);
	private static final RGB LIGHT_GREEN_RGB = new RGB(100, 255, 100);
	private static final RGB LIGHT_GREY_RGB = new RGB(240, 240, 240);
	private static final RGB GREY_RGB = new RGB(200, 200, 200);
	
	private Color rewirableColour = getDarkGrey();
	private Color validTerminalColour = getLightGreen();

	public Color getDischargeTerminalColour(TradesRow tradesRow) {
		return getTerminalColour(tradesRow.isDischargeTerminalValid());
	}

	public Color getLoadTerminalColour(TradesRow tradesRow) {
		return getTerminalColour(tradesRow.isLoadTerminalValid());
	}

	public Color getTerminalColour(boolean valid) {
		if (valid) {
			return getValidTerminalColour();
		} else {
			return getInvalidTerminalColour();
		}
	}

	public Color getSystemWhite() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	}

	public Color getSystemBlack() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	}
	
	public Color getSystemDarkRed() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);
	}
	
	public Color getSystemDarkGrey() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
	}
	
	public Color getSystemInfoBackgroundColour() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
	}
	
	public Color getSystemInfoBackgroundColour(Display display) {
		return display.getCurrent().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
	}

	public Color getValidTerminalColour() {
		return validTerminalColour;
	}

	public Color getBlack() {
		return black;
	}

	public Color getInvalidTerminalColour() {
		return invalidTerminalColour;
	}

	public Color getInvalidWireColour() {
		return invalidWireColour;
	}

	public Color getDarkGrey() {
		return darkGrey;
	}

	public Color getRewirableColour() {
		return rewirableColour;
	}

	public Color getFixedWireColour() {
		return fixedWireColour;
	}

	public Color getGreen() {
		return green;
	}

	public Color getLightGreen() {
		return lightGreen;
	}

	public Color getWhite() {
		return white;
	}

	public Color getLightGrey() {
		return lightGrey;
	}

	public Color getGrey() {
		return grey;
	}
}
