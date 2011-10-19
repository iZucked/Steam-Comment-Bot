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
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.SaveAsDialog;

import scenario.Scenario;
import scenario.schedule.Schedule;

/**
 * Utility class to help save Scenarios.
 * 
 * @author Simon Goodall
 * 
 * @noinstantiate This class is not intended to be instantiated.
 * 
 */
public final class SaveJobUtil {

	/**
	 * No expected to be instantiated.
	 */
	private SaveJobUtil() {

	}

	/**
	 * Save the current {@link Schedule} & {@link Scenario} from the optimisation job into a new resource determined by the user.
	 * 
	 * @param job
	 * @param resource
	 *            Existing resource to base UI on
	 * @return New resource {@link IPath}.
	 */
	public static IPath saveLNGSchedulerJob(final LNGSchedulerJobDescriptor job, LNGSchedulerJobControl control, final String fileExt, final IResource resource) {

		final IPath newFile = openSaveAsDialog(null, fileExt, resource);
		if (newFile == null) {
			return null;
		}

		// Take copy of scenario
		// this was wrong
		// final Scenario scenario = EcoreUtil.copy((job).getJobContext());
		final Scenario scenario = control.getJobOutput().scenario;

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

	/**
	 * Opens a "Save As" dialog prompting the user for a new file location. A desired file extension can be provided which will be appended to the filename if missing. An {@link IResource} instance
	 * can also be provided as the original file to point the UI at. Returns a new {@link IPath} to the specified file.
	 * 
	 * @param fileExtension
	 * @param resource
	 * @return
	 */
	public static IPath openSaveAsDialog(final String message, final String fileExtension, final IResource resource) {
		// Generate a prompt to ask the user for the new resource string
		final SaveAsDialog dialog = new SaveAsDialog(Display.getDefault().getActiveShell());
		dialog.setBlockOnOpen(true);
		if (resource instanceof IFile) {
			dialog.setOriginalFile((IFile) resource);
		}

		// Need to create the dialog before setting strings....
		dialog.create();
		dialog.setTitle("Save As...");

		if (message != null) {
			dialog.setMessage(message);
		}

		if (dialog.open() != Window.OK) {
			// Cancelled
			return null;
		}

		IPath newFile = dialog.getResult();
		// Check required fileExt is present and append if required.
		if (fileExtension != null) {
			if (fileExtension.equals(newFile.getFileExtension()) == false) {
				newFile = newFile.addFileExtension(fileExtension);
			}
		}
		return newFile;
	}
}
