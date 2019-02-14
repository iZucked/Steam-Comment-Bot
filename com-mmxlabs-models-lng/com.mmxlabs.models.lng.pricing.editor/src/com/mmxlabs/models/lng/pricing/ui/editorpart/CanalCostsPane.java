/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.StringFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * Quick hack for vessel route cost editing
 * 
 * @author hinton
 * 
 */
public class CanalCostsPane extends ScenarioTableViewerPane {

	private FormattedText panama_MarkupEditor;
	private FormattedText suez_TugCostEditor;
	private FormattedText suez_FixedCostsEditor;
	private FormattedText suez_DiscountFactorEditor;
	private FormattedText suez_sdrToUSDEditor;

	private SuezCanalTariff suezCanalTariff;
	private PanamaCanalTariff panamaCanalTariff;

	public CanalCostsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		final EditingDomain ed = getEditingDomain();

		final NonEditableColumn routeManipulator = new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof RouteCost) {
					final RouteOption routeOption = ((RouteCost) object).getRouteOption();
					if (routeOption != null) {
						return PortModelLabeller.getName(routeOption);
					}
				}
				return "Unknown";
			}

		};

		addTypicalColumn("Route", routeManipulator);
		addTypicalColumn("Vessels",
				new MultipleReferenceManipulator(PricingPackage.Literals.ROUTE_COST__VESSELS, getReferenceValueProviderCache(), ed, MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		addTypicalColumn("Laden Toll", new NumericAttributeManipulator(PricingPackage.Literals.ROUTE_COST__LADEN_COST, ed));
		addTypicalColumn("Ballast Toll", new NumericAttributeManipulator(PricingPackage.Literals.ROUTE_COST__BALLAST_COST, ed));

		defaultSetTitle("Canal Costs");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_CanalCosts");
	}

	private final @NonNull AdapterImpl changeListener = new EContentAdapter() {
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification msg) {
			//
			super.notifyChanged(msg);
			if (msg.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}
			if (msg.getFeature() == PricingPackage.Literals.PANAMA_CANAL_TARIFF__BANDS) {
				panamaBandsTableViewer.setInput(msg.getNewValue());
				return;
			}
			if (msg.getNotifier() instanceof PanamaCanalTariffBand) {
				panamaBandsTableViewer.setInput(panamaCanalTariff);
				panamaBandsTableViewer.refresh();
				return;
			}
			if (msg.getFeature() == PricingPackage.Literals.PANAMA_CANAL_TARIFF__MARKUP_RATE) {
				panama_MarkupEditor.setValue(msg.getNewDoubleValue() * 100.0);
				return;
			}
			if (msg.getFeature() == PricingPackage.Literals.SUEZ_CANAL_TARIFF__BANDS) {
				suezBandsTableViewer.setInput(msg.getNewValue());
				suezBandsTableViewer.refresh();

				return;
			}
			if (msg.getNotifier() instanceof SuezCanalTariffBand) {
				suezBandsTableViewer.setInput(suezCanalTariff);
				suezBandsTableViewer.refresh();
				return;
			}
			if (msg.getFeature() == PricingPackage.Literals.SUEZ_CANAL_TARIFF__TUG_BANDS) {
				suezTugsTableViewer.setInput(msg.getNewValue());
				return;
			}
			if (msg.getNotifier() instanceof SuezCanalTugBand) {
				suezTugsTableViewer.setInput(suezCanalTariff);
				suezTugsTableViewer.refresh();
				return;
			}
			if (msg.getFeature() == PricingPackage.Literals.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR) {
				suez_DiscountFactorEditor.setValue(msg.getNewValue());
				return;
			}
			if (msg.getFeature() == PricingPackage.Literals.SUEZ_CANAL_TARIFF__FIXED_COSTS) {
				suez_FixedCostsEditor.setValue(msg.getNewValue());
				return;
			}
			if (msg.getFeature() == PricingPackage.Literals.SUEZ_CANAL_TARIFF__SDR_TO_USD) {
				suez_sdrToUSDEditor.setValue(msg.getNewValue());
				return;
			}
			if (msg.getFeature() == PricingPackage.Literals.SUEZ_CANAL_TARIFF__TUG_COST) {
				suez_TugCostEditor.setValue(msg.getNewValue());
				return;
			}
		};
	};
	private TableViewer panamaBandsTableViewer;
	private TableViewer suezBandsTableViewer;
	private TableViewer suezTugsTableViewer;

	@Override
	public ScenarioTableViewer createViewer(final Composite parent) {

		createSuezTariffControls(parent);
		createPanamaTariffControls(parent);

		return super.createViewer(parent);
	}

	private void createPanamaTariffControls(final Composite parent) {

		final ExpandableComposite parametersExpandable = new ExpandableComposite(parent, ExpandableComposite.TWISTIE);
		parametersExpandable.setExpanded(false);
		parametersExpandable.setText("Panama tariff");

		final Composite parametersParent = new Composite(parametersExpandable, SWT.NONE);

		parametersExpandable.setClient(parametersParent);
		parametersExpandable.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(final ExpansionEvent e) {
				parametersParent.layout(true);
				parent.layout(true);
			}
		});
		parametersParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).spacing(0, 0).margins(0, 0).create());
		{
		}
		// Markup
		{

			final Label lbl = new Label(parametersParent, SWT.NONE);
			lbl.setText("Markup (%) ");
			lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).minSize(1000, -1).create());

			final Composite strictParent = new Composite(parametersParent, SWT.NONE);
			strictParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());

			panama_MarkupEditor = new FormattedText(strictParent);
			panama_MarkupEditor.setFormatter(new IntegerFormatter());
			panama_MarkupEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(30, SWT.DEFAULT).create());

			panama_MarkupEditor.getControl().addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(final ModifyEvent e) {
					final Object newValue = panama_MarkupEditor.getValue();
					if (panamaCanalTariff != null && newValue instanceof Double) {
						final double v = ((Double) newValue) / 100.0;
						if (v != panamaCanalTariff.getMarkupRate()) {
							final Command cmd = SetCommand.create(getEditingDomain(), panamaCanalTariff, PricingPackage.eINSTANCE.getPanamaCanalTariff_MarkupRate(), v);
							getEditingDomain().getCommandStack().execute(cmd);
						}
					}
				}

			});
			panama_MarkupEditor.getControl().setToolTipText("Markup pecentage for misc costs on top of basic fee");
		}

		{
			final Label lbl = new Label(parametersParent, SWT.NONE);
			lbl.setText("Pricing bands per m³ of capacity");
			lbl.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

			final TableViewer tableViewer = new TableViewer(parametersParent, SWT.FULL_SELECTION);
			final Table table = tableViewer.getTable();

			table.setLayoutData(new GridData(GridData.FILL_BOTH));
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			table.setLayout(new TableLayout());
			table.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

			tableViewer.setContentProvider(new IStructuredContentProvider() {
				@Override
				public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

				}

				@Override
				public void dispose() {

				}

				@Override
				public Object[] getElements(final Object inputElement) {
					final Object[] things = ((PanamaCanalTariff) inputElement).getBands().toArray();

					return things;
				}
			});

			final TableViewerColumn labelColumn = createLabelColumn(tableViewer);
			final TableViewerColumn ladenColumn = createPriceColumn("Laden", PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF, tableViewer);
			final TableViewerColumn ballastColumn = createPriceColumn("Ballast", PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF, tableViewer);
			final TableViewerColumn ballastRoundtripColumn = createPriceColumn("Ballast Round-trip", PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF, tableViewer);

			table.addListener(SWT.Resize, new Listener() {
				boolean resizing = false;

				@Override
				public void handleEvent(final Event event) {
					if (resizing)
						return;
					resizing = true;
					labelColumn.getColumn().pack();
					ladenColumn.getColumn().pack();
					ballastColumn.getColumn().pack();
					ballastRoundtripColumn.getColumn().pack();
					resizing = false;
				}
			});

			CanalCostsPane.this.panamaBandsTableViewer = tableViewer;
			tableViewer.setComparator(new ViewerComparator() {
				@Override
				public int compare(final Viewer viewer, final Object e1, final Object e2) {

					final PanamaCanalTariffBand b1 = (PanamaCanalTariffBand) e1;
					final PanamaCanalTariffBand b2 = (PanamaCanalTariffBand) e2;

					final int v1 = b1 == null ? -1 : (b1.isSetBandEnd() ? b1.getBandEnd() : Integer.MAX_VALUE);
					final int v2 = b2 == null ? 2 : (b2.isSetBandEnd() ? b2.getBandEnd() : Integer.MAX_VALUE);

					return Integer.compare(v1, v2);
				}
			});
		}

	}

	private void createSuezTariffControls(final Composite parent) {

		final ExpandableComposite parametersExpandable = new ExpandableComposite(parent, ExpandableComposite.TWISTIE);
		parametersExpandable.setExpanded(false);
		parametersExpandable.setText("Suez tariff");

		final Composite parametersParent = new Composite(parametersExpandable, SWT.NONE);

		parametersExpandable.setClient(parametersParent);
		parametersExpandable.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(final ExpansionEvent e) {
				parametersParent.layout(true);
				parent.layout(true);
			}
		});
		parametersParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).spacing(0, 0).margins(0, 0).create());
		parametersParent.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, false).create());
		{
		}
		// Tug cost editor
		{

			final Label lbl = new Label(parametersParent, SWT.NONE);
			lbl.setText("Tug cost ");
			lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).minSize(1000, -1).create());

			final Composite strictParent = new Composite(parametersParent, SWT.NONE);
			strictParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());

			suez_TugCostEditor = new FormattedText(strictParent);
			suez_TugCostEditor.setFormatter(new IntegerFormatter());
			suez_TugCostEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(100, SWT.DEFAULT).create());
			suez_TugCostEditor.getControl().addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(final ModifyEvent e) {
					final Object newValue = suez_TugCostEditor.getValue();
					if (suezCanalTariff != null && newValue instanceof Double && !Objects.equals(newValue, suezCanalTariff.getTugCost())) {
						final Command cmd = SetCommand.create(getEditingDomain(), suezCanalTariff, PricingPackage.eINSTANCE.getSuezCanalTariff_TugCost(), newValue);
						getEditingDomain().getCommandStack().execute(cmd);
					}
				}

			});
			suez_TugCostEditor.getControl().setToolTipText("Cost per tug. In $/tug");
		}
		{

			final Label lbl = new Label(parametersParent, SWT.NONE);
			lbl.setText("Fixed costs ");
			lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).minSize(1000, -1).create());

			final Composite strictParent = new Composite(parametersParent, SWT.NONE);
			strictParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());

			suez_FixedCostsEditor = new FormattedText(strictParent);
			suez_FixedCostsEditor.setFormatter(new IntegerFormatter());
			suez_FixedCostsEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(100, SWT.DEFAULT).create());
			suez_FixedCostsEditor.getControl().addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(final ModifyEvent e) {
					final Object newValue = suez_FixedCostsEditor.getValue();
					if (suezCanalTariff != null && newValue instanceof Double && !Objects.equals(newValue, suezCanalTariff.getFixedCosts())) {
						final Command cmd = SetCommand.create(getEditingDomain(), suezCanalTariff, PricingPackage.eINSTANCE.getSuezCanalTariff_FixedCosts(), newValue);
						getEditingDomain().getCommandStack().execute(cmd);
					}
				}

			});
			suez_FixedCostsEditor.getControl().setToolTipText("Lumpsum fixed costs. E.g. mooring fees and disbursements. In $");
		}
		{
			final Label lbl = new Label(parametersParent, SWT.NONE);
			lbl.setText("Discount factor (%) ");
			lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).minSize(1000, -1).create());

			final Composite strictParent = new Composite(parametersParent, SWT.NONE);
			strictParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());

			suez_DiscountFactorEditor = new FormattedText(strictParent);
			suez_DiscountFactorEditor.setFormatter(new IntegerFormatter());
			suez_DiscountFactorEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(30, SWT.DEFAULT).create());
			suez_DiscountFactorEditor.getControl().addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(final ModifyEvent e) {

					final Object newValue = suez_DiscountFactorEditor.getValue();
					if (suezCanalTariff != null && newValue instanceof Double) {
						final double v = ((Double) newValue) / 100.0;
						if (v != suezCanalTariff.getDiscountFactor()) {
							final Command cmd = SetCommand.create(getEditingDomain(), suezCanalTariff, PricingPackage.eINSTANCE.getSuezCanalTariff_DiscountFactor(), v);
							getEditingDomain().getCommandStack().execute(cmd);
						}
					}
				}

			});
			suez_DiscountFactorEditor.getControl().setToolTipText("Discount factor. Applies to the SCNT band cost only.");
		}
		{

			final Label lbl = new Label(parametersParent, SWT.NONE);
			lbl.setText("SDP to USD ");
			lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).minSize(1000, -1).create());

			final Composite strictParent = new Composite(parametersParent, SWT.NONE);
			strictParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());

			suez_sdrToUSDEditor = new FormattedText(strictParent);
			suez_sdrToUSDEditor.setFormatter(new StringFormatter());
			suez_sdrToUSDEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(100, SWT.DEFAULT).create());
			suez_sdrToUSDEditor.getControl().addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(final ModifyEvent e) {
					final Object newValue = suez_DiscountFactorEditor.getValue();
					if (suezCanalTariff != null && newValue instanceof String && !Objects.equals(newValue, suezCanalTariff.getSdrToUSD())) {
						final Command cmd = SetCommand.create(getEditingDomain(), suezCanalTariff, PricingPackage.eINSTANCE.getSuezCanalTariff_SdrToUSD(), newValue);
						getEditingDomain().getCommandStack().execute(cmd);
					}
				}

			});
			suez_sdrToUSDEditor.getControl().setToolTipText("Expression to convert SDL to USD.");
		}
		{

			final Composite tableParent = new Composite(parametersParent, SWT.NONE);
			tableParent.setLayout(GridLayoutFactory.fillDefaults().create());

			final Label lbl = new Label(tableParent, SWT.NONE);
			lbl.setText("Pricing bands based on SCNT");
			tableParent.setLayoutData(GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).create());
			lbl.setLayoutData(GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).create());

			final TableViewer tableViewer = new TableViewer(tableParent, SWT.FULL_SELECTION);
			final Table table = tableViewer.getTable();

			table.setLayoutData(new GridData(GridData.FILL_BOTH));
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			table.setLayout(new TableLayout());
			table.setLayoutData(GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).create());

			tableViewer.setContentProvider(new IStructuredContentProvider() {
				@Override
				public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

				}

				@Override
				public void dispose() {

				}

				@Override
				public Object[] getElements(final Object inputElement) {
					final Object[] things = ((SuezCanalTariff) inputElement).getBands().toArray();

					return things;
				}
			});

			final TableViewerColumn labelColumn = createLabelColumn(tableViewer);
			final TableViewerColumn ladenColumn = createPriceColumn("Laden", PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__LADEN_TARIFF, tableViewer);
			final TableViewerColumn ballastColumn = createPriceColumn("Ballast", PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__BALLAST_TARIFF, tableViewer);

			table.addListener(SWT.Resize, new Listener() {
				boolean resizing = false;

				@Override
				public void handleEvent(final Event event) {
					if (resizing)
						return;
					resizing = true;
					labelColumn.getColumn().pack();
					ladenColumn.getColumn().pack();
					ballastColumn.getColumn().pack();
					resizing = false;
				}
			});

			CanalCostsPane.this.suezBandsTableViewer = tableViewer;
			tableViewer.setComparator(new ViewerComparator() {
				@Override
				public int compare(final Viewer viewer, final Object e1, final Object e2) {

					final SuezCanalTariffBand b1 = (SuezCanalTariffBand) e1;
					final SuezCanalTariffBand b2 = (SuezCanalTariffBand) e2;

					final int v1 = b1 == null ? -1 : (b1.isSetBandEnd() ? b1.getBandEnd() : Integer.MAX_VALUE);
					final int v2 = b2 == null ? 2 : (b2.isSetBandEnd() ? b2.getBandEnd() : Integer.MAX_VALUE);

					return Integer.compare(v1, v2);
				}
			});
		}
		{
			final Composite tableParent = new Composite(parametersParent, SWT.NONE);
			tableParent.setLayout(GridLayoutFactory.fillDefaults().create());
			tableParent.setLayoutData(GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).create());

			final Label lbl = new Label(tableParent, SWT.NONE);
			lbl.setText("Tugs for vessel capacity");
			lbl.setLayoutData(GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).create());
			final TableViewer tableViewer = new TableViewer(tableParent, SWT.FULL_SELECTION);
			final Table table = tableViewer.getTable();

			table.setLayoutData(new GridData(GridData.FILL_BOTH));
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			table.setLayout(new TableLayout());
			table.setLayoutData(GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).create());

			tableViewer.setContentProvider(new IStructuredContentProvider() {
				@Override
				public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

				}

				@Override
				public void dispose() {

				}

				@Override
				public Object[] getElements(final Object inputElement) {
					final Object[] things = ((SuezCanalTariff) inputElement).getTugBands().toArray();

					return things;
				}
			});

			final TableViewerColumn labelColumn = createLabelColumn(tableViewer);
			final TableViewerColumn ladenColumn = createTugColumn("Tugs", PricingPackage.Literals.SUEZ_CANAL_TUG_BAND__TUGS, tableViewer);

			table.addListener(SWT.Resize, new Listener() {
				boolean resizing = false;

				@Override
				public void handleEvent(final Event event) {
					if (resizing)
						return;
					resizing = true;
					labelColumn.getColumn().pack();
					ladenColumn.getColumn().pack();
					resizing = false;
				}
			});

			CanalCostsPane.this.suezTugsTableViewer = tableViewer;
			tableViewer.setComparator(new ViewerComparator() {
				@Override
				public int compare(final Viewer viewer, final Object e1, final Object e2) {

					final SuezCanalTugBand b1 = (SuezCanalTugBand) e1;
					final SuezCanalTugBand b2 = (SuezCanalTugBand) e2;

					final int v1 = b1 == null ? -1 : (b1.isSetBandEnd() ? b1.getBandEnd() : Integer.MAX_VALUE);
					final int v2 = b2 == null ? 2 : (b2.isSetBandEnd() ? b2.getBandEnd() : Integer.MAX_VALUE);

					return Integer.compare(v1, v2);
				}
			});
		}

	}

	public void setInput(final CostModel costModel, final SuezCanalTariff suezCanalTariff, final PanamaCanalTariff panamaCanalTariff) {

		if (this.suezCanalTariff != null) {
			this.suezCanalTariff.eAdapters().remove(changeListener);
		}
		if (this.panamaCanalTariff != null) {
			this.panamaCanalTariff.eAdapters().remove(changeListener);
		}

		this.suezCanalTariff = suezCanalTariff;
		this.panamaCanalTariff = panamaCanalTariff;

		getViewer().setInput(costModel);

		if (panamaCanalTariff != null) {
			panama_MarkupEditor.setValue(panamaCanalTariff.getMarkupRate() * 100.0);
			panamaBandsTableViewer.setInput(panamaCanalTariff);
		} else {
			panama_MarkupEditor.setValue(0);
		}
		if (suezCanalTariff != null) {
			suezBandsTableViewer.setInput(suezCanalTariff);
			suezTugsTableViewer.setInput(suezCanalTariff);

			suez_DiscountFactorEditor.setValue(suezCanalTariff.getDiscountFactor() * 100.0);
			suez_FixedCostsEditor.setValue(suezCanalTariff.getFixedCosts());
			suez_sdrToUSDEditor.setValue(suezCanalTariff.getSdrToUSD());
			suez_TugCostEditor.setValue(suezCanalTariff.getTugCost());
		} else {
			suez_DiscountFactorEditor.setValue(0.0);
			suez_FixedCostsEditor.setValue(0.0);
			suez_sdrToUSDEditor.setValue(0.0);
			suez_TugCostEditor.setValue("");

		}

		if (this.suezCanalTariff != null) {
			this.suezCanalTariff.eAdapters().add(changeListener);
		}
		if (this.panamaCanalTariff != null) {
			this.panamaCanalTariff.eAdapters().add(changeListener);
		}

	}

	@Override
	public void dispose() {

		if (this.suezCanalTariff != null) {
			this.suezCanalTariff.eAdapters().remove(changeListener);
		}
		this.suezCanalTariff = null;
		if (this.panamaCanalTariff != null) {
			this.panamaCanalTariff.eAdapters().remove(changeListener);
		}
		this.panamaCanalTariff = null;

		super.dispose();
	}

	private TableViewerColumn createPriceColumn(final String name, final EAttribute attr, final TableViewer tableViewer) {

		final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);

		column.getColumn().setText(name);

		column.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(final Object element, final Object value) {
				getEditingDomain().getCommandStack().execute(SetCommand.create(getEditingDomain(), element, attr, value));
			}

			@Override
			protected Object getValue(final Object element) {
				return ((EObject) element).eGet(attr);
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(tableViewer.getTable());
				ed.setFormatter(new DoubleFormatter("#0.##"));
				return ed;
			}

			@Override
			protected boolean canEdit(final Object element) {
				return true;
			}
		});

		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return element == null ? "" : String.format("%.3f", ((EObject) element).eGet(attr));
			}
		});

		return column;
	}

	private TableViewerColumn createTugColumn(final String name, final EAttribute attr, final TableViewer tableViewer) {

		final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);

		column.getColumn().setText(name);

		column.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(final Object element, final Object value) {
				final EditingDomain ed = getEditingDomain();
				ed.getCommandStack().execute(SetCommand.create(ed, element, attr, value));
			}

			@Override
			protected Object getValue(final Object element) {
				return ((EObject) element).eGet(attr);
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(tableViewer.getTable());
				ed.setFormatter(new IntegerFormatter("0"));
				return ed;
			}

			@Override
			protected boolean canEdit(final Object element) {
				return true;
			}
		});

		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element instanceof EObject) {
					final EObject eObject = (EObject) element;
					final Object value = eObject.eGet(attr);
					if (value instanceof Number) {
						final Number number = (Number) value;
						return element == null ? "" : String.format("%d", number.intValue());
					}
				}
				return "";
			}
		});

		return column;

	}

	private TableViewerColumn createLabelColumn(final TableViewer tableViewer) {

		final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);

		column.getColumn().setText("");

		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				String label = "";
				if (element instanceof PanamaCanalTariffBand) {
					final PanamaCanalTariffBand band = (PanamaCanalTariffBand) element;
					if (!band.isSetBandStart()) {
						label = String.format("First %,d", band.getBandEnd());
					} else if (!band.isSetBandEnd()) {
						label = String.format("Over %,d", band.getBandStart());
					} else {
						final int diff = band.getBandEnd() - band.getBandStart();
						label = String.format("Next %,d", diff);
					}
				}
				if (element instanceof SuezCanalTariffBand) {
					final SuezCanalTariffBand band = (SuezCanalTariffBand) element;
					if (!band.isSetBandStart()) {
						label = String.format("First %,d", band.getBandEnd());
					} else if (!band.isSetBandEnd()) {
						label = String.format("Over %,d", band.getBandStart());
					} else {
						final int diff = band.getBandEnd() - band.getBandStart();
						label = String.format("Next %,d", diff);
					}
				}
				if (element instanceof SuezCanalTugBand) {
					final SuezCanalTugBand band = (SuezCanalTugBand) element;
					if (!band.isSetBandStart()) {
						label = String.format("Up to %,d", band.getBandEnd());
					} else if (!band.isSetBandEnd()) {
						label = String.format("Over %,d", band.getBandStart());
					} else {
						final int diff = band.getBandEnd() - band.getBandStart();
						label = String.format("Next %,d", diff);
					}
				}
				return label;
			}
		});

		return column;

	}
}
