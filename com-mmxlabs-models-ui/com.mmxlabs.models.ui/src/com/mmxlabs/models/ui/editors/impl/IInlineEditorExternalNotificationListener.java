/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IInlineEditor;

/**
 * @since 2.0
 */
public interface IInlineEditorExternalNotificationListener {

	void notifyChanged(Notification notification);

	void postDisplay(IInlineEditor editor, final IScenarioEditingLocation location, final MMXRootObject scenario, final EObject object, final Collection<EObject> range);

}
