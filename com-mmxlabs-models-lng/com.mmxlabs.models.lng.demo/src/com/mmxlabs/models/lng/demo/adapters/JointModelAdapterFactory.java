package com.mmxlabs.models.lng.demo.adapters;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IURIEditorInput;

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
				final IURIEditorInput uriInput = (IURIEditorInput) input.getAdapter(IURIEditorInput.class);
				if (uriInput != null) {
					try {
						return new DemoJointModel(URI.createURI(uriInput.getURI().toString()));
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
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {DemoJointModel.class};
	}

}
