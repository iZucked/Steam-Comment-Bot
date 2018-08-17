/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

/**
 * @author Simon Goodall
 * 
 */
public class ContractDetailComposite extends DefaultDetailComposite {

	private final boolean topOfPane;

	public ContractDetailComposite(final Composite parent, final int style, final boolean top, final FormToolkit toolkit) {
		super(parent, style, toolkit);

		this.topOfPane = top;

	}

	@Override
	protected void setDefaultHelpContext(EObject object) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, "com.mmxlabs.lingo.doc.DataModel_lng_commercial_Contract");
	}

	@Override
	public IInlineEditor addInlineEditor(final IInlineEditor editor) {

		// By default all elements are in the main tab
		boolean topOfPaneElement = true;

		// Here the exceptions are listed for the elements which should go into the bottom
		// Here the exceptions are listed for the elements which should go into the bottom
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedListsArePermissive()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedContracts()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedPorts()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_MaxCvValue()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_MinCvValue()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getPurchaseContract_SalesDeliveryType()) {
			topOfPaneElement = false;
		}
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_PurchaseDeliveryType()) {
			topOfPaneElement = false;
		}

		// Do not add elements if they are for the wrong section.
		if (topOfPane != topOfPaneElement) {
			// Rejected...
			return null;
		}

		return super.addInlineEditor(editor);
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				return new GridLayout(8, false);
			}

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {

				// Special case for min/max volumes - ensure text box has enough width for around 7 digits.
				// Note: Should really render the font to get width - this is ok on my system, but other systems (default font & size, resolution, dpi etc) could make this wrong
				final EStructuralFeature feature = editor.getFeature();
				if (feature == CommercialPackage.Literals.CONTRACT__MAX_QUANTITY || feature == CommercialPackage.Literals.CONTRACT__MIN_QUANTITY
						|| feature == CommercialPackage.Literals.CONTRACT__VOLUME_LIMITS_UNIT || feature == CommercialPackage.Literals.CONTRACT__OPERATIONAL_TOLERANCE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					if (feature == CommercialPackage.Literals.CONTRACT__MAX_QUANTITY || feature == CommercialPackage.Literals.CONTRACT__MIN_QUANTITY) {
						gd.widthHint = 80;
					}

					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == CommercialPackage.Literals.CONTRACT__MIN_QUANTITY) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Volume");
						}
						editor.setLabel(null);
					} else {
						editor.setLabel(null);
					}
					return gd;
				}
				if (feature == CommercialPackage.Literals.CONTRACT__COUNTERPARTY || feature == CommercialPackage.Literals.CONTRACT__CN) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.horizontalSpan = 3;

					return gd;
				}

				GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
				gd.horizontalSpan = 7;
				return gd;
			}
		};
	}
}
