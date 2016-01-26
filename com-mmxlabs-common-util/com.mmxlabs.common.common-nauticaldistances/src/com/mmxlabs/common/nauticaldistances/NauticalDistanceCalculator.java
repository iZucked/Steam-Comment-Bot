/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.nauticaldistances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;

/**
 * Calculator for port-port distances which uses a simple model of the sea as passable/impassable terrain and computes the shortest path through passable squares.
 * 
 * Assumes the matrix is a rough mercator projection, and the earth is normal-sized for an earth.
 * 
 * Estimates will be over by maybe 5% because of a lack of genuine diagonal lines and the low resolution of the terrain bitmap. A better solution would probably have to find all coastal points,
 * determine which coastal points are great-circle reachable from one another, build a graph on that and then run dijkstra like this.
 * 
 * Copyright (C) Minimax Labs Ltd., 2010 All rights reserved.
 * 
 * @author hinton
 * 
 */
@Deprecated
public class NauticalDistanceCalculator {

	private static final Logger log = LoggerFactory.getLogger(NauticalDistanceCalculator.class);

	private final int longitudeResolution;
	private final int latitudeResolution;
	private static final double RAD_KNOTS = 3443.89849;
	private final boolean[][] landMatrix;
	/**
	 * This matrix will be filled with the shortest path to each square from the current start square
	 */
	private final int[][] distances;
	private Pair<Integer, Integer> startSquare = null;

	public NauticalDistanceCalculator(final boolean[][] mercatorLandMatrix, double verticalRange, final int latitudeResolution, final int longitudeResolution) {
		this.landMatrix = new boolean[latitudeResolution][longitudeResolution];
		this.latitudeResolution = latitudeResolution;
		this.longitudeResolution = longitudeResolution;
		verticalRange = d2r(verticalRange);
		// populate land matrix using mercator projected image

		final double maxProjectedLat = Math.log(Math.tan(verticalRange) + (1 / Math.cos(verticalRange)));

		final int mapWidth = mercatorLandMatrix.length;
		final int mapHeight = mercatorLandMatrix[0].length;

		for (int i = 0; i < latitudeResolution; i++) {
			for (int j = 0; j < longitudeResolution; j++) {
				final double lat = ((i / (double) latitudeResolution) - 0.5) * Math.PI;
				final double lon = ((j / (double) longitudeResolution) - 0.5) * Math.PI * 2;
				if (Math.abs(lat) > verticalRange) {
					this.landMatrix[i][j] = true; // land
				} else {
					// compute mercator position
					final double px = ((lon / Math.PI) + 1) / 2;
					// better not use a longitude higher than the maximum, or
					// this will explode
					final double py = (1 - ((Math.log(Math.tan(lat) + (1 / Math.cos(lat)))) / maxProjectedLat)) / 2;
					// System.err
					// .println(String.format(
					// "%.2f %.2f => %.2f %.2f, %d %d %s",
					// lat,
					// lon,
					// px,
					// py,
					// (int) Math.floor(px * mapWidth),
					// (int) Math.floor(py * mapHeight),
					// mercatorLandMatrix[(int) Math.floor(px
					// * mapWidth)][(int) Math.floor(py
					// * mapHeight)] ? "land" : "sea"));
					this.landMatrix[i][j] = mercatorLandMatrix[(int) Math.floor(px * mapWidth)][(int) Math.floor(((1 - py) * mapHeight) - 1)];
				}
			}
		}

		// for (int i = 0; i < LAT_RES; i++) {
		// System.err.print("|");
		// for (int j = 0; j < LON_RES; j++) {
		// System.err.print(landMatrix[i][j] ? "*" : " ");
		// }
		// System.err.println("|");
		// }

		this.distances = new int[latitudeResolution][longitudeResolution];
	}

	/**
	 * move point to nearest water
	 * 
	 * @param point
	 */
	private void moveToSea(final Pair<Integer, Integer> point) {
		if (landMatrix[point.getFirst()][point.getSecond()]) {
			final int p1 = point.getFirst();
			final int p2 = point.getSecond();

			double distance = Double.MAX_VALUE;

			for (int i = 0; i < latitudeResolution; i++) {
				for (int j = 0; j < latitudeResolution; j++) {
					if (!landMatrix[i][j]) {
						final double d2 = Math.pow(i - p1, 2) + Math.pow(j - p2, 2);
						if (d2 < distance) {
							distance = d2;
							point.setBoth(i, j);
						}
					}
				}
			}
		}
	}

	public List<Integer> getMultipleDistances(final Pair<Double, Double> latlon, final List<Pair<Double, Double>> others) {
		final ArrayList<Integer> result = new ArrayList<Integer>();
		final ArrayList<Pair<Integer, Integer>> ends = new ArrayList<Pair<Integer, Integer>>();

		for (final Pair<Double, Double> p : others) {
			final Pair<Integer, Integer> grid = gridLatLon(p);
			moveToSea(grid);
			ends.add(grid);
		}

		// transform the lat/lon into simple map coordinates
		startSquare = gridLatLon(latlon);
		moveToSea(startSquare);
		log.info("Start square for " + latlon + " is " + startSquare + " " + (landMatrix[startSquare.getFirst()][startSquare.getSecond()] ? "(land)" : "(sea)"));

		// for (int i = 0; i < LAT_RES; i++) {
		// System.err.print("|");
		// for (int j = 0; j < LON_RES; j++) {
		// if (i == startSquare.getFirst() && j == startSquare.getSecond())
		// System.err.print("*");
		// else
		// System.err.print(landMatrix[i][j] ? "." : " ");
		// }
		// System.err.println("|");
		// }

		// flood-fill shortest-path matrix with suitably computed distances
		// 1. reset distances to maximum value
		for (final int[] row : distances) {
			Arrays.fill(row, Integer.MAX_VALUE);
		}
		// 2. set point to be 0 from itself
		distances[startSquare.getFirst()][startSquare.getSecond()] = 0;
		// 3. set up marks; all land is already done
		final boolean[][] marks = new boolean[latitudeResolution][longitudeResolution];
		final int markCount = latitudeResolution * longitudeResolution;
		int percent = -1;
		int markProgress = 0;
		final Pair<Integer, Integer> here = new Pair<Integer, Integer>(startSquare);
		int distanceToHere = distances[here.getFirst()][here.getSecond()];
		while (distanceToHere != Integer.MAX_VALUE) {
			boolean markedEveryEnd = true;
			for (final Pair<Integer, Integer> end : ends) {
				markedEveryEnd = markedEveryEnd && marks[end.getFirst()][end.getSecond()];
			}
			if (markedEveryEnd) {
				break;
			}

			if (landMatrix[here.getFirst()][here.getSecond()] == false) {
				for (final int[] n : neighbours(here)) {
					if (marks[n[0]][n[1]]) {
						continue; // marked entries are done
					}
					if (landMatrix[n[0]][n[1]]) {
						continue; // nowhere is reachable by land
					}

					final int delta = distance(here, n);
					if ((delta + distanceToHere) < distances[n[0]][n[1]]) {
						distances[n[0]][n[1]] = delta + distanceToHere;
					}
				}
			}
			marks[here.getFirst()][here.getSecond()] = true;
			markProgress++;
			// System.err.println(markProgress + " of " + markCount);
			final int newpercent = (100 * markProgress) / markCount;
			if (newpercent != percent) {
				percent = newpercent;
				log.info(percent + "% of sea marked");
			}
			// find next location; ugly and slow, but sufficient
			distanceToHere = Integer.MAX_VALUE;
			for (int i = 0; i < distances.length; i++) {
				for (int j = 0; j < distances[i].length; j++) {
					if (marks[i][j] == false) {
						if (distances[i][j] <= distanceToHere) {
							distanceToHere = distances[i][j];
							here.setBoth(i, j);
						}
					}
				}
			}
		}

		for (final Pair<Integer, Integer> end : ends) {
			result.add(distances[end.getFirst()][end.getSecond()]);
		}

		return result;
	}

	private Pair<Integer, Integer> gridLatLon(final Pair<Double, Double> latlon) {
		return new Pair<Integer, Integer>((int) Math.floor(((latlon.getFirst() + 90) / 180.0) * latitudeResolution), (int) Math.floor(((latlon.getSecond() + 180) / 360.0) * longitudeResolution));
	}

	private double d2r(final double degrees) {
		return Math.PI * (degrees / 180);
	}

	/**
	 * Compute great-circle distance in nautical miles. parameters are a lat/lon index, and n, which has delta-values in elements 2 and 3.
	 * 
	 * @param here
	 * @param n
	 * @return
	 */
	private int distance(final Pair<Integer, Integer> here, final int[] delta) {
		final double latH = (((here.getFirst() / (double) latitudeResolution) - 0.5) * Math.PI) / 2;
		final double latT = ((((here.getFirst() + delta[2]) / (double) latitudeResolution) - 0.5) * Math.PI) / 2;
		final double dLat = (Math.PI * delta[2]) / latitudeResolution;
		final double dLon = (2 * Math.PI * delta[3]) / longitudeResolution;
		return (int) (RAD_KNOTS * 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(dLat / 2), 2) + (Math.cos(latH) * Math.cos(latT) * Math.pow(Math.sin(dLon / 2), 2)))));
	}

	/**
	 * Return an iterable of the neighbours of the given square. Doesn't have to be well behaved (i.e. may just return the same object again and again). Doesn't work at the poles, but doesn't need to.
	 * 
	 * @param marks
	 * @param here
	 * @return
	 */
	private Iterable<int[]> neighbours(final Pair<Integer, Integer> here) {
		return new Iterable<int[]>() {

			@Override
			public Iterator<int[]> iterator() {
				return new Iterator<int[]>() {
					final int[] n = new int[4];
					int neighbour = 0;

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}

					@Override
					public int[] next() {
						switch (neighbour) {
						case 0:
							n[2] = -1;
							n[3] = 0;
							break;
						case 1:
							n[2] = -1;
							n[3] = +1;
							break;
						case 2:
							n[2] = 0;
							n[3] = +1;
							break;
						case 3:
							n[2] = +1;
							n[3] = +1;
							break;
						case 4:
							n[2] = +1;
							n[3] = 0;
							break;
						case 5:
							n[2] = +1;
							n[3] = -1;
							break;
						case 6:
							n[2] = 0;
							n[3] = -1;
							break;
						case 7:
							n[2] = -1;
							n[3] = -1;
							break;
						}
						neighbour++;

						n[0] = here.getFirst() + n[2];
						n[1] = here.getSecond() + n[3];

						// wrap
						if (n[0] < 0) {
							n[0] += latitudeResolution;
						} else if (n[0] >= latitudeResolution) {
							n[0] -= latitudeResolution;
						}

						if (n[1] < 0) {
							n[1] += longitudeResolution;
						} else if (n[1] >= longitudeResolution) {
							n[1] -= longitudeResolution;
						}

						return n;
					}

					@Override
					public boolean hasNext() {
						return neighbour < 7;
					}
				};
			}
		};
	}
}
