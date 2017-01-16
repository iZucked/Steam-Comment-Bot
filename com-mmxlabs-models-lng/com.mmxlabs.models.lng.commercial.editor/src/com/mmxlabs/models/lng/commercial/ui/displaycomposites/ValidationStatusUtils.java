/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import org.eclipse.core.runtime.IStatus;

public class ValidationStatusUtils {
	/**
	 * Returns a list of validation statuses which need to be iterated over
	 * to account for a particular status (which may or may not be a compound
	 * MultiStatus). If the status is a MultiStatus, returns a list of its
	 * children; otherwise, returns a list with one element corresponding to the
	 * status parameter itself.
	 * 
	 * @param status
	 * @return
	 */
	static public IStatus[] getStatuses(final IStatus status) {
		if (status.isMultiStatus()) {
			return status.getChildren();
		}
		IStatus [] result = { status };
		return result;
	}
	
}
