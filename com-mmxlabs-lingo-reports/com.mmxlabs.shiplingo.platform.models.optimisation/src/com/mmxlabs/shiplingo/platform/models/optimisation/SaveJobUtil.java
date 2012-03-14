/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.models.manifest.ManifestJointModel;

/**
 * Utility class to help save Scenarios.
 * 
 * @author Simon Goodall
 * 
 * @noinstantiate This class is not intended to be instantiated.
 * 
 */
public final class SaveJobUtil {

	private static final Logger log = LoggerFactory.getLogger(SaveJobUtil.class); 
	
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
	public static IPath saveLNGSchedulerJob(final LNGSchedulerJobDescriptor job, final LNGSchedulerJobControl control, final String fileExt, final IResource resource) {
		final MMXRootObject scenario = control.getJobOutput();
		return saveRootObject(scenario, fileExt, resource);
	}

	public static IPath saveRootObject(final MMXRootObject root, final String ext, final IResource resource) {
		final IPath newFile = openSaveAsDialog(null, ext, resource);
		if (newFile == null) {
			return null;
		}
		final MMXRootObject duplicate = EcoreUtil.copy(root);
		try {
			final ManifestJointModel jm = new ManifestJointModel(duplicate, URI.createPlatformResourceURI(newFile.toString(), true));
			jm.consolidate();
			jm.save();
			return newFile;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return null;
		}
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
