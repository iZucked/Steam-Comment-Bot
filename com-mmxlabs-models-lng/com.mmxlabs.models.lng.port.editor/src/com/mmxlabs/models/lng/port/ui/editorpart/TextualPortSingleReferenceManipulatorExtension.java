/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.ui.tabular.manipulators.TextualSingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class TextualPortSingleReferenceManipulatorExtension extends TextualSingleReferenceManipulator {
	public TextualPortSingleReferenceManipulatorExtension(final EReference field, final IReferenceValueProviderProvider valueProviderProvider, final EditingDomain editingDomain) {
		super(field, valueProviderProvider, editingDomain);
	}

	@Override
	protected IContentProposalProvider createProposalProvider() {
		return (fullContents, position) -> {

			final int completeFrom = 0;

			final String contents = fullContents.substring(completeFrom, position);
			final ArrayList<ContentProposal> list = new ArrayList<>();
			final Set<Object> seenItems = new HashSet<>();
			for (int i = 0; i < names.size(); ++i) {
				final String proposal = names.get(i);
				if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
					final String c = proposal.substring(contents.length());
					final StringBuilder description = new StringBuilder();
					final EObject eObject = valueList.get(i);
					seenItems.add(eObject);
					if (eObject instanceof Port) {
						final Port port = (Port) eObject;
						final Location l = port.getLocation();
						if (l != null) {
							description.append(" - " + l.getCountry());
							if (!l.getOtherNames().isEmpty()) {
								description.append(" Also: " + String.join(", ", l.getOtherNames()));
							}
						}
					}

					list.add(new ContentProposal(proposal, proposal + description.toString(), null, 0));
				}
			}
			LOOP_VALUES: for (final EObject eObject : valueList) {
				if (seenItems.contains(eObject)) {
					continue;
				}
				if (eObject instanceof Port) {
					final Port port = (Port) eObject;
					final Location l = port.getLocation();
					if (l != null) {
						for (final String otherName : l.getOtherNames()) {
							if (otherName.toLowerCase().startsWith(fullContents.toLowerCase())) {
								final String description = " - " + l.getCountry() + " Aliases: " + String.join(", ", l.getOtherNames());
								list.add(new ContentProposal(port.getName(), port.getName() + description, null, 0));
								continue LOOP_VALUES;
							}
						}
					}
				}
			}

			return list.toArray(new IContentProposal[list.size()]);
		};
	}
}