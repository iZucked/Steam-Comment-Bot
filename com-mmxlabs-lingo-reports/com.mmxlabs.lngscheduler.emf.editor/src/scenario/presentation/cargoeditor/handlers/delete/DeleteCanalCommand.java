/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.delete;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.fleet.VesselClassCost;
import scenario.port.Canal;

/**
 * @author hinton
 *
 */
public class DeleteCanalCommand extends Deleter {
	public DeleteCanalCommand(EditingDomain domain, Set<? extends EObject> value) {
		super(domain, value);
	}

	@Override
	public Set<EObject> getObjectsToDelete() {
		final Set<EObject> t = super.getObjectsToDelete();

		for (final EObject o : collection) {
			if (o instanceof Canal) {
				final Map<EObject, Collection<Setting>> refs = super.findReferences(Collections.singleton(o));
				final Collection<Setting> s = refs.get(o);
				if (s == null) continue;
				for (final Setting setting : s) {
					if (setting.getEObject() instanceof VesselClassCost) {
						t.addAll(DeleteHelper.createDeleter(domain, setting.getEObject()).getObjectsToDelete());
					}
				}
			}
		}

		return t;
	}
}
