/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.flows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mmxlabs.lingo.reports.views.flows.FlowsReport.Link;
import com.mmxlabs.lingo.reports.views.flows.FlowsReport.Node;

/**
 * An optimising sorter. This aims to minimise the vertical distance between the lhs and rhs positions of a link.
 * 
 * This can produce an unstable sort order - e.g. equivalent flows in different orders produce the same penalty.
 * 
 * 
 * @author Simon Goodall
 *
 */
public class FlatnessSorter implements IFlowSorter {

	protected enum Mode {
		BEST, WORKING
	};

	protected static class NodeData {
		public Node node;
		public Link[] bestLHSLinks;
		public Link[] workingLHSLinks;

		public Link[] bestRHSLinks;
		public Link[] workingRHSLinks;

		Link[] getLHS(Mode m) {
			if (m == Mode.BEST) {
				return bestLHSLinks;
			} else {
				if (m == Mode.WORKING)
					return workingLHSLinks;
			}
			throw new IllegalArgumentException();
		}

		Link[] getRHS(Mode m) {
			if (m == Mode.BEST) {
				return bestRHSLinks;
			} else {
				if (m == Mode.WORKING)
					return workingRHSLinks;
			}
			throw new IllegalArgumentException();
		}

		public NodeData(Node n) {
			this.node = n;
			bestLHSLinks = new Link[n.lhsLinks.size()];
			workingLHSLinks = new Link[n.lhsLinks.size()];
			for (int i = 0; i < n.lhsLinks.size(); ++i) {
				bestLHSLinks[i] = n.lhsLinks.get(i);
				workingLHSLinks[i] = n.lhsLinks.get(i);
			}
			bestRHSLinks = new Link[n.rhsLinks.size()];
			workingRHSLinks = new Link[n.rhsLinks.size()];
			for (int i = 0; i < n.rhsLinks.size(); ++i) {
				bestRHSLinks[i] = n.rhsLinks.get(i);
				workingRHSLinks[i] = n.rhsLinks.get(i);
			}
		}

		public void bestToWorking() {
			for (int idx = 0; idx < bestLHSLinks.length; ++idx) {
				workingLHSLinks[idx] = bestLHSLinks[idx];
			}
			for (int idx = 0; idx < bestRHSLinks.length; ++idx) {
				workingRHSLinks[idx] = bestRHSLinks[idx];
			}
		}

		public void workingToBest() {
			for (int idx = 0; idx < bestLHSLinks.length; ++idx) {
				bestLHSLinks[idx] = workingLHSLinks[idx];
			}
			for (int idx = 0; idx < bestRHSLinks.length; ++idx) {
				bestRHSLinks[idx] = workingRHSLinks[idx];
			}
		}
	}

	protected static class Column {
		public Column(int size) {
			bestNodes = new NodeData[size];
			workingNodes = new NodeData[size];
		}

		public NodeData[] bestNodes;
		public NodeData[] workingNodes;

		NodeData[] get(Mode m) {
			if (m == Mode.BEST) {
				return bestNodes;
			} else {
				if (m == Mode.WORKING)
					return workingNodes;
			}
			throw new IllegalArgumentException();
		}

		public void bestToWorking() {
			for (int idx = 0; idx < bestNodes.length; ++idx) {
				workingNodes[idx] = bestNodes[idx];
				workingNodes[idx].bestToWorking();
			}
		}

		public void workingToBest() {
			for (int idx = 0; idx < workingNodes.length; ++idx) {
				bestNodes[idx] = workingNodes[idx];
				bestNodes[idx].workingToBest();
			}
		}
	}

	protected static class Problem {
		public Column[] columns;

		public void bestToWorking() {
			for (int idx = 0; idx < columns.length; ++idx) {
				columns[idx].bestToWorking();
			}
		}

		public void workingToBest() {
			for (int idx = 0; idx < columns.length; ++idx) {
				columns[idx].workingToBest();
			}
		}
	}

	@Override
	public void sortNodesAndLinks(final List<Node> lhsNodes, final List<Node> middleNodes, final List<Node> rhsNodes) {

		Problem problem = new Problem();
		problem.columns = new Column[3];
		problem.columns[0] = new Column(lhsNodes.size());
		problem.columns[1] = new Column(middleNodes.size());
		problem.columns[2] = new Column(rhsNodes.size());

		for (int idx = 0; idx < lhsNodes.size(); ++idx) {
			Node n = lhsNodes.get(idx);
			NodeData d = new NodeData(n);
			problem.columns[0].bestNodes[idx] = d;
			problem.columns[0].workingNodes[idx] = d;
		}
		for (int idx = 0; idx < middleNodes.size(); ++idx) {
			Node n = middleNodes.get(idx);
			NodeData d = new NodeData(n);
			problem.columns[1].bestNodes[idx] = d;
			problem.columns[1].workingNodes[idx] = d;
		}
		for (int idx = 0; idx < rhsNodes.size(); ++idx) {
			Node n = rhsNodes.get(idx);
			NodeData d = new NodeData(n);
			problem.columns[2].bestNodes[idx] = d;
			problem.columns[2].workingNodes[idx] = d;
		}

		// All gone wrong!
		// print the fitness for the solutions and see if they agree?
		// then start to debug changes etc./agree
		// Maybe limit to 10 iterations and display result.

		boolean hasRHS = !rhsNodes.isEmpty();
		final int minNodeListSize = rhsNodes.isEmpty() ? Math.min(lhsNodes.size(), middleNodes.size()) : Math.min(lhsNodes.size(), Math.min(middleNodes.size(), rhsNodes.size()));

		// realToCpy(lhsNodes, middleNodes, rhsNodes);

		final Random rnd = new Random(0);
		// Evaluation starting point state
		long bestP = eval(problem, Mode.BEST);
		// Number of iterations
		for (int i = 0; i < 50_000; ++i) {

			// Number of move chains
			for (int j = 0; j < rnd.nextInt(5); ++j) {
				final int move = rnd.nextInt(3);
				if (move == 0) {
					// Node swap
					final int r = rnd.nextInt(hasRHS ? 3 : 2);
					NodeData[] l = problem.columns[r].workingNodes;
					if (l.length > 1) {
						final int a = rnd.nextInt(l.length);
						int b = rnd.nextInt(l.length - 1);
						// Make sure a!=b
						if (b >= a) {
							++b;
						}
						final NodeData na = l[a];
						final NodeData nb = l[b];
						l[a] = nb;
						l[b] = na;
					}
				} else if (move == 1) {
					// Within node swap
					// Node swap
					final int r = rnd.nextInt(hasRHS ? 3 : 2);
					NodeData[] nl = problem.columns[r].workingNodes;
					boolean rhs;
					if (r == 0) {
						rhs = true;
					} else if (r == 1) {
						rhs = hasRHS ? rnd.nextBoolean() : false;
					} else {
						rhs = false;
					}
					final NodeData n = nl[rnd.nextInt(nl.length)];

					final Link[] l = rhs ? n.workingRHSLinks : n.workingLHSLinks;
					if (l.length > 1) {
						final int a = rnd.nextInt(l.length);
						int b = rnd.nextInt(l.length - 1);
						if (b >= a) {
							++b;
						}
						final var na = l[a];
						final var nb = l[b];
						l[a] = nb;
						l[b] = na;
					}
				} else {
					if (minNodeListSize > 1) {
						// Row swap
						final int a = rnd.nextInt(minNodeListSize);
						int b = rnd.nextInt(minNodeListSize - 1);
						if (b >= a) {
							++b;
						}
						{
							final var l = problem.columns[0].workingNodes;
							final NodeData na = l[a];
							final NodeData nb = l[b];
							l[a] = nb;
							l[b] = na;
						}
						{
							final var l = problem.columns[1].workingNodes;
							final NodeData na = l[a];
							final NodeData nb = l[b];
							l[a] = nb;
							l[b] = na;
						}
						if (!rhsNodes.isEmpty()) {
							final var l = problem.columns[2].workingNodes;
							;
							final NodeData na = l[a];
							final NodeData nb = l[b];
							l[a] = nb;
							l[b] = na;
						}
					}
				}
			}

			final long p = eval(problem, Mode.WORKING);
			if (p < bestP) {
				bestP = p;
				problem.workingToBest();
			} else {
				// Reset
				problem.bestToWorking();
			}
		}

		List<List<Node>> result = new ArrayList<>(3);
		result.add(lhsNodes);
		result.add(middleNodes);
		result.add(rhsNodes);
		for (int i = 0; i < 3; ++i) {
			List<Node> ln = result.get(i);

			ln.clear();
			for (var nd : problem.columns[i].bestNodes) {
				ln.add(nd.node);
				nd.node.lhsLinks.clear();
				for (var l : nd.bestLHSLinks) {
					nd.node.lhsLinks.add(l);
				}
				nd.node.rhsLinks.clear();

				for (var l : nd.bestRHSLinks) {
					nd.node.rhsLinks.add(l);
				}
			}
		}
	}

	protected long eval(final Problem p, Mode m) {

		long penalty = 0L;

		int lhsStart = 0;
		for (final var lNode : p.columns[0].get(m)) {
			final int l = lhsStart;
			for (final var lLink : lNode.getRHS(m)) {

				int rhsStart = 0;
				RHS_LOOP: for (final var rNode : p.columns[1].get(m)) {
					final int r = rhsStart;

					for (final var rLink : rNode.getLHS(m)) {
						if (lLink == rLink) {
							penalty += Math.abs(lhsStart - rhsStart);
							break RHS_LOOP;
						} else {
							rhsStart += rLink.rawValue;
						}
					}
					rhsStart = r + rNode.node.rawValue;
				}
				lhsStart += lLink.rawValue;
			}
			lhsStart = l + lNode.node.rawValue;
		}

		lhsStart = 0;
		for (final var lNode : p.columns[1].get(m)) {
			final int l = lhsStart;

			for (final var lLink : lNode.getRHS(m)) {

				int rhsStart = 0;
				RHS_LOOP: for (final var rNode : p.columns[2].get(m)) {
					final int r = rhsStart;

					for (final var rLink : rNode.getLHS(m)) {
						if (lLink == rLink) {
							penalty += Math.abs(lhsStart - rhsStart);
							break RHS_LOOP;
						} else {
							rhsStart += rLink.rawValue;
						}
					}
					rhsStart = r + rNode.node.rawValue;

				}
				lhsStart += lLink.rawValue;
			}
			lhsStart = l + lNode.node.rawValue;
		}
		return penalty;
	}
}
