package com.mmxlabs.models.ui.merge;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * The {@link IMappingDescriptor} describes the relationship between two instances of the same containment feature.
 * 
 * @author Simon Goodall
 * @since 4.1
 */
public interface IMappingDescriptor {

	/**
	 * Returns a map of objects in the destination which have an equivalent in the source
	 * 
	 * @return
	 */
	Map<EObject, EObject> getDestinationToSourceMap();

	/**
	 * Returns the list of object in the source which are not in the destination
	 * 
	 * @return
	 */
	List<EObject> getAddedObjects();

	/**
	 * Returns the list of objects in the destination which are not in the source
	 * 
	 * @return
	 */
	Collection<EObject> getRemovedObjects();

	/**
	 * Returns the {@link EReference} feature representing the containment from the container
	 * 
	 * @return
	 */
	EReference getReference();

	/**
	 * Returns the destination container
	 * 
	 * @return
	 */
	EObject getDestinationContainer();

	/**
	 * Returns the source container
	 * 
	 * @return
	 */
	EObject getSourceContainer();
}