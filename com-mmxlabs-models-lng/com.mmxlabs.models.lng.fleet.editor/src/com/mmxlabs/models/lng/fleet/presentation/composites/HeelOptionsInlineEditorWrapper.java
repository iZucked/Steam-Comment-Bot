/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

/**
 * @generated NOT
 */
public class HeelOptionsInlineEditorWrapper extends IInlineEditorEnablementWrapper {

	public HeelOptionsInlineEditorWrapper(IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean respondToNotification(Notification notification) {
		Object feature = notification.getFeature();
		return feature == FleetPackage.eINSTANCE.getHeelOptions_VolumeAvailable();
	}

	@Override
	protected boolean isEnabled() {
		HeelOptions options = (HeelOptions) input;
		Double vol = options.getVolumeAvailable();
		return vol > 0.0;
	}

	@Override
	public Object createLayoutData(MMXRootObject root, EObject value,
			Control control) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
