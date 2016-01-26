/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.common.impl;

import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixEditor;

public final class IndexedMatrixEditor<T extends IIndexedObject, U> implements IMatrixEditor<T, U> {
	private final U defaultValue;
	private U[][] matrix;

	private int maxIndex = -1;

	@SuppressWarnings("unchecked")
	public IndexedMatrixEditor(final U defaultValue) {
		super();
		this.defaultValue = defaultValue;
		matrix = (U[][]) new Object[0][0];
	}

	@Override
	public final U get(final T x, final T y) {
		// added bounds check - we may not have seen all indexed objects possible.
		final int xi = x.getIndex();
		if (xi >= matrix.length) {
			return defaultValue;
		}
		final int yi = y.getIndex();
		final U[] subMatrix = matrix[xi];
		if (yi >= subMatrix.length) {
			return defaultValue;
		}
		return subMatrix[yi];
	}

	@Override
	public final void set(final T x, final T y, final U v) {
		// the resizing mechanism is quite unpleasant, but slow set / fast get
		// seems like a reasonable tradeoff.
		final int xi, yi;
		xi = x.getIndex();
		yi = y.getIndex();
		if ((xi > maxIndex) || (yi > maxIndex)) {
			resize(Math.max(xi, yi));
		}

		matrix[xi][yi] = v;
	}

	private void resize(final int newMax) {
		maxIndex = newMax;

		@SuppressWarnings("unchecked")
		final U[][] newMatrix = (U[][]) new Object[newMax + 1][newMax + 1];

		// copy old matrix contents
		for (int i = 0; i < matrix.length; i++) {
			System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
		}

		for (int i = matrix.length; i < newMatrix.length; i++) {
			for (int j = 0; j < newMatrix.length; j++) {
				newMatrix[i][j] = newMatrix[j][i] = defaultValue;
			}
		}

		matrix = newMatrix;
	}

	@Override
	public final boolean has(final T x, final T y) {
		return get(x, y) != defaultValue;
	}
}
