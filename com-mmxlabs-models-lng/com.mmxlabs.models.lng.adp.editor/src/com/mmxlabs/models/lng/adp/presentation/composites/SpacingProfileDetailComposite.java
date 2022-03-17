/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DesSpacingAllocation;
import com.mmxlabs.models.lng.adp.DesSpacingRow;
import com.mmxlabs.models.lng.adp.FobSpacingAllocation;
import com.mmxlabs.models.lng.adp.SpacingProfile;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateTimeAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;

public class SpacingProfileDetailComposite extends Composite implements IDisplayComposite {

	private IDisplayComposite delegate;
	private IDialogEditingContext dialogContext;
	private IStatus status;

	private SpacingProfile oldValue = null;

	private ICommandHandler commandHandler;

	private Runnable releaseAdaptersRunnable = null;

	private final Composite spacingProfileComposite;

	private final EObjectTableViewer unshippedCargoesViewer;
	private final EObjectTableViewer shippedCargoesViewer;
	private final EObjectTableViewer shippedCargoDetailsViewer;

	private Group shippedCargoDetailsGroup;

	private Action unshippedCargoesPackAction;
	private Action shippedCargoesPackAction;
	private Action shippedCargoDetailsPackAction;

	private Color systemWhite;

	protected DefaultStatusProvider statusProvider = new DefaultStatusProvider() {

		@Override
		public IStatus getStatus() {
			return status;
		}
	};

	private final Adapter adapter = new MMXContentAdapter() {

		@Override
		public void reallyNotifyChanged(Notification notification) {
			if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}
			if (!isDisposed() && isVisible()) {
				boolean disposed = true;
				// if (viewer == null || viewer.getGrid().isDisposed()) {
				// disposed = false;
				// } else {
				// viewer.refresh();
				// }
				if (disposed)
					dialogContext.getDialogController().validate();
			} else {
				SpacingProfileDetailComposite.this.removeAdapter();
				if (releaseAdaptersRunnable != null) {
					releaseAdaptersRunnable.run();
					releaseAdaptersRunnable = null;
				}
			}
		}
	};

	public SpacingProfileDetailComposite(final Composite parent, final IDialogEditingContext dialogContext, final int style, final FormToolkit toolkit) {
		super(parent, style);
		this.dialogContext = dialogContext;
		toolkit.adapt(this);
		setLayout(new GridLayout(1, false));
		delegate = new DefaultDetailComposite(this, style, toolkit);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 500;
		delegate.getComposite().setLayoutData(gd);

		spacingProfileComposite = toolkit.createComposite(this, style);
		spacingProfileComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		spacingProfileComposite.setLayout(new GridLayout());

		this.systemWhite = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

		this.unshippedCargoesViewer = getUnshippedCargoesViewer(spacingProfileComposite);
		this.shippedCargoesViewer = createShippedCargoesViewers(spacingProfileComposite);
		this.shippedCargoDetailsViewer = createShippedCargoDetailsViewer(spacingProfileComposite);
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc) {
		if (value != null) {
			final SpacingProfile spacingProfile = (SpacingProfile) value;
		}
		delegate.display(dialogContext, root, value, range, dbc);
		removeAdapter();
		oldValue = (SpacingProfile) value;
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel((LNGScenarioModel) root);
		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}
		unshippedCargoesViewer.setInput(value);
		unshippedCargoesPackAction.run();
		shippedCargoesViewer.setInput(value);
		shippedCargoesPackAction.run();
		// releaseAdaptersRunnable = () -> cargoModel.eAdapters().remove(cargoAdapter);

	}

	void removeAdapter() {
		if (oldValue != null) {
			oldValue.eAdapters().remove(adapter);
			oldValue = null;
		}
	}

	@Override
	public void setCommandHandler(ICommandHandler commandHandler) {
		delegate.setCommandHandler(commandHandler);
		this.commandHandler = commandHandler;

	}

	@Override
	public void setEditorWrapper(IInlineEditorWrapper wrapper) {
		delegate.setEditorWrapper(wrapper);

	}

	@Override
	public void displayValidationStatus(IStatus status) {
		this.status = status;
		delegate.displayValidationStatus(status);
		statusProvider.fireStatusChanged(status);

	}

	@Override
	public boolean checkVisibility(IDialogEditingContext context) {
		return delegate.checkVisibility(context);
	}

	private EObjectTableViewer getUnshippedCargoesViewer(final Composite parent) {
		final Group unshippedCargoesGroup = buildTableGroup(parent, "Unshipped Cargoes");

		final DetailToolbarManager buttonManager = new DetailToolbarManager(unshippedCargoesGroup, SWT.TOP);
		final EObjectTableViewer eViewer = new EObjectTableViewer(unshippedCargoesGroup, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);
		final Action addUnshippedCargoesRows = new Action("Add") {
			@Override
			public void run() {
				final List<SalesContract> selectedSalesContracts = openSelectionDialogBox(parent, oldValue, SpacingProfile::getFobSpacingAllocations, FobSpacingAllocation::getContract,
						ADPPackage.Literals.FOB_SPACING_ALLOCATION, ADPPackage.Literals.SPACING_ALLOCATION__CONTRACT, "FOB Contract");
				updateStateWithSelection(selectedSalesContracts, oldValue, SpacingProfile::getFobSpacingAllocations, FobSpacingAllocation::getContract, "Contracts",
						ADPPackage.Literals.SPACING_PROFILE__FOB_SPACING_ALLOCATIONS, ADPFactory.eINSTANCE::createFobSpacingAllocation, FobSpacingAllocation::setContract);
			}
		};

		CommonImages.setImageDescriptors(addUnshippedCargoesRows, IconPaths.Plus);
		buttonManager.getToolbarManager().add(addUnshippedCargoesRows);

		final Action deleteUnshippedCargoesRows = createDeleteTableRowsAction(eViewer, "Unshipped Cargo Rows");
		CommonImages.setImageDescriptors(deleteUnshippedCargoesRows, IconPaths.Delete);
		deleteUnshippedCargoesRows.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteUnshippedCargoesRows);

		unshippedCargoesPackAction = PackActionFactory.createPackColumnsAction(eViewer);
		buttonManager.getToolbarManager().add(new ActionContributionItem(unshippedCargoesPackAction));

		buttonManager.getToolbarManager().update(true);

		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();

		eViewer.addTypicalColumn("Contract", new ReadOnlyManipulatorWrapper<>(
				new SingleReferenceManipulator(ADPPackage.eINSTANCE.getSpacingAllocation_Contract(), sel.getReferenceValueProviderCache(), sel.getDefaultCommandHandler())));
		eViewer.addTypicalColumn("Cargo Count", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getFobSpacingAllocation_CargoCount(), sel.getDefaultCommandHandler()));
		eViewer.addTypicalColumn("Min Spacing", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getFobSpacingAllocation_MinSpacing(), sel.getDefaultCommandHandler()));
		eViewer.addTypicalColumn("Max Spacing", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getFobSpacingAllocation_MaxSpacing(), sel.getDefaultCommandHandler()));
		eViewer.setStatusProvider(statusProvider);

		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.SPACING_PROFILE__FOB_SPACING_ALLOCATIONS);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 150;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		eViewer.addSelectionChangedListener(event -> {
			deleteUnshippedCargoesRows.setEnabled(!event.getSelection().isEmpty());
			if (event.getStructuredSelection().size() == 1) {
				final Object selection = event.getStructuredSelection().getFirstElement();
				if (selection instanceof final FobSpacingAllocation fobSpacingAllocation) {
				} else {
				}
			} else {
			}
		});
		return eViewer;
	}

	private EObjectTableViewer createShippedCargoesViewers(final Composite parent) {
		final Group shippedCargoesGroup = buildTableGroup(parent, "Shipped Cargoes");
		final DetailToolbarManager buttonManager = new DetailToolbarManager(shippedCargoesGroup, SWT.TOP);
		final EObjectTableViewer eViewer = new EObjectTableViewer(shippedCargoesGroup, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);
		final Action addShippedCargoesRows = new Action("Add") {
			@Override
			public void run() {
				final List<SalesContract> selectedSalesContracts = openSelectionDialogBox(parent, oldValue, SpacingProfile::getDesSpacingAllocations, DesSpacingAllocation::getContract,
						ADPPackage.Literals.DES_SPACING_ALLOCATION, ADPPackage.Literals.SPACING_ALLOCATION__CONTRACT, "DES Contract");
				updateStateWithSelection(selectedSalesContracts, oldValue, SpacingProfile::getDesSpacingAllocations, DesSpacingAllocation::getContract, "Contracts",
						ADPPackage.Literals.SPACING_PROFILE__DES_SPACING_ALLOCATIONS, ADPFactory.eINSTANCE::createDesSpacingAllocation, DesSpacingAllocation::setContract);
			}
		};

		CommonImages.setImageDescriptors(addShippedCargoesRows, IconPaths.Plus);
		buttonManager.getToolbarManager().add(addShippedCargoesRows);

		final Action deleteShippedCargoesRows = createDeleteTableRowsAction(eViewer, "Shipped Cargo Rows");
		CommonImages.setImageDescriptors(deleteShippedCargoesRows, IconPaths.Delete);
		deleteShippedCargoesRows.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteShippedCargoesRows);

		shippedCargoesPackAction = PackActionFactory.createPackColumnsAction(eViewer);
		buttonManager.getToolbarManager().add(new ActionContributionItem(shippedCargoesPackAction));

		buttonManager.getToolbarManager().update(true);

		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		eViewer.addTypicalColumn("Contract", new ReadOnlyManipulatorWrapper<>(
				new SingleReferenceManipulator(ADPPackage.eINSTANCE.getSpacingAllocation_Contract(), sel.getReferenceValueProviderCache(), sel.getDefaultCommandHandler())));
		eViewer.addTypicalColumn("Vessel", new SingleReferenceManipulator(ADPPackage.eINSTANCE.getDesSpacingAllocation_Vessel(), sel.getReferenceValueProviderCache(), sel.getDefaultCommandHandler()));
		eViewer.addTypicalColumn("Port", new SingleReferenceManipulator(ADPPackage.eINSTANCE.getDesSpacingAllocation_Port(), sel.getReferenceValueProviderCache(), sel.getDefaultCommandHandler()));
		eViewer.addColumn("Cargo Count", new ICellRenderer() {

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
				if (object instanceof final DesSpacingAllocation desSpacingAllocation) {
					return Integer.toString(desSpacingAllocation.getDesSpacingRows().size());
				}
				return null;
			}

			@Override
			public boolean isValueUnset(Object object) {
				return false;
			}

			@Override
			public @Nullable Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
				return null;
			}
		}, new ICellManipulator() {

			@Override
			public void setValue(Object object, Object value) {
			}

			@Override
			public void setParent(Object parent, Object object) {
			}

			@Override
			public void setExtraCommandsHook(IExtraCommandsHook extraCommandsHook) {
			}

			@Override
			public Object getValue(Object object) {
				if (object instanceof final DesSpacingAllocation desSpacingAllocation) {
					return desSpacingAllocation.getDesSpacingRows().size();
				}
				return null;
			}

			@Override
			public CellEditor getCellEditor(Composite parent, Object object) {
				return null;
			}

			@Override
			public boolean canEdit(Object object) {
				return false;
			}
		});
		eViewer.setStatusProvider(statusProvider);

		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.SPACING_PROFILE__DES_SPACING_ALLOCATIONS);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 150;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		eViewer.addSelectionChangedListener(event -> {
			deleteShippedCargoesRows.setEnabled(!event.getSelection().isEmpty());
			if (event.getStructuredSelection().size() == 1) {
				final Object selection = event.getStructuredSelection().getFirstElement();
				if (selection instanceof final DesSpacingAllocation desSpacingAllocation) {
					final SalesContract selectedContract = desSpacingAllocation.getContract();
					if (selectedContract != null) {
						final String contractName = selectedContract.getName();
						if (contractName != null) {
							shippedCargoDetailsGroup.setText(contractName + " cargo details");
						} else {
							shippedCargoDetailsGroup.setText("Cargo details");
						}
					} else {
						shippedCargoDetailsGroup.setText("Cargo details");
					}
					shippedCargoDetailsViewer.setInput(desSpacingAllocation);
					shippedCargoDetailsPackAction.run();
					shippedCargoDetailsGroup.setVisible(true);
				} else {
					shippedCargoDetailsGroup.setVisible(false);
				}
			} else {
				shippedCargoDetailsGroup.setVisible(false);
			}
		});
		return eViewer;
	}

	private EObjectTableViewer createShippedCargoDetailsViewer(final Composite parent) {
		shippedCargoDetailsGroup = buildTableGroup(parent, "Cargo Details");
		final DetailToolbarManager buttonManager = new DetailToolbarManager(shippedCargoDetailsGroup, SWT.TOP);
		final EObjectTableViewer eViewer = new EObjectTableViewer(shippedCargoDetailsGroup, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);
		final Action addShippedCargoDetailsRow = new Action("Add") {
			@Override
			public void run() {
				Object selection = shippedCargoesViewer.getStructuredSelection().getFirstElement();
				if (selection instanceof final DesSpacingAllocation desSpacingAllocation) {
					final DesSpacingRow desSpacingRow = ADPFactory.eINSTANCE.createDesSpacingRow();
					final Command cmd = AddCommand.create(commandHandler.getEditingDomain(), desSpacingAllocation, ADPPackage.Literals.DES_SPACING_ALLOCATION__DES_SPACING_ROWS, desSpacingRow);
					if (cmd.canExecute()) {
						commandHandler.handleCommand(cmd);
					}
				}
			}
		};

		CommonImages.setImageDescriptors(addShippedCargoDetailsRow, IconPaths.Plus);
		buttonManager.getToolbarManager().add(addShippedCargoDetailsRow);

		final Action deleteShippedCargoDetailsRows = createDeleteTableRowsAction(eViewer, "Shipped Cargo Rows");
		CommonImages.setImageDescriptors(deleteShippedCargoDetailsRows, IconPaths.Delete);
		deleteShippedCargoDetailsRows.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteShippedCargoDetailsRows);

		shippedCargoDetailsPackAction = PackActionFactory.createPackColumnsAction(eViewer);
		buttonManager.getToolbarManager().add(new ActionContributionItem(shippedCargoDetailsPackAction));

		buttonManager.getToolbarManager().update(true);

		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		eViewer.addTypicalColumn("Min Discharge Date", new LocalDateTimeAttributeManipulator(ADPPackage.eINSTANCE.getDesSpacingRow_MinDischargeDate(), sel.getDefaultCommandHandler()));
		eViewer.addTypicalColumn("Max Discharge Date", new LocalDateTimeAttributeManipulator(ADPPackage.eINSTANCE.getDesSpacingRow_MaxDischargeDate(), sel.getDefaultCommandHandler()));
		
		eViewer.setStatusProvider(statusProvider);

		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.DES_SPACING_ALLOCATION__DES_SPACING_ROWS);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 150;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		eViewer.addSelectionChangedListener(event -> {
			deleteShippedCargoDetailsRows.setEnabled(!event.getSelection().isEmpty());
			if (event.getStructuredSelection().size() == 1) {
				final Object selection = event.getStructuredSelection().getFirstElement();
				if (selection instanceof final DesSpacingAllocation desSpacingAllocation) {
				} else {
				}
			} else {
			}
		});
		shippedCargoDetailsGroup.setVisible(false);
		return eViewer;
	}

	private <R extends EObject, S extends EObject, T extends EObject> List<T> openSelectionDialogBox(final Control cellEditorWindow, final S target, final Function<S, List<R>> rowsExtractor,
			final Function<R, T> valueExtractor, final EClass owner, final EReference field, @NonNull final String selectionLabel) {
		final IReferenceValueProvider valueProvider = commandHandler.getReferenceValueProviderProvider().getReferenceValueProvider(owner, field);
		final List<Pair<String, EObject>> options = valueProvider.getAllowedValues(target, field);
		final ListSelectionDialog dlg = new ListSelectionDialog(cellEditorWindow.getShell(), options.toArray(), new ArrayContentProvider(), new LabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
		dlg.setTitle(String.format("%s Selection", selectionLabel));
		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<>();
		final Set<T> sel = rowsExtractor.apply(target).stream().map(valueExtractor).collect(Collectors.toSet());
		if (sel != null) {
			for (final Pair<String, EObject> p : options) {
				if (sel.contains(p.getSecond())) {
					selectedOptions.add(p);
				}
			}
		}
		dlg.setInitialSelections(selectedOptions.toArray());

		dlg.addColumn(selectionLabel, new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});

		if (dlg.open() == Window.OK) {
			final Object[] result = dlg.getResult();
			final ArrayList<T> resultList = new ArrayList<>();
			for (final Object o : result) {
				resultList.add(((Pair<String, T>) o).getSecond());
			}
			return resultList;
		}
		return null;
	}

	private <R extends EObject, S extends EObject, T extends EObject> List<T> openSelectionDialogBox(final Control cellEditorWindow, final S target, final Function<S, List<R>> rowsExtractor,
			final Function<R, T> valueExtractor, final EClass owner, final EReference field, final EReference reference, @NonNull final String selectionLabel) {
		final IReferenceValueProvider valueProvider = commandHandler.getReferenceValueProviderProvider().getReferenceValueProvider(owner, reference);
		final List<Pair<String, EObject>> options = valueProvider.getAllowedValues(target, field);
		final ListSelectionDialog dlg = new ListSelectionDialog(cellEditorWindow.getShell(), options.toArray(), new ArrayContentProvider(), new LabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
		dlg.setTitle(String.format("%s Selection", selectionLabel));
		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<>();
		final Set<T> sel = rowsExtractor.apply(target).stream().map(valueExtractor).collect(Collectors.toSet());
		if (sel != null) {
			for (final Pair<String, EObject> p : options) {
				if (sel.contains(p.getSecond())) {
					selectedOptions.add(p);
				}
			}
		}
		dlg.setInitialSelections(selectedOptions.toArray());

		dlg.addColumn(selectionLabel, new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});

		if (dlg.open() == Window.OK) {
			final Object[] result = dlg.getResult();
			final ArrayList<T> resultList = new ArrayList<>();
			for (final Object o : result) {
				resultList.add(((Pair<String, T>) o).getSecond());
			}
			return resultList;
		}
		return null;
	}

	private <R extends EObject, S extends EObject, T extends EObject> void updateStateWithSelection(final List<T> selectedValues, final S selectedRow, final Function<S, List<R>> rowsExtractor,
			final Function<R, T> valueExtractor, final String elementString, final EReference feature, final Supplier<R> rowCreator, final BiConsumer<R, T> valueSetter) {
		if (selectedValues != null) {
			final Set<T> afterSelectionSet = new HashSet<>(selectedValues);
			final List<R> rowsToRemove = new LinkedList<>();
			rowsExtractor.apply(selectedRow).stream().forEach(row -> {
				final T oldRowValue = valueExtractor.apply(row);
				if (afterSelectionSet.contains(oldRowValue)) {
					afterSelectionSet.remove(oldRowValue);
				} else {
					rowsToRemove.add(row);
				}
			});
			final CompoundCommand cmd = new CompoundCommand(String.format("Add %s", elementString));
			if (!rowsToRemove.isEmpty()) {
				cmd.append(RemoveCommand.create(commandHandler.getEditingDomain(), selectedRow, feature, rowsToRemove));
			}
			if (!afterSelectionSet.isEmpty()) {
				final List<R> newRows = afterSelectionSet.stream() //
						.map(s -> {
							final R newRow = rowCreator.get();
							valueSetter.accept(newRow, s);
							return newRow;
						}) //
						.collect(Collectors.toList());
				cmd.append(AddCommand.create(commandHandler.getEditingDomain(), selectedRow, feature, newRows));
			}
			if (!cmd.isEmpty()) {
				commandHandler.handleCommand(cmd, selectedRow, feature);
			}
		}
	}

	private @NonNull Group buildTableGroup(final Composite parent, final String groupName) {
		final Group group = new Group(parent, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setBackground(systemWhite);
		group.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
		group.setText(groupName);
		return group;
	}

	private Action createDeleteTableRowsAction(final EObjectTableViewer tableViewer, final String deleteCommandText) {
		return new Action("Delete") {
			@Override
			public void run() {
				if (tableViewer != null) {
					final ISelection selection = tableViewer.getSelection();
					if (selection.isEmpty()) {
						return;
					}
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
						final CompoundCommand cc = new CompoundCommand(String.format("Delete %s", deleteCommandText));
						final EditingDomain ed = dialogContext.getScenarioEditingLocation().getEditingDomain();
						final EObject firstLine = (EObject) iStructuredSelection.getFirstElement();
						cc.append(RemoveCommand.create(ed, firstLine.eContainer(), firstLine.eContainingFeature(), iStructuredSelection.toList()));
						ed.getCommandStack().execute(cc);
					}
				}
			}
		};
	}
}
