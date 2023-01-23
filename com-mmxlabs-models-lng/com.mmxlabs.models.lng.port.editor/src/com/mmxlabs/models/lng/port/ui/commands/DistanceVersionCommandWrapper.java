/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.commands;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
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
public class DistanceVersionCommandWrapper extends AbstractVersionCommandWrapper {

	private static final String TYPE_ID = LNGScenarioSharedModelTypes.DISTANCES.getID();
	private static final EReference VERSION_FEATURE = PortPackage.Literals.PORT_MODEL__DISTANCE_VERSION_RECORD;

	public DistanceVersionCommandWrapper() {
		super(TYPE_ID, VERSION_FEATURE);
	}

	@Override
	protected @Nullable EObject getModelRoot(EObject obj) {
		if (obj instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) obj;
			return ScenarioModelUtil.getPortModel(lngScenarioModel);
		}
		return null;
	}

	@Override
	protected @NonNull Adapter createAdapter(final boolean[] changedRef) {

		return new EContentAdapter() {

			@Override
			public void notifyChanged(final Notification notification) {
				super.notifyChanged(notification);
				if (notification.getNotifier() instanceof RouteLine) {
					changedRef[0] = true;
				} else if (notification.getNotifier() instanceof Location) {
					changedRef[0] = true;
				} else if (notification.getNotifier() instanceof Route) {
					changedRef[0] = true;
				} else if (notification.getNotifier() instanceof EntryPoint) {
					changedRef[0] = true;
				} else if (notification.getFeature() == PortPackage.Literals.ROUTE__LINES) {
					changedRef[0] = true;
				} else if (notification.getFeature() == PortPackage.Literals.PORT__LOCATION) {
					changedRef[0] = true;
				} else if (notification.getFeature() == PortPackage.Literals.PORT_MODEL__ROUTES) {
					changedRef[0] = true;
				} else if (notification.getFeature() == PortPackage.Literals.PORT_MODEL__PORTS) {
					// Add or remove is a change.
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
				if (target instanceof PortModel) {
					PortModel portModel = (PortModel) target;
					addAdapter(portModel.getDistanceVersionRecord());

					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						if (notifier instanceof Route) {
							addAdapter(notifier);
						} else if (notifier instanceof Port) {
							Port port = (Port) notifier;
							addAdapter(port.getLocation());
						}
					}
				} else if (target instanceof Route) {
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						addAdapter(notifier);
					}
				} else if (target instanceof Location) {
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
				if (target instanceof PortModel) {
					PortModel portModel = (PortModel) target;
					removeAdapter(portModel.getDistanceVersionRecord(), false, true);
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						if (notifier instanceof Route) {
							removeAdapter(notifier, false, true);
						} else if (notifier instanceof Port) {
							Port port = (Port) notifier;
							removeAdapter(port.getLocation(), false, true);
						}
					}
				} else if (target instanceof Route) {
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						removeAdapter(notifier, false, true);
					}
				} else if (target instanceof Location) {
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						removeAdapter(notifier, false, true);
					}
				}
			}
		};
	}
}
