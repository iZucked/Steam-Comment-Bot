/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

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
import java.util.List;

import javax.imageio.ImageIO;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.options.InvalidArgumentException;
import com.mmxlabs.common.options.InvalidOptionException;
import com.mmxlabs.common.options.Option;
import com.mmxlabs.common.options.Options;
import com.mmxlabs.common.options.OptionsException;

public class DistanceMatrixCreator {

	private String imageFilePath;
	private String outputFilePath;
	private String coordinatesFilePath;
	private double maxLatitude;
	private double resolution;

	public double getResolution() {
		return resolution;
	}

	@Option(defaultValue = "0.5", help = "How many grid squares per pixel of input, roughly")
	public void setResolution(double resolution) {
		this.resolution = resolution;
	}

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
		final int lonRes = (int) (getResolution() * landMatrix.length);
		final int latRes = (int) (getResolution() * landMatrix[0].length * 90 / getMaxLatitude());
		System.err.println("Map resolution: " + latRes + "x" + lonRes);
		final NauticalDistanceCalculator calculator = new NauticalDistanceCalculator(
				landMatrix, getMaxLatitude(), latRes, lonRes);

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

		final int[][] matrix = new int[ports.size()][ports.size()];
		final ArrayList<Pair<Double, Double>> otherPorts = new ArrayList<Pair<Double, Double>>();
		for (int i = 0; i < matrix.length; i++) {
			final Pair<Double, Double> startPort = ports.get(i).getSecond();
			otherPorts.clear();
			for (int j = 0; j < i; j++)
				otherPorts.add(ports.get(j).getSecond());
			System.err.println("Calculating distances from " + ports.get(i));
			final List<Integer> distances = calculator.getMultipleDistances(
					startPort, otherPorts);

			for (int j = 0; j < i; j++) {
				matrix[i][j] = matrix[j][i] = distances.get(j);
			}
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
	}

	private boolean isLand(int pixel) {
		return !(ColorModel.getRGBdefault().getRed(pixel) == 0
				&& ColorModel.getRGBdefault().getGreen(pixel) == 0 && ColorModel
				.getRGBdefault().getBlue(pixel) == 255);
	}

	public String getImageFilePath() {
		return imageFilePath;
	}

	@Option(defaultValue = "earth.png", help = "Image to load (white = sea, other colours = land)")
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	@Option(defaultValue = "distances.csv", help = "Distance matrix file name (output)")
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	@Option(defaultValue = "70", help = "Maximum/minimum latitude of image in degrees")
	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	public String getCoordinatesFilePath() {
		return coordinatesFilePath;
	}

	@Option(defaultValue = "ports.csv", help = "List of port coordinates")
	public void setCoordinatesFilePath(String coordinatesFilePath) {
		this.coordinatesFilePath = coordinatesFilePath;
	}
}
