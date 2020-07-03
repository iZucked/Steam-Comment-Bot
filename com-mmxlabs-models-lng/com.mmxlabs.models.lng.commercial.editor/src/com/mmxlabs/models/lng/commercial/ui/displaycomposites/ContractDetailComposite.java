/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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

	private final ContractDetailGroup contractDetailGroup;
	
	public enum ContractDetailGroup{
		GENERAL, RESTRICTIONS
	}

	public ContractDetailComposite(final Composite parent, final int style, final ContractDetailGroup contractDetailGroup, final FormToolkit toolkit) {
		super(parent, style, toolkit);
		this.contractDetailGroup = contractDetailGroup;
	}

	@Override
	protected void setDefaultHelpContext(EObject object) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, "com.mmxlabs.lingo.doc.DataModel_lng_commercial_Contract");
	}

	@Override
	public IInlineEditor addInlineEditor(final IInlineEditor editor) {

		// By default all elements are in the main tab
		ContractDetailGroup cdg = ContractDetailGroup.GENERAL;

		// Here the exceptions are listed for the elements which should go into the bottom
		if (editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedContracts()
				|| editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedPorts()
				|| editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedVessels()
				|| editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedContractsArePermissive()
				|| editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedPortsArePermissive()
				|| editor.getFeature() == CommercialPackage.eINSTANCE.getContract_RestrictedVesselsArePermissive()
				|| editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_MaxCvValue()
				|| editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_MinCvValue()
				|| editor.getFeature() == CommercialPackage.eINSTANCE.getPurchaseContract_SalesDeliveryType()
				|| editor.getFeature() == CommercialPackage.eINSTANCE.getSalesContract_PurchaseDeliveryType()) {
			cdg = ContractDetailGroup.RESTRICTIONS;
		}
	
		// Do not add elements if they are for the wrong section.
		if (contractDetailGroup != cdg){
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
				return new GridLayout(10, false);
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
				if (feature == CommercialPackage.Literals.CONTRACT__FULL_CARGO_LOT) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					gd.horizontalSpan = 1;
					return gd;
				}
				if (feature == CommercialPackage.Literals.CONTRACT__START_DATE || feature == CommercialPackage.Literals.CONTRACT__END_DATE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					if (feature == CommercialPackage.Literals.CONTRACT__START_DATE) {
						gd.widthHint = 80;
					}

					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == CommercialPackage.Literals.CONTRACT__START_DATE) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Contract dates");
						}
						editor.setLabel(null);
					} else {
						editor.setLabel(null);
					}
					gd.horizontalSpan = 2;
					return gd;
				}
				if (feature == CommercialPackage.Literals.CONTRACT__CONTRACT_YEAR_START) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					gd.horizontalSpan = 3;
					return gd;
				}
				
				
				if (feature == CommercialPackage.Literals.CONTRACT__COUNTERPARTY) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.horizontalSpan = 4;

					return gd;
				}
				if (feature == CommercialPackage.Literals.CONTRACT__CN) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.horizontalSpan = 4;

					return gd;
				}

				if (feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS 
						|| feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.horizontalSpan = 4;
					if (feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Contracts");
						}
						editor.setLabel(null);
					} else if (feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE){
						editor.setLabel(null);
					}else {
						editor.setLabel(null);
					}
					return gd;
				}
				
				if (feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS 
						|| feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.horizontalSpan = 4;
					if (feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Ports");
						}
						editor.setLabel(null);
					} else if (feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE){
						editor.setLabel(null);
					}else {
						editor.setLabel(null);
					}
					return gd;
				}
				if (feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_VESSELS 
						|| feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.horizontalSpan = 4;
					if (feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_VESSELS) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Vessels");
						}
						editor.setLabel(null);
					} else if (feature == CommercialPackage.Literals.CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE){
						editor.setLabel(null);
					}else {
						editor.setLabel(null);
					}
					return gd;
				}
				
				GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
				gd.horizontalSpan = 9;
				return gd;
			}
		};
	}
}
