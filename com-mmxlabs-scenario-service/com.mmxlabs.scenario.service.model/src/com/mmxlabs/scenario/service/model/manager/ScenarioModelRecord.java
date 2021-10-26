/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ecore.SafeAdapterImpl;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.model.manager.SSDataManager.PostChangeType;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil.EncryptedScenarioException;
import com.mmxlabs.scenario.service.model.util.MMXAdaptersAwareCommandStack;
import com.mmxlabs.scenario.service.model.util.ResourceHelper;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;

public final class ScenarioModelRecord extends ModelRecord {

	private static final Logger LOG = LoggerFactory.getLogger(ScenarioModelRecord.class);

	private final @NonNull Manifest manifest;

	private String name;

	private @NonNull IStatus validationStatus = Status.OK_STATUS;

	private final @NonNull ConcurrentLinkedQueue<com.mmxlabs.scenario.service.model.manager.IScenarioValidationListener> validationListeners = new ConcurrentLinkedQueue<>();

	private final Map<ISharedDataModelType<?>, ModelRecord> extraDataRecords = new HashMap<>();

	private @Nullable ScenarioInstance scenarioInstance;

	public ScenarioModelRecord(@NonNull final Manifest manifest, final @NonNull BiFunction<ModelRecord, IProgressMonitor, InstanceData> loadFunction) {
		super(loadFunction);
		this.manifest = manifest;
	}

	public void setValidationStatus(final @NonNull IStatus status) {
		this.validationStatus = status;
		if (scenarioInstance != null) {
			scenarioInstance.setValidationStatusCode(status.getSeverity());
		}
		fireValidationStatusChanged(status);
	}

	public int getValidationStatusSeverity() {
		return validationStatus.getSeverity();
	}

	public @NonNull IStatus getValidationStatus() {
		return validationStatus;
	}

	private void fireValidationStatusChanged(final @NonNull IStatus status) {

		for (final IScenarioValidationListener l : validationListeners) {
			// Safe loop
			try {
				l.validationChanged(this, status);
			} catch (final Exception e) {
				LOG.error("Error in validation listener", e);
			}
		}
	}

	public void addValidationListener(@NonNull final IScenarioValidationListener l) {
		validationListeners.add(l);
	}

	public void removeValidationListener(@NonNull final IScenarioValidationListener l) {
		validationListeners.remove(l);
	}

	@Override
	public void dispose() {
		super.dispose();
		// Right now we are asserting that we have been cleaned-up properly
		if (!validationListeners.isEmpty()) {
			// The ValidatingDecorator is often still attached.
			// SG: Not sure how to remove listener, except with a service listener?
			int ii = 0;
		}
		// assert validationListeners.isEmpty();
	}

	public Manifest getManifest() {
		return manifest;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void saveCopyTo(final String uuid, final URI archiveURI) throws Exception {
		// If not loaded, try quick byte stream copy
		if (!isLoaded()) {
			if (scenarioInstance != null && scenarioInstance.getRootObjectURI() != null) {
				final URI sourceURI = URI.createURI(scenarioInstance.getRootObjectURI());
				ScenarioServiceUtils.copyURIData(new ExtensibleURIConverterImpl(), sourceURI, archiveURI);
				return;
			}
		}
		try (ModelReference ref = aquireReference("ModelRecord:saveAsCopy")) {
			final EObject rootObject = EcoreUtil.copy(ref.getInstance());
			ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
				final Map<String, EObject> extraDataObjects = ScenarioStorageUtil.createCopyOfExtraData(this);
				ScenarioStorageUtil.storeToURI(uuid, rootObject, extraDataObjects, manifest, archiveURI, scenarioCipherProvider);
			});
		}
	}

	public @Nullable ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	private final @NonNull AdapterImpl readOnlyAdapter = new SafeAdapterImpl() {
		public void safeNotifyChanged(final org.eclipse.emf.common.notify.Notification msg) {
			if (msg.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_Readonly()) {
				setReadOnly(msg.getNewBooleanValue());
			}
		};
	};

	public void setScenarioInstance(final ScenarioInstance scenarioInstance) {
		if (this.scenarioInstance != null) {
			this.scenarioInstance.eAdapters().remove(readOnlyAdapter);
		}

		this.scenarioInstance = scenarioInstance;

		if (scenarioInstance != null) {
			this.validationStatus = new Status(scenarioInstance.getValidationStatusCode(), "com.mmxlabs.scenario.service.model", "(Open scenario to refresh validation status)");
			scenarioInstance.eAdapters().add(readOnlyAdapter);
			setReadOnly(scenarioInstance.isReadonly());
		}
	}

	public void addExtraDataRecord(final ISharedDataModelType<?> key, final ModelRecord record) {
		this.extraDataRecords.put(key, record);
	}

	public @NonNull ModelRecord getExtraDataRecord(final ISharedDataModelType<?> key) {
		return extraDataRecords.get(key);
	}

	@Override
	protected void onPostUnload() {
		if (scenarioInstance != null) {
			this.validationStatus = new Status(scenarioInstance.getValidationStatusCode(), "com.mmxlabs.scenario.service.model", "(Open scenario to refresh validation status)");
		} else {
			this.validationStatus = Status.OK_STATUS;
		}
	}

	@Override
	public void runPostChangeHooks(final PostChangeType type) {
		// Hacky way to see if this record is part of the main workspace
		if (scenarioInstance != null) {
			SSDataManager.Instance.runPostChangeHooks(this, type);
		}
	}

	public void revert() {
		// Wait a little for reference to get closed
		for (int i = 0; i < 100; ++i) {
			System.gc();
			if (referencesList.isEmpty()) {
				break;
			} else {
				try {
					Thread.sleep(100);
				} catch (final InterruptedException e) {
				}
			}
		}
		dumpReferences();

		try {
			referenceLock.lock();
			referenceCount = 0;
			if (referenceCount == 0) {
				// Release strong reference
				if (data != null) {
					for (final IScenarioLockListener l : lockListeners) {
						data.getLock().removeLockListener(l);
					}
					for (final IScenarioDirtyListener l : dirtyListeners) {
						l.dirtyStatusChanged(this, false);
						data.removeDirtyListener(l);
					}
					SSDataManager.Instance.runPostChangeHooks(this, PostChangeType.UNLOAD);
					sharedReference.close();
					sharedReference = null;
					data.close();
				}
				data = null;
				// Reset validation status
				validationStatus = Status.OK_STATUS;
			}
			cleanupReferencesList();
		} finally {
			referenceLock.unlock();
		}
	}

	public boolean hasExtraDataRecord(final ISharedDataModelType<?> artifactKey) {
		return extraDataRecords.containsKey(artifactKey);
	}

	/**
	 * Create a new {@link IScenarioDataProvider} from this {@link ScenarioModelRecord}. This will obtain a {@link ModelReference} and load the model if required. The returned
	 * {@link IScenarioDataProvider} should be disposed to release the model reference.
	 * 
	 * @param referenceID
	 * @return
	 */
	public @NonNull IScenarioDataProvider aquireScenarioDataProvider(final String referenceID) {
		return new ModelRecordScenarioDataProvider(this, referenceID, new NullProgressMonitor());
	}

	public @NonNull IScenarioDataProvider aquireScenarioDataProvider(final String referenceID, IProgressMonitor progressMonitor) {
		return new ModelRecordScenarioDataProvider(this, referenceID, progressMonitor);
	}

	/**
	 * Execute some code within the context of a {@link ModelReference}
	 */
	public void executeWithProvider(final @NonNull Consumer<IScenarioDataProvider> hook) {
		try (IScenarioDataProvider ref = aquireScenarioDataProvider("ScenarioModelRecord:executeWithProvider")) {
			hook.accept(ref);
		}
	}

	public static ScenarioModelRecord forException(Exception e) {

		BiFunction<ModelRecord, IProgressMonitor, InstanceData> loadFunc = (record, monitor) -> {
			record.setLoadFailure(e);
			return null;
		};
		Manifest m = ManifestFactory.eINSTANCE.createManifest();

		ScenarioModelRecord record = new ScenarioModelRecord(m, loadFunc);

		// TODO Auto-generated method stub
		return record;
	}

}