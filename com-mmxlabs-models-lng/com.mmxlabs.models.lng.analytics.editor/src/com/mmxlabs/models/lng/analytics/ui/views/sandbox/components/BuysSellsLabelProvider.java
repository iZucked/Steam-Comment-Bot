/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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

import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.DefaultSandboxLabelProvider;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class BuysSellsLabelProvider extends DefaultSandboxLabelProvider {

	public BuysSellsLabelProvider(SandboxUIHelper sandboxUIHelper, final ICellRenderer renderer, Map<Object, IStatus> validationErrors, String name, final ETypedElement... pathObjects) {
		super(sandboxUIHelper, renderer, validationErrors, name, pathObjects);
	}

	public BuysSellsLabelProvider(SandboxUIHelper sandboxUIHelper, ICellRenderer renderer, Map<Object, IStatus> validationErrors, String name, @Nullable EMFPath path) {
		super(sandboxUIHelper, renderer, validationErrors, name, path);
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
		if (element instanceof BuyOption) {
			BuyOption option = (BuyOption) element;
			boolean isDES = AnalyticsBuilder.isDESPurchase().test(option);
			boolean isSpot = AnalyticsBuilder.isSpot(option);
			if (isSpot) {
				return isDES ? sandboxUIHelper.imgSPOT_DES : sandboxUIHelper.imgSPOT_FOB;
			} else {
				return isDES ? sandboxUIHelper.imgDES : sandboxUIHelper.imgFOB;
			}
		} else if (element instanceof SellOption) {
			SellOption option = (SellOption) element;
			boolean isDES = AnalyticsBuilder.isDESSale().test(option);
			boolean isSpot = AnalyticsBuilder.isSpot(option);
			if (isSpot) {
				return isDES ? sandboxUIHelper.imgSPOT_DES : sandboxUIHelper.imgSPOT_FOB;
			} else {
				return isDES ? sandboxUIHelper.imgDES : sandboxUIHelper.imgFOB;
			}
		}
		return null;
	}
}
