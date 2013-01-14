/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import org.eclipse.emf.common.notify.Notification;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.HeelOptions;
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
		Integer vol = options.getVolumeAvailable();
		return vol > 0;
	}
	
	
}
