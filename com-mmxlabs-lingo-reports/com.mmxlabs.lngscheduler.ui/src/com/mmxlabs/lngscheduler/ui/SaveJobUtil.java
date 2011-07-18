package com.mmxlabs.lngscheduler.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.SaveAsDialog;

import scenario.Scenario;
import scenario.schedule.Schedule;

public class SaveJobUtil {

	/**
	 * Save the current {@link Schedule} & {@link Scenario} from the optimisation job into a new resource determined by the user.
	 * 
	 * @param job
	 * @param resource Existing resource to base UI on
	 * @return New resource {@link IPath}.
	 */
	public static IPath saveLNGSchedulerJob(final LNGSchedulerJob job, final IResource resource) {

		// Generate a prompt to ask the user for the new resource string
		final SaveAsDialog dialog = new SaveAsDialog(Display.getDefault().getActiveShell());
		dialog.setBlockOnOpen(true);
		if (resource instanceof IFile) {
			dialog.setOriginalFile((IFile) resource);
		}
		// Need to create the dialog before setting strings....
		dialog.create();
		dialog.setTitle("Save As...");

		if (dialog.open() != Window.OK) {
			return null;
		}

		IPath newFile = dialog.getResult();
		// FIXME: Do not hardcode extension
		if ("scenario".equals(newFile.getFileExtension()) == false) {
			newFile = newFile.addFileExtension("scenario");
		}

		// Take copy of scenario
		final Scenario scenario = EcoreUtil.copy((job).getScenario());

		// Process scenario - prune out intermediate schedules ....
		int numSchedules = scenario.getScheduleModel().getSchedules().size();
		while (numSchedules > 1) {
			scenario.getScheduleModel().getSchedules().remove(0);
			--numSchedules;
		}
		// .. and set remaining schedule to the new initial state
		if (numSchedules == 1) {
			scenario.getOptimisation().getCurrentSettings().setInitialSchedule(scenario.getScheduleModel().getSchedules().get(0));
		} else {
			// TODO: Necessary?
			// scenario.getOptimisation().getCurrentSettings().setInitialSchedule(null);
		}

		// Create resource set to save into
		final ResourceSetImpl resourceSet = new ResourceSetImpl();

		final URI uri = URI.createPlatformResourceURI(newFile.toString(), true);

		final Resource nResource = resourceSet.createResource(uri);

		// Add copied scenario to this resource
		nResource.getContents().add(scenario);

		final Map<String, String> options = new HashMap<String, String>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		try {
			nResource.save(options);
			return newFile;
		} catch (final IOException e) {
			Activator.error(e.getMessage(), e);
		}
		return null;

	}
}
