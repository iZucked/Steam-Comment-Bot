/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class MarketIndexViewerPane extends ScenarioTableViewerPane {

	private final IScenarioEditingLocation jointModelEditor;

	public MarketIndexViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), jointModelEditor.getEditingDomain()));
		
		final IReferenceValueProviderProvider irvpp = getReferenceValueProviderCache();
		addTypicalColumn("Calendar", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getMarketIndex_PricingCalendar(), irvpp, getEditingDomain()));
		addTypicalColumn("Holidays", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getMarketIndex_SettleCalendar(), irvpp, getEditingDomain()));
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS)) {
			addTypicalColumn("Bid curve", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getMarketIndex_BidCurve(), irvpp, getEditingDomain()));
			addTypicalColumn("Offer curve", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getMarketIndex_OfferCurve(), irvpp, getEditingDomain()));
		}
		
		setTitle("Indices", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}
}
