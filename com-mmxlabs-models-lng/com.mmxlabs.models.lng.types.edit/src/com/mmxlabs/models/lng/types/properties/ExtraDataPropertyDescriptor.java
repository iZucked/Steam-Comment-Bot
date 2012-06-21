package com.mmxlabs.models.lng.types.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.mmxlabs.models.lng.types.ExtraData;

public class ExtraDataPropertyDescriptor extends ExtraDataContainerPropertySource implements IPropertyDescriptor {
	private ExtraData object;
	public ExtraDataPropertyDescriptor(ExtraData object) {
		super(object);
		this.object = object;
	}
	@Override
	public String getCategory() {
		return null;
	}
	@Override
	public String getDescription() {
		return "";
	}
	@Override
	public String getDisplayName() {
		return object.getName();
	}
	@Override
	public String[] getFilterFlags() {
		return null;
	}
	@Override
	public Object getHelpContextIds() {
		return null;
	}
	@Override
	public Object getId() {
		return object.getKey();
	}
	@Override
	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		return false;
	}
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return null;
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
