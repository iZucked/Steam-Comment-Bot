/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.actions;

import java.text.ParseException;
import java.time.YearMonth;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;

import com.mmxlabs.models.datetime.importers.YearMonthAttributeImporter;
import com.mmxlabs.models.lng.pricing.ui.editorpart.IndexPane;

/**
 * Adds a new date into an IndexPane editor.
 * 
 * @author Simon McGregor
 * 
 */
public class AddDateToIndexAction extends Action {

	private final IndexPane pane;
	private static int earliestYear = 1990;
	private static int latestYear = 2050;

	public AddDateToIndexAction(final IndexPane pane) {
		super("Extend dates");
		this.pane = pane;
		setText("Extend dates");
		setDescription("Adds new date columns");
		setToolTipText("Adds new date columns");
	}

	@Override
	public void run() {
		final YearMonthAttributeImporter format = new YearMonthAttributeImporter();

		final IInputValidator validator = new IInputValidator() {
			@Override
			public String isValid(final String newText) {
				try {
					final YearMonth date = format.parseYearMonth(newText);
					if (date != null) {

						final int year = date.getYear();

						if (year < earliestYear || year > latestYear) {
							return String.format("Year should be in range %d to %d", earliestYear, latestYear);
						}
						return null;
					}
				} catch (final ParseException e) {
				}

				return "Please use the form MM-yyyy";
			}

		};

		final InputDialog dialog = new InputDialog(null, "Extend date range", "Enter a new start or end date.", "yyyy-MM", validator);

		if (dialog.open() == Window.OK) {
			try {
				final YearMonth date = format.parseYearMonth(dialog.getValue());
				pane.selectDateColumn(date);
			} catch (final ParseException e) {
			}
		}

	}
}
