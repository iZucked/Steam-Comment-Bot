package com.mmxlabs.rcp.navigator.ecore;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;

public class EcoreLabelProvider extends AdapterFactoryLabelProvider {

	public EcoreLabelProvider() {
		super(EcoreComposedAdapterFactory.getAdapterFactory());
	}

	@Override
	public Image getImage(Object element) {

		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		return super.getText(element);
	}
}
