/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.flows;

import java.util.function.ToLongFunction;

/**
 * Extended implementation of FlatnessSorter to add in a small name order penalty.
 * 
 * TODO: Scale depending on mode.
 * 
 * @author Simon Goodall
 *
 */
public class FlatnessAndNameSorter extends FlatnessSorter {

	@Override
	protected long eval(Problem problem, Mode m) {

		// Take flatness penalty from super class
		long flatnessPenalty = super.eval(problem, m);

		// Then compute the number of out of consecutive node pairs which are not in alphabetical order
		ToLongFunction<NodeData[]> func = nodes -> {
			long p = 0L;
			NodeData lastNode = null;
			for (var n : nodes) {
				if (lastNode != null) {
					if (n.node.name.compareTo(lastNode.node.name) < 0) {
						p++;
					}
				}
				lastNode = n;

			}
			return p;
		};

		// We prefer the Source/Load port sorting over destination/discharge port
		long namePenalty = 0L;
		if (problem.columns[2].get(m).length == 0) {
			namePenalty += 2 * func.applyAsLong(problem.columns[0].get(m));
			namePenalty += func.applyAsLong(problem.columns[1].get(m));
		} else {
			namePenalty += func.applyAsLong(problem.columns[0].get(m));
			namePenalty += 2 * func.applyAsLong(problem.columns[1].get(m));
			namePenalty += func.applyAsLong(problem.columns[2].get(m));
		}

		return flatnessPenalty * 10L + namePenalty;
	}
}
