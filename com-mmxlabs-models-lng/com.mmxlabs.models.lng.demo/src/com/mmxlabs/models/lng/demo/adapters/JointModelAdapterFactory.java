package com.mmxlabs.models.lng.demo.adapters;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.ui.IEditorInput;

import com.mmxlabs.models.lng.demo.DemoJointModel;

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
			if (adapterType.isAssignableFrom(DemoJointModel.class)) {
				final IEditorInput input = (IEditorInput) adaptableObject;
				final IFile file = (IFile) input.getAdapter(IFile.class);
				if (file != null) {
					try {
						//TODO update demo joint model to work with workspace URIs rather than absolute paths?
						// Since it just uses resource sets we can use workspace URIs everywhere, hopefully.
						return new DemoJointModel(file.getLocation().toFile());
					} catch (FileNotFoundException e) {
						
					} catch (IOException e) {
						
					} catch (MigrationException e) {
						
					}
				}
			}
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {DemoJointModel.class};
	}

}
