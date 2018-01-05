/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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

import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class OptionsTreeViewerLabelProvider extends DefaultSandboxLabelProvider implements IFontProvider {
	private OptionModellerView optionModellerView;
	Font boldFont = createFont(true);
	Font normalFont = createFont(false);
	

	public OptionsTreeViewerLabelProvider(final ICellRenderer renderer, Map<Object, IStatus> validationErrors, String name, OptionModellerView optionModellerView, final ETypedElement... pathObjects) {
		super(renderer, validationErrors, name, pathObjects);
		this.optionModellerView = optionModellerView;
	}
	
	private Font createFont(boolean bold) {
		final Font systemFont = Display.getDefault().getSystemFont();
		// Clone the font data
		final FontData fd = new FontData(systemFont.getFontData()[0].toString());
		// Set the bold bit.
		if (bold) {
		fd.setStyle(fd.getStyle() | SWT.BOLD);
		} else {
			fd.setStyle(fd.getStyle());
		}
		return new Font(Display.getDefault(), fd);
	}

	public OptionsTreeViewerLabelProvider(ICellRenderer renderer, Map<Object, IStatus> validationErrors, String name, OptionModellerView optionModellerView, @Nullable EMFPath path) {
		super(renderer, validationErrors, name, path);
		this.optionModellerView = optionModellerView;
	}

	@Override
	public Font getFont(Object object) {
		if (object instanceof OptionAnalysisModel) {
			return (object == optionModellerView.getModel() ? boldFont : normalFont);
		}
		return normalFont;
	}
	
	@Override
	protected @Nullable Image getImage(@NonNull final ViewerCell cell, @Nullable final Object element) {

		if (validationErrors.containsKey(element)) {
			final IStatus status = validationErrors.get(element);
			if (!status.isOK()) {
				if (status.matches(IStatus.ERROR)) {
					return imgError;
				}
				if (status.matches(IStatus.WARNING)) {
					return imgWarn;
				}
				if (status.matches(IStatus.INFO)) {
					return imgWarn;
				}
			}
		} else {
			if (element instanceof RoundTripShippingOption) {
				return imgShippingRoundTrip;
			} else if (element instanceof FleetShippingOption) {
				return imgShippingFleet;
			} else if (element instanceof OptionAnalysisModel && element == optionModellerView.getModel()) {
				return imgModel;
			}
		}
		return null;
	}

	
	@Override
	protected void setFont(ViewerCell cell, Object element) {
		cell.setFont(getFont(element));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		normalFont.dispose();
		boldFont.dispose();
	}

}
