/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.delete;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.schedule.CargoAllocation;

/**
 * @author Tom Hinton
 * 
 */
public class DeleteSequenceCommand extends Deleter {
	/**
	 * @param domain
	 * @param collection
	 * @param deletedObjects 
	 */
	public DeleteSequenceCommand(EditingDomain domain, Collection<? extends EObject> targets) {
		super(domain, targets);
	}
	
	@Override
	public Set<EObject> getObjectsToDelete() {
		final Set<EObject> t = super.getObjectsToDelete();
		
		final Collection<EObject> eObjects = new LinkedHashSet<EObject>();
		for (final Object wrappedObject : collection) {
			final Object object = AdapterFactoryEditingDomain
					.unwrap(wrappedObject);
			if (object instanceof EObject) {
				eObjects.add((EObject) object);
				for (Iterator<EObject> j = ((EObject) object).eAllContents(); j
						.hasNext();) {
					eObjects.add(j.next());
				}
			} else if (object instanceof Resource) {
				for (Iterator<EObject> j = ((Resource) object).getAllContents(); j
						.hasNext();) {
					eObjects.add(j.next());
				}
			}
		}

		final Map<EObject, Collection<EStructuralFeature.Setting>> usages = super
				.findReferences(eObjects);

		for (final Map.Entry<EObject, Collection<EStructuralFeature.Setting>> entry : usages
				.entrySet()) {
			for (final EStructuralFeature.Setting setting : entry.getValue()) {
				final EObject referer = setting.getEObject();
				if (referer instanceof CargoAllocation) {
					t.addAll(DeleteHelper.createDeleter(domain, referer).getObjectsToDelete());
				}
			}
		}
		
		return t;
	}



}
