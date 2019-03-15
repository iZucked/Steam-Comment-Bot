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

	protected IContentProposalProvider createProposalProvider() {
		return new IContentProposalProvider() {

			@Override
			public IContentProposal[] getProposals(final String full_contents, final int position) {

				final int completeFrom = 0;

				final String contents = full_contents.substring(completeFrom, position);
				final ArrayList<ContentProposal> list = new ArrayList<>();
				Set<Object> seenItems = new HashSet<>();
				for (int i = 0; i < names.size(); ++i) {
					final String proposal = names.get(i);
					if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
						final String c = proposal.substring(contents.length());
						String description = "";
						final EObject eObject = valueList.get(i);
						seenItems.add(eObject);
						if (eObject instanceof Port) {
							final Port port = (Port) eObject;
							final Location l = port.getLocation();
							if (l != null) {
								description = " - " + l.getCountry();
								if (!l.getOtherNames().isEmpty()) {
									description += " Aliases: " + String.join(", ", l.getOtherNames());
								}
							}
						}

						list.add(new ContentProposal(c, proposal + description, null, c.length()));
					}
				}
				LOOP_VALUES: for (EObject eObject : valueList) {
					if (seenItems.contains(eObject)) {
						continue;
					}
					if (eObject instanceof Port) {
						final Port port = (Port) eObject;
						final Location l = port.getLocation();
						if (l != null) {
							for (String otherName : l.getOtherNames()) {
								if (otherName.toLowerCase().contains(full_contents.toLowerCase())) {
									String description = " - " + l.getCountry() + " Aliases: " + String.join(", ", l.getOtherNames());
									list.add(new ContentProposal(port.getName(), port.getName() + description, null, 0));
									continue LOOP_VALUES;
								}
							}
						}
					}

				}

				return list.toArray(new IContentProposal[list.size()]);
			}
		};
	}
}