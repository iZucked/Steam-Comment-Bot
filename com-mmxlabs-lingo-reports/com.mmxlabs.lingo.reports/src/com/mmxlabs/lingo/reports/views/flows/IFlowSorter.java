/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.flows;

import java.util.List;

import com.mmxlabs.lingo.reports.views.flows.FlowsReport.Node;

public interface IFlowSorter {

	void sortNodesAndLinks(List<Node> lhsNodes, List<Node> middleNodes, List<Node> rhsNodes);

}
