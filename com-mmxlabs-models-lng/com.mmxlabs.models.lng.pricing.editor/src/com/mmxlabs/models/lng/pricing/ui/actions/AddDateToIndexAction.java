/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.actions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;

import com.mmxlabs.models.lng.pricing.ui.editorpart.IndexPane;

/**
 * Adds a new date into an IndexPane editor.
 * 
 * @author Simon McGregor
 * 
 */
public class AddDateToIndexAction extends Action {

	private final IndexPane pane;

	public AddDateToIndexAction(final IndexPane pane) {
		super("Add Date");
		this.pane = pane;
		setText("Add Date");
		setDescription("Adds a new date column");
		setToolTipText("Adds a new date column");

		//setImageDescriptor(Activator.getImageDescriptor("/icons/add.gif"));
		
	}

	@Override
	public void run() {
		final DateFormat format = new SimpleDateFormat("yyyy-MM");
		
		IInputValidator validator = new IInputValidator() {
			@Override
			public String isValid(String newText) {
				try {
					Date date = format.parse(newText);
					if (date != null) {
						return null;
					}
				} catch (ParseException e) {
				}
				
				return "Not a valid date";
			}
			
		};
		
		InputDialog dialog = new InputDialog(null, "Enter date", "Enter a date for the new column.", "yyyy-MM", validator);
		
		if (dialog.open() == Window.OK) {
			try {
				Date date = format.parse(dialog.getValue());
				pane.selectDateColumn(date);			
			} catch (ParseException e) {
			}
		}

	}

}
