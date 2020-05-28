package com.mmxlabs.lingo.reports.customizable;

import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;

public class CustomReportNameValidator implements IInputValidator {
	
	final List<String> existingNames;
	
	public CustomReportNameValidator(List<String> existingNames) {
		this.existingNames = existingNames;
	}
	
	@Override
	public String isValid(String name) {
		if (this.existingNames.contains(name.toLowerCase())) {
			return "Cannot use this name as a report already exists with it";
		}
		if (name == null || name.isEmpty()) {
			return "A name must be set";
		}
		if ("Schedule Summary".equals(name.trim())) {
			return "Cannot use this name";
		}
		return null;
	}
}
