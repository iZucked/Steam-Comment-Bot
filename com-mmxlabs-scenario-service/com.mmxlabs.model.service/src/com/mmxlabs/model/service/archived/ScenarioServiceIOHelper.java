/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.model.service.archived;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.eclipse.emf.query.conditions.strings.StringValue;
import org.eclipse.emf.query.statements.FROM;
import org.eclipse.emf.query.statements.IQueryResult;
import org.eclipse.emf.query.statements.SELECT;
import org.eclipse.emf.query.statements.WHERE;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

public class ScenarioServiceIOHelper {

	private final ScenarioService scenarioService;

	private final IPath dataPath;

	public ScenarioServiceIOHelper(final ScenarioService scenarioService, final IPath dataPath) {
		this.scenarioService = scenarioService;
		this.dataPath = dataPath;
	}

	public InputStream createInputStream(final String uuid, final Map<?, ?> options) throws IOException {

		final URI uri = URI.createFileURI(dataPath.append(uuid).toString());
		final ExtensibleURIConverterImpl convertor = new ExtensibleURIConverterImpl();
		return convertor.createInputStream(uri, options);
	}

	public OutputStream createOutputStream(final String uuid, final Map<?, ?> options) throws IOException {

		final URI uri = URI.createFileURI(dataPath.append(uuid).toString());
		final ExtensibleURIConverterImpl convertor = new ExtensibleURIConverterImpl();
		return convertor.createOutputStream(uri, options);
	}

	public boolean exists(final String uuid, final Map<?, ?> options) {

		final IPath filePath = dataPath.append(uuid);
		return filePath.toFile().exists();
	}

	public ScenarioInstance findInstance(final String uuid) {
		final SELECT query = new SELECT(1, new FROM(scenarioService), new WHERE(new EObjectAttributeValueCondition(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Uuid(), new StringValue(uuid))));
		final IQueryResult queryResult = query.execute();

		final Iterator<EObject> itr = queryResult.iterator();
		while (itr.hasNext()) {
			return (ScenarioInstance) itr.next();
		}
		return null;
	}

	public void delete(final String uuid, final Map<?, ?> options) {
		final IPath filePath = dataPath.append(uuid);
		filePath.toFile().delete();

		// Remove from the model
		final SELECT query = new SELECT(1, new FROM(scenarioService), new WHERE(new EObjectAttributeValueCondition(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Uuid(), new StringValue(uuid))));
		final IQueryResult queryResult = query.execute();

		final Iterator<EObject> itr = queryResult.iterator();
		while (itr.hasNext()) {
			final EObject obj = itr.next();
			obj.eContainer().eContents().remove(obj);
		}
	}

	public EObject loadScenario(final String uuid, final Map<?, ?> options) throws IOException {
		final IPath filePath = dataPath.append(uuid);

		final URI uri = URI.createFileURI(filePath.toFile().toString());

		final Resource.Factory f = Resource.Factory.Registry.INSTANCE.getFactory(uri);

		final Resource res = f.createResource(uri);

		final HashMap<Object, Object> options2 = new HashMap<Object, Object>(options);

		options2.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
		options2.put(XMLResource.OPTION_RESOURCE_HANDLER, new XMLResource.ResourceHandler() {

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

		res.load(options2);

		return res.getContents().get(0);
	}

	
}
