/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.formatters;

public class CharterOutMarketParametersLocalDateTextFormatter extends LocalDateTextFormatter {
	@Override
	public Object getValue() {
		// Custom code: Allow empty strings regardless of format string
		if (isFieldsEmpty()) {
			return null;
		}
		return super.getValue();
	}

	@Override
	public boolean isValid() {
		if (isFieldsEmpty()) {
			return true;
		}
		return super.isValid();
	}
	
	@Override
	public String getDisplayString() {
		if (isFieldsEmpty()) {
			return getEditString();
		}
		return super.getDisplayString();
	}

	private boolean isFieldsEmpty() {
		boolean result = false;
		
		for (FieldDesc fd : fields) {
			if (fd!= null) {
				result = fd.empty;
				if (!result) {
					break;
				}
			}
		}
		return result;
	}
}