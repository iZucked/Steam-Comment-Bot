/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.shared.port.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;
import com.mmxlabs.scheduler.optimiser.shared.port.IDistanceMatrixEditor;

public final class DefaultDistanceMatrixEditor implements IDistanceMatrixEditor {

	private ERouteOption[] preSortedKeys;

	// Index by ERouteOption ordinal, then from port index, then to port index
	private final DistanceMatrixEntry[][][] matrices;

	// Cached min/max value lookups. Note changing an existing distance may invalidate this.
	private DistanceMatrixEntry[][] minima;
	private DistanceMatrixEntry[][] maxima;

	private int maxIndex = -1;

	public DefaultDistanceMatrixEditor() {
		matrices = new DistanceMatrixEntry[ERouteOption.values().length][0][0];
		minima = new DistanceMatrixEntry[0][0];
		maxima = new DistanceMatrixEntry[0][0];
	}

	@Override
	public final int get(final ERouteOption route, final IPort from, final IPort to) {
		// added bounds check - we may not have seen all indexed objects possible.
		final DistanceMatrixEntry[][] matrix = matrices[route.ordinal()];
		final int xi = from.getIndex();
		if (xi >= matrix.length) {
			return Integer.MAX_VALUE;
		}
		final int yi = to.getIndex();
		final DistanceMatrixEntry[] subMatrix = matrix[xi];
		if (yi >= subMatrix.length) {
			return Integer.MAX_VALUE;
		}
		final DistanceMatrixEntry e = subMatrix[yi];
		if (e == null) {
			return Integer.MAX_VALUE;
		}
		return e.getDistance();
	}

	@Override
	public final void set(final ERouteOption route, final IPort from, final IPort to, final int distance) {
		// the resizing mechanism is quite unpleasant, but slow set / fast get
		// seems like a reasonable tradeoff.

		final int xi, yi;
		xi = from.getIndex();
		yi = to.getIndex();
		if ((xi > maxIndex) || (yi > maxIndex)) {
			resize(Math.max(xi, yi));
		}
		final DistanceMatrixEntry[][] matrix = matrices[route.ordinal()];
		matrix[xi][yi] = new DistanceMatrixEntry(route, from, to, distance);

		if (minima[xi][yi] == null || matrix[xi][yi].getDistance() < minima[xi][yi].getDistance()) {
			minima[xi][yi] = matrix[xi][yi];
		}
		if (maxima[xi][yi] == null || matrix[xi][yi].getDistance() > maxima[xi][yi].getDistance()) {
			maxima[xi][yi] = matrix[xi][yi];
		}
	}

	/**
	 * Pre-allocate matrix size;
	 * 
	 * @param capacity
	 */
	public void ensureCapacity(int capacity) {
		if (capacity > maxIndex) {
			resize(capacity);
		}
	}

	/**
	 * Resize all matrices to cover the new port value
	 * 
	 * @param newMax
	 */
	private synchronized void resize(final int newMax) {
		maxIndex = newMax;

		for (final ERouteOption r : ERouteOption.values()) {
			final DistanceMatrixEntry[][] newMatrix = new DistanceMatrixEntry[newMax + 1][newMax + 1];
			final DistanceMatrixEntry[][] matrix = matrices[r.ordinal()];

			// copy old matrix contents
			for (int i = 0; i < matrix.length; i++) {
				System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
			}

			matrices[r.ordinal()] = newMatrix;
		}
		{
			final DistanceMatrixEntry[][] newMatrix = new DistanceMatrixEntry[newMax + 1][newMax + 1];
			final DistanceMatrixEntry[][] matrix = minima;

			// copy old matrix contents
			for (int i = 0; i < matrix.length; i++) {
				System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
			}

			minima = newMatrix;
		}
		{
			final DistanceMatrixEntry[][] newMatrix = new DistanceMatrixEntry[newMax + 1][newMax + 1];
			final DistanceMatrixEntry[][] matrix = maxima;

			// copy old matrix contents
			for (int i = 0; i < matrix.length; i++) {
				System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
			}

			maxima = newMatrix;
		}
	}

	@Override
	public @NonNull List<@NonNull DistanceMatrixEntry> getValues(@NonNull final IPort from, @NonNull final IPort to) {
		final @NonNull List<@NonNull DistanceMatrixEntry> values = new ArrayList<>(ERouteOption.values().length);

		for (final ERouteOption r : ERouteOption.values()) {
			final DistanceMatrixEntry e = matrices[r.ordinal()][from.getIndex()][to.getIndex()];
			if (e != null && e.getDistance() != Integer.MAX_VALUE) {
				values.add(e);
			}
		}

		return values;
	}

	@Override
	public @Nullable DistanceMatrixEntry getMinimum(@NonNull final IPort from, @NonNull final IPort to) {
		return minima[from.getIndex()][to.getIndex()];
	}

	@Override
	public @Nullable DistanceMatrixEntry getMaximum(@NonNull final IPort from, @NonNull final IPort to) {
		return maxima[from.getIndex()][to.getIndex()];
	}

	@Override
	public int getMinimumValue(@NonNull final IPort from, @NonNull final IPort to) {
		final DistanceMatrixEntry e = minima[from.getIndex()][to.getIndex()];
		if (e != null) {
			return e.getDistance();
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public int getMaximumValue(@NonNull final IPort from, @NonNull final IPort to) {
		final DistanceMatrixEntry e = maxima[from.getIndex()][to.getIndex()];
		if (e != null) {
			return e.getDistance();
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public void setPreSortedRoutes(@Nullable final ERouteOption[] preSortedKeys) {
		this.preSortedKeys = preSortedKeys;
	}

	@Override
	public ERouteOption[] getRoutes() {
		if (preSortedKeys != null) {
			return preSortedKeys;
		}
		return ERouteOption.values();
	}

}
