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
import java.util.function.BiFunction;

import org.eclipse.core.runtime.IProgressMonitor;
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

	public static enum PostChangeHookPhase {
		ON_LOAD, ON_UNLOAD, VALIDATION, EVALUATION;
	};

	public static enum PostChangeType {
		LOAD, UNLOAD, EDIT, UNDO, REDO;
	};

	private static final Logger LOG = LoggerFactory.getLogger(SSDataManager.class);

	public static final SSDataManager Instance = new SSDataManager();

	private final @NonNull ConcurrentLinkedQueue<@NonNull IPostChangeHook> postChangeHooks_OnLoad = new ConcurrentLinkedQueue<>();
	private final @NonNull ConcurrentLinkedQueue<@NonNull IPostChangeHook> postChangeHooks_OnUnload = new ConcurrentLinkedQueue<>();
	private final @NonNull ConcurrentLinkedQueue<@NonNull IPostChangeHook> postChangeHooks_Validation = new ConcurrentLinkedQueue<>();
	private final @NonNull ConcurrentLinkedQueue<@NonNull IPostChangeHook> postChangeHooks_Evaluation = new ConcurrentLinkedQueue<>();

	// FIXME: Big memory leak - references are never (rarely) cleaned up -- more
	// reference counting?
	private final ConcurrentHashMap<ScenarioInstance, ModelRecord> lookup = new ConcurrentHashMap<>();

	private final ExecutorService postChangeExecutor = Executors.newFixedThreadPool(1);

	private final Map<String, IScenarioService> serviceMap = new HashMap<>();

	private final Map<ModelRecord, CompletableFuture<Void>> futureMap = new ConcurrentHashMap<ModelRecord, CompletableFuture<Void>>();

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
		final BundleContext bundleContext = bundle.getBundleContext();

		final ServiceTracker<IScenarioService, IScenarioService> tracker = new ServiceTracker<>(bundleContext, IScenarioService.class, new Tracker(bundleContext));
		tracker.open();
	}

	public @NonNull ModelRecord getModelRecord(@NonNull final ScenarioInstance instance) {
		return get(instance);
	}

	public @NonNull ModelRecord getModelRecord(@NonNull final ScenarioInstance instance, final BiFunction<ModelRecord, IProgressMonitor, InstanceData> loadFunction) {
		if (lookup.containsKey(instance)) {
			throw new IllegalStateException("Instance already in manager");
		}
		final ModelRecord record = new ModelRecord(instance, loadFunction);
		lookup.put(instance, record);
		return record;
	}

	protected @NonNull ModelRecord get(@NonNull final ScenarioInstance instance) {
		final boolean[] runPostLoadHook = new boolean[1];
		final ModelRecord modelRecord = lookup.computeIfAbsent(instance, (_key) -> {
			return new ModelRecord(instance, (r, m) -> {
				try {
					final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(instance);
					InstanceData data = scenarioService.load(_key, m);
					runPostLoadHook[0] = true;
					return data;
				} catch (final Exception e) {
					r.setLoadFailure(e);
					return null;
				}
			});
		});
		// We have this slight faff to ensure this gets call after the lookup map has been updated. Otherwise there is a race condition where the load callback could be called before the lookup map is
		// populated.
		if (runPostLoadHook[0]) {
			CompletableFuture.runAsync(() -> runPostChangeHooks(modelRecord, PostChangeType.LOAD), postChangeExecutor);
		}
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

	private static BiConsumer<@NonNull ModelRecord, @NonNull Collection<@NonNull IPostChangeHook>> runHooks = (modelRecord, postChangeHooks) -> {
		for (final IPostChangeHook hook : postChangeHooks) {
			// Safe loop
			try {
				hook.changed(modelRecord);
			} catch (final Exception e) {
				LOG.error("Error in post change hook", e);
			}
		}
	};

	public void runPostChangeHooks(@NonNull final ModelRecord modelRecord, @NonNull final PostChangeType changeType) {

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
