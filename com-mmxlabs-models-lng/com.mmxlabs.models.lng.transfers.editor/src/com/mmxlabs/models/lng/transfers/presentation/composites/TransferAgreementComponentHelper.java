/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.presentation.composites;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.PreferredPricingBasesWrapper;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class TransferAgreementComponentHelper extends DefaultComponentHelper {

	public TransferAgreementComponentHelper() {
		super(TransfersPackage.Literals.TRANSFER_AGREEMENT);
		
		ignoreFeatures.add(TransfersPackage.Literals.TRANSFER_AGREEMENT__PREFERRED_PBS);
	}
	
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
			final List<IInlineEditor> editors = new LinkedList<>();
			// Create a dummy implementation to gather the editors allowing changes to the
			// order before callign the real class.
			final IInlineEditorContainer container = editor -> {
				editors.add(editor);
				return editor;
			};
			addEditorsToComposite(container, targetClass);

			sortEditors(editors);

			editors.forEach(detailComposite::addInlineEditor);
			
			if (true) {
				TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
				b.withShowHeaders(true);
				b.withLabel("Preffered PBs");
				b.withContentProvider(new ArrayContentProvider());

				b.buildColumn("     Name     ", MMXCorePackage.Literals.NAMED_OBJECT__NAME) //
				.withWidth(100) //
				.withRMMaker((ed, rvp) -> new StringAttributeManipulator(MMXCorePackage.Literals.NAMED_OBJECT__NAME, ed)) //
				.build();

				// Add action
				b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
					if (input instanceof final TransferAgreement priceInfo) {
						final PreferredPricingBasesWrapper newPPBW = CommercialFactory.eINSTANCE.createPreferredPricingBasesWrapper();
						ch.handleCommand(AddCommand.create(ch.getEditingDomain(), priceInfo,  TransfersPackage.Literals.TRANSFER_AGREEMENT__PREFERRED_PBS, newPPBW), priceInfo,  TransfersPackage.Literals.TRANSFER_AGREEMENT__PREFERRED_PBS);
					}
				});
				// Delete action
				b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {
					if (input instanceof final TransferAgreement priceInfo) {
						if (sel instanceof final IStructuredSelection ss && !ss.isEmpty()) {
							ch.handleCommand(RemoveCommand.create(ch.getEditingDomain(), priceInfo, TransfersPackage.Literals.TRANSFER_AGREEMENT__PREFERRED_PBS, ss.getFirstElement()), priceInfo, TransfersPackage.Literals.TRANSFER_AGREEMENT__PREFERRED_PBS);
						}
					}
				}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

				detailComposite.addInlineEditor(b.build(TransfersPackage.Literals.TRANSFER_AGREEMENT__PREFERRED_PBS));
			}
	}
}
