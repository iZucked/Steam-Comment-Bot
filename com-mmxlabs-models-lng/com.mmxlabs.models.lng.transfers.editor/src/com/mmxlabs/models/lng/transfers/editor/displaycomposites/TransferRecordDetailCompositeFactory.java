/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.editor.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

/**
 * Factory for {@link TransferAgreementDetailComposite}.
 * 
 * @author FM
 * 
 */
public class TransferRecordDetailCompositeFactory extends DefaultDisplayCompositeFactory {

	@Override
	public IDisplayComposite createSublevelComposite(Composite parent, EClass eClass, IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new TransferAgreementDetailComposite(parent, SWT.NONE, toolkit);
	}
}
