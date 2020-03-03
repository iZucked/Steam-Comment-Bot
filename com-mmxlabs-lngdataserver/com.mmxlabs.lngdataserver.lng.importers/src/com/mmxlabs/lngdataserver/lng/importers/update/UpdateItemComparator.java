/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.update;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class UpdateItemComparator extends ViewerComparator {

	@Override
	public int compare(final Viewer viewer, final Object e1, final Object e2) {

	
		
		if (e1 instanceof UserUpdateStep && !(e2 instanceof UserUpdateStep)) {
			return 1;
		}
		if (e2 instanceof UserUpdateStep && !(e1 instanceof UserUpdateStep)) {
			return -1;
		}
		if (e1 instanceof UpdateStep && !(e2 instanceof UpdateStep)) {
			return 1;
		}
		if (e2 instanceof UpdateStep && !(e1 instanceof UpdateStep)) {
			return -1;
		}
		return super.compare(viewer, e1, e2);
	}

}
