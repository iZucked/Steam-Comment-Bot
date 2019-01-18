/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.inlineeditors;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.UnsettableInlineEditor;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.rcp.common.menus.SubLocalMenuHelper;

public class NominatedVesselInlineEditor extends UnsettableInlineEditor {

	private Label theLabel;
	private Button button;
	private LocalMenuHelper helper;

	/**
	 * @param path
	 * @param feature
	 * @param editingDomain
	 * @param commandProcessor
	 */
	public NominatedVesselInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	// @Override
	// public Control createControl(Composite parent, EMFDataBindingContext dbc, FormToolkit toolkit) {
	// isOverridable = false;
	// isOverridableWithButton = false;
	// return super.createControl(parent, dbc, toolkit);
	// }

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		super.display(dialogContext, context, input, range);
	}

	@Override
	protected Object getInitialUnsetValue() {
		return Collections.emptyList();
	}

	@Override
	protected Control createValueControl(final Composite parent) {
		final Composite buttonAndLabel = toolkit.createComposite(parent);
		final GridLayout gl = new GridLayout(2, false);
		buttonAndLabel.setLayout(gl);
		gl.marginWidth = 0;
		gl.marginHeight = 0;

		final Label label = toolkit.createLabel(buttonAndLabel, "");
		{
			// Set a size hint, but allow width to increase if needed.
			final GridData gd = GridDataFactory.fillDefaults().hint(150, SWT.DEFAULT).grab(true, false).create();
			label.setLayoutData(gd);
		}
		final ImageDescriptor d = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.cargo.editor", "icons/edit.png");
		button = toolkit.createButton(buttonAndLabel, "", SWT.NONE);
		final Image img = d.createImage();
		button.setImage(img);
		button.addDisposeListener(e -> img.dispose());

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				openMenus(parent);
			}
		});

		theLabel = label;

		helper = new LocalMenuHelper(buttonAndLabel);
		buttonAndLabel.addDisposeListener(event -> helper.dispose());

		return super.wrapControl(buttonAndLabel);

	}

	@Override
	protected Command createSetCommand(final Object value) {
		if (value == SetCommand.UNSET_VALUE) {
			final CompoundCommand cmd = new CompoundCommand();

			cmd.append(SetCommand.create(commandHandler.getEditingDomain(), input, feature, value));
			if (overrideToggleFeature != null) {
				cmd.append(SetCommand.create(commandHandler.getEditingDomain(), input, overrideToggleFeature, Boolean.FALSE));
			}
			return cmd;
		} else {
			return SetCommand.create(commandHandler.getEditingDomain(), input, feature, value);
		}
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (theLabel == null || theLabel.isDisposed()) {
			return;
		}
		final IReferenceValueProvider valueProvider = getValueProvider();
		if (valueProvider != null) {
			if (value == null || value == SetCommand.UNSET_VALUE) {
				theLabel.setText("Unassigned");
			} else {
				theLabel.setText(valueProvider.getName(input, (EReference) feature, (EObject) value));
			}
		}
	}

	protected void openMenus(final Control cellEditorWindow) {
		final IReferenceValueProvider valueProvider = getValueProvider();
		final Object currentValue = getValue();

		final List<Pair<String, EObject>> allowedValues = valueProvider.getAllowedValues(input, feature);

		if (allowedValues.size() > 0 && allowedValues.get(0).getSecond() == null) {
			allowedValues.remove(0);
		}

		helper.clearActions();
		if (input instanceof Slot) {
			final int split = 15;
			final Slot<?> slot = (Slot<?>) input;
			if (allowedValues.size() > split) {
				int counter = 0;
				SubLocalMenuHelper m = new SubLocalMenuHelper("");
				String firstEntry = null;
				String lastEntry = null;
				for (final Pair<String, EObject> p : allowedValues) {
					if (p.getSecond() == null) {
						continue;
					}
					if (p.getSecond() == slot.getNominatedVessel()) {
						continue;
					}
					final int capacity = ((Vessel) p.getSecond()).getVesselOrDelegateCapacity();
					m.addAction(new RunnableAction(String.format("%s (%dk)", p.getFirst(), capacity / 1000), () -> update(p.getSecond()))).setChecked(currentValue == p.getSecond());
					;
					counter++;
					if (firstEntry == null) {
						firstEntry = p.getFirst();
					}
					lastEntry = p.getFirst();

					if (counter == split) {
						final String title = String.format("%s ... %s", firstEntry, lastEntry);
						m.setTitle(title);
						helper.addSubMenu(m);
						m = new SubLocalMenuHelper("");
						counter = 0;
						firstEntry = null;
						lastEntry = null;
					}
				}
				if (counter > 0) {
					final String title = String.format("%s ... %s", firstEntry, lastEntry);
					m.setTitle(title);
					helper.addSubMenu(m);
				}

				// Carve up menu
			} else {
				for (final Pair<String, EObject> p : allowedValues) {
					if (p.getSecond() != slot.getNominatedVessel()) {
						final Vessel vessel = (Vessel) p.getSecond();
						if (vessel != null) {
							final int capacity = vessel.getVesselOrDelegateCapacity();
							helper.addAction(new RunnableAction(String.format("%s (%dk)", p.getFirst(), capacity / 1000), () -> update(p.getSecond()))).setChecked(currentValue == p.getSecond());
						}
					}
				}
			}
			if (currentValue != null) {
				helper.addAction(new RunnableAction("Unassign", () -> update(SetCommand.UNSET_VALUE)));
			}
		}
		helper.open();
	}

	private synchronized void update(final Object o) {
		if (o != null) {
			doSetValue(o, false);
			updateDisplay(o);
		}
	}

	private IReferenceValueProvider getValueProvider() {
		if (input == null) {
			return null;
		}
		final IReferenceValueProviderProvider referenceValueProviderProvider = commandHandler.getReferenceValueProviderProvider();
		if (referenceValueProviderProvider == null) {
			return null;
		}
		return referenceValueProviderProvider.getReferenceValueProvider(input.eClass(), (EReference) feature);
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;

		theLabel.setEnabled(controlsEnabled);
		button.setEnabled(controlsEnabled);

		super.setControlsEnabled(controlsEnabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {
		theLabel.setVisible(visible);
		button.setVisible(visible);

		super.setControlsVisible(visible);
	}

}