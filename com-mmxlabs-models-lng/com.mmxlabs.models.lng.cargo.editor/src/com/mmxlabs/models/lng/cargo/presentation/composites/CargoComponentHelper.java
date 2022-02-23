/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for Cargo instances
 * 
 * @generated NOT
 */
public class CargoComponentHelper extends DefaultComponentHelper {

	public CargoComponentHelper() {
		super(CargoPackage.Literals.CARGO);

		ignoreFeatures.add(CargoPackage.Literals.CARGO__SLOTS);

		addEditor(CargoPackage.Literals.CARGO__ALLOW_REWIRING, this::createAllowRewiringEditor);
	}

	/**
	 * Create the editor for the allowRewiring feature on Cargo
	 * 
	 * @generated NOT
	 */
	protected IInlineEditor createAllowRewiringEditor(final EClass topClass) {

		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO__ALLOW_REWIRING);

		editor.addNotificationChangedListener(new IInlineEditorExternalNotificationListener() {

			// private IScenarioEditingLocation location;
			private IDialogEditingContext dialogContext;

			private EObject input;

			@Override
			public void postDisplay(final IInlineEditor editor, final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
				this.dialogContext = dialogContext;
				this.input = object;
			}

			@Override
			public void notifyChanged(final Notification notification) {
				if (notification.getFeature() == CargoPackage.eINSTANCE.getAssignableElement_Locked()) {
					editor.setEditorEnabled(!notification.getNewBooleanValue());
					if (notification.getNewBooleanValue()) {
						final ICommandHandler handler = dialogContext.getScenarioEditingLocation().getDefaultCommandHandler();
						handler.handleCommand(SetCommand.create(dialogContext.getScenarioEditingLocation().getEditingDomain(), input, CargoPackage.eINSTANCE.getCargo_AllowRewiring(), false), input,
								CargoPackage.eINSTANCE.getCargo_AllowRewiring());
					}
				}

			}
		});

		return editor;
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {
		final Set<EObject> external = new LinkedHashSet<>(super.getExternalEditingRange(root, value));

		if (value instanceof Cargo cargo) {

			final Set<Slot<?>> slots = new LinkedHashSet<>();
			for (final Slot<?> slot : cargo.getSortedSlots()) {
				slots.add(slot);
				if (slot instanceof LoadSlot loadSlot) {
					if (loadSlot.getTransferFrom() != null) {
						slots.add(loadSlot.getTransferFrom());
					}
				} else if (slot instanceof DischargeSlot dischargeSlot) {
					if (dischargeSlot.getTransferTo() != null) {
						slots.add(dischargeSlot.getTransferTo());
					}
				}
				external.addAll(super.getExternalEditingRange(root, slot));
			}
			external.addAll(slots);
		}

		return new ArrayList<>(external);
	}
}