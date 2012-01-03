/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.demo.reports.views.tabular;

import java.lang.reflect.Method;

import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * Reflectively provide labels for model objects based on their
 * {@link TableField} annotations.
 * 
 * TODO add value transformer
 * 
 * @author hinton
 * 
 */
public class ReflectiveLabelProvider extends ColumnLabelProvider {
	final Method accessor;
	public ReflectiveLabelProvider(final Method accessor) {
		super();
		this.accessor = accessor;
	}
	@Override
	public String getText(Object element) {
		try {
			final Object o = accessor.invoke(element, null);
			return o.toString();
		} catch (final Exception ex) {
			return "ERROR";
		}
	}
}
