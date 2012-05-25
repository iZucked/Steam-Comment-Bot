package com.mmxlabs.models.lng.input.editor;

import java.util.Date;

public interface IAssignmentInformationProvider<R,T> {
	public Date getStartDate(T task);
	public Date getEndDate(T task);
	public String getLabel(T task);
	public String getResourceLabel(R resource);
	public String getTooltip(T task);
	public boolean isLocked(T task);
}
