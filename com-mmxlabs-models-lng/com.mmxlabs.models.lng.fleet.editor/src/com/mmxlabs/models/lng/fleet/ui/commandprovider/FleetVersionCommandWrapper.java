/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.commandprovider;

import java.util.Iterator;
import java.util.Objects;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.manifest.StorageType;
import com.mmxlabs.scenario.service.model.util.extpoint.IWrappedCommandProvider;

/**
 * @author Simon Goodall
 * 
 */
public class FleetVersionCommandWrapper implements IWrappedCommandProvider {

	private EditingDomain editingDomain;
	private final boolean[] changedRef = new boolean[1];
	private final @NonNull Adapter adapter = createAdapter(changedRef);
	private FleetModel fleetModel;
	private ModelArtifact modelArtifact;

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

			public void execute() {
				if (changedRef[0]) {
					String newID = EcoreUtil.generateUUID();
					System.out.println("Generate Fleet Model Version ID " + newID);
					final Command cmd = SetCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__FLEET_DATA_VERSION, newID);
					appendAndExecute(cmd);
				}
			}
		};
	}

	private @NonNull Adapter createAdapter(final boolean[] changedRef) {

		return new EContentAdapter() {

			@Override
			public void notifyChanged(final Notification notification) {
				super.notifyChanged(notification);
				if (notification.getNotifier() instanceof Vessel) {
					changedRef[0] = true;
				} else if (notification.getNotifier() instanceof VesselStateAttributes) {
					changedRef[0] = true;
				} else if (notification.getNotifier() instanceof VesselClassRouteParameters) {
					changedRef[0] = true;
				} else if (notification.getNotifier() instanceof BaseFuel) {
					changedRef[0] = true;
				} else if (notification.getFeature() == FleetPackage.Literals.FLEET_MODEL__VESSELS) {
					changedRef[0] = true;
				} else if (notification.getFeature() == FleetPackage.Literals.FLEET_MODEL__BASE_FUELS) {
					changedRef[0] = true;
				}

				// Reset!
				if (notification.getFeature() == FleetPackage.Literals.FLEET_MODEL__FLEET_DATA_VERSION) {

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
				if (target instanceof FleetModel) {
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						if (notifier instanceof Vessel //
								|| notifier instanceof BaseFuel //
						) {
							addAdapter(notifier);
						}
					}
				} else if (target instanceof Vessel || target instanceof BaseFuel || target instanceof VesselStateAttributes || target instanceof VesselClassRouteParameters) {
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
				if (target instanceof FleetModel) {
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						if (notifier instanceof Vessel //
								|| notifier instanceof BaseFuel //
						) {
							removeAdapter(notifier, false, true);
						}
					}
				} else if (target instanceof Vessel || target instanceof BaseFuel || target instanceof VesselStateAttributes || target instanceof VesselClassRouteParameters) {
					for (final Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
						final Notifier notifier = i.next();
						removeAdapter(notifier, false, true);
					}
				}
			}
		};
	}

	@Override
	public void registerEditingDomain(final Manifest manifest, final EditingDomain editingDomain) {
		if (this.editingDomain != null) {
			throw new IllegalStateException("Editing domain already registered");
		}

		this.editingDomain = editingDomain;

		if (this.editingDomain != null) {
			for (final Resource r : this.editingDomain.getResourceSet().getResources()) {
				for (final EObject obj : r.getContents()) {
					if (obj instanceof LNGScenarioModel) {
						final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) obj;
						fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);
					}
				}
			}
		}
		modelArtifact = null;
		if (fleetModel != null) {
			fleetModel.eAdapters().add(adapter);
			if (manifest != null) {
				for (final ModelArtifact artifact : manifest.getModelDependencies()) {
					if (LNGScenarioSharedModelTypes.FLEET.getID().equals(artifact.getKey())) {
						if (artifact.getStorageType() == StorageType.INTERNAL) {
							this.modelArtifact = artifact;
							// Update version if needed.
							if (!Objects.equals(this.modelArtifact.getDataVersion(), fleetModel.getFleetDataVersion())) {
								this.modelArtifact.setDataVersion(fleetModel.getFleetDataVersion());
							}
							break;
						}
					}
				}
				if (modelArtifact == null) {
					modelArtifact = ManifestFactory.eINSTANCE.createModelArtifact();
					modelArtifact.setDataVersion(fleetModel.getFleetDataVersion());
					modelArtifact.setStorageType(StorageType.INTERNAL);
					modelArtifact.setType("EOBJECT");
					modelArtifact.setKey(LNGScenarioSharedModelTypes.FLEET.getID());
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

		if (fleetModel != null) {
			fleetModel.eAdapters().remove(adapter);
			fleetModel = null;
		}

		modelArtifact = null;

		this.editingDomain = null;
	}

}
