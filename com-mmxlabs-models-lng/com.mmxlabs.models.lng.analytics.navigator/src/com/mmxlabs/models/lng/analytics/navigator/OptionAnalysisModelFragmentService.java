/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.databinding.swt.DisplayRealm;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.model.manager.IPostChangeHook;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class OptionAnalysisModelFragmentService {

	private final Map<ScenarioModelRecord, ModelAdapter> dataMap = new ConcurrentHashMap<>();

	private final IPostChangeHook loadHook = new IPostChangeHook() {

		@Override
		public void changed(@NonNull final ScenarioModelRecord modelRecord) {
			synchronized (dataMap) {
				dataMap.put(modelRecord, new ModelAdapter(modelRecord));
			}
		}
	};

	private final IPostChangeHook unloadHook = new IPostChangeHook() {

		@Override
		public void changed(@NonNull final ScenarioModelRecord modelRecord) {

			synchronized (dataMap) {
				final ModelAdapter adapter = dataMap.remove(modelRecord);
				if (adapter != null) {
					adapter.dispose();
				}
			}
		}
	};

	public void start() {
		SSDataManager.Instance.registerChangeHook(loadHook, SSDataManager.PostChangeHookPhase.ON_LOAD);
		SSDataManager.Instance.registerChangeHook(unloadHook, SSDataManager.PostChangeHookPhase.ON_UNLOAD);
	}

	public void stop() {
		SSDataManager.Instance.removeChangeHook(loadHook, SSDataManager.PostChangeHookPhase.ON_LOAD);
		SSDataManager.Instance.removeChangeHook(unloadHook, SSDataManager.PostChangeHookPhase.ON_UNLOAD);

		while (!dataMap.isEmpty()) {
			final Map.Entry<ScenarioModelRecord, ModelAdapter> e = dataMap.entrySet().iterator().next();
			dataMap.remove(e.getKey());
			final ModelAdapter adapter = e.getValue();
			if (adapter != null) {
				adapter.dispose();
			}
		}
	}

	private class ModelAdapter extends MMXAdapterImpl {
		private final ObservablesManager manager = new ObservablesManager();
		private final EMFDataBindingContext dbc = new EMFDataBindingContext(DisplayRealm.getRealm(PlatformUI.getWorkbench().getDisplay()));

		// private final ScenarioInstance scenarioInstance;
		private final ScenarioModelRecord modelRecord;
		private LNGScenarioModel scenarioModel;
		private final Map<EObject, ScenarioFragment> objectToFragmentMap = new HashMap<EObject, ScenarioFragment>();

		public ModelAdapter(final ScenarioModelRecord modelRecord) {
			this.modelRecord = modelRecord;
			// Execute with reference to avoid unloading while processing
			modelRecord.execute(modelReference -> {
				processScenario();
			});

		}

		@Override
		public void setTarget(Notifier newTarget) {
			// Do not store the target.
			// TODO (SG) - Does this break anything or does this mean we should create a new instance each time it is used?
		}

		/**
		 * Process initial scenario state and create fragments.
		 */
		private void processScenario() {
			final ModelReference modelReference = modelRecord.getSharedReference();
			assert modelReference != null;
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
			if (scenarioModel.getAnalyticsModel() != null) {
				final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);
				for (final OptionAnalysisModel plan : analyticsModel.getOptionModels()) {
					createFragment(plan);
				}
				for (final BreakEvenAnalysisModel plan : analyticsModel.getBreakevenModels()) {
					createFragment(plan);
				}
				analyticsModel.eAdapters().add(ModelAdapter.this);
			}
			this.scenarioModel = scenarioModel;
		}

		public void dispose() {

			manager.dispose();
			if (scenarioModel != null) {
				final AnalyticsModel analyticsModel = scenarioModel.getAnalyticsModel();
				if (analyticsModel != null) {

					analyticsModel.eAdapters().remove(ModelAdapter.this);
					for (final OptionAnalysisModel plan : analyticsModel.getOptionModels()) {
						removeFragment(plan);
					}
					for (final BreakEvenAnalysisModel plan : analyticsModel.getBreakevenModels()) {
						removeFragment(plan);
					}

					// Safety check - previous step should have removed all the fragments, but just in case, remove anything left over.
					for (final EObject plan : new HashSet<EObject>(objectToFragmentMap.keySet())) {
						removeFragment(plan);
					}
				}
			}
			objectToFragmentMap.clear();
			scenarioModel = null;
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

			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels()) {
				if (notification.getEventType() == Notification.ADD) {
					final EObject eObj = (EObject) notification.getNewValue();
					if (eObj instanceof NamedObject) {
						createFragment((NamedObject) eObj);
					}
				} else if (notification.getEventType() == Notification.REMOVE) {
					final EObject eObj = (EObject) notification.getOldValue();
					removeFragment(eObj);
				}
				// TODO: Handle ADD_/REMOVE_MANY ?
			}
			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_BreakevenModels()) {
				if (notification.getEventType() == Notification.ADD) {
					final EObject eObj = (EObject) notification.getNewValue();
					if (eObj instanceof NamedObject) {
						createFragment((NamedObject) eObj);
					}
				} else if (notification.getEventType() == Notification.REMOVE) {
					final EObject eObj = (EObject) notification.getOldValue();
					removeFragment(eObj);
				}
				// TODO: Handle ADD_/REMOVE_MANY ?
			}
		}

		private void removeFragment(final EObject plan) {
			final ScenarioFragment fragment = objectToFragmentMap.remove(plan);
			modelRecord.getScenarioInstance().getFragments().remove(fragment);
		}

		private void createFragment(final NamedObject plan) {
			final ScenarioFragment fragment = ScenarioServiceFactory.eINSTANCE.createScenarioFragment();
			fragment.setFragment(plan);
			fragment.setName(plan.getName());
			fragment.setUseCommandStack(true);
			objectToFragmentMap.put(plan, fragment);
			modelRecord.getScenarioInstance().getFragments().add(fragment);
			// Create a databinding to keep names in sync
			dbc.getValidationRealm().exec(() -> {
				final IObservableValue fragmentObserver = EMFObservables.observeValue(fragment, ScenarioServicePackage.eINSTANCE.getScenarioFragment_Name());
				final IObservableValue objectObserver = EMFObservables.observeValue(plan, MMXCorePackage.eINSTANCE.getNamedObject_Name());
				dbc.bindValue(fragmentObserver, objectObserver);

				// Add to manager to handle clean up
				manager.addObservable(fragmentObserver);
				manager.addObservable(objectObserver);
			});
		}
	}
}
