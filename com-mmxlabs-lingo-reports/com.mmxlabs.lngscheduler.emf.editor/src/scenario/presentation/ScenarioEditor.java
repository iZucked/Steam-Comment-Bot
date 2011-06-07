/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.common.ui.editor.ProblemEditorPart;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.UnwrappingSelectionProvider;
import org.eclipse.emf.edit.ui.util.EditUIUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.cargo.provider.CargoItemProviderAdapterFactory;
import scenario.contract.Contract;
import scenario.contract.ContractPackage;
import scenario.contract.FixedPricePurchaseContract;
import scenario.contract.IndexPricePurchaseContract;
import scenario.contract.NetbackPurchaseContract;
import scenario.contract.ProfitSharingPurchaseContract;
import scenario.contract.provider.ContractItemProviderAdapterFactory;
import scenario.fleet.FleetPackage;
import scenario.fleet.PortAndTime;
import scenario.fleet.VesselStateAttributes;
import scenario.fleet.provider.FleetItemProviderAdapterFactory;
import scenario.market.MarketModel;
import scenario.market.MarketPackage;
import scenario.market.StepwisePriceCurve;
import scenario.market.provider.MarketItemProviderAdapterFactory;
import scenario.optimiser.lso.provider.LsoItemProviderAdapterFactory;
import scenario.optimiser.provider.OptimiserItemProviderAdapterFactory;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortPackage;
import scenario.port.provider.PortItemProviderAdapterFactory;
import scenario.presentation.ChartViewer.IChartContentProvider;
import scenario.presentation.cargoeditor.BasicAttributeManipulator;
import scenario.presentation.cargoeditor.DateManipulator;
import scenario.presentation.cargoeditor.DialogFeatureManipulator;
import scenario.presentation.cargoeditor.EObjectEditorViewerPane;
import scenario.presentation.cargoeditor.EnumAttributeManipulator;
import scenario.presentation.cargoeditor.IReferenceValueProvider;
import scenario.presentation.cargoeditor.MultipleReferenceManipulator;
import scenario.presentation.cargoeditor.NonEditableColumn;
import scenario.presentation.cargoeditor.NumericAttributeManipulator;
import scenario.presentation.cargoeditor.SingleReferenceManipulator;
import scenario.presentation.cargoeditor.autocorrect.AutoCorrector;
import scenario.presentation.cargoeditor.autocorrect.DateLocalisingCorrector;
import scenario.presentation.cargoeditor.autocorrect.SlotVolumeCorrector;
import scenario.presentation.cargoeditor.detailview.EENumInlineEditor;
import scenario.presentation.cargoeditor.detailview.EObjectDetailDialog;
import scenario.presentation.cargoeditor.detailview.EObjectDetailPropertySheetPage;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.IInlineEditor;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.IInlineEditorFactory;
import scenario.presentation.cargoeditor.detailview.EObjectMultiDialog;
import scenario.presentation.cargoeditor.detailview.FuelCurveEditor;
import scenario.presentation.cargoeditor.detailview.IDetailViewContainer;
import scenario.presentation.cargoeditor.detailview.ReferenceInlineEditor;
import scenario.presentation.cargoeditor.dialogs.PortAndTimeDialog;
import scenario.presentation.cargoeditor.dialogs.VesselStateAttributesDialog;
import scenario.presentation.cargoeditor.handlers.AddAction;
import scenario.presentation.cargoeditor.handlers.SwapDischargeSlotsAction;
import scenario.presentation.cargoeditor.importer.CSVReader;
import scenario.presentation.cargoeditor.importer.DeferredReference;
import scenario.presentation.cargoeditor.importer.EObjectImporter;
import scenario.presentation.cargoeditor.importer.EObjectImporterFactory;
import scenario.presentation.cargoeditor.importer.ExportCSVAction;
import scenario.presentation.cargoeditor.importer.ImportCSVAction;
import scenario.presentation.cargoeditor.importer.NamedObjectRegistry;
import scenario.presentation.cargoeditor.importer.Postprocessor;
import scenario.presentation.cargoeditor.wiringeditor.WiringDialog;
import scenario.provider.ScenarioItemProviderAdapterFactory;
import scenario.schedule.SchedulePackage;
import scenario.schedule.events.provider.EventsItemProviderAdapterFactory;
import scenario.schedule.fleetallocation.provider.FleetallocationItemProviderAdapterFactory;
import scenario.schedule.provider.ScheduleItemProviderAdapterFactory;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * k EObjectEditorViewerPane}, which displays general-purpose reflectively
 * created editing tables for the contents of ELists, and the
 * {@link EObjectDetailPropertySheetPage}, which displays a custom property
 * sheet page for editing those values which are not shown in a table column.
 * The separate editors are displayed in tabs and constructed in the
 * {@link #createPages()} method.
 * 
 * @generated NO
 */
public class ScenarioEditor extends MultiPageEditorPart implements
		IEditingDomainProvider, ISelectionProvider, IMenuListener,
		IViewerProvider {

	final static EAttribute namedObjectName = ScenarioPackage.eINSTANCE
			.getNamedObject_Name();

	/**
	 * This extension of {@link EObjectEditorViewerPane} adds the following
	 * <ol>
	 * <li>A handler which pops up either an {@link EObjectDetailDialog} or an
	 * {@link EObjectMultiDialog} when you hit return</li>
	 * <li>
	 * An add action which displays an {@link EObjectDetailDialog} when you add
	 * a new element</li>
	 * 
	 * @author Tom Hinton
	 * 
	 */
	private class ScenarioObjectEditorViewerPane extends
			EObjectEditorViewerPane {
		/**
		 * @param page
		 * @param part
		 */
		public ScenarioObjectEditorViewerPane(final IWorkbenchPage page,
				final ScenarioEditor part) {
			super(page, part);
		}

		/**
		 * Create a custom add action, which delegates to the default behaviour
		 * to create the object, but adds in an editor dialog.
		 */
		@Override
		protected Action createAddAction(final TableViewer viewer,
				final EditingDomain editingDomain, final EMFPath contentPath) {
			final AddAction delegate = (AddAction) super.createAddAction(
					viewer, editingDomain, contentPath);
			final Action result = new AddAction(editingDomain, contentPath
					.getTargetType().getName()) {
				@Override
				public EObject createObject() {
					final EObject newObject = delegate.createObject();

					final EObjectDetailDialog dialog = new EObjectDetailDialog(
							viewer.getControl().getShell(), SWT.NONE,
							editingDomain);

					ScenarioEditor.this.setupDetailViewContainer(dialog);

					if (dialog.open(Collections.singletonList(newObject))
							.size() > 0) {
						return newObject;
					} else {
						return null;
					}
				}

				@Override
				public Object getOwner() {
					return delegate.getOwner();
				}

				@Override
				public Object getFeature() {
					return delegate.getFeature();
				}
			};
			return result;
		}

		@Override
		/**
		 * Hook a key listener to the viewer to pick up return and display an editor.
		 * 
		 * TODO cache editors for efficiency.
		 */
		public Viewer createViewer(final Composite parent) {
			final Viewer v = super.createViewer(parent);
			v.getControl().addKeyListener(new KeyListener() {
				@Override
				public void keyReleased(final org.eclipse.swt.events.KeyEvent e) {
					
					// TODO: Wrap up in a command with keybindings
					if (e.keyCode == '\r') {
						final ISelection selection = getViewer().getSelection();
						if (selection instanceof IStructuredSelection) {
							final IStructuredSelection ssel = (IStructuredSelection) selection;
							final List l = Arrays.asList(ssel.toArray());

							if (l.size() > 1
									&& (e.stateMask & SWT.CONTROL) == 0) {
								final EObjectMultiDialog multiDialog = new EObjectMultiDialog(
										new IShellProvider() {
											@Override
											public Shell getShell() {
												return v.getControl()
														.getShell();
											}
										});
								ScenarioEditor.this
										.setupDetailViewContainer(multiDialog);
								multiDialog.setEditorFactoryForFeature(
										CargoPackage.eINSTANCE.getCargo_Id(),
										null);
								multiDialog.setEditorFactoryForFeature(
										ScenarioPackage.eINSTANCE
												.getNamedObject_Name(), null);

								multiDialog.setEditorFactoryForFeature(
										FleetPackage.eINSTANCE
												.getVesselEvent_Id(), null);
								if (multiDialog.open(l, getEditingDomain()) == Dialog.OK) {
									getEditingDomain()
											.getCommandStack()
											.execute(
													multiDialog.createCommand());
								}
							} else {
								final EObjectDetailDialog dialog = new EObjectDetailDialog(
										v.getControl().getShell(), SWT.NONE,
										getEditingDomain());

								ScenarioEditor.this
										.setupDetailViewContainer(dialog);

								if (dialog.open(l).size() > 0)
									getViewer().refresh();
							}
						}
					}
				}

				@Override
				public void keyPressed(final org.eclipse.swt.events.KeyEvent e) {
				}
			});
			return v;
		}
	}

	private abstract class ScenarioRVP implements IReferenceValueProvider {
		protected Scenario getEnclosingScenario(final EObject target) {
			return getScenario(); // required so that dialog editor works
			// while (target != null && !(target instanceof Scenario)) {
			// target = target.eContainer();
			// }
			// return (Scenario) target;
		}

		protected ArrayList<Pair<String, EObject>> getSortedNames(
				final EList<? extends EObject> objects,
				final EAttribute nameAttribute) {
			final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();

			for (final EObject object : objects) {
				result.add(new Pair<String, EObject>(object.eGet(nameAttribute)
						.toString(), object));
			}

			Collections.sort(result, new Comparator<Pair<String, ?>>() {
				@Override
				public int compare(final Pair<String, ?> o1,
						final Pair<String, ?> o2) {
					return o1.getFirst().compareTo(o2.getFirst());
				}
			});

			return result;
		}
	}

	final ScenarioRVP scheduleProvider = new ScenarioRVP() {
		@Override
		public List<Pair<String, EObject>> getAllowedValues(
				final EObject target, final EStructuralFeature field) {
			final Scenario scenario = getEnclosingScenario(target);
			final List<Pair<String, EObject>> result = getSortedNames(scenario
					.getScheduleModel().getSchedules(),
					SchedulePackage.eINSTANCE.getSchedule_Name());
			result.add(0, new Pair<String, EObject>("none", null));
			return result;
		}
	};

	final ScenarioRVP fuelProvider = new ScenarioRVP() {
		@Override
		public List<Pair<String, EObject>> getAllowedValues(final EObject target,
				final EStructuralFeature field) {
			return getSortedNames(getEnclosingScenario(target).getFleetModel()
					.getFuels(), namedObjectName);
		}
	};

	final ScenarioRVP vesselProvider = new ScenarioRVP() {
		@Override
		public List<Pair<String, EObject>> getAllowedValues(final EObject target,
				final EStructuralFeature field) {
			final Scenario scenario = getEnclosingScenario(target);
			final List<Pair<String, EObject>> result = getSortedNames(scenario
					.getFleetModel().getFleet(), namedObjectName);
			return result;
		}
	};

	final ScenarioRVP vesselClassProvider = new ScenarioRVP() {
		@Override
		public List<Pair<String, EObject>> getAllowedValues(
				final EObject target, final EStructuralFeature field) {
			final Scenario scenario = getEnclosingScenario(target);
			final List<Pair<String, EObject>> result = getSortedNames(scenario
					.getFleetModel().getVesselClasses(), namedObjectName);
			return result;
		}
	};

	final ScenarioRVP entityProvider = new ScenarioRVP() {
		@Override
		public List<Pair<String, EObject>> getAllowedValues(
				final EObject target, final EStructuralFeature field) {
			final Scenario scenario = getEnclosingScenario(target);
			final List<Pair<String, EObject>> result = getSortedNames(scenario
					.getContractModel().getEntities(), namedObjectName);
			return result;
		}
	};

	final ScenarioRVP portProvider = new ScenarioRVP() {
		@Override
		public List<Pair<String, EObject>> getAllowedValues(
				final EObject target, final EStructuralFeature field) {
			if (target == null)
				return Collections.emptyList();
			final Scenario scenario = getEnclosingScenario(target);
			final List<Pair<String, EObject>> result = getSortedNames(scenario
					.getPortModel().getPorts(), namedObjectName);
			return result;
		}
	};

	final ScenarioRVP contractProvider = new ScenarioRVP() {
		@Override
		public List<Pair<String, EObject>> getAllowedValues(
				final EObject target, final EStructuralFeature field) {
			final String nullValueName;
			final List<Pair<String, EObject>> result;
			final Scenario scenario = getEnclosingScenario(target);

			if (scenario != null) {

				if (target instanceof LoadSlot) {
					// load slots have load contracts
					result = getSortedNames(scenario.getContractModel()
							.getPurchaseContracts(),
							ScenarioPackage.eINSTANCE.getNamedObject_Name());
					final Port port = ((Slot) target).getPort();
					final Contract portContract = port == null ? null : port
							.getDefaultContract();
					if (portContract == null) {
						nullValueName = "empty";
					} else {
						nullValueName = portContract.getName() + " [from "
								+ ((Slot) target).getPort().getName() + "]";
					}
				} else if (target instanceof Slot) {
					// discharge slots have discharge contracts
					result = getSortedNames(scenario.getContractModel()
							.getSalesContracts(),
							ScenarioPackage.eINSTANCE.getNamedObject_Name());
					final Port port = ((Slot) target).getPort();
					final Contract portContract = port == null ? null : port
							.getDefaultContract();
					if (portContract == null) {
						nullValueName = "empty";
					} else {
						nullValueName = portContract.getName() + " [from "
								+ ((Slot) target).getPort().getName() + "]";
					}
				} else {
					// other things have all kinds of contract
					result = getSortedNames(scenario.getContractModel()
							.getSalesContracts(),
							ScenarioPackage.eINSTANCE.getNamedObject_Name());
					result.addAll(getSortedNames(scenario.getContractModel()
							.getPurchaseContracts(), ScenarioPackage.eINSTANCE
							.getNamedObject_Name()));
					nullValueName = "empty";
				}
			} else {
				result = new LinkedList<Pair<String, EObject>>();
				nullValueName = "empty";
			}
			result.add(0, new Pair<String, EObject>(nullValueName, null));

			return result;
		}
	};

	final ScenarioRVP indexProvider = new ScenarioRVP() {
		@Override
		public List<Pair<String, EObject>> getAllowedValues(
				final EObject target, final EStructuralFeature field) {
			final Scenario scenario = getEnclosingScenario(target);
			final List<Pair<String, EObject>> result = getSortedNames(scenario
					.getMarketModel().getIndices(), namedObjectName);
			return result;
		}
	};

	/**
	 * The filters for file extensions supported by the editor. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<String> FILE_EXTENSION_FILTERS = prefixExtensions(
			ScenarioModelWizard.FILE_EXTENSIONS, "*.");

	/**
	 * Returns a new unmodifiable list containing prefixed versions of the
	 * extensions in the given list. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	private static List<String> prefixExtensions(final List<String> extensions,
			final String prefix) {
		final List<String> result = new ArrayList<String>();
		for (final String extension : extensions) {
			result.add(prefix + extension);
		}
		return Collections.unmodifiableList(result);
	}

	/**
	 * This keeps track of the editing domain that is used to track all changes
	 * to the model. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AdapterFactoryEditingDomain editingDomain;

	/**
	 * This is the one adapter factory used for providing views of the model.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ComposedAdapterFactory adapterFactory;

	/**
	 * This is the content outline page. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected IContentOutlinePage contentOutlinePage;

	/**
	 * This is a kludge... <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IStatusLineManager contentOutlineStatusLineManager;

	/**
	 * This is the content outline page's viewer. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected TreeViewer contentOutlineViewer;

	/**
	 * This is the property sheet page. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected PropertySheetPage propertySheetPage;

	/**
	 * This is the viewer that shadows the selection in the content outline. The
	 * parent relation must be correctly defined for this to work. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TreeViewer selectionViewer;

	/**
	 * This inverts the roll of parent and child in the content provider and
	 * show parents as a tree. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TreeViewer parentViewer;

	/**
	 * This shows how a tree view works. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected TreeViewer treeViewer;

	/**
	 * This shows how a list view works. A list viewer doesn't support icons.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ListViewer listViewer;

	/**
	 * This shows how a table view works. A table can be used as a list with
	 * icons. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TableViewer tableViewer;

	/**
	 * This shows how a tree view with columns works. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TreeViewer treeViewerWithColumns;

	/**
	 * This keeps track of the active viewer pane, in the book. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ViewerPane currentViewerPane;

	/**
	 * This keeps track of the active content viewer, which may be either one of
	 * the viewers in the pages or the content outline viewer. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Viewer currentViewer;

	/**
	 * This listens to which ever viewer is active. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected ISelectionChangedListener selectionChangedListener;

	/**
	 * This keeps track of all the
	 * {@link org.eclipse.jface.viewers.ISelectionChangedListener}s that are
	 * listening to this editor. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<ISelectionChangedListener> selectionChangedListeners = new ArrayList<ISelectionChangedListener>();

	/**
	 * This keeps track of the selection of the editor as a whole. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ISelection editorSelection = StructuredSelection.EMPTY;

	/**
	 * This listens for when the outline becomes active <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IPartListener partListener = new IPartListener() {
		@Override
		public void partActivated(final IWorkbenchPart p) {
			if (p instanceof ContentOutline) {
				if (((ContentOutline) p).getCurrentPage() == contentOutlinePage) {
					getActionBarContributor().setActiveEditor(
							ScenarioEditor.this);

					setCurrentViewer(contentOutlineViewer);
				}
			} else if (p instanceof PropertySheet) {
				if (((PropertySheet) p).getCurrentPage() == propertySheetPage) {
					getActionBarContributor().setActiveEditor(
							ScenarioEditor.this);
					handleActivate();
				}
			} else if (p == ScenarioEditor.this) {
				handleActivate();
			}
		}

		@Override
		public void partBroughtToTop(final IWorkbenchPart p) {
			// Ignore.
		}

		@Override
		public void partClosed(final IWorkbenchPart p) {
			// Ignore.
		}

		@Override
		public void partDeactivated(final IWorkbenchPart p) {
			// Ignore.
		}

		@Override
		public void partOpened(final IWorkbenchPart p) {
			// Ignore.
		}
	};

	/**
	 * Resources that have been removed since last activation. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<Resource> removedResources = new ArrayList<Resource>();

	/**
	 * Resources that have been changed since last activation. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<Resource> changedResources = new ArrayList<Resource>();

	/**
	 * Resources that have been saved. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected Collection<Resource> savedResources = new ArrayList<Resource>();

	/**
	 * Map to store the diagnostic associated with a resource. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Map<Resource, Diagnostic> resourceToDiagnosticMap = new LinkedHashMap<Resource, Diagnostic>();

	/**
	 * Controls whether the problem indication should be updated. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected boolean updateProblemIndication = true;

	/**
	 * Adapter used to update the problem indication when resources are demanded
	 * loaded. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EContentAdapter problemIndicationAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final Notification notification) {
			if (notification.getNotifier() instanceof Resource) {
				switch (notification.getFeatureID(Resource.class)) {
				case Resource.RESOURCE__IS_LOADED:
				case Resource.RESOURCE__ERRORS:
				case Resource.RESOURCE__WARNINGS: {
					final Resource resource = (Resource) notification.getNotifier();
					final Diagnostic diagnostic = analyzeResourceProblems(resource,
							null);
					if (diagnostic.getSeverity() != Diagnostic.OK) {
						resourceToDiagnosticMap.put(resource, diagnostic);
					} else {
						resourceToDiagnosticMap.remove(resource);
					}

					if (updateProblemIndication) {
						getSite().getShell().getDisplay()
								.asyncExec(new Runnable() {
									public void run() {
										updateProblemIndication();
									}
								});
					}
					break;
				}
				}
			} else {
				super.notifyChanged(notification);
			}
		}

		@Override
		protected void setTarget(final Resource target) {
			basicSetTarget(target);
		}

		@Override
		protected void unsetTarget(final Resource target) {
			basicUnsetTarget(target);
		}
	};

	private AutoCorrector autoCorrector;

	/**
	 * Handles activation of the editor or it's associated views. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void handleActivate() {
		// Recompute the read only state.
		//
		if (editingDomain.getResourceToReadOnlyMap() != null) {
			editingDomain.getResourceToReadOnlyMap().clear();

			// Refresh any actions that may become enabled or disabled.
			//
			setSelection(getSelection());
		}

		if (!removedResources.isEmpty()) {
			if (handleDirtyConflict()) {
				getSite().getPage().closeEditor(ScenarioEditor.this, false);
			} else {
				removedResources.clear();
				changedResources.clear();
				savedResources.clear();
			}
		} else if (!changedResources.isEmpty()) {
			changedResources.removeAll(savedResources);
			handleChangedResources();
			changedResources.clear();
			savedResources.clear();
		}
	}

	/**
	 * Handles what to do with changed resources on activation. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void handleChangedResources() {
		if (!changedResources.isEmpty()
				&& (!isDirty() || handleDirtyConflict())) {
			if (isDirty()) {
				changedResources.addAll(editingDomain.getResourceSet()
						.getResources());
			}
			editingDomain.getCommandStack().flush();

			updateProblemIndication = false;
			for (final Resource resource : changedResources) {
				if (resource.isLoaded()) {
					resource.unload();
					try {
						resource.load(Collections.EMPTY_MAP);
					} catch (final IOException exception) {
						if (!resourceToDiagnosticMap.containsKey(resource)) {
							resourceToDiagnosticMap
									.put(resource,
											analyzeResourceProblems(resource,
													exception));
						}
					}
				}
			}

			if (AdapterFactoryEditingDomain.isStale(editorSelection)) {
				setSelection(StructuredSelection.EMPTY);
			}

			updateProblemIndication = true;
			updateProblemIndication();
		}
	}

	/**
	 * Updates the problems indication with the information described in the
	 * specified diagnostic. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void updateProblemIndication() {
		if (updateProblemIndication) {
			final BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.OK,
					"com.mmxlabs.lngscheduler.emf.editor", 0, null,
					new Object[] { editingDomain.getResourceSet() });
			for (final Diagnostic childDiagnostic : resourceToDiagnosticMap.values()) {
				if (childDiagnostic.getSeverity() != Diagnostic.OK) {
					diagnostic.add(childDiagnostic);
				}
			}

			int lastEditorPage = getPageCount() - 1;
			if (lastEditorPage >= 0
					&& getEditor(lastEditorPage) instanceof ProblemEditorPart) {
				((ProblemEditorPart) getEditor(lastEditorPage))
						.setDiagnostic(diagnostic);
				if (diagnostic.getSeverity() != Diagnostic.OK) {
					setActivePage(lastEditorPage);
				}
			} else if (diagnostic.getSeverity() != Diagnostic.OK) {
				final ProblemEditorPart problemEditorPart = new ProblemEditorPart();
				problemEditorPart.setDiagnostic(diagnostic);
				try {
					addPage(++lastEditorPage, problemEditorPart,
							getEditorInput());
					setPageText(lastEditorPage, problemEditorPart.getPartName());
					setActivePage(lastEditorPage);
					showTabs();
				} catch (final PartInitException exception) {
					LngEditorPlugin.INSTANCE.log(exception);
				}
			}
		}
	}

	/**
	 * Shows a dialog that asks if conflicting changes should be discarded. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected boolean handleDirtyConflict() {
		return MessageDialog.openQuestion(getSite().getShell(),
				getString("_UI_FileConflict_label"),
				getString("_WARN_FileConflict"));
	}

	/**
	 * This creates a model editor. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public ScenarioEditor() {
		super();
		initializeEditingDomain();
	}

	/**
	 * This sets up the editing domain for the model editor. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void initializeEditingDomain() {
		// Create an adapter factory that yields item providers.
		//
		adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory
				.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new ScenarioItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new FleetItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new ScheduleItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new EventsItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new FleetallocationItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new PortItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new CargoItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new ContractItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new MarketItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new OptimiserItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new LsoItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// Create the command stack that will notify this editor as commands are
		// executed.
		//
		final BasicCommandStack commandStack = new BasicCommandStack();

		// Add a listener to set the most recent command's affected objects to
		// be the selection of the viewer with focus.
		//
		commandStack.addCommandStackListener(new CommandStackListener() {
			@Override
			public void commandStackChanged(final EventObject event) {
				getContainer().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						firePropertyChange(IEditorPart.PROP_DIRTY);

						// Try to select the affected objects.
						//
						final Command mostRecentCommand = ((CommandStack) event
								.getSource()).getMostRecentCommand();
						if (mostRecentCommand != null) {
							setSelectionToViewer(mostRecentCommand
									.getAffectedObjects());
						}
						if (propertySheetPage != null
								&& !propertySheetPage.getControl().isDisposed()) {
							propertySheetPage.refresh();
						}
					}
				});
			}
		});

		// Create the editing domain with a special command stack.
		//
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				commandStack, new HashMap<Resource, Boolean>());
	}

	/**
	 * This is here for the listener to be able to call it. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void firePropertyChange(final int action) {
		super.firePropertyChange(action);
	}

	/**
	 * This sets the selection into whichever viewer is active. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSelectionToViewer(final Collection<?> collection) {
		final Collection<?> theSelection = collection;
		// Make sure it's okay.
		//
		if (theSelection != null && !theSelection.isEmpty()) {
			final Runnable runnable = new Runnable() {
				@Override
				public void run() {
					// Try to select the items in the current content viewer of
					// the editor.
					//
					if (currentViewer != null) {
						currentViewer.setSelection(new StructuredSelection(
								theSelection.toArray()), true);
					}
				}
			};
			getSite().getShell().getDisplay().asyncExec(runnable);
		}
	}

	/**
	 * This returns the editing domain as required by the
	 * {@link IEditingDomainProvider} interface. This is important for
	 * implementing the static methods of {@link AdapterFactoryEditingDomain}
	 * and for supporting {@link org.eclipse.emf.edit.ui.action.CommandAction}.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static class ReverseAdapterFactoryContentProvider extends
			AdapterFactoryContentProvider {
		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public ReverseAdapterFactoryContentProvider(
				final AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		public Object[] getElements(final Object object) {
			final Object parent = super.getParent(object);
			return (parent == null ? Collections.EMPTY_SET : Collections
					.singleton(parent)).toArray();
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		public Object[] getChildren(final Object object) {
			final Object parent = super.getParent(object);
			return (parent == null ? Collections.EMPTY_SET : Collections
					.singleton(parent)).toArray();
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		public boolean hasChildren(final Object object) {
			final Object parent = super.getParent(object);
			return parent != null;
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		public Object getParent(final Object object) {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCurrentViewerPane(final ViewerPane viewerPane) {
		if (currentViewerPane != viewerPane) {
			if (currentViewerPane != null) {
				currentViewerPane.showFocus(false);
			}
			currentViewerPane = viewerPane;
		}
		setCurrentViewer(currentViewerPane.getViewer());
	}

	/**
	 * This makes sure that one content viewer, either for the current page or
	 * the outline view, if it has focus, is the current one. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCurrentViewer(final Viewer viewer) {
		// If it is changing...
		//
		if (currentViewer != viewer) {
			if (selectionChangedListener == null) {
				// Create the listener on demand.
				//
				selectionChangedListener = new ISelectionChangedListener() {
					// This just notifies those things that are affected by the
					// section.
					//
					@Override
					public void selectionChanged(
							final SelectionChangedEvent selectionChangedEvent) {
						setSelection(selectionChangedEvent.getSelection());
					}
				};
			}

			// Stop listening to the old one.
			//
			if (currentViewer != null) {
				currentViewer
						.removeSelectionChangedListener(selectionChangedListener);
			}

			// Start listening to the new one.
			//
			if (viewer != null) {
				viewer.addSelectionChangedListener(selectionChangedListener);
			}

			// Remember it.
			//
			currentViewer = viewer;

			// Set the editors selection based on the current viewer's
			// selection.
			//
			setSelection(currentViewer == null ? StructuredSelection.EMPTY
					: currentViewer.getSelection());
		}
	}

	/**
	 * This returns the viewer as required by the {@link IViewerProvider}
	 * interface. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Viewer getViewer() {
		return currentViewer;
	}

	/**
	 * This creates a context menu for the viewer and adds a listener as well
	 * registering the menu for extension. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void createContextMenuFor(final StructuredViewer viewer) {
		final MenuManager contextMenu = new MenuManager("#PopUp");
		contextMenu.add(new Separator("additions"));
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(this);
		final Menu menu = contextMenu.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(contextMenu,
				new UnwrappingSelectionProvider(viewer));

		final int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
		final Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
		viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(
				viewer));
		viewer.addDropSupport(dndOperations, transfers,
				new EditingDomainViewerDropAdapter(editingDomain, viewer));
	}

	/**
	 * This is the method called to load a resource into the editing domain's
	 * resource set based on the editor's input. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public void createModel() {
		final URI resourceURI = EditUIUtil.getURI(getEditorInput());
		Exception exception = null;
		Resource resource = null;
		try {
			// Load the resource through the editing domain.
			//
			resource = editingDomain.getResourceSet().getResource(resourceURI,
					true);
		} catch (final Exception e) {
			exception = e;
			resource = editingDomain.getResourceSet().getResource(resourceURI,
					false);
		}

		final Diagnostic diagnostic = analyzeResourceProblems(resource, exception);
		if (diagnostic.getSeverity() != Diagnostic.OK) {
			resourceToDiagnosticMap.put(resource,
					analyzeResourceProblems(resource, exception));
		}
		editingDomain.getResourceSet().eAdapters()
				.add(problemIndicationAdapter);
	}

	/**
	 * Returns a diagnostic describing the errors and warnings listed in the
	 * resource and the specified exception (if any). <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Diagnostic analyzeResourceProblems(final Resource resource,
			final Exception exception) {
		if (!resource.getErrors().isEmpty()
				|| !resource.getWarnings().isEmpty()) {
			final BasicDiagnostic basicDiagnostic = new BasicDiagnostic(
					Diagnostic.ERROR,
					"com.mmxlabs.lngscheduler.emf.editor",
					0,
					getString("_UI_CreateModelError_message", resource.getURI()),
					new Object[] { exception == null ? (Object) resource
							: exception });
			basicDiagnostic.merge(EcoreUtil.computeDiagnostic(resource, true));
			return basicDiagnostic;
		} else if (exception != null) {
			return new BasicDiagnostic(Diagnostic.ERROR,
					"com.mmxlabs.lngscheduler.emf.editor", 0, getString(
							"_UI_CreateModelError_message", resource.getURI()),
					new Object[] { exception });
		} else {
			return Diagnostic.OK_INSTANCE;
		}
	}

	/**
	 * This is the method used by the framework to install your own controls.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated not any more
	 */
	@Override
	public void createPages() {
		// Creates the model from the editor input
		//
		createModel();

		// Only creates the other pages if there is something that can be edited
		//
		if (!getEditingDomain().getResourceSet().getResources().isEmpty()) {

			createCargoEditor(portProvider, contractProvider, contractProvider);

			createFleetEditor(vesselClassProvider, portProvider);

			createEventsEditor();

			createPortEditor(contractProvider, indexProvider);

			createIndexEditor();

			createContractEditor();

			// add autocorrector

			autoCorrector = new AutoCorrector(getEditingDomain());
			autoCorrector.addCorrector(new SlotVolumeCorrector());
			autoCorrector.addCorrector(new DateLocalisingCorrector());

			final Scenario s = ((Scenario) (editingDomain.getResourceSet()
					.getResources().get(0).getContents().get(0)));

			s.eAdapters().add(autoCorrector); // TODO dispose listener

			// Create a page for the selection tree view.
			//
			{
				final ViewerPane viewerPane = new ViewerPane(getSite()
						.getPage(), ScenarioEditor.this) {
					@Override
					public Viewer createViewer(final Composite composite) {
						final Tree tree = new Tree(composite, SWT.MULTI);
						final TreeViewer newTreeViewer = new TreeViewer(tree);
						return newTreeViewer;
					}

					@Override
					public void requestActivation() {
						super.requestActivation();
						setCurrentViewerPane(this);
					}
				};
				viewerPane.createControl(getContainer());

				selectionViewer = (TreeViewer) viewerPane.getViewer();
				selectionViewer
						.setContentProvider(new AdapterFactoryContentProvider(
								adapterFactory));

				selectionViewer
						.setLabelProvider(new AdapterFactoryLabelProvider(
								adapterFactory));
				selectionViewer.setInput(editingDomain.getResourceSet());
				selectionViewer.setSelection(new StructuredSelection(
						editingDomain.getResourceSet().getResources().get(0)),
						true);
				viewerPane.setTitle(editingDomain.getResourceSet());

				new AdapterFactoryTreeEditor(selectionViewer.getTree(),
						adapterFactory);

				createContextMenuFor(selectionViewer);
				final int pageIndex = addPage(viewerPane.getControl());
				setPageText(pageIndex, getString("_UI_SelectionPage_label"));
			}

			// Other default ViewerPane bits are commented out
			/*
			 * // Create a page for the parent tree view. // { ViewerPane
			 * viewerPane = new ViewerPane(getSite().getPage(),
			 * ScenarioEditor.this) {
			 * 
			 * @Override public Viewer createViewer(Composite composite) { Tree
			 * tree = new Tree(composite, SWT.MULTI); TreeViewer newTreeViewer =
			 * new TreeViewer(tree); return newTreeViewer; }
			 * 
			 * @Override public void requestActivation() {
			 * super.requestActivation(); setCurrentViewerPane(this); } };
			 * viewerPane.createControl(getContainer());
			 * 
			 * parentViewer = (TreeViewer) viewerPane.getViewer();
			 * parentViewer.setAutoExpandLevel(30); parentViewer
			 * .setContentProvider(new ReverseAdapterFactoryContentProvider(
			 * adapterFactory)); parentViewer.setLabelProvider(new
			 * AdapterFactoryLabelProvider( adapterFactory));
			 * 
			 * createContextMenuFor(parentViewer); int pageIndex =
			 * addPage(viewerPane.getControl()); setPageText(pageIndex,
			 * getString("_UI_ParentPage_label")); }
			 * 
			 * // This is the page for the list viewer // { ViewerPane
			 * viewerPane = new ViewerPane(getSite().getPage(),
			 * ScenarioEditor.this) {
			 * 
			 * @Override public Viewer createViewer(Composite composite) {
			 * return new ListViewer(composite); }
			 * 
			 * @Override public void requestActivation() {
			 * super.requestActivation(); setCurrentViewerPane(this); } };
			 * viewerPane.createControl(getContainer()); listViewer =
			 * (ListViewer) viewerPane.getViewer(); listViewer
			 * .setContentProvider(new AdapterFactoryContentProvider(
			 * adapterFactory)); listViewer.setLabelProvider(new
			 * AdapterFactoryLabelProvider( adapterFactory));
			 * 
			 * createContextMenuFor(listViewer); int pageIndex =
			 * addPage(viewerPane.getControl()); setPageText(pageIndex,
			 * getString("_UI_ListPage_label")); }
			 * 
			 * // This is the page for the tree viewer // { ViewerPane
			 * viewerPane = new ViewerPane(getSite().getPage(),
			 * ScenarioEditor.this) {
			 * 
			 * @Override public Viewer createViewer(Composite composite) {
			 * return new TreeViewer(composite); }
			 * 
			 * @Override public void requestActivation() {
			 * super.requestActivation(); setCurrentViewerPane(this); } };
			 * viewerPane.createControl(getContainer()); treeViewer =
			 * (TreeViewer) viewerPane.getViewer(); treeViewer
			 * .setContentProvider(new AdapterFactoryContentProvider(
			 * adapterFactory)); treeViewer.setLabelProvider(new
			 * AdapterFactoryLabelProvider( adapterFactory));
			 * 
			 * new AdapterFactoryTreeEditor(treeViewer.getTree(),
			 * adapterFactory);
			 * 
			 * createContextMenuFor(treeViewer); int pageIndex =
			 * addPage(viewerPane.getControl()); setPageText(pageIndex,
			 * getString("_UI_TreePage_label")); }
			 * 
			 * // This is the page for the table viewer. // { ViewerPane
			 * viewerPane = new ViewerPane(getSite().getPage(),
			 * ScenarioEditor.this) {
			 * 
			 * @Override public Viewer createViewer(Composite composite) {
			 * return new TableViewer(composite); }
			 * 
			 * @Override public void requestActivation() {
			 * super.requestActivation(); setCurrentViewerPane(this); } };
			 * viewerPane.createControl(getContainer()); tableViewer =
			 * (TableViewer) viewerPane.getViewer();
			 * 
			 * Table table = tableViewer.getTable(); TableLayout layout = new
			 * TableLayout(); table.setLayout(layout);
			 * table.setHeaderVisible(true); table.setLinesVisible(true);
			 * 
			 * TableColumn objectColumn = new TableColumn(table, SWT.NONE);
			 * layout.addColumnData(new ColumnWeightData(3, 100, true));
			 * objectColumn.setText(getString("_UI_ObjectColumn_label"));
			 * objectColumn.setResizable(true);
			 * 
			 * TableColumn selfColumn = new TableColumn(table, SWT.NONE);
			 * layout.addColumnData(new ColumnWeightData(2, 100, true));
			 * selfColumn.setText(getString("_UI_SelfColumn_label"));
			 * selfColumn.setResizable(true);
			 * 
			 * tableViewer.setColumnProperties(new String[] { "a", "b" });
			 * tableViewer .setContentProvider(new
			 * AdapterFactoryContentProvider( adapterFactory));
			 * tableViewer.setLabelProvider(new AdapterFactoryLabelProvider(
			 * adapterFactory));
			 * 
			 * createContextMenuFor(tableViewer); int pageIndex =
			 * addPage(viewerPane.getControl()); setPageText(pageIndex,
			 * getString("_UI_TablePage_label")); }
			 * 
			 * // This is the page for the table tree viewer. // { ViewerPane
			 * viewerPane = new ViewerPane(getSite().getPage(),
			 * ScenarioEditor.this) {
			 * 
			 * @Override public Viewer createViewer(Composite composite) {
			 * return new TreeViewer(composite); }
			 * 
			 * @Override public void requestActivation() {
			 * super.requestActivation(); setCurrentViewerPane(this); } };
			 * viewerPane.createControl(getContainer());
			 * 
			 * treeViewerWithColumns = (TreeViewer) viewerPane.getViewer();
			 * 
			 * Tree tree = treeViewerWithColumns.getTree();
			 * tree.setLayoutData(new FillLayout());
			 * tree.setHeaderVisible(true); tree.setLinesVisible(true);
			 * 
			 * TreeColumn objectColumn = new TreeColumn(tree, SWT.NONE);
			 * objectColumn.setText(getString("_UI_ObjectColumn_label"));
			 * objectColumn.setResizable(true); objectColumn.setWidth(250);
			 * 
			 * TreeColumn selfColumn = new TreeColumn(tree, SWT.NONE);
			 * selfColumn.setText(getString("_UI_SelfColumn_label"));
			 * selfColumn.setResizable(true); selfColumn.setWidth(200);
			 * 
			 * treeViewerWithColumns.setColumnProperties(new String[] { "a", "b"
			 * }); treeViewerWithColumns .setContentProvider(new
			 * AdapterFactoryContentProvider( adapterFactory));
			 * treeViewerWithColumns .setLabelProvider(new
			 * AdapterFactoryLabelProvider( adapterFactory));
			 * 
			 * createContextMenuFor(treeViewerWithColumns); int pageIndex =
			 * addPage(viewerPane.getControl()); setPageText(pageIndex,
			 * getString("_UI_TreeWithColumnsPage_label")); }
			 */

			getSite().getShell().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					setActivePage(0);
				}
			});
		}

		// Ensures that this editor will only display the page's tab
		// area if there are more than one page
		//
		getContainer().addControlListener(new ControlAdapter() {
			boolean guard = false;

			@Override
			public void controlResized(final ControlEvent event) {
				if (!guard) {
					guard = true;
					hideTabs();
					guard = false;
				}
			}
		});

		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				updateProblemIndication();
			}
		});
	}

	private void createContractEditor() {
		final Object input = editingDomain.getResourceSet().getResources()
				.get(0).getContents().get(0);

		final SashForm sash = new SashForm(getContainer(), SWT.HORIZONTAL);
		{
			final EObjectEditorViewerPane entitiesPane = new ScenarioObjectEditorViewerPane(
					getSite().getPage(), ScenarioEditor.this);

			entitiesPane.createControl(sash);
			entitiesPane.init(CollectionsUtil.makeArrayList(
					ScenarioPackage.eINSTANCE.getScenario_ContractModel(),
					ContractPackage.eINSTANCE.getContractModel_Entities()),
					getAdapterFactory());

			entitiesPane.addTypicalColumn(
					"Name",
					new BasicAttributeManipulator(ScenarioPackage.eINSTANCE
							.getNamedObject_Name(), getEditingDomain()));

			entitiesPane.addTypicalColumn(
					"Tax Rate",
					new NumericAttributeManipulator(ContractPackage.eINSTANCE
							.getEntity_TaxRate(), getEditingDomain()));

			entitiesPane.addTypicalColumn(
					"Ownership",
					new NumericAttributeManipulator(ContractPackage.eINSTANCE
							.getEntity_Ownership(), getEditingDomain()));

			entitiesPane.setTitle("Entities", getTitleImage());
			entitiesPane.getViewer().setInput(input);
		}
		{
			final EObjectEditorViewerPane salesPane = new ScenarioObjectEditorViewerPane(
					getSite().getPage(), ScenarioEditor.this);

			salesPane.createControl(sash);

			salesPane.init(
					CollectionsUtil.makeArrayList(ScenarioPackage.eINSTANCE
							.getScenario_ContractModel(),
							ContractPackage.eINSTANCE
									.getContractModel_SalesContracts()),
					getAdapterFactory());

			salesPane.addTypicalColumn("Name", new BasicAttributeManipulator(
					ScenarioPackage.eINSTANCE.getNamedObject_Name(),
					getEditingDomain()));

			salesPane.addTypicalColumn(
					"Entity",
					new SingleReferenceManipulator(ContractPackage.eINSTANCE
							.getContract_Entity(), entityProvider,
							getEditingDomain()));

			salesPane.addTypicalColumn("Index", new SingleReferenceManipulator(
					ContractPackage.eINSTANCE.getSalesContract_Index(),
					indexProvider, getEditingDomain()));

			// salesPane.addTypicalColumn(
			// "Regas Efficiency",
			// new NumericAttributeManipulator(ContractPackage.eINSTANCE
			// .getSalesContract_RegasEfficiency(),
			// getEditingDomain()));

			salesPane.addTypicalColumn(
					"Sales Mark-up",
					new NumericAttributeManipulator(ContractPackage.eINSTANCE
							.getSalesContract_Markup(), getEditingDomain()));

			salesPane.setTitle("Sales Contracts", getTitleImage());
			salesPane.getViewer().setInput(input);
		}
		{
			final EObjectEditorViewerPane purchasePane = new ScenarioObjectEditorViewerPane(
					getSite().getPage(), ScenarioEditor.this);

			purchasePane.createControl(sash);

			purchasePane.init(CollectionsUtil.makeArrayList(
					ScenarioPackage.eINSTANCE.getScenario_ContractModel(),
					ContractPackage.eINSTANCE
							.getContractModel_PurchaseContracts()),
					getAdapterFactory());

			purchasePane.addTypicalColumn("Type", new NonEditableColumn() {
				@Override
				public String render(final Object object) {
					if (object instanceof IndexPricePurchaseContract) {
						return "Index Price";
					} else if (object instanceof FixedPricePurchaseContract) {
						return "Fixed Price";
					} else if (object instanceof NetbackPurchaseContract) {
						return "Netback";
					} else if (object instanceof ProfitSharingPurchaseContract) {
						return "Profit-sharing";
					} else {
						return object.getClass().getSimpleName();
					}
				}
			});

			purchasePane.addTypicalColumn(
					"Name",
					new BasicAttributeManipulator(ScenarioPackage.eINSTANCE
							.getNamedObject_Name(), getEditingDomain()));

			purchasePane.addTypicalColumn(
					"Entity",
					new SingleReferenceManipulator(ContractPackage.eINSTANCE
							.getContract_Entity(), entityProvider,
							getEditingDomain()));

			purchasePane.setTitle("Purchase Contracts", getTitleImage());
			purchasePane.getViewer().setInput(input);
		}

		setPageText(addPage(sash), "Contracts / Entities");
	}

	private void createEventsEditor() {
		final EObjectEditorViewerPane eventsPane = new ScenarioObjectEditorViewerPane(
				getSite().getPage(), ScenarioEditor.this);

		eventsPane.createControl(getContainer());

		final List<EReference> path = new LinkedList<EReference>();

		path.add(ScenarioPackage.eINSTANCE.getScenario_FleetModel());
		path.add(FleetPackage.eINSTANCE.getFleetModel_VesselEvents());

		eventsPane.setTitle("Events", getTitleImage());

		eventsPane.init(path, adapterFactory);

		final FleetPackage fp = FleetPackage.eINSTANCE;

		final NonEditableColumn type = new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof EObject) {
					return ((EObject) object).eClass().getName();
				} else {
					return object.getClass().getSimpleName();
				}
			}
		};

		eventsPane.addTypicalColumn("Event Type", type);

		final BasicAttributeManipulator id = new BasicAttributeManipulator(
				fp.getVesselEvent_Id(), getEditingDomain());

		eventsPane.addColumn("ID", id, id);

		final DateManipulator start = new DateManipulator(
				fp.getVesselEvent_StartDate(), editingDomain);
		eventsPane.addColumn("Start Date", start, start);

		final DateManipulator end = new DateManipulator(
				fp.getVesselEvent_EndDate(), editingDomain);
		eventsPane.addColumn("End Date", end, end);

		final NumericAttributeManipulator duration = new NumericAttributeManipulator(
				fp.getVesselEvent_Duration(), editingDomain);
		eventsPane.addColumn("Duration (days)", duration, duration);

		final SingleReferenceManipulator port = new SingleReferenceManipulator(
				fp.getVesselEvent_Port(), portProvider, editingDomain);
		eventsPane.addColumn("Port", port, port);

		final MultipleReferenceManipulator vessels = new MultipleReferenceManipulator(
				fp.getVesselEvent_Vessels(), editingDomain, vesselProvider,
				namedObjectName);

		eventsPane.addColumn("Vessels", vessels, vessels);

		final MultipleReferenceManipulator classes = new MultipleReferenceManipulator(
				fp.getVesselEvent_VesselClasses(), editingDomain,
				vesselClassProvider, namedObjectName);

		eventsPane.addColumn("Classes", classes, classes);

		eventsPane.getViewer().setInput(
				editingDomain.getResourceSet().getResources().get(0)
						.getContents().get(0));

		// TODO should this really be here?
		createContextMenuFor(eventsPane.getViewer());

		final int pageIndex = addPage(eventsPane.getControl());
		setPageText(pageIndex, "Events");
	}

	private void createIndexEditor() {
		final SashForm sash = new SashForm(getContainer(), SWT.SMOOTH
				| SWT.HORIZONTAL);

		final EObjectEditorViewerPane indices = new ScenarioObjectEditorViewerPane(
				getSite().getPage(), this);

		indices.createControl(sash);
		indices.setTitle("Indices", getTitleImage());

		final List<EReference> path2 = new LinkedList<EReference>();

		path2.add(ScenarioPackage.eINSTANCE.getScenario_MarketModel());
		path2.add(MarketPackage.eINSTANCE.getMarketModel_Indices());

		indices.init(path2, getAdapterFactory());

		final BasicAttributeManipulator name = new BasicAttributeManipulator(
				ScenarioPackage.eINSTANCE.getNamedObject_Name(),
				getEditingDomain());

		indices.addColumn("Name", name, name);

		final NumericAttributeManipulator defaultValue = new NumericAttributeManipulator(
				MarketPackage.eINSTANCE.getStepwisePriceCurve_DefaultValue(),
				getEditingDomain());

		indices.addColumn("Default Value", defaultValue, defaultValue,
				MarketPackage.eINSTANCE.getIndex_PriceCurve());

		indices.getViewer().setInput(
				editingDomain.getResourceSet().getResources().get(0)
						.getContents().get(0));

		final ChartViewer chart = new ChartViewer(sash);

		// TODO this is a hack
		final MarketModel mm = ((Scenario) (editingDomain.getResourceSet()
				.getResources().get(0).getContents().get(0))).getMarketModel();

		mm.eAdapters().add(new EContentAdapter() {
			{
				final EContentAdapter eca = this;
				getContainer().addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(final DisposeEvent e) {
						mm.eAdapters().remove(eca);
					}
				});
			}

			@Override
			public void notifyChanged(final Notification notification) {
				super.notifyChanged(notification);
				if (notification.isTouch() == false)
					chart.refresh();
			}
		});

		chart.setContentProvider(new IChartContentProvider() {
			@Override
			public boolean isDateSeries(final int i) {
				return true;
			}

			@Override
			public double[] getYSeries(final int i) {
				final StepwisePriceCurve curve = mm.getIndices().get(i)
						.getPriceCurve();
				final double[] answer = new double[curve.getPrices().size()];
				for (int j = 0; j < answer.length; j++) {
					answer[j] = curve.getPrices().get(j).getPriceFromDate();
				}
				return answer;
			}

			@Override
			public double[] getXSeries(final int i) {
				return null;
			}

			@Override
			public String getSeriesName(final int i) {
				return mm.getIndices().get(i).getName();
			}

			@Override
			public int getSeriesCount() {
				// return 1;
				return mm.getIndices().size();
			}

			@Override
			public Date[] getDateXSeries(final int i) {
				final StepwisePriceCurve curve = mm.getIndices().get(i)
						.getPriceCurve();
				final Date[] answer = new Date[curve.getPrices().size()];
				for (int j = 0; j < curve.getPrices().size(); j++) {
					answer[j] = curve.getPrices().get(j).getDate();
				}

				return answer;
			}
		});

		setPageText(addPage(sash), "Indices");
	}

	public Scenario getScenario() {
		return (Scenario) editingDomain.getResourceSet().getResources().get(0)
				.getContents().get(0);
	}

	private void createCargoEditor(final IReferenceValueProvider portProvider,
			final IReferenceValueProvider loadContractProvider,
			final IReferenceValueProvider dischargeContractProvider) {
		// Create a page for the cargo editor
		{

			final EObjectEditorViewerPane cargoPane = new ScenarioObjectEditorViewerPane(
					getSite().getPage(), ScenarioEditor.this) {

				final SwapDischargeSlotsAction swapAction = new SwapDischargeSlotsAction();

				@Override
				public Viewer createViewer(final Composite parent) {
					final Viewer v = super.createViewer(parent);

					getToolBarManager().appendToGroup("edit", swapAction);
					getToolBarManager().update(true);

					v.addSelectionChangedListener(swapAction);

					v.getControl().addKeyListener(new KeyListener() {
						@Override
						public void keyPressed(final KeyEvent e) {
						}

						@Override
						public void keyReleased(final KeyEvent e) {

							if (e.keyCode == ' ') {

								final ISelection selection = v.getSelection();
								if (selection instanceof IStructuredSelection) {
									final IStructuredSelection ssel = (IStructuredSelection) selection;
									final List l = Arrays.asList(ssel.toArray());

									final WiringDialog wiringDialog = new WiringDialog(
											v.getControl().getShell());

									wiringDialog.open(l, getEditingDomain());
								}
							}
						}
					});

					return v;
				}

				@Override
				public void dispose() {

					getViewer().removeSelectionChangedListener(swapAction);

					super.dispose();
				}

			};
			// cargoPane.createControl(getContainer());

			final CargoPackage cargoPackage = CargoPackage.eINSTANCE;

			cargoPane.createControl(getContainer());

			final List<EReference> path = new LinkedList<EReference>();

			path.add(ScenarioPackage.eINSTANCE.getScenario_CargoModel());
			path.add(CargoPackage.eINSTANCE.getCargoModel_Cargoes());

			cargoPane.setTitle("Cargoes", getTitleImage());

			cargoPane.init(path, adapterFactory);

			{
				final BasicAttributeManipulator id = new BasicAttributeManipulator(
						cargoPackage.getCargo_Id(), getEditingDomain());
				cargoPane.addColumn("ID", id, id);
			}

			{
				final EnumAttributeManipulator type = new EnumAttributeManipulator(
						cargoPackage.getCargo_CargoType(), getEditingDomain());
				cargoPane.addColumn("Type", type, type);
			}

			{
				final SingleReferenceManipulator port = new SingleReferenceManipulator(
						cargoPackage.getSlot_Port(), portProvider,
						getEditingDomain());
				cargoPane.addColumn("Load Port", port, port,
						cargoPackage.getCargo_LoadSlot());
			}

			{
				final DateManipulator date = new DateManipulator(
						cargoPackage.getSlot_WindowStart(), getEditingDomain());
				cargoPane.addColumn("Load Date", date, date,
						cargoPackage.getCargo_LoadSlot());
			}

			{
				final SingleReferenceManipulator port = new SingleReferenceManipulator(
						cargoPackage.getSlot_Contract(), loadContractProvider,
						getEditingDomain());

				cargoPane.addColumn("Load Contract", port, port,
						cargoPackage.getCargo_LoadSlot());
			}

			{
				final SingleReferenceManipulator port = new SingleReferenceManipulator(
						cargoPackage.getSlot_Port(), portProvider,
						getEditingDomain());
				cargoPane.addColumn("Discharge Port", port, port,
						cargoPackage.getCargo_DischargeSlot());
			}
			{
				final DateManipulator date = new DateManipulator(
						cargoPackage.getSlot_WindowStart(), getEditingDomain());
				cargoPane.addColumn("Discharge Date", date, date,
						cargoPackage.getCargo_DischargeSlot());
			}

			{
				final SingleReferenceManipulator port = new SingleReferenceManipulator(
						cargoPackage.getSlot_Contract(), loadContractProvider,
						getEditingDomain());
				cargoPane.addColumn("Discharge Contract", port, port,
						cargoPackage.getCargo_DischargeSlot());
			}

			// TODO sort out initial vessel column
			cargoPane.getViewer().setInput(
					editingDomain.getResourceSet().getResources().get(0)
							.getContents().get(0));

			// TODO should this really be here?
			createContextMenuFor(cargoPane.getViewer());

			final int pageIndex = addPage(cargoPane.getControl());
			setPageText(pageIndex, "Cargoes"); // TODO localize this
												// string or whatever
		}
	}

	private void createPortEditor(
			final IReferenceValueProvider everyContractProvider,
			final IReferenceValueProvider marketProvider) {
		final EObjectEditorViewerPane portsPane = new ScenarioObjectEditorViewerPane(
				getSite().getPage(), ScenarioEditor.this) {

			// override import and export to do distance matrix along with
			// ports.

			@Override
			protected Action createExportAction(final TableViewer viewer,
					final EMFPath ePath) {
				final Action exportPortsAction = super.createExportAction(viewer, ePath);
				final Action exportDistanceModelAction = new ExportCSVAction() {
					@Override
					public void run() {
						exportPortsAction.run();
						super.run();
					}

					@Override
					protected List<EObject> getObjectsToExport() {
						return Collections.singletonList((EObject) getScenario().getDistanceModel());
					}
					
					@Override
					protected EClass getExportEClass() {
						return PortPackage.eINSTANCE.getDistanceModel();
					}
				};
				return exportDistanceModelAction;
			}

			@Override
			protected Action createImportAction(final TableViewer viewer,
					final EditingDomain editingDomain, final EMFPath ePath) {
				final ImportCSVAction delegate = (ImportCSVAction) super.createImportAction(viewer, editingDomain, ePath);
				return new ImportCSVAction() {
					@Override
					public void run() {
						delegate.run();
						super.run();
					}

					@Override
					protected EObject getToplevelObject() {
						return getScenario();
					}
					
					@Override
					protected EClass getImportClass() {
						return PortPackage.eINSTANCE.getDistanceModel();
					}
					
					@Override
					public void addObjects(final Collection<EObject> newObjects) {
						getScenario().setDistanceModel((DistanceModel) newObjects.iterator().next());
					}
				};
			}

		};

		final SashForm sash = new SashForm(getContainer(), SWT.VERTICAL);

		portsPane.createControl(sash);

		final List<EReference> path = new LinkedList<EReference>();

		path.add(ScenarioPackage.eINSTANCE.getScenario_PortModel());
		path.add(PortPackage.eINSTANCE.getPortModel_Ports());

		portsPane.setTitle("Ports", getTitleImage());

		portsPane.init(path, adapterFactory);
		final PortPackage pp = PortPackage.eINSTANCE;
		{
			BasicAttributeManipulator manipulator = new BasicAttributeManipulator(
					namedObjectName, getEditingDomain());
			portsPane.addColumn("Name", manipulator, manipulator);
			manipulator = new BasicAttributeManipulator(pp.getPort_TimeZone(),
					getEditingDomain());
			portsPane.addColumn("Timezone", manipulator, manipulator);

			final SingleReferenceManipulator mm = new SingleReferenceManipulator(
					pp.getPort_DefaultIndex(), marketProvider,
					getEditingDomain());

			portsPane.addColumn("Default Index", mm, mm);

			final SingleReferenceManipulator cm = new SingleReferenceManipulator(
					pp.getPort_DefaultContract(), everyContractProvider,
					getEditingDomain());

			portsPane.addColumn("Default Contract", cm, cm);

			portsPane.addTypicalColumn(
					"Regas Efficiency",
					new NumericAttributeManipulator(PortPackage.eINSTANCE
							.getPort_RegasEfficiency(), getEditingDomain()));
		}

		portsPane.getViewer().setInput(
				editingDomain.getResourceSet().getResources().get(0)
						.getContents().get(0));

		createContextMenuFor(portsPane.getViewer());

		{
			final EObjectEditorViewerPane canalEVP = new ScenarioObjectEditorViewerPane(
					getSite().getPage(), ScenarioEditor.this);

			canalEVP.createControl(sash);
			canalEVP.setTitle("Canals", getTitleImage());

			final List<EReference> p2 = new LinkedList<EReference>();

			p2.add(ScenarioPackage.eINSTANCE.getScenario_CanalModel());
			p2.add(PortPackage.eINSTANCE.getCanalModel_Canals());

			canalEVP.init(p2, adapterFactory);

			canalEVP.addTypicalColumn("Name", new BasicAttributeManipulator(
					namedObjectName, getEditingDomain()));
			
			canalEVP.getViewer().setInput(getScenario());
			createContextMenuFor(canalEVP.getViewer());
		}

		final int pageIndex = addPage(sash);
		setPageText(pageIndex, "Ports and Distances");

	}

	private void createFleetEditor(
			final IReferenceValueProvider vesselClassProvider,
			final IReferenceValueProvider portProvider) {
		{
			final SashForm sash = new SashForm(getContainer(), SWT.VERTICAL);

			final EObjectEditorViewerPane vcePane = new ScenarioObjectEditorViewerPane(
					getSite().getPage(), ScenarioEditor.this) {

				/**
				 * Because vessel classes are split into two files we need some
				 * custom code here to ask the user to select both.
				 * 
				 * For now it just displays two open dialogs, one for each file
				 * and then runs two import sessions. The second import session
				 * is customized to hook up fuel consumption curves.
				 */
				@Override
				protected Action createImportAction(final TableViewer viewer,
						final EditingDomain editingDomain, final EMFPath ePath) {
					final ImportCSVAction delegate = (ImportCSVAction) super
							.createImportAction(viewer, editingDomain, ePath);
					return new ImportCSVAction() {
						@Override
						public void run() {
							try {
								final FileDialog dialog = new FileDialog(
										PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow()
												.getShell(), SWT.OPEN);

								dialog.setFilterExtensions(new String[] { "*.csv" });
								dialog.setText("Select the vessel class file");
								final String vcFile = dialog.open();
								if (vcFile == null)
									return;
								dialog.setText("Now select the fuel curves file");
								final String fcFile = dialog.open();
								if (fcFile == null)
									return;

								final ArrayList<DeferredReference> defer = new ArrayList<DeferredReference>();
								final NamedObjectRegistry reg = new NamedObjectRegistry();
								reg.addEObjects((EObject) viewer.getInput());

								final EObjectImporter vci = EObjectImporterFactory
										.getInstance().getImporter(
												FleetPackage.eINSTANCE
														.getVesselClass());

								final CSVReader reader = new CSVReader(vcFile);
								final Collection<EObject> vesselClasses = vci
										.importObjects(reader, defer, reg);

								// register new objects
								for (final EObject object : vesselClasses) {
									reg.addEObject(object);
								}
								// add stuff to scenario and re-register names

								final EObjectImporter fcImporter = EObjectImporterFactory
										.getInstance()
										.getImporter(
												FleetPackage.eINSTANCE
														.getFuelConsumptionLine());

								final CSVReader reader2 = new CSVReader(fcFile);
								fcImporter.importObjects(reader2, defer, reg);

								for (final DeferredReference r : defer) {
									r.setRegistry(reg.getContents());
									r.run();
								}

								for (final EObject object : vesselClasses) {
									Postprocessor.getInstance().postprocess(
											object);
								}

								delegate.addObjects(vesselClasses);

							} catch (final IOException ex) {

							}
						}

						/*
						 * Because I've over-ridden the run method up there,
						 * these methods can safely return null.
						 */

						@Override
						protected EObject getToplevelObject() {
							return null;
						}

						@Override
						public void addObjects(final Collection<EObject> newObjects) {

						}

						@Override
						protected EClass getImportClass() {
							return null;
						}
					};
				}
			};

			vcePane.createControl(sash);

			final List<EReference> path2 = new LinkedList<EReference>();

			path2.add(ScenarioPackage.eINSTANCE.getScenario_FleetModel());
			path2.add(FleetPackage.eINSTANCE.getFleetModel_VesselClasses());

			vcePane.init(path2, adapterFactory);
			{
				final BasicAttributeManipulator name = new BasicAttributeManipulator(
						namedObjectName, getEditingDomain());
				vcePane.addColumn("Name", name, name);
			}
			{
				final BasicAttributeManipulator capacity = new NumericAttributeManipulator(
						FleetPackage.eINSTANCE.getVesselClass_Capacity(),
						getEditingDomain());
				vcePane.addColumn("Capacity", capacity, capacity);
			}

			{
				final MultipleReferenceManipulator capacity = new MultipleReferenceManipulator(
						FleetPackage.eINSTANCE
								.getVesselClass_InaccessiblePorts(),
						getEditingDomain(), portProvider, namedObjectName);
				vcePane.addColumn("Inaccessible Ports", capacity, capacity);
			}

			{
				final DialogFeatureManipulator laden = new DialogFeatureManipulator(
						FleetPackage.eINSTANCE.getVesselClass_LadenAttributes(),
						getEditingDomain()) {
					@Override
					protected String renderValue(final Object object) {
						final VesselStateAttributes a = (VesselStateAttributes) object;
						return "NBO: " + a.getNboRate() + " Idle NBO: "
								+ a.getIdleNBORate() + " Idle Base:"
								+ a.getIdleConsumptionRate();
					}

					@Override
					protected Object openDialogBox(
							final Control cellEditorWindow, final Object object) {
						final VesselStateAttributesDialog dlg = new VesselStateAttributesDialog(
								cellEditorWindow.getShell(),
								(SWT.DIALOG_TRIM & ~SWT.CLOSE)
										| SWT.APPLICATION_MODAL);

						return dlg
								.open((VesselStateAttributes) getValue(object));
					}

				};
				vcePane.addColumn("Laden Fuel Usage", laden, laden);
			}

			{
				final DialogFeatureManipulator laden = new DialogFeatureManipulator(
						FleetPackage.eINSTANCE
								.getVesselClass_BallastAttributes(),
						getEditingDomain()) {

					@Override
					protected Object openDialogBox(
							final Control cellEditorWindow, final Object object) {
						final VesselStateAttributesDialog dlg = new VesselStateAttributesDialog(
								cellEditorWindow.getShell(),
								(SWT.DIALOG_TRIM & ~SWT.CLOSE)
										| SWT.APPLICATION_MODAL);

						return dlg
								.open((VesselStateAttributes) getValue(object));
					}

					@Override
					protected String renderValue(final Object object) {
						final VesselStateAttributes a = (VesselStateAttributes) object;
						return "NBO: " + a.getNboRate() + " Idle NBO: "
								+ a.getIdleNBORate() + " Idle Base:"
								+ a.getIdleConsumptionRate();
					}

				};
				vcePane.addColumn("Ballast Fuel Usage", laden, laden);
			}

			vcePane.getViewer().setInput(
					editingDomain.getResourceSet().getResources().get(0)
							.getContents().get(0));

			vcePane.setTitle("Vessel Classes", getTitleImage());

			createContextMenuFor(vcePane.getViewer());

			final EObjectEditorViewerPane fleetPane = new ScenarioObjectEditorViewerPane(
					getSite().getPage(), ScenarioEditor.this);

			fleetPane.createControl(sash);
			// fleetPane.getControl().setLayoutData(new GridData(SWT.FILL,
			// SWT.FILL, true, true));

			final List<EReference> path = new LinkedList<EReference>();

			path.add(ScenarioPackage.eINSTANCE.getScenario_FleetModel());
			path.add(FleetPackage.eINSTANCE.getFleetModel_Fleet());

			fleetPane.init(path, adapterFactory);
			{
				final BasicAttributeManipulator name = new BasicAttributeManipulator(
						namedObjectName, getEditingDomain());
				fleetPane.addColumn("Name", name, name);
			}

			{
				final SingleReferenceManipulator vclass = new SingleReferenceManipulator(
						FleetPackage.eINSTANCE.getVessel_Class(),
						vesselClassProvider, getEditingDomain());
				fleetPane.addColumn("Class", vclass, vclass);
			}

			{
				class RequirementFeatureManipulator extends
						DialogFeatureManipulator {

					public RequirementFeatureManipulator(
							final EStructuralFeature field,
							final EditingDomain editingDomain) {
						super(field, editingDomain);
					}

					@Override
					protected String renderValue(final Object value) {
						final PortAndTime pat = (PortAndTime) value;
						if (pat == null) {
							return "No constraint";
						}
						return (pat.isSetPort() ? pat.getPort().getName()
								: "Anywhere")
								+ " "
								+ "from "
								+ (pat.isSetStartTime() ? pat.getStartTime()
										.toString() : "any time")
								+ " "
								+ "to "
								+ (pat.isSetEndTime() ? pat.getEndTime()
										.toString() : "any time");
					}

					@Override
					protected Object openDialogBox(
							final Control cellEditorWindow, final Object object) {
						final PortAndTimeDialog patDialog = new PortAndTimeDialog(
								cellEditorWindow.getShell(),
								(SWT.DIALOG_TRIM & ~SWT.CLOSE)
										| SWT.APPLICATION_MODAL);

						return patDialog.open((PortAndTime) getValue(object));
					}
				}
				final DialogFeatureManipulator startRequirement = new RequirementFeatureManipulator(
						FleetPackage.eINSTANCE.getVessel_StartRequirement(),
						getEditingDomain());

				fleetPane.addColumn("Start constraint", startRequirement,
						startRequirement);

				final DialogFeatureManipulator endRequirement = new RequirementFeatureManipulator(
						FleetPackage.eINSTANCE.getVessel_EndRequirement(),
						getEditingDomain());

				fleetPane.addColumn("Start constraint", endRequirement,
						endRequirement);
			}

			// TODO add other desired vessel columns here

			fleetPane.setTitle("Vessels", getTitleImage());

			fleetPane.getViewer().setInput(
					editingDomain.getResourceSet().getResources().get(0)
							.getContents().get(0));

			createContextMenuFor(fleetPane.getViewer());

			final int pageIndex = addPage(sash);
			setPageText(pageIndex, "Fleet"); // TODO localize this
			// string or whatever
		}
	}

	/**
	 * If there is just one page in the multi-page editor part, this hides the
	 * single tab at the bottom. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void hideTabs() {
		if (getPageCount() <= 1) {
			setPageText(0, "");
			if (getContainer() instanceof CTabFolder) {
				((CTabFolder) getContainer()).setTabHeight(1);
				final Point point = getContainer().getSize();
				getContainer().setSize(point.x, point.y + 6);
			}
		}
	}

	/**
	 * If there is more than one page in the multi-page editor part, this shows
	 * the tabs at the bottom. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void showTabs() {
		if (getPageCount() > 1) {
			setPageText(0, getString("_UI_SelectionPage_label"));
			if (getContainer() instanceof CTabFolder) {
				((CTabFolder) getContainer()).setTabHeight(SWT.DEFAULT);
				final Point point = getContainer().getSize();
				getContainer().setSize(point.x, point.y - 6);
			}
		}
	}

	/**
	 * This is used to track the active viewer. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void pageChange(final int pageIndex) {
		super.pageChange(pageIndex);

		if (contentOutlinePage != null) {
			handleContentOutlineSelection(contentOutlinePage.getSelection());
		}
	}

	/**
	 * This is how the framework determines which interfaces we implement. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class key) {
		if (key.equals(IContentOutlinePage.class)) {
			return showOutlineView() ? getContentOutlinePage() : null;
		} else if (key.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
		} else {
			return super.getAdapter(key);
		}
	}

	/**
	 * This accesses a cached version of the content outliner. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IContentOutlinePage getContentOutlinePage() {
		if (contentOutlinePage == null) {
			// The content outline is just a tree.
			//
			class MyContentOutlinePage extends ContentOutlinePage {
				@Override
				public void createControl(final Composite parent) {
					super.createControl(parent);
					contentOutlineViewer = getTreeViewer();
					contentOutlineViewer.addSelectionChangedListener(this);

					// Set up the tree viewer.
					//
					contentOutlineViewer
							.setContentProvider(new AdapterFactoryContentProvider(
									adapterFactory));
					contentOutlineViewer
							.setLabelProvider(new AdapterFactoryLabelProvider(
									adapterFactory));
					contentOutlineViewer.setInput(editingDomain
							.getResourceSet());

					// Make sure our popups work.
					//
					createContextMenuFor(contentOutlineViewer);

					if (!editingDomain.getResourceSet().getResources()
							.isEmpty()) {
						// Select the root object in the view.
						//
						contentOutlineViewer
								.setSelection(new StructuredSelection(
										editingDomain.getResourceSet()
												.getResources().get(0)), true);
					}
				}

				@Override
				public void makeContributions(final IMenuManager menuManager,
						final IToolBarManager toolBarManager,
						final IStatusLineManager statusLineManager) {
					super.makeContributions(menuManager, toolBarManager,
							statusLineManager);
					contentOutlineStatusLineManager = statusLineManager;
				}

				@Override
				public void setActionBars(final IActionBars actionBars) {
					super.setActionBars(actionBars);
					getActionBarContributor().shareGlobalActions(this,
							actionBars);
				}
			}

			contentOutlinePage = new MyContentOutlinePage();

			// Listen to selection so that we can handle it is a special way.
			//
			contentOutlinePage
					.addSelectionChangedListener(new ISelectionChangedListener() {
						// This ensures that we handle selections correctly.
						//
						public void selectionChanged(final SelectionChangedEvent event) {
							handleContentOutlineSelection(event.getSelection());
						}
					});
		}

		return contentOutlinePage;
	}

	/**
	 * This accesses a cached version of the property sheet. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated NO
	 */
	public IPropertySheetPage getPropertySheetPage() {
		final EObjectDetailPropertySheetPage page = new EObjectDetailPropertySheetPage(
				getEditingDomain());
		setupDetailViewContainer(page);
		return page;
	}

	/**
	 * Add editors to any detail view container
	 * 
	 * @param page
	 */
	private void setupDetailViewContainer(final IDetailViewContainer page) {
		page.addDefaultEditorFactories();
		page.setEditorFactoryForClassifier(PortPackage.eINSTANCE.getPort(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(final EMFPath path,
							final EStructuralFeature feature,
							final ICommandProcessor processor) {
						return new ReferenceInlineEditor(path, feature,
								editingDomain, processor, portProvider);
					}
				});

		page.setEditorFactoryForClassifier(
				FleetPackage.eINSTANCE.getVesselFuel(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(final EMFPath path,
							final EStructuralFeature feature,
							final ICommandProcessor processor) {
						return new ReferenceInlineEditor(path, feature,
								editingDomain, processor, fuelProvider);
					}
				});

		page.setEditorFactoryForClassifier(
				ContractPackage.eINSTANCE.getContract(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(final EMFPath path,
							final EStructuralFeature feature,
							final ICommandProcessor processor) {
						return new ReferenceInlineEditor(path, feature,
								editingDomain, processor, contractProvider) {
							@Override
							protected boolean updateOnChangeToFeature(
									final Object changedFeature) {
								// update contract display when port is changed
								// on a load slot.
								return super
										.updateOnChangeToFeature(changedFeature)
										|| ((changedFeature instanceof EReference) && ((EReference) changedFeature)
												.getEReferenceType().equals(
														PortPackage.eINSTANCE
																.getPort()));
							}
						};
					}
				});

		page.setEditorFactoryForClassifier(MarketPackage.eINSTANCE.getIndex(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(final EMFPath path,
							final EStructuralFeature feature,
							final ICommandProcessor processor) {
						return new ReferenceInlineEditor(path, feature,
								editingDomain, processor, indexProvider);
					}
				});

		page.setEditorFactoryForClassifier(
				CargoPackage.eINSTANCE.getCargoType(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(final EMFPath path,
							final EStructuralFeature feature,
							final ICommandProcessor processor) {
						return new EENumInlineEditor(path,
								(EAttribute) feature, editingDomain, processor);
					}
				});

		page.setEditorFactoryForClassifier(
				ContractPackage.eINSTANCE.getEntity(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(final EMFPath path,
							final EStructuralFeature feature,
							final ICommandProcessor processor) {
						return new ReferenceInlineEditor(path, feature,
								editingDomain, processor, entityProvider);
					}
				});

		page.setEditorFactoryForFeature(FleetPackage.eINSTANCE
				.getVesselStateAttributes_FuelConsumptionCurve(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(final EMFPath path,
							final EStructuralFeature feature,
							final ICommandProcessor processor) {
						return new FuelCurveEditor(path, feature,
								editingDomain, processor);
					}
				});

		page.setEditorFactoryForClassifier(
				FleetPackage.eINSTANCE.getVesselClass(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(final EMFPath path,
							final EStructuralFeature feature,
							final ICommandProcessor processor) {
						return new ReferenceInlineEditor(path, feature,
								editingDomain, processor, vesselClassProvider);
					}
				});

		page.setEditorFactoryForClassifier(
				SchedulePackage.eINSTANCE.getSchedule(),
				new IInlineEditorFactory() {
					@Override
					public IInlineEditor createEditor(final EMFPath path,
							final EStructuralFeature feature,
							final ICommandProcessor processor) {
						return new ReferenceInlineEditor(path, feature,
								editingDomain, processor, scheduleProvider);
					}
				});

		// Disable/Hide Slot ID editors
		page.setEditorFactoryForFeature(CargoPackage.eINSTANCE.getSlot_Id(),
				null);

		page.setNameForFeature(CargoPackage.eINSTANCE.getCargo_LoadSlot(),
				"Load");
		page.setNameForFeature(CargoPackage.eINSTANCE.getCargo_DischargeSlot(),
				"Discharge");
		page.setNameForFeature(
				CargoPackage.eINSTANCE.getLoadSlot_CargoCVvalue(), "Cargo CV");
		
		page.setNameForFeature(FleetPackage.eINSTANCE.getVesselStateAttributes_NboRate(), "NBO Rate");
		page.setNameForFeature(FleetPackage.eINSTANCE.getVesselStateAttributes_IdleNBORate(), "Idle NBO Rate");
	}

	/**
	 * This deals with how we want selection in the outliner to affect the other
	 * views. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void handleContentOutlineSelection(final ISelection selection) {
		if (currentViewerPane != null && !selection.isEmpty()
				&& selection instanceof IStructuredSelection) {
			final Iterator<?> selectedElements = ((IStructuredSelection) selection)
					.iterator();
			if (selectedElements.hasNext()) {
				// Get the first selected element.
				//
				final Object selectedElement = selectedElements.next();

				// If it's the selection viewer, then we want it to select the
				// same selection as this selection.
				//
				if (currentViewerPane.getViewer() == selectionViewer) {
					final ArrayList<Object> selectionList = new ArrayList<Object>();
					selectionList.add(selectedElement);
					while (selectedElements.hasNext()) {
						selectionList.add(selectedElements.next());
					}

					// Set the selection to the widget.
					//
					selectionViewer.setSelection(new StructuredSelection(
							selectionList));
				} else {
					// Set the input to the widget.
					//
					if (currentViewerPane.getViewer().getInput() != selectedElement) {
						currentViewerPane.getViewer().setInput(selectedElement);
						currentViewerPane.setTitle(selectedElement);
					}
				}
			}
		}
	}

	/**
	 * This is for implementing {@link IEditorPart} and simply tests the command
	 * stack. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isDirty() {
		return ((BasicCommandStack) editingDomain.getCommandStack())
				.isSaveNeeded();
	}

	/**
	 * This is for implementing {@link IEditorPart} and simply saves the model
	 * file. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void doSave(final IProgressMonitor progressMonitor) {
		// Save only resources that have actually changed.
		//
		final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
		saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
				Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);

		// Do the work within an operation because this is a long running
		// activity that modifies the workbench.
		//
		final IRunnableWithProgress operation = new IRunnableWithProgress() {
			// This is the method that gets invoked when the operation runs.
			//
			public void run(final IProgressMonitor monitor) {
				// Save the resources to the file system.
				//
				boolean first = true;
				for (final Resource resource : editingDomain.getResourceSet()
						.getResources()) {
					if ((first || !resource.getContents().isEmpty() || isPersisted(resource))
							&& !editingDomain.isReadOnly(resource)) {
						try {
							final long timeStamp = resource.getTimeStamp();
							resource.save(saveOptions);
							if (resource.getTimeStamp() != timeStamp) {
								savedResources.add(resource);
							}
						} catch (final Exception exception) {
							resourceToDiagnosticMap
									.put(resource,
											analyzeResourceProblems(resource,
													exception));
						}
						first = false;
					}
				}
			}
		};

		updateProblemIndication = false;
		try {
			// This runs the options, and shows progress.
			//
			new ProgressMonitorDialog(getSite().getShell()).run(true, false,
					operation);

			// Refresh the necessary state.
			//
			((BasicCommandStack) editingDomain.getCommandStack()).saveIsDone();
			firePropertyChange(IEditorPart.PROP_DIRTY);
		} catch (final Exception exception) {
			// Something went wrong that shouldn't.
			//
			LngEditorPlugin.INSTANCE.log(exception);
		}
		updateProblemIndication = true;
		updateProblemIndication();
	}

	/**
	 * This returns whether something has been persisted to the URI of the
	 * specified resource. The implementation uses the URI converter from the
	 * editor's resource set to try to open an input stream. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected boolean isPersisted(final Resource resource) {
		boolean result = false;
		try {
			final InputStream stream = editingDomain.getResourceSet()
					.getURIConverter().createInputStream(resource.getURI());
			if (stream != null) {
				result = true;
				stream.close();
			}
		} catch (final IOException e) {
			// Ignore
		}
		return result;
	}

	/**
	 * This always returns true because it is not currently supported. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * This also changes the editor's input. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void doSaveAs() {
		final String[] filters = FILE_EXTENSION_FILTERS
				.toArray(new String[FILE_EXTENSION_FILTERS.size()]);
		final String[] files = LngEditorAdvisor.openFilePathDialog(getSite()
				.getShell(), SWT.SAVE, filters);
		if (files.length > 0) {
			final URI uri = URI.createFileURI(files[0]);
			doSaveAs(uri, new URIEditorInput(uri));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void doSaveAs(final URI uri, final IEditorInput editorInput) {
		(editingDomain.getResourceSet().getResources().get(0)).setURI(uri);
		setInputWithNotify(editorInput);
		setPartName(editorInput.getName());
		final IProgressMonitor progressMonitor = getActionBars()
				.getStatusLineManager() != null ? getActionBars()
				.getStatusLineManager().getProgressMonitor()
				: new NullProgressMonitor();
		doSave(progressMonitor);
	}

	/**
	 * This is called during startup. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	@Override
	public void init(final IEditorSite site, final IEditorInput editorInput) {
		setSite(site);
		setInputWithNotify(editorInput);
		setPartName(editorInput.getName());
		site.setSelectionProvider(this);
		site.getPage().addPartListener(partListener);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setFocus() {
		if (currentViewerPane != null) {
			currentViewerPane.setFocus();
		} else {
			getControl(getActivePage()).setFocus();
		}
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider}.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void addSelectionChangedListener(final ISelectionChangedListener listener) {
		selectionChangedListeners.add(listener);
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider}.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void removeSelectionChangedListener(
			final ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider} to
	 * return this editor's overall selection. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ISelection getSelection() {
		return editorSelection;
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider} to
	 * set this editor's overall selection. Calling this result will notify the
	 * listeners. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setSelection(final ISelection selection) {
		editorSelection = selection;

		for (final ISelectionChangedListener listener : selectionChangedListeners) {
			listener.selectionChanged(new SelectionChangedEvent(this, selection));
		}
		setStatusLineManager(selection);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStatusLineManager(final ISelection selection) {
		final IStatusLineManager statusLineManager = currentViewer != null
				&& currentViewer == contentOutlineViewer ? contentOutlineStatusLineManager
				: getActionBars().getStatusLineManager();

		if (statusLineManager != null) {
			if (selection instanceof IStructuredSelection) {
				final Collection<?> collection = ((IStructuredSelection) selection)
						.toList();
				switch (collection.size()) {
				case 0: {
					statusLineManager
							.setMessage(getString("_UI_NoObjectSelected"));
					break;
				}
				case 1: {
					final String text = new AdapterFactoryItemDelegator(
							adapterFactory).getText(collection.iterator()
							.next());
					statusLineManager.setMessage(getString(
							"_UI_SingleObjectSelected", text));
					break;
				}
				default: {
					statusLineManager.setMessage(getString(
							"_UI_MultiObjectSelected",
							Integer.toString(collection.size())));
					break;
				}
				}
			} else {
				statusLineManager.setMessage("");
			}
		}
	}

	/**
	 * This looks up a string in the plugin's plugin.properties file. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static String getString(final String key) {
		return LngEditorPlugin.INSTANCE.getString(key);
	}

	/**
	 * This looks up a string in plugin.properties, making a substitution. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static String getString(final String key, final Object s1) {
		return LngEditorPlugin.INSTANCE.getString(key, new Object[] { s1 });
	}

	/**
	 * This implements {@link org.eclipse.jface.action.IMenuListener} to help
	 * fill the context menus with contributions from the Edit menu. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void menuAboutToShow(final IMenuManager menuManager) {
		((IMenuListener) getEditorSite().getActionBarContributor())
				.menuAboutToShow(menuManager);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EditingDomainActionBarContributor getActionBarContributor() {
		return (EditingDomainActionBarContributor) getEditorSite()
				.getActionBarContributor();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IActionBars getActionBars() {
		return getActionBarContributor().getActionBars();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void dispose() {
		updateProblemIndication = false;

		getSite().getPage().removePartListener(partListener);

		adapterFactory.dispose();

		if (getActionBarContributor().getActiveEditor() == this) {
			getActionBarContributor().setActiveEditor(null);
		}

		if (propertySheetPage != null) {
			propertySheetPage.dispose();
		}

		if (contentOutlinePage != null) {
			contentOutlinePage.dispose();
		}

		super.dispose();
	}

	/**
	 * Returns whether the outline view should be presented to the user. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected boolean showOutlineView() {
		return true;
	}
}
