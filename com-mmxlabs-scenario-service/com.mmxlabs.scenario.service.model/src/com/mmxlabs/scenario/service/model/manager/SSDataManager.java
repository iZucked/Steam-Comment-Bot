/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;

/**
 * The {@link SSDataManager} manages the transient values of a {@link ScenarioInstance} - i.e. lock status, references to underlying data, {@link EditingDomain}s etc.
 * 
 * @author Simon Goodall
 *
 */
public class SSDataManager {
	/**
	 * List of enums we can register handlers for
	 *
	 */
	public static enum PostChangeHookPhase {
		ON_LOAD, ON_UNLOAD, VALIDATION, EVALUATION;
	};

	/**
	 * Change events. These will trigger one or more {@link PostChangeHookPhase}s
	 *
	 */
	public static enum PostChangeType {
		LOAD, UNLOAD, EDIT, UNDO, REDO;
	};

	private static final Logger LOG = LoggerFactory.getLogger(SSDataManager.class);

	public static final SSDataManager Instance = new SSDataManager();

	private final @NonNull ConcurrentLinkedQueue<com.mmxlabs.scenario.service.model.manager.IPostChangeHook> postChangeHooks_OnLoad = new ConcurrentLinkedQueue<>();
	private final @NonNull ConcurrentLinkedQueue<com.mmxlabs.scenario.service.model.manager.IPostChangeHook> postChangeHooks_OnUnload = new ConcurrentLinkedQueue<>();
	private final @NonNull ConcurrentLinkedQueue<com.mmxlabs.scenario.service.model.manager.IPostChangeHook> postChangeHooks_Validation = new ConcurrentLinkedQueue<>();
	private final @NonNull ConcurrentLinkedQueue<com.mmxlabs.scenario.service.model.manager.IPostChangeHook> postChangeHooks_Evaluation = new ConcurrentLinkedQueue<>();

	// FIXME: Big memory leak - references are never (rarely) cleaned up -- more
	// reference counting?
	private final ConcurrentHashMap<ScenarioInstance, ScenarioModelRecord> lookup = new ConcurrentHashMap<>();

	private final ExecutorService postChangeExecutor = Executors.newFixedThreadPool(1);

	private final Map<String, IScenarioService> serviceMap = new HashMap<>();

	private final Map<ScenarioModelRecord, CompletableFuture<Void>> futureMap = new ConcurrentHashMap<ScenarioModelRecord, CompletableFuture<Void>>();

	private class Tracker implements ServiceTrackerCustomizer<IScenarioService, IScenarioService> {
		private final BundleContext ctx;

		Tracker(final BundleContext ctx) {
			this.ctx = ctx;
		}

		@Override
		public IScenarioService addingService(final ServiceReference<IScenarioService> reference) {
			final IScenarioService service = ctx.getService(reference);
			serviceMap.put(service.getSerivceID(), service);

			return service;
		}

		@Override
		public void modifiedService(final ServiceReference<IScenarioService> reference, final IScenarioService service) {

		}

		@Override
		public void removedService(final ServiceReference<IScenarioService> reference, final IScenarioService service) {
			serviceMap.remove(service.getSerivceID());
			ctx.ungetService(reference);
		}
	}

	public SSDataManager() {
		final Bundle bundle = FrameworkUtil.getBundle(SSDataManager.class);
		if (bundle != null) {
			final BundleContext bundleContext = bundle.getBundleContext();

			tracker = new ServiceTracker<>(bundleContext, IScenarioService.class, new Tracker(bundleContext));
			tracker.open();
		}

	}

	public void dispose() {
		if (tracker != null) {
			tracker.close();
			tracker = null;
		}
	}

	// public @NonNull ModelRecord getModelRecord(@NonNull final ScenarioInstance instance) {
	// return get(instance);
	// }
	//
	// public @NonNull ModelRecord getModelRecord(@NonNull final ScenarioInstance instance, final BiFunction<ModelRecord, IProgressMonitor, InstanceData> loadFunction) {
	// if (lookup.containsKey(instance)) {
	// throw new IllegalStateException("Instance already in manager");
	// }
	// final ModelRecord record = new ModelRecord( loadFunction);
	// lookup.put(instance, record);
	// return record;
	// }

	public void register(@NonNull final ScenarioInstance instance, final ScenarioModelRecord record) {
		assert instance != null;
		assert record != null;

		lookup.put(instance, record);
	}

	public void remove(@NonNull final ScenarioInstance instance) {
		lookup.remove(instance);
	}

	public @NonNull ScenarioModelRecord getModelRecord(@NonNull final ScenarioInstance instance) {
		// final boolean[] runPostLoadHook = new boolean[1];
		final ScenarioModelRecord modelRecord = lookup.get(instance);

		// assert modelRecord != null;

		// final ModelRecord modelRecord = lookup.computeIfAbsent(instance, (_key) -> {
		//
		// VersionData versionData = new VersionData();
		// versionData.setContentType(instance.getMetadata().getContentType());
		// versionData.setScenarioContext(instance.getVersionContext());
		// versionData.setScenarioVersion(instance.getScenarioVersion());
		// versionData.setClientContext(instance.getClientVersionContext());
		// versionData.setClientVersion(instance.getClientScenarioVersion());
		//
		//// return new ModelRecord(versionData, (r, m) -> {
		// // ServiceHelper.withOptionalService(IS)
		// ModelRecord data = ScenarioStorageUtil.loadInstanceFromURI(URI.createURI(instance.getRootObjectURI()), false, true, null);
		//
		//// if (data != null) {
		//// runPostLoadHook[0] = true;
		//// }
		// return data;
		//// });
		// });
		// // We have this slight faff to ensure this gets call after the lookup map has been updated. Otherwise there is a race condition where the load callback could be called before the lookup map
		// is
		// // populated.
		//// if (runPostLoadHook[0]) {
		//// CompletableFuture.runAsync(() -> runPostChangeHooks(modelRecord, PostChangeType.LOAD), postChangeExecutor);
		//// }
		return modelRecord;
	}

	public void registerChangeHook(final IPostChangeHook hook, final PostChangeHookPhase phase) {
		switch (phase) {
		case EVALUATION:
			postChangeHooks_Evaluation.add(hook);
			break;
		case VALIDATION:
			postChangeHooks_Validation.add(hook);
			break;
		case ON_LOAD:
			postChangeHooks_OnLoad.add(hook);
			break;
		case ON_UNLOAD:
			postChangeHooks_OnUnload.add(hook);
			break;
		default:
			throw new IllegalArgumentException("Unsupported phase");
		}
	}

	public void removeChangeHook(final IPostChangeHook hook, final PostChangeHookPhase phase) {
		switch (phase) {
		case EVALUATION:
			postChangeHooks_Evaluation.remove(hook);
			break;
		case VALIDATION:
			postChangeHooks_Validation.remove(hook);
		case ON_LOAD:
			postChangeHooks_OnLoad.remove(hook);
		case ON_UNLOAD:
			postChangeHooks_OnUnload.remove(hook);
			break;
		default:
			throw new IllegalArgumentException("Unsupported phase");
		}
	}

	private static BiConsumer<ScenarioModelRecord, @NonNull Collection<IPostChangeHook>> runHooks = (modelRecord, postChangeHooks) -> {
		for (final IPostChangeHook hook : postChangeHooks) {
			// Safe loop
			try {
				hook.changed(modelRecord);
			} catch (final Exception e) {
				LOG.error("Error in post change hook", e);
			}
		}
	};

	private ServiceTracker<IScenarioService, IScenarioService> tracker;

	public void runPostChangeHooks(@NonNull final ScenarioModelRecord modelRecord, @NonNull final PostChangeType changeType) {

		// Cancel existing requests
		// FIXME: I am not sure if this really works. System.out.printlns seems
		// to suggest not, but maybe it kicks in with longer running processes.
		final CompletableFuture<Void> oldFuture = futureMap.remove(modelRecord);
		if (oldFuture != null) {
			oldFuture.cancel(true);
		}

		if (changeType == PostChangeType.UNLOAD) {
			// Only run the unloads synchronously
			runHooks.accept(modelRecord, postChangeHooks_OnUnload);
		} else {
			final CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
				try (ModelReference ref = modelRecord.aquireReference("SSDataManager")) {
					if (changeType == PostChangeType.LOAD) {
						runHooks.accept(modelRecord, postChangeHooks_OnLoad);
					}
					runHooks.accept(modelRecord, postChangeHooks_Validation);
					if (changeType == PostChangeType.EDIT) {
						runHooks.accept(modelRecord, postChangeHooks_Evaluation);
					}
				} catch (final Exception e) {
					e.printStackTrace();
				} finally {
					// Clean up mapping
					futureMap.remove(modelRecord);
				}
			}, postChangeExecutor);
			futureMap.put(modelRecord, future);
		}
	}

	public void releaseModelRecord(@NonNull final ScenarioInstance instance) {
		// TODO: .dispose()?
		lookup.remove(instance).dispose();

	}

	public IScenarioService findScenarioService(@NonNull Container c) {
		while (c != null && !(c instanceof ScenarioService)) {
			c = c.getParent();
		}
		if (c instanceof ScenarioService) {
			final ScenarioService scenarioService = (ScenarioService) c;
			return serviceMap.get(scenarioService.getServiceID());
		}
		return null;
	}
}
