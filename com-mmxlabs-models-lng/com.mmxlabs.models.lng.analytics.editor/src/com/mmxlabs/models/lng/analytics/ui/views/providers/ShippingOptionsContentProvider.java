/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.providers;

import java.util.Collection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class ShippingOptionsContentProvider implements ITreeContentProvider {

	private IScenarioEditingLocation loc;

	public ShippingOptionsContentProvider(IScenarioEditingLocation loc) {
		this.loc = loc;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

	}

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof OptionAnalysisModel) {
			final OptionAnalysisModel model = (OptionAnalysisModel) inputElement;
			return model.getShippingTemplates().toArray();
		}
//		return new Object[0];
//		
//		MMXRootObject rootObject = loc.getRootObject();
//		if (rootObject instanceof LNGScenarioModel) {
//			LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
//			LNGReferenceModel referenceModel = lngScenarioModel.getReferenceModel();
//			if (referenceModel != null) {
//				FleetModel fleetModel = referenceModel.getFleetModel();
//				return new Object[] { fleetModel.getVessels(), fleetModel.getVesselClasses() };
//			}
//		}
//
		return new Object[0];
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof Object[]) {
			return (Object[]) parentElement;
		}
		if (parentElement instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) parentElement;
			return collection.toArray();
		}

		return new Object[0];
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		if (element instanceof Object[]) {
			return true;
		}
		if (element instanceof Collection<?>) {
			return true;
		}
		if (element instanceof OptionAnalysisModel) {
			return true;
		}
		return false;
	}

}