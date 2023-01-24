/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.ui.editorpart.PortPickerDialog;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.UnsettableInlineEditor;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * An inline editor for editing multi-value references. Pops up a dialog.
 * 
 * @author Tom Hinton
 * 
 */
public class PortMultiReferenceInlineEditor extends UnsettableInlineEditor {

	/** @see MultipleReferenceManipulator */
	private static final int MAX_DISPLAY_LENGTH = 32;
	private static final int MIN_DISPLAY_NAMES = 2;

	private IReferenceValueProvider valueProvider;
	private Label theLabel;
	private Button button;

	/**
	 * @param path
	 * @param feature
	 * @param editingDomain
	 * @param commandProcessor
	 */
	public PortMultiReferenceInlineEditor(final ETypedElement feature) {
		super(feature);
	}

	@Override
	public Control createControl(Composite parent, EMFDataBindingContext dbc, FormToolkit toolkit) {
		isOverridable = false;
		if (typedElement instanceof EStructuralFeature feature) {
		EAnnotation eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
		if (eAnnotation == null) {
			eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverrideByContainer");
		}
		if (eAnnotation != null) {
			for (EStructuralFeature f : feature.getEContainingClass().getEAllAttributes()) {
				if (f.getName().equals(typedElement.getName() + "Override")) {
					isOverridable = true;
					this.overrideToggleFeature = f;
				}
			}
			if (feature.isUnsettable()) {
				isOverridable = true;
			}
		}
		if (isOverridable) {
			isOverridableWithButton = true;
		}
		}
		return super.createControl(parent, dbc, toolkit);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		valueProvider = commandHandler.getReferenceValueProviderProvider().getReferenceValueProvider(input.eClass(), (EReference) typedElement);
		super.display(dialogContext, context, input, range);
	}

	@Override
	public Control createValueControl(final Composite parent) {
		final Composite buttonAndLabel = new Composite(parent, SWT.NONE);
		final GridLayout gl = new GridLayout(2, false);
		buttonAndLabel.setLayout(gl);
		gl.marginWidth = 0;
		gl.marginHeight = 0;

		final Label label = new Label(buttonAndLabel, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonAndLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		
		button = toolkit.createButton(buttonAndLabel, "", SWT.NONE);
		button.setImage(CommonImages.getImage(IconPaths.Edit, IconMode.Enabled));

//		button = new Button(buttonAndLabel, SWT.NONE);
//		button.setText("Edit");

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final List<EObject> o = openDialogBox(parent);
				if (o != null) {
					doSetValue(o, false);
					updateDisplay(o);
				}
			}
		});

		theLabel = label;

		return super.wrapControl(buttonAndLabel);
	}

	@Override
	protected Command createSetCommand(final Object value) {
		if (value == SetCommand.UNSET_VALUE) {
			CompoundCommand cmd = new CompoundCommand();

			cmd.append(SetCommand.create(commandHandler.getEditingDomain(), input, typedElement, value));
			if (overrideToggleFeature != null) {
				cmd.append(SetCommand.create(commandHandler.getEditingDomain(), input, overrideToggleFeature, Boolean.FALSE));
			}
			return cmd;
		} else {
			final CompoundCommand setter = CommandUtil.createMultipleAttributeSetter(commandHandler.getEditingDomain(), input, typedElement, (Collection<?>) value);
			return setter;
		}
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (theLabel == null || theLabel.isDisposed()) {
			return;
		}

		final List<? extends EObject> selectedValues = (List<? extends EObject>) value;
		if (selectedValues != null) {
			final StringBuilder sb = new StringBuilder();
			int numNamesAdded = 0;
			for (final EObject obj : selectedValues) {
				String name = valueProvider.getName(input, (EReference) typedElement, obj);
				if (sb.length() > 0) {
					sb.append(", ");
				}
				if (sb.length() + name.length() <= MAX_DISPLAY_LENGTH || numNamesAdded <= MIN_DISPLAY_NAMES - 1) {
					sb.append(name);
					++numNamesAdded;
				} else {
					sb.append("...");
					break;
				}
			}
			theLabel.setText(sb.toString());
		}
	}

	@SuppressWarnings("unchecked")
	protected List<EObject> openDialogBox(final Control cellEditorWindow) {
		final List<Pair<String, EObject>> options = valueProvider.getAllowedValues(input, typedElement);

		if (options.size() > 0 && options.get(0).getSecond() == null)
			options.remove(0);

		PortPickerDialog picker = new PortPickerDialog(cellEditorWindow.getShell(), options.toArray());
		return picker.pick(options, (List<EObject>) getValue(), (EReference) typedElement);

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

	@Override
	protected Object getInitialUnsetValue() {
		return Collections.emptyList();
	}

	@Override
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		return super.updateOnChangeToFeature(changedFeature);

	}
}
