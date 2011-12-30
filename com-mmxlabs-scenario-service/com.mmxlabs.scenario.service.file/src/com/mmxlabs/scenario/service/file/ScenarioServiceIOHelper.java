package com.mmxlabs.scenario.service.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.eclipse.emf.query.conditions.strings.StringValue;
import org.eclipse.emf.query.statements.FROM;
import org.eclipse.emf.query.statements.IQueryResult;
import org.eclipse.emf.query.statements.SELECT;
import org.eclipse.emf.query.statements.WHERE;

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
}
