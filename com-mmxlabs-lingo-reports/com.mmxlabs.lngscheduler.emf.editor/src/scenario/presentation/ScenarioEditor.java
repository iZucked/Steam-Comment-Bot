/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.eclipse.emf.common.notify.Notifier;
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
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
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
import scenario.contract.ContractModel;
import scenario.contract.ContractPackage;
import scenario.contract.provider.ContractItemProviderAdapterFactory;
import scenario.fleet.FleetPackage;
import scenario.fleet.provider.FleetItemProviderAdapterFactory;
import scenario.market.MarketModel;
import scenario.market.MarketPackage;
import scenario.market.StepwisePriceCurve;
import scenario.market.provider.MarketItemProviderAdapterFactory;
import scenario.optimiser.lso.provider.LsoItemProviderAdapterFactory;
import scenario.optimiser.provider.OptimiserItemProviderAdapterFactory;
import scenario.port.Port;
import scenario.port.PortPackage;
import scenario.port.provider.PortItemProviderAdapterFactory;
import scenario.presentation.ChartViewer.IChartContentProvider;
import scenario.presentation.cargoeditor.autocorrect.AutoCorrector;
import scenario.presentation.cargoeditor.autocorrect.DateLocalisingCorrector;
import scenario.presentation.cargoeditor.autocorrect.SlotIdCorrector;
import scenario.presentation.cargoeditor.autocorrect.SlotVolumeCorrector;
import scenario.presentation.model_editors.CanalEVP;
import scenario.presentation.model_editors.CargoEVP;
import scenario.presentation.model_editors.EntityEVP;
import scenario.presentation.model_editors.IndexEVP;
import scenario.presentation.model_editors.PortEVP;
import scenario.presentation.model_editors.PurchaseContractEVP;
import scenario.presentation.model_editors.SalesContractEVP;
import scenario.presentation.model_editors.VesselClassEVP;
import scenario.presentation.model_editors.VesselEVP;
import scenario.presentation.model_editors.VesselEventEVP;
import scenario.provider.ScenarioItemProviderAdapterFactory;
import scenario.schedule.Schedule;
import scenario.schedule.SchedulePackage;
import scenario.schedule.events.provider.EventsItemProviderAdapterFactory;
import scenario.schedule.fleetallocation.provider.FleetallocationItemProviderAdapterFactory;
import scenario.schedule.provider.ScheduleItemProviderAdapterFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.shiplingo.ui.detailview.base.IReferenceValueProvider;
import com.mmxlabs.shiplingo.ui.detailview.base.IValueProviderProvider;
import com.mmxlabs.shiplingo.ui.detailview.containers.DetailCompositePropertySheetPage;

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
		IViewerProvider, IValueProviderProvider {

	final static EAttribute namedObjectName = ScenarioPackage.eINSTANCE
			.getNamedObject_Name();

	private abstract class ScenarioRVP extends EContentAdapter implements
			IReferenceValueProvider {
		final EAttribute nameAttribute;

		public ScenarioRVP(EAttribute nameAttribute) {
			super();
			this.nameAttribute = nameAttribute;
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

		@Override
		public String getName(final EObject referer,
				final EReference reference, final EObject target) {
			if (target == null)
				return "empty";
			return (String) target.eGet(nameAttribute);
		}

		@Override
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);
			if (!notification.isTouch()
					&& isRelevantTarget(notification.getNotifier(),
							notification.getFeature())) {
				cacheValues();
			}
		}

		protected boolean isRelevantTarget(final Object target,
				final Object feature) {
			return feature.equals(nameAttribute);
		}

		protected abstract void cacheValues();

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getNotifiers(
				EObject referer, EReference feature, EObject referenceValue) {
			if (referenceValue == null)
				return Collections.emptySet();
			return Collections.singleton(new Pair<Notifier, List<Object>>(
					referenceValue, Collections
							.singletonList((Object) nameAttribute)));
		}
	}

	private abstract class SimpleRVP extends ScenarioRVP {
		private List<Pair<String, EObject>> cachedValues = null;
		private final EReference containingReference;

		public SimpleRVP(final EReference containingReference) {
			super(namedObjectName);
			this.containingReference = containingReference;
		}

		public SimpleRVP(final EReference containingReference,
				final EAttribute name) {
			super(name);
			this.containingReference = containingReference;
		}

		@Override
		public List<Pair<String, EObject>> getAllowedValues(EObject target,
				EStructuralFeature field) {
			if (cachedValues == null) {
				install();
				cacheValues();
			}
			return cachedValues;
		}

		protected abstract void install();

		@Override
		protected boolean isRelevantTarget(Object target, Object feature) {
			return (super.isRelevantTarget(target, feature) && (containingReference
					.getEReferenceType().isSuperTypeOf(((EObject) target)
					.eClass())))
					|| feature == containingReference;
		}

		@Override
		protected void cacheValues() {
			cachedValues = getSortedNames(getObjects(), nameAttribute);
			final Pair<String, EObject> none = getEmptyObject();
			if (none != null)
				cachedValues.add(0, none);
		}

		protected Pair<String, EObject> getEmptyObject() {
			return null;
		}

		protected abstract EList<? extends EObject> getObjects();
	}

	final ScenarioRVP scheduleProvider = new SimpleRVP(
			SchedulePackage.eINSTANCE.getScheduleModel_Schedules(),
			SchedulePackage.eINSTANCE.getSchedule_Name()) {

		@Override
		protected void install() {
			getScenario().getScheduleModel().eAdapters().add(this);
		}

		@Override
		public String getName(final EObject referer,
				final EReference reference, final EObject target) {
			if (target == null)
				return "none";
			return ((Schedule) target).getName();
		}

		@Override
		protected Pair<String, EObject> getEmptyObject() {
			return new Pair<String, EObject>("none", null);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getScheduleModel().getSchedules();
		}
	};

	final ScenarioRVP fuelProvider = new SimpleRVP(
			FleetPackage.eINSTANCE.getFleetModel_Fuels()) {
		@Override
		protected void install() {
			getScenario().getFleetModel().eAdapters().add(this);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getFleetModel().getFuels();
		}
	};

	final ScenarioRVP vesselProvider = new SimpleRVP(
			FleetPackage.eINSTANCE.getFleetModel_Fleet()) {
		@Override
		protected void install() {
			getScenario().getFleetModel().eAdapters().add(this);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getFleetModel().getFleet();
		}
	};

	final ScenarioRVP vesselClassProvider = new SimpleRVP(
			FleetPackage.eINSTANCE.getFleetModel_VesselClasses()) {
		@Override
		protected void install() {
			getScenario().getFleetModel().eAdapters().add(this);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getFleetModel().getVesselClasses();
		}
	};

	final ScenarioRVP entityProvider = new SimpleRVP(
			ContractPackage.eINSTANCE.getContractModel_Entities()) {
		@Override
		protected void install() {
			getScenario().getContractModel().eAdapters().add(this);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getContractModel().getEntities();
		}
	};

	final ScenarioRVP portProvider = new SimpleRVP(
			PortPackage.eINSTANCE.getPortModel_Ports()) {
		@Override
		protected void install() {
			getScenario().getPortModel().eAdapters().add(this);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getPortModel().getPorts();
		}
	};

	final ScenarioRVP indexProvider = new SimpleRVP(
			MarketPackage.eINSTANCE.getMarketModel_Indices()) {
		@Override
		protected void install() {
			getScenario().getMarketModel().eAdapters().add(this);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getMarketModel().getIndices();
		}
	};

	final ScenarioRVP contractProvider = new ScenarioRVP(namedObjectName) {
		private List<Pair<String, EObject>> purchaseContracts = null;
		private List<Pair<String, EObject>> salesContracts = null;
		private List<Pair<String, EObject>> allContracts = null;

		protected void install() {
			getScenario().getContractModel().eAdapters().add(this);
		}

		@Override
		public List<Pair<String, EObject>> getAllowedValues(
				final EObject target, final EStructuralFeature field) {
			final Scenario scenario = getScenario();

			if (purchaseContracts == null) {
				install();
				cacheValues();
			}

			if (scenario != null) {
				if (target instanceof LoadSlot) {
					final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>(
							purchaseContracts);
					result.get(0).setFirst(getName(target, null, null));
					return result;
				} else if (target instanceof Slot) {
					final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>(
							salesContracts);

					result.get(0).setFirst(getName(target, null, null));
					return result;
				} else {
					return allContracts;
				}
			}

			return Collections.singletonList(new Pair<String, EObject>("empty",
					null));
		}

		@Override
		public String getName(EObject referer, EReference reference,
				EObject target) {
			if (target == null) {
				if (referer instanceof Slot) {
					final Port port = ((Slot) referer).getPort();
					Contract portContract = null;
					final Scenario scenario = getScenario();
					if (scenario != null) {
						final ContractModel cm = scenario.getContractModel();
						if (cm != null) {
							portContract = cm.getDefaultContract(port);
						}
					}

					if (portContract != null) {
						return portContract.getName() + " [from "
								+ port.getName() + "]";
					} else {
						return "empty";
					}
				} else {
					return "empty";
				}
			} else {
				return (String) target.eGet(nameAttribute);
			}
		}

		@Override
		protected void cacheValues() {
			final Scenario scenario = getScenario();
			salesContracts = getSortedNames(scenario.getContractModel()
					.getSalesContracts(), nameAttribute);
			purchaseContracts = getSortedNames(scenario.getContractModel()
					.getPurchaseContracts(), nameAttribute);
			allContracts = getSortedNames(scenario.getContractModel()
					.getSalesContracts(), nameAttribute);
			allContracts.addAll(purchaseContracts);
			allContracts.add(0, new Pair<String, EObject>("empty", null));
			salesContracts.add(0, new Pair<String, EObject>("empty", null));
			purchaseContracts.add(0, new Pair<String, EObject>("empty", null));
		}

		@Override
		protected boolean isRelevantTarget(Object target, Object feature) {
			return (feature == nameAttribute && target instanceof Contract)
					|| feature == ContractPackage.eINSTANCE
							.getContractModel_PurchaseContracts()
					|| feature == ContractPackage.eINSTANCE
							.getContractModel_SalesContracts();
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getNotifiers(
				EObject referer, EReference feature, EObject referenceValue) {
			if (referenceValue == null && referer instanceof Slot) {
				final Port port = ((Slot) referer).getPort();
				if (port == null)
					return Collections.emptySet();

				final List<Pair<Notifier, List<Object>>> notifiers = new LinkedList<Pair<Notifier, List<Object>>>();
				final Scenario s = getScenario();
				if (s != null) {
					final ContractModel cm = s.getContractModel();
					if (cm != null) {
						final List<Object> features = new LinkedList<Object>();
						features.add(ContractPackage.eINSTANCE
								.getContract_DefaultPorts());
						features.add(namedObjectName);
						for (final Contract c : cm.getPurchaseContracts()) {
							notifiers.add(new Pair<Notifier, List<Object>>(c,
									features));
						}
						for (final Contract c : cm.getSalesContracts()) {
							notifiers.add(new Pair<Notifier, List<Object>>(c,
									features));
						}
					}
				}
				notifiers.add(new Pair<Notifier, List<Object>>(port,
						Collections.singletonList((Object) namedObjectName)));

				return notifiers;
			}
			return super.getNotifiers(referer, feature, referenceValue);
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
	private static List<String> prefixExtensions(List<String> extensions,
			String prefix) {
		List<String> result = new ArrayList<String>();
		for (String extension : extensions) {
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
		public void partActivated(IWorkbenchPart p) {
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

		public void partBroughtToTop(IWorkbenchPart p) {
			// Ignore.
		}

		public void partClosed(IWorkbenchPart p) {
			// Ignore.
		}

		public void partDeactivated(IWorkbenchPart p) {
			// Ignore.
		}

		public void partOpened(IWorkbenchPart p) {
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
		public void notifyChanged(Notification notification) {
			if (notification.getNotifier() instanceof Resource) {
				switch (notification.getFeatureID(Resource.class)) {
				case Resource.RESOURCE__IS_LOADED:
				case Resource.RESOURCE__ERRORS:
				case Resource.RESOURCE__WARNINGS: {
					Resource resource = (Resource) notification.getNotifier();
					Diagnostic diagnostic = analyzeResourceProblems(resource,
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
		protected void setTarget(Resource target) {
			basicSetTarget(target);
		}

		@Override
		protected void unsetTarget(Resource target) {
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
			for (Resource resource : changedResources) {
				if (resource.isLoaded()) {
					resource.unload();
					try {
						resource.load(Collections.EMPTY_MAP);
					} catch (IOException exception) {
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
			BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.OK,
					"com.mmxlabs.lngscheduler.emf.editor", 0, null,
					new Object[] { editingDomain.getResourceSet() });
			for (Diagnostic childDiagnostic : resourceToDiagnosticMap.values()) {
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
				ProblemEditorPart problemEditorPart = new ProblemEditorPart();
				problemEditorPart.setDiagnostic(diagnostic);
				try {
					addPage(++lastEditorPage, problemEditorPart,
							getEditorInput());
					setPageText(lastEditorPage, problemEditorPart.getPartName());
					setActivePage(lastEditorPage);
					showTabs();
				} catch (PartInitException exception) {
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
		BasicCommandStack commandStack = new BasicCommandStack();

		// Add a listener to set the most recent command's affected objects to
		// be the selection of the viewer with focus.
		//
		commandStack.addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(final EventObject event) {
				getContainer().getDisplay().asyncExec(new Runnable() {
					public void run() {
						firePropertyChange(IEditorPart.PROP_DIRTY);

						// Try to select the affected objects.
						//
						Command mostRecentCommand = ((CommandStack) event
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
	protected void firePropertyChange(int action) {
		super.firePropertyChange(action);
	}

	/**
	 * This sets the selection into whichever viewer is active. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSelectionToViewer(Collection<?> collection) {
		final Collection<?> theSelection = collection;
		// Make sure it's okay.
		//
		if (theSelection != null && !theSelection.isEmpty()) {
			Runnable runnable = new Runnable() {
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
	public class ReverseAdapterFactoryContentProvider extends
			AdapterFactoryContentProvider {
		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public ReverseAdapterFactoryContentProvider(
				AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		public Object[] getElements(Object object) {
			Object parent = super.getParent(object);
			return (parent == null ? Collections.EMPTY_SET : Collections
					.singleton(parent)).toArray();
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		public Object[] getChildren(Object object) {
			Object parent = super.getParent(object);
			return (parent == null ? Collections.EMPTY_SET : Collections
					.singleton(parent)).toArray();
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		public boolean hasChildren(Object object) {
			Object parent = super.getParent(object);
			return parent != null;
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		public Object getParent(Object object) {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCurrentViewerPane(ViewerPane viewerPane) {
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
	public void setCurrentViewer(Viewer viewer) {
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
					public void selectionChanged(
							SelectionChangedEvent selectionChangedEvent) {
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
	protected void createContextMenuFor(StructuredViewer viewer) {
		MenuManager contextMenu = new MenuManager("#PopUp");
		contextMenu.add(new Separator("additions"));
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(this);
		Menu menu = contextMenu.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(contextMenu,
				new UnwrappingSelectionProvider(viewer));

		int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
		Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
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
		URI resourceURI = EditUIUtil.getURI(getEditorInput());
		Exception exception = null;
		Resource resource = null;
		try {
			// Load the resource through the editing domain.
			//
			resource = editingDomain.getResourceSet().getResource(resourceURI,
					true);
		} catch (Exception e) {
			exception = e;
			resource = editingDomain.getResourceSet().getResource(resourceURI,
					false);
		}

		Diagnostic diagnostic = analyzeResourceProblems(resource, exception);
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
	public Diagnostic analyzeResourceProblems(Resource resource,
			Exception exception) {
		if (!resource.getErrors().isEmpty()
				|| !resource.getWarnings().isEmpty()) {
			BasicDiagnostic basicDiagnostic = new BasicDiagnostic(
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
			final Scenario scenario = getScenario();
			if (scenario.getCargoModel() != null)
				createCargoEditor(portProvider, contractProvider,
						contractProvider);

			if (scenario.getFleetModel() != null) {
				createFleetEditor(vesselClassProvider, portProvider);

				// TODO events shouldn't be in the fleet model but in another
				// model really
				createEventsEditor();
			}

			if (scenario.getPortModel() != null)
				createPortEditor(contractProvider, indexProvider);

			if (scenario.getMarketModel() != null)
				createIndexEditor();

			if (scenario.getContractModel() != null)
				createContractEditor();

			// add autocorrector

			if (scenario.getScheduleModel() != null)
				createScheduleEditor();

			autoCorrector = new AutoCorrector(getEditingDomain());
			autoCorrector.addCorrector(new SlotVolumeCorrector());
			autoCorrector.addCorrector(new DateLocalisingCorrector());
			autoCorrector.addCorrector(new SlotIdCorrector());

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

	/**
	 * Create an editor for the initial schedule
	 */
	private void createScheduleEditor() {

	}

	private void createContractEditor() {
		final Object input = editingDomain.getResourceSet().getResources()
				.get(0).getContents().get(0);

		final SashForm sash = new SashForm(getContainer(), SWT.HORIZONTAL);
		{
			final EntityEVP entitiesPane = new EntityEVP(getSite().getPage(),
					ScenarioEditor.this);

			entitiesPane.createControl(sash);

			entitiesPane.init(getAdapterFactory(),
					ScenarioPackage.eINSTANCE.getScenario_ContractModel(),
					ContractPackage.eINSTANCE.getContractModel_Entities());

			entitiesPane.setTitle("Entities", getTitleImage());
			entitiesPane.getViewer().setInput(input);
		}
		{
			final SalesContractEVP salesPane = new SalesContractEVP(getSite()
					.getPage(), ScenarioEditor.this);

			salesPane.createControl(sash);

			salesPane
					.init(getAdapterFactory(), ScenarioPackage.eINSTANCE
							.getScenario_ContractModel(),
							ContractPackage.eINSTANCE
									.getContractModel_SalesContracts());

			salesPane.setTitle("Sales Contracts", getTitleImage());
			salesPane.getViewer().setInput(input);
		}
		{
			final PurchaseContractEVP purchasePane = new PurchaseContractEVP(
					getSite().getPage(), ScenarioEditor.this);

			purchasePane.createControl(sash);

			purchasePane.init(getAdapterFactory(), ScenarioPackage.eINSTANCE
					.getScenario_ContractModel(), ContractPackage.eINSTANCE
					.getContractModel_PurchaseContracts());

			purchasePane.setTitle("Purchase Contracts", getTitleImage());
			purchasePane.getViewer().setInput(input);
		}

		setPageText(addPage(sash), "Contracts / Entities");
	}

	private void createEventsEditor() {
		final VesselEventEVP eventsPane = new VesselEventEVP(getSite()
				.getPage(), ScenarioEditor.this);

		eventsPane.createControl(getContainer());
		eventsPane.setTitle("Events", getTitleImage());

		eventsPane.init(getAdapterFactory(),
				ScenarioPackage.eINSTANCE.getScenario_FleetModel(),
				FleetPackage.eINSTANCE.getFleetModel_VesselEvents());

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

		final IndexEVP indices = new IndexEVP(getSite().getPage(), this);

		indices.createControl(sash);
		indices.setTitle("Indices", getTitleImage());

		indices.init(getAdapterFactory(),
				ScenarioPackage.eINSTANCE.getScenario_MarketModel(),
				MarketPackage.eINSTANCE.getMarketModel_Indices());

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

			final CargoEVP cargoPane = new CargoEVP(getSite().getPage(),
					ScenarioEditor.this);

			cargoPane.createControl(getContainer());
			cargoPane.setTitle("Cargoes", getTitleImage());

			cargoPane.init(getAdapterFactory(),
					ScenarioPackage.eINSTANCE.getScenario_CargoModel(),
					CargoPackage.eINSTANCE.getCargoModel_Cargoes());

			cargoPane.getViewer().setInput(
					editingDomain.getResourceSet().getResources().get(0)
							.getContents().get(0));

			// createContextMenuFor(cargoPane.getViewer());

			final int pageIndex = addPage(cargoPane.getControl());
			setPageText(pageIndex, "Cargoes"); // TODO localize this
												// string or whatever
		}
	}

	private void createPortEditor(
			final IReferenceValueProvider everyContractProvider,
			final IReferenceValueProvider marketProvider) {
		final PortEVP portEditor = new PortEVP(getSite().getPage(), this);

		final SashForm sash = new SashForm(getContainer(), SWT.HORIZONTAL);

		portEditor.createControl(sash);

		final List<EReference> path = new LinkedList<EReference>();

		path.add(ScenarioPackage.eINSTANCE.getScenario_PortModel());
		path.add(PortPackage.eINSTANCE.getPortModel_Ports());

		portEditor.setTitle("Ports", getTitleImage());

		portEditor.init(getAdapterFactory(),
				ScenarioPackage.eINSTANCE.getScenario_PortModel(),
				PortPackage.eINSTANCE.getPortModel_Ports());
		portEditor.getViewer().setInput(
				editingDomain.getResourceSet().getResources().get(0)
						.getContents().get(0));

		// createContextMenuFor(portEditor.getViewer());

		final CanalEVP canalEVP = new CanalEVP(getSite().getPage(), this);

		canalEVP.createControl(sash);
		canalEVP.setTitle("Canals", getTitleImage());

		canalEVP.init(getAdapterFactory(),
				ScenarioPackage.eINSTANCE.getScenario_CanalModel(),
				PortPackage.eINSTANCE.getCanalModel_Canals());

		canalEVP.getViewer().setInput(getScenario());
		// createContextMenuFor(canalEVP.getViewer());

		final int pageIndex = addPage(sash);
		setPageText(pageIndex, "Ports and Distances");

	}

	private void createFleetEditor(
			final IReferenceValueProvider vesselClassProvider,
			final IReferenceValueProvider portProvider) {
		{
			final SashForm sash = new SashForm(getContainer(), SWT.HORIZONTAL);

			final VesselClassEVP vcePane = new VesselClassEVP(getSite()
					.getPage(), this);

			vcePane.createControl(sash);

			vcePane.init(getAdapterFactory(),
					ScenarioPackage.eINSTANCE.getScenario_FleetModel(),
					FleetPackage.eINSTANCE.getFleetModel_VesselClasses());

			vcePane.getViewer().setInput(
					editingDomain.getResourceSet().getResources().get(0)
							.getContents().get(0));

			vcePane.setTitle("Vessel Classes", getTitleImage());

			// createContextMenuFor(vcePane.getViewer());

			final VesselEVP fleetPane = new VesselEVP(getSite().getPage(), this);

			fleetPane.createControl(sash);

			fleetPane.init(getAdapterFactory(),
					ScenarioPackage.eINSTANCE.getScenario_FleetModel(),
					FleetPackage.eINSTANCE.getFleetModel_Fleet());

			fleetPane.setTitle("Vessels", getTitleImage());

			fleetPane.getViewer().setInput(
					editingDomain.getResourceSet().getResources().get(0)
							.getContents().get(0));

			// createContextMenuFor(fleetPane.getViewer());

			final int pageIndex = addPage(sash);
			setPageText(pageIndex, "Fleet");
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
				Point point = getContainer().getSize();
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
				Point point = getContainer().getSize();
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
	protected void pageChange(int pageIndex) {
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
	public Object getAdapter(Class key) {
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
				public void createControl(Composite parent) {
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
				public void makeContributions(IMenuManager menuManager,
						IToolBarManager toolBarManager,
						IStatusLineManager statusLineManager) {
					super.makeContributions(menuManager, toolBarManager,
							statusLineManager);
					contentOutlineStatusLineManager = statusLineManager;
				}

				@Override
				public void setActionBars(IActionBars actionBars) {
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
						public void selectionChanged(SelectionChangedEvent event) {
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
		// final EObjectDetailPropertySheetPage page = new
		// EObjectDetailPropertySheetPage(
		// getEditingDomain());
		// setupDetailViewContainer(page);

		final DetailCompositePropertySheetPage page = new DetailCompositePropertySheetPage(
				getEditingDomain(), this);

		return page;
	}
	
	@Override
	public IReferenceValueProvider getValueProvider(
			final EClass c) {
		if (c == PortPackage.eINSTANCE.getPort()) {
			return getPortProvider();
		} else if (c == FleetPackage.eINSTANCE.getVessel()) {
			return getVesselProvider();
		} else if (c == FleetPackage.eINSTANCE.getVesselClass()) {
			return getVesselClassProvider();
		} else if (c == MarketPackage.eINSTANCE.getIndex()) {
			return getIndexProvider();
		} else if (c == ContractPackage.eINSTANCE.getContract()) {
			return getContractProvider();
		} else if (c == ContractPackage.eINSTANCE.getEntity()) {
			return getEntityProvider();
		} else if (c == FleetPackage.eINSTANCE.getVesselFuel()) {
			return fuelProvider;
		} else {
			return null;
		}
	}

//	private class MultiReferenceEditorFactory implements
//			IMultiInlineEditorFactory {
//		private final EditingDomain editingDomain;
//		private final IReferenceValueProvider valueProvider;
//
//		public MultiReferenceEditorFactory(EditingDomain editingDomain,
//				IReferenceValueProvider valueProvider) {
//			super();
//			this.editingDomain = editingDomain;
//			this.valueProvider = valueProvider;
//		}
//
//		@Override
//		public IInlineEditor createEditor(EMFPath path,
//				EStructuralFeature feature, ICommandProcessor commandProcessor) {
//			return new ReferenceInlineEditor(path, feature, editingDomain,
//					commandProcessor, valueProvider);
//		}
//
//		@Override
//		public IInlineEditor createMultiEditor(EMFPath path,
//				EStructuralFeature feature, ICommandProcessor commandProcessor) {
//			return new MultiReferenceInlineEditor(path, feature, editingDomain,
//					commandProcessor, valueProvider);
//		}
//	}

//	/**
//	 * Add editors to any detail view container
//	 * 
//	 * @param page
//	 */
//	public void setupDetailViewContainer(final IDetailViewContainer page) {
//		page.addDefaultEditorFactories();
//
//		page.setEditorFactoryForFeature(
//				PortPackage.eINSTANCE.getPort_DefaultWindowStart(),
//				new IInlineEditorFactory() {
//					@Override
//					public IInlineEditor createEditor(final EMFPath path,
//							final EStructuralFeature feature,
//							final ICommandProcessor commandProcessor) {
//						final List<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
//						for (int i = 0; i < 24; i++) {
//							values.add(new Pair<String, Object>(String.format(
//									"%02d:00", i), i));
//						}
//						return new ValueListInlineEditor(path, feature,
//								editingDomain, commandProcessor, values);
//					}
//				});
//
//		page.setEditorFactoryForFeature(
//				PortPackage.eINSTANCE.getPort_TimeZone(),
//				new IInlineEditorFactory() {
//					@Override
//					public IInlineEditor createEditor(final EMFPath path,
//							final EStructuralFeature feature,
//							final ICommandProcessor commandProcessor) {
//						return new TimezoneInlineEditor(path, feature,
//								editingDomain, commandProcessor);
//					}
//				});
//
//		page.setEditorFactoryForFeature(
//				ScenarioPackage.eINSTANCE.getUUIDObject_UUID(), null);
//
//		page.setEditorFactoryForClassifier(PortPackage.eINSTANCE.getPort(),
//				new MultiReferenceEditorFactory(getEditingDomain(),
//						portProvider));
//
//		page.setEditorFactoryForClassifier(
//				FleetPackage.eINSTANCE.getVesselFuel(),
//				new IInlineEditorFactory() {
//					@Override
//					public IInlineEditor createEditor(final EMFPath path,
//							final EStructuralFeature feature,
//							final ICommandProcessor processor) {
//						return new ReferenceInlineEditor(path, feature,
//								editingDomain, processor, fuelProvider);
//					}
//				});
//
//		page.setEditorFactoryForClassifier(
//				ContractPackage.eINSTANCE.getContract(),
//				new IInlineEditorFactory() {
//					@Override
//					public IInlineEditor createEditor(final EMFPath path,
//							final EStructuralFeature feature,
//							final ICommandProcessor processor) {
//						return new ReferenceInlineEditor(path, feature,
//								editingDomain, processor, contractProvider) {
//							@Override
//							protected boolean updateOnChangeToFeature(
//									final Object changedFeature) {
//								// update contract display when port is changed
//								// on a load slot.
//								return super
//										.updateOnChangeToFeature(changedFeature)
//										|| ((changedFeature instanceof EReference) && ((EReference) changedFeature)
//												.getEReferenceType().equals(
//														PortPackage.eINSTANCE
//																.getPort()));
//							}
//						};
//					}
//				});
//
//		page.setEditorFactoryForClassifier(MarketPackage.eINSTANCE.getIndex(),
//				new IInlineEditorFactory() {
//					@Override
//					public IInlineEditor createEditor(final EMFPath path,
//							final EStructuralFeature feature,
//							final ICommandProcessor processor) {
//						return new ReferenceInlineEditor(path, feature,
//								editingDomain, processor, indexProvider);
//					}
//				});
//
//		page.setEditorFactoryForClassifier(
//				CargoPackage.eINSTANCE.getCargoType(),
//				new IInlineEditorFactory() {
//					@Override
//					public IInlineEditor createEditor(final EMFPath path,
//							final EStructuralFeature feature,
//							final ICommandProcessor processor) {
//						return new EENumInlineEditor(path,
//								(EAttribute) feature, editingDomain, processor);
//					}
//				});
//
//		page.setEditorFactoryForClassifier(
//				ContractPackage.eINSTANCE.getEntity(),
//				new IInlineEditorFactory() {
//					@Override
//					public IInlineEditor createEditor(final EMFPath path,
//							final EStructuralFeature feature,
//							final ICommandProcessor processor) {
//						return new ReferenceInlineEditor(path, feature,
//								editingDomain, processor, entityProvider);
//					}
//				});
//
//		page.setEditorFactoryForFeature(FleetPackage.eINSTANCE
//				.getVesselStateAttributes_FuelConsumptionCurve(),
//				new IMultiInlineEditorFactory() {
//					@Override
//					public IInlineEditor createEditor(final EMFPath path,
//							final EStructuralFeature feature,
//							final ICommandProcessor processor) {
//						return new FuelCurveEditor(path, feature,
//								editingDomain, processor);
//					}
//
//					@Override
//					public IInlineEditor createMultiEditor(EMFPath path,
//							EStructuralFeature feature,
//							ICommandProcessor commandProcessor) {
//						return createEditor(path, feature, commandProcessor);
//					}
//				});
//
//		page.setEditorFactoryForClassifier(FleetPackage.eINSTANCE
//				.getVesselClass(), new MultiReferenceEditorFactory(
//				getEditingDomain(), vesselClassProvider));
//
//		page.setEditorFactoryForFeature(
//				FleetPackage.eINSTANCE.getVesselClass_CanalCosts(),
//				new IMultiInlineEditorFactory() {
//					@Override
//					public IInlineEditor createEditor(EMFPath path,
//							EStructuralFeature feature,
//							ICommandProcessor commandProcessor) {
//						throw new RuntimeException(
//								"There should be no single references to vessel class costs in the model");
//					}
//
//					@Override
//					public IInlineEditor createMultiEditor(EMFPath path,
//							EStructuralFeature feature,
//							ICommandProcessor commandProcessor) {
//						return new VesselClassCostEditor(path, feature,
//								commandProcessor, ScenarioEditor.this);
//					}
//				});
//
//		page.setEditorFactoryForClassifier(FleetPackage.eINSTANCE.getVessel(),
//				new MultiReferenceEditorFactory(getEditingDomain(),
//						vesselProvider));
//
//		page.setEditorFactoryForClassifier(
//				SchedulePackage.eINSTANCE.getSchedule(),
//				new IInlineEditorFactory() {
//					@Override
//					public IInlineEditor createEditor(final EMFPath path,
//							final EStructuralFeature feature,
//							final ICommandProcessor processor) {
//						return new ReferenceInlineEditor(path, feature,
//								editingDomain, processor, scheduleProvider);
//					}
//				});
//
//		page.setEditorFactoryForFeature(
//				PortPackage.eINSTANCE.getCanal_DistanceModel(),
//				new IInlineEditorFactory() {
//					@Override
//					public IInlineEditor createEditor(final EMFPath path,
//							final EStructuralFeature feature,
//							final ICommandProcessor commandProcessor) {
//						return new DialogInlineEditor(path, feature,
//								editingDomain, commandProcessor) {
//							@Override
//							protected String render(Object value) {
//								return "Distance Matrix";
//							}
//
//							@Override
//							protected Object displayDialog(
//									final Object currentValue) {
//								final DistanceModel dm = (DistanceModel) currentValue;
//								final DistanceEditorDialog ded = new DistanceEditorDialog(
//										getShell());
//
//								if (ded.open(ScenarioEditor.this, dm) == Window.OK)
//									return ded.getResult();
//
//								return null;
//							}
//						};
//					}
//				});
//
//		// Disable/Hide Slot ID editors
//		page.setEditorFactoryForFeature(CargoPackage.eINSTANCE.getSlot_Id(),
//				null);
//
//		page.setNameForFeature(CargoPackage.eINSTANCE.getCargo_LoadSlot(),
//				"Load");
//		page.setNameForFeature(CargoPackage.eINSTANCE.getCargo_DischargeSlot(),
//				"Discharge");
//		page.setNameForFeature(
//				CargoPackage.eINSTANCE.getLoadSlot_CargoCVvalue(), "Cargo CV");
//
//		page.setNameForFeature(
//				FleetPackage.eINSTANCE.getVesselStateAttributes_NboRate(),
//				"NBO Rate");
//		page.setNameForFeature(
//				FleetPackage.eINSTANCE.getVesselStateAttributes_IdleNBORate(),
//				"Idle NBO Rate");
//
//		page.setNameForFeature(
//				CargoPackage.eINSTANCE.getCargo_AllowedVessels(), "Restrict To");
//	}

	/**
	 * This deals with how we want selection in the outliner to affect the other
	 * views. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void handleContentOutlineSelection(ISelection selection) {
		if (currentViewerPane != null && !selection.isEmpty()
				&& selection instanceof IStructuredSelection) {
			Iterator<?> selectedElements = ((IStructuredSelection) selection)
					.iterator();
			if (selectedElements.hasNext()) {
				// Get the first selected element.
				//
				Object selectedElement = selectedElements.next();

				// If it's the selection viewer, then we want it to select the
				// same selection as this selection.
				//
				if (currentViewerPane.getViewer() == selectionViewer) {
					ArrayList<Object> selectionList = new ArrayList<Object>();
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
	public void doSave(IProgressMonitor progressMonitor) {
		// Save only resources that have actually changed.
		//
		final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
		saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
				Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);

		// Do the work within an operation because this is a long running
		// activity that modifies the workbench.
		//
		IRunnableWithProgress operation = new IRunnableWithProgress() {
			// This is the method that gets invoked when the operation runs.
			//
			public void run(IProgressMonitor monitor) {
				// Save the resources to the file system.
				//
				boolean first = true;
				for (Resource resource : editingDomain.getResourceSet()
						.getResources()) {
					if ((first || !resource.getContents().isEmpty() || isPersisted(resource))
							&& !editingDomain.isReadOnly(resource)) {
						try {
							long timeStamp = resource.getTimeStamp();
							resource.save(saveOptions);
							if (resource.getTimeStamp() != timeStamp) {
								savedResources.add(resource);
							}
						} catch (Exception exception) {
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
		} catch (Exception exception) {
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
	protected boolean isPersisted(Resource resource) {
		boolean result = false;
		try {
			InputStream stream = editingDomain.getResourceSet()
					.getURIConverter().createInputStream(resource.getURI());
			if (stream != null) {
				result = true;
				stream.close();
			}
		} catch (IOException e) {
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
		String[] filters = FILE_EXTENSION_FILTERS
				.toArray(new String[FILE_EXTENSION_FILTERS.size()]);
		String[] files = LngEditorAdvisor.openFilePathDialog(getSite()
				.getShell(), SWT.SAVE, filters);
		if (files.length > 0) {
			URI uri = URI.createFileURI(files[0]);
			doSaveAs(uri, new URIEditorInput(uri));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void doSaveAs(URI uri, IEditorInput editorInput) {
		(editingDomain.getResourceSet().getResources().get(0)).setURI(uri);
		setInputWithNotify(editorInput);
		setPartName(editorInput.getName());
		IProgressMonitor progressMonitor = getActionBars()
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
	public void init(IEditorSite site, IEditorInput editorInput) {
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
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
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
			ISelectionChangedListener listener) {
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
	public void setSelection(ISelection selection) {
		editorSelection = selection;

		for (ISelectionChangedListener listener : selectionChangedListeners) {
			listener.selectionChanged(new SelectionChangedEvent(this, selection));
		}
		setStatusLineManager(selection);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStatusLineManager(ISelection selection) {
		IStatusLineManager statusLineManager = currentViewer != null
				&& currentViewer == contentOutlineViewer ? contentOutlineStatusLineManager
				: getActionBars().getStatusLineManager();

		if (statusLineManager != null) {
			if (selection instanceof IStructuredSelection) {
				Collection<?> collection = ((IStructuredSelection) selection)
						.toList();
				switch (collection.size()) {
				case 0: {
					statusLineManager
							.setMessage(getString("_UI_NoObjectSelected"));
					break;
				}
				case 1: {
					String text = new AdapterFactoryItemDelegator(
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
	private static String getString(String key) {
		return LngEditorPlugin.INSTANCE.getString(key);
	}

	/**
	 * This looks up a string in plugin.properties, making a substitution. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static String getString(String key, Object s1) {
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
	public void menuAboutToShow(IMenuManager menuManager) {
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

	/**
	 * Get the contract provider in this scenario editor.
	 * 
	 * @return
	 */
	public IReferenceValueProvider getContractProvider() {
		return contractProvider;
	}

	/**
	 * Get the index provider for this scenario editor
	 * 
	 * @return
	 */
	public IReferenceValueProvider getIndexProvider() {
		return indexProvider;
	}

	/**
	 * Get the vessel class provider for this scenario editor.
	 * 
	 * @return
	 */
	public IReferenceValueProvider getVesselClassProvider() {
		return vesselClassProvider;
	}

	/**
	 * @return
	 */
	public IReferenceValueProvider getPortProvider() {
		return portProvider;
	}

	/**
	 * @return
	 */
	public IReferenceValueProvider getVesselProvider() {
		return vesselProvider;
	}

	/**
	 * @return
	 */
	public IReferenceValueProvider getEntityProvider() {
		return entityProvider;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.shiplingo.ui.detailview.base.IValueProviderProvider#getModel(org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public EObject getModel() {
		return getScenario();
	}
}
