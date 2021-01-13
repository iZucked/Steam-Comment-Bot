/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.presentation.composites;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.nebula.widgets.formattedtext.ITextFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.UnsettableInlineEditor;

public class NominatedValueInlineEditor extends UnsettableInlineEditor {

	public enum NominatedValueMode {
		PICKER, FORMATTED_TEXT
	}

	public interface NominatedValueProvider {
		@NonNull
		NominatedValueMode getMode(MMXRootObject rootObject, Notifier target);

		@NonNull
		List<String> getPossibleValues(MMXRootObject rootObject, Notifier target);

		@NonNull
		ITextFormatter getTextFormatter(String nominationType);
	}

	private final NominatedValueProvider suggestions;
	protected FormattedText editor;
	protected IItemPropertyDescriptor propertyDescriptor = null;

	public NominatedValueInlineEditor(final EStructuralFeature feature, NominatedValueProvider suggestions) {
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
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		if (object instanceof AbstractNomination) {
			String nominationType = ((AbstractNomination) object).getType();
			ITextFormatter f = this.suggestions.getTextFormatter(nominationType);
			this.editor.setFormatter(f);
		}
		super.display(dialogContext, scenario, object, range);
	}

	@Override
	protected void updateControl() {
		if (editor == null || editor.getControl().isDisposed()) {
			return;
		}
		super.updateControl();
	}

	@Override
	protected Object getInitialUnsetValue() {
		return null;
	}

	@Override
	public void reallyNotifyChanged(final Notification msg) {
		if (msg.getFeature() == NominationsPackage.eINSTANCE.getAbstractNominationSpec_Type()) {
			// it is a change to our feature
			String nominationType = msg.getNewStringValue();
			this.editor.setFormatter(this.suggestions.getTextFormatter(nominationType));
		}
		super.reallyNotifyChanged(msg);
	}

	@Override
	public Control createValueControl(final Composite parent) {
		this.editor = new FormattedText(parent, SWT.BORDER);

		toolkit.adapt(editor.getControl(), true, true);

		editor.getControl().setEnabled(feature.isChangeable() && isEditorEnabled());
		editor.getControl().addModifyListener(e -> {
			editor.getControl().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			final String text = editor.getControl().getText().toLowerCase();
			if (text.isEmpty()) {
				doSetValue(SetCommand.UNSET_VALUE, false);
			} else {
				switch (this.suggestions.getMode(rootObject, target)) {
				case PICKER:
					setPickerValue(text);
					break;
				case FORMATTED_TEXT:
					setFormattedTextValue();
					break;
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

		final ContentProposalAdapter proposalAdapter = new ContentProposalAdapter(editor.getControl(), controlContentAdapter, proposalProvider, AutoCompleteHelper.getActivationKeystroke(),
				AutoCompleteHelper.getAutoactivationChars());
		proposalAdapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
		proposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		proposalAdapter.setPropagateKeys(true);

		return super.wrapControl(editor.getControl());

	}

	private void setFormattedTextValue() {
		Object object = editor.getValue();
		if (object == null) {
			String editString = editor.getFormatter().getEditString();
			int window = 0;
			char units = 'd';

			// Add extra bits, if just window end missing.
			editString = editString.trim();
			if (isWholeDate(editString)) {
				if (isWindowSpecified(editString) && isWindowMissingUnits(editString)) {
					window = getWindow(editString);
					editString += "d";
				} else if (!isWindowSpecified(editString) && isWindowMissingUnits(editString)) {
					editString += "0d";
				}

				// Convert to TimeWindowHolder object.
				int day = Integer.valueOf(editString.substring(0, 2));
				int month = Integer.valueOf(editString.substring(3, 5));
				int year = Integer.valueOf(editString.substring(6, 10));

				object = new TimeWindowHolder(LocalDate.of(year, Month.of(month), day), window, units);
			} else {
				// Mark control as invalid
				editor.getControl().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				object = editString;
			}
		}
		// Should always be a nomination..
		if (target instanceof AbstractNomination) {
			// Convert from JSON string to java String value.
			String jsonString = NominationsModelUtils.getNominatedValueJSONString(object);
			doSetValue(jsonString, false);
		}
	}

	private int getWindow(String editString) {
		String splitStr[] = editString.split("\\+");
		return Integer.valueOf(splitStr[1]);
	}

	private boolean isWindowSpecified(String editString) {
		String splitStr[] = editString.split("\\+");
		if (splitStr.length == 2 && splitStr[1].length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isWindowMissingUnits(String editString) {
		return !(editString.endsWith("h") || editString.endsWith("m") || editString.endsWith("d"));
	}

	// OK
	private boolean isWholeDate(String editString) {
		int[] digitIdxs = new int[] { 0, 1, 3, 4, 6, 7, 8, 9 };
		for (int i = 0; i < digitIdxs.length; i++) {
			int idx = digitIdxs[i];
			if (idx < editString.length() && !Character.isDigit(editString.charAt(idx))) {
				return false;
			}
		}
		return true;
	}

	private void setPickerValue(final String text) {
		final List<String> possibleValues = getPossibleValues(rootObject, target);
		final int indexOf = getLowerValues(possibleValues).indexOf(text);
		if (indexOf >= 0) {
			doSetValue(possibleValues.get(indexOf), false);
		} else {
			// Mark control as invalid
			editor.getControl().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		}
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
		String valueStr = "";
		if (target instanceof AbstractNomination) {
			// Convert from JSON string to java String value.
			AbstractNomination nomination = (AbstractNomination) target;
			String nomType = nomination.getType();
			if (value instanceof String) {
				Object valueObject = NominationsModelUtils.getNominatedValueObjectFromJSON(nomType, (String) value);

				// Convert to a string for display.
				if (valueObject != null && !(valueObject instanceof String)) {
					valueStr = valueObject.toString();
				} else if (valueObject != null) {
					valueStr = (String) valueObject;
				} else {
					valueStr = (String) value;
				}
			}
		} else {
			if (value instanceof String) {
				valueStr = (String) value;
			} else {
				valueStr = "";
			}
		}

		if (editor.getControl() == null || editor.getControl().isDisposed()) {
			return;
		}
		final List<String> possibleValues = getPossibleValues(rootObject, target);
		final List<String> lowerPossibleValues = getLowerValues(possibleValues);
		if (value instanceof String) {
			editor.getControl().setText(valueStr);
		} else {
			final int curIndex = possibleValues.indexOf(value);
			if (curIndex == -1) {
				editor.getControl().setText("");
			} else {
				editor.getControl().setText(possibleValues.get(curIndex));
			}
		}
		if (lowerPossibleValues.contains(editor.getControl().getText().toLowerCase())) {
			editor.getControl().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		} else {
			switch (this.suggestions.getMode(rootObject, target)) {
			case PICKER:
				editor.getControl().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				break;
			case FORMATTED_TEXT:
				editor.getControl().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				break;
			}
		}
	}

	@Override
	public void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;

		if (editor.getControl() != null && !editor.getControl().isDisposed()) {
			editor.getControl().setEnabled(controlsEnabled);
		}

		super.setControlsEnabled(controlsEnabled);
	}

	@Override
	public void setControlsVisible(final boolean visible) {
		if (!editor.getControl().isDisposed()) {
			editor.getControl().setVisible(visible);
		}

		super.setControlsVisible(visible);
	}
}
