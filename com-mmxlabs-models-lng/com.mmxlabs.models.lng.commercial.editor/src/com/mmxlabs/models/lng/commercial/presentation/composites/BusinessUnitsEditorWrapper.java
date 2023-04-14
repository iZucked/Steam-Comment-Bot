/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

public class BusinessUnitsEditorWrapper extends IInlineEditorEnablementWrapper {

	private boolean wrapperEnabled = false;
	
	public BusinessUnitsEditorWrapper(IInlineEditor wrapped) {
		super(wrapped);
	}
	
	@Override
	protected boolean isEnabled() {
		return wrapperEnabled;
	}
	
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		wrapperEnabled = false;
		BaseLegalEntity output = null;
		if (object instanceof final BaseLegalEntity entity && !entity.isThirdParty()) {
			wrapperEnabled = true;
			output = entity;
		}
		super.display(dialogContext, scenario, output, range);
		dialogContext.getDialogController().setEditorVisibility(object, getFeature(), wrapperEnabled);
	}
	
	@Override
	public Object createLayoutData(MMXRootObject root, EObject value, Control control) {
		return null;
	}
	
	@Override
	protected boolean respondToNotification(final Notification notification) {
		return true;
	}

}
