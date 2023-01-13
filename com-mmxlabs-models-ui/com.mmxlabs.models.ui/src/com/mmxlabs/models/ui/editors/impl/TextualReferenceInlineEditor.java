/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 * 
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 * @author hinton
 * 
 */

public class TextualReferenceInlineEditor extends UnsettableInlineEditor {
	private static final Logger LOG = LoggerFactory.getLogger(TextualReferenceInlineEditor.class);
	/**
	 */
	protected Text editor;
	/**
	 */
	protected IReferenceValueProvider valueProvider;
	protected final List<EObject> valueList = new ArrayList<>();
	protected final List<String> names = new ArrayList<>();
	protected final List<String> lowerNames = new ArrayList<>();

	/**
	 */
	protected IItemPropertyDescriptor propertyDescriptor = null;

	public TextualReferenceInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		isOverridable = false;
		if (typedElement instanceof EStructuralFeature feature) {
			EAnnotation eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
			if (eAnnotation == null) {
				eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverrideByContainer");
			}
			if (eAnnotation != null) {
				for (final EStructuralFeature f : feature.getEContainingClass().getEAllAttributes()) {
					if (f.getName().equals(feature.getName() + "Override")) {
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
		if (input == null) {
			valueProvider = null;
		} else {
			valueProvider = commandHandler.getReferenceValueProviderProvider().getReferenceValueProvider(input.eClass(), (EReference) typedElement);
			if (valueProvider == null) {
				LOG.error("Could not get a value provider for " + input.eClass().getName() + "." + typedElement.getName());
			}
		}
		super.display(dialogContext, context, input, range);
	}

	@Override
	protected void updateControl() {
		if (editor == null || editor.isDisposed()) {
			return;
		}

		final List<Pair<String, EObject>> values = getValues();

		valueList.clear();
		names.clear();
		lowerNames.clear();
		for (final Pair<String, EObject> value : values) {
			names.add(value.getFirst());
			lowerNames.add(value.getFirst().toLowerCase());
			valueList.add(value.getSecond());
		}
		super.updateControl();
	}

	@Override
	protected Object getInitialUnsetValue() {
		return null;
	}

	@Override
	public Control createValueControl(final Composite parent) {
		this.editor = new Text(parent, SWT.BORDER);
		// this.combo = toolkit.createCombo(parent, SWT.READ_ONLY);
		toolkit.adapt(editor, true, true);

		if (typedElement instanceof EStructuralFeature feature) {
			editor.setEnabled(feature.isChangeable() && isEditorEnabled());
		} else {
			editor.setEnabled(false);
		}
		editor.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {

				editor.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				final String portName = editor.getText().toLowerCase();
				if (portName.isEmpty()) {
					doSetValue(SetCommand.UNSET_VALUE, false);
				} else {
					final int indexOf = lowerNames.indexOf(portName);
					if (indexOf >= 0) {
						doSetValue(valueList.get(indexOf), false);
					} else {
						// Mark control as invalid
						editor.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					}
				}
			}
		});

		// Sub-class to strip new-line character coming from the proposal adapter
		final TextContentAdapter controlContentAdapter = new TextContentAdapter() {

			@Override
			public void setControlContents(final Control control, final String text, final int cursorPosition) {
				((Text) control).setText(text.trim());
				((Text) control).setSelection(cursorPosition, cursorPosition);
			}

			@Override
			public void insertControlContents(final Control control, String text, final int cursorPosition) {
				text = text.trim();
				final Point selection = ((Text) control).getSelection();
				((Text) control).insert(text);
				// Insert will leave the cursor at the end of the inserted text. If this
				// is not what we wanted, reset the selection.
				if (cursorPosition < text.length()) {
					((Text) control).setSelection(selection.x + cursorPosition, selection.x + cursorPosition);
				}
			}
		};

		final IContentProposalProvider proposalProvider = createProposalProvider();

		final ContentProposalAdapter proposalAdapter = new ContentProposalAdapter(editor, controlContentAdapter, proposalProvider, AutoCompleteHelper.getActivationKeystroke(),
				AutoCompleteHelper.getAutoactivationChars());
		proposalAdapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
		proposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		proposalAdapter.setPropagateKeys(true);

		return super.wrapControl(editor);

	}

	protected IContentProposalProvider createProposalProvider() {
		return (fullContents, position) -> {
			final int completeFrom = 0;
			final String contents = fullContents.substring(completeFrom, position);
			final ArrayList<ContentProposal> list = new ArrayList<>();
			for (final String proposal : names) {
				if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
					final String c = proposal.substring(contents.length());
					list.add(new ContentProposal(proposal, proposal, null, 0));
				}
			}

			return list.toArray(new IContentProposal[list.size()]);
		};
	}

	protected List<Pair<String, EObject>> getValues() {
		return valueProvider != null ? valueProvider.getAllowedValues(input, typedElement) : Collections.<Pair<String, EObject>> emptyList();
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (editor == null || editor.isDisposed()) {
			return;
		}
		if (value instanceof String) {
			editor.setText((String) value);
		} else {
			final int curIndex = valueList.indexOf(value);
			if (curIndex == -1) {
				editor.setText("");
			} else {
				editor.setText(names.get(curIndex));
			}
		}
		if (lowerNames.contains(editor.getText().toLowerCase())) {
			editor.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		} else {
			editor.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		}
	}

	@Override
	protected boolean updateOnChangeToFeature(final Object changedFeature) {

		if (valueProvider != null) {
			final boolean b = valueProvider.updateOnChangeToFeature(changedFeature);
			if (b) {
				return true;
			}
		}
		return super.updateOnChangeToFeature(changedFeature);
	}

	@Override
	public void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;

		if (editor != null && !editor.isDisposed()) {
			editor.setEnabled(controlsEnabled);
		}

		super.setControlsEnabled(controlsEnabled);
	}

	@Override
	public void setControlsVisible(final boolean visible) {
		if (!editor.isDisposed()) {
			editor.setVisible(visible);
		}

		super.setControlsVisible(visible);
	}
}
