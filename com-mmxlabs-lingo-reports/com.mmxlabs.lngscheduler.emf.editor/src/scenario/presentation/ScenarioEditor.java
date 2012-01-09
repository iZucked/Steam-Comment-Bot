/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IResource;
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
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
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
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import scenario.contract.ContractModel;
import scenario.contract.ContractPackage;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;
import scenario.contract.provider.ContractItemProviderAdapterFactory;
import scenario.fleet.Drydock;
import scenario.fleet.FleetPackage;
import scenario.fleet.provider.FleetItemProviderAdapterFactory;
import scenario.market.MarketModel;
import scenario.market.MarketPackage;
import scenario.market.StepwisePriceCurve;
import scenario.market.provider.MarketItemProviderAdapterFactory;
import scenario.optimiser.lso.provider.LsoItemProviderAdapterFactory;
import scenario.optimiser.provider.OptimiserItemProviderAdapterFactory;
import scenario.port.Port;
import scenario.port.PortCapability;
import scenario.port.PortPackage;
import scenario.port.provider.PortItemProviderAdapterFactory;
import scenario.presentation.ChartViewer.IChartContentProvider;
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
import scenario.presentation.rvps.ScenarioRVP;
import scenario.presentation.rvps.SimpleRVP;
import scenario.provider.ScenarioItemProviderAdapterFactory;
import scenario.schedule.Schedule;
import scenario.schedule.SchedulePackage;
import scenario.schedule.events.provider.EventsItemProviderAdapterFactory;
import scenario.schedule.fleetallocation.provider.FleetallocationItemProviderAdapterFactory;
import scenario.schedule.provider.ScheduleItemProviderAdapterFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.shiplingo.ui.autocorrector.AutoCorrector;
import com.mmxlabs.shiplingo.ui.autocorrector.DateLocalisingCorrector;
import com.mmxlabs.shiplingo.ui.autocorrector.SlotIdCorrector;
import com.mmxlabs.shiplingo.ui.autocorrector.SlotVolumeCorrector;
import com.mmxlabs.shiplingo.ui.detailview.base.IReferenceValueProvider;
import com.mmxlabs.shiplingo.ui.detailview.base.IValueProviderProvider;
import com.mmxlabs.shiplingo.ui.detailview.containers.DetailCompositePropertySheetPage;

/**
 * k EObjectEditorViewerPane}, which displays general-purpose reflectively created editing tables for the contents of ELists, and the {@link EObjectDetailPropertySheetPage}, which displays a custom
 * property sheet page for editing those values which are not shown in a table column. The separate editors are displayed in tabs and constructed in the {@link #createPages()} method.
 * 
 * @generated NO
 */
public class ScenarioEditor extends MultiPageEditorPart implements IEditingDomainProvider, ISelectionProvider, IMenuListener, IViewerProvider, IValueProviderProvider, IEclipseJobManagerListener,
		IJobControlListener {

	final static EAttribute namedObjectName = ScenarioPackage.eINSTANCE.getNamedObject_Name();

	final ScenarioRVP scheduleProvider = new SimpleRVP(SchedulePackage.eINSTANCE.getScheduleModel_Schedules(), SchedulePackage.eINSTANCE.getSchedule_Name()) {

		@Override
		protected void install() {
			getScenario().getScheduleModel().eAdapters().add(this);
		}

		@Override
		public String getName(final EObject referer, final EReference reference, final EObject target) {
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

	final ScenarioRVP fuelProvider = new SimpleRVP(FleetPackage.eINSTANCE.getFleetModel_Fuels()) {
		@Override
		protected void install() {
			getScenario().getFleetModel().eAdapters().add(this);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getFleetModel().getFuels();
		}
	};

	final ScenarioRVP vesselProvider = new SimpleRVP(FleetPackage.eINSTANCE.getFleetModel_Fleet()) {
		@Override
		protected void install() {
			getScenario().getFleetModel().eAdapters().add(this);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getFleetModel().getFleet();
		}
	};

	final ScenarioRVP vesselClassProvider = new SimpleRVP(FleetPackage.eINSTANCE.getFleetModel_VesselClasses()) {
		@Override
		protected void install() {
			getScenario().getFleetModel().eAdapters().add(this);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getFleetModel().getVesselClasses();
		}
	};

	final ScenarioRVP entityProvider = new SimpleRVP(ContractPackage.eINSTANCE.getContractModel_Entities()) {
		@Override
		protected void install() {
			getScenario().getContractModel().eAdapters().add(this);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getContractModel().getEntities();
		}
	};

	final ScenarioRVP portProvider = new SimpleRVP(PortPackage.eINSTANCE.getPortModel_Ports()) {
		private final Map<PortCapability, List<Pair<String, EObject>>> matchingValues = new HashMap<PortCapability, List<Pair<String, EObject>>>();
		
		@Override
		protected void install() {
			getScenario().getPortModel().eAdapters().add(this);
		}

		@Override
		protected EList<? extends EObject> getObjects() {
			return getScenario().getPortModel().getPorts();
		}

		@Override
		public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
			final List<Pair<String, EObject>> allValues = super.getAllowedValues(target, field);

			if (target instanceof LoadSlot || target instanceof PurchaseContract) {
				return matchingValues.get(PortCapability.LOAD);
			} else if (target instanceof Slot || target instanceof SalesContract) {
				return matchingValues.get(PortCapability.DISCHARGE);
			} else if (target instanceof Drydock) {
				return matchingValues.get(PortCapability.DRYDOCK);
			}
			
			return allValues;
		}

		@Override
		protected void cacheValues() {
			super.cacheValues();
			matchingValues.clear();
			for (final PortCapability pc : PortCapability.values()) {
				final ArrayList<Pair<String, EObject>> matches = new ArrayList<Pair<String, EObject>>();
				for (final Pair<String, EObject> value : cachedValues) {
					if (value.getSecond() != null) {
						final Port p = (Port) value.getSecond();
						if (p.getCapabilities().isEmpty()) matches.add(value); //TODO this is a hack, possibly;
						if (p.getCapabilities().contains(pc)) // this will be slow; may need to persuade EMF to use a hashset to be more sensible.
							matches.add(value);
					}
				}
				matchingValues.put(pc, matches);
			}
		}

		@Override
		protected boolean isRelevantTarget(Object target, Object feature) {
			return super.isRelevantTarget(target, feature) || feature == PortPackage.eINSTANCE.getPort_Capabilities();
		}
	};

	final ScenarioRVP indexProvider = new SimpleRVP(MarketPackage.eINSTANCE.getMarketModel_Indices()) {
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
		public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
			final Scenario scenario = getScenario();

			if (purchaseContracts == null) {
				install();
				cacheValues();
			}

			if (scenario != null) {
				if (target instanceof LoadSlot) {
					final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>(purchaseContracts);
					result.get(0).setFirst(getName(target, null, null));
					return result;
				} else if (target instanceof Slot) {
					final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>(salesContracts);

					result.get(0).setFirst(getName(target, null, null));
					return result;
				} else {
					return allContracts;
				}
			}

			return Collections.singletonList(new Pair<String, EObject>("empty", null));
		}

		@Override
		public String getName(EObject referer, EReference reference, EObject target) {
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
						return portContract.getName() + " [from " + port.getName() + "]";
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
			salesContracts = getSortedNames(scenario.getContractModel().getSalesContracts(), nameAttribute);
			purchaseContracts = getSortedNames(scenario.getContractModel().getPurchaseContracts(), nameAttribute);
			allContracts = getSortedNames(scenario.getContractModel().getSalesContracts(), nameAttribute);
			allContracts.addAll(purchaseContracts);
			allContracts.add(0, new Pair<String, EObject>("empty", null));
			salesContracts.add(0, new Pair<String, EObject>("empty", null));
			purchaseContracts.add(0, new Pair<String, EObject>("empty", null));
		}

		@Override
		protected boolean isRelevantTarget(Object target, Object feature) {
			return (feature == nameAttribute && target instanceof Contract) || feature == ContractPackage.eINSTANCE.getContractModel_PurchaseContracts()
					|| feature == ContractPackage.eINSTANCE.getContractModel_SalesContracts();
		}

		@Override
		public boolean updateOnChangeToFeature(Object changedFeature) {
			return changedFeature == CargoPackage.eINSTANCE.getSlot_Port();
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getNotifiers(EObject referer, EReference feature, EObject referenceValue) {
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
						features.add(ContractPackage.eINSTANCE.getContract_DefaultPorts());
						features.add(namedObjectName);
						for (final Contract c : cm.getPurchaseContracts()) {
							notifiers.add(new Pair<Notifier, List<Object>>(c, features));
						}
						for (final Contract c : cm.getSalesContracts()) {
							notifiers.add(new Pair<Notifier, List<Object>>(c, features));
						}
					}
				}
				notifiers.add(new Pair<Notifier, List<Object>>(port, Collections.singletonList((Object) namedObjectName)));

				return notifiers;
			}
			return super.getNotifiers(referer, feature, referenceValue);
		}
	};

	/**
	 * The filters for file extensions supported by the editor. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<String> FILE_EXTENSION_FILTERS = prefixExtensions(ScenarioModelWizard.FILE_EXTENSIONS, "*.");

	/**
	 * Returns a new unmodifiable list containing prefixed versions of the extensions in the given list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static List<String> prefixExtensions(List<String> extensions, String prefix) {
		List<String> result = new ArrayList<String>();
		for (String extension : extensions) {
			result.add(prefix + extension);
		}
		return Collections.unmodifiableList(result);
	}

	/**
	 * This keeps track of the editing domain that is used to track all changes to the model. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AdapterFactoryEditingDomain editingDomain;

	/**
	 * This is the one adapter factory used for providing views of the model. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ComposedAdapterFactory adapterFactory;

	/**
	 * This is the content outline page. <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * This is the content outline page's viewer. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TreeViewer contentOutlineViewer;

	/**
	 * This is the property sheet page. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PropertySheetPage propertySheetPage;

	/**
	 * This is the viewer that shadows the selection in the content outline. The parent relation must be correctly defined for this to work. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TreeViewer selectionViewer;

	/**
	 * This keeps track of the active viewer pane, in the book. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ViewerPane currentViewerPane;

	/**
	 * This keeps track of the active content viewer, which may be either one of the viewers in the pages or the content outline viewer. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Viewer currentViewer;

	/**
	 * This listens to which ever viewer is active. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ISelectionChangedListener selectionChangedListener;

	/**
	 * This keeps track of all the {@link org.eclipse.jface.viewers.ISelectionChangedListener}s that are listening to this editor. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<ISelectionChangedListener> selectionChangedListeners = new ArrayList<ISelectionChangedListener>();

	/**
	 * This keeps track of the selection of the editor as a whole. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ISelection editorSelection = StructuredSelection.EMPTY;

	/**
	 * This listens for when the outline becomes active <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IPartListener partListener = new IPartListener() {
		public void partActivated(IWorkbenchPart p) {
			if (p instanceof ContentOutline) {
				if (((ContentOutline) p).getCurrentPage() == contentOutlinePage) {
					getActionBarContributor().setActiveEditor(ScenarioEditor.this);

					setCurrentViewer(contentOutlineViewer);
				}
			} else if (p instanceof PropertySheet) {
				if (((PropertySheet) p).getCurrentPage() == propertySheetPage) {
					getActionBarContributor().setActiveEditor(ScenarioEditor.this);
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
	 * Resources that have been removed since last activation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<Resource> removedResources = new ArrayList<Resource>();

	/**
	 * Resources that have been changed since last activation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<Resource> changedResources = new ArrayList<Resource>();

	/**
	 * Resources that have been saved. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<Resource> savedResources = new ArrayList<Resource>();

	/**
	 * Map to store the diagnostic associated with a resource. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Map<Resource, Diagnostic> resourceToDiagnosticMap = new LinkedHashMap<Resource, Diagnostic>();

	/**
	 * Controls whether the problem indication should be updated. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected boolean updateProblemIndication = true;

	/**
	 * Adapter used to update the problem indication when resources are demanded loaded. <!-- begin-user-doc --> <!-- end-user-doc -->
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
					Diagnostic diagnostic = analyzeResourceProblems(resource, null);
					if (diagnostic.getSeverity() != Diagnostic.OK) {
						resourceToDiagnosticMap.put(resource, diagnostic);
					} else {
						resourceToDiagnosticMap.remove(resource);
					}

					if (updateProblemIndication) {
						getSite().getShell().getDisplay().asyncExec(new Runnable() {
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

	private VesselClassEVP vesselClassEditorViewerPane;

	private VesselEVP vesselEditorViewerPane;

	private PortEVP portEditorViewerPane;

	private CanalEVP canalEditorViewerPane;

	private CargoEVP cargoEditorViewerPane;

	private IndexEVP indexEditorViewerPane;

	private VesselEventEVP vesselEventEditorViewerPane;

	private PurchaseContractEVP purchaseContractEditorViewerPane;

	private SalesContractEVP salesContractEditorViewerPane;

	private EntityEVP entityEditorViewerPane;

	/**
	 * Handles activation of the editor or it's associated views. <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * Handles what to do with changed resources on activation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void handleChangedResources() {
		if (!changedResources.isEmpty() && (!isDirty() || handleDirtyConflict())) {
			if (isDirty()) {
				changedResources.addAll(editingDomain.getResourceSet().getResources());
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
							resourceToDiagnosticMap.put(resource, analyzeResourceProblems(resource, exception));
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
	 * Updates the problems indication with the information described in the specified diagnostic. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void updateProblemIndication() {
		if (updateProblemIndication) {
			BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.OK, "com.mmxlabs.lngscheduler.emf.editor", 0, null, new Object[] { editingDomain.getResourceSet() });
			for (Diagnostic childDiagnostic : resourceToDiagnosticMap.values()) {
				if (childDiagnostic.getSeverity() != Diagnostic.OK) {
					diagnostic.add(childDiagnostic);
				}
			}

			int lastEditorPage = getPageCount() - 1;
			if (lastEditorPage >= 0 && getEditor(lastEditorPage) instanceof ProblemEditorPart) {
				((ProblemEditorPart) getEditor(lastEditorPage)).setDiagnostic(diagnostic);
				if (diagnostic.getSeverity() != Diagnostic.OK) {
					setActivePage(lastEditorPage);
				}
			} else if (diagnostic.getSeverity() != Diagnostic.OK) {
				ProblemEditorPart problemEditorPart = new ProblemEditorPart();
				problemEditorPart.setDiagnostic(diagnostic);
				try {
					addPage(++lastEditorPage, problemEditorPart, getEditorInput());
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
	 * Shows a dialog that asks if conflicting changes should be discarded. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected boolean handleDirtyConflict() {
		return MessageDialog.openQuestion(getSite().getShell(), getString("_UI_FileConflict_label"), getString("_WARN_FileConflict"));
	}

	/**
	 * This creates a model editor. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ScenarioEditor() {
		super();
		initializeEditingDomain();
	}

	/**
	 * This sets up the editing domain for the model editor. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void initializeEditingDomain() {
		// Create an adapter factory that yields item providers.
		//
		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ScenarioItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new FleetItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ScheduleItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new EventsItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new FleetallocationItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new PortItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new CargoItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ContractItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new MarketItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new OptimiserItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new LsoItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

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
						Command mostRecentCommand = ((CommandStack) event.getSource()).getMostRecentCommand();
						if (mostRecentCommand != null) {
							setSelectionToViewer(mostRecentCommand.getAffectedObjects());
						}
						if (propertySheetPage != null && !propertySheetPage.getControl().isDisposed()) {
							propertySheetPage.refresh();
						}
					}
				});
			}
		});

		// Create the editing domain with a special command stack.
		//
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());
	}

	/**
	 * This is here for the listener to be able to call it. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void firePropertyChange(int action) {
		super.firePropertyChange(action);
	}

	/**
	 * This sets the selection into whichever viewer is active. <!-- begin-user-doc --> <!-- end-user-doc -->
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
						currentViewer.setSelection(new StructuredSelection(theSelection.toArray()), true);
					}
				}
			};
			getSite().getShell().getDisplay().asyncExec(runnable);
		}
	}

	/**
	 * This returns the editing domain as required by the {@link IEditingDomainProvider} interface. This is important for implementing the static methods of {@link AdapterFactoryEditingDomain} and for
	 * supporting {@link org.eclipse.emf.edit.ui.action.CommandAction}. <!-- begin-user-doc --> <!-- end-user-doc -->
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
	public class ReverseAdapterFactoryContentProvider extends AdapterFactoryContentProvider {
		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public ReverseAdapterFactoryContentProvider(AdapterFactory adapterFactory) {
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
			return (parent == null ? Collections.EMPTY_SET : Collections.singleton(parent)).toArray();
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		public Object[] getChildren(Object object) {
			Object parent = super.getParent(object);
			return (parent == null ? Collections.EMPTY_SET : Collections.singleton(parent)).toArray();
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
	 * This makes sure that one content viewer, either for the current page or the outline view, if it has focus, is the current one. <!-- begin-user-doc --> <!-- end-user-doc -->
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
					public void selectionChanged(SelectionChangedEvent selectionChangedEvent) {
						setSelection(selectionChangedEvent.getSelection());
					}
				};
			}

			// Stop listening to the old one.
			//
			if (currentViewer != null) {
				currentViewer.removeSelectionChangedListener(selectionChangedListener);
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
			setSelection(currentViewer == null ? StructuredSelection.EMPTY : currentViewer.getSelection());
		}
	}

	/**
	 * This returns the viewer as required by the {@link IViewerProvider} interface. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Viewer getViewer() {
		return currentViewer;
	}

	/**
	 * This creates a context menu for the viewer and adds a listener as well registering the menu for extension. <!-- begin-user-doc --> <!-- end-user-doc -->
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
		getSite().registerContextMenu(contextMenu, new UnwrappingSelectionProvider(viewer));

		int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
		Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
		viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));
		viewer.addDropSupport(dndOperations, transfers, new EditingDomainViewerDropAdapter(editingDomain, viewer));
	}

	/**
	 * This is the method called to load a resource into the editing domain's resource set based on the editor's input. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createModel() {
		URI resourceURI = EditUIUtil.getURI(getEditorInput());

		final IEclipseJobManager manager = LngEditorPlugin.getPlugin().getJobManager();
		for (final IJobDescriptor descriptor : manager.getJobs()) {
			final Object resource = manager.findResourceForJob(descriptor);
			if (resource instanceof IResource) {
				if (URI.createPlatformResourceURI(((IResource)resource).getFullPath().toString(), true).equals(resourceURI)) {
					final IJobControl control = manager.getControlForJob(descriptor);
					control.addListener(this);
					jobControl = control;
					this.jobStateChanged(jobControl, EJobState.CREATED, jobControl.getJobState());
					break;
				}
			}
		}
		manager.addEclipseJobManagerListener(this);

		Exception exception = null;
		Resource resource = null;

		{
			final ResourceSet resourceSet = editingDomain.getResourceSet();
			resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
			resourceSet.getLoadOptions().put(XMLResource.OPTION_RESOURCE_HANDLER, new XMLResource.ResourceHandler() {

				@Override
				public void preLoad(XMLResource resource, InputStream inputStream, Map<?, ?> options) {
					if (resource instanceof ResourceImpl) {
						((ResourceImpl) resource).setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
					}
				}

				@Override
				public void postLoad(XMLResource resource, InputStream inputStream, Map<?, ?> options) {
					if (resource instanceof ResourceImpl) {
						((ResourceImpl) resource).setIntrinsicIDToEObjectMap(null);
					}
				}

				@Override
				public void preSave(XMLResource resource, OutputStream outputStream, Map<?, ?> options) {
				}

				@Override
				public void postSave(XMLResource resource, OutputStream outputStream, Map<?, ?> options) {
				}
			});
		}

		try {
			// Load the resource through the editing domain.
			//
			resource = editingDomain.getResourceSet().getResource(resourceURI, true);
		} catch (Exception e) {
			exception = e;
			resource = editingDomain.getResourceSet().getResource(resourceURI, false);
		}

		Diagnostic diagnostic = analyzeResourceProblems(resource, exception);
		if (diagnostic.getSeverity() != Diagnostic.OK) {
			resourceToDiagnosticMap.put(resource, analyzeResourceProblems(resource, exception));
		}
		editingDomain.getResourceSet().eAdapters().add(problemIndicationAdapter);
	}

	/**
	 * Returns a diagnostic describing the errors and warnings listed in the resource and the specified exception (if any). <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Diagnostic analyzeResourceProblems(Resource resource, Exception exception) {
		if (!resource.getErrors().isEmpty() || !resource.getWarnings().isEmpty()) {
			BasicDiagnostic basicDiagnostic = new BasicDiagnostic(Diagnostic.ERROR, "com.mmxlabs.lngscheduler.emf.editor", 0, getString("_UI_CreateModelError_message", resource.getURI()),
					new Object[] { exception == null ? (Object) resource : exception });
			basicDiagnostic.merge(EcoreUtil.computeDiagnostic(resource, true));
			return basicDiagnostic;
		} else if (exception != null) {
			return new BasicDiagnostic(Diagnostic.ERROR, "com.mmxlabs.lngscheduler.emf.editor", 0, getString("_UI_CreateModelError_message", resource.getURI()), new Object[] { exception });
		} else {
			return Diagnostic.OK_INSTANCE;
		}
	}

	/**
	 * This is the method used by the framework to install your own controls. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated not any more
	 */
	@Override
	public void createPages() {

		((CTabFolder) getContainer()).setTabPosition(SWT.TOP);
		// Creates the model from the editor input
		//
		createModel();

		// Only creates the other pages if there is something that can be edited
		//
		if (!getEditingDomain().getResourceSet().getResources().isEmpty()) {
			final Scenario scenario = getScenario();
			if (scenario.getCargoModel() != null)
				createCargoEditor(portProvider, contractProvider, contractProvider);

			if (scenario.getFleetModel() != null) {
				createFleetEditor(vesselClassProvider, portProvider);
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

			final Scenario s = ((Scenario) (editingDomain.getResourceSet().getResources().get(0).getContents().get(0)));

			s.eAdapters().add(autoCorrector); // TODO dispose listener

			// Create a page for the selection tree view.
			//
			if (LngEditorPlugin.DEBUG_UI_ENABLED) {
				{
					final ViewerPane viewerPane = new ViewerPane(getSite().getPage(), ScenarioEditor.this) {
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
					selectionViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));

					selectionViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
					selectionViewer.setInput(editingDomain.getResourceSet());
					selectionViewer.setSelection(new StructuredSelection(editingDomain.getResourceSet().getResources().get(0)), true);
					viewerPane.setTitle(editingDomain.getResourceSet());

					new AdapterFactoryTreeEditor(selectionViewer.getTree(), adapterFactory);

					createContextMenuFor(selectionViewer);
					final int pageIndex = addPage(viewerPane.getControl());
					setPageText(pageIndex, getString("_UI_SelectionPage_label"));
				}

				// create a debug page
				final Composite debugPage = new Composite(getContainer(), SWT.NONE);
				debugPage.setLayout(new GridLayout());
				final Button lockButton = new Button(debugPage, SWT.TOGGLE);
				lockButton.setText("Locked for editing");

				lockButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						setLockedForEditing(lockButton.getSelection());
					}
				});

				setPageText(addPage(debugPage), "Debug");
			}

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

		setLockedForEditing(isLockedForEditing());
	}

	/**
	 * Create an editor for the initial schedule
	 */
	private void createScheduleEditor() {

	}

	private void createContractEditor() {
		final Object input = editingDomain.getResourceSet().getResources().get(0).getContents().get(0);

		final SashForm sash = new SashForm(getContainer(), SWT.HORIZONTAL);
		{
			entityEditorViewerPane = new EntityEVP(getSite().getPage(), ScenarioEditor.this);

			entityEditorViewerPane.createControl(sash);

			entityEditorViewerPane.init(getAdapterFactory(), ScenarioPackage.eINSTANCE.getScenario_ContractModel(), ContractPackage.eINSTANCE.getContractModel_Entities());

			entityEditorViewerPane.setTitle("Entities", getTitleImage());
			entityEditorViewerPane.getViewer().setInput(input);
		}
		{
			salesContractEditorViewerPane = new SalesContractEVP(getSite().getPage(), ScenarioEditor.this);

			salesContractEditorViewerPane.createControl(sash);

			salesContractEditorViewerPane.init(getAdapterFactory(), ScenarioPackage.eINSTANCE.getScenario_ContractModel(), ContractPackage.eINSTANCE.getContractModel_SalesContracts());

			salesContractEditorViewerPane.setTitle("Sales Contracts", getTitleImage());
			salesContractEditorViewerPane.getViewer().setInput(input);
		}
		{
			purchaseContractEditorViewerPane = new PurchaseContractEVP(getSite().getPage(), ScenarioEditor.this);

			purchaseContractEditorViewerPane.createControl(sash);

			purchaseContractEditorViewerPane.init(getAdapterFactory(), ScenarioPackage.eINSTANCE.getScenario_ContractModel(), ContractPackage.eINSTANCE.getContractModel_PurchaseContracts());

			purchaseContractEditorViewerPane.setTitle("Purchase Contracts", getTitleImage());
			purchaseContractEditorViewerPane.getViewer().setInput(input);
		}

		setPageText(addPage(sash), "Commercial");
	}

	private void createEventsEditor() {
		vesselEventEditorViewerPane = new VesselEventEVP(getSite().getPage(), ScenarioEditor.this);

		vesselEventEditorViewerPane.createControl(getContainer());
		vesselEventEditorViewerPane.setTitle("Events", getTitleImage());

		vesselEventEditorViewerPane.init(getAdapterFactory(), ScenarioPackage.eINSTANCE.getScenario_FleetModel(), FleetPackage.eINSTANCE.getFleetModel_VesselEvents());

		vesselEventEditorViewerPane.getViewer().setInput(editingDomain.getResourceSet().getResources().get(0).getContents().get(0));

		// TODO should this really be here?
		createContextMenuFor(vesselEventEditorViewerPane.getViewer());

		final int pageIndex = addPage(vesselEventEditorViewerPane.getControl());
		setPageText(pageIndex, "Events");
	}

	private void createIndexEditor() {
		final SashForm sash = new SashForm(getContainer(), SWT.SMOOTH | SWT.HORIZONTAL);

		indexEditorViewerPane = new IndexEVP(getSite().getPage(), this);

		indexEditorViewerPane.createControl(sash);
		indexEditorViewerPane.setTitle("Indices", getTitleImage());

		indexEditorViewerPane.init(getAdapterFactory(), ScenarioPackage.eINSTANCE.getScenario_MarketModel(), MarketPackage.eINSTANCE.getMarketModel_Indices());

		indexEditorViewerPane.getViewer().setInput(editingDomain.getResourceSet().getResources().get(0).getContents().get(0));

		final ChartViewer chart = new ChartViewer(sash);

		// TODO this is a hack
		final MarketModel mm = ((Scenario) (editingDomain.getResourceSet().getResources().get(0).getContents().get(0))).getMarketModel();

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
				final StepwisePriceCurve curve = mm.getIndices().get(i).getPriceCurve();
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
				final StepwisePriceCurve curve = mm.getIndices().get(i).getPriceCurve();
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
		return (Scenario) editingDomain.getResourceSet().getResources().get(0).getContents().get(0);
	}

	private void createCargoEditor(final IReferenceValueProvider portProvider, final IReferenceValueProvider loadContractProvider, final IReferenceValueProvider dischargeContractProvider) {
		// Create a page for the cargo editor
		{
			cargoEditorViewerPane = new CargoEVP(getSite().getPage(), ScenarioEditor.this);

			cargoEditorViewerPane.createControl(getContainer());
			cargoEditorViewerPane.setTitle("Cargoes", getTitleImage());

			cargoEditorViewerPane.init(getAdapterFactory(), ScenarioPackage.eINSTANCE.getScenario_CargoModel(), CargoPackage.eINSTANCE.getCargoModel_Cargoes());

			cargoEditorViewerPane.getViewer().setInput(editingDomain.getResourceSet().getResources().get(0).getContents().get(0));

			final int pageIndex = addPage(cargoEditorViewerPane.getControl());
			setPageText(pageIndex, "Cargoes");
		}
	}

	private void createPortEditor(final IReferenceValueProvider everyContractProvider, final IReferenceValueProvider marketProvider) {
		portEditorViewerPane = new PortEVP(getSite().getPage(), this);

		final SashForm sash = new SashForm(getContainer(), SWT.HORIZONTAL);

		portEditorViewerPane.createControl(sash);

		final List<EReference> path = new LinkedList<EReference>();

		path.add(ScenarioPackage.eINSTANCE.getScenario_PortModel());
		path.add(PortPackage.eINSTANCE.getPortModel_Ports());

		portEditorViewerPane.setTitle("Ports", getTitleImage());

		portEditorViewerPane.init(getAdapterFactory(), ScenarioPackage.eINSTANCE.getScenario_PortModel(), PortPackage.eINSTANCE.getPortModel_Ports());
		portEditorViewerPane.getViewer().setInput(editingDomain.getResourceSet().getResources().get(0).getContents().get(0));

		// createContextMenuFor(portEditor.getViewer());

		canalEditorViewerPane = new CanalEVP(getSite().getPage(), this);

		canalEditorViewerPane.createControl(sash);
		canalEditorViewerPane.setTitle("Canals", getTitleImage());

		canalEditorViewerPane.init(getAdapterFactory(), ScenarioPackage.eINSTANCE.getScenario_CanalModel(), PortPackage.eINSTANCE.getCanalModel_Canals());

		canalEditorViewerPane.getViewer().setInput(getScenario());
		// createContextMenuFor(canalEVP.getViewer());

		final int pageIndex = addPage(sash);
		setPageText(pageIndex, "Ports and Distances");

	}

	private void createFleetEditor(final IReferenceValueProvider vesselClassProvider, final IReferenceValueProvider portProvider) {
		{
			final SashForm sash = new SashForm(getContainer(), SWT.HORIZONTAL);

			vesselClassEditorViewerPane = new VesselClassEVP(getSite().getPage(), this);

			vesselClassEditorViewerPane.createControl(sash);

			vesselClassEditorViewerPane.init(getAdapterFactory(), ScenarioPackage.eINSTANCE.getScenario_FleetModel(), FleetPackage.eINSTANCE.getFleetModel_VesselClasses());

			vesselClassEditorViewerPane.getViewer().setInput(editingDomain.getResourceSet().getResources().get(0).getContents().get(0));

			vesselClassEditorViewerPane.setTitle("Vessel Classes", getTitleImage());

			// createContextMenuFor(vcePane.getViewer());

			vesselEditorViewerPane = new VesselEVP(getSite().getPage(), this);

			vesselEditorViewerPane.createControl(sash);

			vesselEditorViewerPane.init(getAdapterFactory(), ScenarioPackage.eINSTANCE.getScenario_FleetModel(), FleetPackage.eINSTANCE.getFleetModel_Fleet());

			vesselEditorViewerPane.setTitle("Vessels", getTitleImage());

			vesselEditorViewerPane.getViewer().setInput(editingDomain.getResourceSet().getResources().get(0).getContents().get(0));

			// createContextMenuFor(fleetPane.getViewer());

			final int pageIndex = addPage(sash);
			setPageText(pageIndex, "Fleet");
		}
	}

	/**
	 * If there is just one page in the multi-page editor part, this hides the single tab at the bottom. <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * If there is more than one page in the multi-page editor part, this shows the tabs at the bottom. <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * This is used to track the active viewer. <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * This is how the framework determines which interfaces we implement. <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * This accesses a cached version of the content outliner. <!-- begin-user-doc --> <!-- end-user-doc -->
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
					contentOutlineViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
					contentOutlineViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
					contentOutlineViewer.setInput(editingDomain.getResourceSet());

					// Make sure our popups work.
					//
					createContextMenuFor(contentOutlineViewer);

					if (!editingDomain.getResourceSet().getResources().isEmpty()) {
						// Select the root object in the view.
						//
						contentOutlineViewer.setSelection(new StructuredSelection(editingDomain.getResourceSet().getResources().get(0)), true);
					}
				}

				@Override
				public void makeContributions(IMenuManager menuManager, IToolBarManager toolBarManager, IStatusLineManager statusLineManager) {
					super.makeContributions(menuManager, toolBarManager, statusLineManager);
					contentOutlineStatusLineManager = statusLineManager;
				}

				@Override
				public void setActionBars(IActionBars actionBars) {
					super.setActionBars(actionBars);
					getActionBarContributor().shareGlobalActions(this, actionBars);
				}
			}

			contentOutlinePage = new MyContentOutlinePage();

			// Listen to selection so that we can handle it is a special way.
			//
			contentOutlinePage.addSelectionChangedListener(new ISelectionChangedListener() {
				// This ensures that we handle selections correctly.
				//
				public void selectionChanged(SelectionChangedEvent event) {
					handleContentOutlineSelection(event.getSelection());
				}
			});
		}

		return contentOutlinePage;
	}

	private WeakHashMap<DetailCompositePropertySheetPage, Boolean> propertySheetPages = new WeakHashMap<DetailCompositePropertySheetPage, Boolean>();

	/**
	 * This accesses a cached version of the property sheet. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NO
	 */
	public IPropertySheetPage getPropertySheetPage() {
		final DetailCompositePropertySheetPage page = new DetailCompositePropertySheetPage(getEditingDomain(), this);
		page.setLockedForEditing(isLockedForEditing());
		propertySheetPages.put(page, true);

		return page;
	}

	@Override
	public IReferenceValueProvider getValueProvider(final EClass c) {
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

	/**
	 * This deals with how we want selection in the outliner to affect the other views. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void handleContentOutlineSelection(ISelection selection) {
		if (currentViewerPane != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			Iterator<?> selectedElements = ((IStructuredSelection) selection).iterator();
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
					selectionViewer.setSelection(new StructuredSelection(selectionList));
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
	 * This is for implementing {@link IEditorPart} and simply tests the command stack. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isDirty() {
		return ((BasicCommandStack) editingDomain.getCommandStack()).isSaveNeeded();
	}

	private boolean lockedForEditing = false;

	public boolean isLockedForEditing() {
		return lockedForEditing;
	}

	public void setLockedForEditing(final boolean lockedForEditing) {
		this.lockedForEditing = lockedForEditing;
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (cargoEditorViewerPane != null) cargoEditorViewerPane.setLockedForEditing(lockedForEditing);
				if (vesselClassEditorViewerPane != null) vesselClassEditorViewerPane.setLockedForEditing(lockedForEditing);
				if (vesselEditorViewerPane != null) vesselEditorViewerPane.setLockedForEditing(lockedForEditing);
				if (purchaseContractEditorViewerPane != null) purchaseContractEditorViewerPane.setLockedForEditing(lockedForEditing);
				if (salesContractEditorViewerPane != null) salesContractEditorViewerPane.setLockedForEditing(lockedForEditing);
				if (entityEditorViewerPane != null) entityEditorViewerPane.setLockedForEditing(lockedForEditing);
				if (indexEditorViewerPane != null) indexEditorViewerPane.setLockedForEditing(lockedForEditing);
				for (final DetailCompositePropertySheetPage page : propertySheetPages.keySet()) {
					page.setLockedForEditing(lockedForEditing);
				}
			}
		});
	}

	/**
	 * This is for implementing {@link IEditorPart} and simply saves the model file. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NO
	 */
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		// // run validation on save
		// TODO fix this - doesn't work, presumably because of resource mismatch
		// final IStatus status = (IStatus) Platform.getAdapterManager().getAdapter(getScenario().eResource(), IStatus.class);
		//
		// try {
		// MarkerUtil.updateMarkers(status);
		//
		// if (status.matches(Status.ERROR)) {
		// // activate problems view
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.ProblemView" // TODO find where this lives
		// , null, IWorkbenchPage.VIEW_VISIBLE);
		// }
		// } catch (Exception ex) {
		//
		// }

		// Save only resources that have actually changed.
		//
		final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
		saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);

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
				for (Resource resource : editingDomain.getResourceSet().getResources()) {
					if ((first || !resource.getContents().isEmpty() || isPersisted(resource)) && !editingDomain.isReadOnly(resource)) {
						try {
							long timeStamp = resource.getTimeStamp();
							resource.save(saveOptions);
							if (resource.getTimeStamp() != timeStamp) {
								savedResources.add(resource);
							}
						} catch (Exception exception) {
							resourceToDiagnosticMap.put(resource, analyzeResourceProblems(resource, exception));
						}
						first = false;
					}
				}
			}
		};

		if (jobControl != null && jobControl.getJobState() == EJobState.COMPLETED) {
			// nuke job as we have to start again
			jobControl.cancel();
		}

		updateProblemIndication = false;
		try {
			// This runs the options, and shows progress.
			//
			new ProgressMonitorDialog(getSite().getShell()).run(true, false, operation);

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
	 * This returns whether something has been persisted to the URI of the specified resource. The implementation uses the URI converter from the editor's resource set to try to open an input stream.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected boolean isPersisted(Resource resource) {
		boolean result = false;
		try {
			InputStream stream = editingDomain.getResourceSet().getURIConverter().createInputStream(resource.getURI());
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
	 * This always returns true because it is not currently supported. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * This also changes the editor's input. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void doSaveAs() {
		String[] filters = FILE_EXTENSION_FILTERS.toArray(new String[FILE_EXTENSION_FILTERS.size()]);
		String[] files = LngEditorAdvisor.openFilePathDialog(getSite().getShell(), SWT.SAVE, filters);
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
		IProgressMonitor progressMonitor = getActionBars().getStatusLineManager() != null ? getActionBars().getStatusLineManager().getProgressMonitor() : new NullProgressMonitor();
		doSave(progressMonitor);
	}

	/**
	 * This is called during startup. <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider}. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.add(listener);
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider}. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider} to return this editor's overall selection. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ISelection getSelection() {
		return editorSelection;
	}

	/**
	 * This implements {@link org.eclipse.jface.viewers.ISelectionProvider} to set this editor's overall selection. Calling this result will notify the listeners. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
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
		IStatusLineManager statusLineManager = currentViewer != null && currentViewer == contentOutlineViewer ? contentOutlineStatusLineManager : getActionBars().getStatusLineManager();

		if (statusLineManager != null) {
			if (selection instanceof IStructuredSelection) {
				Collection<?> collection = ((IStructuredSelection) selection).toList();
				switch (collection.size()) {
				case 0: {
					statusLineManager.setMessage(getString("_UI_NoObjectSelected"));
					break;
				}
				case 1: {
					String text = new AdapterFactoryItemDelegator(adapterFactory).getText(collection.iterator().next());
					statusLineManager.setMessage(getString("_UI_SingleObjectSelected", text));
					break;
				}
				default: {
					statusLineManager.setMessage(getString("_UI_MultiObjectSelected", Integer.toString(collection.size())));
					break;
				}
				}
			} else {
				statusLineManager.setMessage("");
			}
		}
	}

	/**
	 * This looks up a string in the plugin's plugin.properties file. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static String getString(String key) {
		return LngEditorPlugin.INSTANCE.getString(key);
	}

	/**
	 * This looks up a string in plugin.properties, making a substitution. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static String getString(String key, Object s1) {
		return LngEditorPlugin.INSTANCE.getString(key, new Object[] { s1 });
	}

	/**
	 * This implements {@link org.eclipse.jface.action.IMenuListener} to help fill the context menus with contributions from the Edit menu. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void menuAboutToShow(IMenuManager menuManager) {
		((IMenuListener) getEditorSite().getActionBarContributor()).menuAboutToShow(menuManager);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EditingDomainActionBarContributor getActionBarContributor() {
		return (EditingDomainActionBarContributor) getEditorSite().getActionBarContributor();
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
		final IEclipseJobManager manager = LngEditorPlugin.getPlugin().getJobManager();
		manager.removeEclipseJobManagerListener(this);
		
		if (jobControl != null) jobControl.removeListener(this);
		
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
	 * Returns whether the outline view should be presented to the user. <!-- begin-user-doc --> <!-- end-user-doc -->
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

	@Override
	public EObject getModel() {
		return getScenario();
	}

	private IJobControl jobControl = null;
	
	@Override
	public void jobAdded(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl control, Object resource) {
		final URI uri = EditUIUtil.getURI(getEditorInput());
		if (resource instanceof IResource) {
			final URI resourceURI = URI.createPlatformResourceURI(((IResource)resource).getFullPath().toString(), true);
			if (jobControl != null)
				jobControl.removeListener(this);
			if (uri.equals(resourceURI)) {
				control.addListener(this);
				jobControl = control;
			}
		}
	}

	@Override
	public void jobRemoved(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl control, Object resource) {
		if (control == jobControl) {
			control.removeListener(this);
			jobControl = null;
		}
	}

	@Override
	public void jobSelected(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl jobControl, Object resource) {

	}

	@Override
	public void jobDeselected(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl jobControl, Object resource) {

	}

	@Override
	public void jobManagerAdded(IEclipseJobManager eclipseJobManager, IJobManager jobManager) {

	}

	@Override
	public void jobManagerRemoved(IEclipseJobManager eclipseJobManager, IJobManager jobManager) {

	}

	@Override
	public boolean jobStateChanged(IJobControl job, EJobState oldState, EJobState newState) {
		switch (newState) {
		case INITIALISING:
		case INITIALISED:
		case RUNNING:
			setLockedForEditing(true);
			break;
		default:
			setLockedForEditing(false);
		}
		return true;
	}

	@Override
	public boolean jobProgressUpdated(IJobControl job, int progressDelta) {
		return true;
	}
}
