/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author hinton
 *
 */
public class ReplicateCargoDialog extends Dialog {
	private int numberOfRepetitions = 0;
	private int interval = 1;
	private int intervalType = 0;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ReplicateCargoDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 3;

		Label lblNumberOfCopies = new Label(container, SWT.NONE);
		lblNumberOfCopies.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNumberOfCopies.setText("Number of copies:");

		final Spinner spinner = new Spinner(container, SWT.BORDER);
		spinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				numberOfRepetitions = spinner.getSelection();
			}
		});
		spinner.setMinimum(0);
		spinner.setToolTipText("Set to zero (0) to offset selected cargoes rather than replicating them");
		
		new Label(container, SWT.NONE);

		Label lblRepeatEvery = new Label(container, SWT.NONE);
		lblRepeatEvery.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRepeatEvery.setText("Repeat every:");

		final Spinner spinner_1 = new Spinner(container, SWT.BORDER);
		spinner_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				interval = spinner_1.getSelection();
			}
		});
		spinner_1.setMinimum(1);

		final Combo combo = new Combo(container, SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				intervalType = combo.getSelectionIndex();
			}
		});
		combo.setItems(new String[] { "Days", "Weeks", "Months" });
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.select(0);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(314, 161);
	}

	/**
	 * Given a list of calendar objects as inputs, generates a list of lists of calendar objects of outputs, each member of which has members corresponding to members in the same place in the input,
	 * so
	 * 
	 * repeatDates([today, tomorrow])
	 * 
	 * might give you
	 * 
	 * [[today + 1 day, tomorrow + 1 day], [today + 2 days, tomorrow + 2 days]] etc.
	 * 
	 * @param input
	 * @return
	 */
	public List<List<Calendar>> repeatDates(final List<Calendar> input) {
		final ArrayList<List<Calendar>> result = new ArrayList<List<Calendar>>();

		final int calendarInterval;
		if (intervalType == 0) {
			calendarInterval = Calendar.DATE;
		} else if (intervalType == 1) {
			calendarInterval = Calendar.WEEK_OF_MONTH;
		} else if (intervalType == 2) {
			calendarInterval = Calendar.MONTH;
		} else {
			calendarInterval = Calendar.DATE;
		}
		final int count = isReplicating() ? numberOfRepetitions : 1;
		for (int i = 0; i < count; i++) {
			final List<Calendar> output = new ArrayList<Calendar>(input.size());
			for (final Calendar in : input) {
				final Calendar out = (Calendar) in.clone();
				
				out.add(calendarInterval, (i + 1) * interval);
				
				output.add(out);
			}
			result.add(output);
		}

		return result;
	}

	public boolean isReplicating() {
		return numberOfRepetitions > 0;
	}
}
