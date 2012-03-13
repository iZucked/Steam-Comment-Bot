/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.adapters;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IURIEditorInput;

import com.mmxlabs.models.mmxcore.jointmodel.JointModel;
import com.mmxlabs.shiplingo.platform.models.manifest.ManifestJointModel;

/**
 * Adapts IEditorInputs to DemoJointModels where possible.
 * 
 * @author hinton
 *
 */
public class JointModelAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof IEditorInput) {
			if (adapterType.isAssignableFrom(ManifestJointModel.class)) {
				final IEditorInput input = (IEditorInput) adaptableObject;
				final IURIEditorInput uriInput = (IURIEditorInput) input.getAdapter(IURIEditorInput.class);
				if (uriInput != null) {
					try {
						return new ManifestJointModel(URI.createURI(uriInput.getURI().toString()));
					} catch (FileNotFoundException e) {
						return null;
					} catch (IOException e) {
						return null;
					} catch (MigrationException e) {
						return null; 
					}
				} else {
					return null;
				}
			}
		} else if (adaptableObject instanceof IResource) {
			try {
				return new ManifestJointModel(URI.createPlatformResourceURI(((IResource) adaptableObject).getFullPath().toString(), true));
			} catch (FileNotFoundException e) {
				return null;
			} catch (IOException e) {
				return null;
			} catch (MigrationException e) {
				return null;
			}

		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {JointModel.class};
	}

}
