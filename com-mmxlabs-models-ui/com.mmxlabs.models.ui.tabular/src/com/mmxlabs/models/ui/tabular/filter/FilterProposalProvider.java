/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerFilterSupport;

/**
 * Provide a bunch of filter suggestions
 * 
 * @author hinton
 * 
 */
public class FilterProposalProvider implements IContentProposalProvider {
	private final List<Pair<String, String>> proposals = new ArrayList<Pair<String, String>>();
	private EObjectTableViewerFilterSupport filterSupport = null;

	public void setProposals(final Map<String, List<String>> map) {
		proposals.clear();
		for (final Map.Entry<String, List<String>> entry : map.entrySet()) {
			for (final String value : entry.getValue()) {
				proposals.add(new Pair<String, String>(value + ":", entry.getKey()));
			}
		}
	}

	@Override
	public IContentProposal[] getProposals(final String contents, final int position) {
		setProposals(filterSupport.getColumnMnemonics());
		final List<ContentProposal> out = new ArrayList<ContentProposal>();

		int spaceBefore = contents.substring(0, position).lastIndexOf(' ');

		if (spaceBefore == -1) {
			spaceBefore = 0;
		} else {
			spaceBefore++;
		}
		final String prefix = contents.substring(spaceBefore, position);

		for (final Pair<String, String> proposal : proposals) {
			if (proposal.getFirst().equals(prefix)) {
				// look up values
				return getValues(proposal.getSecond());
			}
			if (proposal.getFirst().startsWith(prefix)) {
				out.add(new ContentProposal(proposal.getFirst().substring(prefix.length()), proposal.getFirst() + " (" + proposal.getSecond() + ")", "Match column " + proposal.getSecond()));
			}
		}

		return out.toArray(new IContentProposal[out.size()]);
	}

	private IContentProposal[] getValues(final String columnName) {
		if (filterSupport == null) {
			return new IContentProposal[0];
		}

		final Set<String> values = filterSupport.getDistinctValues(columnName);

		final IContentProposal[] result = new IContentProposal[values.size()];
		int index = 0;
		for (final String s : values) {
			if (needsEscaping(s)) {
				result[index++] = new ContentProposal("\"" + s.replaceAll("\"", "\\\"") + "\" ", s, "Match " + columnName + " to " + s);
			} else {
				result[index++] = new ContentProposal(s + " ", s, "Match " + columnName + " to " + s);
			}
		}

		return result;
	}

	private boolean needsEscaping(final String s) {
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (Character.isWhitespace(c)) {
				return true;
			}
			if ((c == ':') || (c == '<') || (c == '>') || (c == '~') || (c == '=')) {
				return true;
			}
			if ((c == '\'') || (c == '"') || (c == ',')) {
				return true;
			}
		}
		return false;
	}

	/**
	 */
	public EObjectTableViewerFilterSupport getFilterSupport() {
		return filterSupport;
	}

	/**
	 */
	public void setFilterSupport(EObjectTableViewerFilterSupport filterSupport) {
		this.filterSupport = filterSupport;
	}
}
