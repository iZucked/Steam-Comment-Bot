/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.ColumnLabelProviderFactory;
import com.mmxlabs.models.ui.NumberFormatterFactory.ExtendedIntegerFormatter;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerValidationSupport;
import com.mmxlabs.models.ui.tabular.GridViewerEditingSupport;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.manipulators.MultiAttributeInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the bottom of the composite which contains a fuel curve table.
 * 
 * @author hinton
 * 
 */
public class PeriodDistributionProfileConstraintDetailComposite extends Composite implements IDisplayComposite {

	// private IDisplayComposite delegate;
	private ICommandHandler commandHandler;
	private GridTableViewer tableViewer;
	private EObjectTableViewerValidationSupport validationSupport;

	private final Color errorColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private final Color warningColour = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
	private final Color lockedColour = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

	private DefaultStatusProvider statusProvider = new DefaultStatusProvider() {

		private IStatus status;

		@Override
		public void fireStatusChanged(final IStatus status) {
			this.status = status;
			super.fireStatusChanged(status);
		}

		@Override
		public IStatus getStatus() {
			return status;
		}
	};

	private Function<Object, Color> colourProvider = (element) -> {

		final IStatus s = validationSupport.getValidationErrors().get(element);
		if (s != null) {
			if (s.getSeverity() == IStatus.ERROR) {
				return errorColour;
			} else if (s.getSeverity() == IStatus.WARNING) {
				return warningColour;
			}
		}
		return null;

	};

	public PeriodDistributionProfileConstraintDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style);
		toolkit.adapt(this);
		setLayout(new GridLayout(1, false));
		// delegate = new DefaultDetailComposite(this, style, toolkit);
		//
		// delegate.getComposite().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Composite lblComposite = new Composite(this, SWT.NONE);
		lblComposite.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).create());
		lblComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		tableViewer = new GridTableViewer(this, SWT.FULL_SELECTION | SWT.V_SCROLL);

		final ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(tableViewer) {
			@Override
			protected boolean isEditorActivationEvent(final ColumnViewerEditorActivationEvent event) {
				final boolean activate = event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION //
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC //
						|| event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL;
				if (activate) {
					return true;
				}
				if (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED) {
					if (event.keyCode == KeyLookupFactory.getDefault().formalKeyLookup(IKeyLookup.ENTER_NAME)) {
						return true;
					}
				}
				return false;
			}
		};

		GridViewerEditor.create(tableViewer, actSupport, ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.KEYBOARD_ACTIVATION);

		final Grid table = tableViewer.getGrid();
		GridViewerHelper.configureLookAndFeel(tableViewer);

		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		validationSupport = new EObjectTableViewerValidationSupport(tableViewer);
		validationSupport.setStatusProvider(statusProvider);

		table.setLayoutData(GridDataFactory.fillDefaults().hint(100, 200).create());

		final GridViewerColumn rangeColumn = new GridViewerColumn(tableViewer, SWT.NONE);
		rangeColumn.getColumn().setWidth(200);
		final GridViewerColumn minCargoesColumn = new GridViewerColumn(tableViewer, SWT.NONE);
		minCargoesColumn.getColumn().setWidth(50);
		final GridViewerColumn maxCargoesColumn = new GridViewerColumn(tableViewer, SWT.NONE);
		maxCargoesColumn.getColumn().setWidth(50);

		GridViewerHelper.configureLookAndFeel(rangeColumn);
		GridViewerHelper.configureLookAndFeel(minCargoesColumn);
		GridViewerHelper.configureLookAndFeel(maxCargoesColumn);

		rangeColumn.getColumn().setText("Range");
		minCargoesColumn.getColumn().setText("Min");
		maxCargoesColumn.getColumn().setText("Max");

		{
			rangeColumn.setEditingSupport(
					new GridViewerEditingSupport(tableViewer, table, () -> new MultiAttributeInlineEditor(ADPPackage.Literals.PERIOD_DISTRIBUTION__RANGE, commandHandler.getEditingDomain(), a -> {
						return String.format("%s %02d", //
								((YearMonth) a).getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()), //
								((YearMonth) a).getYear());
					}, () -> {
						final List<Object> toAdd = new LinkedList<>();
						final ADPModel adpModel = ADPModelUtil.getADPModel(oldValue);
						if (adpModel != null) {
							YearMonth start = adpModel.getYearStart();
							while (start.isBefore(adpModel.getYearEnd())) {

								toAdd.add(start);
								start = start.plusMonths(1);
							}
						}
						return toAdd;
					})));
		}
		// Use hyphen for consecutive months, comma otherwise
		rangeColumn.setLabelProvider(ColumnLabelProviderFactory.make(PeriodDistribution.class, "", periodDistribution -> {
			return ADPModelUtil.getPeriodDistributionRangeString(periodDistribution);
		}, colourProvider));

		minCargoesColumn.setLabelProvider(ColumnLabelProviderFactory.make(PeriodDistribution.class, "", periodDistribution -> {
			if (periodDistribution.isSetMinCargoes()) {
				final int num = periodDistribution.getMinCargoes();
				return String.format("%d", num);
			}
			return "";
		}, colourProvider));
		maxCargoesColumn.setLabelProvider(ColumnLabelProviderFactory.make(PeriodDistribution.class, "", periodDistribution -> {
			if (periodDistribution.isSetMaxCargoes()) {
				final int num = periodDistribution.getMaxCargoes();
				return String.format("%d", num);
			}
			return "";
		}, colourProvider));

		minCargoesColumn.setEditingSupport(
				new GridViewerEditingSupport(tableViewer, table, () -> new NumericAttributeManipulator(ADPPackage.Literals.PERIOD_DISTRIBUTION__MIN_CARGOES, commandHandler.getEditingDomain())));
		maxCargoesColumn.setEditingSupport(
				new GridViewerEditingSupport(tableViewer, table, () -> new NumericAttributeManipulator(ADPPackage.Literals.PERIOD_DISTRIBUTION__MAX_CARGOES, commandHandler.getEditingDomain())));

		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object inputElement) {
				final PeriodDistributionProfileConstraint model = (PeriodDistributionProfileConstraint) inputElement;
				final List<?> list = (List<?>) model.eGet(ADPPackage.Literals.PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT__DISTRIBUTIONS);
				if (list == null) {
					return new Object[0];
				}
				final Object[] things = list.toArray();
				// Arrays.sort((PeriodDistribution[]) things, (a, b) -> a.getStart() == null ? -1 : b.getStart() == null ? 1 : a.getStart().compareTo(b.getStart()));

				return things;
			}
		});

		final Composite buttons = toolkit.createComposite(this);

		buttons.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		final GridLayout buttonLayout = new GridLayout(7, false);
		buttons.setLayout(buttonLayout);
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;

		final Button add = toolkit.createButton(buttons, "+", SWT.NONE);
		add.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		FormattedText[] minMaxControls = new FormattedText[2];

		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final PeriodDistribution newDistribution = createPeriodDistribution(minMaxControls);

				commandHandler.handleCommand(
						AddCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions(), newDistribution), oldValue,
						ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions());
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(newDistribution));
			}
		});

		final Button remove = toolkit.createButton(buttons, "-", SWT.NONE);
		remove.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		remove.setEnabled(false);
		tableViewer.addSelectionChangedListener((event) -> remove.setEnabled(!event.getSelection().isEmpty()));

		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = tableViewer.getSelection();
				if (sel.isEmpty())
					return;
				if (sel instanceof IStructuredSelection) {
					final PeriodDistribution fc = (PeriodDistribution) ((IStructuredSelection) sel).getFirstElement();
					commandHandler.handleCommand(RemoveCommand.create(commandHandler.getEditingDomain(), fc.eContainer(), fc.eContainingFeature(), fc), oldValue,
							ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions());
					tableViewer.refresh();
				}
			}
		});

		{
			{

				final Consumer<LocalMenuHelper> menuCreator = (helper) -> {
					final ADPModel adpModel = ADPModelUtil.getADPModel(oldValue);
					if (adpModel != null) {
						helper.addAction(new RunnableAction("Monthly", () -> {

							final CompoundCommand cc = new CompoundCommand("Replace period data");
							if (!oldValue.getDistributions().isEmpty()) {
								cc.append(RemoveCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions(),
										oldValue.getDistributions()));
							}
							YearMonth start = adpModel.getYearStart();
							final List<PeriodDistribution> toAdd = new LinkedList<>();
							while (start.isBefore(adpModel.getYearEnd())) {
								final PeriodDistribution d = createPeriodDistribution(minMaxControls);

								d.getRange().add(start);
								toAdd.add(d);
								start = start.plusMonths(1);

							}

							cc.append(AddCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions(), toAdd));
							commandHandler.handleCommand(cc, oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions());
							ViewerHelper.refresh(tableViewer, false);
						}));
						helper.addAction(new RunnableAction("Bi-monthly (overlaps)", () -> {

							final CompoundCommand cc = new CompoundCommand("Replace period data");
							if (!oldValue.getDistributions().isEmpty()) {
								cc.append(RemoveCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions(),
										oldValue.getDistributions()));
							}
							YearMonth start = adpModel.getYearStart();
							final List<PeriodDistribution> toAdd = new LinkedList<>();
							while (start.isBefore(adpModel.getYearEnd().minusMonths(1))) {
								final PeriodDistribution d = createPeriodDistribution(minMaxControls);

								d.getRange().add(start);
								d.getRange().add(start.plusMonths(1));
								toAdd.add(d);
								start = start.plusMonths(1);
							}

							cc.append(AddCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions(), toAdd));
							commandHandler.handleCommand(cc, oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions());
							ViewerHelper.refresh(tableViewer, false);
						}));
						helper.addAction(new RunnableAction("Quarterly", () -> {

							final CompoundCommand cc = new CompoundCommand("Replace period data");
							if (!oldValue.getDistributions().isEmpty()) {
								cc.append(RemoveCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions(),
										oldValue.getDistributions()));
							}
							YearMonth start = adpModel.getYearStart();
							// Start of calendar quarter
							start = start.withMonth(1 + ((start.getMonthValue() - 1) / 3 * 3));
							final List<PeriodDistribution> toAdd = new LinkedList<>();
							while (start.isBefore(adpModel.getYearEnd())) {
								final PeriodDistribution d = createPeriodDistribution(minMaxControls);

								d.getRange().add(start);
								d.getRange().add(start.plusMonths(1));
								d.getRange().add(start.plusMonths(2));
								toAdd.add(d);
								start = start.plusMonths(3);
							}

							cc.append(AddCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions(), toAdd));
							commandHandler.handleCommand(cc, oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions());
							ViewerHelper.refresh(tableViewer, false);
						}));
						helper.addAction(new RunnableAction("Yearly", () -> {

							final CompoundCommand cc = new CompoundCommand("Replace period data");
							if (!oldValue.getDistributions().isEmpty()) {
								cc.append(RemoveCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions(),
										oldValue.getDistributions()));
							}
							YearMonth start = adpModel.getYearStart();
							final List<PeriodDistribution> toAdd = new LinkedList<>();

							final Contract c = ((ContractProfile<?, ?>) oldValue.eContainer()).getContract();
							final YearMonth contractStart = c.getStartDate();

							PeriodDistribution d = createPeriodDistribution(minMaxControls);
							while (start.isBefore(adpModel.getYearEnd())) {
								if (!toAdd.contains(d)) {
									toAdd.add(d);
								}
								d.getRange().add(start);
								start = start.plusMonths(1);
								if (contractStart != null && start.getMonth() == contractStart.getMonth()) {
									d = createPeriodDistribution(minMaxControls);
								}
							}

							cc.append(AddCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions(), toAdd));
							commandHandler.handleCommand(cc, oldValue, ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions());
							ViewerHelper.refresh(tableViewer, false);
						}));
					}
				};
				final Button generateAction = toolkit.createButton(buttons, "Reset", SWT.PUSH);
				final LocalMenuHelper helper = new LocalMenuHelper(buttons);

				generateAction.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						helper.clearActions();
						menuCreator.accept(helper);
						helper.open();
					}
				});

				generateAction.setToolTipText("Replace existing values and populate a new set of values");

				{
					Label l = toolkit.createLabel(buttons, "Min");
					FormattedText txt = new FormattedText(buttons);
					final IntegerFormatter inner = new IntegerFormatter("##");
					final Supplier<String> overrideStringSupplier = inner::getDisplayString;
					IntegerFormatter formatter = new ExtendedIntegerFormatter("##", overrideStringSupplier);
					txt.setFormatter(formatter);
					minMaxControls[0] = txt;
				}
				{
					Label l = toolkit.createLabel(buttons, "Max");
					FormattedText txt = new FormattedText(buttons);
					final IntegerFormatter inner = new IntegerFormatter("##");
					final Supplier<String> overrideStringSupplier = inner::getDisplayString;
					IntegerFormatter formatter = new ExtendedIntegerFormatter("##", overrideStringSupplier);
					txt.setFormatter(formatter);
					minMaxControls[1] = txt;
				}
			}
		}

	}

	private PeriodDistribution createPeriodDistribution(FormattedText[] minMaxControls) {
		PeriodDistribution d = ADPFactory.eINSTANCE.createPeriodDistribution();
		if (minMaxControls[0].getValue() != null) {
			d.setMinCargoes((Integer) minMaxControls[0].getValue());
		}
		if (minMaxControls[1].getValue() != null) {
			d.setMaxCargoes((Integer) minMaxControls[1].getValue());
		}
		return d;
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	private PeriodDistributionProfileConstraint oldValue = null;
	private final @NonNull Adapter adapter = new EContentAdapter() {

		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			if (!notification.isTouch()) {
				ViewerHelper.refresh(tableViewer, false);
			}

		};
	};

	void removeAdapter() {
		final PeriodDistributionProfileConstraint oldValue2 = oldValue;
		oldValue = null;
		if (oldValue2 != null) {
			oldValue2.eAdapters().remove(adapter);

			if (oldValue2.eContainer() != null) {
				oldValue2.eContainer().eAdapters().remove(adapter);
			}

		}
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject value, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		removeAdapter();
		oldValue = (PeriodDistributionProfileConstraint) value;
		// delegate.display(dialogContext, root, value, range, dbc);
		tableViewer.setInput(value);

		value.eAdapters().add(adapter);
		if (value.eContainer() != null) {
			value.eContainer().eAdapters().add(adapter);
		}
	}

	@Override
	public void dispose() {
		removeAdapter();
		super.dispose();
	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		// delegate.setCommandHandler(commandHandler);
		this.commandHandler = commandHandler;
	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		// delegate.displayValidationStatus(status);
		statusProvider.fireStatusChanged(status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editors.IDisplayComposite#setEditorWrapper(com.mmxlabs.models.ui.editors.IInlineEditorWrapper)
	 */
	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		// delegate.setEditorWrapper(wrapper);
	}

	@Override
	public boolean checkVisibility(final IDialogEditingContext context) {
		return true;// l.. delegate.checkVisibility(context);
	}
}
