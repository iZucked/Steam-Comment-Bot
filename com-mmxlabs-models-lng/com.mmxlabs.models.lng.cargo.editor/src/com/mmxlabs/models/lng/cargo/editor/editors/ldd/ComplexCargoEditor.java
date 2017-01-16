/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors.ldd;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.databinding.edit.IEMFEditListProperty;
import org.eclipse.emf.databinding.edit.IEMFEditValueProperty;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoEditorMenuHelper;
import com.mmxlabs.models.lng.cargo.ui.editorpart.ContractManipulator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DefaultDialogEditingContext;
import com.mmxlabs.models.ui.editors.dialogs.DialogValidationSupport;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.dialogs.NullDialogController;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.util.emfpath.EMFPath;

/**
 * Custom dialog editor for complex cargoes, focussing on LDD cargoes.
 * 
 * @author Simon Goodall
 * 
 */
public class ComplexCargoEditor extends Dialog {

	private Text cargoName;

	private DialogValidationSupport validationSupport;

	private final ObservablesManager observablesManager = new ObservablesManager();
	private final EMFDataBindingContext dbc = new EMFDataBindingContext();

	private GridTableViewer viewer;

	private Cargo cargo;

	private final Map<Object, IStatus> validationErrors = new HashMap<Object, IStatus>();

	private final List<Command> executedCommands = new LinkedList<Command>();

	// For LDD we need to link min.max discharge volumes
	private final boolean linkDischargeVolumes = true;

	private final IDialogEditingContext dialogContext;

	public ComplexCargoEditor(final IShellProvider parentShell, final IScenarioEditingLocation scenarioEditingLocation, final boolean isCargoObjectNew) {
		super(parentShell);
		this.dialogContext = new DefaultDialogEditingContext(new NullDialogController(), scenarioEditingLocation, false, isCargoObjectNew);
	}

	public ComplexCargoEditor(final Shell parentShell, final IScenarioEditingLocation scenarioEditingLocation, final boolean isCargoObjectNew) {
		super(parentShell);
		this.dialogContext = new DefaultDialogEditingContext(new NullDialogController(), scenarioEditingLocation, false, isCargoObjectNew);
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Edit Complex Cargo (LDD)");
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite c = (Composite) super.createDialogArea(parent);
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();

		validationSupport = new DialogValidationSupport(sel.getExtraValidationContext());

		final Composite area = new Composite(c, SWT.NONE);
		area.setLayout(new GridLayout(1, true));
		{
			final Group g = new Group(area, SWT.DEFAULT);
			g.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, true, false));
			g.setLayout(new GridLayout(2, false));

			final Label l = new Label(g, SWT.NONE);
			l.setText("Name");
			l.setLayoutData(new GridData(40, -1));
			cargoName = new Text(g, SWT.BORDER);
			cargoName.setLayoutData(new GridData(80, -1));
		}
		{
			final Group g = new Group(area, SWT.DEFAULT);
			viewer = new GridTableViewer(g);
			GridViewerHelper.configureLookAndFeel(viewer);

			ColumnViewerToolTipSupport.enableFor(viewer);

			viewer.getGrid().setLinesVisible(true);
			viewer.getGrid().setHeaderVisible(true);

			g.setLayout(new GridLayout(1, true));

			final ObservableListContentProvider contentProvider = new ObservableListContentProvider();
			viewer.setContentProvider(contentProvider);
			viewer.getGrid().setLayoutData(new GridData(700, 150));
			{
				final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
				column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				column.getColumn().setText("T");
				column.getColumn().setWidth(15);
				// final ContractManipulator manipulator = new ContractManipulator(scenarioEditingLocation.getReferenceValueProviderCache(), scenarioEditingLocation.getEditingDomain());
				column.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(final Object element) {
						if (element instanceof LoadSlot) {
							return "L";
						} else if (element instanceof DischargeSlot) {
							return "D";
						}
						return "?";
					}
				});
				//
				// final CellManipulatorEditingSupport es = new CellManipulatorEditingSupport(column.getViewer(), viewer, manipulator, new EMFPath(false));
				// column.setEditingSupport(es);
			}

			{
				final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
				column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				column.getColumn().setText("Name");
				column.getColumn().setWidth(60);

				final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), sel.getEditingDomain());
				column.setLabelProvider(new CellRendererColumnLabelProvider(viewer, manipulator, validationErrors, new EMFPath(false)));

				final CellManipulatorEditingSupport es = new CellManipulatorEditingSupport(column.getViewer(), viewer, manipulator, new EMFPath(false));
				column.setEditingSupport(es);
			}

			{
				final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
				column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				column.getColumn().setText("Contract");
				column.getColumn().setWidth(150);
				final ContractManipulator manipulator = new ContractManipulator(sel.getReferenceValueProviderCache(), sel.getEditingDomain());
				column.setLabelProvider(new CellRendererColumnLabelProvider(viewer, manipulator, validationErrors, new EMFPath(false)));

				final CellManipulatorEditingSupport es = new CellManipulatorEditingSupport(column.getViewer(), viewer, manipulator, new EMFPath(false));
				column.setEditingSupport(es);
			}

			{

				final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
				column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				column.getColumn().setText("Port");
				column.getColumn().setWidth(100);

				final SingleReferenceManipulator manipulator = new SingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Port(), sel.getReferenceValueProviderCache(), sel.getEditingDomain());
				column.setLabelProvider(new CellRendererColumnLabelProvider(viewer, manipulator, validationErrors, new EMFPath(false)));

				final CellManipulatorEditingSupport es = new CellManipulatorEditingSupport(column.getViewer(), viewer, manipulator, new EMFPath(false));
				column.setEditingSupport(es);
			}

			{
				final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
				column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				column.getColumn().setText("Date");
				column.getColumn().setWidth(100);
				// column.setLabelProvider(new GenericMapCellLabelProvider("{0}", attributeMap));

				final LocalDateAttributeManipulator manipulator = new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), sel.getEditingDomain());
				column.setLabelProvider(new CellRendererColumnLabelProvider(viewer, manipulator, validationErrors, new EMFPath(false)));

				final CellManipulatorEditingSupport es = new CellManipulatorEditingSupport(column.getViewer(), viewer, manipulator, new EMFPath(false));
				column.setEditingSupport(es);
			}
			{
				final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
				column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				column.getColumn().setText("Volume (Min)");
				column.getColumn().setWidth(100);

				final NumericAttributeManipulator manipulator = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MinQuantity(), sel.getEditingDomain());
				column.setLabelProvider(new CellRendererColumnLabelProvider(viewer, manipulator, validationErrors, new EMFPath(false)));

				final CellManipulatorEditingSupport es = new CellManipulatorEditingSupport(column.getViewer(), viewer, manipulator, new EMFPath(false)) {
					@Override
					protected void setValue(final Object element, final Object value) {
						if (linkDischargeVolumes && element instanceof DischargeSlot) {
							final DischargeSlot dischargeSlot = (DischargeSlot) element;

							final CompoundCommand cmd = new CompoundCommand("Set discharge volumes");

							cmd.append(SetCommand.create(sel.getEditingDomain(), dischargeSlot, CargoPackage.eINSTANCE.getSlot_MinQuantity(), value));
							cmd.append(SetCommand.create(sel.getEditingDomain(), dischargeSlot, CargoPackage.eINSTANCE.getSlot_MaxQuantity(), value));
							sel.getEditingDomain().getCommandStack().execute(cmd);

						} else {
							super.setValue(element, value);
						}

					}
				};
				column.setEditingSupport(es);
			}
			{
				final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
				column.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				column.getColumn().setText("Volume (Max)");
				column.getColumn().setWidth(100);

				final NumericAttributeManipulator manipulator = new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), sel.getEditingDomain());
				column.setLabelProvider(new CellRendererColumnLabelProvider(viewer, manipulator, validationErrors, new EMFPath(false)));

				final CellManipulatorEditingSupport es = new CellManipulatorEditingSupport(column.getViewer(), viewer, manipulator, new EMFPath(false)) {
					@Override
					protected void setValue(final Object element, final Object value) {
						if (linkDischargeVolumes && element instanceof DischargeSlot) {
							final DischargeSlot dischargeSlot = (DischargeSlot) element;

							final CompoundCommand cmd = new CompoundCommand("Set discharge volumes");

							cmd.append(SetCommand.create(sel.getEditingDomain(), dischargeSlot, CargoPackage.eINSTANCE.getSlot_MinQuantity(), value));
							cmd.append(SetCommand.create(sel.getEditingDomain(), dischargeSlot, CargoPackage.eINSTANCE.getSlot_MaxQuantity(), value));
							sel.getEditingDomain().getCommandStack().execute(cmd);

						} else {
							super.setValue(element, value);
						}

					}
				};
				column.setEditingSupport(es);
			}

			final MenuManager mgr = new MenuManager();

			viewer.getGrid().addMenuDetectListener(new MenuDetectListener() {

				private final LNGScenarioModel scenarioModel = (LNGScenarioModel) sel.getRootObject();

				private Menu menu;
				private final CargoEditorMenuHelper menuHelper = new CargoEditorMenuHelper(viewer.getGrid().getShell(), dialogContext.getScenarioEditingLocation(), scenarioModel);

				@Override
				public void menuDetected(final MenuDetectEvent e) {

					// if (locked) {
					// return;
					// }
					final Point mousePoint = viewer.getGrid().toControl(new Point(e.x, e.y));
					final GridColumn column = viewer.getGrid().getColumn(mousePoint);

					final GridItem item = viewer.getGrid().getItem(mousePoint);
					if (item == null) {
						return;
					}
					final Object data = item.getData();
					// if (data instanceof RowData) {

					// final RowData rowDataItem = (RowData) data;
					final int idx = cargo.getSlots().indexOf(data);

					if (menu == null) {
						menu = mgr.createContextMenu(viewer.getGrid());
					}
					mgr.removeAll();

					final IMenuListener listener = menuHelper.createSwapSlotsMenuListener(cargo.getSlots(), idx);
					listener.menuAboutToShow(mgr);

					menu.setVisible(true);
					// }
				}
			});
		}

		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {

				if (e1 instanceof LoadSlot) {
					return -1;
				} else if (e2 instanceof LoadSlot) {
					return 1;
				}

				final Slot s1 = (Slot) e1;
				final Slot s2 = (Slot) e2;

				if (s1.getWindowStart() != null && s2.getWindowStart() != null) {
					return s1.getWindowStart().compareTo(s2.getWindowStart());
				}

				return super.compare(viewer, e1, e2);
			}
		});

		return c;
	}

	public void setCargo(final Cargo cargo) {
		this.cargo = cargo;
		observablesManager.runAndCollect(new Runnable() {

			@Override
			public void run() {
				final EditingDomain editingDomain = dialogContext.getScenarioEditingLocation().getEditingDomain();
				{
					final IEMFEditValueProperty property = EMFEditProperties.value(editingDomain, FeaturePath.fromList(MMXCorePackage.eINSTANCE.getNamedObject_Name()));
					final IObservableValue modelObservableValue = property.observe(cargo);
					final ISWTObservableValue targetObservableValue = WidgetProperties.text(SWT.Modify).observe(cargoName);
					dbc.bindValue(targetObservableValue, modelObservableValue);
				}

				final IEMFEditListProperty prop = EMFEditProperties.list(editingDomain, CargoPackage.eINSTANCE.getCargo_Slots());

				viewer.setInput(prop.observe(cargo));

			}
		});
	}

	public int open(final Cargo c) {

		create();
		setCargo(c);
		return open();
	}

	@Override
	public int open() {
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();

		validationSupport = new DialogValidationSupport(new DefaultExtraValidationContext(sel.getExtraValidationContext(), false));

		final List<EObject> validationTargets = new ArrayList<EObject>();
		validationTargets.add(cargo);
		validationTargets.addAll(cargo.getSlots());
		validationSupport.setValidationTargets(validationTargets);

		final CommandStack commandStack = sel.getEditingDomain().getCommandStack();
		final CommandStackListener listener = new CommandStackListener() {

			@Override
			public void commandStackChanged(final EventObject event) {
				executedCommands.add(commandStack.getMostRecentCommand());
				validate();
			}
		};
		commandStack.addCommandStackListener(listener);
		try {
			sel.pushExtraValidationContext(validationSupport.getValidationContext());
			validate();
			return super.open();
		} finally {
			commandStack.removeCommandStackListener(listener);

			sel.popExtraValidationContext();

		}
	}

	public Cargo getCargo() {
		return cargo;
	}

	private void validate() {
		final IStatus status = validationSupport.validate();

		validationErrors.clear();
		validationSupport.processStatus(status, validationErrors);

		viewer.refresh();
	}

	public List<Command> getExecutedCommands() {
		return executedCommands;
	}

}
