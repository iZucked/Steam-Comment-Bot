/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.ui.tabular.manipulators.BooleanFlagAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class MarketIndexViewerPane extends ScenarioTableViewerPane {

	public MarketIndexViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getCommandHandler()));

		final IReferenceValueProviderProvider irvpp = getReferenceValueProviderCache();
		addTypicalColumn("Calendar", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getMarketIndex_PricingCalendar(), irvpp, getCommandHandler()));
		addTypicalColumn("Holidays", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getMarketIndex_SettleCalendar(), irvpp, getCommandHandler()));
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS)) {
			addTypicalColumn("Auto-Hedge", new BooleanFlagAttributeManipulator(PricingPackage.eINSTANCE.getMarketIndex_AutoHedgeEnabled(), getCommandHandler()));
			addTypicalColumn("Flat curve", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getMarketIndex_FlatCurve(), irvpp, getCommandHandler()));
			addTypicalColumn("Bid curve", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getMarketIndex_BidCurve(), irvpp, getCommandHandler()));
			addTypicalColumn("Offer curve", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getMarketIndex_OfferCurve(), irvpp, getCommandHandler()));
		}

		setTitle("Indices", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}
}
