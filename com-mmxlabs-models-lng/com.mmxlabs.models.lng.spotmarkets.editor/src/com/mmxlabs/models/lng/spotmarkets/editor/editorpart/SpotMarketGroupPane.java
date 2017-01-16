/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.editorpart;

import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

/**
 * Spot Market Group
 * 
 * @author Simon Goodall
 * 
 */
public class SpotMarketGroupPane extends ScenarioTableViewerPane {

	private final SpotType spotType;

	public SpotMarketGroupPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars, final SpotType spotType) {
		super(page, part, location, actionBars);
		this.spotType = spotType;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);

		final MMXCorePackage mmx = MMXCorePackage.eINSTANCE;
		final SpotMarketsPackage pp = SpotMarketsPackage.eINSTANCE;

		CommercialPackage ppp = CommercialPackage.eINSTANCE;

		final IReferenceValueProviderProvider provider = getReferenceValueProviderCache();

		addTypicalColumn("Name", new BasicAttributeManipulator(mmx.getNamedObject_Name(), getEditingDomain()));
		addTypicalColumn("Enabled", new BooleanAttributeManipulator(pp.getSpotMarket_Enabled(), getEditingDomain()));
		switch (spotType) {
		case DES_PURCHASE:
			addTypicalColumn("Count", new NumericAttributeManipulator(pp.getSpotAvailability_Constant(), getEditingDomain()), pp.getSpotMarket_Availability());
			addTypicalColumn("CV", new BasicAttributeManipulator(pp.getDESPurchaseMarket_Cv(), getEditingDomain()));
			addTypicalColumn("Price", new BasicAttributeManipulator(ppp.getExpressionPriceParameters_PriceExpression(), getEditingDomain()), pp.getSpotMarket_PriceInfo());
			break;
		case DES_SALE:
			addTypicalColumn("Price", new BasicAttributeManipulator(ppp.getExpressionPriceParameters_PriceExpression(), getEditingDomain()), pp.getSpotMarket_PriceInfo());
			addTypicalColumn("Count", new NumericAttributeManipulator(pp.getSpotAvailability_Constant(), getEditingDomain()), pp.getSpotMarket_Availability());
			addTypicalColumn("Port", new SingleReferenceManipulator(pp.getDESSalesMarket_NotionalPort(), provider, getEditingDomain()));
			break;
		case FOB_PURCHASE:
			addTypicalColumn("Count", new BasicAttributeManipulator(pp.getSpotAvailability_Constant(), getEditingDomain()), pp.getSpotMarket_Availability());
			addTypicalColumn("Price", new BasicAttributeManipulator(ppp.getExpressionPriceParameters_PriceExpression(), getEditingDomain()), pp.getSpotMarket_PriceInfo());
			addTypicalColumn("CV", new NumericAttributeManipulator(pp.getFOBPurchasesMarket_Cv(), getEditingDomain()));
			addTypicalColumn("Port", new SingleReferenceManipulator(pp.getFOBPurchasesMarket_NotionalPort(), provider, getEditingDomain()));
			break;
		case FOB_SALE:
			addTypicalColumn("Price", new BasicAttributeManipulator(ppp.getExpressionPriceParameters_PriceExpression(), getEditingDomain()), pp.getSpotMarket_PriceInfo());
			addTypicalColumn("Count", new NumericAttributeManipulator(pp.getSpotAvailability_Constant(), getEditingDomain()), pp.getSpotMarket_Availability());
			addTypicalColumn("Ports", new MultipleReferenceManipulator(pp.getFOBSalesMarket_OriginPorts(), provider, getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));
			break;
		}
	}

	@Override
	public void defaultSetTitle(final String string) {
		super.defaultSetTitle(string);
	}
}
