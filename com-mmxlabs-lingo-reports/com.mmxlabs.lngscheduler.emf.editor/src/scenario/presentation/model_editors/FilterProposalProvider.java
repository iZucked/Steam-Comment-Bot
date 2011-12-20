/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import com.mmxlabs.common.Pair;

/**
 * Provide a bunch of filter suggestions
 * 
 * @author hinton
 *
 */
public class FilterProposalProvider implements IContentProposalProvider {
	private List<Pair<String, String>> proposals = new ArrayList<Pair<String, String>>();
	
	public void setProposals(final Map<String, List<String>> map) {
		proposals.clear();
		for (final Map.Entry<String, List<String>> entry : map.entrySet()) {
			for (final String value : entry.getValue())
				proposals.add(new Pair<String, String>(value + ":", entry.getKey()));
		}
	}
	
	@Override
	public IContentProposal[] getProposals(String contents, int position) {
		final List<ContentProposal> out = new ArrayList<ContentProposal>();
		
		int spaceBefore = contents.substring(0, position).lastIndexOf(' ');

		
		if (spaceBefore == -1) spaceBefore = 0;
		else spaceBefore++;
		final String prefix=contents.substring(spaceBefore, position);

		for (final Pair<String, String> proposal : proposals) {
			if (proposal.getFirst().startsWith(prefix)) {
				out.add(new ContentProposal(
						proposal.getFirst().substring(prefix.length())
						, proposal.getFirst() + " ("+proposal.getSecond()+")", "Match column " + proposal.getSecond()));
			}
		}
		
		return out.toArray(new IContentProposal[out.size()]);
	}
	
	public static void main(String args[]) {
		final FilterProposalProvider fpp = new FilterProposalProvider();
		for (int i =0;i<args[0].length(); i++) {
			fpp.getProposals(args[0], i);
		}
	}

}
