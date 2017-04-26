/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.actions;

import java.text.ParseException;
import java.time.YearMonth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;
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

		@NonNull
		Pattern pattern = Pattern.compile("(?<month>[01]?[0-9])-(?<year>[12][0-9]{3})");
		final IInputValidator validator = new IInputValidator() {
			@Override
			public String isValid(final String newText) {
				final Matcher m = pattern.matcher(newText);
				if (m.matches()) {
					final int year = Integer.parseInt(m.group("year"));
					final int month = Integer.parseInt(m.group("month"));
					//
					if (year < earliestYear || year > latestYear) {
						return String.format("Year should be in range %d to %d", earliestYear, latestYear);
					}
					if (month < 1 || year > 12) {
						return "Please use the form MM-yyyy";
					}
					return null;
				}

				return "Please use the form MM-yyyy";
			}

		};

		final InputDialog dialog = new InputDialog(null, "Extend date range", "Enter a new start or end date.", "MM-yyyy", validator);

		if (dialog.open() == Window.OK) {
			try {
				final YearMonth date = format.parseYearMonth(dialog.getValue());
				pane.selectDateColumn(date);
			} catch (final ParseException e) {
			}
		}

	}
}
