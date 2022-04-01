/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.DefaultSandboxLabelProvider;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.SandboxUIHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class OptionsTreeViewerLabelProvider extends DefaultSandboxLabelProvider implements IFontProvider {
	private final OptionModellerView optionModellerView;
	// Font boldFont = createFont(true);
	// Font normalFont = createFont(false);

	public OptionsTreeViewerLabelProvider(final SandboxUIHelper sandboxUIHelper, final ICellRenderer renderer, final Map<Object, IStatus> validationErrors, final String name,
			final OptionModellerView optionModellerView, final ETypedElement... pathObjects) {
		super(sandboxUIHelper, renderer, validationErrors, name, pathObjects);
		this.optionModellerView = optionModellerView;
	}

	// private Font createFont(boolean bold) {
	// final Font systemFont = Display.getDefault().getSystemFont();
	// // Clone the font data
	// final FontData fd = new FontData(systemFont.getFontData()[0].toString());
	// // Set the bold bit.
	// if (bold) {
	// fd.setStyle(fd.getStyle() | SWT.BOLD);
	// } else {
	// fd.setStyle(fd.getStyle());
	// }
	// return new Font(Display.getDefault(), fd);
	// }

	public OptionsTreeViewerLabelProvider(final SandboxUIHelper sandboxUIHelper, final ICellRenderer renderer, final Map<Object, IStatus> validationErrors, final String name,
			final OptionModellerView optionModellerView, @Nullable final EMFPath path) {
		super(sandboxUIHelper, renderer, validationErrors, name, path);
		this.optionModellerView = optionModellerView;
	}

	@Override
	public Font getFont(final Object object) {
		if (object instanceof OptionAnalysisModel) {
			return (object == optionModellerView.getModel() ? sandboxUIHelper.boldFont : sandboxUIHelper.normalFont);
		}
		return sandboxUIHelper.normalFont;
	}

	@Override
	protected @Nullable Image getImage(@NonNull final ViewerCell cell, @Nullable final Object element) {

		if (validationErrors.containsKey(element)) {
			final IStatus status = validationErrors.get(element);
			final Image img = sandboxUIHelper.getValidationImageForStatus(status);
			if (img != null) {
				return img;
			}
		}
		if (element instanceof RoundTripShippingOption) {
			return sandboxUIHelper.imgShippingRoundTrip;
		} else if (element instanceof SimpleVesselCharterOption) {
			return sandboxUIHelper.imgShippingFleet;
		} else if (element instanceof OptionAnalysisModel && element == optionModellerView.getModel()) {
			return sandboxUIHelper.imgModel;
		}
		return null;
	}

	@Override
	protected void setFont(final ViewerCell cell, final Object element) {
		cell.setFont(getFont(element));
	}

}
