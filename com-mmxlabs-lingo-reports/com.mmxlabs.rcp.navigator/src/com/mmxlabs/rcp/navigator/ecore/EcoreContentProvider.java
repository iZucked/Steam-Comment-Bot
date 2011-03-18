package com.mmxlabs.rcp.navigator.ecore;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

public class EcoreContentProvider extends AdapterFactoryContentProvider
		implements IResourceChangeListener, IResourceDeltaVisitor {
	private static ResourceSetImpl resourceSet = new ResourceSetImpl();

	public EcoreContentProvider() {
		super(EcoreComposedAdapterFactory.getAdapterFactory());
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
				IResourceChangeEvent.POST_CHANGE);
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IFile) {
			final String path = ((IFile) parentElement).getFullPath()
					.toString();
			final URI uri = URI.createPlatformResourceURI(path, true);
			parentElement = resourceSet.getResource(uri, true);
		}
		return super.getChildren(parentElement);
	}

	public Object getParent(final Object element) {
		if (element instanceof IFile)
			return ((IResource) element).getParent();
		return super.getParent(element);
	}

	public boolean hasChildren(final Object element) {
		if (element instanceof IFile)
			return true;
		return super.hasChildren(element);
	}

	public Object[] getElements(final Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {
		super.dispose();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	public void resourceChanged(final IResourceChangeEvent event) {
		try {
			final IResourceDelta delta = event.getDelta();
			delta.accept(this);
		} catch (final CoreException e) {
			System.out.println("Resource Changed Fail - " + e.toString());
		}
	}

	public boolean visit(final IResourceDelta delta) throws CoreException {
		final IResource changedResource = delta.getResource();
		if (changedResource.getType() == IResource.FILE
				&& changedResource.getFileExtension().equals("scenario")) {
			try {
				final String path = ((IFile) changedResource).getFullPath()
						.toString();
				final URI uri = URI.createPlatformResourceURI(path, true);
				final Resource res = resourceSet.getResource(uri, true);
				res.unload();
				res.load(resourceSet.getLoadOptions());
			} catch (final IOException ie) {
				System.err.println("Error reloading resource - "
						+ ie.toString());
			}
			return false;
		}
		return true;
	}

}
