package com.mmxlabs.model.service;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;

public interface IModelService {

	IModelInstance getModel(URI uri) throws IOException;

	void saveAs(IModelInstance instance, URI uri) throws IOException;

	void copyTo(IModelInstance from, URI to) throws IOException;

	void copyTo(URI from, URI to) throws IOException;

	void delete(IModelInstance instance) throws IOException;
}
