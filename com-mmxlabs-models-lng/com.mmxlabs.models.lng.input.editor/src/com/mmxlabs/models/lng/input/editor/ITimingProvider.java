package com.mmxlabs.models.lng.input.editor;

import java.util.Date;

public interface ITimingProvider {
	public Date getStartDate(Object task);
	public Date getEndDate(Object task);
}
