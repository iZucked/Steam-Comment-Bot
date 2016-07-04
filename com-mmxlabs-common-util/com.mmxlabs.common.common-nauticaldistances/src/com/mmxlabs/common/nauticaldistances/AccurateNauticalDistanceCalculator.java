/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.nauticaldistances;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;

/**
 * Nautical distance matrix calculator which models the earth as a sphere.
 * 
 * Given a bitmap of passable/impassable terrain (passable = false, impassable = true), finds all the "coastal" pixels (passable with impassable in one of the cardinal directions), and then computes
 * line-of-sight distances by sweeping out a great circle between pairs of coastal pixels and testing for impassable terrain in between them. Finally, given the line-of-sight great-circle distances
 * uses Dijkstra's algorithm to find the shortest navigable path between pairs of coastal points. Not very fast, because of lots of matrix maths.
 * 
 * Copyright (C) Minimax Labs Ltd., 2010 All rights reserved.
 * 
 * @author hinton
 * 
 */
public class AccurateNauticalDistanceCalculator {

	private final Logger log = LoggerFactory.getLogger(AccurateNauticalDistanceCalculator.class);

	final int[][] distanceMatrix;
	final List<double[]> coast;

	public AccurateNauticalDistanceCalculator(final boolean[][] mercatorLandMatrix, final double dmaxLatitude) {
		final int width = mercatorLandMatrix.length;
		final int height = mercatorLandMatrix[0].length;
		// transform coast pixels to points in cartesian 3-space on the unit
		// sphere

		final double maxLatitude = (dmaxLatitude * Math.PI) / 180;

		final double maxProjectedLat = Math.log(Math.tan(maxLatitude) + (1 / Math.cos(maxLatitude)));

		coast = new ArrayList<double[]>();
		for (final Pair<Integer, Integer> coastxy : findCoastalPixels(mercatorLandMatrix)) {
			coast.add(pixelTo3dPoint(coastxy, width, height, maxLatitude, maxProjectedLat));
		}

		log.info("Coast has " + coast.size() + " points");

		// now fill distance matrix for these points
		distanceMatrix = new int[coast.size()][coast.size()];
		int badEdges = 0;
		int goodEdges = 0;
		int percent = 0;
		for (int i = 0; i < coast.size(); i++) {
			final int newPercent = (100 * i) / coast.size();
			if (newPercent != percent) {
				percent = newPercent;
				log.info("" + percent + "% ");
			}
			for (int j = 0; j < i; j++) {
				if (lineOfSight(coast.get(i), coast.get(j), mercatorLandMatrix, maxLatitude, maxProjectedLat)) {
					distanceMatrix[j][i] = distanceMatrix[i][j] = greatCircleDistance(coast.get(i), coast.get(j));
					goodEdges++;
				} else {
					distanceMatrix[j][i] = distanceMatrix[i][j] = Integer.MAX_VALUE;
					badEdges++;
				}
			}
			distanceMatrix[i][i] = 0;
		}

		// for (int[] x : distanceMatrix) {
		// log.info(Arrays.toString(x));
		// }
		log.info("Sparseness = " + (goodEdges / (double) (badEdges + goodEdges)));
		// distance matrix is computed, now ready for normal use
	}

	/**
	 * Get the shortest distances from origin to each point in the list, after snapping all points to their nearest (by great-circle distance) coastal points.
	 * 
	 * In principle runs the risk of snapping a point to something on the other side of a landmass, but so far I haven't seen that happen.
	 * 
	 * @param origin
	 * @param latLonPairs
	 * @return
	 */
	public List<Integer> getShortestPaths(final Pair<Double, Double> origin, final List<Pair<Double, Double>> latLonPairs) {
		final List<Integer> results = new ArrayList<Integer>();

		final int startPoint = snap(origin);
		final int[] endPoints = new int[latLonPairs.size()];
		for (int i = 0; i < latLonPairs.size(); i++) {
			endPoints[i] = snap(latLonPairs.get(i));
		}

		final boolean[] marks = new boolean[coast.size()];
		final int[] distances = new int[coast.size()];
		Arrays.fill(distances, Integer.MAX_VALUE);
		distances[startPoint] = 0;

		final Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(final Integer o1, final Integer o2) {
				final Integer v1 = distances[o1];
				final Integer v2 = distances[o2];
				return v1.compareTo(v2);
			}
		};

		final PriorityQueue<Integer> queue = new PriorityQueue<Integer>(coast.size(), comparator);
		final Set<Integer> remaining = new HashSet<Integer>();
		for (int i = 0; i < coast.size(); i++) {
			remaining.add(i);
			queue.add(i);
		}

		while (!allTrue(marks, endPoints)) {
			final int next = queue.remove(); // get closest guy
			marks[next] = true;
			remaining.remove(next);

			if (distances[next] == Integer.MAX_VALUE) {
				break;
			}

			for (final int nbr : remaining) {
				if (distanceMatrix[next][nbr] == Integer.MAX_VALUE) {
					continue;
				}
				final int altDistance = distances[next] + distanceMatrix[next][nbr];
				if (altDistance < distances[nbr]) {
					// relax nbr
					queue.remove(nbr);
					distances[nbr] = altDistance;
					queue.add(nbr);
				}
			}
		}

		for (final int i : endPoints) {
			results.add(distances[i]);
		}

		return results;
	}

	private boolean allTrue(final boolean[] marks, final int[] endPoints) {
		for (final int p : endPoints) {
			if (marks[p] == false) {
				return false;
			}
		}
		return true;
	}

	public List<double[]> shortestPath(final Pair<Double, Double> from, final Pair<Double, Double> to) {
		final ArrayList<double[]> result = new ArrayList<double[]>();

		final int before[] = new int[coast.size()];

		final int startPoint = snap(from);
		int endPoint = snap(to);
		before[startPoint] = -1;

		final boolean[] marks = new boolean[coast.size()];
		final int[] distances = new int[coast.size()];
		Arrays.fill(distances, Integer.MAX_VALUE);
		distances[startPoint] = 0;

		final Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(final Integer o1, final Integer o2) {
				final Integer v1 = distances[o1];
				final Integer v2 = distances[o2];
				return v1.compareTo(v2);
			}
		};

		final PriorityQueue<Integer> queue = new PriorityQueue<Integer>(coast.size(), comparator);
		final Set<Integer> remaining = new HashSet<Integer>();
		for (int i = 0; i < coast.size(); i++) {
			remaining.add(i);
			queue.add(i);
		}

		while (!marks[endPoint]) {
			final int next = queue.remove(); // get closest guy
			marks[next] = true;
			remaining.remove(next);

			if (distances[next] == Integer.MAX_VALUE) {
				break;
			}

			for (final int nbr : remaining) {
				if (distanceMatrix[next][nbr] == Integer.MAX_VALUE) {
					continue;
				}
				final int altDistance = distances[next] + distanceMatrix[next][nbr];
				if (altDistance < distances[nbr]) {
					// relax nbr
					before[nbr] = next;
					queue.remove(nbr);
					distances[nbr] = altDistance;
					queue.add(nbr);
				}
			}
		}

		while (true) {
			final double[] pt = coast.get(endPoint);

			result.add(new double[] { pt[0], pt[1], pt[2], distances[endPoint] });
			endPoint = before[endPoint];
			if (endPoint == -1) {
				break;
			}
		}

		return result;
	}

	/**
	 * Snap a lat/lon pair to a coast point. Input are in degrees, coast in radians. find nearest coast point
	 * 
	 * @param pair
	 * @return
	 */
	private int snap(final Pair<Double, Double> pair) {
		final double lat = (pair.getFirst() * Math.PI) / 180;
		final double lon = (pair.getSecond() * Math.PI) / 180;

		final double[] xyz = new double[] { Math.cos(lat) * Math.cos(lon), Math.cos(lat) * Math.sin(lon), Math.sin(lat) };

		double minDistance = Double.MAX_VALUE;
		int winner = -1;

		for (int i = 0; i < coast.size(); i++) {
			final double d = greatCircleDistance(xyz, coast.get(i));
			if (d < minDistance) {
				minDistance = d;
				winner = i;
			}
		}

		// System.err.println("Snapped " + Arrays.toString(xyz) + " to " +
		// Arrays.toString(coast.get(winner)));

		return winner;
	}

	/**
	 * Compute the great-circle distance in nautical miles between the two points represented as vectors to points on the unit sphere (scaled up to earth radius)
	 * 
	 * @param pair
	 * @param pair2
	 * @return
	 */
	private int greatCircleDistance(final double[] p1, final double[] p2) {
		// length of arc between them, scaled up. always the inner angle.
		// final double angle = angleBetween(p1, p2);
		// return (int) Math.floor(RAD_KNOTS * angle); // radians, yay

		return (int) Math.floor(RAD_KNOTS * latLonDistance(cartesianToLatLon(p1), cartesianToLatLon(p2)));
	}

	private static final double RAD_KNOTS = 3443.89849;

	private double[] cartesianToLatLon(final double[] pt) {
		return new double[] { Math.asin(pt[2]), Math.atan2(pt[1], pt[0]) };
	}

	private double latLonDistance(final double[] ll1, final double[] ll2) {
		return 2 * Math.asin(Math.sqrt(Math.pow(Math.sin((ll1[0] - ll2[0]) / 2), 2) + (Math.cos(ll1[0]) * Math.cos(ll2[0]) * Math.pow(Math.sin((ll1[1] - ll2[1]) / 2), 2))));
	}

	/**
	 * Test all the pixels between cartesian normal sphere points v1 and v2 in the given mercator matrix, and return true if and only if they are all sea pixels.
	 * 
	 * @param v1
	 * @param v2
	 * @param mercatorLandMatrix
	 * @param maxLatitude
	 * @param maxProjectedLatitude
	 * @param loslog
	 * @return
	 * @throws IOException
	 */
	private boolean lineOfSight(final double[] v1, final double[] v2, final boolean[][] mercatorLandMatrix, final double maxLatitude, final double maxProjectedLatitude) {
		// find angle between v1 and v2;
		final double angle = angleBetween(v1, v2);
		// System.err.println("Angle = " + angle);
		// sweep along great circle by rotating v1 by a small quantity until it
		// coincides with v2

		// in mercator, distances are smallest at the equator
		final double rotationAngle = (Math.PI * 2) / (mercatorLandMatrix.length * 1.5);

		if (angle < (4 * rotationAngle)) {
			return true;
		}

		final double[] ll1 = cartesianToLatLon(v1);
		final double[] ll2 = cartesianToLatLon(v2);

		final double d = latLonDistance(ll1, ll2);
		final double sind = Math.sin(d);

		double f = 0;
		final double deltaf = rotationAngle / angle;
		// StringBuffer buffer = new StringBuffer();
		// buffer.append("0,0,0\n");
		while (f < 1) {
			final double A = Math.sin((1 - f) * d) / sind;
			final double B = Math.sin(f * d) / sind;
			final double x = (A * Math.cos(ll1[0]) * Math.cos(ll1[1])) + (B * Math.cos(ll2[0]) * Math.cos(ll2[1]));
			final double y = (A * Math.cos(ll1[0]) * Math.sin(ll1[1])) + (B * Math.cos(ll2[0]) * Math.sin(ll2[1]));
			final double z = (A * Math.sin(ll1[0])) + (B * Math.sin(ll2[0]));

			final double lat = Math.atan2(z, Math.sqrt((x * x) + (y * y)));
			final double lon = Math.atan2(y, x);

			// buffer.append(x+","+y+","+z+"\n");

			if (getMercatorPoint(mercatorLandMatrix, maxLatitude, lat, lon, maxProjectedLatitude)) {
				return false;
			}

			f += deltaf;
		}
		// loslog.write(buffer.toString());
		return true;

		// if (angle < 3 * rotationAngle)
		// return true;
		//
		// double[] axis = cross(v1, v2);
		// normalise(axis);
		//
		// double[][] rotationMatrix = rotator(axis, rotationAngle);
		//
		// double[] pt = v1;
		// // System.err.println("Sweeping through "+angle + " rads in " +
		// // rotationAngle + " jumps");
		//
		// // System.err
		// // .println("Sweep from " + Arrays.toString(v1) + " to "
		// // + Arrays.toString(v2) + " " + rotationAngle
		// // + " rads at a time");
		//
		// int bad = 0;
		// // StringBuffer sb = new StringBuffer();
		// // sb.append(pt[0] + ", " + pt[1] + ", "+pt[2] +"\n");
		//
		// int applications = 0;
		//
		// // loslog.write(pt[0] + "," + pt[1] + "," + pt[2] + "\n");
		// while (angle > rotationAngle * 3) {
		// applications++;
		// if (applications >= 10) {
		// //Reset matrix, due to cumulative drift problems.
		// //this ensure that the matrix always points towards the destination
		// axis = cross(pt, v2);
		// rotationMatrix = rotator(axis, rotationAngle);
		// }
		//
		// pt = applyMatrix(rotationMatrix, pt);
		// normalise(pt);// /just in case
		// // sb.append(pt[0] + ", " + pt[1] + ", "+pt[2] +"\n");
		// // loslog.write(pt[0] + ", " + pt[1] + ", " + pt[2] + "\n");
		// // project out point and test land/sea. should be fine as pt is
		// // unitary
		// final double lat = Math.asin(pt[2]);
		// final double lon = Math.atan2(pt[1], pt[0]);
		//
		// // System.err.println("Testing point " + Arrays.toString(pt) + " ("
		// // + lat + ", " + lon + ")");
		//
		// // System.err.println("Sweep : " + lat + ", " + lon);
		// if (getMercatorPoint(mercatorLandMatrix, maxLatitude, lat, lon,
		// maxProjectedLatitude)) {
		// bad++;
		// if (bad > 0)
		// return false;
		// } else {
		// bad = 0;
		// }
		//
		// angle = angleBetween(pt, v2);
		// }
		// // loslog.write("0,0,0\n");
		// // sb.append(v2[0] + ", " + v2[1] + ", "+v2[2] +"\n");
		// // loslog.write(sb.toString());
		// return true;
	}

	private boolean getMercatorPoint(final boolean[][] mercatorLandMatrix, final double maxLatitude, final double lat, final double lon, final double maxProjectedLatitude) {
		// lookup lat/lon in matrix
		// 1. everything out of range is land

		final int mapWidth = mercatorLandMatrix.length - 1;
		final int mapHeight = mercatorLandMatrix[0].length - 1;

		if (Math.abs(lat) > maxLatitude) {
			return true;
		}
		// compute mercator position
		final double px = ((lon / Math.PI) + 1) / 2;
		final double py = (((((Math.log(Math.tan(lat) + (1 / Math.cos(lat))) / maxProjectedLatitude)) // -1..1
		) * -1) // upside down
				+ 1) / 2; // all positive, scaled to fit in unit

		final boolean answer = mercatorLandMatrix[(int) Math.floor(px * mapWidth)][(int) Math.floor(py * mapHeight)];

		// System.err.println(lat + ", " + lon + " => " + px + ", " + px
		// * mapWidth + ", " + py * mapHeight + " = " + answer);

		return answer;
	}

	/**
	 * In place multiply pt by rotationMatrix
	 * 
	 * @param rotationMatrix
	 * @param pt
	 */
	@SuppressWarnings("unused")
	private double[] applyMatrix(final double[][] M, final double[] V) {
		return new double[] { dot(M[0], V), dot(M[1], V), dot(M[2], V) };
	}

	private double dot(final double[] a, final double[] b) {
		return (a[0] * b[0]) + (a[1] * b[1]) + (a[2] * b[2]);
	}

	/**
	 * Generate a rotation matrix around axis for given angle
	 * 
	 * @param axis
	 * @param rotationAngle
	 * @return
	 */
	@SuppressWarnings("unused")
	private double[][] rotator(final double[] axis, final double rotationAngle) {
		final double c = Math.cos(rotationAngle);
		final double s = Math.sin(rotationAngle);
		final double t = 1 - c;

		final double x = axis[0];
		final double y = axis[1];
		final double z = axis[2];

		return new double[][] { { (t * x * x) + c, (t * x * y) - (s * z), (t * x * z) + (s * y) }, { (t * x * y) + (s * z), (t * y * y) + c, (t * y * z) - (s * x) },
				{ (t * x * z) - (s * y), (t * y * z) + (x * s), (t * z * z) + c } };
	}

	/**
	 * Cross product of v1 and v2
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	@SuppressWarnings("unused")
	private double[] cross(final double[] v1, final double[] v2) {
		return new double[] { (v1[1] * v2[2]) - (v1[2] * v2[1]), (v1[2] * v1[0]) - (v1[0] * v2[2]), (v1[0] * v2[1]) - (v1[1] * v2[0]) };
	}

	private double length(final double[] v1) {
		return Math.sqrt((v1[0] * v1[0]) + (v1[1] * v1[1]) + (v1[2] * v1[2]));
	}

	@SuppressWarnings("unused")
	private void normalise(final double[] v) {
		final double l = length(v);
		v[0] /= l;
		v[1] /= l;
		v[2] /= l;
	}

	/**
	 * Inner product angle between v1 and v2
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	private double angleBetween(final double[] v1, final double[] v2) {
		final double dot = dot(v1, v2);

		final double l1 = length(v1);
		final double l2 = length(v2);

		return Math.acos(dot / (l1 * l2));
	}

	private double[] pixelTo3dPoint(final Pair<Integer, Integer> xy, final int w, final int h, final double maxLatitude, final double maxProjectedLat) {
		// undo mercator projection
		final double lon = Math.PI * 2 * ((xy.getFirst() / (double) w) - 0.5);
		final double y = maxProjectedLat * 2 * ((1 - (xy.getSecond() / (double) h)) - 0.5); // flip
																							// vertically
																							// and shift
																							// equator to
																							// middle
		final double lat = Math.atan(Math.sinh(y));

		// convert to cartesian 3d unit vector

		return new double[] { Math.cos(lat) * Math.cos(lon), Math.cos(lat) * Math.sin(lon), Math.sin(lat) };
	}

	private List<Pair<Integer, Integer>> findCoastalPixels(final boolean[][] landMatrix) {
		final List<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer, Integer>>();

		for (int x = 0; x < landMatrix.length; x++) {
			for (int y = 0; y < landMatrix[0].length; y++) {
				if ((landMatrix[x][y] == false) && hasLandNeighbour(landMatrix, x, y)) {
					result.add(new Pair<Integer, Integer>(x, y));
				}
			}
		}

		return result;
	}

	private boolean hasLandNeighbour(final boolean[][] m, final int x, final int y) {
		final int width = m.length;
		final int height = m[0].length;

		final int yu = clip(height, y + 1);
		final int yd = clip(height, y - 1);
		final int xu = clip(width, x + 1);
		final int xd = clip(width, x - 1);

		// return m[x][yu] || m[x][yd] || m[xu][y] || m[xu][yu] || m[xu][yd]
		// || m[xd][y] || m[xd][yu] || m[xd][yd];
		return m[xd][y] || m[xu][y] || m[x][yd] || m[x][yu];
	}

	private int clip(final int width, final int i) {
		if (i < 0) {
			return i + width;
		}
		if (i >= width) {
			return i - width;
		}
		return i;
	}

	public void writeCoastalPoints(final BufferedWriter output) throws IOException {
		for (final double[] pt : coast) {
			output.write(pt[0] + "," + pt[1] + "," + pt[2] + "\n");
		}
	}

	public void writeSnappedPoints(final BufferedWriter output, final BufferedWriter realValues, final ArrayList<Pair<Double, Double>> otherPorts) throws IOException {
		for (final Pair<Double, Double> pt : otherPorts) {
			final double[] ptxyz = coast.get(snap(pt));
			final double lat = (pt.getFirst() * Math.PI) / 180;
			final double lon = (pt.getSecond() * Math.PI) / 180;
			final double[] xyz = new double[] { Math.cos(lat) * Math.cos(lon), Math.cos(lat) * Math.sin(lon), Math.sin(lat) };
			realValues.write(xyz[0] + "," + xyz[1] + "," + xyz[2] + "\n");
			output.write(ptxyz[0] + "," + ptxyz[1] + "," + ptxyz[2] + "\n");
		}
	}
}
