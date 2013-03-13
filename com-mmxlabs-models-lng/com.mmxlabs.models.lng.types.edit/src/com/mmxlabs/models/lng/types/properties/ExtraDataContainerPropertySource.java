/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;

/**
 * @since 2.0
 */
public class ExtraDataContainerPropertySource implements IPropertySource {
	private ExtraDataContainer container;

	public ExtraDataContainerPropertySource(final ExtraDataContainer container) {
		this.container = container;
	}
	
	@Override
	public Object getEditableValue() {
		return container;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		final List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		
		for (final ExtraData ed : container.getExtraData()) {
			descriptors.add(createDescriptor(ed));
		}
		
		createExtraDescriptors(descriptors);
		
		return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}

	protected void createExtraDescriptors(final List<IPropertyDescriptor> descriptors) {
		
	}
	
	protected IPropertyDescriptor createDescriptor(final ExtraData ed) {
		return new ExtraDataPropertyDescriptor(ed);
	}
	
	@Override
	public Object getPropertyValue(final Object id) {
		final ExtraData dataWithKey = container.getDataWithKey(id.toString());
		
		if (shouldExpand(dataWithKey)) {
			return new ExtraDataContainerPropertySource(dataWithKey);
		} else {
			return dataWithKey;
		}
	}

	protected boolean shouldExpand(ExtraData dataWithKey) {
		return dataWithKey.getExtraData().isEmpty() == false;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return true;
	}

	@Override
	public void resetPropertyValue(Object id) {
		
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		
	}
	
}
