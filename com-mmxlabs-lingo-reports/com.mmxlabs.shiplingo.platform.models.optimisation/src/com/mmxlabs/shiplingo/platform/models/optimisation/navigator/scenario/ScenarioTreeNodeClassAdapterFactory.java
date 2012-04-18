/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.scenario;

import java.io.IOException;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.jointmodel.JointModel;
import com.mmxlabs.shiplingo.platform.models.manifest.ManifestJointModel;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;

/**
 * An {@link IAdapterFactory} used to get {@link Schedule} instances from a {@link Scenario} or {@link IResource}. 
 * 
 * This should be registered in the plugin.xml for this plugin.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioTreeNodeClassAdapterFactory implements IAdapterFactory, IResourceChangeListener, IResourceDeltaVisitor {

	private static final Logger log = LoggerFactory.getLogger(ScenarioTreeNodeClassAdapterFactory.class);
	
	// global cache of load model map resources
	private final static WeakHashMap<IResource, JointModel> modelMap = new WeakHashMap<IResource, JointModel>();

	// FIXME: Get this string from somewhere else
	public ScenarioTreeNodeClassAdapterFactory() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
		log.debug("Constructed");
	}

	@Override
	public Object getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class adapterType) {

		if (adaptableObject instanceof IResource) {
			// Fall back to directly loading from resource
			final JointModel model = getJointModel((IResource) adaptableObject);
			
			if (MMXRootObject.class.isAssignableFrom(adapterType)) {
				return model.getRootObject();
			} else if (Schedule.class.isAssignableFrom(adapterType)) {
				return getSchedule(model.getRootObject());
			} else if (JointModel.class.isAssignableFrom(adapterType)) {
				return model;
			}
		}

		return null;
	}

	private Schedule getSchedule(final MMXRootObject scenario) {
		if (scenario != null) {
			final ScheduleModel scheduleModel = scenario.getSubModel(ScheduleModel.class);
			if (scheduleModel != null) {
				if (scheduleModel.getOptimisedSchedule() != null) {
					return scheduleModel.getOptimisedSchedule();
				} else {
					return scheduleModel.getInitialSchedule();
				}
			}
		}
		return null;
	}

	private JointModel getJointModel(final IResource resource) {
		if (modelMap.containsKey(resource)) {
			return modelMap.get(resource);
		} else {
			synchronized (modelMap) {
				// Re-check in case we've just reloaded
				if (modelMap.containsKey(resource)) {
					return modelMap.get(resource);
				}
				log.debug("Loading model from " + resource);
				log.debug("Already loaded " + modelMap.size() + " resources:");
				for (final IResource key : modelMap.keySet()) {
					log.debug("\t" + key);
				}
				JointModel jointModel;
				try {
					jointModel = new ManifestJointModel(URI.createPlatformResourceURI(resource.getFullPath().toString(), true));
					if (jointModel != null) {
						modelMap.put(resource, jointModel);
						log.debug("Storing loaded model");
						jointModel.getRootObject().setName(resource.getName());//hack
						return jointModel;
					}
				} catch (IOException e) {
					log.error("Error loading joint model", e);
				} catch (MigrationException e) {
					log.error("Error loading joint model", e);
				}
			}
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { MMXRootObject.class, IResource.class };
	}

	// @Override
	public void dispose() {

		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		try {
			final IResourceDelta delta = event.getDelta();
			delta.accept(this);
		} catch (final CoreException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}

	@Override
	public boolean visit(final IResourceDelta delta) throws CoreException {
		final IResource changedResource = delta.getResource();

		if (modelMap.containsKey(changedResource)) {
//			modelMap.remove(changedResource);
			return false;
		}
		return true;
	}
}
