/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 * A column manipulator for setting single-valued EReference features.
 * 
 * Uses {@link ComboBoxCellEditor} for its edit control, and takes the values from an {@link IReferenceValueProvider}.
 * 
 * @author FM
 * 
 */
public class HoursSingleReferenceManipulator extends BasicAttributeManipulator {

	protected final List<Integer> valueList = new ArrayList<>();
	protected final List<String> names = new ArrayList<>();
	protected final List<String> lowerNames = new ArrayList<>();

	private static final List<Pair<String, Integer>> hours;
	static {
		hours = new ArrayList<>(24);
		for (int i = 0; i < 24; i++) {
			hours.add(new Pair<>(String.format("%02d:00", i), i));
		}
	}

	/**
	 * Create a manipulator for the given field in the target object, taking values from the given valueProvider and creating set commands in the provided editingDomain.
	 * 
	 * @param field
	 *            the field to set
	 * @param editingDomain
	 *            editing domain for setting
	 */
	public HoursSingleReferenceManipulator(final EAttribute field, final ICommandHandler commandHandler) {
		super(field, commandHandler);
	}

	@Override
	public String render(final Object object) {
		final Object superValue = super.getValue(object);
		if (superValue == SetCommand.UNSET_VALUE) {
			if (object instanceof MMXObject) {
				final Object defaultValue = ((MMXObject) object).getUnsetValue(field);
				if (defaultValue instanceof Object || defaultValue == null) {
					return "";
				}
			}
		} else {
			if (superValue instanceof Integer) {
				return hours.get((Integer) superValue).getFirst();
			} else {
				return "";
			}
		}
		return "";
	}

	@Override
	public void doSetValue(final Object object, final Object value) {
		if (value == null || value.equals(-1) || value.toString().isEmpty()) {
			super.runSetCommand(object, SetCommand.UNSET_VALUE);
			return;
		}
		final int idx = names.indexOf(value);
		if (idx >= 0) {

			final int newValue = valueList.get(idx);
			super.runSetCommand(object, newValue);
		}
	}

	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		TextCellEditor editor = new TextCellEditor(c);

		editor.setValidator(value -> {
			if (value == null || value.toString().isEmpty()) {
				return null;
			}
			if (names.contains(value)) {
				return null;
			}
			if (value instanceof String && lowerNames.contains(value.toString().toLowerCase())) {
				return null;
			}
			return "Unknown name";
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
		proposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT);
		proposalAdapter.setPropagateKeys(true);

		return editor;
	}

	protected IContentProposalProvider createProposalProvider() {
		return new IContentProposalProvider() {

			@Override
			public IContentProposal[] getProposals(final String full_contents, final int position) {

				final int completeFrom = 0;

				final String contents = full_contents.substring(completeFrom, position);
				final ArrayList<ContentProposal> list = new ArrayList<>();
				for (final String proposal : names) {
					if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
						final String c = proposal.substring(contents.length());
						list.add(new ContentProposal(c, proposal, null, c.length()));

					}
				}

				return list.toArray(new IContentProposal[list.size()]);
			}
		};
	}

	@Override
	public Object getValue(final Object object) {
		final Object value = super.getValue(object);
		final int x = valueList.indexOf(value);
		if (x == -1) {
			// this can happen where there is no existing selection
			return "";
		}
		return names.get(x);
	}

	@Override
	public boolean canEdit(final Object object) {

		valueList.clear();
		names.clear();
		lowerNames.clear();
		for (final Pair<String, Integer> value : hours) {
			names.add(value.getFirst());
			lowerNames.add(value.getFirst().toLowerCase());
			valueList.add(value.getSecond());
		}

		return !valueList.isEmpty();
	}
}
