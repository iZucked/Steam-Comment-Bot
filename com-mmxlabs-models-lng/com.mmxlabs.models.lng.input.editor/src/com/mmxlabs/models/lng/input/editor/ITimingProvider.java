package com.mmxlabs.models.lng.input.editor;

import java.util.Date;

public interface ITimingProvider<T> {
	public Date getStartDate(T task);
	public Date getEndDate(T task);
}
