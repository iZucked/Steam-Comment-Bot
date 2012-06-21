package com.mmxlabs.models.lng.types.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;

public class ExtraDataContainerPropertySource implements IPropertySource {
	private ExtraDataContainer container;

	public ExtraDataContainerPropertySource(final ExtraDataContainer container) {
		this.container = container;
	}
	
	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		final List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		
		for (final ExtraData ed : container.getExtraData()) {
			descriptors.add(new ExtraDataPropertyDescriptor(ed));
		}
		
		return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}

	@Override
	public Object getPropertyValue(final Object id) {
		return container.getDataWithKey(id.toString());
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
