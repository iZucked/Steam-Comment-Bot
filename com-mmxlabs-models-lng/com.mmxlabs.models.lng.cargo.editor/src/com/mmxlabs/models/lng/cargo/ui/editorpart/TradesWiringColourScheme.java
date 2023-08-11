package com.mmxlabs.models.lng.cargo.ui.editorpart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;

public class TradesWiringColourScheme {
	public TradesWiringColourScheme() {
		super();
	}

	private final Color invalidTerminalColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private final Color invalidWireColour = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
	private final Color darkGrey = new Color(Display.getDefault(), new RGB(64, 64, 64));
	private final Color rewirableColour = getDarkGrey();
	private final Color fixedWireColour = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	private final Color green = new Color(Display.getCurrent(), new RGB(0, 180, 50));
	private final Color lightGreen = new Color(Display.getCurrent(), new RGB(100, 255, 100));
	private final Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	private final Color lightGrey = new Color(Display.getCurrent(), new RGB(240, 240, 240));
	private final Color grey = new Color(Display.getCurrent(), new RGB(200, 200, 200));
	private final Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private final Color validTerminalColour = getLightGreen();

	public Color getDischargeTerminalColour(TradesRow tradesRow) {
		if (tradesRow.isDischargeTerminalValid()) {
			return getValidTerminalColour();
		} else {
			return getInvalidTerminalColour();
		}
	}

	public Color getLoadTerminalColour(TradesRow tradesRow) {
		if (tradesRow.isLoadTerminalValid()) {
			return getValidTerminalColour();
		} else {
			return getInvalidTerminalColour();
		}
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
