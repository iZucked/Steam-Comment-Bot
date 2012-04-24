package com.mmxlabs.scenario.service;

import org.eclipse.emf.common.util.URI;

import com.mmxlabs.model.service.IModelService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Implementations of this interface handle the loading of a scenario definition from a URI. An {@link IModelService} instance is expected to be provided to load models.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioFormatHandler {

	String getName();

	/**
	 * The {@link IModelService} instance to find sub-models. This shold be called before any other calls to {@link #load(URI)}, {@link #save(URI)} or getSc;
	 * 
	 * @param modelService
	 */
	void setModelService(IModelService modelService);

	/**
	 * Load the scenario from the given {@link URI}
	 * 
	 * @param uri
	 */
	void load(URI uri);

	void save(URI uri);

	/**
	 * Once loaded return the {@link ScenarioInstance}.
	 * 
	 * @return
	 */
	ScenarioInstance getScenario();
}
