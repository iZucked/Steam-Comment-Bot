/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntBiFunction;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.google.common.base.Joiner;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetView.ViewState;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils.ShippingCostType;
import com.mmxlabs.models.ui.tabular.renderers.CenteringColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.WrappingColumnHeaderRenderer;

public class ChangeSetViewColumnHelper {

	/**
	 * Data key to store selected vessel column
	 */
	static final String DATA_SELECTED_COLUMN = "selected-vessel-column";

	private Font boldFont;

	private Image imageClosedCircle;

	private Image imageHalfCircle;
	private Image imageOpenCircle;

	private final ChangeSetView view;
	private final GridTreeViewer viewer;

	private GridColumnGroup vesselColumnGroup;
	private GridViewerColumn vesselColumnStub;

	private GridViewerColumn violationColumn;

	private GridViewerColumn latenessColumn;

	private ChangeSetWiringDiagram diagram;

	public ChangeSetWiringDiagram getDiagram() {
		return diagram;
	}

	private GridViewerColumn column_SetName;

	private GridViewerColumn column_Lateness;

	private GridViewerColumn column_Violations;

	/**
	 * Display textual vessel change markers - used for unit testing where graphics are not captured in data dump.
	 */
	private boolean textualVesselMarkers = false;

	public boolean isTextualVesselMarkers() {
		return textualVesselMarkers;
	}

	public void setTextualVesselMarkers(final boolean textualVesselMarkers) {
		this.textualVesselMarkers = textualVesselMarkers;
	}

	public ChangeSetViewColumnHelper(final ChangeSetView view, final GridTreeViewer viewer) {
		this.view = view;
		this.viewer = viewer;

		final ImageDescriptor openCircleDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lingo.reports", "icons/open-circle.png");
		final ImageDescriptor closedCircleDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lingo.reports", "icons/closed-circle.png");
		final ImageDescriptor halfCircleDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lingo.reports", "icons/half-circle.png");

		imageOpenCircle = openCircleDescriptor.createImage();
		imageClosedCircle = closedCircleDescriptor.createImage();
		imageHalfCircle = halfCircleDescriptor.createImage();

		final Font systemFont = Display.getDefault().getSystemFont();
		final FontData fontData = systemFont.getFontData()[0];
		boldFont = new Font(Display.getDefault(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));

	}

	public void makeColumns() {
		// Create columns
		{
			column_SetName = new GridViewerColumn(viewer, SWT.CENTER);
			column_SetName.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			column_SetName.getColumn().setText("");
			column_SetName.getColumn().setTree(true);
			column_SetName.getColumn().setWidth(60);
			column_SetName.getColumn().setResizeable(true);
			column_SetName.getColumn().setMoveable(false);
			column_SetName.setLabelProvider(createCSLabelProvider());
			column_SetName.getColumn().setCellRenderer(createCellRenderer());
		}

		final GridColumnGroup pnlComponentGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER | SWT.TOGGLE);
		pnlComponentGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());

		// pnlComponentGroup.setText("P&L Components");
		createCenteringGroupRenderer(pnlComponentGroup);
		pnlComponentGroup.setExpanded(false);
		pnlComponentGroup.addTreeListener(new TreeListener() {

			@Override
			public void treeExpanded(final TreeEvent e) {
				pnlComponentGroup.setText("P&L Components");

			}

			@Override
			public void treeCollapsed(final TreeEvent e) {
				pnlComponentGroup.setText("");

			}
		});

		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("P&L (m)");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createPNLDeltaLabelProvider());
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(true);

		}
		// {
		// final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
		// final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
		// gvc.getColumn().setText("");
		// gvc.getColumn().setHeaderTooltip("P&&L Components");
		// gvc.getColumn().setWidth(20);
		// gvc.setLabelProvider(createStubLabelProvider());
		// // gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
		// // ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
		// createWordWrapRenderer(gvc);
		// gvc.getColumn().setCellRenderer(createCellRenderer());
		// gvc.getColumn().setDetail(false);
		// gvc.getColumn().setSummary(true);
		// }
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("+ Sales");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createDeltaLabelProvider(true, true, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("- Purchase");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createDeltaLabelProvider(true, true, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("- Ship FOB");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createShippingDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("- Ship DES");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createAdditionalShippingPNLDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("- Upside");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createAdditionalUpsidePNLDeltaLabelProvider());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("+ Cargo other");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createCargoOtherPNLDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		if (LicenseFeatures.isPermitted("features:report-equity-book")) {
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("+ Equity");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createUpstreamDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("+ Tax, etc.");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createTaxDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);

			gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		// Add total days
		if (false) {
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Days");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createShippingDaysDeltaLabelProvider(ShippingCostType.HOURS));
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		if (false) {
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("- Hire");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createShippingCostDeltaLabelProvider(ShippingCostType.HIRE_COSTS));
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		if (false) {
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("- Misc");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createShippingCostDeltaLabelProvider(ShippingCostType.COOLDOWN_COSTS, ShippingCostType.HEEL_COST, ShippingCostType.HEEL_REVENUE, ShippingCostType.OTHER_COSTS));
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}

		// Space col
		createSpacerColumn();
		{
			column_Lateness = new GridViewerColumn(viewer, SWT.CENTER);
			column_Lateness.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			column_Lateness.getColumn().setText("Late");
			column_Lateness.getColumn().setHeaderTooltip("Lateness");
			column_Lateness.getColumn().setWidth(50);
			column_Lateness.setLabelProvider(createLatenessDeltaLabelProvider());
			column_Lateness.getColumn().setCellRenderer(createCellRenderer());

			this.latenessColumn = column_Lateness;
		}
		{
			column_Violations = new GridViewerColumn(viewer, SWT.CENTER);
			column_Violations.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			column_Violations.getColumn().setText("Violations");
			column_Violations.getColumn().setHeaderTooltip("Capacity Violations");
			column_Violations.getColumn().setWidth(50);
			column_Violations.setLabelProvider(createViolationsDeltaLabelProvider());
			createWordWrapRenderer(column_Violations);
			column_Violations.getColumn().setCellRenderer(createCellRenderer());

			this.violationColumn = column_Violations;

		}

		// Space col
		createSpacerColumn();

		final GridColumnGroup loadGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER);
		loadGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());
		loadGroup.setText("Purchase");
		createCenteringGroupRenderer(loadGroup);
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("ID");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createIDLabelProvider(true));
			gvc.getColumn().setCellRenderer(createCellRenderer(SlotIDRenderMode.LHS_IN_TARGET));
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Date");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createDateLabelProvider(true));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Price");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createPriceLabelProvider(true));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("tBtu");
			gvc.getColumn().setWidth(55);
			gvc.setLabelProvider(createDeltaLabelProvider(true, true, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Wiring");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createWiringLabelProvider());
			this.diagram = createWiringDiagram(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		final GridColumnGroup dischargeGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER);
		dischargeGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());
		dischargeGroup.setText("Sale");
		createCenteringGroupRenderer(dischargeGroup);

		{
			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("ID");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createIDLabelProvider(false));
			gvc.getColumn().setCellRenderer(createCellRenderer(SlotIDRenderMode.RHS_IN_TARGET));
		}
		{
			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Date");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createDateLabelProvider(false));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{

			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Price");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createPriceLabelProvider(false));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{

			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("tBtu");
			gvc.getColumn().setWidth(55);
			gvc.setLabelProvider(
					createDeltaLabelProvider(true, false, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		// Space col
		createSpacerColumn();

		vesselColumnGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER | SWT.TOGGLE);
		vesselColumnGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());

		// vesselColumnGroup.setText("Vessels");
		vesselColumnGroup.setExpanded(false);
		createCenteringGroupRenderer(vesselColumnGroup);
		vesselColumnGroup.addTreeListener(new TreeListener() {

			@Override
			public void treeExpanded(final TreeEvent e) {
				// TODO Auto-generated method stub
				vesselColumnGroup.setText("Vessels");

			}

			@Override
			public void treeCollapsed(final TreeEvent e) {
				// TODO Auto-generated method stub
				vesselColumnGroup.setText("");

			}
		});
		// Vessel columns are dynamically created - create a stub column to lock down the position in the table
		{
			final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setVisible(false);
			gvc.getColumn().setWidth(0);
			gvc.setLabelProvider(createStubLabelProvider());
			vesselColumnStub = gvc;
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}

	}

	public void updateVesselColumns(final Collection<String> sortedNames, final Map<String, String> shortNameMap) {
		for (final String name : sortedNames) {
			assert name != null;
			final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText(shortNameMap.get(name));
			gvc.getColumn().setHeaderTooltip(name);
			gvc.getColumn().setWidth(22);
			gvc.getColumn().setResizeable(false);
			gvc.setLabelProvider(createVesselLabelProvider(name));
			gvc.getColumn().setHeaderRenderer(new VesselNameColumnHeaderRenderer());
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
			gvc.getColumn().addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (viewer.getData(DATA_SELECTED_COLUMN) == gc) {
						viewer.setData(DATA_SELECTED_COLUMN, null);
					} else {
						viewer.setData(DATA_SELECTED_COLUMN, gc);
					}
				}

				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {

				}
			});
		}

		{
			final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setHeaderTooltip("Vessel assignment changed");
			gvc.getColumn().setWidth(22);
			gvc.getColumn().setResizeable(false);
			gvc.setLabelProvider(createGroupVesselLabelProvider());
			gvc.getColumn().setHeaderRenderer(new VesselNameColumnHeaderRenderer());
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(false);
			gvc.getColumn().setSummary(true);
		}

	}

	protected void createSpacerColumn() {
		final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
		gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		gvc.getColumn().setText("");
		gvc.getColumn().setResizeable(false);
		gvc.getColumn().setWidth(5);
		gvc.setLabelProvider(createStubLabelProvider());
		gvc.getColumn().setCellRenderer(createCellRenderer());
	}

	private enum SlotIDRenderMode {
		None, LHS_IN_TARGET, RHS_IN_TARGET
	};

	protected DefaultCellRenderer createCellRenderer() {
		return createCellRenderer(SlotIDRenderMode.None);
	}

	protected DefaultCellRenderer createCellRenderer(final SlotIDRenderMode renderMode) {
		return new DefaultCellRenderer() {

			@Override
			public boolean isSelected() {
				if (viewer.getData(DATA_SELECTED_COLUMN) == viewer.getGrid().getColumn(getColumn())) {
					return true;
				}

				return super.isSelected();
			}

			@Override
			public void paint(final GC gc, final Object value) {

				// TODO Auto-generated method stub
				super.paint(gc, value);

				if (value instanceof GridItem) {
					final GridItem gridItem = (GridItem) value;
					final Object data = gridItem.getData();
					if (data instanceof ChangeSetTableGroup) {
						final int currentLineWidth = gc.getLineWidth();
						gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
						final int s = gc.getLineStyle();
						gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
						// gc.setLineStyle(SWT.LINE_DOT);
						// gc.drawLine(getBounds().x, getBounds().y, getBounds().width + getBounds().x, getBounds().y);
						gc.setLineStyle(SWT.LINE_DOT);
						// gc.setLineWidth(1);
						gc.drawLine(getBounds().x, getBounds().y + getBounds().height, getBounds().width + getBounds().x, getBounds().y + getBounds().height);
						gc.setLineStyle(s);
						gc.setLineWidth(currentLineWidth);

					} else if (data instanceof ChangeSetTableRow) {

						final ViewState viewState = view.getCurrentViewState();
						if (viewState != null && getBounds().height > 1) {
							final int currentLineWidth = gc.getLineWidth();
							final ChangeSetTableRow changeSetTableRow = (ChangeSetTableRow) data;
							boolean found = false;
							boolean foundTarget = false;
							if (renderMode == SlotIDRenderMode.LHS_IN_TARGET) {
								if (changeSetTableRow.getLhsAfter() != null) {
									if (viewState.allTargetSlots.contains(changeSetTableRow.getLhsAfter().getLoadSlot())) {
										found = true;
										if (changeSetTableRow.getLhsAfter().getLoadSlot() == viewState.lastTargetSlot) {
											foundTarget = true;
										}
									}
								}
							}
							if (renderMode == SlotIDRenderMode.RHS_IN_TARGET) {
								if (changeSetTableRow.getRhsAfter() != null) {
									if (viewState.allTargetSlots.contains(changeSetTableRow.getRhsAfter().getDischargeSlot())) {
										found = true;
										if (changeSetTableRow.getRhsAfter().getDischargeSlot() == viewState.lastTargetSlot) {
											foundTarget = true;
										}
									}
								}
							}
							if (found) {
								final int s = gc.getLineStyle();
								gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
								// gc.setLineStyle(SWT.LINE_DOT);
								// gc.drawLine(getBounds().x, getBounds().y, getBounds().width + getBounds().x, getBounds().y);
								gc.setLineStyle(SWT.LINE_SOLID);
								if (foundTarget) {
									gc.setLineWidth(2);
								} else {
									gc.setLineWidth(1);
								}
								gc.drawRoundRectangle(getBounds().x, getBounds().y, getBounds().width - 1, getBounds().height, 4, 4);
								gc.setLineStyle(s);
							}
							gc.setLineWidth(currentLineWidth);

						}
					}
				}

			}
		};
	}

	@SuppressWarnings("restriction")
	private void createWordWrapRenderer(final GridViewerColumn gvc) {
		final WrappingColumnHeaderRenderer renderer = new WrappingColumnHeaderRenderer();
		renderer.setWordWrap(true);
		gvc.getColumn().setHeaderRenderer(renderer);
	}

	private void createCenteringGroupRenderer(final GridColumnGroup gcg) {
		final CenteringColumnGroupHeaderRenderer renderer = new CenteringColumnGroupHeaderRenderer();
		gcg.setHeaderRenderer(renderer);
	}

	// @PostConstruct
	// private void updateToolItems(@Optional final MPart part) {
	// updateToolItem(part, viewMode);
	// }

	private ChangeSetWiringDiagram createWiringDiagram(final GridViewerColumn gvc) {

		final ChangeSetWiringDiagram d = new ChangeSetWiringDiagram(viewer.getGrid(), gvc);

		return d;

	}

	private CellLabelProvider createStubLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				// Do nothing
			}
		};
	}

	private CellLabelProvider createIDLabelProvider(final boolean lhs) {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");

				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow d = (ChangeSetTableRow) element;
					if (lhs) {
						if (d.getLhsName() != null && !d.getLhsName().isEmpty()) {
							cell.setText((String) d.getLhsName());
							return;
						}
					} else {
						if (d.getRhsName() != null && !d.getRhsName().isEmpty()) {
							cell.setText((String) d.getRhsName());
							return;
						}
					}
				}
			}

		};
	}

	private CellLabelProvider createDateLabelProvider(final boolean isLoadSide) {
		return new CellLabelProvider() {

			private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

			@Override
			public String getToolTipText(Object element) {

				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow tableRow = (ChangeSetTableRow) element;

					StringBuilder sb = new StringBuilder();
					sb.append("Scheduled dates:\n");

					final SlotAllocation originalAllocation;
					final SlotAllocation newAllocation;
					if (isLoadSide) {
						originalAllocation = tableRow.getLhsBefore() != null ? tableRow.getLhsBefore().getLoadAllocation() : null;
						newAllocation = tableRow.getLhsAfter() != null ? tableRow.getLhsAfter().getLoadAllocation() : null;
					} else {
						// Unlike other RHS colums where we want to diff against the old and new slot linked to the cargo, we want to show the date diff for this slot.
						originalAllocation = tableRow.getRhsBefore() != null ? tableRow.getRhsBefore().getDischargeAllocation() : null;
						newAllocation = tableRow.getRhsAfter() != null ? tableRow.getRhsAfter().getDischargeAllocation() : null;
					}

					boolean hasDate = false;
					boolean newLine = false;
					if (originalAllocation != null) {
						final ZonedDateTime slotDate = originalAllocation.getSlotVisit().getStart();
						if (slotDate != null) {
							if (newLine) {
								sb.append("\n");
							}
							sb.append(String.format("Before: %s ", slotDate.format(formatter)));
							hasDate = true;
						}
					}
					if (newAllocation != null) {
						final ZonedDateTime slotDate = newAllocation.getSlotVisit().getStart();
						if (slotDate != null) {
							sb.append(String.format("After: %s ", slotDate.format(formatter)));
							newLine = true;
							hasDate = true;
						}
					}
					if (hasDate) {
						return sb.toString();
					}
				}
				return super.getToolTipText(element);
			}

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow tableRow = (ChangeSetTableRow) element;

					boolean isSpot = false;
					LocalDate windowStart = null;
					boolean isDelta = false;
					int deltaHours = 0;

					if (isLoadSide) {
						final SlotAllocation originalLoadAllocation = tableRow.getLhsBefore() != null ? tableRow.getLhsBefore().getLoadAllocation() : null;
						final SlotAllocation newLoadAllocation = tableRow.getLhsAfter() != null ? tableRow.getLhsAfter().getLoadAllocation() : null;

						isSpot = tableRow.isLhsSpot();
						if (newLoadAllocation != null) {
							final Slot slot = newLoadAllocation.getSlot();
							if (slot != null) {
								windowStart = slot.getWindowStart();
							}
						} else if (originalLoadAllocation != null) {
							final Slot slot = originalLoadAllocation.getSlot();
							if (slot != null) {
								windowStart = slot.getWindowStart();
							}
						}

						if (newLoadAllocation != null && originalLoadAllocation != null) {
							deltaHours = Hours.between(originalLoadAllocation.getSlotVisit().getStart(), newLoadAllocation.getSlotVisit().getStart());
							if (deltaHours != 0) {
								isDelta = true;
							}
						}

						if (windowStart != null) {

							final String windowDate;
							if (isSpot) {
								windowDate = String.format("%02d/%04d", windowStart.getMonthValue(), windowStart.getYear());
							} else {
								windowDate = windowStart.format(formatter);
							}

							if (isDelta) {
								cell.setText(String.format("%s (%s%.1f)", windowDate, deltaHours < 0 ? "↓" : "↑", Math.abs(deltaHours / 24.0)));
							} else {
								cell.setText(windowDate);
							}
						}

					} else {
						// Unlike other RHS colums where we want to diff against the old and new slot linked to the cargo, we want to show the date diff for this slot.
						final SlotAllocation originalDischargeAllocation = tableRow.getRhsBefore() != null ? tableRow.getRhsBefore().getDischargeAllocation() : null;
						final SlotAllocation newDischargeAllocation = tableRow.getRhsAfter() != null ? tableRow.getRhsAfter().getDischargeAllocation() : null;
						isSpot = tableRow.isRhsSpot();

						if (newDischargeAllocation != null) {
							final Slot slot = newDischargeAllocation.getSlot();
							if (slot != null) {
								windowStart = slot.getWindowStart();
							}
						} else if (originalDischargeAllocation != null) {
							final Slot slot = originalDischargeAllocation.getSlot();
							if (slot != null) {
								windowStart = slot.getWindowStart();
							}
						}

						if (newDischargeAllocation != null && originalDischargeAllocation != null) {
							deltaHours = Hours.between(originalDischargeAllocation.getSlotVisit().getStart(), newDischargeAllocation.getSlotVisit().getStart());
							if (deltaHours != 0) {
								isDelta = true;
							}
						}

						if (windowStart != null) {

							final String windowDate;
							if (isSpot) {
								windowDate = String.format("%02d/%04d", windowStart.getMonthValue(), windowStart.getYear());
							} else {
								windowDate = windowStart.format(formatter);
							}

							if (isDelta) {
								cell.setText(String.format("%s (%s%.1f)", windowDate, deltaHours < 0 ? "↓" : "↑", Math.abs(deltaHours / 24.0)));
							} else {
								cell.setText(windowDate);
							}
						}
					}
				}
			}
		};
	}

	private CellLabelProvider createDeltaLabelProvider(final boolean asInt, final boolean isLHS, final EStructuralFeature field, final EStructuralFeature attrib) {

		final EReference from = isLHS ? ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_BEFORE : ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_BEFORE;
		final EReference to = isLHS ? ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_AFTER : ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_AFTER;

		return createLambdaLabelProvider(asInt, false, change -> getNumber(from, field, attrib, change), change -> getNumber(to, field, attrib, change));
	}

	@NonNull
	private Number getNumber(final EReference from, final EStructuralFeature field, final EStructuralFeature attrib, final ChangeSetTableRow row) {
		Number n = null;
		try {
			final ChangeSetRowData change = (ChangeSetRowData) row.eGet(from);
			if (change != null) {
				n = getNumberInt(field, attrib, change);
			}
		} catch (final Exception e) {
			final int ii = 0;
		}
		if (n == null) {
			return Long.valueOf(0L);

		}
		return n;

	}

	@Nullable
	private Number getNumberInt(final EStructuralFeature field, final EStructuralFeature attrib, final ChangeSetRowData change) {
		if (change != null && change.eClass().getEAllStructuralFeatures().contains(field)) {
			final EObject eObject = (EObject) change.eGet(field);
			if (eObject != null) {
				return (Number) eObject.eGet(attrib);
			}
		}
		return null;

	}

	private CellLabelProvider createPNLDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				if (element instanceof ChangeSetTableGroup) {

					cell.setFont(boldFont);

					final ChangeSetTableGroup changeSet = (ChangeSetTableGroup) element;
					final DeltaMetrics metrics = changeSet.getDeltaMetrics();
					if (metrics != null) {
						double delta = metrics.getPnlDelta();

						delta = delta / 1000000.0;
						if (Math.abs(delta) < 0.0001) {
							delta = 0;
						}
						if (delta == 0) {
							cell.setText("0.00");
						} else {
							cell.setText(String.format("%s%,.3G", metrics.getPnlDelta() < 0 ? "↓" : "↑", Math.abs(delta)));
						}
					}
				}
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final long f = ChangeSetKPIUtil.getPNL(change, ResultType.Before);
					final long t = ChangeSetKPIUtil.getPNL(change, ResultType.After);

					double delta = t - f;
					delta = delta / 1000000.0;
					if (Math.abs(delta) < 0.0001) {
						delta = 0;
					}
					if (delta != 0) {
						cell.setText(String.format("%s %,.3G", delta < 0 ? "↓" : "↑", Math.abs(delta)));
					}

				}
			}
		};
	}

	private CellLabelProvider createTaxDeltaLabelProvider() {
		return createLambdaLabelProvider(true, true, change -> {
			return ChangeSetKPIUtil.getTax(change, ResultType.Before);
		}, change -> {
			return ChangeSetKPIUtil.getTax(change, ResultType.After);
		});
	}

	private CellLabelProvider createLatenessDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final long[] originalLateness = ChangeSetKPIUtil.getLateness(change, ResultType.Before);
					final long[] newLateness = ChangeSetKPIUtil.getLateness(change, ResultType.After);

					// No lateness
					if (originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] == 0 && newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] == 0) {
						return null;
					}

					final boolean originalInFlex = originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] > 0 && originalLateness[ChangeSetKPIUtil.FlexType.WithFlex.ordinal()] == 0;
					final boolean newInFlex = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] > 0 && newLateness[ChangeSetKPIUtil.FlexType.WithFlex.ordinal()] == 0;

					if (originalInFlex != newInFlex) {
						// Lateness shift between flex and non-flex times.
						final long delta = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] - originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
						final String flexStr;
						if (originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] == 0) {
							assert originalInFlex == false;
							flexStr = "within";
						} else if (delta > 0) {
							flexStr = "out of";
						} else {
							flexStr = "within";
						}
						// CHECK -- IF Original was zero, then we have moved into the felx time.
						return String.format("Lateness %s by %d days, %d hours %s flex time", delta > 0 ? "increased" : "decreased", Math.abs(delta) / 24, Math.abs(delta) % 24, flexStr);
					}
					if (!originalInFlex) {
						// if (originalLateWithoutFlex > 0 && newLatenessWithoutFlex > 0) {
						final long delta = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] - originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
						return String.format("Lateness %s by %d days, %d hours", delta > 0 ? "increased" : "decreased", Math.abs(delta) / 24, Math.abs(delta) % 24);
						// }
					} else {
						// if (originalLateWithoutFlex > 0 && newLatenessWithoutFlex > 0) {
						final long delta = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] - originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
						return String.format("Lateness (within flex time) %s by %d days, %d hours", delta > 0 ? "increased" : "decreased", Math.abs(delta) / 24, Math.abs(delta) % 24);
						// }
					}

				}
				return null;
			}

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setForeground(null);
				if (element instanceof ChangeSetTableGroup) {

					cell.setFont(boldFont);

					final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
					final Metrics scenarioMetrics = group.getCurrentMetrics();
					final DeltaMetrics deltaMetrics = group.getDeltaMetrics();

					if (deltaMetrics != null) {
						final int latenessDelta = (int) Math.round((double) deltaMetrics.getLatenessDelta() / 24.0);
						final int lateness = (int) Math.round((double) scenarioMetrics.getLateness() / 24.0);
						cell.setText(String.format("%s%d / %d", latenessDelta < 0 ? "↓" : latenessDelta == 0 ? "" : "↑", Math.abs(latenessDelta), lateness));

						if (lateness != 0) {
							cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
						}
					}
				}
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final long[] originalLateness = ChangeSetKPIUtil.getLateness(change, ResultType.Before);
					final long[] newLateness = ChangeSetKPIUtil.getLateness(change, ResultType.After);

					final boolean originalInFlex = originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] > 0 && originalLateness[ChangeSetKPIUtil.FlexType.WithFlex.ordinal()] == 0;
					final boolean newInFlex = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] > 0 && newLateness[ChangeSetKPIUtil.FlexType.WithFlex.ordinal()] == 0;

					String flexStr = "";
					if (originalInFlex != newInFlex) {
						// Lateness shift between flex and non-flex times.
						final long delta = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] - originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
						if (originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] == 0) {
							assert originalInFlex == false;
							flexStr = " *";
						} else if (delta > 0) {
							flexStr = "";
						} else {
							flexStr = " *";
						}
					}
					long delta = 0L;
					delta -= originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
					delta += newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
					final long originalDelta = delta;
					delta = (int) Math.round((double) delta / 24.0);
					if (delta != 0L) {
						cell.setText(String.format("%s %d%s", delta < 0 ? "↓" : "↑", Math.abs(delta), flexStr));
					} else if (originalDelta != 0L) {
						cell.setText(String.format("%s %s%s", originalDelta < 0 ? "↓" : "↑", "<1", flexStr));
					}
				}
			}
		};

	}

	private CellLabelProvider createViolationsDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public String getToolTipText(Object element) {

				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final Set<CapacityViolationType> beforeViolatios = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.Before));
					final Set<CapacityViolationType> afterViolatios = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.After));

					Set<CapacityViolationType> tmp = new HashSet<>(afterViolatios);
					tmp.removeAll(beforeViolatios);
					StringBuilder sb = new StringBuilder();
					boolean newLine = false;
					if (!tmp.isEmpty()) {
						sb.append(String.format("Caused %s violation%s", generateDisplayString(tmp), tmp.size() > 1 ? "s" : ""));
						newLine = true;
					}
					tmp.clear();
					tmp.addAll(beforeViolatios);
					tmp.removeAll(afterViolatios);
					if (!tmp.isEmpty()) {
						if (newLine) {
							sb.append("\n");
						}
						sb.append(String.format("Resolved %s violation%s", generateDisplayString(tmp), tmp.size() > 1 ? "s" : ""));
					}
					return sb.toString();

				}

				return super.getToolTipText(element);
			}

			private Map<CapacityViolationType, String> nameMap = new HashMap<>();
			{
				nameMap.put(CapacityViolationType.FORCED_COOLDOWN, "forced cooldown");
				nameMap.put(CapacityViolationType.LOST_HEEL, "lost heel");
				nameMap.put(CapacityViolationType.MAX_DISCHARGE, "max discharge");
				nameMap.put(CapacityViolationType.MAX_HEEL, "max heel");
				nameMap.put(CapacityViolationType.MAX_LOAD, "max load");
				nameMap.put(CapacityViolationType.MIN_DISCHARGE, "min discharge");
				nameMap.put(CapacityViolationType.MIN_HEEL, "min heel");
				nameMap.put(CapacityViolationType.MIN_LOAD, "min load");
				nameMap.put(CapacityViolationType.VESSEL_CAPACITY, "vessel capacity");
			}

			private String generateDisplayString(Set<CapacityViolationType> tmp) {
				List<String> sorted = tmp.stream() //
						.map(cvt -> nameMap.get(cvt)) //
						.sorted() // .
						.collect(Collectors.toList());

				return Joiner.on(", ").join(sorted);
			}

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setForeground(null);
				if (element instanceof ChangeSetTableGroup) {

					cell.setFont(boldFont);

					final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
					final Metrics scenarioMetrics = group.getCurrentMetrics();
					final DeltaMetrics deltaMetrics = group.getDeltaMetrics();
					if (deltaMetrics != null) {
						cell.setText(String.format("%s%d / %d", deltaMetrics.getCapacityDelta() < 0 ? "↓" : deltaMetrics.getCapacityDelta() == 0 ? "" : "↑", Math.abs(deltaMetrics.getCapacityDelta()),
								scenarioMetrics.getCapacity()));

						if (scenarioMetrics.getCapacity() != 0) {
							cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
						}
					}
				}
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final long f = ChangeSetKPIUtil.getViolations(change, ResultType.Before);
					final long t = ChangeSetKPIUtil.getViolations(change, ResultType.After);

					final long delta = t - f;
					if (delta != 0) {
						cell.setText(String.format("%s %d", delta < 0 ? "↓" : "↑", Math.abs(delta)));
					}

				}
			}
		};
	}

	private CellLabelProvider createCargoOtherPNLDeltaLabelProvider() {
		return createLambdaLabelProvider(true, false, change -> ChangeSetKPIUtil.getCargoOtherPNL(change, ResultType.Before), change -> ChangeSetKPIUtil.getCargoOtherPNL(change, ResultType.After));
	}

	private CellLabelProvider createUpstreamDeltaLabelProvider() {

		return createLambdaLabelProvider(true, true, change -> ChangeSetKPIUtil.getUpstreamPNL(change, ResultType.Before), change -> ChangeSetKPIUtil.getUpstreamPNL(change, ResultType.After));
	}

	private CellLabelProvider createAdditionalShippingPNLDeltaLabelProvider() {

		return createLambdaLabelProvider(true, true, change -> ChangeSetKPIUtil.getAdditionalShippingPNL(change, ResultType.Before),
				change -> ChangeSetKPIUtil.getAdditionalShippingPNL(change, ResultType.After));
	}

	private CellLabelProvider createAdditionalUpsidePNLDeltaLabelProvider() {

		return createLambdaLabelProvider(true, true, change -> ChangeSetKPIUtil.getAdditionalUpsidePNL(change, ResultType.Before),
				change -> ChangeSetKPIUtil.getAdditionalUpsidePNL(change, ResultType.After));
	}

	private CellLabelProvider createShippingDeltaLabelProvider() {
		return createLambdaLabelProvider(true, false, change -> ChangeSetKPIUtil.getShipping(change, ResultType.Before), change -> ChangeSetKPIUtil.getShipping(change, ResultType.After));
	}

	private CellLabelProvider createShippingDaysDeltaLabelProvider(ShippingCostType shippingCostType) {
		return createLambdaDaysLabelProvider(change -> ChangeSetKPIUtil.getShipping(change, ResultType.Before, shippingCostType),
				change -> ChangeSetKPIUtil.getShipping(change, ResultType.After, shippingCostType));
	}

	private CellLabelProvider createShippingCostDeltaLabelProvider(ShippingCostType shippingCostType) {
		return createLambdaLabelProvider(true, false, change -> ChangeSetKPIUtil.getShipping(change, ResultType.Before, shippingCostType),
				change -> ChangeSetKPIUtil.getShipping(change, ResultType.After, shippingCostType));
	}

	private CellLabelProvider createShippingCostDeltaLabelProvider(ShippingCostType... shippingCostTypes) {
		return createLambdaLabelProvider(true, false, change -> {
			long sum = 0L;
			for (ShippingCostType shippingCostType : shippingCostTypes) {
				sum += ChangeSetKPIUtil.getShipping(change, ResultType.Before, shippingCostType);
			}
			return (Number) Long.valueOf(sum);
		}, change -> {
			long sum = 0L;
			for (ShippingCostType shippingCostType : shippingCostTypes) {
				sum += ChangeSetKPIUtil.getShipping(change, ResultType.After, shippingCostType);
			}
			return (Number) Long.valueOf(sum);
		});
	}

	private CellLabelProvider createCSLabelProvider() {

		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");

				if (element instanceof ChangeSetTableGroup) {

					cell.setFont(boldFont);

					final ChangeSetTableGroup changeSet = (ChangeSetTableGroup) element;

					if (changeSet.getDescription() != null && !changeSet.getDescription().isEmpty()) {
						cell.setText(changeSet.getDescription());
					} else {
						final ChangeSetTableRoot root = (ChangeSetTableRoot) changeSet.eContainer();
						int idx = 0;
						if (root != null) {
							idx = root.getGroups().indexOf(changeSet);
						}

						cell.setText(String.format("Set %d", idx + 1));
					}
				}
			}
		};

	}

	private CellLabelProvider createVesselLabelProvider(@NonNull final String name) {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				cell.setImage(null);
				final Object element = cell.getElement();
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) element;

					if (name.equals(changeSetRow.getAfterVesselName())) {
						cell.setImage(imageClosedCircle);
						if (textualVesselMarkers) {
							cell.setText("●");
						}
					} else if (name.equals(changeSetRow.getBeforeVesselName())) {
						cell.setImage(imageOpenCircle);
						if (textualVesselMarkers) {
							cell.setText("○");
						}
					}
				}
			}

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) element;

					if (name.equals(changeSetRow.getAfterVesselName())) {
						return name;
					}
					if (name.equals(changeSetRow.getBeforeVesselName())) {
						return name;
					}
				}
				return null;
			}
		};
	}

	private CellLabelProvider createWiringLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				cell.setImage(null);
				final Object element = cell.getElement();
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) element;

					if (textualVesselMarkers) {
						if (changeSetRow.isWiringChange()) {
							String left = "";
							String right = "";
							if (changeSetRow.isLhsSlot()) {
								left = changeSetRow.isLhsNonShipped() ? "○" : "●";
							}
							if (changeSetRow.isRhsSlot()) {
								right = !changeSetRow.isRhsNonShipped() ? "○" : "●";
							}

							if (left.isEmpty() && right.isEmpty()) {
								final int ii = 0;
							} else

								cell.setText(String.format("%s      %s", left, right));
						}
					}
				}
			}
		};
	}

	private CellLabelProvider createGroupVesselLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				cell.setImage(null);
				final Object element = cell.getElement();
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) element;
					if (!Objects.equals(changeSetRow.getAfterVesselName(), changeSetRow.getBeforeVesselName())) {
						cell.setImage(imageHalfCircle);
						if (textualVesselMarkers) {
							cell.setText("○");
						}
						// } else {
						// if (isSet(changeSetRow.getNewVesselName())) {
						// cell.setImage(imageClosedCircle);
						// if (textualVesselMarkers) {
						// cell.setText("●");
						// }
						// }
					}
				}
			}

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) element;

					if (isSet(changeSetRow.getBeforeVesselName()) && isSet(changeSetRow.getAfterVesselName())) {
						if (!Objects.equals(changeSetRow.getBeforeVesselName(), changeSetRow.getAfterVesselName())) {
							return String.format("Changed from %s to %s", changeSetRow.getBeforeVesselName(), changeSetRow.getAfterVesselName());
						} else {
							return String.format("On %s", changeSetRow.getAfterVesselName());
						}
					} else if (isSet(changeSetRow.getBeforeVesselName())) {
						return String.format("Unassigned from %s", changeSetRow.getBeforeVesselName());
					} else if (isSet(changeSetRow.getAfterVesselName())) {
						return String.format("Assigned to %s", changeSetRow.getAfterVesselName());
					}
				}
				return null;
			}
		};
	}

	private CellLabelProvider createLambdaLabelProvider(final boolean asInt, final boolean asSigFigs, final Function<ChangeSetTableRow, Number> calcF,
			final Function<ChangeSetTableRow, Number> calcT) {

		final ToDoubleBiFunction<Number, Number> deltaDoubleUpdater = (f, t) -> {
			double delta = 0.0;
			if (f != null) {
				delta -= f.doubleValue();
			}
			if (t != null) {
				delta += t.doubleValue();
			}
			return delta;
		};
		final ToIntBiFunction<Number, Number> deltaIntegerUpdater = (f, t) -> {
			int delta = 0;
			if (f != null) {
				delta -= f.intValue();
			}
			if (t != null) {
				delta += t.intValue();
			}
			return delta;
		};

		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setFont(null);
				double delta = 0;
				if (element instanceof ChangeSetTableGroup) {
					cell.setFont(boldFont);
					final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
					final List<ChangeSetTableRow> rows = group.getRows();
					if (rows != null) {
						for (final ChangeSetTableRow change : rows) {
							if (asInt) {
								delta += deltaIntegerUpdater.applyAsInt(calcF.apply(change), calcT.apply(change));
							} else {
								delta += deltaDoubleUpdater.applyAsDouble(calcF.apply(change), calcT.apply(change));
							}
						}
					}
				} else if (element instanceof ChangeSetTableRow) {

					final ChangeSetTableRow change = (ChangeSetTableRow) element;
					if (asInt) {
						delta += deltaIntegerUpdater.applyAsInt(calcF.apply(change), calcT.apply(change));
					} else {
						delta += deltaDoubleUpdater.applyAsDouble(calcF.apply(change), calcT.apply(change));
					}
				}

				if (asInt) {
					delta = delta / 1000000.0;
					if (Math.abs(delta) > 0.001) {
						if (asSigFigs) {
							cell.setText(String.format("%s %,.3G", delta < 0 ? "↓" : "↑", Math.abs(delta)));
						} else {
							cell.setText(String.format("%s %,.3f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
						}
					}
				} else {
					if (Math.abs(delta) > 0.009) {
						if (asSigFigs) {
							cell.setText(String.format("%s %,.2G", delta < 0 ? "↓" : "↑", Math.abs(delta)));
						} else {
							cell.setText(String.format("%s %,.2f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
						}
					}
				}
			}
		};
	}

	private CellLabelProvider createLambdaDaysLabelProvider(final Function<ChangeSetTableRow, Number> calcF, final Function<ChangeSetTableRow, Number> calcT) {

		final ToIntBiFunction<Number, Number> deltaIntegerUpdater = (f, t) -> {
			int delta = 0;
			if (f != null) {
				delta -= f.intValue();
			}
			if (t != null) {
				delta += t.intValue();
			}
			return delta;
		};

		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setFont(null);
				long delta = 0;
				if (element instanceof ChangeSetTableGroup) {
					cell.setFont(boldFont);
					final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
					final List<ChangeSetTableRow> rows = group.getRows();
					if (rows != null) {
						for (final ChangeSetTableRow change : rows) {
							delta += deltaIntegerUpdater.applyAsInt(calcF.apply(change), calcT.apply(change));
						}
					}
				} else if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;
					delta += deltaIntegerUpdater.applyAsInt(calcF.apply(change), calcT.apply(change));
				}

				final long originalDelta = delta;
				delta = (int) Math.round((double) delta / 24.0);
				if (delta != 0L) {
					cell.setText(String.format("%s %d", delta < 0 ? "↓" : "↑", Math.abs(delta)));
				} else if (originalDelta != 0L) {
					cell.setText(String.format("%s %s", originalDelta < 0 ? "↓" : "↑", "<1"));
				}
			}
		};
	}

	boolean isSet(@Nullable final String str) {
		return str != null && !str.isEmpty();
	}

	private CellLabelProvider createPriceLabelProvider(final boolean isLoad) {

		final Function<ChangeSetTableRow, Number> calcF;
		final Function<ChangeSetTableRow, Number> calcT;
		if (isLoad) {
			calcF = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_BEFORE, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
			calcT = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_AFTER, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
		} else {
			calcF = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_BEFORE, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
			calcT = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_AFTER, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
		}
		final ToDoubleBiFunction<Number, Number> deltaDoubleUpdater = (f, t) -> {
			double delta = 0.0;
			if (f != null) {
				delta -= f.doubleValue();
			}
			if (t != null) {
				delta += t.doubleValue();
			}
			return delta;
		};

		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setFont(null);
				double delta = 0;
				if (element instanceof ChangeSetTableGroup) {
					// cell.setFont(boldFont);
					final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
					final List<ChangeSetTableRow> rows = group.getRows();
					if (rows != null) {
						for (final ChangeSetTableRow change : rows) {
							delta += deltaDoubleUpdater.applyAsDouble(calcF.apply(change), calcT.apply(change));
						}
					}
				} else if (element instanceof ChangeSetTableRow) {

					final ChangeSetTableRow change = (ChangeSetTableRow) element;
					delta += deltaDoubleUpdater.applyAsDouble(calcF.apply(change), calcT.apply(change));

					if (isLoad) {
						final SlotAllocation allocation = change.getLhsAfter() != null ? change.getLhsAfter().getLoadAllocation() : null;
						if (allocation != null) {
							final Slot slot = allocation.getSlot();
							if (slot != null) {
								final String expr = slot.getPriceExpression();
								if (expr != null && expr.contains("?")) {
									cell.setText(String.format("=%,.2f", allocation.getPrice()));
									return;
								}
							}
						}
					} else {
						final SlotAllocation allocation = change.getRhsAfter() != null ? change.getRhsAfter().getDischargeAllocation() : null;
						if (allocation != null) {
							final Slot slot = allocation.getSlot();
							if (slot != null) {
								final String expr = slot.getPriceExpression();
								if (expr != null && expr.contains("?")) {
									cell.setText(String.format("=%,.2f", allocation.getPrice()));
									return;
								}
							}
						}
					}
				}
				if (Math.abs(delta) > 0.009) {
					cell.setText(String.format("%s %,.2f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
				}
			}

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetTableRow) {

					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					if (isLoad) {
						final SlotAllocation allocation = change.getLhsAfter() != null ? change.getLhsAfter().getLoadAllocation() : null;
						if (allocation != null) {
							final Slot slot = allocation.getSlot();
							if (slot != null) {
								final String expr = slot.getPriceExpression();
								if (expr != null && expr.contains("?")) {
									return String.format("Break-even price is %,.2f", allocation.getPrice());
								}
							}
						}
					} else {
						final SlotAllocation allocation = change.getRhsAfter() != null ? change.getRhsAfter().getDischargeAllocation() : null;
						if (allocation != null) {
							final Slot slot = allocation.getSlot();
							if (slot != null) {
								final String expr = slot.getPriceExpression();
								if (expr != null && expr.contains("?")) {
									return String.format("Break-even price is %,.2f", allocation.getPrice());
								}
							}
						}
					}
				}

				return null;
			}
		};
	}

	public void packMainColumns() {

		column_SetName.getColumn().pack();
		column_Lateness.getColumn().pack();
		column_Violations.getColumn().pack();
	}

	public void dispose() {

		if (vesselColumnGroup != null && !vesselColumnGroup.isDisposed()) {
			vesselColumnGroup.dispose();
		}

		if (boldFont != null) {
			boldFont.dispose();
			boldFont = null;
		}
		if (imageHalfCircle != null) {
			imageHalfCircle.dispose();
			imageHalfCircle = null;
		}
		if (imageOpenCircle != null) {
			imageOpenCircle.dispose();
			imageOpenCircle = null;
		}
		if (imageClosedCircle != null) {
			imageClosedCircle.dispose();
			imageClosedCircle = null;
		}

	}

	public void cleanUpVesselColumns() {
		if (vesselColumnGroup != null && !vesselColumnGroup.isDisposed()) {
			final GridColumn[] columns = vesselColumnGroup.getColumns();
			for (final GridColumn c : columns) {
				// Quick hack - do not dispose the hidden col
				if (vesselColumnStub.getColumn() == c) {
					continue;
				}
				if (!c.isDisposed()) {
					c.dispose();
				}
			}
		}
	}

	public GridColumn getLatenessColumn() {
		if (latenessColumn != null) {
			return latenessColumn.getColumn();
		}
		return null;
	}

	public GridColumn getViolationsColumn() {
		if (violationColumn != null) {
			return violationColumn.getColumn();
		}
		return null;
	}
}
