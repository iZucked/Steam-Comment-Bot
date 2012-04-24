package com.mmxlabs.model.service;

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;

public interface IModelInstance {
	
	EObject getModel();
	
	void save() throws IOException;
	
	void delete() throws IOException;
	
	void rollback();
	
}
