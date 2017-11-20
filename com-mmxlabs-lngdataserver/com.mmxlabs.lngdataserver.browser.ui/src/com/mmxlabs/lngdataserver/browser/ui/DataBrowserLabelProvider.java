package com.mmxlabs.lngdataserver.browser.ui;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import com.mmxlabs.lngdataserver.browser.Node;

public class DataBrowserLabelProvider extends AdapterFactoryLabelProvider{

	public DataBrowserLabelProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
		// TODO Auto-generated constructor stub
	}
	
	public DataBrowserLabelProvider() {
		super(null);
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof Node) {
			return ((Node)element).getDisplayName();
		}
		return "Error displaying label";
	}
}
