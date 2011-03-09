/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;

/**
 * A widget which allows the user to select a range by choosing a lower and
 * upper bound.
 * 
 * The widget will ensure that:
 * 
 * <ol>
 * <li>minimumValue <= lower <= upper <= maximumValue</li>
 * <li>minimumSeparation <= (upper - lower) <= maximumSeparation</li>
 * </ol>
 * 
 * These values can be set with {@link #setMaximumValue(int)},
 * {@link #setMinimumValue(int)}, {@link #setMaximumSeparation(int)} and
 * {@link #setMinimumSeparation(int)}.
 * 
 * By default the range can contain any pair of integers l, u such that l <= u.
 * 
 * @author hinton
 * 
 */
public class Range extends Composite {
	/**
	 * The maximum value any part of the range can contain
	 */
	private int maximumValue = Integer.MAX_VALUE;
	/**
	 * The minimum value any part of the range can contain
	 */
	private int minimumValue = Integer.MIN_VALUE;
	/**
	 * The smallest distance between upper and lower values
	 */
	private int minimumSeparation = 0;
	/**
	 * The largest distance between upper and lower values
	 */
	private int maximumSeparation = Integer.MAX_VALUE;

	private int lowerValue, upperValue;

	private final Spinner minSpinner, maxSpinner;

	private SelectionListener selectionListener = null;

	private SelectionListener getSelectionListener() {
		if (selectionListener == null) {
			selectionListener = new SelectionListener() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (e.widget == minSpinner) {
						setLowerValue(minSpinner.getSelection());
					} else if (e.widget == maxSpinner) {
						setUpperValue(maxSpinner.getSelection());
					}
				}

				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {
					if (e.widget == minSpinner) {
						setLowerValue(minSpinner.getSelection());
					} else if (e.widget == maxSpinner) {
						setUpperValue(maxSpinner.getSelection());
					}
				}
			};
		}
		return selectionListener;
	}

	public Range(Composite parent, int style) {
		super(parent, style);
		final RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginBottom = layout.marginTop = layout.marginLeft = layout.marginRight = 0;
		setLayout(layout);
		minSpinner = new Spinner(parent, style);
		maxSpinner = new Spinner(parent, style);

		maxSpinner.addSelectionListener(getSelectionListener());
		minSpinner.addSelectionListener(getSelectionListener());
	}

	/**
	 * Get the lower bound value entered into this range
	 * 
	 * @return
	 */
	public int getLowerValue() {
		return lowerValue;
	}

	/**
	 * Get the upper bound value entered into this range
	 * 
	 * @return
	 */
	public int getUpperValue() {
		return upperValue;
	}

	/**
	 * Set the lower bound value entered into this range. May also adjust the
	 * upper bound value to ensure that the constraints are met (see
	 * {@link Range} for details).
	 * 
	 * Triggers notifies any {@link SelectionListener}s that are attached
	 * 
	 * @param lowerValue
	 *            new lower value
	 */
	public void setLowerValue(final int lowerValue) {
		minSpinner.removeSelectionListener(getSelectionListener());
		maxSpinner.removeSelectionListener(getSelectionListener());
		this.lowerValue = lowerValue;
		minSpinner.setSelection(lowerValue);

		// clamp upper value
		if (upperValue - lowerValue < minimumSeparation) {
			upperValue = lowerValue + minimumSeparation;
			maxSpinner.setSelection(upperValue);
		} else if (upperValue - lowerValue > maximumSeparation) {
			upperValue = lowerValue + maximumSeparation;
			maxSpinner.setSelection(upperValue);
		}

		minSpinner.addSelectionListener(getSelectionListener());
		maxSpinner.addSelectionListener(getSelectionListener());

		final Event event = new Event();
		event.widget = this;
		notifyListeners(SWT.Selection, event);
	}

	/**
	 * Set the upper bound value entered into this range. May also adjust the
	 * lower bound value to ensure that the constraints are met (see
	 * {@link Range} for details).
	 * 
	 * Triggers notifies any {@link SelectionListener}s that are attached
	 * 
	 * @param upperValue
	 *            new upper value
	 */
	public void setUpperValue(final int upperValue) {
		minSpinner.removeSelectionListener(getSelectionListener());
		maxSpinner.removeSelectionListener(getSelectionListener());
		this.upperValue = upperValue;
		maxSpinner.setSelection(upperValue);

		// clamp lower value
		if (upperValue - lowerValue < minimumSeparation) {
			lowerValue = upperValue - minimumSeparation;
			minSpinner.setSelection(lowerValue);
		} else if (upperValue - lowerValue > maximumSeparation) {
			lowerValue = upperValue - maximumSeparation;
			minSpinner.setSelection(lowerValue);
		}

		minSpinner.addSelectionListener(getSelectionListener());
		maxSpinner.addSelectionListener(getSelectionListener());

		final Event event = new Event();
		event.widget = this;
		notifyListeners(SWT.Selection, event);
	}

	/**
	 * Sets the ranges of the two spinners to ensure only legal lvalues can
	 * happen
	 */
	private void setSpinnerLimits() {
		minSpinner.setMinimum(minimumValue);
		maxSpinner.setMinimum(minimumValue + minimumSeparation);
		minSpinner.setMaximum(maximumValue - minimumSeparation);
		maxSpinner.setMaximum(maximumValue);
	}

	/**
	 * Get the maximum value of the upper bound
	 * 
	 * @return
	 */
	public int getMaximumValue() {
		return maximumValue;
	}

	/**
	 * Set the maximum value of the upper bound
	 * 
	 * @return
	 */
	public void setMaximumValue(int maximumValue) {
		this.maximumValue = maximumValue;
		setSpinnerLimits();
	}

	/**
	 * Get the minimum value of the lower bound
	 * 
	 * @return
	 */
	public int getMinimumValue() {
		return minimumValue;
	}

	/**
	 * Set the minimum value of the lower bound
	 * 
	 * @return
	 */
	public void setMinimumValue(int minimumValue) {
		this.minimumValue = minimumValue;
		setSpinnerLimits();
	}

	/**
	 * Get the minimum legal difference between the upper bound and the lower
	 * bound
	 * 
	 * @return
	 */
	public int getMinimumSeparation() {
		return minimumSeparation;
	}

	/**
	 * Set the minimum legal difference between the upper bound and the lower
	 * bound
	 * 
	 * @return
	 */
	public void setMinimumSeparation(int minimumSeparation) {
		this.minimumSeparation = minimumSeparation;
		setSpinnerLimits();
	}

	/**
	 * Get the maximum legal difference between the upper bound and the lower
	 * bound
	 * 
	 * @return
	 */
	public int getMaximumSeparation() {
		return maximumSeparation;
	}

	/**
	 * Set the maximum legal difference between the upper bound and the lower
	 * bound
	 * 
	 * @return
	 */
	public void setMaximumSeparation(int maximumSeparation) {
		this.maximumSeparation = maximumSeparation;
	}
}
