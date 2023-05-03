/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.flows;

import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.lingo.reports.views.flows.FlowsReport.Link;
import com.mmxlabs.lingo.reports.views.flows.FlowsReport.Node;

public class SimpleSorter implements IFlowSorter {

	@Override
	public void sortNodesAndLinks(final List<Node> lhsNodes, final List<Node> middleNodes, final List<Node> rhsNodes) {
		// First sort alphabetically
		lhsNodes.sort((a, b) -> a.name.compareTo(b.name));
		middleNodes.sort((a, b) -> a.name.compareTo(b.name));
		rhsNodes.sort((a, b) -> a.name.compareTo(b.name));

		// Single complete lines, show together
		if (rhsNodes.isEmpty()) {
			final List<Node> moveFirst = new LinkedList<>();
			final List<Node> moveSecond = new LinkedList<>();

			for (final Node n : lhsNodes) {
				if (n.rhsLinks.size() == 1) {
					final Link lhsLink = n.rhsLinks.get(0);
					if (lhsLink.to.lhsLinks.size() == 1 && lhsLink.to.lhsLinks.size() == 1) {
						moveFirst.add(n);
						if (!moveSecond.contains(lhsLink.to)) {
							moveSecond.add(lhsLink.to);
						}
					}
				}
			}

			lhsNodes.removeAll(moveFirst);
			lhsNodes.addAll(0, moveFirst);
			middleNodes.removeAll(moveSecond);
			middleNodes.addAll(0, moveSecond);
		} else {
			final List<Node> moveFirst = new LinkedList<>();
			final List<Node> moveSecond = new LinkedList<>();
			final List<Node> moveThird = new LinkedList<>();

			for (final Node n : lhsNodes) {
				if (n.rhsLinks.size() == 1) {
					final Link lhsLink = n.rhsLinks.get(0);
					if (lhsLink.to.lhsLinks.size() == 1 && lhsLink.to.rhsLinks.size() == 1) {
						moveFirst.add(n);
						if (!moveSecond.contains(lhsLink.to)) {
							moveSecond.add(lhsLink.to);
						}
						if (!moveThird.contains(lhsLink.to.rhsLinks.get(0).to) && lhsLink.to.rhsLinks.get(0).to.lhsLinks.size() == 1) {
							moveThird.add(lhsLink.to.rhsLinks.get(0).to);
						}
					}
				}
			}

			lhsNodes.removeAll(moveFirst);
			lhsNodes.addAll(0, moveFirst);
			middleNodes.removeAll(moveSecond);
			middleNodes.addAll(0, moveSecond);
			rhsNodes.removeAll(moveThird);
			rhsNodes.addAll(0, moveThird);
		}

		// Now sort link rendering order to reduce cross-over
		for (Node n : lhsNodes) {
			n.rhsLinks.sort((a, b) -> {
				int c = lhsNodes.indexOf(a.from) - lhsNodes.indexOf(b.from);
				if (c == 0) {
					c = middleNodes.indexOf(a.to) - middleNodes.indexOf(b.to);
				}
				return c;

			});
		}
		for (Node n : middleNodes) {

			n.rhsLinks.sort((a, b) -> {
				int c = middleNodes.indexOf(a.from) - middleNodes.indexOf(b.from);
				if (c == 0) {
					c = rhsNodes.indexOf(a.to) - rhsNodes.indexOf(b.to);
				}
				return c;

			});
		}
	}
}
