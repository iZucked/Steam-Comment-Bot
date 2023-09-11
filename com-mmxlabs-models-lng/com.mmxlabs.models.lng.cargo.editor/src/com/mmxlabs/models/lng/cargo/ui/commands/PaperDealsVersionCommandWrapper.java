/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

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

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.VersionRecord;
import com.mmxlabs.rcp.common.versions.VersionsUtil;
import com.mmxlabs.scenario.service.model.util.extpoint.AbstractVersionCommandWrapper;

public class PaperDealsVersionCommandWrapper extends AbstractVersionCommandWrapper {

	private static final String TYPE_ID = LNGScenarioSharedModelTypes.PAPER_DEALS.getID();
	private static final EReference VERSION_FEATURE = CargoPackage.Literals.CARGO_MODEL__PAPER_DEALS_VERSION_RECORD;

	public PaperDealsVersionCommandWrapper() {
		super(TYPE_ID, VERSION_FEATURE);
	}

	@Override
	protected @Nullable EObject getModelRoot(EObject obj) {
		if (obj instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) obj;
			return ScenarioModelUtil.getCargoModel(lngScenarioModel);
		}
		return null;
	}

	@Override
	protected @NonNull Adapter createAdapter(final boolean[] changedRef) {

		return new EContentAdapter() {

			@Override
			public void notifyChanged(final Notification notification) {
				super.notifyChanged(notification);
				if (notification.isTouch()) {
					return;
				}
				if (notification.getNotifier() instanceof PaperDeal) {
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
				if (target instanceof CargoModel) {
					CargoModel cargoModel = (CargoModel) target;
					// Hook to migrate
					if (cargoModel.getPaperDealsVersionRecord() == null) {
						cargoModel.setPaperDealsVersionRecord(VersionsUtil.createNewRecord());
					}
					addAdapter(cargoModel.getPaperDealsVersionRecord());
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						if (notifier instanceof PaperDeal) {
							addAdapter(notifier);
						}
					}
				} else if (target instanceof PaperDeal) {
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
				if (target instanceof CargoModel) {
					CargoModel cargoModel = (CargoModel) target;
					removeAdapter(cargoModel.getPaperDealsVersionRecord(), false, true);

					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						if (notifier instanceof PaperDeal) {
							removeAdapter(notifier, false, true);
						}
					}
				} else if (target instanceof PaperDeal) {
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						removeAdapter(notifier, false, true);
					}
				}
			}
		};
	}

}
