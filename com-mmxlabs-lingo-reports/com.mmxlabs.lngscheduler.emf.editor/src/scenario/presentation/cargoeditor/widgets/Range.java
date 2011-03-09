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
import org.eclipse.swt.widgets.Spinner;

/**
 * A widget which displays two {@link Spinner}s whose values are connected in a
 * certain way
 * 
 * @author hinton
 * 
 */
public class Range extends Composite {
	/**
	 * The maximum value any part of the range can contain
	 */
	private int maximumValue;
	/**
	 * The minimum value any part of the range can contain
	 */
	private int minimumValue;
	/**
	 * The smallest distance between upper and lower values
	 */
	private int minimumSeparation;
	/**
	 * The largest distance between upper and lower values
	 */
	private int maximumSeparation;

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
	
	public int getLowerValue() {
		return lowerValue;
	}
	
	public int getUpperValue() {
		return upperValue;
	}
	
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
	}
	
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
	}
	
	private void setSpinnerLimits() {
		minSpinner.setMinimum(minimumValue);
		maxSpinner.setMinimum(minimumValue + minimumSeparation);
		minSpinner.setMaximum(maximumValue - minimumSeparation);
		maxSpinner.setMaximum(maximumValue);
	}

	public int getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(int maximumValue) {
		this.maximumValue = maximumValue;
		setSpinnerLimits();
	}

	public int getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(int minimumValue) {
		this.minimumValue = minimumValue;
		setSpinnerLimits();
	}

	public int getMinimumSeparation() {
		return minimumSeparation;
	}

	public void setMinimumSeparation(int minimumSeparation) {
		this.minimumSeparation = minimumSeparation;
		setSpinnerLimits();
	}

	public int getMaximumSeparation() {
		return maximumSeparation;
	}

	public void setMaximumSeparation(int maximumSeparation) {
		this.maximumSeparation = maximumSeparation;
	}
}
