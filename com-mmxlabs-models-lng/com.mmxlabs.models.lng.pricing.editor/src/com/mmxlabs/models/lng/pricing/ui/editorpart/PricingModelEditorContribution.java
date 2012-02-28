/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Collections;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

public class PricingModelEditorContribution implements IJointModelEditorContribution {
	private UUIDObject modelObject;
	private JointModelEditorPart editorPart;

	@Override
	public void init(JointModelEditorPart editorPart, MMXRootObject rootObject,
			UUIDObject modelObject) {
		this.editorPart = editorPart;
		this.modelObject = modelObject;
	}

	@Override
	public void addPages(final Composite parent) {
		addIndexPage(parent);
		addMarketPage(parent);
		addPricingPage(parent);
	}

	private void addPricingPage(final Composite parent) {
		
	}

	private void addMarketPage(final Composite parent) {
		
	}

	private void addIndexPage(final Composite parent) {
		final IndexEditorPane pane = new IndexEditorPane(editorPart.getSite().getPage(), editorPart);
		pane.createControl(parent);
		pane.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_CommodityIndices()), editorPart.getAdapterFactory());
		pane.getViewer().setInput(modelObject);
		
		int page = editorPart.addPage(pane.getControl());
		editorPart.setPageText(page, "Indices");
	}

}
