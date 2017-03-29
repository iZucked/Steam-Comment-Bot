/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
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
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;

/**
 * A Class to wrap {@link IInlineEditor}s which are part of a a Slot-Contract data structure. This handle the visibility of the control.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            The LNGPriceCalculatorParameters subclass
 * @param <U>
 *            The custom data structure class
 */
public class SlotContractExtensionWrapper<T extends LNGPriceCalculatorParameters, U extends EObject> extends IInlineEditorEnablementWrapper {
	private boolean enabled = false;
	private IDialogEditingContext dialogContext = null;
	private MMXRootObject scenario;
	private Collection<EObject> range = null;
	private final Class<T> paramsClass;
	private final Class<U> slotContractParamsClass;

	private Control control;

	public SlotContractExtensionWrapper(@NonNull final IInlineEditor wrapped, @NonNull final Class<T> paramsClass, @NonNull final Class<U> slotContractParamsClass) {
		super(wrapped);
		this.paramsClass = paramsClass;
		this.slotContractParamsClass = slotContractParamsClass;
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {

		final EObject object = (EObject) notification.getNotifier();
		if (notification.getFeature() == CargoPackage.eINSTANCE.getSlot_Contract()) {
			if (notification.getNotifier() instanceof Slot) {
				final Slot slot = (Slot) notification.getNotifier();
				enabled = false;
				if (notification.getNewValue() != null) {
					final Contract contract = (Contract) notification.getNewValue();
					if (paramsClass.isInstance(contract.getPriceInfo())) {

						// Scan through extensions and see if there is a current object to display
						for (final EObject r : slot.getExtensions()) {
							if (slotContractParamsClass.isInstance(r)) {
								enabled = true;
								dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
								dialogContext.getDialogController().updateEditorVisibility();
								super.display(dialogContext, scenario, r, range);
								getLabel().pack();
								dialogContext.getDialogController().relayout();
								return true;
							}
						}
					}
				}
				dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
				dialogContext.getDialogController().updateEditorVisibility();
				return true;
			}
		} else if (notification.getFeature() == MMXCorePackage.eINSTANCE.getMMXObject_Extensions()) {
			if (notification.getNotifier() instanceof Slot) {
				// If an instance of the slot specific code has just been added, then display it
				if (slotContractParamsClass.isInstance(notification.getNewValue())) {
					enabled = true;
					dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
					dialogContext.getDialogController().updateEditorVisibility();
					super.display(dialogContext, scenario, slotContractParamsClass.cast(notification.getNewValue()), range);
					getLabel().pack();
					dialogContext.getDialogController().relayout();
					return true;

				}
				enabled = false;
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
		if (object instanceof Slot) {
			final Slot slot = (Slot) object;
			if (slot.getContract() != null) {
				final Contract contract = slot.getContract();
				if (paramsClass.isInstance(contract.getPriceInfo())) {
					enabled = true;
				}
			}
		}

		if (enabled) {
			if (range != null) {
				for (final EObject r : range) {
					if (slotContractParamsClass.isInstance(r)) {
						dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
						super.display(dialogContext, scenario, r, range);
						getLabel().pack();
						return;
					}
				}
			}
			enabled = false;
			dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
			super.display(dialogContext, scenario, null, range);
		} else {
			enabled = false;
			dialogContext.getDialogController().setEditorVisibility(object, getFeature(), false);
			super.display(dialogContext, scenario, null, range);
		}
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		control = super.createControl(parent, dbc, toolkit);
		return control;
	}

	@Override
	public Object createLayoutData(MMXRootObject root, EObject value,
			Control control) {
		// TODO Auto-generated method stub
		return null;
	}
}