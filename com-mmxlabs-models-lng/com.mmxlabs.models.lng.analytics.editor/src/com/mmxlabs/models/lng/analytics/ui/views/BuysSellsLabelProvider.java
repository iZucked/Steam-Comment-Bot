package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
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
				if (
						(element instanceof BuyOpportunity && ((BuyOpportunity) element).isDesPurchase())
						|| (element instanceof BuyReference && ((BuyReference) element).getSlot().isDESPurchase())
					) {
					return imgDES;
				} else if ((element instanceof BuyMarket)) {
					if (((BuyMarket) element).getMarket() instanceof DESPurchaseMarket) {
						return imgSPOT_DES;
					}	else {
						return imgSPOT_FOB;
					}
				} else {
					return imgFOB;
				}
			} else if (element instanceof SellOption) {
				if (
						(element instanceof SellOpportunity && ((SellOpportunity) element).isFobSale())
						|| (element instanceof SellReference && ((SellReference) element).getSlot().isFOBSale())
						) {
					return imgFOB;
				} else if ((element instanceof SellMarket)) {
					if (((SellMarket) element).getMarket() instanceof FOBSalesMarket) {
						return imgSPOT_FOB;
					}	else {
						return imgSPOT_DES;
					}
				} else {
					return imgDES;
				}
			}
		}
		return null;
	}

	
	public void dispose() {
		super.dispose();
	}

}
