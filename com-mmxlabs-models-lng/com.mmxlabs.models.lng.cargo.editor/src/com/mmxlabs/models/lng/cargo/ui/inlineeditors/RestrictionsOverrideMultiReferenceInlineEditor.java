/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.inlineeditors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.BasicAttributeInlineEditor;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * An inline editor for editing multi-value references. Pops up a dialog.
 * 
 * @author Tom Hinton
 * 
 */
public class RestrictionsOverrideMultiReferenceInlineEditor extends BasicAttributeInlineEditor {
	private IReferenceValueProvider valueProvider;
	private Label theLabel;
	private Button button;
	private Button setButton;
	private Object lastSetValue;
	private Control inner;
	/**
	 */
	protected FormToolkit toolkit;
	/**
	 */
	protected EMFDataBindingContext dbc;
	protected EStructuralFeature overrideToggleFeature;

	/**
	 * @param path
	 * @param feature
	 * @param editingDomain
	 * @param commandProcessor
	 */
	public RestrictionsOverrideMultiReferenceInlineEditor(final EStructuralFeature feature, final EStructuralFeature overrideFeature) {
		super(feature);
		this.overrideToggleFeature = overrideFeature;
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		valueProvider = commandHandler.getReferenceValueProviderProvider().getReferenceValueProvider(input.eClass(), (EReference) typedElement);
		super.display(dialogContext, context, input, range);
	}

	protected Object getInitialUnsetValue() {
		return Collections.emptyList();
	}

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

		button = toolkit.createButton(buttonAndLabel, "", SWT.NONE);
		button.setImage(CommonImages.getImage(IconPaths.Edit, IconMode.Enabled));

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
			cmd.append(SetCommand.create(commandHandler.getEditingDomain(), input, overrideToggleFeature, Boolean.FALSE));
			cmd.append(SetCommand.create(commandHandler.getEditingDomain(), input, typedElement, value));
			return cmd;
		} else {
			final CompoundCommand setter = CommandUtil.createMultipleAttributeSetter(commandHandler.getEditingDomain(), input, typedElement, (Collection<?>) value);
			return setter;
		}
	}

	protected void updateValueDisplay(final Object value) {
		if (theLabel == null || theLabel.isDisposed()) {
			return;
		}

		final List<? extends EObject> selectedValues = (List<? extends EObject>) value;
		if (selectedValues != null) {
			final StringBuilder sb = new StringBuilder();
			for (final EObject obj : selectedValues) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(valueProvider.getName(input, (EReference) typedElement, obj));
			}
			theLabel.setText(sb.toString());
		}
	}

	@SuppressWarnings("unchecked")
	protected List<EObject> openDialogBox(final Control cellEditorWindow) {
		final List<Pair<String, EObject>> options = valueProvider.getAllowedValues(input, typedElement);

		if (options.size() > 0 && options.get(0).getSecond() == null)
			options.remove(0);

		final ListSelectionDialog dlg = new ListSelectionDialog(cellEditorWindow.getShell(), options.toArray(), new ArrayContentProvider(), new LabelProvider() {

			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
		dlg.setTitle("Value Selection");

		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<>();
		final Collection<EObject> sel = (Collection<EObject>) getValue();
		if (sel != null) {
			for (final Pair<String, EObject> p : options) {
				if (sel.contains(p.getSecond())) {
					selectedOptions.add(p);
				}
			}
		}

		dlg.setInitialSelections(selectedOptions.toArray());

		createColumns(dlg);

		dlg.groupBy(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<?, EObject>) element).getSecond().eClass().getName();
			}
		});

		if (dlg.open() == Window.OK) {
			final Object[] result = dlg.getResult();

			final ArrayList<EObject> resultList = new ArrayList<>();
			for (final Object o : result) {
				resultList.add(((Pair<String, EObject>) o).getSecond());
			}
			return resultList;
		}

		return null;
	}

	/**
	 */
	protected void createColumns(final ListSelectionDialog dlg) {
		dlg.addColumn("Name", new ColumnLabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;

		theLabel.setEnabled(controlsEnabled);
		button.setEnabled(controlsEnabled);

		super.setControlsEnabled(controlsEnabled);
		if (setButton != null) {
			setButton.setEnabled(controlsEnabled);
			if (inner != null) {
				inner.setEnabled(controlsEnabled && (setButton.getSelection() || !canOverride()));
			}
		} else if (inner != null) {
			inner.setEnabled(controlsEnabled);
		}
	}

	@Override
	protected void setControlsVisible(final boolean visible) {
		if (canOverride()) {
			setButton.setVisible(visible);
		} else {
			setButton.setVisible(visible);
		}
		theLabel.setVisible(visible);
		button.setVisible(visible);

		super.setControlsVisible(visible);
	}

	protected boolean canOverride() {
		if (input instanceof Slot) {
			Slot<?> slot = (Slot<?>) input;
			return slot.getContract() != null;
		}
		return false;
	}

	private void setControlEnabled(final Control c, final boolean enabled) {
		if (c == null) {
			return;
		}
		c.setEnabled(enabled);
		if (c instanceof Composite) {
			for (final Control c2 : ((Composite) c).getChildren()) {
				setControlEnabled(c2, enabled);
			}
		}
	}

	/**
	 */
	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {

		this.dbc = dbc;
		this.toolkit = toolkit;
		final Control c;

		{
			// FIXME: too early! Needs to be after/part of display(), once input has been set
			this.lastSetValue = getInitialUnsetValue();
			// final Composite sub = new Composite(parent, SWT.NONE);
			final Composite sub = toolkit.createComposite(parent);
			final GridLayout gl = new GridLayout(2, false);
			gl.marginLeft = 0;
			gl.marginBottom = 0;
			gl.marginHeight = 0;
			gl.marginTop = 0;
			gl.marginRight = 0;
			gl.marginWidth = 0;
			sub.setLayout(gl);
			// this.setButton = new Button(sub, SWT.CHECK);
			this.setButton = new Button(sub, SWT.CHECK) {
				@Override
				protected void checkSubclass() {
				};

				@Override
				public void setVisible(final boolean visible) {
					if (!canOverride()) {
						if (!visible) {
							super.setVisible(visible);
						} else {
							super.setVisible(false);

						}
					} else {
						super.setVisible(visible);
					}
				}
			};
			this.inner = createValueControl(sub);
			setButton.addSelectionListener(new SelectionAdapter() {
				{
					final SelectionAdapter self = this;
					setButton.addDisposeListener(new DisposeListener() {
						@Override
						public void widgetDisposed(final DisposeEvent e) {
							setButton.removeSelectionListener(self);
						}
					});
				}

				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (setButton.getSelection()) {
						if (overrideToggleFeature != null) {
							commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), input, overrideToggleFeature, Boolean.TRUE), input, overrideToggleFeature);
						}
						// Apply a set command otherwise the display value may not be the value stored in eobject
						commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), input, typedElement, getValue()), input, typedElement);
						setControlEnabled(inner, true);
					} else {
						unsetValue();
						setControlEnabled(inner, false);
					}
					currentlySettingValue = true;
					updateValueDisplay(getValue());
					currentlySettingValue = false;
				}
			});
			setButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

			inner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			c = sub;
		}
		return super.wrapControl(c);
	}

	protected void unsetValue() {
		super.doSetValue(SetCommand.UNSET_VALUE, true);
	}

	@Override
	protected Object getValue() {

		if (input != null && getFeature() instanceof EStructuralFeature feature){
			if (!canOverride()) {
				return ((MMXObject) input).getUnsetValue(feature);
			} else if (input.eIsSet(feature)) {
				return super.getValue();
			} else {
				if (input instanceof MMXObject) {
					return ((MMXObject) input).getUnsetValue(feature);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	protected synchronized void doSetValue(final Object value, final boolean forceCommandExecution) {
		if (currentlySettingValue) {
			return;
		}

		if (value != null) {
			lastSetValue = value; // hold for later checking and unchecking.
		}
		// maybe set button when value is changed
		if (setButton != null && value != null && !setButton.isDisposed()) {
			setButton.setSelection(true);
		}
		if (setButton == null || setButton.getSelection()) {
			super.doSetValue(value, forceCommandExecution);
		}
	}

	protected boolean valueIsSet() {
		if (input != null && getFeature() instanceof EStructuralFeature feature){
			return input.eIsSet(feature);
		}
		return false;
	}

	@Override
	protected void updateDisplay(final Object value) {

		if (inner != null && inner.isDisposed()) {
			return;
		}
		if (setButton == null || value != null) {
			updateValueDisplay(value);
		}
		if (setButton != null && !setButton.isDisposed()) {
			setButton.setEnabled(isEditorEnabled());
			if (!canOverride()) {
				setButton.setVisible(false);
			} else {
				setButton.setSelection(valueIsSet());
			}

		}

		boolean setEnabled = false;
		if (setButton == null) {
			setEnabled = true;
		} else {
			setEnabled = !canOverride() || setButton.getSelection();
		}

		final boolean innerEnabled = !isFeatureReadonly() && isEditorEnabled() && !isEditorLocked() && setEnabled;
		if (inner != null) {
			setControlEnabled(inner, innerEnabled);
		}
	}

	@Override
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		if (changedFeature == overrideToggleFeature) {
			return true;
		}
		if (changedFeature == CargoPackage.Literals.SLOT__CONTRACT) {
			return true;
		}
		return super.updateOnChangeToFeature(changedFeature);
	}

}
