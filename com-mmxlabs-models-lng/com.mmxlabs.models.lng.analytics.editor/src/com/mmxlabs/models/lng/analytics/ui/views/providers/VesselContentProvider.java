package com.mmxlabs.models.lng.analytics.ui.views.providers;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class VesselContentProvider implements ITreeContentProvider {

	private IScenarioEditingLocation loc;

	public VesselContentProvider(IScenarioEditingLocation loc) {
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

		MMXRootObject rootObject = loc.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			LNGReferenceModel referenceModel = lngScenarioModel.getReferenceModel();
			if (referenceModel != null) {
				FleetModel fleetModel = referenceModel.getFleetModel();

				return fleetModel.getVessels().toArray();
			}
		}

		return new Object[0];
	}

	@Override
	public Object[] getChildren(final Object parentElement) {

		return new Object[0];
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {

		return false;
	}

}