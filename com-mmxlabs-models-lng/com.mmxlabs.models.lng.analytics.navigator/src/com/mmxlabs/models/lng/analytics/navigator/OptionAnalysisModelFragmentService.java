/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.navigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jface.databinding.swt.DisplayRealm;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.manifest.StorageType;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.model.manager.IPostChangeHook;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class OptionAnalysisModelFragmentService {

	private static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}

	private static final String TYPE_SANDBOX = "fragment-basic-sandbox";

	private final Map<ScenarioModelRecord, ModelAdapter> dataMap = new ConcurrentHashMap<>();

	private ComposedAdapterFactory adapterFactory = createAdapterFactory();

	private final IPostChangeHook loadHook = modelRecord -> {
		synchronized (dataMap) {
			dataMap.put(modelRecord, new ModelAdapter(modelRecord));
		}
	};

	private final IPostChangeHook unloadHook = modelRecord -> {
		synchronized (dataMap) {
			final ModelAdapter adapter = dataMap.remove(modelRecord);
			if (adapter != null) {
				adapter.dispose();
			}
		}
	};

	public void bindScenarioModel(ScenarioServiceRegistry registry) {
		registry.getScenarioModel().eAdapters().add(modelAdapter);
	}

	public void unbindScenarioModel(ScenarioServiceRegistry registry) {
		registry.getScenarioModel().eAdapters().remove(modelAdapter);
	}

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
		private ScenarioModelRecord modelRecord;
		private LNGScenarioModel scenarioModel;
		private final Map<EObject, ScenarioFragment> objectToFragmentMap = new HashMap<>();
		private final Map<String, ScenarioFragment> uuidToFragmentMap = new HashMap<>();
		private final Map<String, ModelArtifact> uuidToArtifactMap = new HashMap<>();
		private final Map<String, EObject> uuidToModelMap = new HashMap<>();

		public ModelAdapter(final ScenarioModelRecord modelRecord) {
			this.modelRecord = modelRecord;
			// Execute with reference to avoid unloading while processing
			modelRecord.execute(modelReference -> {
				processScenario();
			});

		}

		/**
		 * Process initial scenario state and create fragments.
		 */
		private void processScenario() {
			final ModelReference modelReference = modelRecord.getSharedReference();
			assert modelReference != null;

			// Gather existing fragments
			for (ScenarioFragment f : modelRecord.getScenarioInstance().getFragments()) {
				if (f.getContentType() != null) {
					uuidToFragmentMap.put(f.getContentType(), f);
				}
			}

			// Gather existing artifacts
			for (ModelArtifact fragment : modelRecord.getManifest().getModelFragments()) {
				if (fragment.getStorageType() != StorageType.INTERNAL) {
					continue;
				}
				if (TYPE_SANDBOX.equals(fragment.getType())) {
					String uuid = fragment.getKey();
					uuidToArtifactMap.put(uuid, fragment);
				}
			}

			Set<ModelArtifact> seenArtifacts = new HashSet<>();

			// Update the scenario model artifacts
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
			if (scenarioModel.getAnalyticsModel() != null) {
				final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);

				List<AbstractAnalysisModel> plans = new LinkedList<>();
				plans.addAll(analyticsModel.getOptionModels());
				plans.addAll(analyticsModel.getBreakevenModels());

				for (final AbstractAnalysisModel plan : plans) {
					String uuid = plan.getUuid();
					final ModelArtifact artifact;
					if (uuidToArtifactMap.containsKey(uuid)) {
						artifact = uuidToArtifactMap.get(uuid);
						artifact.setDisplayName(plan.getName());
						seenArtifacts.add(artifact);
					} else {
						artifact = ManifestFactory.eINSTANCE.createModelArtifact();
						artifact.setKey(uuid);
						artifact.setStorageType(StorageType.INTERNAL);
						artifact.setType(TYPE_SANDBOX);
						artifact.setDisplayName(plan.getName());
						seenArtifacts.add(artifact);
						uuidToArtifactMap.put(uuid, artifact);
						modelRecord.getManifest().getModelFragments().add(artifact);
					}
					final ScenarioFragment fragment;
					if (uuidToFragmentMap.containsKey(uuid)) {
						fragment = uuidToFragmentMap.get(uuid);
					} else {
						fragment = ScenarioServiceFactory.eINSTANCE.createScenarioFragment();
						fragment.setContentType(uuid);
						uuidToFragmentMap.put(uuid, fragment);
					}
					createFragment(fragment, plan, modelReference);
				}
				analyticsModel.eAdapters().add(ModelAdapter.this);
				List<ModelArtifact> allArtifacts = modelRecord.getManifest().getModelFragments();
				Iterator<ModelArtifact> itr = allArtifacts.iterator();
				Set<ScenarioFragment> fragmentsToRemove = new HashSet<>();
				while (itr.hasNext()) {
					ModelArtifact artifact = itr.next();
					if (!seenArtifacts.contains(artifact)) {
						itr.remove();
						fragmentsToRemove.add(uuidToFragmentMap.remove(artifact.getKey()));
						modelReference.setDirty();
					}
				}

				fragmentsToRemove.remove(null);
				modelRecord.getScenarioInstance().getFragments().removeAll(fragmentsToRemove);
			}
			this.scenarioModel = scenarioModel;
		}

		public void dispose() {

			manager.dispose();
			if (scenarioModel != null) {
				final AnalyticsModel analyticsModel = scenarioModel.getAnalyticsModel();
				if (analyticsModel != null) {
					analyticsModel.eAdapters().remove(ModelAdapter.this);
					List<AbstractAnalysisModel> plans = new LinkedList<>();
					plans.addAll(analyticsModel.getOptionModels());
					plans.addAll(analyticsModel.getBreakevenModels());

					for (final AbstractAnalysisModel plan : plans) {
						ScenarioFragment fragment = uuidToFragmentMap.get(plan.getUuid());
						if (fragment != null) {
							fragment.setFragment(null);
						}
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

		private Consumer<EObject> addHandler = eObj -> RunnerHelper.asyncExec(() -> {

			if (eObj instanceof AbstractAnalysisModel) {
				AbstractAnalysisModel plan = (AbstractAnalysisModel) eObj;
				String uuid = plan.getUuid();
				final ScenarioFragment fragment;
				if (uuidToFragmentMap.containsKey(uuid)) {
					fragment = uuidToFragmentMap.get(uuid);
				} else {
					fragment = ScenarioServiceFactory.eINSTANCE.createScenarioFragment();
					fragment.setContentType(uuid);
					uuidToFragmentMap.put(uuid, fragment);
				}

				{
					boolean found = false;
					List<ModelArtifact> allArtifacts = modelRecord.getManifest().getModelFragments();
					Iterator<ModelArtifact> itr = allArtifacts.iterator();
					while (itr.hasNext()) {
						ModelArtifact artifact = itr.next();
						if (uuid.equals(artifact.getKey())) {
							found = true;
							break;
						}
					}
					if (!found) {
						ModelArtifact artifact = ManifestFactory.eINSTANCE.createModelArtifact();
						artifact.setKey(uuid);
						artifact.setStorageType(StorageType.INTERNAL);
						artifact.setType(TYPE_SANDBOX);
						artifact.setDisplayName(plan.getName());
						uuidToArtifactMap.put(uuid, artifact);
						modelRecord.getManifest().getModelFragments().add(artifact);
					}
				}
				createFragment(fragment, plan, modelRecord.getSharedReference());
			}
		});

		private Consumer<EObject> removeHandler = eObj -> RunnerHelper.asyncExec(() -> {
			if (eObj instanceof AbstractAnalysisModel) {

				AbstractAnalysisModel plan = (AbstractAnalysisModel) eObj;
				String uuid = plan.getUuid();
				removeFragment(eObj);

				List<ModelArtifact> allArtifacts = modelRecord.getManifest().getModelFragments();
				Iterator<ModelArtifact> itr = allArtifacts.iterator();
				while (itr.hasNext()) {
					ModelArtifact artifact = itr.next();
					if (uuid.equals(artifact.getKey())) {
						itr.remove();
						uuidToArtifactMap.remove(uuid);
					}
				}
			}
		});

		@Override
		public void reallyNotifyChanged(final Notification notification) {

			// Basic filter
			if (notification.isTouch() || notification.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}

			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels()) {
				if (notification.getEventType() == Notification.ADD) {
					final EObject eObj = (EObject) notification.getNewValue();
					addHandler.accept(eObj);
				} else if (notification.getEventType() == Notification.REMOVE) {
					final EObject eObj = (EObject) notification.getOldValue();
					removeHandler.accept(eObj);
				}
				// TODO: Handle ADD_/REMOVE_MANY ?
			}
			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_BreakevenModels()) {
				if (notification.getEventType() == Notification.ADD) {
					final EObject eObj = (EObject) notification.getNewValue();
					addHandler.accept(eObj);

				} else if (notification.getEventType() == Notification.REMOVE) {
					final EObject eObj = (EObject) notification.getOldValue();
					removeHandler.accept(eObj);
				}
				// TODO: Handle ADD_/REMOVE_MANY ?
			}
		}

		private void removeFragment(final EObject plan) {
			final ScenarioFragment fragment = objectToFragmentMap.remove(plan);
			modelRecord.getScenarioInstance().getFragments().remove(fragment);
		}

		private void createFragment(final ScenarioFragment fragment, final NamedObject plan, ModelReference modelReference) {
			if (modelReference == null || fragment == null) {
				return;
			}
			fragment.setFragment(plan);
			fragment.setName(plan.getName());
			fragment.setUseCommandStack(true);
			objectToFragmentMap.put(plan, fragment);
			modelRecord.getScenarioInstance().getFragments().add(fragment);
			// Create a databinding to keep names in sync
			dbc.getValidationRealm().exec(() -> {
				final IObservableValue fragmentObserver = EMFObservables.observeValue(fragment, ScenarioServicePackage.eINSTANCE.getScenarioFragment_Name());
				final IObservableValue objectObserver = EMFObservables.observeValue(plan, MMXCorePackage.eINSTANCE.getNamedObject_Name());

				objectObserver.addChangeListener(new IChangeListener() {

					@Override
					public void handleChange(ChangeEvent event) {
						modelRecord.getSharedReference().setDirty();
					}
				});

				dbc.bindValue(fragmentObserver, objectObserver);

				// Add to manager to handle clean up
				manager.addObservable(fragmentObserver);
				manager.addObservable(objectObserver);
			});
		}
	}

	private ScenarioModel model;

	EContentAdapter modelAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(Notification notification) {

			// Check for add/remove child models
			super.notifyChanged(notification);

			if (notification.isTouch()) {
				return;
			}
			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioModel_ScenarioServices()) {
				if (notification.getEventType() == Notification.ADD) {
					walkContainer(((ScenarioService) notification.getNewValue()));
					((ScenarioService) notification.getNewValue()).eAdapters().add(serviceAdapter);
				} else if (notification.getEventType() == Notification.REMOVE) {
					((ScenarioService) notification.getOldValue()).eAdapters().remove(serviceAdapter);
				}
			}
		}
	};

	private void walkContainer(Container c) {
		if (c instanceof ScenarioInstance) {
			ScenarioInstance scenarioInstance = (ScenarioInstance) c;
			Manifest manifest = scenarioInstance.getManifest();
			if (manifest != null) {
				Runnable r = () -> {
					// Gather existing fragments
					Set<String> seenIds = new HashSet<>();
					for (ScenarioFragment f : scenarioInstance.getFragments()) {
						if (f.getContentType() != null) {
							seenIds.add(f.getContentType());
						}
					}

					List<ScenarioFragment> fragments = new LinkedList<>();
					for (ModelArtifact artifact : manifest.getModelFragments()) {
						if (artifact.getStorageType() != StorageType.INTERNAL) {
							continue;
						}
						if (TYPE_SANDBOX.equals(artifact.getType())) {
							String uuid = artifact.getKey();
							if (seenIds.contains(uuid)) {
								continue;
							}
							final ScenarioFragment fragment = ScenarioServiceFactory.eINSTANCE.createScenarioFragment();
							fragment.setFragment(null);
							fragment.setName(artifact.getDisplayName());
							fragment.setUseCommandStack(false);
							fragment.setContentType(uuid); // Subvert the use of content type to store UUID
							fragments.add(fragment);
						}
					}

					scenarioInstance.getFragments().addAll(fragments);

				};
				// When starting LiNGO, there may not be a display thread
				if (!RunnerHelper.asyncExec(r)) {
					r.run();
				}

			}
		}
		for (Container child : c.getElements()) {
			walkContainer(child);
		}
	}

	EContentAdapter serviceAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);

			if (notification.isTouch()) {
				return;
			}

			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_Manifest()) {
				if (notification.getEventType() == Notification.SET) {
					Object child = notification.getNewValue();
					if (child instanceof ScenarioInstance) {
						ScenarioInstance scenarioInstance = (ScenarioInstance) child;
						walkContainer(scenarioInstance);
					}
				}
			}
			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getContainer_Elements()) {
				if (notification.getEventType() == Notification.ADD) {
					Object child = notification.getNewValue();
					if (child instanceof ScenarioInstance) {
						ScenarioInstance scenarioInstance = (ScenarioInstance) child;
						walkContainer(scenarioInstance);
					}
				}
			}
		}
	};
}
