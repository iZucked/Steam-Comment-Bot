/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.license.features.LicenseFeatures;
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
import com.mmxlabs.rcp.common.dnd.BasicDragSource;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

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
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		final MMXCorePackage mmx = MMXCorePackage.eINSTANCE;
		final SpotMarketsPackage pp = SpotMarketsPackage.eINSTANCE;

		CommercialPackage ppp = CommercialPackage.eINSTANCE;

		final IReferenceValueProviderProvider provider = getReferenceValueProviderCache();

		addTypicalColumn("Name", new BasicAttributeManipulator(mmx.getNamedObject_Name(), getCommandHandler()));
		addTypicalColumn("Active", new BooleanAttributeManipulator(pp.getSpotMarket_Enabled(), getCommandHandler()));
		switch (spotType) {
		case DES_PURCHASE:
			addTypicalColumn("Price", new BasicAttributeManipulator(ppp.getDateShiftExpressionPriceParameters_PriceExpression(), getCommandHandler()), pp.getSpotMarket_PriceInfo());
			addTypicalColumn("#/month", new NumericAttributeManipulator(pp.getSpotAvailability_Constant(), getCommandHandler()), pp.getSpotMarket_Availability());
			addTypicalColumn("CV", new BasicAttributeManipulator(pp.getDESPurchaseMarket_Cv(), getCommandHandler()));
			break;
		case DES_SALE:
			addTypicalColumn("Price", new BasicAttributeManipulator(ppp.getDateShiftExpressionPriceParameters_PriceExpression(), getCommandHandler()), pp.getSpotMarket_PriceInfo());
			addTypicalColumn("#/month", new NumericAttributeManipulator(pp.getSpotAvailability_Constant(), getCommandHandler()), pp.getSpotMarket_Availability());
			addTypicalColumn("Port", new SingleReferenceManipulator(pp.getDESSalesMarket_NotionalPort(), provider, getCommandHandler()));
			break;
		case FOB_PURCHASE:
			addTypicalColumn("Price", new BasicAttributeManipulator(ppp.getDateShiftExpressionPriceParameters_PriceExpression(), getCommandHandler()), pp.getSpotMarket_PriceInfo());
			addTypicalColumn("#/month", new NumericAttributeManipulator(pp.getSpotAvailability_Constant(), getCommandHandler()), pp.getSpotMarket_Availability());
			addTypicalColumn("CV", new NumericAttributeManipulator(pp.getFOBPurchasesMarket_Cv(), getCommandHandler()));
			addTypicalColumn("Port", new SingleReferenceManipulator(pp.getFOBPurchasesMarket_NotionalPort(), provider, getCommandHandler()));
			break;
		case FOB_SALE:
			addTypicalColumn("Price", new BasicAttributeManipulator(ppp.getDateShiftExpressionPriceParameters_PriceExpression(), getCommandHandler()), pp.getSpotMarket_PriceInfo());
			addTypicalColumn("#/month", new NumericAttributeManipulator(pp.getSpotAvailability_Constant(), getCommandHandler()), pp.getSpotMarket_Availability());
			addTypicalColumn("Ports", new MultipleReferenceManipulator(pp.getFOBSalesMarket_OriginPorts(), provider, getCommandHandler(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));
			break;
		}

		// Currently only needed for break-even table
		if (LicenseFeatures.isPermitted("features:break-even-table")) {
			final DragSource source = new DragSource(getScenarioViewer().getControl(), DND.DROP_MOVE);
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			source.setTransfer(types);
			source.addDragListener(new BasicDragSource(viewer));
		}

	}

	@Override
	public void defaultSetTitle(final String string) {
		super.defaultSetTitle(string);
	}
}
