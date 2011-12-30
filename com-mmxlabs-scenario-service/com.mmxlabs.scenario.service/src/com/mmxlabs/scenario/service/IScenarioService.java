package com.mmxlabs.scenario.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.mmxlabs.scenario.service.model.ScenarioService;

public interface IScenarioService {

	/**
	 * Returns the name for this service
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Return the EMF logical model of the service
	 * 
	 * @return
	 */
	ScenarioService getServiceModel();

	/**
	 * Returns true if a resource exists with the specified UUID
	 * 
	 * @param uuid
	 * @param options
	 * @return
	 */
	boolean exists(String uuid, Map<?, ?> options);

	InputStream createInputStream(String uuid, Map<?, ?> options) throws IOException;

	OutputStream createOutputStream(String uuid, Map<?, ?> options) throws IOException;

	void delete(String uuid, Map<?, ?> options) throws IOException;
}
