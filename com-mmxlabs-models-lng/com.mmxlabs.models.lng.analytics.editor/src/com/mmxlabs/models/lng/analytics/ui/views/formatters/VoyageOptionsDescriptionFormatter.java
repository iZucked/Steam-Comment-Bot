/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.util.LinkedList;
import java.util.List;

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

	private static final String LBL_BUY_SIDE = "Buy:";
	private static final String LBL_SELL_SIDE = "Sell:";

	private static final String PLUGIN_ID = "com.mmxlabs.models.lng.analytics.editor";

	private static final String IMG_DATE = "/icons/voyageopts/date.png";
	private static final String IMG_FUEL = "/icons/voyageopts/fuel.png";
	private static final String IMG_CANAL_MIXED = "/icons/voyageopts/canal-plain.png";
	private static final String IMG_CANAL_DIRECT = "/icons/voyageopts/canal-plain.png";
	private static final String IMG_CANAL_SUEZ = "/icons/voyageopts/canal-suez.png";
	private static final String IMG_CANAL_PANAMA = "/icons/voyageopts/canal-panama.png";

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

			final List<String> lhsImages = new LinkedList<>();
			final List<String> rhsImages = new LinkedList<>();

			if (!rowOptions.getLoadDates().isEmpty()) {
				lhsImages.add(IMG_DATE);
			}
			if (!rowOptions.getLadenFuelChoices().isEmpty()) {
				lhsImages.add(IMG_FUEL);

			}
			if (!rowOptions.getLadenRoutes().isEmpty()) {
				if (rowOptions.getLadenRoutes().size() == 1) {
					if (rowOptions.getLadenRoutes().get(0) == RouteOption.SUEZ) {
						lhsImages.add(IMG_CANAL_SUEZ);
					}
					if (rowOptions.getLadenRoutes().get(0) == RouteOption.PANAMA) {
						lhsImages.add(IMG_CANAL_PANAMA);
					}
					if (rowOptions.getLadenRoutes().get(0) == RouteOption.DIRECT) {
						lhsImages.add(IMG_CANAL_DIRECT);
					}
				} else {
					lhsImages.add(IMG_CANAL_MIXED);
				}

			}

			if (!rowOptions.getDischargeDates().isEmpty()) {
				rhsImages.add(IMG_DATE);
			}
			if (!rowOptions.getBallastFuelChoices().isEmpty()) {
				rhsImages.add(IMG_FUEL);
			}
			if (!rowOptions.getBallastRoutes().isEmpty()) {
				if (rowOptions.getBallastRoutes().size() == 1) {
					if (rowOptions.getBallastRoutes().get(0) == RouteOption.SUEZ) {
						rhsImages.add(IMG_CANAL_SUEZ);
					}
					if (rowOptions.getBallastRoutes().get(0) == RouteOption.PANAMA) {
						rhsImages.add(IMG_CANAL_PANAMA);
					}
					if (rowOptions.getBallastRoutes().get(0) == RouteOption.DIRECT) {
						rhsImages.add(IMG_CANAL_DIRECT);
					}
				} else {
					rhsImages.add(IMG_CANAL_MIXED);
				}
			}

			return getImage(lhsImages, rhsImages);
		}
		if (element instanceof final BaseCaseRowOptions rowOptions) {

			final List<String> lhsImages = new LinkedList<>();
			final List<String> rhsImages = new LinkedList<>();

			if (rowOptions.isSetLoadDate()) {
				lhsImages.add(IMG_DATE);
			}
			if (rowOptions.isSetLadenFuelChoice()) {
				lhsImages.add(IMG_FUEL);

			}
			if (rowOptions.isSetLadenRoute()) {
				if (rowOptions.getLadenRoute() == RouteOption.SUEZ) {
					lhsImages.add(IMG_CANAL_SUEZ);
				}
				if (rowOptions.getLadenRoute() == RouteOption.PANAMA) {
					lhsImages.add(IMG_CANAL_PANAMA);
				}
				if (rowOptions.getLadenRoute() == RouteOption.DIRECT) {
					lhsImages.add(IMG_CANAL_DIRECT);
				}
			}

			if (rowOptions.isSetDischargeDate()) {
				rhsImages.add(IMG_DATE);
			}
			if (rowOptions.isSetBallastFuelChoice()) {
				rhsImages.add(IMG_FUEL);
			}
			if (rowOptions.isSetBallastRoute()) {
				if (rowOptions.getBallastRoute() == RouteOption.SUEZ) {
					rhsImages.add(IMG_CANAL_SUEZ);
				}
				if (rowOptions.getBallastRoute() == RouteOption.PANAMA) {
					rhsImages.add(IMG_CANAL_PANAMA);
				}
				if (rowOptions.getBallastRoute() == RouteOption.DIRECT) {
					rhsImages.add(IMG_CANAL_DIRECT);
				}
			}

			return getImage(lhsImages, rhsImages);
		}

		return null;
	}

	private @Nullable Image getImage(final List<String> lhsImages, final List<String> rhsImages) {

		if (lhsImages.isEmpty() && rhsImages.isEmpty()) {
			return null;
		}

		final String imageName = "Buy" + String.join(",", lhsImages) + "Sell" + String.join(",", rhsImages);

		final Image cache = Activator.getDefault().getImageRegistry().get(imageName);
		if (cache != null) {
			return cache;
		}

		final Image imageGCImg = new Image(Display.getDefault(), 16, 16);
		final GC tempGC = new GC(imageGCImg);
		tempGC.setAdvanced(true);
		tempGC.setAntialias(SWT.ON);

		int width = 0;
		boolean withSpacer = false;
		if (!lhsImages.isEmpty() && !rhsImages.isEmpty()) {
			width += 8;
			withSpacer = true;
		}
		final int buyWidth = tempGC.textExtent(LBL_BUY_SIDE).x;
		if (lhsImages.size() == 1) {
			width += buyWidth;
			width += 16;
		} else if (lhsImages.size() > 1) {
			width += buyWidth;
			// Add image width
			width += 16 * lhsImages.size();
			// plus padding
			width += 2 * (lhsImages.size() - 1);
		}
		final int sellWidth = tempGC.textExtent(LBL_SELL_SIDE).x;
		if (rhsImages.size() == 1) {
			width += sellWidth;
			width += 16;
		} else if (rhsImages.size() > 1) {
			width += sellWidth;
			// Add image width
			width += 16 * rhsImages.size();
			// plus padding
			width += 2 * (rhsImages.size() - 1);
		}
		tempGC.dispose();
		imageGCImg.dispose();

		final Image image = new Image(Display.getDefault(), width, 16);
		final byte[] destArray = new byte[width * 16];

		final GC gc = new GC(image);
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);

		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		// gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));

		int xpos = 0;
		boolean first = true;
		for (final String p : lhsImages) {
			if (first) {
				first = false;
				gc.drawString(LBL_BUY_SIDE, xpos, 0, true);

				final byte[] d = image.getImageData().data;

				for (int x = 0; x < buyWidth; x++) {
					for (int y = 0; y < 16; y++) {
						final int destPos = width * y + xpos + x;
						final byte b = d[destPos * 4 + 3];
						destArray[destPos] = b;
					}
				}
				xpos += buyWidth;
			} else {
				xpos += 2;
			}
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

			xpos += 16;
		}
		if (withSpacer) {
			xpos += 8;
		}

		first = true;
		for (final String p : rhsImages) {
			if (first) {
				first = false;
				gc.drawString(LBL_SELL_SIDE, xpos, 0, true);

				final byte[] d = image.getImageData().data;

				for (int x = 0; x < sellWidth; x++) {
					for (int y = 0; y < 16; y++) {
						final int destPos = width * y + xpos + x;
						final byte b = d[destPos * 4 + 3];
						destArray[destPos] = b;
					}
				}

				xpos += sellWidth;
			} else {
				xpos += 2;
			}
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

			xpos += 16;
		}

		final ImageData data = image.getImageData();
		data.alphaData = destArray;

		gc.dispose();

		image.dispose();

		final Image result = new Image(Display.getDefault(), data);
		Activator.getDefault().getImageRegistry().put(imageName, result);
		return result;
	}
}
