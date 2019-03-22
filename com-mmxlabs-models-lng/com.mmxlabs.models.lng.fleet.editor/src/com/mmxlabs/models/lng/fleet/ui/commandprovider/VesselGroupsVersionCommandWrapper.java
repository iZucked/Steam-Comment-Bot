/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.commandprovider;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.VersionRecord;
import com.mmxlabs.scenario.service.model.util.extpoint.AbstractVersionCommandWrapper;

/**
 * @author Simon Goodall
 * 
 */
public class VesselGroupsVersionCommandWrapper extends AbstractVersionCommandWrapper {

	private static final String TYPE_ID = LNGScenarioSharedModelTypes.VESSEL_GROUPS.getID();
	private static final EReference VERSION_FEATURE = FleetPackage.Literals.FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD;

	public VesselGroupsVersionCommandWrapper() {
		super(TYPE_ID, VERSION_FEATURE);
	}

	@Override
	protected @Nullable EObject getModelRoot(EObject obj) {
		if (obj instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) obj;
			return ScenarioModelUtil.getFleetModel(lngScenarioModel);
		}
		return null;
	}

	@Override
	protected @NonNull Adapter createAdapter(final boolean[] changedRef) {
		return new EContentAdapter() {

			@Override
			public void notifyChanged(final Notification notification) {
				super.notifyChanged(notification);
				if (notification.getNotifier() instanceof VesselGroup) {
					changedRef[0] = true;
				} else if (notification.getFeature() == FleetPackage.Literals.FLEET_MODEL__VESSEL_GROUPS) {
					changedRef[0] = true;
				}

				// Reset!
				if (notification.getFeature() == MMXCorePackage.Literals.VERSION_RECORD__VERSION) {

					if (modelArtifact != null) {
						modelArtifact.setDataVersion(notification.getNewStringValue());
					}

					changedRef[0] = false;
				} else if (notification.getFeature() == VERSION_FEATURE) {

					if (modelArtifact != null) {
						VersionRecord vr = (VersionRecord) notification.getNewValue();
						modelArtifact.setDataVersion(vr.getVersion());
					}

					changedRef[0] = false;
				}
			}

			/**
			 * Handles installation of the adapter on an EObject by adding the adapter to each of the directly contained objects.
			 */
			@Override
			protected void setTarget(final EObject target) {
				basicSetTarget(target);
				if (target instanceof FleetModel) {
					FleetModel fleetModel = (FleetModel) target;
					addAdapter(fleetModel.getVesselGroupVersionRecord());

					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						if (notifier instanceof VesselGroup //
						) {
							addAdapter(notifier);
						}
					}
				} else if (target instanceof VesselGroup) {
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						addAdapter(notifier);
					}
				}
			}

			/**
			 * Handles undoing the installation of the adapter from an EObject by removing the adapter from each of the directly contained objects.
			 */
			@Override
			protected void unsetTarget(final EObject target) {
				basicUnsetTarget(target);
				if (target instanceof FleetModel) {
					FleetModel fleetModel = (FleetModel) target;
					removeAdapter(fleetModel.getVesselGroupVersionRecord(), false, true);
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						if (notifier instanceof VesselGroup //
						) {
							removeAdapter(notifier, false, true);
						}
					}
				} else if (target instanceof VesselGroup) {
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						removeAdapter(notifier, false, true);
					}
				}
			}
		};
	}

}
