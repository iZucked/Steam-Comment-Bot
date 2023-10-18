/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public abstract class DrawableCheckboxButton extends DrawableElement {
	private static Image CHECKED = null;
	private static Image UNCHECKED = null;
	
	private boolean isChecked;
	private Control control;

	public DrawableCheckboxButton(Control control, boolean isChecked) {
		this.isChecked = isChecked;
		this.control = control;
	}
	
	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		Image img = getImage();

		if (img != null) {
			Point p = calculateXY(bounds, img.getBounds());
			return List.of(new BasicDrawableElements.Image(img, p.x, p.y - 1));
		}

		return Collections.emptyList();
	}
	
	private static Point calculateXY(Rectangle bounds, Rectangle imgBounds) {
		Rectangle bs = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
		bs.width /= 2;
		bs.width -= imgBounds.width / 2;
		bs.height /= 2;
		bs.height -= imgBounds.height / 2;

		int x = bs.width > 0 ? bs.x + bs.width : bs.x;
		int y = bs.height > 0 ? bs.y + bs.height : bs.y;
		return new Point(x, y);
	}

	private Image getImage() {
		if (isChecked) {
			if (CHECKED == null) {
				CHECKED = makeShot(control, true);
			}
			return CHECKED;
		} else {
			if (UNCHECKED == null) {
				UNCHECKED = makeShot(control, false);
			}
			return UNCHECKED;
		}
	}

	private static Image makeShot(Control control, boolean isChecked) {
		// TODO Auto-generated method stub
		// Hopefully no platform uses exactly this color because we'll make
		// it transparent in the image.
		Color greenScreen = new Color(control.getDisplay(), 222, 223, 224);

		Shell shell = new Shell(control.getShell(), SWT.NO_TRIM);

		// otherwise we have a default gray color
		shell.setBackground(greenScreen);

		if (Util.isMac()) {
			Button button2 = new Button(shell, SWT.CHECK);
			Point bsize = button2.computeSize(SWT.DEFAULT, SWT.DEFAULT);

			// otherwise an image is stretched by width
			bsize.x = Math.max(bsize.x - 1, bsize.y - 1);
			bsize.y = Math.max(bsize.x - 1, bsize.y - 1);
			button2.setSize(bsize);
			button2.setLocation(100, 100);
		}

		Button button = new Button(shell, SWT.CHECK);
		button.setBackground(greenScreen);
		button.setSelection(isChecked);

		// otherwise an image is located in a corner
		button.setLocation(1, 1);
		Point bsize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		// otherwise an image is stretched by width
		bsize.x = Math.max(bsize.x - 1, bsize.y - 1);
		bsize.y = Math.max(bsize.x - 1, bsize.y - 1);
		button.setSize(bsize);

		shell.setSize(bsize);

		shell.open();

		GC gc = new GC(shell);
		Image image = new Image(control.getDisplay(), bsize.x, bsize.y);
		gc.copyArea(image, 0, 0);
		gc.dispose();
		shell.close();

		ImageData imageData = image.getImageData();
		imageData.transparentPixel = imageData.palette.getPixel(greenScreen
				.getRGB());

		Image img = new Image(control.getDisplay(), imageData);
		image.dispose();

		return img;
	}
	
	public boolean getChecked() {
		return isChecked;
	}
	
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		checkAction();

	}
	
	protected abstract void checkAction();

}
