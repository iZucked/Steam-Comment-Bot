package com.mmxlabs.models.lng.scenario.importWizards;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * Class to bulk-import information from CSV files.
 *  
 * Contains modified cut&paste code from ImportAction and SimpleImportAction (class was
 * written in a hurry). 
 * TODO: Move duplicated code into a common code object.
 * 
 * @author Simon McGregor
 *
 */
public class BulkImportCSVHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}

		Shell shell = HandlerUtil.getActiveShell(event);
		IScenarioServiceEditorInput editor = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);


		BulkImportWizard wizard = new BulkImportWizard(editor.getScenarioInstance());
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);
		
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();
		
		List<ScenarioInstance> scenarios = wizard.getSelectedScenarios();
		String importFilename = wizard.getImportFilename();
		
		if (scenarios != null && importFilename != null && !"".equals(importFilename)) {
			char separator = wizard.getCsvSeparator();
			EReference containment;
			Class<?> clazz;
			
			switch (wizard.getImportedField()) {
				/*
				case CARGOES: {
					// this is incorrect logic to import cargoes
					containment = CargoPackage.Literals.CARGO_MODEL__CARGOES;
					clazz = CargoModel.class;
					break;
				}
				*/
				case COMMODITY_INDICES: {
					containment = PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES;
					clazz = PricingModel.class;
					break;
				}
				
				default: {
					throw new ExecutionException(String.format("Unsupported import target (code %d).", wizard.getImportedField()) );
				}			
			}
			bulkImport(containment, clazz, scenarios, importFilename, separator);
		}

		return null;
	}
	
	void bulkImport(EReference containment, Class<?> clazz, List<ScenarioInstance> instances, String filename, char separator) {
		try {
			for (ScenarioInstance instance: instances) {
				if (instance.getInstance() == null) {
					instance.getScenarioService().load(instance);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (ScenarioInstance instance: instances) {
			CommandProviderAwareEditingDomain domain = (CommandProviderAwareEditingDomain) instance.getAdapters().get(EditingDomain.class); 
			domain.setCommandProvidersDisabled(true);
			ScenarioLock lock = instance.getLock(ScenarioLock.EDITORS);
			try {
				MMXRootObject rootObject = (MMXRootObject) instance.getInstance();
				
				final DefaultImportContext context = new DefaultImportContext();
				context.setRootObject(rootObject);
				context.registerNamedObjectsFromSubModels();
				
				EObject model = (EObject) rootObject.getSubModel(clazz);
				
				doImportStages(containment, model, context, filename, instance, separator);
			}
			finally {
				domain.setCommandProvidersDisabled(false);
				lock.release();
			}
		}		
		
	}
	
	protected void doImportStages(EReference containment, EObject container, final DefaultImportContext context, String path, ScenarioInstance instance, char separator) {

		final IClassImporter importer = getImporter(containment);

		CSVReader reader = null;
		try {
			reader = new CSVReader(new File(path), separator);
			final Collection<EObject> importedObjects = importer.importObjects(containment.getEReferenceType(), reader, context);
			context.run();

			EditingDomain domain = (EditingDomain) instance.getAdapters().get(EditingDomain.class);
			
			final Command cmd = mergeImports(container, containment, importedObjects, instance);
			domain.getCommandStack().execute(cmd);
		} catch (final IOException e) {
			//log.error(e.getMessage(), e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (final IOException e) {
			}
		}
	}

	protected IClassImporter getImporter(final EReference containment) {
		return Activator.getDefault().getImporterRegistry().getClassImporter(containment.getEReferenceType());
	}

	protected Command mergeImports(final EObject container, final EReference containment, final Collection<EObject> imports, ScenarioInstance instance) {
		return mergeLists(container, containment, new ArrayList<EObject>(imports), instance);
	}
	
	protected CompoundCommand mergeLists(final EObject container, final EReference containment, final List<EObject> importedObjects, ScenarioInstance instance) {
		final CompoundCommand merge = new CompoundCommand();
		
		final CompoundCommand setter = new CompoundCommand();
		setter.append(IdentityCommand.INSTANCE);
		@SuppressWarnings("unchecked")
		final List<EObject> existingObjects = (List<EObject>) container.eGet(containment);
		final Map<String, NamedObject> existingNamedObjects = collectNamedObjects(existingObjects);
		final Map<String, NamedObject> newNamedObjects = collectNamedObjects(importedObjects);
		// intersect key set
		final Set<String> updatedObjectNames = new HashSet<String>(existingNamedObjects.keySet());
		updatedObjectNames.retainAll(newNamedObjects.keySet());
		
		EditingDomain domain = (EditingDomain) instance.getAdapters().get(EditingDomain.class);

		final List<EObject> deletedObjects = new ArrayList<EObject>();
		for (final String name : updatedObjectNames) {
			final EObject oldObject = existingNamedObjects.get(name);
			final EObject newObject = newNamedObjects.get(name);
			
			// update references to point from old object to new object
			setter.append(replace(domain, oldObject, newObject, (MMXRootObject) instance.getInstance()));
			// add new object

			deletedObjects.add(oldObject);
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
	
	private Map<String, NamedObject> collectNamedObjects(final List<EObject> existingObjects) {
		final HashMap<String, NamedObject> result = new HashMap<String, NamedObject>();
		for (final EObject object : existingObjects) {
			if (object instanceof NamedObject) {
				final NamedObject namedObject = (NamedObject) object;
				result.put(namedObject.getName(), namedObject);
				for (final String otherName : namedObject.getOtherNames()) {
					result.put(otherName, namedObject);
				}
			}
		}
		return result;
	}	

	private Command replace(final EditingDomain domain, final EObject oldObject, final EObject newObject, final MMXRootObject rootObject) {
		final CompoundCommand result = new CompoundCommand();
		result.append(IdentityCommand.INSTANCE);
		result.setDescription("Replacing " + oldObject + " with " + newObject);
		if (oldObject == null) return result;
		
		// update old references
		final List<EObject> subModels = new ArrayList<EObject>();
		for (final MMXSubModel sub : rootObject.getSubModels()) {
			subModels.add(sub.getSubModelInstance());
		}
		final Collection<Setting> refsToOldObject = EcoreUtil.UsageCrossReferencer.find(oldObject, subModels);
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
}
