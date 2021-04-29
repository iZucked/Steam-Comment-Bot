/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.vesselsimport.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortTypeConstants;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;
import com.mmxlabs.lngdataserver.lng.io.vessels.VesselsToScenarioCopier;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.VesselConstants;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.DefaultCommandHandler;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerFilterSupport;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerSortingSupport;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.IComparableProvider;
import com.mmxlabs.models.ui.tabular.IFilterProvider;
import com.mmxlabs.models.ui.tabular.filter.FilterField;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderCache;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class ImportSingleVesselPage extends WizardPage {

	private static final String NODE_OWNER = "owner";

	private static final String ATTRIBUTE_NAME = "name";
	private static final String ATTRIBUTE_REFERENCE_VESSEL = "referenceVessel";
	private static final String ATTRIBUTE_CAPACITY = "capacity";
	private static final String ATTRIBUTE_VESSEL_INDEX = "vesselIndex";

	private static final String LABEL_NOT_SPECIFIED = "<Not specified>";

	private EPackage tableDataModel;

	private EAttribute attribName;
	private EReference refReferenceVessel;
	private EAttribute attribCapacity;
	private EAttribute attribVesselIndex;

	private ReferenceValueProviderCache temporaryReferenceValueProviderCache = null;

	final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();
	private ModelReference modelReference;
	private VesselsVersion vesselRecords;
	private Map<String, Vessel> referenceVesselMap;
	private EObject lastSelectedRow = null;
	private final ArrayList<com.mmxlabs.lngdataserver.integration.vessels.model.Vessel> importableVessels = new ArrayList<>();

	public ImportSingleVesselPage(final String pageName, ModelReference modelReference, VesselsVersion vesselRecords) {
		super(pageName);
		this.modelReference = modelReference;
		this.vesselRecords = vesselRecords;
		this.referenceVesselMap = new HashMap<>();
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel((LNGScenarioModel) modelReference.getInstance());
		fleetModel.getVessels().stream() //
				.filter(Vessel::isReferenceVessel) //
				.forEach(v -> this.referenceVesselMap.put(v.getName(), v));
		setTitle("Select a vessel to import. Reference vessels can be amended.");
	}

	private void createDataModel() {
		tableDataModel = GenericEMFTableDataModel.createEPackage(NODE_OWNER);
		final EClass rowClass = GenericEMFTableDataModel.getRowClass(tableDataModel);
		attribName = GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.eINSTANCE.getEString(), ATTRIBUTE_NAME);
		refReferenceVessel = GenericEMFTableDataModel.createRowReference(rowClass, FleetPackage.eINSTANCE.getVessel(), ATTRIBUTE_REFERENCE_VESSEL);
		attribCapacity = GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.eINSTANCE.getEInt(), ATTRIBUTE_CAPACITY);
		attribVesselIndex = GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.eINSTANCE.getEInt(), ATTRIBUTE_VESSEL_INDEX);
	}

	private EObject createRow(final EObject dataModelInstance, final String name, final Vessel referenceVessel, final int capacity, final int vesselIndex) {
		final EObject row = GenericEMFTableDataModel.createRow(tableDataModel, dataModelInstance, null);
		row.eSet(attribName, name);
		row.eSet(refReferenceVessel, referenceVessel);
		row.eSet(attribCapacity, capacity);
		row.eSet(attribVesselIndex, vesselIndex);
		return row;
	}

	private static class TreeArrayContentProvider implements ITreeContentProvider {

		private static final Object[] EMPTY = new Object[0];

		@Override
		public void dispose() {
			//
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			//
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Object[]) {
				// see bug 9262 why we can't return the same array
				Object[] orig = (Object[]) inputElement;
				Object[] arr = new Object[orig.length];
				System.arraycopy(orig, 0, arr, 0, arr.length);
				return arr;
			}
			if (inputElement instanceof Collection) {
				return ((Collection<?>) inputElement).toArray();
			}
			return EMPTY;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			return EMPTY;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return false;
		}

	}

	private class LocalReferenceVesselValueProvider implements IReferenceValueProvider {
		private IReferenceValueProvider delegate;

		public LocalReferenceVesselValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
			this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), FleetPackage.eINSTANCE.getVessel())
					.createReferenceValueProvider(owner, reference, rootObject);
		}

		@Override
		public List<Pair<String, EObject>> getAllowedValues(EObject target, EStructuralFeature field) {
			final List<Pair<String, EObject>> delegateValue = delegate.getAllowedValues(target, field);

			final List<Pair<String, EObject>> filteredList = new ArrayList<>();
			filteredList.add(new Pair<>(LABEL_NOT_SPECIFIED, null));

			for (final Pair<String, EObject> p : delegateValue) {
				if (p.getSecond() instanceof Vessel) {
					final Vessel vessel = (Vessel) p.getSecond();
					if (vessel.isReferenceVessel()) {
						filteredList.add(p);
					}
				}
			}
			return filteredList;
		}

		@Override
		public String getName(EObject referer, EReference feature, EObject referenceValue) {
			return delegate.getName(referer, feature, referenceValue);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getNotifiers(EObject referer, EReference feature, EObject referenceValue) {
			return delegate.getNotifiers(referer, feature, referenceValue);
		}

		@Override
		public boolean updateOnChangeToFeature(Object changedFeature) {
			return delegate.updateOnChangeToFeature(changedFeature);
		}

		@Override
		public void dispose() {
			delegate.dispose();
		}
	}

	@Override
	public void createControl(Composite parent) {
		createDataModel();

		Group vesselImportGroup = new Group(parent, SWT.NONE);
		vesselImportGroup.setLayout(new GridLayout(1, false));
		vesselImportGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

		final DetailToolbarManager buttonManager = new DetailToolbarManager(vesselImportGroup, SWT.TOP);

		final FilterField filterField = new FilterField(vesselImportGroup);
		buttonManager.getToolbarManager().add(filterField.getContribution());

		final EObjectTableViewer viewer = new EObjectTableViewer(vesselImportGroup, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);

		final EObjectTableViewerFilterSupport filterSupport = new EObjectTableViewerFilterSupport(viewer, viewer.getGrid());
		final EObjectTableViewerSortingSupport sortingSupport = new EObjectTableViewerSortingSupport();
		viewer.addFilter(filterSupport.createViewerFilter());
		filterField.setFilterSupport(filterSupport);
		viewer.setComparator(sortingSupport.createViewerComparer());

		final GridData gdViewer = new GridData(SWT.FILL, SWT.NONE, true, true);
		gdViewer.heightHint = 200;
		viewer.getControl().setLayoutData(gdViewer);

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(true);
		viewer.setContentProvider(new TreeArrayContentProvider());

		temporaryReferenceValueProviderCache = new ReferenceValueProviderCache((MMXRootObject) modelReference.getInstance());
		final ICommandHandler commandHandler = new DefaultCommandHandler(modelReference, temporaryReferenceValueProviderCache);

		addNameColumn(viewer, commandHandler, filterSupport, sortingSupport);
		addReferenceVesselColumn(viewer, commandHandler, filterSupport, sortingSupport);
		addCapacityColumn(viewer, commandHandler, sortingSupport);

		setControl(vesselImportGroup);

		buttonManager.getToolbarManager().add(new ActionContributionItem(PackActionFactory.createPackColumnsAction(viewer)));
		buttonManager.getToolbarManager().update(true);

		final EObject dataModelInstance = GenericEMFTableDataModel.createRootInstance(tableDataModel);
		final List<EObject> rows = getImportRows(modelReference, dataModelInstance);
		viewer.setInput(rows);
		viewer.addSelectionChangedListener(event -> {
			setPageComplete(!event.getStructuredSelection().isEmpty());
			final IStructuredSelection iStructuredSelection = event.getStructuredSelection();
			lastSelectedRow = iStructuredSelection.isEmpty() ? null : (EObject) iStructuredSelection.getFirstElement();
		});
		final GridColumn[] columns = viewer.getGrid().getColumns();
		for (final GridColumn c : columns) {
			if (c.getResizeable()) {
				c.pack();
			}
		}
		setPageComplete(false);
	}

	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	@Override
	public void dispose() {
		if (temporaryReferenceValueProviderCache != null) {
			temporaryReferenceValueProviderCache.dispose();
		}
		super.dispose();
	}

	private List<EObject> getImportRows(final ModelReference modelReference, final EObject dataModelInstance) {
		final Set<String> existingVesselIMOs = ScenarioModelUtil.getFleetModel((LNGScenarioModel) modelReference.getInstance()).getVessels().stream() //
				.filter(v -> !v.isMmxReference()) //
				.map(Vessel::getIMO) //
				.filter(s -> s != null && !s.isBlank()) //
				.collect(Collectors.toSet());
		final List<EObject> rows = new LinkedList<>();
		int index = 0;
		for (final com.mmxlabs.lngdataserver.integration.vessels.model.Vessel vessel : this.vesselRecords.getVessels()) {
			if (!existingVesselIMOs.contains(vessel.getImo())) {
				final Optional<String> optRefVess = vessel.getReferenceVessel();
				final Vessel referenceVessel;
				if (optRefVess.isPresent()) {
					final String expectedReferenceVessel = VesselConstants.convertMMXReferenceNameToInternalName(optRefVess.get());
					Vessel fetchedVessel = referenceVesselMap.get(expectedReferenceVessel);
					if (fetchedVessel == null) {
						fetchedVessel = referenceVesselMap.get(optRefVess.get());
					}
					referenceVessel = fetchedVessel;
				} else {
					referenceVessel = null;
				}
				final int capacity = vessel.getCapacity().isPresent() ? vessel.getCapacity().getAsInt() : 0;
				importableVessels.add(vessel);
				rows.add(createRow(dataModelInstance, vessel.getName(), referenceVessel, capacity, index++));
			}
		}
		return rows;
	}

	private void addNameColumn(final EObjectTableViewer viewer, final ICommandHandler commandHandler, final EObjectTableViewerFilterSupport filterSupport,
			final EObjectTableViewerSortingSupport sortingSupport) {
		final ReadOnlyManipulatorWrapper<BasicAttributeManipulator> m = new ReadOnlyManipulatorWrapper<>(new BasicAttributeManipulator(attribName, commandHandler));
		final GridViewerColumn gvc = viewer.addColumn("Name", m, m);
		filterSupport.createColumnMnemonics(gvc.getColumn(), gvc.getColumn().getText());
		final IFilterProvider filterProvider = new IFilterProvider() {

			@Override
			public @Nullable String render(Object object) {
				final EObject eObject = (EObject) object;
				return (String) eObject.eGet(attribName);
			}

			@Override
			public @Nullable Object getFilterValue(Object object) {
				return render(object);
			}
		};
		gvc.getColumn().setData(EObjectTableViewer.COLUMN_FILTER, filterProvider);
		sortingSupport.addSortableColumn(viewer, gvc, gvc.getColumn());
		final IComparableProvider sortingProvider = element -> (String) ((EObject) element).eGet(attribName);
		gvc.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, sortingProvider);
	}

	private void addReferenceVesselColumn(final EObjectTableViewer viewer, final ICommandHandler commandHandler, final EObjectTableViewerFilterSupport filterSupport,
			final EObjectTableViewerSortingSupport sortingSupport) {
		IReferenceValueProvider provider = new LocalReferenceVesselValueProvider(refReferenceVessel.getEContainingClass(), refReferenceVessel, (LNGScenarioModel) modelReference.getInstance());
		final SingleReferenceManipulator m = new SingleReferenceManipulator(refReferenceVessel, provider, commandHandler);
		GridViewerColumn gvc = viewer.addColumn("Reference Vessel", m, m);
		filterSupport.createColumnMnemonics(gvc.getColumn(), gvc.getColumn().getText());
		final IFilterProvider filterProvider = new IFilterProvider() {

			@Override
			public @Nullable String render(Object object) {
				final Vessel refVessel = (Vessel) ((EObject) object).eGet(refReferenceVessel);
				return refVessel == null ? LABEL_NOT_SPECIFIED : refVessel.getName();
			}

			@Override
			public @Nullable Object getFilterValue(Object object) {
				return render(object);
			}
		};
		gvc.getColumn().setData(EObjectTableViewer.COLUMN_FILTER, filterProvider);
		sortingSupport.addSortableColumn(viewer, gvc, gvc.getColumn());
		final IComparableProvider sortingProvider = element -> {
			final EObject eObject = (EObject) element;
			final Vessel referenceVessel = (Vessel) eObject.eGet(refReferenceVessel);
			if (referenceVessel == null) {
				return LABEL_NOT_SPECIFIED;
			}
			if (referenceVessel.isMmxReference()) {
				final String refName = referenceVessel.getName();
				return refName.substring(1, refName.length() - 1);
			}
			return referenceVessel.getName();
		};
		gvc.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, sortingProvider);
	}

	private void addCapacityColumn(final EObjectTableViewer viewer, final ICommandHandler commandHandler, final EObjectTableViewerSortingSupport sortingSupport) {
		final ReadOnlyManipulatorWrapper<NumericAttributeManipulator> m = new ReadOnlyManipulatorWrapper<>(new NumericAttributeManipulator(attribCapacity, commandHandler));
		final GridViewerColumn gvc = viewer.addColumn("Capacity", new ICellRenderer() {
			@Override
			public @Nullable Object getFilterValue(Object object) {
				return null;
			}

			@Override
			public Comparable getComparable(Object object) {
				return null;
			}

			@Override
			public @Nullable String render(Object object) {
				final EObject currentRow = (EObject) object;
				final int vesselIndex = ((Integer) currentRow.eGet(attribVesselIndex)).intValue();
				final com.mmxlabs.lngdataserver.integration.vessels.model.Vessel currentVessel = importableVessels.get(vesselIndex);
				if (currentVessel.getCapacity().isPresent()) {
					return String.format("%,d", currentVessel.getCapacity().getAsInt());
				} else {
					final Vessel refVess = (Vessel) currentRow.eGet(refReferenceVessel);
					if (refVess == null) {
						return "-";
					} else {
						return String.format("%,d", refVess.getCapacity());
					}
				}
			}

			@Override
			public boolean isValueUnset(Object object) {
				return false;
			}

			@Override
			public @Nullable Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
				return Collections.emptySet();
			}
		}, m);
		sortingSupport.addSortableColumn(viewer, gvc, gvc.getColumn());
		final IComparableProvider sortingProvider = element -> (Integer) ((EObject) element).eGet(attribCapacity);
		gvc.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, sortingProvider);
	}

	public boolean apply() {
		try {
			getContainer().run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Import vessel", IProgressMonitor.UNKNOWN);
					try {
						final CompoundCommand command = new CompoundCommand("Import single vessel");
						if (lastSelectedRow != null) {
							final EObject selectedRow = lastSelectedRow;
							final int selectedVesselIndex = ((Integer) selectedRow.eGet(attribVesselIndex)).intValue();
							final com.mmxlabs.lngdataserver.integration.vessels.model.Vessel selectedVessel = importableVessels.get(selectedVesselIndex);
							final Vessel newVessel = FleetFactory.eINSTANCE.createVessel();
							newVessel.setName(selectedVessel.getName());
							newVessel.setShortName(selectedVessel.getShortName());
							newVessel.setMmxId(selectedVessel.getMmxId());
							newVessel.setIMO(selectedVessel.getImo());
							newVessel.setNotes(selectedVessel.getNotes());

							newVessel.setLadenAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
							newVessel.setBallastAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
							newVessel.setReference((Vessel) selectedRow.eGet(refReferenceVessel));

							final PortModel portModel = ScenarioModelUtil.getPortModel((LNGScenarioModel) modelReference.getInstance());
							final FleetModel fleetModel = ScenarioModelUtil.getFleetModel((LNGScenarioModel) modelReference.getInstance());
							final EditingDomain editingDomain = modelReference.getEditingDomain();
							final Map<String, APortSet<Port>> portTypeMap = new HashMap<>();

							portModel.getPorts().forEach(c -> portTypeMap.put(PortTypeConstants.PORT_PREFIX + c.getLocation().getMmxId(), c));
							portModel.getPortCountryGroups().forEach(c -> portTypeMap.put(PortTypeConstants.COUNTRY_GROUP_PREFIX + c.getName(), c));
							portModel.getPortGroups().forEach(c -> portTypeMap.put(PortTypeConstants.PORT_GROUP_PREFIX + c.getName(), c));

							final Map<String, BaseFuel> baseFuels = new HashMap<>(fleetModel.getBaseFuels().stream() //
									.collect(Collectors.toMap(BaseFuel::getName, Function.identity())));

							command.append(AddCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, newVessel));
							VesselsToScenarioCopier.updateVessel(command, editingDomain, newVessel, selectedVessel, baseFuels, portTypeMap, fleetModel);

							RunnerHelper.syncExecDisplayOptional(() -> modelReference.getCommandStack().execute(command));

						}
					} catch (final Exception e) {
						setErrorMessage(e.getMessage());
					} finally {
						monitor.done();
					}
				}
			});
			setPageComplete(true);
			getContainer().updateButtons();
			return true;
		} catch (final InvocationTargetException | InterruptedException e) {
			return false;
		}
	}
}
