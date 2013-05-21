/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.editor;

import java.util.Date;

public interface IAssignmentInformationProvider<R,T> {
	public Date getStartDate(T task);
	public Date getEndDate(T task);
	public String getLabel(T task);
	public String getResourceLabel(R resource);
	public String getTooltip(T task);
	public boolean isLocked(T task);
	
	public Date getResourceStartDate(R resource);
	public Date getResourceEndDate(R resource);
	
	public boolean isSensibleSequence(T task1, T task2);
}
