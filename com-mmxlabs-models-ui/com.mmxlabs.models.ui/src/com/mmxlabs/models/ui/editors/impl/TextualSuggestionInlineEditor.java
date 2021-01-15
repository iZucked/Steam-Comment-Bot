/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;

public class TextualSuggestionInlineEditor extends UnsettableInlineEditor {
	
	public interface SuggestionsProvider {
		@NonNull List<String> getPossibleValues(MMXRootObject rootObject, Notifier target);
	}
	private final SuggestionsProvider suggestions;
	protected Text editor;
	protected IItemPropertyDescriptor propertyDescriptor = null;

	public TextualSuggestionInlineEditor(final EStructuralFeature feature, final List<String> possibleValues) {
		super(feature);
		this.suggestions = (ro, t) -> possibleValues;
	}
	
	public TextualSuggestionInlineEditor(final EStructuralFeature feature, SuggestionsProvider suggestions) {
		super(feature);
		this.suggestions = suggestions;
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		isOverridable = false;
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
		return super.createControl(parent, dbc, toolkit);
	}

	@Override
	protected void updateControl() {
		if (editor == null || editor.isDisposed()) {
			return;
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

		toolkit.adapt(editor, true, true);

		editor.setEnabled(feature.isChangeable() && isEditorEnabled());
		editor.addModifyListener(e -> {
			editor.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			final String text = editor.getText().toLowerCase();
			if (text.isEmpty()) {
				doSetValue(SetCommand.UNSET_VALUE, false);
			} else {
				final List<String> possibleValues = getPossibleValues(rootObject, target);
				final int indexOf = getLowerValues(possibleValues).indexOf(text);
				if (indexOf >= 0) {
					doSetValue(possibleValues.get(indexOf), false);
				} else {
					// Mark control as invalid
					editor.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
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
			for (final String proposal : getPossibleValues(this.rootObject, this.target)) {
				if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
					list.add(new ContentProposal(proposal, proposal, null, 0));
				}
			}
			return list.toArray(new IContentProposal[list.size()]);
		};
	}

	List<String> getPossibleValues(MMXRootObject rootObject, Notifier target) {
		final List<String> sortedPossibleValues = new ArrayList<>();
		sortedPossibleValues.addAll(suggestions.getPossibleValues(rootObject, target));
		Collections.sort(sortedPossibleValues);
		return sortedPossibleValues;
	}
	
	List<String> getLowerValues(List<String> values) {
		final List<String> list = new ArrayList<>();
		values.forEach(s -> list.add(s.toLowerCase()));
		return list;
	}
	
	@Override
	protected void updateValueDisplay(final Object value) {
		if (editor == null || editor.isDisposed()) {
			return;
		}
		final List<String> possibleValues = getPossibleValues(rootObject, target);
		final List<String> lowerPossibleValues = getLowerValues(possibleValues);
		if (value instanceof String) {
			editor.setText((String) value);
		} else {
			final int curIndex = possibleValues.indexOf(value);
			if (curIndex == -1) {
				editor.setText("");
			} else {
				editor.setText(possibleValues.get(curIndex));
			}
		}
		if (lowerPossibleValues.contains(editor.getText().toLowerCase())) {
			editor.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		} else {
			editor.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		}
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
