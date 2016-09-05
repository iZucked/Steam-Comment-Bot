package com.mmxlabs.models.lng.adp.presentation.wizard;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.adp.ADPModel;

public class BindingRuleContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {

		if (inputElement instanceof ADPModel) {
			ADPModel adpModel = (ADPModel) inputElement;
			return adpModel.getBindingRules().toArray();
		}

		return new Object[0];
	}

}
