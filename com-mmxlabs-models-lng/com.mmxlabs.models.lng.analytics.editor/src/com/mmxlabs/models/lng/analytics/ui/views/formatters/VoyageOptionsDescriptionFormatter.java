/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BaseCaseRowOptions;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRowOptions;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.IImageProvider;

public class VoyageOptionsDescriptionFormatter extends BaseFormatter implements IImageProvider {

	private static final String PLUGIN_ID = "com.mmxlabs.models.lng.analytics.editor";

	private static final String IMG_DATE = "icons:/icons/16x16/date.png";
	private static final String IMG_FUEL = "icons:/icons/16x16/fuel.png";
	private static final String IMG_CANAL_MIXED = "icons:/icons/16x16/canal_plain.png";
	private static final String IMG_CANAL_DIRECT = "icons:/icons/16x16/canal_plain.png";
	private static final String IMG_CANAL_SUEZ = "icons:/icons/16x16/canal_suez.png";
	private static final String IMG_CANAL_PANAMA = "icons:/icons/16x16/canal_panama.png";

	private static final int IDX_DATE = 0;
	private static final int IDX_FUEL = 1;
	private static final int IDX_CANAL = 2;
	private static final int NUM_IMAGES = 3;

	@Override
	public String render(final Object object) {
		return "";
	}

	@Override
	public Image getImage(Object element) {

		if (element instanceof final PartialCaseRow row) {
			element = row.getOptions();
		}
		if (element instanceof final BaseCaseRow row) {
			element = row.getOptions();
		}
		if (element instanceof final PartialCaseRowOptions rowOptions) {

			final String[] lhsImages = new String[NUM_IMAGES];
			final String[] rhsImages = new String[NUM_IMAGES];

			if (!rowOptions.getLoadDates().isEmpty()) {
				lhsImages[IDX_DATE] = IMG_DATE;
			}
			if (!rowOptions.getLadenFuelChoices().isEmpty()) {
				lhsImages[IDX_FUEL] = IMG_FUEL;
			}
			if (!rowOptions.getLadenRoutes().isEmpty()) {
				if (rowOptions.getLadenRoutes().size() == 1) {
					if (rowOptions.getLadenRoutes().get(0) == RouteOption.SUEZ) {
						lhsImages[IDX_CANAL] = IMG_CANAL_SUEZ;
					}
					if (rowOptions.getLadenRoutes().get(0) == RouteOption.PANAMA) {
						lhsImages[IDX_CANAL] = IMG_CANAL_PANAMA;
					}
					if (rowOptions.getLadenRoutes().get(0) == RouteOption.DIRECT) {
						lhsImages[IDX_CANAL] = IMG_CANAL_DIRECT;
					}
				} else {
					lhsImages[IDX_CANAL] = IMG_CANAL_MIXED;
				}

			}

			if (!rowOptions.getDischargeDates().isEmpty()) {
				rhsImages[IDX_DATE] = IMG_DATE;
			}
			if (!rowOptions.getBallastFuelChoices().isEmpty()) {
				rhsImages[IDX_FUEL] = IMG_FUEL;
			}
			if (!rowOptions.getBallastRoutes().isEmpty()) {
				if (rowOptions.getBallastRoutes().size() == 1) {
					if (rowOptions.getBallastRoutes().get(0) == RouteOption.SUEZ) {
						rhsImages[IDX_CANAL] = IMG_CANAL_SUEZ;
					}
					if (rowOptions.getBallastRoutes().get(0) == RouteOption.PANAMA) {
						rhsImages[IDX_CANAL] = IMG_CANAL_PANAMA;
					}
					if (rowOptions.getBallastRoutes().get(0) == RouteOption.DIRECT) {
						rhsImages[IDX_CANAL] = IMG_CANAL_DIRECT;
					}
				} else {
					rhsImages[IDX_CANAL] = IMG_CANAL_MIXED;
				}
			}

			return getImage(lhsImages, rhsImages);
		}
		if (element instanceof final BaseCaseRowOptions rowOptions) {

			final String[] lhsImages = new String[NUM_IMAGES];
			final String[] rhsImages = new String[NUM_IMAGES];

			if (rowOptions.isSetLoadDate()) {
				lhsImages[IDX_DATE] = IMG_DATE;
			}
			if (rowOptions.isSetLadenFuelChoice()) {
				lhsImages[IDX_FUEL] = IMG_FUEL;

			}
			if (rowOptions.isSetLadenRoute()) {
				if (rowOptions.getLadenRoute() == RouteOption.SUEZ) {
					lhsImages[IDX_CANAL] = IMG_CANAL_SUEZ;
				}
				if (rowOptions.getLadenRoute() == RouteOption.PANAMA) {
					lhsImages[IDX_CANAL] = IMG_CANAL_PANAMA;
				}
				if (rowOptions.getLadenRoute() == RouteOption.DIRECT) {
					lhsImages[IDX_CANAL] = IMG_CANAL_DIRECT;
				}
			}

			if (rowOptions.isSetDischargeDate()) {
				rhsImages[IDX_DATE] = IMG_DATE;
			}
			if (rowOptions.isSetBallastFuelChoice()) {
				rhsImages[IDX_FUEL] = IMG_FUEL;
			}
			if (rowOptions.isSetBallastRoute()) {
				if (rowOptions.getBallastRoute() == RouteOption.SUEZ) {
					rhsImages[IDX_CANAL] = IMG_CANAL_SUEZ;
				}
				if (rowOptions.getBallastRoute() == RouteOption.PANAMA) {
					rhsImages[IDX_CANAL] = IMG_CANAL_PANAMA;
				}
				if (rowOptions.getBallastRoute() == RouteOption.DIRECT) {
					rhsImages[IDX_CANAL] = IMG_CANAL_DIRECT;
				}
			}

			return getImage(lhsImages, rhsImages);
		}

		return null;
	}

	private @Nullable Image getImage(final String[] lhsImages, final String[] rhsImages) {

		// Define a key for this set of images
		final String imageName = "Buy" + String.join(",", lhsImages) + "Sell" + String.join(",", rhsImages);

		final Image cache = Activator.getDefault().getImageRegistry().get(imageName);
		if (cache != null) {
			return cache;
		}

		// Width of central dash
		final int dashWidth = 10;
		// padding either size of the dash
		final int dashSpacer = 4;

		final int paddingBetweenImages = 2;
		final int imageWidth = 16;

		// Calculate the size of the image
		int width = 0;
		// Add in LHS images. 16px per image and 2px between images.
		width += imageWidth * NUM_IMAGES;
		width += paddingBetweenImages * (NUM_IMAGES - 1);

		// Add width for spacer and padding
		width += 2 * dashSpacer;
		width += dashWidth;

		// Add in RHS images. 16px per image and 2px between images.
		width += imageWidth * NUM_IMAGES;
		// plus padding
		width += paddingBetweenImages * (NUM_IMAGES - 1);

		final Image image = new Image(Display.getDefault(), width, 16);
		final byte[] destArray = new byte[width * 16];

		final GC gc = new GC(image);
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);

		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		// gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));

		int xpos = 0;

		// Draw the LHS images
		for (int idx = 0; idx < lhsImages.length; ++idx) {
			if (idx > 0) {
				// Padding
				xpos += paddingBetweenImages;
			}
			final String p = lhsImages[idx];
			if (p != null) {
				final ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, p);
				final Image img = desc.createImage();
				gc.drawImage(img, xpos, 0);
				for (int i = 0; i < 16; ++i) {
					final byte[] srcArray = img.getImageData().alphaData;
					final int destPos = width * i + xpos;
					final int srcPos = img.getImageData().width * i;
					System.arraycopy(srcArray, srcPos, destArray, destPos, 16);
				}
				img.dispose();
			}
			xpos += 16;
		}

		// Add spacer before dash
		xpos += dashSpacer;

		// Draw the dash
		gc.setLineWidth(2);
		gc.drawLine(xpos, 8, xpos + dashWidth, 8);

		// Determine the dash alpha channel
		final byte[] d = image.getImageData().data;
		for (int x = 0; x < dashWidth; x++) {
			for (int y = 7; y < 9; y++) {
				final int destPos = width * y + xpos + x;
				//+3 here is because we are looking at alpha byte as in : RGBA
				final byte b = d[destPos * 4 + 3];
				destArray[destPos] = b;
			}
		}
		xpos += dashWidth;
		// Add spacer after dash
		xpos += dashSpacer;

		// Draw RHS images
		for (int idx = 0; idx < lhsImages.length; ++idx) {
			if (idx > 0) {
				xpos += paddingBetweenImages;
			}
			final String p = rhsImages[idx];
			if (p != null) {
				final ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, p);
				final Image img = desc.createImage();
				gc.drawImage(img, xpos, 0);
				for (int i = 0; i < 16; ++i) {
					final byte[] srcArray = img.getImageData().alphaData;
					final int destPos = width * i + xpos;
					final int srcPos = img.getImageData().width * i;
					System.arraycopy(srcArray, srcPos, destArray, destPos, 16);
				}
				img.dispose();
			}
			xpos += 16;
		}
		
		// Create a new image merging in the alpha channel
		final ImageData data = image.getImageData();
		data.alphaData = destArray;

		gc.dispose();

		image.dispose();

		final Image result = new Image(Display.getDefault(), data);
		Activator.getDefault().getImageRegistry().put(imageName, result);
		return result;
	}
}
