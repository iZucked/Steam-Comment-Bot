/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.navigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * An implementation of {@link ScenarioServiceListener} which maintains {@link ScenarioFragment} links to all {@link ShippingCostPlan}s
 * 
 * @author Simon Goodall
 * 
 */
public class OptionAnalysisModelSSListener extends ScenarioServiceListener {

	private final Map<ScenarioInstance, ModelAdapter> adapterMap = new ConcurrentHashMap<>();

	private class ModelAdapter extends MMXAdapterImpl {
		private final ObservablesManager manager = new ObservablesManager();
		private final EMFDataBindingContext dbc = new EMFDataBindingContext(SWTObservables.getRealm(Display.getDefault()));

		private final ScenarioInstance scenarioInstance;
		private LNGScenarioModel scenarioModel;
		private final Map<EObject, ScenarioFragment> objectToFragmentMap = new HashMap<EObject, ScenarioFragment>();

		public ModelAdapter(final ScenarioInstance scenarioInstance) {
			this.scenarioInstance = scenarioInstance;
			processScenario();

		}

		/**
		 * Process initial scenario state and create fragments.
		 */
		private void processScenario() {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenarioInstance.getInstance();

			for (final OptionAnalysisModel plan : scenarioModel.getOptionModels()) {
				createFragment(plan);
			}

			scenarioModel.eAdapters().add(ModelAdapter.this);
			this.scenarioModel = scenarioModel;
		}

		public void dispose() {

			manager.dispose();
			if (scenarioModel != null) {
				scenarioModel.eAdapters().remove(ModelAdapter.this);

				for (final OptionAnalysisModel plan : scenarioModel.getOptionModels()) {
					removeFragment(plan);
				}

				// Safety check - previous step should have removed all the fragments, but just in case, remove anything left over.
				for (final EObject plan : new HashSet<EObject>(objectToFragmentMap.keySet())) {
					removeFragment(plan);
				}
			}
		}

		@Override
		protected void missedNotifications(final List<Notification> missed) {
			super.missedNotifications(missed);
			for (final Notification n : new ArrayList<Notification>(missed)) {
				if (n != null) {
					reallyNotifyChanged(n);
				}
			}
		}

		@Override
		public void reallyNotifyChanged(final Notification notification) {

			// Basic filter
			if (notification.isTouch() || notification.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}

			if (notification.getFeature() == LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_OptionModels()) {
				if (notification.getEventType() == Notification.ADD) {
					final EObject eObj = (EObject) notification.getNewValue();
					createFragment(eObj);
				} else if (notification.getEventType() == Notification.REMOVE) {
					final EObject eObj = (EObject) notification.getOldValue();
					removeFragment(eObj);
				}
				// TODO: Handle ADD_/REMOVE_MANY ?
			}
		}

		private void removeFragment(final EObject plan) {
			final ScenarioFragment fragment = objectToFragmentMap.remove(plan);
			scenarioInstance.getFragments().remove(fragment);
		}

		private void createFragment(final EObject plan) {
			final ScenarioFragment fragment = ScenarioServiceFactory.eINSTANCE.createScenarioFragment();
			fragment.setFragment(plan);
			objectToFragmentMap.put(plan, fragment);
			scenarioInstance.getFragments().add(fragment);

			// Create a databinding to keep names in sync
			final IObservableValue fragmentObserver = EMFObservables.observeValue(fragment, ScenarioServicePackage.eINSTANCE.getScenarioFragment_Name());
			final IObservableValue objectObserver = EMFObservables.observeValue(plan, MMXCorePackage.eINSTANCE.getNamedObject_Name());
			dbc.bindValue(fragmentObserver, objectObserver);

			// Add to manager to handle clean up
			manager.addObservable(fragmentObserver);
			manager.addObservable(objectObserver);
		}

	}

	@Override
	public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		final ModelAdapter adapter = new ModelAdapter(scenarioInstance);
		adapterMap.put(scenarioInstance, adapter);
	}

	@Override
	public void onPreScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		final ModelAdapter adapter = adapterMap.remove(scenarioInstance);
		// Sometimes the adapter will be null - I think if there is an error loading the scenario initially.
		if (adapter != null) {
			adapter.dispose();
		}
	}

	public void dispose() {
		for (final ModelAdapter adapter : adapterMap.values()) {
			adapter.dispose();
		}
		adapterMap.clear();
	}
}
