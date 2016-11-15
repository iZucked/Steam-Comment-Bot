package com.mmxlabs.models.lng.cargo.editor;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

public class VesselAvailabilityDetailWrapper extends IInlineEditorEnablementWrapper {
	private boolean enabled = false;
	private IDialogEditingContext dialogContext = null;
	private MMXRootObject scenario;
	private Collection<EObject> range = null;

	private Control control;

	public VesselAvailabilityDetailWrapper(@NonNull final IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {

		final EObject object = (EObject) notification.getNotifier();
		if (notification.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_Optional()) {
			if (notification.getNotifier() == input) {
				enabled = false;
				if (notification.getNewBooleanValue()) {
					enabled = true;
					dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
					dialogContext.getDialogController().updateEditorVisibility();
					super.display(dialogContext, scenario, input, range);
					getLabel().pack();
					return true;
				}
				dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
				dialogContext.getDialogController().updateEditorVisibility();
				return true;
			}

		}

		return false;
	}

	@Override
	protected boolean isEnabled() {
		return enabled;
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {

		this.dialogContext = dialogContext;
		this.scenario = scenario;
		this.range = range;

		enabled = false;
		if (object instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) object;
			if (vesselAvailability.isOptional()) {
				enabled = true;
				dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
				super.display(dialogContext, scenario, object, range);
				getLabel().pack();
				return;
			}
		}

		enabled = false;
		dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
		super.display(dialogContext, scenario, object, range);
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		control = super.createControl(parent, dbc, toolkit);
		return control;
	}

	@Override
	public Object createLayoutData(MMXRootObject root, EObject value, Control control) {
		// TODO Auto-generated method stub
		return null;
	}

}
