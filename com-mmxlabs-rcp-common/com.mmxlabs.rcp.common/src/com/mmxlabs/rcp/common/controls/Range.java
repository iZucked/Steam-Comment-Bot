/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * A widget which allows the user to select a range by choosing a lower and upper bound.
 * 
 * The widget will ensure that:
 * 
 * <ol>
 * <li>minimumValue <= lower <= upper <= maximumValue</li>
 * <li>minimumSeparation <= (upper - lower) <= maximumSeparation</li>
 * </ol>
 * 
 * These values can be set with {@link #setMaximumValue(int)}, {@link #setMinimumValue(int)}, {@link #setMaximumSeparation(int)} and {@link #setMinimumSeparation(int)}.
 * 
 * By default the range can contain any pair of integers l, u such that l <= u.
 * 
 * TODO check this has correct focus behaviour.
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

	public Range(final Composite parent, final int style) {
		super(parent, style);
		final FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.spacing = 1;

		layout.spacing = 1;
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		setLayout(layout);
		minSpinner = new Spinner(this, style);
		maxSpinner = new Spinner(this, style);

		maxSpinner.addSelectionListener(getSelectionListener());
		minSpinner.addSelectionListener(getSelectionListener());

		final Listener focusListener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (event.type == SWT.FocusIn) {
					handleFocus(SWT.FocusIn);
				}
			}

		};

		maxSpinner.addListener(SWT.FocusIn, focusListener);
		minSpinner.addListener(SWT.FocusIn, focusListener);

		setSpinnerLimits();
	}

	boolean hasFocus = false;

	private final Listener filter = new Listener() {
		@Override
		public void handleEvent(final Event event) {
			if (((Control) event.widget).getShell() == getShell()) {
				handleFocus(SWT.FocusOut);
			}
		}
	};

	protected void handleFocus(final int type) {
		if (isDisposed()) {
			return;
		}
		switch (type) {
		case SWT.FocusIn: {
			if (hasFocus) {
				return;
			}
			hasFocus = true;
			final Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			display.addFilter(SWT.FocusIn, filter);

			final Event e = new Event();
			e.widget = this;
			notifyListeners(SWT.FocusIn, e);
		}
			break;
		case SWT.FocusOut: {
			if (!hasFocus) {
				return;
			}
			final Display display = getDisplay();
			final Control focus = display.getFocusControl();
			if ((focus == maxSpinner) || (focus == minSpinner)) {
				return;
			}
			hasFocus = false;
			display.removeFilter(SWT.FocusIn, filter);
			final Event e = new Event();
			e.widget = this;
			notifyListeners(SWT.FocusOut, e);
		}
			break;
		}
	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		return new Point(wHint, hHint);
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
	 * Set the lower bound value entered into this range. May also adjust the upper bound value to ensure that the constraints are met (see {@link Range} for details).
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
		if (minSpinner.getSelection() != lowerValue) {
			minSpinner.setSelection(lowerValue);
		}

		// clamp upper value
		if ((upperValue - lowerValue) < minimumSeparation) {
			upperValue = lowerValue + minimumSeparation;
			maxSpinner.setSelection(upperValue);
		} else if ((upperValue - lowerValue) > maximumSeparation) {
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
	 * Set the upper bound value entered into this range. May also adjust the lower bound value to ensure that the constraints are met (see {@link Range} for details).
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

		if (maxSpinner.getSelection() != upperValue) {
			maxSpinner.setSelection(upperValue);
		}

		// clamp lower value
		if ((upperValue - lowerValue) < minimumSeparation) {
			lowerValue = upperValue - minimumSeparation;
			minSpinner.setSelection(lowerValue);
		} else if ((upperValue - lowerValue) > maximumSeparation) {
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
	 * Sets the ranges of the two spinners to ensure only legal lvalues can happen
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
	public void setMaximumValue(final int maximumValue) {
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
	public void setMinimumValue(final int minimumValue) {
		this.minimumValue = minimumValue;
		setSpinnerLimits();
	}

	/**
	 * Get the minimum legal difference between the upper bound and the lower bound
	 * 
	 * @return
	 */
	public int getMinimumSeparation() {
		return minimumSeparation;
	}

	/**
	 * Set the minimum legal difference between the upper bound and the lower bound
	 * 
	 * @return
	 */
	public void setMinimumSeparation(final int minimumSeparation) {
		this.minimumSeparation = minimumSeparation;
		setSpinnerLimits();
	}

	/**
	 * Get the maximum legal difference between the upper bound and the lower bound
	 * 
	 * @return
	 */
	public int getMaximumSeparation() {
		return maximumSeparation;
	}

	/**
	 * Set the maximum legal difference between the upper bound and the lower bound
	 * 
	 * @return
	 */
	public void setMaximumSeparation(final int maximumSeparation) {
		this.maximumSeparation = maximumSeparation;
	}

	@Override
	public boolean setFocus() {
		return minSpinner.setFocus() || maxSpinner.setFocus();
	}

	@Override
	public boolean isFocusControl() {
		if (minSpinner.isFocusControl() || maxSpinner.isFocusControl()) {
			return true;
		}
		return super.isFocusControl();
	}

	public static class RangeDisplay {

		public static void main(final String[] args) {
			final Display display = new Display();
			final Shell shell = new Shell(display);

			shell.setLayout(new RowLayout(SWT.VERTICAL));

			final Composite comp = new Composite(shell, SWT.NONE);
			comp.setLayout(new RowLayout(SWT.HORIZONTAL));
			final Text text = new Text(comp, SWT.NONE);
			text.setText("text 1");
			final Text text2 = new Text(comp, SWT.NONE);
			text2.setText("text 1");
			final Text text3 = new Text(shell, SWT.NONE);
			text3.setText("text 3");
			// // These printlns are OK - this main is just a test.
			// final FocusListener fl = new FocusListener() {
			// @Override
			// public void focusGained(final FocusEvent e) {
			// System.err.println("got focus : " + e);
			// }
			//
			// @Override
			// public void focusLost(final FocusEvent e) {
			// System.err.println("lost focus : " + e);
			// }
			// };
			// comp.addFocusListener(fl);
			// shell.addFocusListener(fl);
			// text.addFocusListener(fl);
			// text2.addFocusListener(fl);
			// text3.addFocusListener(fl);
			shell.pack();
			shell.open();

			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();
		}
	}
}
