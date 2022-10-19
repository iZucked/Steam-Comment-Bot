package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.renderers.CenteringColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.ui.valueproviders.RefetchingReferenceValueProviderProvider;

public class ValueMatrixDataComponent extends AbstractValueMatrixComponent {

	private final ValueMatrixTransformer transformer = new ValueMatrixTransformer();
	private EObjectTableViewer parametersViewer;

	public ValueMatrixDataComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull final Supplier<SwapValueMatrixModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	private Control createParametersComposite(final Composite parent) {
		final EObjectTableViewer viewer = new EObjectTableViewer(parent, SWT.NONE);

//		ColumnViewerToolTipSupport.enableFor(viewer);
//
//		GridViewerHelper.configureLookAndFeel(viewer);
//		viewer.addTypicalColumn("DES Purchase Market", new SingleReferenceManipulator(null, null, null));

//		viewer.init(transformer.createContentProvider(), valueMatrixModellerView.getModelReference());
		final GridData gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
		gridData.heightHint = 150;
		final Grid table = viewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		parametersViewer = viewer;
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
				final IReferenceValueProviderProvider referenceValueProviderProvider = new RefetchingReferenceValueProviderProvider(() -> scenarioEditingLocation.getReferenceValueProviderCache());
				parametersViewer.addTypicalColumn("Vessel", cargoColumnGroup, new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getExistingVesselCharterOption_VesselCharter(),
						referenceValueProviderProvider, scenarioEditingLocation.getDefaultCommandHandler()), AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseVesselCharter());
				parametersViewer.addTypicalColumn("Load", cargoColumnGroup,
						new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getBuyReference_Slot(), referenceValueProviderProvider, scenarioEditingLocation.getDefaultCommandHandler()),
						AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseLoad());
				parametersViewer.addTypicalColumn("Discharge", cargoColumnGroup,
						new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getSellReference_Slot(), referenceValueProviderProvider, scenarioEditingLocation.getDefaultCommandHandler()),
						AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischarge());
				parametersViewer.addTypicalColumn("Min", cargoColumnGroup,
						new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeMinPrice(), scenarioEditingLocation.getDefaultCommandHandler()));
				parametersViewer.addTypicalColumn("Max", cargoColumnGroup,
						new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_BaseDischargeMaxPrice(), scenarioEditingLocation.getDefaultCommandHandler()));
				final GridColumnGroup marketsColumnGroup = new GridColumnGroup(parametersViewer.getGrid(), SWT.CENTER);
				{
					marketsColumnGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());
					marketsColumnGroup.setText("Markets");
					final CenteringColumnGroupHeaderRenderer renderer = new CenteringColumnGroupHeaderRenderer();
					marketsColumnGroup.setHeaderRenderer(renderer);
				}
				parametersViewer
						.addTypicalColumn(
								"DES Buy", marketsColumnGroup, new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getBuyMarket_Market(),
										scenarioEditingLocation.getReferenceValueProviderCache(), scenarioEditingLocation.getDefaultCommandHandler()),
								AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapLoadMarket());
				parametersViewer
						.addTypicalColumn(
								"DES Sell", marketsColumnGroup, new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getSellMarket_Market(),
										scenarioEditingLocation.getReferenceValueProviderCache(), scenarioEditingLocation.getDefaultCommandHandler()),
								AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapDischargeMarket());
				parametersViewer.addTypicalColumn("Min", marketsColumnGroup,
						new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMinPrice(), scenarioEditingLocation.getDefaultCommandHandler()));
				parametersViewer.addTypicalColumn("Max", marketsColumnGroup,
						new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_MarketMaxPrice(), scenarioEditingLocation.getDefaultCommandHandler()));
				parametersViewer.addTypicalColumn("Swap fee", marketsColumnGroup,
						new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapFee(), scenarioEditingLocation.getDefaultCommandHandler()));

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
}
