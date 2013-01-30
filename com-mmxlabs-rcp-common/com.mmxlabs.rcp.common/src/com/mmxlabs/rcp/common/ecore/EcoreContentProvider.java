/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.ecore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

import com.mmxlabs.rcp.common.internal.Activator;

/**
 * A subclass of {@link AdapterFactoryContentProvider} which caches EMF model instances. This class implements a {@link IResourceChangeListener} to automaticallty reload instances on filesystem
 * changes.
 * 
 * @author Simon Goodall
 * 
 */
public class EcoreContentProvider extends AdapterFactoryContentProvider implements IResourceChangeListener, IResourceDeltaVisitor {

	/**
	 * Shared resource set impl between all instances of {@link EcoreContentProvider}. FIXME: Should this really be static? - As the resource listener is not - will we reload the model multiple times?
	 */
	private static ResourceSetImpl resourceSet = new ResourceSetImpl();
	static {
		resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
		resourceSet.getLoadOptions().put(XMLResource.OPTION_RESOURCE_HANDLER, new XMLResource.ResourceHandler() {
			@Override
			public void preLoad(final XMLResource resource, final InputStream inputStream, final Map<?, ?> options) {
				if (resource instanceof ResourceImpl) {
					((ResourceImpl) resource).setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
				}
			}

			@Override
			public void postLoad(final XMLResource resource, final InputStream inputStream, final Map<?, ?> options) {
				if (resource instanceof ResourceImpl) {
					((ResourceImpl) resource).setIntrinsicIDToEObjectMap(null);
				}
			}

			@Override
			public void preSave(final XMLResource resource, final OutputStream outputStream, final Map<?, ?> options) {
			}

			@Override
			public void postSave(final XMLResource resource, final OutputStream outputStream, final Map<?, ?> options) {
			}
		});
	}
	private final Collection<String> fileExtensions;

	public EcoreContentProvider(final Collection<String> fileExtensions) {
		super(EcoreComposedAdapterFactory.getAdapterFactory());

		this.fileExtensions = fileExtensions;

		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IFile) {
			final IFile file = (IFile) parentElement;
			final String fileExtension = file.getFileExtension();
			if ((fileExtension != null) && fileExtensions.contains(fileExtension)) {
				final String path = file.getFullPath().toString();
				final URI uri = URI.createPlatformResourceURI(path, true);
				parentElement = resourceSet.getResource(uri, true);
			}
		}
		return super.getChildren(parentElement);
	}

	@Override
	public Object getParent(final Object element) {
		if (element instanceof IFile) {
			return ((IResource) element).getParent();
		}
		return super.getParent(element);
	}

	@Override
	public boolean hasChildren(final Object element) {
		if (element instanceof IFile) {
			return true;
		}
		return super.hasChildren(element);
	}

	@Override
	public Object[] getElements(final Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {

		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);

		super.dispose();
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		try {
			final IResourceDelta delta = event.getDelta();
			delta.accept(this);
		} catch (final CoreException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}

	@Override
	public boolean visit(final IResourceDelta delta) throws CoreException {
		final IResource changedResource = delta.getResource();

		// Get resource file extension
		final String fileExtension = changedResource.getFileExtension();

		// Make sure resource is a File and has a requested file extension
		if ((changedResource.getType() == IResource.FILE) && fileExtensions.contains(fileExtension)) {
			try {
				// Get a URI to the resource
				final String path = ((IFile) changedResource).getFullPath().toString();
				final URI uri = URI.createPlatformResourceURI(path, true);

				// Get existing Resource impl
				final Resource res = resourceSet.getResource(uri, false);
				if (res != null) {

					// Unload
					res.unload();

					// Only load resource if not removed
					if ((delta.getKind() & IResourceDelta.REMOVED) == 0) {
						res.load(resourceSet.getLoadOptions());
					}
				}
			} catch (final IOException ie) {
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error reloading resource", ie));
			}
			return false;
		}
		return true;
	}
}
