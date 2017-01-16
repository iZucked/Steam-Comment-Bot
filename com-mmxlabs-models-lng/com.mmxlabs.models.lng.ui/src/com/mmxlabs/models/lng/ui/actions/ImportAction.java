/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 // * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.actions;

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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.OtherNamesObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.rcp.common.actions.LockableAction;

/**
 * @author hinton
 * 
 */
public abstract class ImportAction extends LockableAction {
	/**
	 * Encapsulation of the data required to perform the import action. Typically pulled from an IScenarioEditingLocation, but can also be pulled from elsewhere, e.g. in a bulk import.
	 * 
	 * @author Simon McGregor
	 * 
	 */
	public interface ImportHooksProvider {
		Shell getShell();

		MMXRootObject getRootObject();

		EditingDomain getEditingDomain();

		String getImportFilePath();

		char getCsvSeparator();

		char getDecimalSeparator();

		void lock();

		void unlock();
	}

	/**
	 */
	protected final ImportHooksProvider importHooksProvider;

	/**
	 */
	public ImportAction(final ImportHooksProvider ihp) {
		super("Import", AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif"));
		importHooksProvider = ihp;

	}

	public ImportAction(final IScenarioEditingLocation part) {
		// create a new import hooks provider which pulls the data from the scenario editing location
		// and gets the import filename from a dialog
		this(new ImportHooksProvider() {

			@Override
			public Shell getShell() {
				return part.getShell();
			}

			@Override
			public MMXRootObject getRootObject() {
				return part.getRootObject();
			}

			@Override
			public EditingDomain getEditingDomain() {
				return part.getEditingDomain();
			}

			@Override
			public void lock() {
				part.setDisableCommandProviders(true);
				part.setDisableUpdates(true);
			}

			@Override
			public void unlock() {
				part.setDisableCommandProviders(false);
				part.setDisableUpdates(false);

			}

			@Override
			public String getImportFilePath() {
				final FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setFilterExtensions(new String[] { "*.csv" });
				return fileDialog.open();
			}

			@Override
			public char getCsvSeparator() {
				return ',';
			}

			@Override
			public char getDecimalSeparator() {
				return '.';
			}
		});
	}

	@Override
	public void run() {
		// import the data
		final DefaultImportContext context = safelyImport();

		// if there were any problems, pop up a problem dialog
		if (context != null && context.getProblems().isEmpty() == false) {
			final ImportProblemDialog ipd = new ImportProblemDialog(importHooksProvider.getShell());
			ipd.open(context);
		}
	}

	/**
	 */
	public DefaultImportContext safelyImport() {
		DefaultImportContext context = null;
		try {
			importHooksProvider.lock();
			context = new DefaultImportContext(importHooksProvider.getDecimalSeparator());
			context.setRootObject(importHooksProvider.getRootObject());
			context.registerNamedObjectsFromSubModels();

			doImportStages(context);

		} finally {
			importHooksProvider.unlock();
		}

		return context;
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

		final EditingDomain domain = importHooksProvider.getEditingDomain();
		final CompoundCommand setter = new CompoundCommand();
		setter.append(IdentityCommand.INSTANCE);
		@SuppressWarnings("unchecked")
		final List<EObject> existingObjects = (List<EObject>) container.eGet(containment);
		final Map<String, NamedObject> existingNamedObjects = collectNamedObjects(existingObjects);
		final Map<String, NamedObject> newNamedObjects = collectNamedObjects(importedObjects);
		// intersect key set
		final Set<String> updatedObjectNames = new HashSet<String>(existingNamedObjects.keySet());
		updatedObjectNames.retainAll(newNamedObjects.keySet());

		// Use Set command as named object map could contain duplicates if the object has multiple names
		final Set<EObject> deletedObjects = new HashSet<EObject>();
		for (final String name : updatedObjectNames) {
			final EObject oldObject = existingNamedObjects.get(name);
			final EObject newObject = newNamedObjects.get(name);

			// Mark object for removal
			if (deletedObjects.add(oldObject)) {
				// update references to point from old object to new object - only if we have not already seen this object
				setter.append(replace(domain, oldObject, newObject, importHooksProvider.getRootObject()));
			}

		}

		merge.append(setter);
		if (deletedObjects.isEmpty() == false) {
			merge.append(DeleteCommand.create(domain, deletedObjects));
		}
		if (importedObjects.isEmpty() == false) {
			merge.append(AddCommand.create(domain, container, containment, importedObjects));
		}

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
	private Command replace(final EditingDomain domain, final EObject oldObject, final EObject newObject, final MMXRootObject rootObject) {
		final CompoundCommand result = new CompoundCommand();
		result.append(IdentityCommand.INSTANCE);
		result.setDescription("Replacing " + oldObject + " with " + newObject);
		if (oldObject == null)
			return result;

		// update old references
		final Collection<Setting> refsToOldObject = EcoreUtil.UsageCrossReferencer.find(oldObject, rootObject);
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

	private Map<String, NamedObject> collectNamedObjects(final List<EObject> existingObjects) {
		final HashMap<String, NamedObject> result = new HashMap<String, NamedObject>();
		for (final EObject object : existingObjects) {
			if (object instanceof NamedObject) {
				final NamedObject namedObject = (NamedObject) object;
				result.put(namedObject.getName(), namedObject);
				if (namedObject instanceof OtherNamesObject) {
					final OtherNamesObject otherNamesObject = (OtherNamesObject) namedObject;
					for (final String otherName : otherNamesObject.getOtherNames()) {
						result.put(otherName, namedObject);
					}
				}
			}
		}
		return result;
	}
}
