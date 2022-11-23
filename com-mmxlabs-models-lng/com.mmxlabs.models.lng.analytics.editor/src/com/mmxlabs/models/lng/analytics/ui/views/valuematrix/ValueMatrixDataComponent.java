package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.MultipleValidationStatusSandboxLabelProvider;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.renderers.CenteringColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.ui.valueproviders.RefetchingReferenceValueProviderProvider;
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.rcp.common.ViewerHelper;

public class ValueMatrixDataComponent extends AbstractValueMatrixValidatedComponent {

	private final ValueMatrixTransformer transformer = new ValueMatrixTransformer();
	private EObjectTableViewer parametersViewer;

	private final Map<Widget, CellEditor> controlToEditor = new HashMap<>();

	private class ControlListener implements Listener {
		@Override
		public void handleEvent(final Event event) {
			switch (event.type) {
			case SWT.Activate:
//				cellEditorActivated(event.widget, controlToEditor.get(event.widget));
				break;
			case SWT.Deactivate:
//				cellEditorDeactivated(event.widget, controlToEditor.get(event.widget));
			default:
				break;
			}
		}
	}

	private final ControlListener controlListener = new ControlListener();

	public ValueMatrixDataComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, Collection<IStatus>> validationErrors,
			@NonNull final Supplier<SwapValueMatrixModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	private GridViewerColumn addSwapValueMatrixColumn(final String columnName, final GridColumnGroup group, final ICellRenderer renderer, final ICellManipulator manipulator,
			final CellFormatterLabelProvider labelProvider, final ETypedElement... pathObjects) {
		return addSwapValueMatrixColumn(columnName, group, renderer, manipulator, labelProvider, new EMFPath(true, pathObjects));
	}

	private GridViewerColumn addSwapValueMatrixColumn(final String columnName, final GridColumnGroup group, final ICellRenderer renderer, final ICellManipulator manipulator,
			final CellFormatterLabelProvider labelProvider, final EMFPath path) {
		final GridColumn tColumn;
		if (group != null) {
			tColumn = new GridColumn(group, SWT.CENTER);
		} else {
			tColumn = new GridColumn(parametersViewer.getGrid(), SWT.CENTER);
		}
		final GridViewerColumn column = new GridViewerColumn(parametersViewer, tColumn);
		tColumn.setHeaderRenderer(new ColumnHeaderRenderer());

		{
			final Pair<EMFPath, ICellRenderer> pathAndRenderer = new Pair<>(path, renderer);
			tColumn.setData(EObjectTableViewer.COLUMN_RENDERER_AND_PATH, pathAndRenderer);
		}
		tColumn.setText(columnName);
		tColumn.pack();
		tColumn.setData(EObjectTableViewer.COLUMN_RENDERER, renderer);
		tColumn.setData(EObjectTableViewer.COLUMN_PATH, path);
		tColumn.setData(EObjectTableViewer.COLUMN_MANIPULATOR, manipulator);

		column.setLabelProvider(labelProvider);
		column.setEditingSupport(new EditingSupport(parametersViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				if (parametersViewer.isLocked()) {
					return;
				}
				Object obj = path.get((EObject) element);
				manipulator.setParent(element, obj);
				manipulator.setValue(obj, value);
				parametersViewer.refresh();
			}

			@Override
			protected Object getValue(Object element) {
				Object value = manipulator.getValue(path.get((EObject) element));
				if (value == SetCommand.UNSET_VALUE) {
					return null;
				}
				return value;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				final CellEditor cellEditor = manipulator.getCellEditor(parametersViewer.getGrid(), path.get((EObject) element));
				if (cellEditor != null) {
					final Control control = cellEditor.getControl();
					if (control != null) {
						control.removeListener(SWT.Activate, controlListener);
						control.removeListener(SWT.Deactivate, controlListener);
						controlToEditor.put(control, cellEditor);
						control.addListener(SWT.Activate, controlListener);
						control.addListener(SWT.Deactivate, controlListener);
					}
				}
				return cellEditor;
			}

			@Override
			protected boolean canEdit(Object element) {
				return !parametersViewer.isLocked() && manipulator != null && manipulator.canEdit(path.get((EObject) element));
			}
		});
		return column;
	}

	private Control createParametersComposite(final Composite parent) {
		final EObjectTableViewer viewer = new EObjectTableViewer(parent, SWT.NONE);

		final GridData gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
		gridData.heightHint = 150;
		final Grid table = viewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		parametersViewer = viewer;
		viewer.getControl().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (parametersViewer != null && parametersViewer.getContentProvider() != null) {
					for (final Widget control : controlToEditor.keySet()) {
						control.removeListener(SWT.Activate, controlListener);
						control.removeListener(SWT.Deactivate, controlListener);
					}
					controlToEditor.clear();
				}
			}
		});
		inputWants.add(model -> {
			if (parametersViewer.getContentProvider() == null) {
				parametersViewer.init(transformer.createContentProvider(), valueMatrixModellerView.getModelReference());
				final GridColumnGroup cargoColumnGroup = new GridColumnGroup(parametersViewer.getGrid(), SWT.CENTER);
				{
					cargoColumnGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());
					cargoColumnGroup.setText("Cargo");
					final CenteringColumnGroupHeaderRenderer renderer = new CenteringColumnGroupHeaderRenderer();
					cargoColumnGroup.setHeaderRenderer(renderer);
				}

				final IReferenceValueProviderProvider referenceValueProviderProvider = new RefetchingReferenceValueProviderProvider(scenarioEditingLocation::getReferenceValueProviderCache);
				{
					final MultipleValidationStatusSandboxLabelProvider vesselLabelProvider = new MultipleValidationStatusSandboxLabelProvider(sandboxUIHelper,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseVesselCharter(), new ValueMatrixVesselCharterDescriptionFormatter(), validationErrors, "Vessel",
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseVesselCharter());
					final SingleReferenceManipulator vesselManipulator = new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getExistingVesselCharterOption_VesselCharter(),
							referenceValueProviderProvider, scenarioEditingLocation.getDefaultCommandHandler());
					addSwapValueMatrixColumn("Vessel", cargoColumnGroup, vesselManipulator, vesselManipulator, vesselLabelProvider,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseVesselCharter());
				}
				{
					final MultipleValidationStatusSandboxLabelProvider loadLabelProvider = new MultipleValidationStatusSandboxLabelProvider(sandboxUIHelper,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseLoad(), new BuyOptionDescriptionFormatter(), validationErrors, "Load",
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseLoad());
					final SingleReferenceManipulator loadManipulator = new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getBuyReference_Slot(), referenceValueProviderProvider,
							scenarioEditingLocation.getDefaultCommandHandler());
					addSwapValueMatrixColumn("Load", cargoColumnGroup, loadManipulator, loadManipulator, loadLabelProvider, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseLoad());
				}
				{
					final MultipleValidationStatusSandboxLabelProvider dischargeLabelProvider = new MultipleValidationStatusSandboxLabelProvider(sandboxUIHelper,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischarge(), new SellOptionDescriptionFormatter(), validationErrors, "Discharge",
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischarge());
					final SingleReferenceManipulator dischargeManipulator = new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getSellReference_Slot(), referenceValueProviderProvider,
							scenarioEditingLocation.getDefaultCommandHandler());
					addSwapValueMatrixColumn("Discharge", cargoColumnGroup, dischargeManipulator, dischargeManipulator, dischargeLabelProvider,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischarge());
				}
				{
					final MultipleValidationStatusSandboxLabelProvider dischargeMinLabelProvider = new MultipleValidationStatusSandboxLabelProvider(sandboxUIHelper,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeMinPrice(), new SellOptionDescriptionFormatter(), validationErrors, "Min",
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeMinPrice());
					final NumericAttributeManipulator dischargeMinManipulator = new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeMinPrice(),
							scenarioEditingLocation.getDefaultCommandHandler());
					addSwapValueMatrixColumn("Min", cargoColumnGroup, dischargeMinManipulator, dischargeMinManipulator, dischargeMinLabelProvider);
				}
				{
					final NumericAttributeManipulator dischargeMaxManipulator = new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeMaxPrice(),
							scenarioEditingLocation.getDefaultCommandHandler());
					final MultipleValidationStatusSandboxLabelProvider dischargeMaxLabelProvider = new MultipleValidationStatusSandboxLabelProvider(sandboxUIHelper,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeMaxPrice(), new SellOptionDescriptionFormatter(), validationErrors, "Max",
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeMaxPrice());
					addSwapValueMatrixColumn("Max", cargoColumnGroup, dischargeMaxManipulator, dischargeMaxManipulator, dischargeMaxLabelProvider);
				}
				final GridColumnGroup marketsColumnGroup = new GridColumnGroup(parametersViewer.getGrid(), SWT.CENTER);
				{
					marketsColumnGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());
					marketsColumnGroup.setText("Markets");
					final CenteringColumnGroupHeaderRenderer renderer = new CenteringColumnGroupHeaderRenderer();
					marketsColumnGroup.setHeaderRenderer(renderer);
				}
				{
					final MultipleValidationStatusSandboxLabelProvider desBuyLabelProvider = new MultipleValidationStatusSandboxLabelProvider(sandboxUIHelper,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapLoadMarket(), new ValueMatrixBuyMarketDescriptionFormatter(), validationErrors, "DES Buy",
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapLoadMarket());
					final SingleReferenceManipulator desBuyManipulator = new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getBuyMarket_Market(), referenceValueProviderProvider,
							scenarioEditingLocation.getDefaultCommandHandler());
					addSwapValueMatrixColumn("DES Buy", marketsColumnGroup, desBuyManipulator, desBuyManipulator, desBuyLabelProvider,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapLoadMarket());
				}
				{
					final MultipleValidationStatusSandboxLabelProvider desSellLabelProvider = new MultipleValidationStatusSandboxLabelProvider(sandboxUIHelper,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapDischargeMarket(), new ValueMatrixSellMarketDescriptionFormatter(), validationErrors, "DES Sell",
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapDischargeMarket());
					final SingleReferenceManipulator desSellManipulator = new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getSellMarket_Market(), referenceValueProviderProvider,
							scenarioEditingLocation.getDefaultCommandHandler());
					addSwapValueMatrixColumn("DES Sell", marketsColumnGroup, desSellManipulator, desSellManipulator, desSellLabelProvider,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapDischargeMarket());
				}
				{
					final NumericAttributeManipulator marketMinManipulator = new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMinPrice(),
							scenarioEditingLocation.getDefaultCommandHandler());
					final MultipleValidationStatusSandboxLabelProvider marketMinLabelProvider = new MultipleValidationStatusSandboxLabelProvider(sandboxUIHelper,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMinPrice(), new SellOptionDescriptionFormatter(), validationErrors, "Min",
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMinPrice());
					addSwapValueMatrixColumn("Min", marketsColumnGroup, marketMinManipulator, marketMinManipulator, marketMinLabelProvider);
				}
				{
					final NumericAttributeManipulator marketMaxManipulator = new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMaxPrice(),
							scenarioEditingLocation.getDefaultCommandHandler());
					final MultipleValidationStatusSandboxLabelProvider marketMaxLabelProvider = new MultipleValidationStatusSandboxLabelProvider(sandboxUIHelper,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMaxPrice(), new SellOptionDescriptionFormatter(), validationErrors, "Max",
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMaxPrice());
					addSwapValueMatrixColumn("Max", marketsColumnGroup, marketMaxManipulator, marketMaxManipulator, marketMaxLabelProvider);
				}
				{
					final NumericAttributeManipulator swapFeeManipulator = new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapFee(),
							scenarioEditingLocation.getDefaultCommandHandler());
					final MultipleValidationStatusSandboxLabelProvider swapFeeLabelProvider = new MultipleValidationStatusSandboxLabelProvider(sandboxUIHelper,
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapFee(), new SellOptionDescriptionFormatter(), validationErrors, "Swap fee",
							AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapFee());
					addSwapValueMatrixColumn("Swap fee", marketsColumnGroup, swapFeeManipulator, swapFeeManipulator, swapFeeLabelProvider);
				}
			}
			parametersViewer.setInput(model);
			for (final GridColumn col : parametersViewer.getGrid().getColumns()) {
				col.pack();
			}
		});
		return viewer.getControl();
	}

	@Override
	public void createControls(final Composite parent, final boolean expanded, final IExpansionListener expansionListener, final ValueMatrixModellerView valueMatrixModellerView) {
		this.valueMatrixModellerView = valueMatrixModellerView;
		final ExpandableComposite expandableComposite = wrapInExpandable(parent, "Parameters", this::createParametersComposite, null, false);
		expandableComposite.setLayoutData(GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, 80).grab(true, false).create());
		expandableComposite.setExpanded(expanded);
		expandableComposite.addExpansionListener(expansionListener);
	}

	@Override
	public void dispose() {
	}

	public void refresh() {
		if (parametersViewer.getContentProvider() == null) {
			parametersViewer.init(transformer.createContentProvider(), valueMatrixModellerView.getModelReference());
		}
		parametersViewer.refresh();
	}

	public void setEditingLock(final boolean locked) {
		ViewerHelper.runIfViewerValid(parametersViewer, false, () -> parametersViewer.setLocked(locked));
	}

	public void pack() {
		ViewerHelper.runIfViewerValid(parametersViewer, false, () -> {
			for (final GridColumn col : parametersViewer.getGrid().getColumns()) {
				col.pack();
			}
		});
	}
}
