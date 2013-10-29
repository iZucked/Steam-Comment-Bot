package com.mmxlabs.models.lng.cargo.editor;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IInlineEditor;
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
	private IScenarioEditingLocation location = null;
	private MMXRootObject scenario;
	private Collection<EObject> range = null;
	private final Class<T> paramsClass;
	private final Class<U> slotContractParamsClass;

	public SlotContractExtensionWrapper(@NonNull final IInlineEditor wrapped, @NonNull final Class<T> paramsClass, @NonNull final Class<U> slotContractParamsClass) {
		super(wrapped);
		this.paramsClass = paramsClass;
		this.slotContractParamsClass = slotContractParamsClass;
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {

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
//								setEditorEnabled(enabled);
//								setEditorVisible(enabled);
								super.display(location, scenario, r, range);
//								setEditorEnabled(enabled);
//								setEditorVisible(enabled);
							}
						}
					}
				}

//				setEditorVisible(enabled);
				return true;
			}
		} else if (notification.getFeature() == MMXCorePackage.eINSTANCE.getMMXObject_Extensions()) {
			if (notification.getNotifier() instanceof Slot) {
				// If an instance of the slot specific code has just been added, then display it
				if (paramsClass.isInstance(notification.getNewValue())) {
					enabled = true;
					// FIXME: Almost works correctly, first time round the label is not visible, but the text is set correctly.
					//
//					setEditorVisible(true);
					super.display(location, scenario, paramsClass.cast(notification.getNewValue()), range);
				}
			}

		}

		return false;
	}

	@Override
	protected boolean isEnabled() {
		return enabled;
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {

		this.location = location;
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
			for (final EObject r : range) {
				if (slotContractParamsClass.isInstance(r)) {
					super.display(location, scenario, r, range);
					setEditorVisible(true);
					return;
				}
			}
			super.display(location, scenario, null, range);
			setEditorVisible(true);
		} else {
			enabled = false;
			super.display(location, scenario, null, range);
			setEditorVisible(false);
		}
	}
}