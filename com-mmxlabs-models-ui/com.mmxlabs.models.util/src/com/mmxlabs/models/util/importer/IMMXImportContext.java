/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer;

import java.util.Collection;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * A single instance of this will be used whenever some importing is happening. It tracks all of the named objects that may be referenced, and handles setting up any deferred processing which has to
 * happen after the main importing is done.
 * 
 * @author hinton
 * 
 */
public interface IMMXImportContext extends IImportContext {
	/**
	 * This stage is intended for any simple modifications to attributes, which just require that the object be largely OK
	 */
	public static final int STAGE_MODIFY_ATTRIBUTES = 5;
	/**
	 * This is the stage during which references are resolved
	 */
	public static final int STAGE_RESOLVE_REFERENCES = 10;
	/**
	 * This is the stage during which references that depend on other references are resolved
	 */
	public static final int STAGE_RESOLVE_CROSSREFERENCES = 20;
	/**
	 * This is for anything which requires all references to be resolved
	 */
	public static final int STAGE_REFERENCES_RESOLVED = 25;
	/**
	 * This is for things which do arbitrary changes and require most stuff to be in place.
	 */
	public static final int STAGE_MODIFY_SUBMODELS = 30;

	MMXRootObject getRootObject();

	/**
	 * Get a named object which has been passed to {@link #registerNamedObject(NamedObject)} whose type is the closest subtype of the given type
	 * 
	 * @param name
	 * @param preferredType
	 * @return
	 */
	NamedObject getNamedObject(@NonNull final String name, @NonNull final EClass preferredType);

	/**
	 * Returns a {@link Collection} of all objects which have been passed to {@link #registerNamedObject(NamedObject)} with the given name.
	 * 
	 * @param name
	 * @return
	 */
	Collection<NamedObject> getNamedObjects(@NonNull String name);

	/**
	 * Add the given named object to this context.
	 * 
	 * @param object
	 */
	void registerNamedObject(@NonNull NamedObject object);

}
