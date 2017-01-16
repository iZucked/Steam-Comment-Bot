/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.providers;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class VesselAndClassContentProvider implements ITreeContentProvider {

	public static class VesselContainer {

		public Object[] vessels;

		public VesselContainer(List<Vessel> vessels) {
			this.vessels = vessels.toArray();
		}
	}

	public static class VesselClassContainer {
		public Object[] vesselClasses;

		public VesselClassContainer(List<VesselClass> vesselClasses) {
			this.vesselClasses = vesselClasses.toArray();
		}
	}

	private IScenarioEditingLocation loc;

	public VesselAndClassContentProvider(IScenarioEditingLocation loc) {
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

				return new Object[] { new VesselContainer(fleetModel.getVessels()), new VesselClassContainer(fleetModel.getVesselClasses()) };
			}
		}

		return new Object[0];
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof VesselContainer) {
			VesselContainer vesselContainer = (VesselContainer) parentElement;
			return vesselContainer.vessels;
		}
		if (parentElement instanceof VesselClassContainer) {
			VesselClassContainer vesselClassContainer = (VesselClassContainer) parentElement;
			return vesselClassContainer.vesselClasses;
		}

		return new Object[0];
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		if (element instanceof VesselContainer) {
			return true;
		}
		if (element instanceof VesselClassContainer) {
			return true;
		}
		return false;
	}

}