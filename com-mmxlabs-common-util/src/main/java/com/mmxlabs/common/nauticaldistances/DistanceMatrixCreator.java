package com.mmxlabs.common.nauticaldistances;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.options.InvalidArgumentException;
import com.mmxlabs.common.options.InvalidOptionException;
import com.mmxlabs.common.options.Option;
import com.mmxlabs.common.options.Options;
import com.mmxlabs.common.options.OptionsException;

/**
 * Command-line interface for {@link AccurateNauticalDistanceCalculator}.
 * 
 * Sample inputs are in the data/nauticaldistances directory; the program
 * requires two input files:
 * <ol>
 * <li>
 * a picture of the earth, as a mercator projection, in which pure blue is
 * terrain passable by vessels, and non-blue is impassable.</li>
 * <li>
 * a list of ports, as a CSV file, whose first three columns are port name, port
 * latitude and port longitude in that order</li>
 * </ol>
 * Run with --help argument for more help.
 * 
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 * @author hinton
 * 
 */
public class DistanceMatrixCreator {

	private String imageFilePath;
	private String outputFilePath;
	private String coordinatesFilePath;

	/**
	 * @param args
	 * @throws InvalidArgumentException
	 * @throws InvalidOptionException
	 * @throws IOException
	 */
	public static void main(String[] args) {
		final DistanceMatrixCreator me = new DistanceMatrixCreator();
		final Options options = new Options();

		try {
			options.parseAndSet(args, me);
		} catch (OptionsException e1) {
			System.err.println("Error: " + e1.getMessage());
			System.err.println(options.getHelp());
			return;
		}

		try {
			me.run();
		} catch (IOException e) {
			System.err.println(e);
		}

	}

	private void run() throws IOException {
		final BufferedImage image = ImageIO.read(new File(getImageFilePath()));

		final boolean[][] landMatrix = new boolean[image.getWidth()][image
				.getHeight()];
		for (int i = 0; i < landMatrix.length; i++) {
			for (int j = 0; j < landMatrix[i].length; j++) {
				final int pixel = image.getRGB(i, j);
				landMatrix[i][j] = isLand(pixel);
			}
		}

		final double mlat = Math.atan(2 * Math.PI * image.getHeight()
				/ (double) image.getWidth())
				* 180 / Math.PI;

		System.err.println("Presuming max. latitude = " + mlat);
		System.err.println("Computing line-of-sight distance matrix");

		// BufferedWriter loslog = new BufferedWriter(new
		// FileWriter("./los.txt"));
		final AccurateNauticalDistanceCalculator calculator = new AccurateNauticalDistanceCalculator(
				landMatrix, mlat);
		// loslog.close();

		{
			BufferedWriter coast = new BufferedWriter(new FileWriter(
					"./coast.txt"));
			calculator.writeCoastalPoints(coast);
			coast.close();
			System.err
					.println("Coastal points written to coast.txt (cartesian)");
		}

		final BufferedReader portReader = new BufferedReader(new FileReader(
				getCoordinatesFilePath()));
		final List<Pair<String, Pair<Double, Double>>> ports = new ArrayList<Pair<String, Pair<Double, Double>>>();

		String line;
		while ((line = portReader.readLine()) != null) {
			String[] parts = line.split(",");
			ports.add(new Pair<String, Pair<Double, Double>>(parts[0],
					new Pair<Double, Double>(Double.parseDouble(parts[1]),
							Double.parseDouble(parts[2]))));
		}

		Collections.sort(ports,
				new Comparator<Pair<String, Pair<Double, Double>>>() {
					@Override
					public int compare(Pair<String, Pair<Double, Double>> o1,
							Pair<String, Pair<Double, Double>> o2) {
						return o1.getFirst().compareTo(o2.getFirst());
					}

				});

		System.err.println("Computing full distance matrix");

		final int[][] matrix = new int[ports.size()][ports.size()];
		final ArrayList<Pair<Double, Double>> otherPorts = new ArrayList<Pair<Double, Double>>();
		for (int i = 0; i < matrix.length; i++) {
			final Pair<Double, Double> startPort = ports.get(i).getSecond();

			System.err.println("Calculating distances from " + ports.get(i));
			final List<Integer> distances = calculator.getShortestPaths(
					startPort, otherPorts);

			for (int j = 0; j < i; j++) {
				matrix[i][j] = matrix[j][i] = distances.get(j);
			}

			otherPorts.add(ports.get(i).getSecond());
		}

		{
			BufferedWriter snap = new BufferedWriter(new FileWriter(
					"./snapports.txt"));
			BufferedWriter real = new BufferedWriter(new FileWriter(
					"./realports.txt"));
			calculator.writeSnappedPoints(snap, real, otherPorts);

			snap.close();
			real.close();
			System.err
					.println("Ports written to realports.txt and snapports.txt");
		}

		// write matrix out
		final BufferedWriter distanceWriter = new BufferedWriter(
				new FileWriter(getOutputFilePath()));
		for (final Pair<String, Pair<Double, Double>> x : ports) {
			distanceWriter.write(",");
			distanceWriter.write(x.getFirst());
		}
		for (int i = 0; i < matrix.length; i++) {
			distanceWriter.write("\n");
			distanceWriter.write(ports.get(i).getFirst());
			distanceWriter.write(",");
			for (int j = 0; j < matrix[i].length; j++) {
				if (j != 0)
					distanceWriter.write(",");
				if (matrix[i][j] != Integer.MAX_VALUE)
					distanceWriter.write(Integer.toString(matrix[i][j]));
			}
		}
		distanceWriter.flush();
		distanceWriter.close();
		System.err.println("Distance matrix written to " + getOutputFilePath());
		// plot all paths;

		// for (Pair<String, Pair<Double, Double>> start : ports) {
		// for (Pair<String, Pair<Double, Double>> end : ports) {
		// List<double[]> path = calculator.shortestPath(start.getSecond(),
		// end.getSecond());
		// BufferedWriter spwriter = new BufferedWriter(new FileWriter("./" +
		// start.getFirst() + "-" + end.getFirst() + ".txt"));
		// for (double[] pt : path) {
		// spwriter.write(pt[0] +","+pt[1]+","+pt[2]+ "," + pt[3] + "\n");
		// }
		// spwriter.close();
		// }
		// }
	}

	private boolean isLand(int pixel) {
		return !(ColorModel.getRGBdefault().getRed(pixel) == 0
				&& ColorModel.getRGBdefault().getGreen(pixel) == 0 && ColorModel
				.getRGBdefault().getBlue(pixel) == 255);
	}

	public String getImageFilePath() {
		return imageFilePath;
	}

	@Option(defaultValue = "earth.png", help = "Image to load. Expects a mercator projection of the Earth with lat/lon 0,0 in the centre. "
			+ "Blue pixels (RGB 0 0 255) are considered sea, and all other pixels are considered land. "
			+ "The projection should be equal-aspect around the equator (so that one pixel across is one pixel up), in order for the maximum latitude to be correctly computed. "
			+ "All terrain above/below the maximum latitude is considered impassable (land).")
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	@Option(defaultValue = "distances.csv", help = "Distance matrix file name (output).")
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	public String getCoordinatesFilePath() {
		return coordinatesFilePath;
	}

	@Option(defaultValue = "ports.csv", help = "List of port coordinates, in a CSV file. Column 0 is port name, column 1 is degrees latitude (-90 to 90) and column 2 degrees longitude (-180 to 180).")
	public void setCoordinatesFilePath(String coordinatesFilePath) {
		this.coordinatesFilePath = coordinatesFilePath;
	}
}
