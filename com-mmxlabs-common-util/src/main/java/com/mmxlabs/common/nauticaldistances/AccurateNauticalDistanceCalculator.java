package com.mmxlabs.common.nauticaldistances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.mmxlabs.common.Pair;

public class AccurateNauticalDistanceCalculator {
	final int[][] distanceMatrix;
	final List<double[]> coast;

	public AccurateNauticalDistanceCalculator(
			final boolean[][] mercatorLandMatrix, final double dmaxLatitude) {
		final int width = mercatorLandMatrix.length;
		final int height = mercatorLandMatrix[0].length;
		// transform coast pixels to points in cartesian 3-space on the unit
		// sphere

		final double maxLatitude = dmaxLatitude * Math.PI / 180;

		final double maxProjectedLat = Math.log(Math.tan(maxLatitude) + 1
				/ Math.cos(maxLatitude));

		coast = new ArrayList<double[]>();
		for (final Pair<Integer, Integer> coastxy : findCoastalPixels(mercatorLandMatrix)) {
			coast.add(pixelTo3dPoint(coastxy, width, height, maxLatitude,
					maxProjectedLat));
		}

		System.err.println("Coast has " + coast.size() + " points");

		// now fill distance matrix for these points
		distanceMatrix = new int[coast.size()][coast.size()];
		int badEdges = 0;
		int goodEdges = 0;
		int percent = 0;
		for (int i = 0; i < coast.size(); i++) {
			int newPercent = 100 * i / coast.size();
			if (newPercent != percent) {
				percent = newPercent;
				System.err.print("" + percent + "% ");
				System.err.flush();
			}
			for (int j = 0; j < i; j++) {
				if (lineOfSight(coast.get(i), coast.get(j), mercatorLandMatrix,
						maxLatitude, maxProjectedLat)) {
					distanceMatrix[j][i] = distanceMatrix[i][j] = greatCircleDistance(
							coast.get(i), coast.get(j));
					goodEdges++;
				} else {
					distanceMatrix[j][i] = distanceMatrix[i][j] = Integer.MAX_VALUE;
					badEdges++;
				}
			}
			distanceMatrix[i][i] = 0;
		}

		System.err.println();

//		for (int[] x : distanceMatrix) {
//			System.err.println(Arrays.toString(x));
//		}
		System.err.println("Sparseness = " + goodEdges
				/ (double) (badEdges + goodEdges));
		// distance matrix is computed, now ready for normal use
	}

	/**
	 * Get the shortest distances from origin to each point in the list, after
	 * snapping points to their closest points on the coast.
	 * 
	 * @param origin
	 * @param latLonPairs
	 * @return
	 */
	public List<Integer> getShortestPaths(final Pair<Double, Double> origin,
			final List<Pair<Double, Double>> latLonPairs) {
		List<Integer> results = new ArrayList<Integer>();

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
				Integer v1 = distances[o1];
				Integer v2 = distances[o2];
				return v1.compareTo(v2);
			}
		};

		final PriorityQueue<Integer> queue = new PriorityQueue<Integer>(
				coast.size(), comparator);
		final Set<Integer> remaining = new HashSet<Integer>();
		for (int i = 0; i < coast.size(); i++) {
			remaining.add(i);
			queue.add(i);
		}

		while (!allTrue(marks, endPoints)) {
			final int next = queue.remove(); // get closest guy
			marks[next] = true;
			remaining.remove(next);

			if (distances[next] == Integer.MAX_VALUE)
				break;

			for (final int nbr : remaining) {
				if (distanceMatrix[next][nbr] == Integer.MAX_VALUE)
					continue;
				final int altDistance = distances[next]
						+ distanceMatrix[next][nbr];
				if (altDistance < distances[nbr]) {
					// relax nbr
					queue.remove(nbr);
					distances[nbr] = altDistance;
					queue.add(nbr);
				}
			}
		}

		for (int i : endPoints) {
			results.add(distances[i]);
		}

		return results;
	}

	private boolean allTrue(final boolean[] marks, final int[] endPoints) {
		for (final int p : endPoints)
			if (marks[p] == false)
				return false;
		return true;
	}

	/**
	 * Snap a lat/lon pair to a coast point. Input are in degrees, coast in
	 * radians. find nearest coast point
	 * 
	 * @param pair
	 * @return
	 */
	private int snap(final Pair<Double, Double> pair) {
		final double lat = pair.getFirst() * Math.PI / 180;
		final double lon = pair.getSecond() * Math.PI / 180;

		final double[] xyz = new double[] { Math.sin(lat) * Math.cos(lon),
				Math.sin(lat) * Math.sin(lon), Math.cos(lat) };

		double minDistance = Double.MAX_VALUE;
		int winner = -1;

		for (int i = 0; i < coast.size(); i++) {
			final double d = greatCircleDistance(xyz, coast.get(i));
			if (d < minDistance) {
				minDistance = d;
				winner = i;
			}
		}

		return winner;
	}

	/**
	 * Compute the great-circle distance in nautical miles between the two
	 * points represented as vectors to points on the unit sphere (scaled up to
	 * earth radius)
	 * 
	 * @param pair
	 * @param pair2
	 * @return
	 */
	private int greatCircleDistance(final double[] p1, final double[] p2) {
		// length of arc between them, scaled up. always the inner angle.
		final double angle = angleBetween(p1, p2);
		return (int) Math.floor(RAD_KNOTS * angle); // radians, yay
	}

	private static final double RAD_KNOTS = 3443.89849;

	/**
	 * Test all the pixels between cartesian normal sphere points v1 and v2 in
	 * the given mercator matrix, and return true if and only if they are all
	 * sea pixels.
	 * 
	 * @param v1
	 * @param v2
	 * @param mercatorLandMatrix
	 * @param maxLatitude
	 * @param maxProjectedLatitude
	 * @return
	 */
	private boolean lineOfSight(final double[] v1, final double[] v2,
			final boolean[][] mercatorLandMatrix, final double maxLatitude,
			double maxProjectedLatitude) {
		// find angle between v1 and v2;
		double angle = angleBetween(v1, v2);

		// sweep along great circle by rotating v1 by a small quantity until it
		// coincides with v2

		// in mercator, distances are smallest at the equator
		final double rotationAngle = Math.PI * 2
				/ (mercatorLandMatrix.length * 2);

		if (angle < 2 * rotationAngle)
			return true;

		final double[] axis = cross(v1, v2);
		normalise(axis);

		final double[][] rotationMatrix = rotator(axis, rotationAngle);

		double[] pt = v1;
		// System.err.println("Sweeping through "+angle + " rads in " +
		// rotationAngle + " jumps");
		while (angle > 0) {
			pt = applyMatrix(rotationMatrix, pt);
			normalise(pt);// /just in case
			// project out point and test land/sea. should be fine as pt is
			// unitary
			final double lat = Math.asin(pt[2]);
			final double lon = Math.atan2(pt[1], pt[0]);
			// System.err.println("Sweep : " + lat + ", " + lon);
			if (getMercatorPoint(mercatorLandMatrix, maxLatitude,
					maxProjectedLatitude, lat, lon))
				return false;

			angle -= rotationAngle;
		}

		return true;
	}

	private boolean getMercatorPoint(boolean[][] mercatorLandMatrix,
			double maxLatitude, double lat, double lon,
			double maxProjectedLatitude) {
		// lookup lat/lon in matrix
		// 1. everything out of range is land

		final int mapWidth = mercatorLandMatrix.length;
		final int mapHeight = mercatorLandMatrix[0].length;

		if (Math.abs(lat) > maxLatitude) {
			return true;
		}
		// compute mercator position
		final double px = ((lon / Math.PI) + 1) / 2;
		final double py = (((Math.log(Math.tan(lat) + 1 / Math.cos(lat)) / maxProjectedLatitude))

		+ 1) / 2;

		// System.err.println(px + ", " + py);

		return mercatorLandMatrix[(int) Math.floor(px * mapWidth)][(int) Math
				.floor((1 - py) * mapHeight - 1)];
	}

	/**
	 * In place multiply pt by rotationMatrix
	 * 
	 * @param rotationMatrix
	 * @param pt
	 */
	private double[] applyMatrix(double[][] M, double[] V) {
		return new double[] { M[0][0] * V[0] + M[0][1] * V[1] + M[0][2] * V[2],
				M[1][0] * V[0] + M[1][1] * V[1] + M[1][2] * V[2],
				M[2][0] * V[0] + M[2][1] * V[1] + M[2][2] * V[2] };
	}

	/**
	 * Generate a rotation matrix around axis for given angle
	 * 
	 * @param axis
	 * @param rotationAngle
	 * @return
	 */
	private double[][] rotator(double[] axis, double rotationAngle) {
		final double c = Math.cos(rotationAngle);
		final double s = Math.sin(rotationAngle);
		final double t = 1 - c;

		final double x = axis[0];
		final double y = axis[1];
		final double z = axis[2];

		return new double[][] {
				{ t * x * x + c, t * x * y - z * s, t * x * z + y * s },
				{ t * x * y + z * s, t * y * y + c, t * y * z - x * s },
				{ t * x * z - y * s, t * y * z + x * s, t * z * z + c } };
	}

	/**
	 * Cross product of v1 and v2
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	private double[] cross(double[] v1, double[] v2) {
		double[] result = new double[3];
		result[0] = v1[1] * v2[2] - v1[2] * v2[1];
		result[1] = v1[2] * v2[0] - v1[0] * v2[2];
		result[2] = v1[0] * v2[1] - v1[1] * v2[0];
		return result;
	}

	private double length(final double[] v1) {
		return Math.sqrt(v1[0] * v1[0] + v1[1] * v1[1] + v1[2] * v1[2]);
	}

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
		final double dot = v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];

		final double l1 = length(v1);
		final double l2 = length(v2);

		return Math.acos(dot / (l1 * l2));
	}

	private double[] pixelTo3dPoint(final Pair<Integer, Integer> xy,
			final int w, final int h, final double maxLatitude,
			double maxProjectedLat) {
		// undo mercator projection
		final double lon = Math.PI * 2 * ((xy.getFirst() / (double) w) - 0.5);
		final double y = maxProjectedLat * 2
				* ((1 - (xy.getSecond() / (double) h)) - 0.5); // flip
																// vertically
																// and shift
																// equator to
																// middle
		final double lat = 2 * Math.atan(Math.exp(y)) - Math.PI / 2.0;

		// convert to cartesian 3d unit vector
		return new double[] { Math.sin(lat) * Math.cos(lon),
				Math.sin(lat) * Math.sin(lon), Math.cos(lat) };
	}

	private List<Pair<Integer, Integer>> findCoastalPixels(
			final boolean[][] landMatrix) {
		List<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer, Integer>>();

		for (int x = 0; x < landMatrix.length; x++) {
			for (int y = 0; y < landMatrix[0].length; y++) {
				if (landMatrix[x][y] == false
						&& hasLandNeighbour(landMatrix, x, y))
					result.add(new Pair<Integer, Integer>(x, y));
			}
		}

		return result;
	}

	private boolean hasLandNeighbour(boolean[][] m, int x, int y) {
		final int width = m.length;
		final int height = m[0].length;

		final int yu = clip(height, y + 1);
		final int yd = clip(height, y - 1);
		final int xu = clip(width, x + 1);
		final int xd = clip(width, x - 1);

		return m[x][yu] || m[x][yd] || m[xu][y] || m[xu][yu] || m[xu][yd]
				|| m[xd][y] || m[xd][yu] || m[xd][yd];
	}

	private int clip(int width, int i) {
		if (i < 0)
			return i + width;
		if (i >= width)
			return i - width;
		return i;
	}
}
