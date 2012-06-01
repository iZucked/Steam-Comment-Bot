/**
// * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.rcp.common.actions.LockableAction;

/**
 * @author hinton
 *
 */
public abstract class ImportAction extends LockableAction {
	protected final IScenarioEditingLocation part;
	public ImportAction(final IScenarioEditingLocation part) {
		super("Import", AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif"));
		this.part = part;
	}

	@Override
	public void run() {
		try {
			part.setDisableCommandProviders(true);
			part.setDisableUpdates(true);
			final DefaultImportContext context = new DefaultImportContext();
			context.setRootObject(part.getRootObject());

			// first set up all existing named objects
			for (final MMXSubModel subModel : context.getRootObject().getSubModels()) {
				final TreeIterator<EObject> allObjects = subModel.getSubModelInstance().eAllContents();
				
				while (allObjects.hasNext()) {
					final EObject o = allObjects.next();
					if (o instanceof NamedObject)
						context.registerNamedObject((NamedObject) o);
				}
			}
			
			doImportStages(context);
			if (context.getProblems().isEmpty() == false) {
				final ImportProblemDialog ipd = new ImportProblemDialog(part.getShell());
				ipd.open(context);
			}
		} finally {
			part.setDisableCommandProviders(false);
			part.setDisableUpdates(false);
		}
	}

	/**
	 * Subclasses should use this method to run any importing they want to do, and then run the context and use {@link #mergeLists(EObject, EReference, List)} to finish off importing any objects.
	 * 
	 * The context has already been loaded with all the named objects in the scenario, but during the import additional named objects will be added.
	 * 
	 * The most recently match of best goodness is always preferred by the context so when running the context equally matching newly imported objects will be linked in preference to existing ones.
	 * 
	 * @param context
	 */
	protected abstract void doImportStages(final DefaultImportContext context);

	protected CompoundCommand mergeLists(final EObject container, final EReference containment, final List<EObject> importedObjects) {
		final CompoundCommand merge = new CompoundCommand();
		
		final EditingDomain domain = part.getEditingDomain();
		final CompoundCommand setter = new CompoundCommand();
		setter.append(IdentityCommand.INSTANCE);
		@SuppressWarnings("unchecked")
		final List<EObject> existingObjects = (List<EObject>) container.eGet(containment);
		final Map<String, NamedObject> existingNamedObjects = collectNamedObjects(existingObjects);
		final Map<String, NamedObject> newNamedObjects = collectNamedObjects(importedObjects);
		// intersect key set
		final Set<String> updatedObjectNames = new HashSet<String>(existingNamedObjects.keySet());
		updatedObjectNames.retainAll(newNamedObjects.keySet());
		
		final List<EObject> deletedObjects = new ArrayList<EObject>();
		for (final String name : updatedObjectNames) {
			final EObject oldObject = existingNamedObjects.get(name);
			final EObject newObject = newNamedObjects.get(name);
			
			// update references to point from old object to new object
			setter.append(replace(domain, oldObject, newObject, part.getRootObject()));
			// add new object

			deletedObjects.add(oldObject);
		}
		
		if (deletedObjects.isEmpty() == false)
			merge.append(DeleteCommand.create(domain, deletedObjects));
		if (importedObjects.isEmpty() == false)
			merge.append(AddCommand.create(domain, container, containment, importedObjects));
		merge.append(setter);
		
		return merge;
	}

	/**
	 * Create a command that replaces the old object with the new object in the given root object's tree
	 * 
	 * @param oldObject
	 * @param newObject
	 * @param rootObject
	 * @return
	 */
	private Command replace(EditingDomain domain, EObject oldObject, EObject newObject, MMXRootObject rootObject) {
		final CompoundCommand result = new CompoundCommand();
		result.append(IdentityCommand.INSTANCE);
		result.setDescription("Replacing " + oldObject + " with " + newObject);
		if (oldObject == null) return result;
		
		// update old references
		final List<EObject> subModels = new ArrayList<EObject>();
		for (final MMXSubModel sub : rootObject.getSubModels()) {
			subModels.add(sub.getSubModelInstance());
		}
		Collection<Setting> refsToOldObject = EcoreUtil.UsageCrossReferencer.find(oldObject, subModels);
		for (final Setting setting : refsToOldObject) {
			if (setting.getEStructuralFeature().isMany()) {
				result.append(ReplaceCommand.create(domain, setting.getEObject(), setting.getEStructuralFeature(), oldObject, Collections.singleton(newObject)));
			} else {
				result.append(SetCommand.create(domain, setting.getEObject(), setting.getEStructuralFeature(), newObject));
			}
		}
		
		if (newObject != null) {
			// recurse on contents
			final EList<EReference> newContainments = newObject.eClass().getEAllContainments();
			for (final EReference reference : oldObject.eClass().getEAllContainments()) {
				if (!reference.isMany() && newContainments.contains(reference)) {
					result.append(replace(domain, (EObject) oldObject.eGet(reference), (EObject) newObject.eGet(reference), rootObject));
				}
			}
		}
		
		// equalize UUIDs for replacements.
		// this is safe because we delete the old objects before adding new objects.
		if (oldObject instanceof UUIDObject && newObject instanceof UUIDObject) {
			result.append(SetCommand.create(domain, newObject, MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), ((UUIDObject) oldObject).getUuid()));
		}
		
		return result;
	}
	private Map<String, NamedObject> collectNamedObjects(List<EObject> existingObjects) {
		final HashMap<String, NamedObject> result = new HashMap<String, NamedObject>();
		for (final EObject object : existingObjects) {
			if (object instanceof NamedObject) {
				result.put(((NamedObject) object).getName(), (NamedObject) object);
			}
		}
		return result;
	}
}
