/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.stochasticactionsets;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.ui.breakdown.Change;
import com.mmxlabs.models.lng.transformer.ui.breakdown.ChangeSet;
import com.mmxlabs.models.lng.transformer.ui.breakdown.JobState;
import com.mmxlabs.models.lng.transformer.ui.breakdown.MetricType;

public class StochasticActionSetUtils {
	/**
	 * Count identical change sets
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int countIdenticalChangeSets(final JobState a, final JobState b) {
		int identical = 0;
		for (final ChangeSet csA : a.changeSetsAsList) {
			if (b.changeSetsAsSet.contains(csA)) {
				identical += 1;
			}
		}
		return identical;
	}

	/**
	 * Get identical change sets
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Set<ChangeSet> getIdenticalChangeSets(final JobState a, final JobState b) {
		final Set<ChangeSet> identical = new HashSet<ChangeSet>();
		for (final ChangeSet csA : a.changeSetsAsList) {
			if (b.changeSetsAsSet.contains(csA)) {
				identical.add(csA);
			}
		}
		return identical;
	}

	/**
	 * Get identical change sets
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Set<Change> getIdenticalChanges(final ChangeSet a, final ChangeSet b) {
		final Set<Change> identical = new HashSet<Change>();
		for (final Change csA : a.changesList) {
			if (b.changesSet.contains(csA)) {
				identical.add(csA);
			}
		}
		return identical;
	}

	/**
	 * Return the percent of a's changesets in b
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int prctChangeSetsIdentical(final JobState a, final JobState b) {
		final int identical = countIdenticalChangeSets(a, b);
		return Math.min((identical * 100) / (a.changeSetsAsList.size()), 100);
	}

	/**
	 * Return the percent of a's changesets in b
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int prctChangeSetsIdentical(final JobState a, final JobState b, final int identical) {
		return Math.min((identical * 100) / (a.changeSetsAsList.size()), 100);
	}

	/**
	 * Return the percent of a's changesets in b
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int prctChangesIdentical(final ChangeSet a, final ChangeSet b) {
		final int identical = getIdenticalChanges(a, b).size();
		return Math.min((identical * 100) / (a.changesList.size()), 100);
	}

	/**
	 * Returns the total p&L per change for a collection of changeSets
	 * 
	 * @return
	 */
	public static long getTotalPNL(final Collection<ChangeSet> changeSets) {
		long totalPnl = 0;

		for (final ChangeSet cs : changeSets) {
			totalPnl += cs.metricDelta[MetricType.PNL.ordinal()];
		}

		return totalPnl;
	}

	/**
	 * Returns the total p&L per change for a collection of changeSets
	 * 
	 * @return
	 */
	public static double getTotalPNLPerChange(final Collection<ChangeSet> changeSets) {
		double totalChanges = 0.0;
		double totalPnl = 0.0;

		for (final ChangeSet cs : changeSets) {
			totalChanges += cs.changesList.size();
			totalPnl += cs.metricDelta[MetricType.PNL.ordinal()];
		}

		return totalPnl / totalChanges;
	}

	/**
	 * Returns the total p&L per change for a collection of changeSets
	 * 
	 * @return
	 */
	public static double getTotalPNLPerChangeForPercentile(final Collection<ChangeSet> changeSets, double percentile) {
		double totalPnl = 0.0;
		double prctilePnl = 0.0;
		double totalChanges = 0.0;

		for (final ChangeSet cs : changeSets) {
			totalPnl += cs.metricDelta[MetricType.PNL.ordinal()];
		}

		for (final ChangeSet cs : changeSets) {
			totalChanges += cs.changesList.size();
			prctilePnl += cs.metricDelta[MetricType.PNL.ordinal()];
			if (prctilePnl > (totalPnl * percentile)) {
				break;
			}
		}

		return prctilePnl / totalChanges;
	}

	/**
	 * Returns the total p&L per change for a change set
	 * 
	 * @return
	 */
	public static double getTotalPNLPerChange(final ChangeSet cs) {
		return (double) cs.metricDelta[MetricType.PNL.ordinal()] / (double) cs.changesList.size();
	}

	/**
	 * Returns the total number of changes in a collection of changeSets
	 * 
	 * @return
	 */
	public static int getTotalChanges(final Collection<ChangeSet> changeSets) {
		int changes = 0;
		for (final ChangeSet cs : changeSets) {
			changes += cs.changesList.size();
		}
		return changes;
	}

	/**
	 * Returns the total p&L per change for a change set
	 * 
	 * @return
	 */
	public static double calculateCurrentEfficiency(final Collection<ChangeSet> changeSets, final int currentDifferences, final int initialDifferences) {
		return ((double) initialDifferences - (double) currentDifferences) / ((double) getTotalChanges(changeSets));
	}

	/**
	 * Returns the number of identical sets contained in (a,b) and number of non-identical
	 * 
	 * @return
	 */
	public static int[] calculateDistanceToNeighbour(final JobState a, final JobState b) {
		final int[] distances = new int[2];
		final Set<ChangeSet> identicalChangeSets = getIdenticalChangeSets(a, b);
		distances[0] = prctChangeSetsIdentical(a, b, identicalChangeSets.size()); // sets identical
		if (distances[0] < 100) {
			distances[1] = getAverageMatching(a, b);
		}
		return distances;
	}

	private static int getAverageMatching(final JobState a, final JobState b) {
		int sum = 0;
		for (final ChangeSet cs : a.changeSetsAsList) {
			sum += getChangeSetMatching(cs, b);
		}
		return sum / a.changeSetsAsList.size();
	}

	public static int getChangeSetMatching(final ChangeSet cs, final JobState b) {
		int max = 0;
		for (final ChangeSet csB : b.changeSetsAsList) {
			final int prct = prctChangesIdentical(cs, csB);
			if (prct > max) {
				max = prct;
			}
		}
		return max;
	}

	/**
	 * Returns the number of identical sets contained in (a,b) and number of non-identical
	 * 
	 * @return
	 */
	public static Pair<Map<JobState, Map<JobState, int[]>>, Map<JobState, JobState>> calculateDistanceMatrixAndClosestNeighbour(final Collection<JobState> jobStates) {
		final Map<JobState, Map<JobState, int[]>> matrix = new HashMap<>();
		final Map<JobState, JobState> closestNeighbourTable = new HashMap<>();
		for (final JobState jsA : jobStates) {
			final HashMap<JobState, int[]> jobStateColumn = new HashMap<JobState, int[]>();
			matrix.put(jsA, jobStateColumn);
			int[] closestNeighbour = null;
			for (final JobState jsB : jobStates) {
				final int[] distances = calculateDistanceToNeighbour(jsA, jsB);
				jobStateColumn.put(jsB, distances);
				if (closestNeighbour == null) {
					closestNeighbourTable.put(jsA, jsB);
					closestNeighbour = distances;
				} else {
					if (distances[0] > closestNeighbour[0]) {
						closestNeighbourTable.put(jsA, jsB);
					} else if (distances[1] > closestNeighbour[1]) {
						closestNeighbourTable.put(jsA, jsB);
					}
				}
			}
		}
		return new Pair<>(matrix, closestNeighbourTable);
	}

	public Collection<JobState> getKNearestNeighbourForAllChangeSets(final Collection<JobState> jobStates, final Pair<Map<JobState, Map<JobState, int[]>>, Map<JobState, JobState>> distancesAndClosest,
			final JobState forComparing, final int k) {
		final Map<JobState, Map<JobState, int[]>> distances = distancesAndClosest.getFirst();
		final LinkedList<JobState> sortedJobStates = new LinkedList<>(jobStates);
		Collections.sort(sortedJobStates, new Comparator<JobState>() {
			@Override
			public int compare(final JobState o1, final JobState o2) {
				if (distances.get(forComparing).get(o1)[0] == distances.get(forComparing).get(o2)[0]) {
					return (Integer.compare(distances.get(forComparing).get(o1)[1], distances.get(forComparing).get(o2)[1]));
				} else {
					return (Integer.compare(distances.get(forComparing).get(o1)[0], distances.get(forComparing).get(o2)[0]));
				}
			};
		});
		return sortedJobStates.subList(Math.max(0, sortedJobStates.size() - 3), sortedJobStates.size());
	}

	public static int[][] createDistanceData(final Collection<JobState> jobStates) {
		final Map<Change, Integer> changes = new HashMap<>();
		int count = 0;
		for (final JobState js : jobStates) {
			for (final ChangeSet cs : js.changeSetsAsList) {
				for (final Change change : js.changesAsList) {
					if (!changes.keySet().contains(change)) {
						changes.put(change, count);
						count++;
					}
				}
			}
		}
		final int[][] changesString = new int[jobStates.size()][changes.size()];
		int changeStringCount = -1;
		for (final JobState js : jobStates) {
			++changeStringCount;
			final ChangeSet cs = js.changeSetsAsList.get(0);
			for (final Change change : js.changesAsList) {
				changesString[changeStringCount][changes.get(change)] = 1;
			}
		}
		return changesString;
	}

	public static Map<JobState, List<Pair<JobState, Integer>>> getDistanceMatrixForFirstChangeGroup(final Collection<JobState> jobStates, final int[][] changesString) {
		final Map<JobState, List<Pair<JobState, Integer>>> distanceMatrix = new HashMap<>();
		int jsAIndex = 0;
		for (final JobState jsA : jobStates) {
			final List<Pair<JobState, Integer>> row = new LinkedList<>();
			int jsBIndex = 0;
			for (final JobState jsB : jobStates) {
				row.add(new Pair<>(jsB, getDistance(changesString[jsAIndex], changesString[jsBIndex])));
				jsBIndex++;
			}
			distanceMatrix.put(jsA, row);
			jsAIndex++;
		}
		return distanceMatrix;
	}

	public static List<Pair<JobState, Double>> getDistancesForFirstChangeSet(final List<JobState> jobStates, final int k) {
		final Map<JobState, List<Pair<JobState, Integer>>> distanceMatrixForFirstChangeGroup = getDistanceMatrixForFirstChangeGroup(jobStates, createDistanceData(jobStates));
		final List<Pair<JobState, Double>> avgDist = new LinkedList<>();
		for (final JobState js : jobStates) {
			avgDist.add(new Pair<>(js, getKAverageDistanceForJobState(distanceMatrixForFirstChangeGroup.get(js), k)));
		}
		return avgDist;
	}

	public static double getKAverageDistanceForJobState(final List<Pair<JobState, Integer>> row, final int k) {
		Collections.sort(row, new Comparator<Pair<JobState, Integer>>() {
			@Override
			public int compare(final Pair<JobState, Integer> o1, final Pair<JobState, Integer> o2) {
				return o1.getSecond().compareTo(o2.getSecond());
			}
		});
		int sum = 0;
		for (int i = 0; i < k; i++) {
			sum += row.get(i).getSecond();
		}
		return sum / k;
	}

	private static int getDistance(final int[] a, final int[] b) {
		assert a.length == b.length;
		int d = 0;
		for (int i = 0; i < b.length; i++) {
			if (a == b) {
				d += 1;
			}
		}
		return d;
	}

	public static long calculatePNLPerCumulativeChange(JobState js) {
		long cumulativeChanges = 0;
		long value = 0;
		for (ChangeSet cs : js.changeSetsAsList) {
			cumulativeChanges += cs.changesList.size();
			value += (cs.metricDelta[MetricType.PNL.ordinal()] / cumulativeChanges);
		}
		return value;
	}

	public static long calculatePNLPerCumulativeChangeWithNegativeHandling(JobState js) {
		long cumulativeChanges = 0;
		long value = 0;
		int idx = 0;
		while (idx < js.changeSetsAsList.size()) {
			ChangeSet cs = js.changeSetsAsList.get(idx);
			cumulativeChanges += cs.changesList.size();
			if (cs.metricDelta[MetricType.PNL.ordinal()] > 0) {
				value += (cs.metricDelta[MetricType.PNL.ordinal()] / cumulativeChanges);
			} else {
				boolean foundPositive = false;
				long currentSum = cs.metricDelta[MetricType.PNL.ordinal()];
				long currentChanges = cumulativeChanges;
				// look for a net +ve
				for (int i = idx + 1; i < js.changeSetsAsList.size(); i++) {
					currentSum += js.changeSetsAsList.get(i).metricDelta[MetricType.PNL.ordinal()];
					currentChanges += js.changeSetsAsList.get(i).changesList.size();
					if (currentSum > 0) {
						value += (currentSum / currentChanges);
						cumulativeChanges = currentChanges;
						idx = i;
						foundPositive = true;
						break;
					}
				}
				if (!foundPositive) {
					// no positive to roll into so just add value
					value += cs.metricDelta[MetricType.PNL.ordinal()];
				}
			}
			idx++;
		}
		return value;
	}

}
