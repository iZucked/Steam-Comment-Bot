/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.editor.displaycomposites;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

/**
 * Composite which is being shown on the dialog window
 * Allows editing the TransferAgreement instance
 * @author FM
 *
 */
public class TransferRecordDetailComposite extends DefaultDetailComposite implements IDisplayComposite {

	public TransferRecordDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	protected void sortEditors(List<IInlineEditor> editors) {
		// Sub classes can sort the editor list prior to rendering
		List<ETypedElement> orderedFeatures = Lists.newArrayList( //
				MMXCorePackage.Literals.NAMED_OBJECT__NAME, //
				TransfersPackage.Literals.TRANSFER_RECORD__TRANSFER_AGREEMENT,//
				
				TransfersPackage.Literals.TRANSFER_RECORD___GET_FROM_ENTITY,//
				TransfersPackage.Literals.TRANSFER_RECORD___GET_TO_ENTITY,//
				
				TransfersPackage.Literals.TRANSFER_RECORD__CARGO_RELEASE_DATE,//
				TransfersPackage.Literals.TRANSFER_RECORD___GET_COMPANY_STATUS, //
				
				TransfersPackage.Literals.TRANSFER_RECORD__PRICE_EXPRESSION,//
				TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS,//
				
				TransfersPackage.Literals.TRANSFER_RECORD__PRICING_DATE				
		);

		// Reverse the list so that we can move the editors to the head of the list
		Collections.reverse(orderedFeatures);
		for (var feature : orderedFeatures) {
			for (var editor : editors) {
				if (editor.getFeature() == feature) {
					editors.remove(editor);
					editors.add(0, editor);
					break;
				}
			}
		}

	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider() {

		return new RowGroupDisplayCompositeLayoutProviderBuilder() //
				.withRow() //
				.withFeature(MMXCorePackage.Literals.NAMED_OBJECT__NAME) //
				.makeRow() //
				//
				.withRow() //
				.withFeature(TransfersPackage.Literals.TRANSFER_RECORD__TRANSFER_AGREEMENT, "Agreement")
				.makeRow() //
				//
				.withRow() //
				.withFeature(TransfersPackage.Literals.TRANSFER_RECORD__CARGO_RELEASE_DATE) //
				.withFeature(TransfersPackage.Literals.TRANSFER_RECORD___GET_COMPANY_STATUS) //
				.makeRow() //
				//
				.withRow() //
				.withFeature(TransfersPackage.Literals.TRANSFER_RECORD___GET_FROM_ENTITY) //
				.withFeature(TransfersPackage.Literals.TRANSFER_RECORD___GET_TO_ENTITY) //
				.makeRow() //
				//
				.withRow() //
				.withFeature(TransfersPackage.Literals.TRANSFER_RECORD__PRICE_EXPRESSION) //
				.withFeature(TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS) //
				.makeRow() //
				//
				.withRow() //
				.withFeature(TransfersPackage.Literals.TRANSFER_RECORD__LHS) //
				.withFeature(TransfersPackage.Literals.TRANSFER_RECORD__RHS) //
				.makeRow() //
				//
				.withRow() //
				.withFeature(TransfersPackage.Literals.TRANSFER_RECORD__INCOTERM) //
				.withFeature(TransfersPackage.Literals.TRANSFER_RECORD__STATUS) //
				.makeRow() //
				.make() //
		;

	}

}
