/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */

package scenario.presentation.cargoeditor;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import scenario.NamedObject;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.handlers.AddAction;
import scenario.presentation.cargoeditor.importer.ExportCSVAction;
import scenario.presentation.cargoeditor.importer.ImportCSVAction;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.rcp.common.actions.CopyTableToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackTableColumnsAction;

/**
 * A viewerpane which displays a list of EObjects in a table and lets you edit
 * them
 * 
 * @author hinton
 * 
 */
public class EObjectEditorViewerPane extends ViewerPane {
	private static final String COLUMN_RENDERER = "COLUMN_RENDERER";
	private static final String COLUMN_PATH = "COLUMN_PATH";
	private final ScenarioEditor part;
	private TableViewer viewer;

	private final ArrayList<TableColumn> columnSortOrder = new ArrayList<TableColumn>();
	private boolean sortDescending = false;

	public EObjectEditorViewerPane(final IWorkbenchPage page,
			final ScenarioEditor part) {
		super(page, part);
		this.part = part;

		IEditorInput x = part.getEditorInput();
		if (x instanceof IResource) {
			System.err.println(" x is a resource ");
		} else if (x instanceof IAdaptable) {
			final IResource res = (IResource) ((IAdaptable) x)
					.getAdapter(IResource.class);
			System.err.println("x adapted to " + res);
			try {
				for (final IMarker m : res.findMarkers(
						"org.eclipse.core.resources.marker", true,
						IResource.DEPTH_INFINITE)) {
					System.err.println(m);
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public Viewer createViewer(final Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI);

		{
			final Action a = new PackTableColumnsAction(viewer);
			getToolBarManager().add(a);
		}
		{
			final Action a = new CopyTableToClipboardAction(viewer.getTable());
			getToolBarManager().add(a);
		}

		getToolBarManager().update(true);
		return viewer;
	}

	protected Action createDeleteAction(final TableViewer viewer,
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
				viewer.refresh();
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
	protected Action createAddAction(final TableViewer viewer,
			final EditingDomain editingDomain, final EMFPath contentPath) {
		return new AddAction(editingDomain, contentPath.getTargetType()
				.getName()) {
			@Override
			protected Object getOwner() {
				return contentPath.get((EObject) viewer.getInput(), 1);
			}

			@Override
			protected Object getFeature() {
				return contentPath.getPathComponent(0);
			}

			@Override
			protected EObject createObject() {
				if (viewer.getSelection().isEmpty() == false) {
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
				final EFactory f = p.getEFactoryInstance();
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
								public String getText(Object element) {
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
					return f.create((EClass) result[0]);
				} else {
					return f.create(ec);
				}
			}

			@Override
			public void run() {
				super.run();
				viewer.refresh();
			}
		};
	}

	private boolean shouldEditCell = false;

	/**
	 * A hack to prevent single click editing, which is really annoying and
	 * silly.
	 * 
	 * @return
	 */
	protected boolean getShouldEditCell() {
		return shouldEditCell;
	}

	protected void setShouldEdit(final boolean b) {
		shouldEditCell = b;
	}

	public void addColumn(final String columnName,
			final ICellRenderer renderer, final ICellManipulator manipulator,
			final Object... pathObjects) {
		// create a column

		final EMFPath path = new EMFPath(true, pathObjects);

		final TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn tColumn = column.getColumn();
		tColumn.setText(columnName);
		tColumn.pack();
		tColumn.setResizable(true);

		// store the renderer here, so that we can use it in sorting later.
		tColumn.setData(COLUMN_RENDERER, renderer);
		tColumn.setData(COLUMN_PATH, path);

		columnSortOrder.add(tColumn);

		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return renderer.render(path.get((EObject) element));
			}

			@Override
			public Color getBackground(Object element) {
				final EObject object = (EObject) element;

				if (hasValidationError(object)) {
					return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
				}

				final TreeIterator<EObject> iterator = object.eAllContents();
				while (iterator.hasNext()) {
					if (hasValidationError(iterator.next()))
						return Display.getCurrent().getSystemColor(
								SWT.COLOR_RED);
				}

				return super.getBackground(element);
			}

			private boolean hasValidationError(final EObject object) {
				final Resource r = object.eResource();
				final IFile containingFile = ResourcesPlugin.getWorkspace()
						.getRoot()
						.getFile(new Path(r.getURI().toPlatformString(true)));
				try {
					final IMarker[] markers = containingFile.findMarkers(
							IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
					for (final IMarker marker : markers) {
						final String uri = marker.getAttribute(
								EValidator.URI_ATTRIBUTE, null);
						if (uri == null)
							continue;
						final URI uri2 = URI.create(uri);
						if (uri2.getFragment().equals(r.getURIFragment(object))) {
							return true;
						}
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}

				return false;
			}

		});

		column.setEditingSupport(new EditingSupport(viewer) {
			@Override
			protected void setValue(final Object element, final Object value) {
				// a value has come out of the celleditor and is being set on
				// the element.
				manipulator.setValue(path.get((EObject) element), value);
				viewer.refresh();
			}

			@Override
			protected Object getValue(final Object element) {
				return manipulator.getValue(path.get((EObject) element));
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {
				final CellEditor result = manipulator.getCellEditor(
						viewer.getTable(), path.get((EObject) element));
				return result;
			}

			@Override
			protected boolean canEdit(final Object element) {
				// intercept mouse listener here
				return getShouldEditCell()
						&& manipulator.canEdit(path.get((EObject) element));
			}
		});

		column.getColumn().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (columnSortOrder.get(0) == tColumn) {
					sortDescending = !sortDescending;
				} else {
					sortDescending = false;
					columnSortOrder.remove(tColumn);
					columnSortOrder.add(0, tColumn);
				}
				viewer.getTable().setSortColumn(tColumn);
				viewer.getTable().setSortDirection(
						sortDescending ? SWT.DOWN : SWT.UP);
				viewer.refresh(false);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});
	}

	public void init(final List<EReference> path,
			final AdapterFactory adapterFactory) {

		final Table table = viewer.getTable();
		final TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1,
					final Object e2) {
				int order = 0;
				final Iterator<TableColumn> iterator = columnSortOrder
						.iterator();
				while (order == 0 && iterator.hasNext()) {
					final TableColumn col = iterator.next();
					// TODO the columnSortOrder could just hold the renderers,
					// avoiding this lookup
					final ICellRenderer renderer = (ICellRenderer) col
							.getData(COLUMN_RENDERER);
					final EMFPath path = (EMFPath) col.getData(COLUMN_PATH);
					final Comparable c1 = renderer.getComparable(path
							.get((EObject) e1));
					final Comparable c2 = renderer.getComparable(path
							.get((EObject) e2));
					order = c1.compareTo(c2);
				}

				return sortDescending ? -order : order;
			}
		});

		final Listener mouseDownListener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				// alernatively, check here whether click lies in the selected
				// row.
				setShouldEdit(false);
			}
		};

		final Listener measureListener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				event.height = 18;
			}
		};

		final Listener doubleClickListener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				setShouldEdit(true);
				final TableItem[] selection = table.getSelection();

				if (selection.length != 1) {
					return;
				}

				final TableItem item = table.getSelection()[0];

				for (int i = 0; i < table.getColumnCount(); i++) {
					if (item.getBounds(i).contains(event.x, event.y)) {
						viewer.editElement(item.getData(), i);
						setShouldEdit(false);
						break;
					}
				}
			}
		};

		table.addListener(SWT.MouseDown, mouseDownListener);
		table.addListener(SWT.MeasureItem, measureListener);
		table.addListener(SWT.MouseDoubleClick, doubleClickListener);

		table.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				table.removeListener(SWT.MouseDown, mouseDownListener);
				table.removeListener(SWT.MeasureItem, measureListener);
				table.removeListener(SWT.MouseDoubleClick, doubleClickListener);
			}

		});

		final HashSet<EObject> currentElements = new HashSet<EObject>();

		final EContentAdapter adapter = new EContentAdapter() {
			@Override
			public void notifyChanged(final Notification notification) {
				super.notifyChanged(notification);
				// System.err.println(notification);
				if (notification.isTouch() == false) {
					// this is a change, so we have to refresh.
					// ideally we just want to update the changed object, but we
					// get the notification from
					// somewhere below, so we need to go up
					EObject source = (EObject) notification.getNotifier();
					// if (currentElements.contains(source))
					// return;
					while (!(currentElements.contains(source))
							&& ((source = source.eContainer()) != null))
						;
					if (source != null) {
						viewer.update(source, null);
						// this seems to clear the selection
						return;
					}
				}
			}

		};
		viewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory) {

			@SuppressWarnings("rawtypes")
			@Override
			public Object[] getElements(Object object) {
				adapter.unsetTarget(adapter.getTarget()); // remove adapter from
															// old input
				currentElements.clear();
				if (object instanceof EObject) {
					EObject o = (EObject) object;
					for (final EReference ref : path) {
						object = o.eGet(ref);
						if (object instanceof EList) {
							for (final EObject e : (EList<EObject>) object) {
								e.eAdapters().add(adapter);
								currentElements.add(e);
							}
							return ((EList) object).toArray();
						}
						if (object instanceof EObject) {
							o = (EObject) object;
						}
					}
					currentElements.add(o);
					return new Object[] { o };
				}
				return new Object[] {};
			}
		});

		{
			final ToolBarManager x = getToolBarManager();
			final EMFPath ePath = new EMFPath(true, path);
			{
				final Action a = createAddAction(viewer,
						part.getEditingDomain(), ePath);
				if (a != null) {
					x.add(a);
				}
			}
			{
				final Action b = createDeleteAction(viewer,
						part.getEditingDomain());
				if (b != null) {
					x.add(b);
				}
			}
			{
				final Action a = createImportAction(viewer,
						part.getEditingDomain(), ePath);
				if (a != null)
					x.add(a);
			}
			{
				final Action a = createExportAction(viewer, ePath);
				if (a != null)
					x.add(a);
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
	protected Action createExportAction(final TableViewer viewer,
			final EMFPath ePath) {
		return new ExportCSVAction() {
			@Override
			protected List<EObject> getObjectsToExport() {
				final EObject root = (EObject) viewer.getInput();
				final Object result = ePath.get(root);
				if (result instanceof List) {
					return (List<EObject>) result;
				} else {
					return Collections.singletonList((EObject) result);
				}
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
	protected Action createImportAction(final TableViewer viewer,
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
			protected void addObjects(final Collection<EObject> newObjects) {
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
						// get the old object
						final EObject oldObject = objectsWithNames.get(newId);
						cc.append(createFixReferencesAndContainments(oldObject,
								newObject));

						// fix references to contained objects as well, or we
						// get a dangling reference

						// now perform replace in the original container
						//TODO should we delete the old object?
						cc.append(ReplaceCommand.create(editingDomain,
								oldObject, Collections.singleton(newObject)));
					} else {
						// just do the add
						cc.append(AddCommand.create(editingDomain, container,
								containerFeature, newObject));
					}
				}

				editingDomain.getCommandStack().execute(cc);

				// editingDomain.getCommandStack().execute(
				// editingDomain.createCommand(
				// AddCommand.class,
				// new CommandParameter(ePath.get(
				// getToplevelObject(), 1), ePath
				// .getPathComponent(0), newObjects)));

				viewer.refresh();
			}

			/**
			 * Fix up any references to oldObject so they refer to newObject,
			 * and fix up any references to singly-contained entries in
			 * oldObject to the analogous entries in newObject, if they exist
			 * 
			 * @param oldObject
			 * @param newObject
			 * @return
			 */
			private Command createFixReferencesAndContainments(
					final EObject oldObject, final EObject newObject) {
				final CompoundCommand cc = new CompoundCommand();
				cc.append(createFixReferences(oldObject, newObject));
				for (final EReference reference : oldObject.eClass()
						.getEAllContainments()) {
					if (reference.isMany() == false) {
						if (newObject.eClass().getEAllContainments()
								.contains(reference)) {
							cc.append(createFixReferencesAndContainments(
									(EObject) oldObject.eGet(reference),
									(EObject) newObject.eGet(reference)));
						}
					}
				}
				return cc;
			}

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
						final int index = ((EList) setting.getEObject().eGet(
								feature)).indexOf(oldObject);

						// multi-references need a remove and an add
						// TODO this will NOT WORK if the old list
						// contains more than one reference
						// to the object being replaced, but at the
						// moment our domain does not do that.
						if (ref.isMany()) {
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
	protected void requestActivation() {
		super.requestActivation();
		part.setCurrentViewerPane(this);
	}

	@Override
	public TableViewer getViewer() {
		return viewer;
	}

	public <T extends ICellManipulator & ICellRenderer> void addTypicalColumn(
			final String columnName, final T manipulatorAndRenderer,
			final Object... path) {
		this.addColumn(columnName, manipulatorAndRenderer,
				manipulatorAndRenderer, path);
	}
}
