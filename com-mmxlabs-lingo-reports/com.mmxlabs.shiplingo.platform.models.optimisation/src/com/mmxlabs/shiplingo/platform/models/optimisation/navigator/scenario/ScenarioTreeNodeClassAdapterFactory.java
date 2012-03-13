/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.scenario;

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
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.jointmodel.JointModel;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;

/**
 * An {@link IAdapterFactory} used to get {@link Schedule} instances from a {@link Scenario} or {@link IResource}. This should be registered in the plugin.xml for this plugin.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioTreeNodeClassAdapterFactory implements IAdapterFactory, IResourceChangeListener, IResourceDeltaVisitor {

	// global cache of load model map resources
	private final static WeakHashMap<IResource, JointModel> modelMap = new WeakHashMap<IResource, JointModel>();

	// FIXME: Get this string from somewhere else
	public ScenarioTreeNodeClassAdapterFactory() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	public Object getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class adapterType) {

		if (adaptableObject instanceof IResource) {

			/**
			 * Try obtaining in memory data from a running job before falling back to loading the scenario from the resource. This allows the current optimisation state to be shown.
			 */
			final IJobDescriptor job = Activator.getDefault().getJobManager().findJobForResource(adaptableObject);

			// if (job != null) {
			// FIXME
			// final Scenario scenario = ((LNGSchedulerJob) job).getScenario();
			// if (scenario != null) {
			// if (Scenario.class.isAssignableFrom(adapterType)) {
			// return scenario;
			// } else if (Schedule.class.isAssignableFrom(adapterType)) {
			// return getSchedule(scenario);
			// }
			// }
			// }

			// Fall back to directly loading from resource
			if (MMXRootObject.class.isAssignableFrom(adapterType)) {
				final MMXRootObject scenario = getScenario((IResource) adaptableObject);
				return scenario;
			} else if (Schedule.class.isAssignableFrom(adapterType)) {
				final MMXRootObject scenario = getScenario((IResource) adaptableObject);
				return getSchedule(scenario);
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

	private MMXRootObject getScenario(final IResource resource) {
		// final Object[] scenarioObjects = scp.getChildren(resource);
		// if (scenarioObjects != null) {
		// for (final Object o : scenarioObjects) {
		// if (o instanceof MMXRootObject) {
		// final MMXRootObject scenario = (MMXRootObject) o;
		// return scenario;
		// }
		// }
		// }

		if (modelMap.containsKey(resource)) {
			return modelMap.get(resource).getRootObject();
		} else {
			synchronized (resource) {
				// Re-check in case we've just reloaded
				if (modelMap.containsKey(resource)) {
					return modelMap.get(resource).getRootObject();
				}
				JointModel jointModel = (JointModel) resource.getAdapter(JointModel.class);
				if (jointModel == null) {
					jointModel = (JointModel) Platform.getAdapterManager().loadAdapter(resource, JointModel.class.getCanonicalName());
				}
				if (jointModel != null) {
					modelMap.put(resource, jointModel);
					return jointModel.getRootObject();
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
			modelMap.remove(changedResource);
			return false;
		}
		return true;
	}
}
