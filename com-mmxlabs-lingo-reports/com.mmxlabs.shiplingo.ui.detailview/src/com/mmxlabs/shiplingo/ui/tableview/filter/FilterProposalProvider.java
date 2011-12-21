/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.tableview.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import com.mmxlabs.common.Pair;
import com.mmxlabs.shiplingo.ui.tableview.EObjectTableViewer;

/**
 * Provide a bunch of filter suggestions
 * 
 * @author hinton
 *
 */
public class FilterProposalProvider implements IContentProposalProvider {
	private List<Pair<String, String>> proposals = new ArrayList<Pair<String, String>>();
	private EObjectTableViewer viewer = null;
	
	public EObjectTableViewer getViewer() {
		return viewer;
	}

	/**
	 * @param viewer the viewer to set
	 */
	public void setViewer(EObjectTableViewer viewer) {
		this.viewer = viewer;
	}

	public void setProposals(final Map<String, List<String>> map) {
		proposals.clear();
		for (final Map.Entry<String, List<String>> entry : map.entrySet()) {
			for (final String value : entry.getValue())
				proposals.add(new Pair<String, String>(value + ":", entry.getKey()));
		}
	}
	
	@Override
	public IContentProposal[] getProposals(String contents, int position) {
		setProposals(viewer.getColumnMnemonics());
		final List<ContentProposal> out = new ArrayList<ContentProposal>();
		
		int spaceBefore = contents.substring(0, position).lastIndexOf(' ');

		
		if (spaceBefore == -1) spaceBefore = 0;
		else spaceBefore++;
		final String prefix=contents.substring(spaceBefore, position);

		for (final Pair<String, String> proposal : proposals) {
			if (proposal.getFirst().equals(prefix)) {
				// look up values
				return getValues(proposal.getSecond());
			}
			if (proposal.getFirst().startsWith(prefix)) {
				out.add(new ContentProposal(
						proposal.getFirst().substring(prefix.length())
						, proposal.getFirst() + " ("+proposal.getSecond()+")", "Match column " + proposal.getSecond()));
			}
		}
		
		return out.toArray(new IContentProposal[out.size()]);
	}

	
	private IContentProposal[] getValues(final String columnName) {
		if (viewer == null) {
			return new IContentProposal[0];
		}
		
		final Set<String> values = viewer.getDistinctValues(columnName);
		
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

	private boolean needsEscaping(String s) {
		for (int i = 0; i<s.length();i++) {
			char c = s.charAt(i);
			if (Character.isWhitespace(c))return true;
			if (c==':' || c=='<' || c=='>' || c=='~' || c=='=') return true;
			if (c=='\'' || c=='"' || c==',') return true;
		}
		return false;
	}
}
