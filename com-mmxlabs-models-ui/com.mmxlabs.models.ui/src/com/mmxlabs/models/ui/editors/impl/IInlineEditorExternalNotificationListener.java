/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

/**
 */
public interface IInlineEditorExternalNotificationListener {

	void notifyChanged(Notification notification);

	void postDisplay(IInlineEditor editor, final IDialogEditingContext context, final MMXRootObject scenario, final EObject object, final Collection<EObject> range);

}
