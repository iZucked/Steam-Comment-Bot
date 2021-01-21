/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.components;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.DefaultSandboxLabelProvider;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class BuysSellsLabelProvider extends DefaultSandboxLabelProvider {
	Image imgFOB = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "/icons/FOB.png").createImage();
	Image imgDES = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "/icons/DES.png").createImage();
	Image imgSPOT_FOB = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "/icons/SPOT_FOB.png").createImage();
	Image imgSPOT_DES = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "/icons/SPOT_DES.png").createImage();

	public BuysSellsLabelProvider(final ICellRenderer renderer, Map<Object, IStatus> validationErrors, String name, final ETypedElement... pathObjects) {
		super(renderer, validationErrors, name, pathObjects);
	}
	
	public BuysSellsLabelProvider(ICellRenderer renderer, Map<Object, IStatus> validationErrors, String name, @Nullable EMFPath path) {
		super(renderer, validationErrors, name, path);
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
			
			if (element instanceof BuyOption) {
				BuyOption option = (BuyOption) element;
				boolean isDES = AnalyticsBuilder.isDESPurchase().test(option);
				boolean isSpot = AnalyticsBuilder.isSpot(option);
				if (isSpot) {
					return isDES ? imgSPOT_DES : imgSPOT_FOB;
				} else {
					return isDES ? imgDES : imgFOB;
				}
			} else if (element instanceof SellOption) {
				SellOption option = (SellOption) element;
				boolean isDES = AnalyticsBuilder.isDESSale().test(option);
				boolean isSpot = AnalyticsBuilder.isSpot(option);
				if (isSpot) {
					return isDES ? imgSPOT_DES : imgSPOT_FOB;
				} else {
					return isDES ? imgDES : imgFOB;
				}
			}
		}
		return null;
	}

	
	@Override
	public void dispose() {
		super.dispose();
		imgFOB.dispose();
		imgDES.dispose();
		imgSPOT_DES.dispose();
		imgSPOT_FOB.dispose();
	}

}
