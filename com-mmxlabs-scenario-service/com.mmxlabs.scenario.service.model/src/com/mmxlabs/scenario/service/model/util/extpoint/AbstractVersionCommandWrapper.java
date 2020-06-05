/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.extpoint;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.hub.services.users.UsernameProvider;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.VersionRecord;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.manifest.StorageType;

/**
 * @author Simon Goodall
 * 
 */
public abstract class AbstractVersionCommandWrapper implements IWrappedCommandProvider {

	protected EditingDomain editingDomain;
	protected final boolean[] changedRef = new boolean[1];
	protected final @NonNull Adapter adapter = createAdapter(changedRef);
	protected EObject modelRoot;
	protected ModelArtifact modelArtifact;

	protected final String typeID;
	protected final EReference versionRecordFeature;

	protected AbstractVersionCommandWrapper(String typeID, EReference versionRecordFeature) {
		this.typeID = typeID;
		this.versionRecordFeature = versionRecordFeature;

	}

	@Override
	public @Nullable Command provideCommand(@NonNull final Command cmd, @NonNull final EditingDomain editingDomain) {

		final CompoundCommand wrapped = new CompoundCommand(cmd.getLabel());

		wrapped.append(cmd);
		wrapped.append(createVersionCommand());

		return wrapped;

	}

	private Command createVersionCommand() {

		return new CompoundCommand() {

			@Override
			public boolean canExecute() {
				return true;
			}

			@Override
			protected boolean prepare() {
				return true;
			}

			@Override
			public void execute() {
				if (changedRef[0]) {
					String newID = UUID.randomUUID().toString();
					VersionRecord record = MMXCoreFactory.eINSTANCE.createVersionRecord();
					record.setVersion(newID);
					record.setCreatedBy(UsernameProvider.INSTANCE.getUserID());
					record.setCreatedAt(Instant.now());
					final Command cmd = SetCommand.create(editingDomain, modelRoot, versionRecordFeature, record);
					appendAndExecute(cmd);
					changedRef[0] = false;
				}
			}
		};
	}

	protected abstract @NonNull Adapter createAdapter(final boolean[] changedRef);

	protected abstract @Nullable EObject getModelRoot(final EObject rootModel);

	@Override
	public void registerEditingDomain(final Manifest manifest, final EditingDomain editingDomain) {
		if (this.editingDomain != null) {
			throw new IllegalStateException("Editing domain already registered");
		}

		this.editingDomain = editingDomain;

		if (this.editingDomain != null) {
			for (final Resource r : this.editingDomain.getResourceSet().getResources()) {
				for (final EObject obj : r.getContents()) {
					modelRoot = getModelRoot(obj);
					if (modelRoot != null) {
						break;
					}
				}
			}
		}
		modelArtifact = null;
		if (modelRoot != null) {
			modelRoot.eAdapters().add(adapter);
			VersionRecord version = (VersionRecord) modelRoot.eGet(versionRecordFeature);
			if (version != null && manifest != null) {
				for (final ModelArtifact artifact : manifest.getModelDependencies()) {
					if (typeID.equals(artifact.getKey())) {
						if (artifact.getStorageType() == StorageType.INTERNAL) {
							this.modelArtifact = artifact;
							// Update version if needed.
							if (!Objects.equals(this.modelArtifact.getDataVersion(), version.getVersion())) {
								this.modelArtifact.setDataVersion(version.getVersion());
							}
							break;
						}
					}
				}
				if (modelArtifact == null) {
					modelArtifact = ManifestFactory.eINSTANCE.createModelArtifact();
					modelArtifact.setDataVersion(version.getVersion());
					modelArtifact.setStorageType(StorageType.INTERNAL);
					modelArtifact.setType("EOBJECT");
					modelArtifact.setKey(typeID);
					manifest.getModelDependencies().add(modelArtifact);
				}
			}
		}
	}

	@Override
	public void deregisterEditingDomain(final Manifest manifest, final EditingDomain editingDomain) {
		if (this.editingDomain != editingDomain) {
			throw new IllegalStateException("A different editing domain has been registered");
		}

		if (modelRoot != null) {
			modelRoot.eAdapters().remove(adapter);
			modelRoot = null;
		}

		modelArtifact = null;

		this.editingDomain = null;
	}

}
