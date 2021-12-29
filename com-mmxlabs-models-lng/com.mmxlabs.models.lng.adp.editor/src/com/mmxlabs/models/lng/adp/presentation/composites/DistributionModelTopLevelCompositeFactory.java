/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class DistributionModelTopLevelCompositeFactory extends DefaultDisplayCompositeFactory {

	@Override
	public IDisplayComposite createSublevelComposite(Composite parent, EClass eClass, IDialogEditingContext dialogContext, FormToolkit toolkit) {
		final DefaultDetailComposite s = (DefaultDetailComposite) super.createSublevelComposite(parent, eClass, dialogContext, toolkit);
		s.setLayoutProvider(new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(@NonNull MMXRootObject root, EObject value) {
				return GridLayoutFactory.swtDefaults().numColumns(3).create();
			}
//			@Override
//			public boolean showLabelFor(@NonNull MMXRootObject root, EObject value, @NonNull IInlineEditor editor) {
//				
//				final EStructuralFeature feature = editor.getFeature();
//				if (feature == ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_UNIT) {
//					return false;
//				}
//				return super.showLabelFor(root, value, editor);
//			}
//			@Override
//			public Object createLabelLayoutData(@NonNull MMXRootObject root, EObject value, @NonNull IInlineEditor editor, Control control, Label label) {
//
//				final EStructuralFeature feature = editor.getFeature();
//				if (feature == ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_UNIT) {
//					return GridDataFactory.swtDefaults().exclude(true).create();
//				}
//				return super.createLabelLayoutData(root, value, editor, control, label);
//			}

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {

//				final EStructuralFeature feature = editor.getFeature();
//				if (feature == ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_PER_CARGO || feature == ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_PER_CARGO_MIN) { // || feature == ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_UNIT) {
//					final GridData gd = GridDataFactory.swtDefaults().hint(80, SWT.DEFAULT).create();
//					// 64 - magic constant from MultiDetailDialog
//					// gd.widthHint = 80;
//
//					// FIXME: Hack pending proper APi to manipulate labels
//					final Label label = editor.getLabel();
//					if (feature == ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_PER_CARGO_MIN) {
//						if (label != null) {
//							label.setText("Volume");
//						}
//						// Disables text overwrite later
//						editor.setLabel(null);
//					} else if (feature == ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_PER_CARGO){
//						editor.setLabel(null);
//					}
//					else {
//						if (label != null) {
//							// Exclude from layout
////							label.setLayoutData(GridDataFactory.swtDefaults().exclude(true).create());
//						}
//						editor.setLabel(null);
//					}
//					return gd;
//				}
				return GridDataFactory.swtDefaults().span(2, 1).grab(true, false).create();
			}
		});
		return s;
	}

}
