/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

public class ReferenceVesselInlineEditor extends IInlineEditorEnablementWrapper {
	
	private boolean enabled = false;
	private IDialogEditingContext dialogContext = null;
	private MMXRootObject scenario;
	private Collection<EObject> range = null;
	
	public ReferenceVesselInlineEditor(IInlineEditor wrapped) {
		super(wrapped);
	}
	
	@Override
	protected boolean respondToNotification(final Notification notification) {
		if (notification.getFeature() == FleetPackage.eINSTANCE.getVessel_ReferenceVessel()) {
			if (notification.getNotifier() == input) {
				final Vessel vessel = (Vessel) input;
				enabled = !vessel.isMmxReference() && !notification.getNewBooleanValue();
				if (!enabled) {
					vessel.setReference(null);
				}
			}
			super.display(dialogContext, scenario, input, range);
			return true;
		}
		return false;
	}

	@Override
	protected boolean isEnabled() {
		return enabled;
	}
	
	@Override 
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		enabled = false;
		this.dialogContext = dialogContext;
		this.scenario = scenario;
		this.range = range;
		if (object instanceof Vessel) {
			final Vessel vessel = (Vessel) object;
			enabled = !vessel.isMmxReference() && !vessel.isReferenceVessel();
		}
		super.display(dialogContext, scenario, object, range);
	}
	
}
