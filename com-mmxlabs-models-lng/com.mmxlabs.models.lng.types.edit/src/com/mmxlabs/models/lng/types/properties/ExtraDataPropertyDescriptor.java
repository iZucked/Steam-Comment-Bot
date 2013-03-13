/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types.properties;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.mmxlabs.models.lng.types.ExtraData;

/**
 * @since 2.0
 */
public class ExtraDataPropertyDescriptor extends PropertyDescriptor {
	private ExtraData object;
	public ExtraDataPropertyDescriptor(ExtraData object) {
		super(object.getKey(), object.getName());
		this.object = object;
	}
	@Override
	public ILabelProvider getLabelProvider() {
		return new LabelProvider() {
			@Override
			public String getText(Object element) {
				return object.formatValue();
			}
		};
	}	
}
