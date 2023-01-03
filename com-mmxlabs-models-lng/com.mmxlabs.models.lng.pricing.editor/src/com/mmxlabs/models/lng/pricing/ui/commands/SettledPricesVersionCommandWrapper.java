/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.commands;

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

import com.mmxlabs.models.lng.pricing.DatePoint;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.scenario.service.model.util.extpoint.AbstractVersionCommandWrapper;

/**
 * @author Simon Goodall
 * 
 */
public class SettledPricesVersionCommandWrapper extends AbstractVersionCommandWrapper {

	private static final String TYPE_ID = LNGScenarioSharedModelTypes.SETTLED_PRICES.getID();
	private static final EReference VERSION_FEATURE = PricingPackage.Literals.PRICING_MODEL__SETTLED_PRICES_VERSION_RECORD;

	public SettledPricesVersionCommandWrapper() {
		super(TYPE_ID, VERSION_FEATURE);
	}

	@Override
	protected @Nullable EObject getModelRoot(EObject obj) {
		if (obj instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) obj;
			return ScenarioModelUtil.getPricingModel(lngScenarioModel);
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
				if (notification.getNotifier() instanceof DatePoint) {
					changedRef[0] = true;
				} else if (notification.getNotifier() instanceof DatePointContainer) {
					changedRef[0] = true;
				} else if (notification.getFeature() == PricingPackage.Literals.PRICING_MODEL__SETTLED_PRICES) {
					changedRef[0] = true;
				}

				// Reset!
				if (notification.getFeature() == MMXCorePackage.Literals.VERSION_RECORD__VERSION) {

					if (modelArtifact != null) {
						modelArtifact.setDataVersion(notification.getNewStringValue());
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
				if (target instanceof PricingModel) {
					PricingModel pricingModel = (PricingModel) target;
					addAdapter(pricingModel.getSettledPricesVersionRecord());
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						if (notifier instanceof DatePointContainer) {
							addAdapter(notifier);
						}
					}

				} else if (target instanceof DatePointContainer) {
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
				if (target instanceof PricingModel) {
					PricingModel pricingModel = (PricingModel) target;
					removeAdapter(pricingModel.getSettledPricesVersionRecord(), false, true);

					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						if (notifier instanceof DatePointContainer) {
							removeAdapter(notifier, false, true);
						}
					}

				} else if (target instanceof DatePointContainer) {
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						removeAdapter(notifier, false, true);
					}
				}
			}
		};
	}

}
