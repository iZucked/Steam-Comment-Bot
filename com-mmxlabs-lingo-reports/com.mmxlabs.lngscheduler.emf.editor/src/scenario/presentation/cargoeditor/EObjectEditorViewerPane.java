/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */

package scenario.presentation.cargoeditor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scenario.NamedObject;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.handlers.AddAction;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.lngscheduler.emf.extras.EMFUtils;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.shiplingo.importer.importers.ExportCSVAction;
import com.mmxlabs.shiplingo.importer.importers.ImportCSVAction;
import com.mmxlabs.shiplingo.importer.importers.ImportUI;
import com.mmxlabs.shiplingo.ui.tableview.EObjectTableViewer;
import com.mmxlabs.shiplingo.ui.tableview.ICellManipulator;
import com.mmxlabs.shiplingo.ui.tableview.ICellRenderer;

/**
 * A viewerpane which displays a list of EObjects in a table and lets you edit
 * them
 * 
 * @author hinton
 * 
 */
public class EObjectEditorViewerPane extends ViewerPane {
	protected final ScenarioEditor part;
	protected EObjectTableViewer eObjectTableViewer;
	
	private static final Logger log = LoggerFactory.getLogger(EObjectEditorViewerPane.class);

	private boolean lockedForEditing;
	
	public EObjectEditorViewerPane(final IWorkbenchPage page,
			final ScenarioEditor part) {
		super(page, part);
		this.part = part;
	}
	
	/**
	 * @return True if edit operations in this editor are disabled
	 */
	public boolean isLockedForEditing() {
		return lockedForEditing;
	}

	/**
	 * @param pass true to disable editing, false to re-enable it.
	 */
	public void setLockedForEditing(boolean lockedForEditing) {
		this.lockedForEditing = lockedForEditing;
		eObjectTableViewer.setLockedForEditing(isLockedForEditing());
	}
	
	@Override
	public EObjectTableViewer createViewer(final Composite parent) {
		eObjectTableViewer = new EObjectTableViewer(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL) {
			@Override
			protected boolean refreshOrGiveUp() {
				if (ImportUI.isImporting()) {
					ImportUI.refreshLater(eObjectTableViewer);
					return true;
				}
				return false;
			}
		};

		getToolBarManager().add(new GroupMarker("filter"));
		getToolBarManager().add(new GroupMarker("pack"));
		getToolBarManager().add(new GroupMarker("additions"));
		getToolBarManager().add(new GroupMarker("edit"));

		getToolBarManager().add(new GroupMarker("importers"));
		getToolBarManager().add(new GroupMarker("exporters"));
		getToolBarManager().add(new GroupMarker("copy"));
		{
			final Action a = new PackGridTableColumnsAction(eObjectTableViewer);
			getToolBarManager().appendToGroup("pack", a);
		}
		{
			final Action a = new CopyGridToClipboardAction(eObjectTableViewer.getGrid());
			getToolBarManager().appendToGroup("copy", a);
		}

		getToolBarManager().update(true);

		return eObjectTableViewer;
	}

	protected Action createDeleteAction(final GridTableViewer viewer,
			final EditingDomain editingDomain) {
		return new scenario.presentation.cargoeditor.handlers.DeleteAction(
				editingDomain) {
			@Override
			protected Collection<EObject> getTargets() {
				return ((IStructuredSelection) viewer.getSelection()).toList();
			}

			@Override
			public void run() {
				super.run();
				refresh();
			}
		};
	}

	/**
	 * Creates an add action; many subclasses will want to override this.
	 * 
	 * @param viewer2
	 * @param editingDomain
	 * @param e
	 * @return
	 */
	protected Action createAddAction(final GridTableViewer viewer,
			final EditingDomain editingDomain, final EMFPath contentPath) {
		return new AddAction(editingDomain, contentPath.getTargetType()
				.getName()) {
			@Override
			public Object getOwner() {
				return contentPath.get((EObject) viewer.getInput(), 1);
			}

			@Override
			public Object getFeature() {
				return contentPath.getPathComponent(0);
			}

			@Override
			public EObject createObject(final boolean usingSelection) {
				if (usingSelection && viewer.getSelection().isEmpty() == false) {
					if (viewer.getSelection() instanceof IStructuredSelection) {
						final IStructuredSelection sel = (IStructuredSelection) viewer
								.getSelection();
						if (sel.size() == 1) {
							final Object selection = sel.getFirstElement();
							if (selection instanceof EObject) {
								final EObject object = (EObject) selection;
								return EcoreUtil.copy(object); // duplicate when
																// there is a
																// selection
							}
						}
					}
				}
				final EReference ref = (EReference) contentPath
						.getPathComponent(0);
				final EClass ec = ref.getEReferenceType();
				final EPackage p = ec.getEPackage();

				if (ec.isAbstract()) {
					// select subclass
					final LinkedList<EClass> subClasses = new LinkedList<EClass>();
					for (final EClassifier classifier : p.getEClassifiers()) {
						if (classifier instanceof EClass) {
							final EClass possibleSubClass = (EClass) classifier;
							if (ec.isSuperTypeOf(possibleSubClass)
									&& possibleSubClass.isAbstract() == false) {
								subClasses.add(possibleSubClass);
							}
						}
					}
					// display picker dialog somehow
					final Shell shell = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell();
					final ElementListSelectionDialog elsd = new ElementListSelectionDialog(
							shell, new LabelProvider() {
								@Override
								public String getText(final Object element) {
									return ((EClass) element).getName();
								}
							});
					elsd.setElements(subClasses.toArray());
					elsd.setTitle("Which type of " + ec.getName()
							+ " do you want to add?");
					if (elsd.open() != Window.OK) {
						return null;
					}
					final Object[] result = elsd.getResult();
					return EMFUtils.createEObject((EClass) result[0]); // include
																		// contained
																		// objects
				} else {
					return EMFUtils.createEObject(ec);
				}
			}
		};
	}

	public void addColumn(final String columnName,
			final ICellRenderer renderer, final ICellManipulator manipulator,
			final Object... pathObjects) {
		eObjectTableViewer.addColumn(columnName, renderer, manipulator, pathObjects);
	}

	public void init(final List<EReference> path,
			final AdapterFactory adapterFactory) {
		eObjectTableViewer.init(adapterFactory, path.toArray(new EReference[path.size()]));
		final Grid table = eObjectTableViewer.getGrid();
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		{
			final ToolBarManager x = getToolBarManager();
			final EMFPath ePath = new EMFPath(true, path);
			{
				final Action a = createAddAction(eObjectTableViewer,
						part.getEditingDomain(), ePath);
				if (a != null) {
					x.appendToGroup("edit", a);
				}
			}
			{
				final Action b = createDeleteAction(eObjectTableViewer,
						part.getEditingDomain());
				if (b != null) {
					x.appendToGroup("edit", b);
				}
			}
			{
				final Action a = createImportAction(eObjectTableViewer,
						part.getEditingDomain(), ePath);
				if (a != null)
					x.appendToGroup("importers", a);
			}
			{
				final Action a = createExportAction(eObjectTableViewer, ePath);
				if (a != null)
					x.appendToGroup("exporters", a);
			}

			x.update(true);
		}
	}

	/**
	 * A hook to override or disable the export button.
	 * 
	 * @param ePath
	 * @param viewer2
	 * @param editingDomain
	 * @return
	 */
	protected Action createExportAction(final GridTableViewer viewer,
			final EMFPath ePath) {
		return new ExportCSVAction() {
			@Override
			public List<EObject> getObjectsToExport() {
				final EObject root = (EObject) viewer.getInput();
				final Object result = ePath.get(root);
				if (result instanceof List) {
					return (List<EObject>) result;
				} else {
					return Collections.singletonList((EObject) result);
				}
			}

			@Override
			public EClass getExportEClass() {
				return (EClass) ePath.getTargetType();
			}
		};
	}

	/**
	 * Provides a hook for subclasses to override the behavior of the import
	 * button, in particular for vessel classes which need a fuel curve file.
	 * 
	 * @param viewer2
	 * @param editingDomain
	 * @param ePath
	 * @return
	 */
	protected Action createImportAction(final GridTableViewer viewer,
			final EditingDomain editingDomain, final EMFPath ePath) {
		return new ImportCSVAction() {

			@Override
			protected EObject getToplevelObject() {
				return (EObject) viewer.getInput();
			}

			@Override
			protected EClass getImportClass() {
				return (EClass) ((EStructuralFeature) ePath.getPathComponent(0))
						.getEType();
			}

			@Override
			public void addObjects(final Collection<EObject> newObjects) {
				// this is quite a complicated procedure because it has to
				// handle potential replacements.
				final Map<String, EObject> objectsWithNames = new HashMap<String, EObject>();
				for (final EObject oldObject : ((EList<EObject>) ePath
						.get(getToplevelObject()))) {
					objectsWithNames.put(getId(oldObject), oldObject);
				}

				final CompoundCommand cc = new CompoundCommand();

				final EObject container = (EObject) ePath.get(
						getToplevelObject(), 1);
				final Object containerFeature = ePath.getPathComponent(0);

				for (final EObject newObject : newObjects) {
					final String newId = getId(newObject);
					if (objectsWithNames.containsKey(newId)) {
						// object existed before, so we have to replace it
						final EObject oldObject = objectsWithNames.get(newId);
						final Command fixrefs = createFixReferencesAndContainments(
								oldObject, newObject);
						if (fixrefs != null)
							cc.append(fixrefs);

						// fix references to contained objects as well, or we
						// get a dangling reference

						// now perform replace in the original container
						final Command replace = ReplaceCommand.create(
								editingDomain, oldObject.eContainer(),
								oldObject.eContainingFeature(), oldObject,
								Collections.singleton(newObject));

						if (replace.canExecute() == false) {
							log.error("Cannot execute replace command from "
									+ oldObject + " to " + newObject, new RuntimeException("Replace command not executable!"));
						}
						cc.append(replace);
					} else {
						// just do the add
						cc.append(AddCommand.create(editingDomain, container,
								containerFeature, newObject));
					}
				}

				if (cc.canExecute() == false) {
					log.error("Cannot execute import command", new RuntimeException("Import command not executable!"));
				}

				editingDomain.getCommandStack().execute(cc);

				ImportUI.refresh(viewer);
			}

			/**
			 * Fix up any references to oldObject so they refer to newObject,
			 * and fix up any references to singly-contained entries in
			 * oldObject to the analogous entries in newObject, if they exist
			 * 
			 * returns null if there is nothing to do.
			 * 
			 * @param oldObject
			 * @param newObject
			 * @return
			 */
			private Command createFixReferencesAndContainments(
					final EObject oldObject, final EObject newObject) {
				final CompoundCommand cc = new CompoundCommand();
				final Command fixReferences = createFixReferences(oldObject,
						newObject);
				if (fixReferences != null)
					cc.append(fixReferences);

				for (final EReference reference : oldObject.eClass()
						.getEAllContainments()) {
					if (reference.isMany() == false) {
						if (newObject.eClass().getEAllContainments()
								.contains(reference)) {
							final Command recur = createFixReferencesAndContainments(
									(EObject) oldObject.eGet(reference),
									(EObject) newObject.eGet(reference));
							if (recur != null)
								cc.append(recur);
						}
					}
				}
				if (cc.getCommandList().isEmpty())
					return null;
				else {
					if (cc.canExecute() == false) {
						System.err.println("Cannot execute ref fixer from "
								+ oldObject + " to " + newObject);
					}
					return cc;
				}

			}

			/**
			 * Create a command which updates references to oldObject so that
			 * they point to newObject instead. Returns null if there are no
			 * references to update (because an empty compoundCommand is not
			 * executable.
			 * 
			 * @param oldObject
			 * @param newObject
			 * @return
			 */
			private Command createFixReferences(final EObject oldObject,
					final EObject newObject) {

				final CompoundCommand cc = new CompoundCommand();
				// find all references to it
				final Collection<Setting> references = EcoreUtil.UsageCrossReferencer
						.find(oldObject, getToplevelObject());

				// iterate over those references and fix them
				for (final Setting setting : references) {
					final EStructuralFeature feature = setting
							.getEStructuralFeature();
					if (feature instanceof EReference) {
						final EReference ref = (EReference) feature;

						// multi-references need a remove and an add
						// TODO this will NOT WORK if the old list
						// contains more than one reference
						// to the object being replaced, but at the
						// moment our domain does not do that.
						if (ref.isMany()) {
							final int index = ((EList) setting.getEObject()
									.eGet(feature)).indexOf(oldObject);

							cc.append(RemoveCommand.create(editingDomain,
									setting.getEObject(),
									setting.getEStructuralFeature(), oldObject));

							// add the new one in at the same index

							cc.append(AddCommand.create(editingDomain,
									setting.getEObject(),
									setting.getEStructuralFeature(), newObject,
									index));

							continue; // skip over generic set four
										// lines below
						}
					}

					// single references need a set
					cc.append(SetCommand.create(editingDomain,
							setting.getEObject(),
							setting.getEStructuralFeature(), newObject));
				}

				if (cc.getCommandList().isEmpty())
					return null;

				if (cc.canExecute() == false) {
					System.err.println("Cannot fix references from "
							+ oldObject + " to " + newObject);
				}
				return cc;
			}

			private String getId(final EObject object) {
				if (object instanceof NamedObject) {
					return ((NamedObject) object).getName();
				} else {
					final EDataType stringType = EcorePackage.eINSTANCE
							.getEString();
					for (final EAttribute attribute : object.eClass()
							.getEAllAttributes()) {
						if (attribute.getEAttributeType().equals(stringType)) {
							if (attribute.getName().equalsIgnoreCase("name")
									|| attribute.getName().equalsIgnoreCase(
											"id")) {
								// add to registry for type
								return (String) object.eGet(attribute);
							}
						}
					}
				}
				return "";
			}
		};
	}

	@Override
	public void dispose() {
		eObjectTableViewer.dispose();

		super.dispose();
	}

	@Override
	protected void requestActivation() {
		final Control c = eObjectTableViewer.getControl();
		if (c.isDisposed() || c.getDisplay() == null)
			return;
		super.requestActivation();
		part.setCurrentViewerPane(this);
	}

	@Override
	public void showFocus(boolean inFocus) {
		final Control c = eObjectTableViewer.getControl();
		if (c.isDisposed() || c.getDisplay() == null)
			return;
		super.showFocus(inFocus);
	}

	@Override
	public void setFocus() {
		final Control c = eObjectTableViewer.getControl();
		if (c.isDisposed() || c.getDisplay() == null)
			return;
		super.setFocus();
	}

	@Override
	public GridTableViewer getViewer() {
		return eObjectTableViewer;
	}

	public <T extends ICellManipulator & ICellRenderer> void addTypicalColumn(
			final String columnName, final T manipulatorAndRenderer,
			final Object... path) {
		this.addColumn(columnName, manipulatorAndRenderer,
				manipulatorAndRenderer, path);
	}

	public void refresh() {
		if (eObjectTableViewer != null) {
			if (eObjectTableViewer.getControl() != null
					&& eObjectTableViewer.getControl().isDisposed() == false) {
				eObjectTableViewer.refresh();
			}
		}
	}
}
