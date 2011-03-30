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

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IFile) {
			final IFile file = (IFile) parentElement;
			if (file.getFileExtension().equals("scenario")) {
				final String path = file.getFullPath().toString();
				final URI uri = URI.createPlatformResourceURI(path, true);
				parentElement = resourceSet.getResource(uri, true);
			}
		}
		return super.getChildren(parentElement);
	}

	@Override
	public Object getParent(final Object element) {
		if (element instanceof IFile)
			return ((IResource) element).getParent();
		return super.getParent(element);
	}

	@Override
	public boolean hasChildren(final Object element) {
		if (element instanceof IFile)
			return true;
		return super.hasChildren(element);
	}

	@Override
	public Object[] getElements(final Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {
		super.dispose();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		try {
			final IResourceDelta delta = event.getDelta();
			delta.accept(this);
		} catch (final CoreException e) {
			System.out.println("Resource Changed Fail - " + e.toString());
		}
	}

	@Override
	public boolean visit(final IResourceDelta delta) throws CoreException {
		final IResource changedResource = delta.getResource();
		if (changedResource.getType() == IResource.FILE
				&& changedResource.getFileExtension().equals("scenario")) {
			try {
				final String path = ((IFile) changedResource).getFullPath()
						.toString();
				final URI uri = URI.createPlatformResourceURI(path, true);
				final Resource res = resourceSet.getResource(uri, false);
				if (res != null) {
					res.unload();
					// Only load resource if not removed
					if ((delta.getKind() & IResourceDelta.REMOVED) == 0) {
						res.load(resourceSet.getLoadOptions());
					}
				}
			} catch (final IOException ie) {
				System.err.println("Error reloading resource - "
						+ ie.toString());
			}
			return false;
		}
		return true;
	}

}
