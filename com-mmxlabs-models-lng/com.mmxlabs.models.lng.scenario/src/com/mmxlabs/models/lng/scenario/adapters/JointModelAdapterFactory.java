/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.adapters;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorInput;

import com.mmxlabs.models.lng.scenario.ManifestJointModel;
import com.mmxlabs.models.mmxcore.jointmodel.JointModel;

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
