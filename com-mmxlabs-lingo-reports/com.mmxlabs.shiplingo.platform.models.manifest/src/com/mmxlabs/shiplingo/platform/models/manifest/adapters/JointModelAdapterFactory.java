/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.adapters;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorInput;

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
				final IResource resource = (IResource) ((IEditorInput) adaptableObject).getAdapter(IResource.class);
				JointModel jm = (JointModel) resource.getAdapter(JointModel.class);
				if (jm == null) {
					jm = (JointModel) Platform.getAdapterManager().loadAdapter(resource, JointModel.class.getCanonicalName());
				}
				return jm;
			}
		} 
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {JointModel.class};
	}

}
