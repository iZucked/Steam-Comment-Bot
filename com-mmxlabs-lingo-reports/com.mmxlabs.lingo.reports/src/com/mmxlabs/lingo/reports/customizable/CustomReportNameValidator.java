/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

public class CustomReportNameValidator implements IInputValidator {
	
	final List<String> existingNames;
	
	public CustomReportNameValidator(List<String> existingNames) {
		this.existingNames = existingNames;
	}
	
	@Override
	public String isValid(String name) {
		if (!name.replaceAll("[^a-zA-Z0-9() \\._]+", "_").equals(name)) {
			return "Report names can only contain characters and digits.";
		}
		
		if (this.existingNames.contains(name.toLowerCase())) {
			return "Cannot use this name as a report already exists with it";
		}
		
		if (name == null || name.isEmpty()) {
			return "A name must be set";
		}
		
		if ("Schedule Summary".equalsIgnoreCase(name.trim())) {
			return "Cannot use this name";
		}
		
		String registeredViewName = isRegisteredViewName(name);
		if (registeredViewName != null) {	
			return "Cannot use this name, as there is already a System view named "+registeredViewName;
		}
		
		return null;
	}
	
	private String isRegisteredViewName(String name) {
		List<String> registeredViewNames = getExistingRegisteredViewNames();
		for (String viewName : registeredViewNames) {
			if (viewName.equalsIgnoreCase(name)) {
				return viewName;
			}
		}
		return null;
	}
	
	private List<String> getExistingRegisteredViewNames() {
		final IViewDescriptor[] views = PlatformUI.getWorkbench().getViewRegistry().getViews();
		List<String> registeredViewNames = new ArrayList<>();
		
		for (IViewDescriptor view : views) {
			if (view.getCategoryPath() != null && 
				view.getCategoryPath().length == 1 &&
				!"[User Reports]".equalsIgnoreCase(view.getCategoryPath()[0]) &&
				!"[Team Reports]".equalsIgnoreCase(view.getCategoryPath()[0])) {
				registeredViewNames.add(view.getLabel());
			}
		}
		
		return registeredViewNames;
	}
}
