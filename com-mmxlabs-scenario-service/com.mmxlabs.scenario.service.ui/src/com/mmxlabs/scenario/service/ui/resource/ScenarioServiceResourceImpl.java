/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.resource;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.eclipse.emf.query.statements.FROM;
import org.eclipse.emf.query.statements.IQueryResult;
import org.eclipse.emf.query.statements.SELECT;
import org.eclipse.emf.query.statements.WHERE;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

public class ScenarioServiceResourceImpl extends ResourceImpl {

	private final IScenarioService scenarioService;

	public ScenarioServiceResourceImpl(final IScenarioService scenarioService) {
		super();
		this.scenarioService = scenarioService;
	}

	public ScenarioServiceResourceImpl(final IScenarioService scenarioService, final URI uri) {
		super(uri);
		this.scenarioService = scenarioService;
	}

	@Override
	public void save(final Map<?, ?> options) throws IOException {

//		scenarioService.save(uuid, options);
		
//		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void load(final Map<?, ?> options) throws IOException {

		// Remove "/" 
		final EObject scenario = scenarioService.getScenario(uri.path().substring(1));

		this.getContents().clear();
		this.getContents().add(scenario);
	}
	
	@Override
	protected void doUnload() {
		// TODO Auto-generated method stub
//		super.doUnload();
	}
	
	public ScenarioInstance findInstance(final String uuid) {
		final SELECT query = new SELECT(1, new FROM(scenarioService.getServiceModel()), new WHERE(new EObjectAttributeValueCondition(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Uuid(), new org.eclipse.emf.query.conditions.strings.StringValue(uuid))));
		final IQueryResult queryResult = query.execute();

		final Iterator<EObject> itr = queryResult.iterator();
		while (itr.hasNext()) {
			return (ScenarioInstance) itr.next();
		}
		return null;
	}

}
