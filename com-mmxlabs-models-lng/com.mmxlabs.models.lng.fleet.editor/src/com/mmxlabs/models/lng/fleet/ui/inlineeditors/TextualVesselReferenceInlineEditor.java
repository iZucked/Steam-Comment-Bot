/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.editors.impl.TextualReferenceInlineEditor;

public class TextualVesselReferenceInlineEditor extends TextualReferenceInlineEditor {

	public TextualVesselReferenceInlineEditor(final EStructuralFeature feature) {
		super(feature);
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
					if (eObject instanceof Vessel) {
						final Vessel vessel = (Vessel) eObject;
						final String otherName = vessel.getShortName();
						if (otherName != null && !otherName.isEmpty()) {
							description.append(" : " + otherName);
						}
					}

					list.add(new ContentProposal(proposal, proposal + description.toString(), null, 0));
				} else if (proposal.length() >= contents.length() && proposal.toLowerCase().contains(contents.toLowerCase())) {
					final StringBuilder description = new StringBuilder();
					final EObject eObject = valueList.get(i);
					seenItems.add(eObject);
					if (eObject instanceof Vessel) {
						final Vessel vessel = (Vessel) eObject;
						final String otherName = vessel.getShortName();
						if (otherName != null && !otherName.isEmpty()) {
							description.append(" : " + otherName);
						}
					}

					list.add(new ContentProposal(proposal, proposal + description.toString(), null, 0));
				}
			}
			for (final EObject eObject : valueList) {
				if (seenItems.contains(eObject)) {
					continue;
				}
				if (eObject instanceof Vessel) {
					final Vessel vessel = (Vessel) eObject;
					final String otherName = vessel.getShortName();
					if (otherName != null && !otherName.isEmpty()) {
						if (otherName.toLowerCase().startsWith(fullContents.toLowerCase())) {

							final String description = " : " + otherName;
							list.add(new ContentProposal(vessel.getName(), vessel.getName() + description, null, 0));
						}
					}
				}
			}

			return list.toArray(new IContentProposal[list.size()]);
		};
	}
}