package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

/**
 * @generated NOT
 */
public class SlotInlineEditorWrapper extends IInlineEditorEnablementWrapper {

	public SlotInlineEditorWrapper(final IInlineEditor wrapped) {
		super(wrapped);
	}

	@Override
	public void notifyChanged(Notification notification) {
		// TODO Auto-generated method stub
		super.notifyChanged(notification);
	}
	
	@Override
	protected void missedNotifications(List<Notification> missed) {
		for (Notification n : missed) {
			reallyNotifyChanged(n);
		}
		super.missedNotifications(missed);
	}
	
	@Override
	public void reallyNotifyChanged(final Notification notification) {
		if (notification.getFeature() == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase()) {

			if (input instanceof LoadSlot) {
				setEnabled(!((LoadSlot) input).isDESPurchase());
			}

		}
		if (notification.getFeature() == CargoPackage.eINSTANCE.getDischargeSlot_FOBSale()) {
			if (input instanceof DischargeSlot) {
				setEnabled(!((DischargeSlot) input).isFOBSale());
			}
		}
	}
	
	@Override
	public void display(final IScenarioEditingLocation location, MMXRootObject scenario, EObject object, Collection<EObject> range) {

		super.display(location, scenario, object, range);
		
		if (input instanceof LoadSlot) {
			setEnabled(!((LoadSlot) input).isDESPurchase());
		}
		else if (input instanceof DischargeSlot) {
			setEnabled(!((DischargeSlot) input).isFOBSale());
		}
	}

	@Override
	public EObject getEditorTarget() {
		return wrapped.getEditorTarget();
	}
}