/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.common.util.ToLongTriFunction;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetView.ViewState;
import com.mmxlabs.lingo.reports.views.changeset.extensions.ChangeSetColumnValueExtenderExtensionUtil;
import com.mmxlabs.lingo.reports.views.changeset.extensions.IChangeSetColumnValueExtender;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.presentation.composites.TimeWindowHolder;
import com.mmxlabs.models.lng.nominations.utils.NominationTypeRegistry;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils.ShippingCostType;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.tabular.renderers.CenteringColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.WrappingColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ChangeSetViewColumnHelper {

	private final Iterable<IChangeSetColumnValueExtender> columnExtenders;

	/**
	 * Data key to store selected vessel column
	 */

	static final String DATA_SELECTED_COLUMN = "selected-vessel-column";

	private Font boldFont;

	private Image imageClosedCircle;

	private Image imageHalfCircle;
	private Image imageOpenCircle;
	private Image imageSteadyArrow;
	private Image imageGreenArrowDown;
	private Image imageGreenArrowUp;
	private Image imageRedArrowDown;
	private Image imageRedArrowUp;
	private Image imageDarkArrowDown;
	private Image imageDarkArrowUp;

	private Color colour_VesselTypeColumn;

	private final ChangeSetView view;
	private final GridTreeViewer viewer;

	private GridColumnGroup vesselColumnGroup;
	private GridViewerColumn vesselColumnStub;

	private GridViewerColumn violationColumn;

	private GridViewerColumn latenessColumn;
	
	private GridViewerColumn nominationBreaksColumn;
	
	private ChangeSetWiringDiagram diagram;

	public ChangeSetWiringDiagram getDiagram() {
		return diagram;
	}

	private GridViewerColumn column_SetName;

	private GridViewerColumn column_Lateness;

	private GridViewerColumn column_Violations;
	
	/**
	 * Display textual vessel change markers - used for unit testing where graphics
	 * are not captured in data dump.
	 */
	private boolean textualVesselMarkers = false;

	private @NonNull BiFunction<ChangeSetTableGroup, Integer, String> changeSetColumnLabelProvider = getDefaultLabelProvider();

	private GridViewerColumn column_LoadPrice;

	private GridViewerColumn column_LoadVolume;

	private GridViewerColumn column_DischargePrice;

	private GridViewerColumn column_DischargeVolume;

	private boolean showCompareColumns = true;

	public BiFunction<ChangeSetTableGroup, Integer, String> getChangeSetColumnLabelProvider() {
		return changeSetColumnLabelProvider;
	}

	public void setChangeSetColumnLabelProvider(@NonNull final BiFunction<ChangeSetTableGroup, Integer, String> changeSetColumnLabelProvider) {
		this.changeSetColumnLabelProvider = changeSetColumnLabelProvider;
	}

	public boolean isTextualVesselMarkers() {
		return textualVesselMarkers;
	}

	public void setTextualVesselMarkers(final boolean textualVesselMarkers) {
		this.textualVesselMarkers = textualVesselMarkers;
	}

	private ImageDescriptor getImageDescriptor(final String path) {
		final ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lingo.reports", path);
		return imageDescriptor;
	}

	public ChangeSetViewColumnHelper(final ChangeSetView view, final GridTreeViewer viewer) {
		this.view = view;
		this.viewer = viewer;

		final ImageDescriptor imageDescriptorSteadyArrow;
		final ImageDescriptor imageDescriptorGreenArrowDown;
		final ImageDescriptor imageDescriptorGreenArrowUp;
		final ImageDescriptor imageDescriptorRedArrowDown;
		final ImageDescriptor imageDescriptorRedArrowUp;
		final ImageDescriptor imageDescriptorDarkArrowDown;
		final ImageDescriptor imageDescriptorDarkArrowUp;

		final ImageDescriptor openCircleDescriptor = getImageDescriptor("icons/open-circle.png");
		final ImageDescriptor closedCircleDescriptor = getImageDescriptor("icons/closed-circle.png");
		final ImageDescriptor halfCircleDescriptor = getImageDescriptor("icons/half-circle.png");

		imageDescriptorSteadyArrow = getImageDescriptor("icons/steady_arrow.png");
		imageDescriptorGreenArrowDown = getImageDescriptor("icons/green_arrow_down.png");
		imageDescriptorGreenArrowUp = getImageDescriptor("icons/green_arrow_up.png");
		imageDescriptorRedArrowDown = getImageDescriptor("icons/red_arrow_down.png");
		imageDescriptorRedArrowUp = getImageDescriptor("icons/red_arrow_up.png");
		imageDescriptorDarkArrowDown = getImageDescriptor("icons/dark_arrow_down.png");
		imageDescriptorDarkArrowUp = getImageDescriptor("icons/dark_arrow_up.png");

		imageOpenCircle = openCircleDescriptor.createImage();
		imageClosedCircle = closedCircleDescriptor.createImage();
		imageHalfCircle = halfCircleDescriptor.createImage();

		imageSteadyArrow = imageDescriptorSteadyArrow.createImage();
		imageGreenArrowDown = imageDescriptorGreenArrowDown.createImage();
		imageGreenArrowUp = imageDescriptorGreenArrowUp.createImage();
		imageRedArrowDown = imageDescriptorRedArrowDown.createImage();
		imageRedArrowUp = imageDescriptorRedArrowUp.createImage();
		imageDarkArrowDown = imageDescriptorDarkArrowDown.createImage();
		imageDarkArrowUp = imageDescriptorDarkArrowUp.createImage();

		final Font systemFont = Display.getDefault().getSystemFont();
		final FontData fontData = systemFont.getFontData()[0];
		boldFont = new Font(Display.getDefault(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));

		colour_VesselTypeColumn = new Color(Display.getDefault(), new RGB(240, 240, 240));

		columnExtenders = ChangeSetColumnValueExtenderExtensionUtil.getColumnExtendeders();
	}

	public void makeColumns(final InsertionPlanGrouperAndFilter insertionPlanFilter) {
		// Create columns
		{
			column_SetName = new GridViewerColumn(viewer, SWT.CENTER);
			column_SetName.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			column_SetName.getColumn().setText("");
			column_SetName.getColumn().setTree(true);
			column_SetName.getColumn().setWidth(60);
			column_SetName.getColumn().setResizeable(true);
			column_SetName.getColumn().setMoveable(false);
			column_SetName.setLabelProvider(createCSLabelProvider(insertionPlanFilter));
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
			gvc.setLabelProvider(createPNLDeltaLabelProvider(false));
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(true);

		}
		// if (showAlternativePNLBase)
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			altPNLBaseColumn = new GridViewerColumn(viewer, gc);
			altPNLBaseColumn.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			altPNLBaseColumn.getColumn().setText("P&L Alt (m)");
			altPNLBaseColumn.getColumn().setWidth(75);
			altPNLBaseColumn.setLabelProvider(createPNLDeltaLabelProvider(true));
			altPNLBaseColumn.getColumn().setCellRenderer(createCellRenderer());
			altPNLBaseColumn.getColumn().setDetail(true);
			altPNLBaseColumn.getColumn().setSummary(true);
			altPNLBaseColumn.getColumn().setVisible(showAlternativePNLBase);

		}
		// {
		// final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
		// final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
		// gvc.getColumn().setText("");
		// gvc.getColumn().setHeaderTooltip("P&&L Components");
		// gvc.getColumn().setWidth(20);
		// gvc.setLabelProvider(createStubLabelProvider());
		// // gvc.setLabelProvider(createDeltaLabelProvider(true,
		// ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
		// // ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION,
		// SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
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
			gvc.setLabelProvider(
					createDeltaLabelProvider(true, false, true, true, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
			createWordWrapRenderer(gvc, 75);
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
			gvc.setLabelProvider(
					createDeltaLabelProvider(true, true, true, true, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
			createWordWrapRenderer(gvc, 70);
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
			createWordWrapRenderer(gvc, 70);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		if (LicenseFeatures.isPermitted("features:report-ship-des")) {
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("- Ship DES");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createAdditionalShippingPNLDeltaLabelProvider());
			createWordWrapRenderer(gvc, 70);
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
			createWordWrapRenderer(gvc, 70);
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("+ Cargo other");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createCargoOtherPNLDeltaLabelProvider());
			createWordWrapRenderer(gvc, 70);
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
			createWordWrapRenderer(gvc, 70);
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
			createWordWrapRenderer(gvc, 70);
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
			createWordWrapRenderer(gvc, 70);
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
			createWordWrapRenderer(gvc, 70);
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
			createWordWrapRenderer(gvc, 70);
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

			this.latenessColumn.getColumn().setVisible(showCompareColumns);
		}
		{
			column_Violations = new GridViewerColumn(viewer, SWT.CENTER);
			column_Violations.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			column_Violations.getColumn().setText("Issues");
			column_Violations.getColumn().setHeaderTooltip("Capacity Violations");
			column_Violations.getColumn().setWidth(50);
			column_Violations.setLabelProvider(createViolationsDeltaLabelProvider());
			column_Violations.getColumn().setCellRenderer(createCellRenderer(true));

			this.violationColumn = column_Violations;

			this.violationColumn.getColumn().setVisible(showCompareColumns);
		}

		createNominationBreaksColumn();

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
			gvc.getColumn().setCellRenderer(createCellRenderer(SlotIDRenderMode.LHS_IN_TARGET, false));
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
			this.column_LoadPrice = new GridViewerColumn(viewer, gc);
			column_LoadPrice.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			column_LoadPrice.getColumn().setText("Price");
			column_LoadPrice.getColumn().setWidth(50);
			column_LoadPrice.setLabelProvider(createPriceLabelProvider(true));
			column_LoadPrice.getColumn().setCellRenderer(createCellRenderer());

			this.column_LoadPrice.getColumn().setVisible(showCompareColumns);

		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			this.column_LoadVolume = new GridViewerColumn(viewer, gc);
			this.column_LoadVolume.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			this.column_LoadVolume.getColumn().setText("tBtu");
			this.column_LoadVolume.getColumn().setWidth(55);
			this.column_LoadVolume.setLabelProvider(
					createDeltaLabelProvider(true, false, false, true, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
			this.column_LoadVolume.getColumn().setCellRenderer(createCellRenderer());

			this.column_LoadVolume.getColumn().setVisible(showCompareColumns);
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
			gvc.getColumn().setCellRenderer(createCellRenderer(SlotIDRenderMode.RHS_IN_TARGET, false));
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
			this.column_DischargePrice = new GridViewerColumn(viewer, gc);
			this.column_DischargePrice.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			this.column_DischargePrice.getColumn().setText("Price");
			this.column_DischargePrice.getColumn().setWidth(50);
			this.column_DischargePrice.setLabelProvider(createPriceLabelProvider(false));
			this.column_DischargePrice.getColumn().setCellRenderer(createCellRenderer());
			this.column_DischargePrice.getColumn().setVisible(showCompareColumns);
		}
		{

			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			this.column_DischargeVolume = new GridViewerColumn(viewer, gc);
			this.column_DischargeVolume.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			this.column_DischargeVolume.getColumn().setText("tBtu");
			this.column_DischargeVolume.getColumn().setWidth(55);
			this.column_DischargeVolume.setLabelProvider(createDeltaLabelProvider(true, false, false, false, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
			this.column_DischargeVolume.getColumn().setCellRenderer(createCellRenderer());
			this.column_DischargeVolume.getColumn().setVisible(showCompareColumns);
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
		// Vessel columns are dynamically created - create a stub column to lock down
		// the position in the table
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

	private void createNominationBreaksColumn() {
		nominationBreaksColumn = new GridViewerColumn(viewer, SWT.CENTER);
		nominationBreaksColumn.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		nominationBreaksColumn.getColumn().setText("Noms");
		nominationBreaksColumn.getColumn().setHeaderTooltip("Number of nominations potentially affected.");
		nominationBreaksColumn.getColumn().setWidth(50);
		nominationBreaksColumn.setLabelProvider(createNominationBreaksLabelProvider());
		nominationBreaksColumn.getColumn().setCellRenderer(createCellRenderer());
		nominationBreaksColumn.getColumn().setVisible(showCompareColumns);
	}

	public static class VesselData {

		public VesselData(final String name, final String shortName, final ChangeSetVesselType vesselType, final int charterNubmer) {
			this.charterNumber = charterNubmer;
			this.name = name;
			this.shortName = shortName;
			this.type = vesselType;
		}

		public final String name;
		public final String shortName;
		public final ChangeSetVesselType type;
		public final Integer charterNumber;

		@Override
		public int hashCode() {
			return Objects.hash(name, shortName, type);
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) {
				return true;

			}
			if (obj instanceof VesselData) {
				final VesselData other = (VesselData) obj;

				return type == other.type //
						&& Objects.equals(shortName, other.shortName) //
						&& Objects.equals(name, other.name) //
				;
			}
			return false;
		}
	}

	private static ChangeSetVesselType[] displayedVesselType = { ChangeSetVesselType.FLEET, ChangeSetVesselType.MARKET, ChangeSetVesselType.NOMINAL };

	private boolean showAlternativePNLBase;

	private GridViewerColumn altPNLBaseColumn;

	public void updateVesselColumns(final Collection<VesselData> data) {

		cleanUpVesselColumns();

		final Map<ChangeSetVesselType, List<VesselData>> colData = data.stream() //
				.filter(a -> a.name != null && !a.name.isEmpty()) // Ignore empty names
				.sorted((a, b) -> {
					int c = a.name.compareTo(b.name);
					if (c==0) 
						c= a.charterNumber.compareTo(b.charterNumber);
					return c;
				}) // Sort alphabetically
				.collect(Collectors.groupingBy(d -> d.type)); // Group by type

		boolean showSpacer = true;
		for (final ChangeSetVesselType type : displayedVesselType) {
			if (colData.containsKey(type)) {
				final List<VesselData> vesselDataList = colData.get(type);
				if (vesselDataList != null && !vesselDataList.isEmpty()) {
					if (showSpacer) {
						final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.NONE);
						final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
						gvc.getColumn().setHeaderRenderer(new VesselNameColumnHeaderRenderer());
						gvc.getColumn().setText("");
						if (type == ChangeSetVesselType.FLEET) {
							gvc.getColumn().setText("Fleet");
						} else if (type == ChangeSetVesselType.MARKET) {
							gvc.getColumn().setText("Mkt.");
						} else if (type == ChangeSetVesselType.NOMINAL) {
							gvc.getColumn().setText("Nom.");
						}

						gvc.getColumn().setResizeable(false);
						gvc.getColumn().setWidth(22);
						gvc.setLabelProvider(createStubLabelProvider());
						gvc.getColumn().setCellRenderer(createGrayCellRenderer());
						gvc.getColumn().setHeaderFont(boldFont);
						gvc.getColumn().setDetail(true);
						gvc.getColumn().setSummary(false);
						gvc.getColumn().setSummary(false);
					}

					for (final VesselData d : vesselDataList) {
						assert d.name != null;
						final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.NONE);
						final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
						gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
						gvc.getColumn().setText(d.shortName);
						gvc.getColumn().setHeaderTooltip(d.name);
						gvc.getColumn().setWidth(22);
						gvc.getColumn().setResizeable(false);
						gvc.setLabelProvider(createVesselLabelProvider(d.name, d.charterNumber));
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
						showSpacer = true;
					}
				}
			}
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
		gvc.getColumn().setCellRenderer(createCellRenderer(false));
	}

	private enum SlotIDRenderMode {
		None, LHS_IN_TARGET, RHS_IN_TARGET
	};

	protected CustomCellRenderer createCellRenderer() {
		return createCellRenderer(false);
	}
	
	protected CustomCellRenderer createCellRenderer(boolean issueColumn) {
		return createCellRenderer(SlotIDRenderMode.None, issueColumn);
	}

	protected CustomCellRenderer createCellRenderer(final SlotIDRenderMode renderMode, boolean issueColumn) {
		return new CustomCellRenderer(issueColumn) {
			
			@Override
			public boolean isSelected() {
				if (viewer.getData(DATA_SELECTED_COLUMN) == viewer.getGrid().getColumn(getColumn())) {
					return true;
				}

				return super.isSelected();
			}

			@Override
			public void paint(final GC gc, final Object value) {
				
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
						// gc.drawLine(getBounds().x, getBounds().y, getBounds().width + getBounds().x,
						// getBounds().y);
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
									if (viewState.allTargetElements.contains(changeSetTableRow.getLhsAfter().getLoadSlot())) {
										found = true;
										if (changeSetTableRow.getLhsAfter().getLoadSlot() == viewState.lastTargetElement) {
											foundTarget = true;
										}
									} else {
										final Event lhsEvent = changeSetTableRow.getLhsAfter().getLhsEvent();
										if (lhsEvent instanceof VesselEventVisit) {
											final VesselEventVisit vesselEventVisit = (VesselEventVisit) lhsEvent;
											if (viewState.allTargetElements.contains(vesselEventVisit.getVesselEvent())) {
												found = true;
												if (vesselEventVisit.getVesselEvent() == viewState.lastTargetElement) {
													foundTarget = true;
												}
											}
										}
									}
								}
							}
							if (renderMode == SlotIDRenderMode.RHS_IN_TARGET) {
								if (changeSetTableRow.getRhsAfter() != null) {
									if (viewState.allTargetElements.contains(changeSetTableRow.getRhsAfter().getDischargeSlot())) {
										found = true;
										if (changeSetTableRow.getRhsAfter().getDischargeSlot() == viewState.lastTargetElement) {
											foundTarget = true;
										}
									}
								}
							}
							if (found) {
								final int s = gc.getLineStyle();
								gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
								// gc.setLineStyle(SWT.LINE_DOT);
								// gc.drawLine(getBounds().x, getBounds().y, getBounds().width + getBounds().x,
								// getBounds().y);
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
			
			@Override
			protected int drawImages(final GC gc, GridItem item, int x, int insideMargin, boolean issuesChange, int addedIssues, int resolvedIssues) {
				Image image =  imageGreenArrowDown;
		        if (issuesChange) {
		        	//Default case, places single arrow in the middle. 
		        	int y = getBounds().y;
		            y += (getBounds().height - image.getBounds().height)/2;
		            
		            assert(addedIssues >= 0);
		            assert(resolvedIssues >= 0);
		            
		            //If two arrows, adjust y position.
		            if (addedIssues > 0 && resolvedIssues > 0)
		            {
		            	y = getBounds().y;
		            	y += (getBounds().height - (image.getBounds().height*2)) /2;
		            	int y2 = y + image.getBounds().height;		           		            
		            
		            	gc.drawImage(imageRedArrowUp, getBounds().x + x, y);						
						gc.drawImage(imageGreenArrowDown, getBounds().x + x, y2);
		            }
		            else if (resolvedIssues > 0) {
		            	gc.drawImage(imageGreenArrowDown, getBounds().x + x, y);
		            }
		            else if (addedIssues > 0) {
		            	gc.drawImage(imageRedArrowUp, getBounds().x + x, y);									
		            }
					x += image.getBounds().width + insideMargin;
		        }
		        
		        return x;
			}
		};
	}

	@SuppressWarnings("restriction")
	protected DefaultCellRenderer createGrayCellRenderer() {
		return new DefaultCellRenderer() {

			@Override
			public void paint(final GC gc, final Object value) {
				gc.setBackground(colour_VesselTypeColumn);
				gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width, getBounds().height);

			}
		};

	}

	private void createWordWrapRenderer(final GridViewerColumn gvc, final int minWidth) {
		final WrappingColumnHeaderRenderer renderer = new WrappingColumnHeaderRenderer();
		renderer.setWordWrap(true);
		renderer.setMinWidth(minWidth);
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

			@Override
			public String getToolTipText(final Object element) {

				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow tableRow = (ChangeSetTableRow) element;

					final StringBuilder sb = new StringBuilder();

					final SlotAllocation originalAllocation;
					final SlotAllocation newAllocation;
					if (lhs) {
						originalAllocation = tableRow.getLhsBefore() != null ? tableRow.getLhsBefore().getLoadAllocation() : null;
						newAllocation = tableRow.getLhsAfter() != null ? tableRow.getLhsAfter().getLoadAllocation() : null;
					} else {
						// Unlike other RHS colums where we want to diff against the old and new slot
						// linked to the cargo, we want to show the date diff for this slot.
						originalAllocation = tableRow.getRhsBefore() != null ? tableRow.getRhsBefore().getDischargeAllocation() : null;
						newAllocation = tableRow.getRhsAfter() != null ? tableRow.getRhsAfter().getDischargeAllocation() : null;
					}

					Slot s = null;
					if (newAllocation != null) {
						s = newAllocation.getSlot();
					} else if (originalAllocation != null) {
						s = originalAllocation.getSlot();
					}
					if (s != null) {
						sb.append(s.getName());
						if (s.getContract() != null) {
							sb.append("\nContract: " + s.getContract().getName());
						}
						;
						if (s.getPort() != null) {
							sb.append("\nPort: " + s.getPort().getName());
						}
					}

					return sb.toString();
				}
				return super.getToolTipText(element);
			}

		};
	}

	private CellLabelProvider createDateLabelProvider(final boolean isLoadSide) {
		return new CellLabelProvider() {

			private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getDateStringDisplay());

			@Override
			public String getToolTipText(final Object element) {

				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow tableRow = (ChangeSetTableRow) element;

					final StringBuilder sb = new StringBuilder();
					sb.append("Scheduled dates:\n");

					final SlotAllocation originalAllocation;
					final SlotAllocation newAllocation;
					if (isLoadSide) {
						originalAllocation = tableRow.getLhsBefore() != null ? tableRow.getLhsBefore().getLoadAllocation() : null;
						newAllocation = tableRow.getLhsAfter() != null ? tableRow.getLhsAfter().getLoadAllocation() : null;
					} else {
						// Unlike other RHS colums where we want to diff against the old and new slot
						// linked to the cargo, we want to show the date diff for this slot.
						originalAllocation = tableRow.getRhsBefore() != null ? tableRow.getRhsBefore().getDischargeAllocation() : null;
						newAllocation = tableRow.getRhsAfter() != null ? tableRow.getRhsAfter().getDischargeAllocation() : null;
					}

					boolean hasDate = false;
					boolean newLine = false;
					if (originalAllocation != null) {
						final ZonedDateTime slotDate = originalAllocation.getSlotVisit().getStart();
						if (slotDate != null) {
							sb.append(String.format("Before: %s ", slotDate.format(formatter)));
							newLine = true;
							hasDate = true;
						}
					}
					if (newAllocation != null) {
						final ZonedDateTime slotDate = newAllocation.getSlotVisit().getStart();
						if (slotDate != null) {
							if (newLine) {
								sb.append("\n");
							}
							sb.append(String.format("After: %s ", slotDate.format(formatter)));
							hasDate = true;
						}
					}
					if (hasDate) {
						return sb.toString();
					} else {
						PaperDealAllocation pda = null;
						if (tableRow.getLhsBefore() != null && tableRow.getLhsBefore().getPaperDealAllocation() != null) {
							pda = tableRow.getLhsBefore().getPaperDealAllocation();
						} else if (tableRow.getLhsAfter() != null && tableRow.getLhsAfter().getPaperDealAllocation() != null) {
							pda = tableRow.getLhsAfter().getPaperDealAllocation();
						}
						if (pda != null && pda.getPaperDeal() != null) {
							final PaperDeal paperDeal = pda.getPaperDeal();
							if (paperDeal.getStartDate() != null && paperDeal.getEndDate() != null) {
								final StringBuilder sb1 = new StringBuilder();
								sb1.append("Paper deal dates:\n");
								sb1.append(String.format("First day: %s \n", paperDeal.getStartDate().format(formatter)));
								sb1.append(String.format("Final day: %s", paperDeal.getEndDate().format(formatter)));
								return sb1.toString();
							}
						}
					}
				}
				return super.getToolTipText(element);
			}

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setImage(null);
				cell.setFont(null);
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
								if (textualVesselMarkers) {
									cell.setText(String.format("%s (%s%.1f)", windowDate, deltaHours < 0 ? "↓" : "↑", Math.abs(deltaHours / 24.0)));
								} else {
									cell.setText(String.format("%s (%.1f)", windowDate, Math.abs(deltaHours / 24.0)));
								}

								if (deltaHours < 0) {
									cell.setImage(imageDarkArrowDown);
								} else {
									cell.setImage(imageDarkArrowUp);
								}

							} else {
								cell.setText(windowDate);
							}
						} else {
							PaperDealAllocation pda = null;
							if (tableRow.getLhsBefore() != null && tableRow.getLhsBefore().getPaperDealAllocation() != null) {
								pda = tableRow.getLhsBefore().getPaperDealAllocation();
							} else if (tableRow.getLhsAfter() != null && tableRow.getLhsAfter().getPaperDealAllocation() != null) {
								pda = tableRow.getLhsAfter().getPaperDealAllocation();
							}
							if (pda != null && pda.getPaperDeal() != null && pda.getPaperDeal().getStartDate() != null) {
								cell.setText(pda.getPaperDeal().getStartDate().format(formatter));
							}
						}

					} else {
						// Unlike other RHS colums where we want to diff against the old and new slot
						// linked to the cargo, we want to show the date diff for this slot.
						final SlotAllocation originalDischargeAllocation = tableRow.getRhsBefore() != null ? tableRow.getRhsBefore().getDischargeAllocation() : null;
						final SlotAllocation newDischargeAllocation = tableRow.getRhsAfter() != null ? tableRow.getRhsAfter().getDischargeAllocation() : null;
						isSpot = tableRow.isRhsSpot();

						if (newDischargeAllocation != null) {
							final Slot<?> slot = newDischargeAllocation.getSlot();
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
								if (textualVesselMarkers) {
									cell.setText(String.format("%s (%s%.1f)", windowDate, deltaHours < 0 ? "↓" : "↑", Math.abs(deltaHours / 24.0)));
								} else {
									cell.setText(String.format("%s (%.1f)", windowDate, Math.abs(deltaHours / 24.0)));
								}

								if (deltaHours < 0) {
									cell.setImage(imageDarkArrowDown);
								} else {
									cell.setImage(imageDarkArrowUp);
								}
							} else {
								cell.setText(windowDate);
							}
						}
					}
				}
			}
		};
	}

	private CellLabelProvider createDeltaLabelProvider(final boolean asInt, final boolean asCost, final boolean withColour, final boolean isLHS, final EStructuralFeature field,
			final EStructuralFeature attrib) {

		final EReference from = isLHS ? ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_BEFORE : ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_BEFORE;
		final EReference to = isLHS ? ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_AFTER : ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_AFTER;

		return createLambdaLabelProvider(asInt, asCost, withColour, false, change -> getNumber(from, field, attrib, change), change -> getNumber(to, field, attrib, change));
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

	private CellLabelProvider createPNLDeltaLabelProvider(final boolean showAlt) {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				
				cell.setText("");
				cell.setImage(null);
				cell.setFont(null);

				if (element instanceof ChangeSetTableGroup) {

					cell.setFont(boldFont);

					ChangeSetTableGroup changeSet = (ChangeSetTableGroup) element;
					if (showAlt) {
						changeSet = changeSet.getLinkedGroup();
					}
					if (changeSet != null) {
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
								if (textualVesselMarkers) {
									cell.setText(String.format("%s%,.3G", metrics.getPnlDelta() < 0 ? "↓" : "↑", Math.abs(delta)));
								} else {
									cell.setText(String.format("%,.3G", Math.abs(delta)));
								}

								if (metrics.getPnlDelta() < 0) {
									cell.setImage(imageRedArrowDown);
								} else {
									cell.setImage(imageGreenArrowUp);
								}
							}
						}
					}
				}
				if (element instanceof ChangeSetTableRow) {
					ChangeSetTableRow change = (ChangeSetTableRow) element;
					if (showAlt && change.eContainer() instanceof ChangeSetTableGroup) {
						final ChangeSetTableGroup thisGroup = (ChangeSetTableGroup) change.eContainer();
						final ChangeSetTableGroup altGroup = thisGroup.getLinkedGroup();
						if (altGroup != null) {
							final boolean checkLHS = change.getLhsName() != null && change.getLhsName().isEmpty();
							final String name = checkLHS ? change.getLhsName() : change.getRhsName();
							change = null;
							if (name != null) {
								for (final ChangeSetTableRow row : altGroup.getRows()) {
									if (checkLHS) {
										if (name.equals(row.getLhsName())) {
											change = row;
											break;
										}
									} else {
										if (name.equals(row.getRhsName())) {
											change = row;
											break;
										}
									}
								}
							}
						} else {
							change = null;
						}
					}
					final long f = ChangeSetKPIUtil.getPNL(change, ResultType.Before);
					final long t = ChangeSetKPIUtil.getPNL(change, ResultType.After);

					double delta = t - f;
					delta = delta / 1000000.0;
					if (Math.abs(delta) < 0.0001) {
						delta = 0;
					}
					if (delta != 0) {

						if (textualVesselMarkers) {
							cell.setText(String.format("%s %,.3G", delta < 0 ? "↓" : "↑", Math.abs(delta)));
						} else {
							cell.setText(String.format("%,.3G", Math.abs(delta)));
						}

						if (delta < 0) {
							cell.setImage(imageRedArrowDown);
						} else {
							cell.setImage(imageGreenArrowUp);
						}
					}

				}
			}
		};
	}

	private CellLabelProvider createTaxDeltaLabelProvider() {
		return createLambdaLabelProvider(true, false, true, true, change -> {
			return ChangeSetKPIUtil.getTax(change, ResultType.Before) + getExtraValue(change, ResultType.Before, IChangeSetColumnValueExtender::getAdditionalTaxEtcValue);
		}, change -> {
			return ChangeSetKPIUtil.getTax(change, ResultType.After) + getExtraValue(change, ResultType.After, IChangeSetColumnValueExtender::getAdditionalTaxEtcValue);
		});
	}

	private CellLabelProvider createLatenessDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;
					String latenessTooltip = "";
					final String lhsLateness = getLatenessDetailForSlot(change, change.getLhsName());
					final String rhsLateness = getLatenessDetailForSlot(change, change.getRhsName());
					if (lhsLateness != null) {
						latenessTooltip = lhsLateness;
					}
					if (rhsLateness != null) {
						if (latenessTooltip != null && !latenessTooltip.isBlank()) {
							latenessTooltip += "\r\n";
							latenessTooltip += "\r\n";
						}
						latenessTooltip += rhsLateness;
					}
					return latenessTooltip;
				}
				return null;
				
			}

			private String getLatenessDetailForSlot(@NonNull final ChangeSetTableRow change, final String slotName) {
				if (slotName == null) {
					return null;
				}
				final long originalLateness = ChangeSetKPIUtil.getLatenessInHours(change, ResultType.Before, slotName);
				final long originalFlexAvailable = ChangeSetKPIUtil.getFlexAvailableInHours(change, ResultType.Before, slotName);
				final long newLateness = ChangeSetKPIUtil.getLatenessInHours(change, ResultType.After, slotName);
				final long newFlexAvailable = ChangeSetKPIUtil.getFlexAvailableInHours(change, ResultType.After, slotName);				
				final long deltaLateness = newLateness - originalLateness;
				if (deltaLateness == 0) {
					return null;
				}
				final String direction = (deltaLateness > 0 ? " more late." : " less late.");
				final StringBuilder sb = new StringBuilder();
				sb.append(slotName);
				sb.append(" is ").append(LatenessUtils.formatLatenessHoursConcise(Math.abs(deltaLateness))).append(direction).append("\n");
				sb.append("Before:\t").append(LatenessUtils.formatLatenessHoursConcise(originalLateness));
				if (originalLateness != 0) {
					sb.append(getFlexString(originalLateness, originalFlexAvailable));
				}
				sb.append("\r\nAfter: \t").append(LatenessUtils.formatLatenessHoursConcise(newLateness));
				if (newLateness != 0) {
					sb.append(getFlexString(newLateness, newFlexAvailable));
				}
				return sb.toString();
			}

			private String getFlexString(final long lateness, final long availableFlex) {
				final StringBuilder sb = new StringBuilder();
				if (availableFlex > 0) {
					sb.append(" \t(");
					if (lateness > availableFlex) {
						sb.append(LatenessUtils.formatLatenessHoursConcise(lateness - availableFlex)).append(" more than flex of ").append(LatenessUtils.formatLatenessHoursConcise(availableFlex)).append(")");
					}
					else {
						sb.append("using flex of ").append(LatenessUtils.formatLatenessHoursConcise(availableFlex)).append(")");
					}
				}
				return sb.toString();
			}
			
			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setForeground(null);
				cell.setImage(null);
				cell.setFont(null);
				if (element instanceof ChangeSetTableGroup) {

					cell.setFont(boldFont);

					final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
					final Metrics scenarioMetrics = group.getCurrentMetrics();
					final DeltaMetrics deltaMetrics = group.getDeltaMetrics();

					if (deltaMetrics != null) {
						final int latenessDelta = (int) Math.round((double) deltaMetrics.getLatenessDelta() / 24.0);
						if (deltaMetrics.getLatenessDelta() != 0) {
							if (textualVesselMarkers) {
								cell.setText(String.format("%s%d", latenessDelta < 0 ? "↓" : latenessDelta == 0 ? "" : "↑", Math.abs(latenessDelta)));
							} else {
								cell.setText(String.format("%d", Math.abs(latenessDelta)));
							}

							if (deltaMetrics.getLatenessDelta() < 0) {
								cell.setImage(imageGreenArrowDown);
							} else if (deltaMetrics.getLatenessDelta() > 0) {
								cell.setImage(imageRedArrowUp);
							}
							if (deltaMetrics.getLatenessDelta() > 0) {
								cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
							}
						}
					}
				}
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final long[] originalLateness = ChangeSetKPIUtil.getLateness(change, ResultType.Before);
					final long[] newLateness = ChangeSetKPIUtil.getLateness(change, ResultType.After);

					final boolean originalInFlex = originalLateness[ChangeSetKPIUtil.FlexType.TotalIfFlexInsufficient.ordinal()] > 0 && originalLateness[ChangeSetKPIUtil.FlexType.TotalIfWithinFlex.ordinal()] == 0;
					final boolean newInFlex = newLateness[ChangeSetKPIUtil.FlexType.TotalIfFlexInsufficient.ordinal()] > 0 && newLateness[ChangeSetKPIUtil.FlexType.TotalIfWithinFlex.ordinal()] == 0;

					String flexStr = "";
					if (originalInFlex != newInFlex) {
						// Lateness shift between flex and non-flex times.
						final long delta = newLateness[ChangeSetKPIUtil.FlexType.TotalIfFlexInsufficient.ordinal()] - originalLateness[ChangeSetKPIUtil.FlexType.TotalIfFlexInsufficient.ordinal()];
						if (originalLateness[ChangeSetKPIUtil.FlexType.TotalIfFlexInsufficient.ordinal()] == 0) {
							assert originalInFlex == false;
							flexStr = " *";
						} else if (delta > 0) {
							flexStr = "";
						} else {
							flexStr = " *";
						}
					}
					long delta = 0L;
					delta -= originalLateness[ChangeSetKPIUtil.FlexType.TotalIfFlexInsufficient.ordinal()];
					delta += newLateness[ChangeSetKPIUtil.FlexType.TotalIfFlexInsufficient.ordinal()];
					final long originalDelta = delta;
					delta = (int) Math.round((double) delta / 24.0);
					if (delta != 0L) {

						if (textualVesselMarkers) {
							cell.setText(String.format("%s %d%s", delta < 0 ? "↓" : "↑", Math.abs(delta), flexStr));
						} else {
							cell.setText(String.format("%d%s", Math.abs(delta), flexStr));
						}

						if (delta < 0) {
							cell.setImage(imageGreenArrowDown);
						} else {
							cell.setImage(imageRedArrowUp);
						}
					} else if (originalDelta != 0L) {

						if (textualVesselMarkers) {
							cell.setText(String.format("%s %s%s", originalDelta < 0 ? "↓" : "↑", "<1", flexStr));
						} else {
							cell.setText(String.format("%s%s", "<1", flexStr));
						}
						if (originalDelta < 0) {
							cell.setImage(imageGreenArrowDown);
						} else {
							cell.setImage(imageRedArrowUp);
						}
					}
				}
			}
		};

	}

	private CellLabelProvider createNominationBreaksLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow cstr = (ChangeSetTableRow)element;
					final IScenarioDataProvider sdp = getScenarioDataProvider(cstr);
					return getNominationBreaksDetail(sdp, cstr);
				}
				return super.getToolTipText(element);
			}
			
			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setForeground(null);
				cell.setFont(boldFont);

				if (element instanceof ChangeSetTableGroup) {
					final ChangeSetTableGroup cstg = (ChangeSetTableGroup)element;
					cell.setText(getNominationBreaksCountString(getScenarioDataProvider(cstg), cstg));	
				}
				if (element instanceof ChangeSetTableRow) {	
					final ChangeSetTableRow cstr = (ChangeSetTableRow)element;
					cell.setText(getNominationBreaks(getScenarioDataProvider(cstr), cstr));
				}
			}
		};		
	}
	
	private IScenarioDataProvider getScenarioDataProvider(final ChangeSetTableRow change) {
		if (change.eContainer() instanceof ChangeSetTableGroup) {
			return getScenarioDataProvider((ChangeSetTableGroup)change.eContainer());
		}
		else {
			return null;
		}
	}

	private IScenarioDataProvider getScenarioDataProvider(final ChangeSetTableGroup change) {
		if (change.getBaseScenario() != null && change.getBaseScenario().getScenarioDataProvider() != null) {
			return change.getBaseScenario().getScenarioDataProvider();
		}
		else {
			return null;
		}
	}

	private String getNominationBreaksCountString(final IScenarioDataProvider sdp, final ChangeSetTableGroup change) {
		int cnt = 0;
		for (final ChangeSetTableRow row : change.getRows()) {
			cnt += getNominationBreakCount(sdp, row);
		}
		if (cnt > 0) {
			return Integer.toString(cnt);
		}
		else {
			return "";
		}
	}
	
	private String getNominationBreaks(final IScenarioDataProvider sdp, final ChangeSetTableRow change) {
		final int cnt = getNominationBreakCount(sdp, change);
		if (cnt > 0) {
			return Integer.toString(cnt);
		}
		else {
			return "";
		}
	}

	private String getNominationBreaksDetail(final IScenarioDataProvider sdp, final ChangeSetTableRow change) {
		final StringBuilder sb = new StringBuilder();
		addNominations(sdp, change.getLhsName(), change.getLhsBefore(), change.getLhsAfter(), sb);
		addNominations(sdp, change.getRhsName(), change.getRhsBefore(), change.getRhsAfter(), sb);
		if (sb.length() == 0) {
			return null;
		}
		else {
			return sb.toString();
		}
	}

	private void addNominations(final IScenarioDataProvider sdp, final String slotName, final ChangeSetRowData before, final ChangeSetRowData after, final StringBuilder sb) {
		final List<AbstractNomination> noms = this.getAffectedNominations(sdp, slotName, before, after);	
		if (!noms.isEmpty()) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(slotName).append(": ");
			for (final var n : noms) {
				if (sb.charAt(sb.length()-2) != ':') {
					sb.append(", ");
				}
				sb.append(n.getType());
			}
		}
	}
		
	private int getNominationBreakCount(final IScenarioDataProvider sdp, final ChangeSetTableRow change) {
		int cnt = 0;
		cnt += getAffectedNominations(sdp, change).size();
		return cnt;
	}

	private List<AbstractNomination> getAffectedNominations(final IScenarioDataProvider sdp, final ChangeSetTableRow change) {
		final List<AbstractNomination> lhsNoms = getAffectedNominations(sdp, change.getLhsName(), change.getLhsBefore(), change.getLhsAfter());
		final List<AbstractNomination> rhsNoms = getAffectedNominations(sdp, change.getRhsName(), change.getRhsBefore(), change.getRhsAfter());
		final List<AbstractNomination> affectedNoms = new ArrayList<AbstractNomination>(lhsNoms.size() + rhsNoms.size());
		affectedNoms.addAll(lhsNoms);
		affectedNoms.addAll(rhsNoms);
		return affectedNoms;
	}
	
	private List<AbstractNomination> getAffectedNominations(final IScenarioDataProvider sdp, final String slotName, final ChangeSetRowData before, final ChangeSetRowData after) {
		if (sdp != null) {
			List<AbstractNomination> noms = NominationsModelUtils.findNominationsForSlot(sdp, slotName);
			noms = filterAffectedNominations(noms, before, after);		
			return noms;
		}
		else {
			return Collections.emptyList();
		}
	}
	
	private List<AbstractNomination> filterAffectedNominations(List<AbstractNomination> noms, ChangeSetRowData before, ChangeSetRowData after) {
		List<AbstractNomination> affectedNoms = new ArrayList<>();
		for (AbstractNomination n : noms) {
			String type = n.getType();
			String nominatedValue = n.getNominatedValue();
			
			//If no nominated value specified yet, then no nomination break.
			if (nominatedValue != null && !nominatedValue.isBlank()) {
				String[] dependentFields = NominationTypeRegistry.getInstance().getDependentFields(type);
				if (dependentFields != null && dependentFields.length > 0) {
					for (String field : dependentFields) {
						Object fieldBefore = getFieldValue(before, field);
						Object fieldAfter = getFieldValue(after, field);

						if (type.toLowerCase().contains("volume")) {
							int nominatedVolume = NominationsModelUtils.getNominatedVolumeValue(n);
							if (fieldBefore instanceof Integer) {
								fieldBefore = Boolean.valueOf(((Integer)fieldBefore) >= nominatedVolume);
							}
							if (fieldAfter instanceof Integer) {
								fieldAfter = Boolean.valueOf(((Integer)fieldAfter) >= nominatedVolume);			
							}
						}
						else if (type.toLowerCase().contains("window")) {
							Object nominatedObject = NominationsModelUtils.getNominatedValueObjectFromJSON(type, nominatedValue);
							if (nominatedObject instanceof TimeWindowHolder) {
								TimeWindowHolder nominatedTimeWindow = (TimeWindowHolder)nominatedObject;
								SlotAllocation beforeSA = getSlotAllocation(type, before);
								SlotAllocation afterSA = getSlotAllocation(type, before);
								
								if (beforeSA != null) {
									fieldBefore = withinNominationTimeWindow(beforeSA, nominatedTimeWindow);
								}
								if (afterSA != null) {
									fieldAfter = withinNominationTimeWindow(afterSA, nominatedTimeWindow);
								}
							}
							//Else must be empty string, so ignore it.
						}
						else {
							fieldBefore = nominatedValue;
						}
						fieldAfter = getName(fieldAfter);

						//If condition broken after-would.
						if (fieldBefore instanceof Boolean && fieldAfter instanceof Boolean && 
							fieldAfter.equals(Boolean.FALSE)) {
							affectedNoms.add(n);
							break;
						}
						//Else if values different from nominated value.
						else if (!(fieldBefore instanceof Boolean && fieldAfter instanceof Boolean) && 
								fieldBefore != null && fieldAfter != null && 
								(!Objects.equals(fieldBefore, fieldAfter))) {
							affectedNoms.add(n);
							break; //No need to add it twice.
						}
					}
				}
				else {
					//No dependent fields found, so assume affected if any change to slot.
					affectedNoms.add(n);
				}
			} //End if nominatedValue not blank.
		} //End for each nomination.
		
		return affectedNoms;
	}
	
	private SlotAllocation getSlotAllocation(String type, ChangeSetRowData rowData) {
		if (type.toLowerCase().contains("buy")) {
			return rowData.getLoadAllocation();
		}
		else if (type.toLowerCase().contains("sell")) {
			return rowData.getDischargeAllocation();
		}
		return null;
	}

	/**
	 * Determine if this slot visit is within the time window.
	 * @generated NOT
	 */
	boolean withinNominationTimeWindow(SlotAllocation slotAllocation, @NonNull TimeWindowHolder window) {
		if (slotAllocation != null && slotAllocation.getSlotVisit() != null) {
			ZonedDateTime start = slotAllocation.getSlotVisit().getStart();
			if (start != null) {
				Slot<?> slot = slotAllocation.getSlot();
				if (start != null) {
					return (!start.toLocalDate().isBefore(window.getWindowStart()) && 
							!start.toLocalDate().isAfter(window.getWindowEnd()));
				}
			}
		}
		//No slot or start date date.
		return false;
	}
	
	/**
	 * Gets the name of the object if possible.
	 * @param obj
	 * @return the name of the object.
	 */
	private Object getName(Object obj) {
		try {
			if (obj != null) {
				final Class<?> cls = obj.getClass();
				final Method[] methods = cls.getMethods();

				for (final Method m : methods) {
					if (m.getName().equals("getName")) {
						obj = m.invoke(obj);
						break;
					}
				}
			}

			return obj;
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}

		return obj;
	}
	
	private Object getFieldValue(Object obj, final String fieldPath) {
		try {
			final String[] elementNames = fieldPath.split("\\.");
			
			for (final String elementName : elementNames) {
				if (obj != null) {
					final Class<?> cls = obj.getClass();

					//Is it a method / getter.
					if (elementName.endsWith("()")) {
						final String methodName = elementName.replace("()","");
						final Method[] methods = cls.getMethods();

						for (final Method m : methods) {
							if (m.getName().equals(methodName)) {
								obj = m.invoke(obj);
								break;
							}
						}
					}
					//Or a field?
					else {
						final Field[] fields = cls.getFields();
						for (final Field f : fields) {
							if (f.getName().equals(elementName)) {
								obj = f.get(obj);
								break;
							}
						}
					}
				}
				else {
					break;
				}
			}
			
			//Value found successfully.
			return obj;
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	

	
	private CellLabelProvider createViolationsDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public String getToolTipText(final Object element) {

				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final Set<CapacityViolationType> beforeViolations = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.Before));
					final Set<CapacityViolationType> afterViolations = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.After));

					//Check for nominal vessel changes.
					final long nominalVesselCountBefore = ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.Before);
					final long nominalVesselCountAfter = ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.After);
					final long nominalVesselDelta = nominalVesselCountAfter - nominalVesselCountBefore;
				
					String vesselBefore = change.getBeforeVesselShortName();
					String vesselAfter = change.getAfterVesselShortName();
					String vesselAfterType = "";
					ChangeSetVesselType afterVesselType = change.getAfterVesselType();

					switch(afterVesselType) { 	
						case FLEET:
							vesselAfterType = "charter ";
						case MARKET: 
							vesselAfterType = "market vessel ";
					}

					long capacityDelta = afterViolations.size() - beforeViolations.size();
					
					final Set<CapacityViolationType> newViolations = new HashSet<>(afterViolations);
					newViolations.removeAll(beforeViolations);
					final StringBuilder sb = new StringBuilder();
				
					if (!newViolations.isEmpty() || nominalVesselDelta > 0) {
						int addedIssues = newViolations.size();
						if (nominalVesselDelta > 0) {
							addedIssues += Math.abs(nominalVesselDelta);
						}
						sb.append(String.format("Added:\r\n"));
						if (nominalVesselDelta > 0) {
							sb.append("- Nominal cargo ");
							sb.append("moved to " + vesselAfterType + "'");
							sb.append(vesselAfter);
							sb.append("'\r\n");
						}
					}
					if (!newViolations.isEmpty()) {
						sb.append(String.format("%s", generateDisplayString(newViolations)));
					}
					newViolations.clear();
					newViolations.addAll(beforeViolations);
					newViolations.removeAll(afterViolations);

					if (!newViolations.isEmpty() || nominalVesselDelta < 0) {
						int resolvedIssues = newViolations.size();
						if (nominalVesselDelta < 0) {
							resolvedIssues += Math.abs(nominalVesselDelta);
						}
						if (sb.length() > 0) {
							sb.append("\r\n");
						}
						sb.append(String.format("Resolved:\r\n"));
						if (nominalVesselDelta < 0) {
							sb.append("- Nominal cargo ");
							sb.append("moved to " + vesselAfterType);
							sb.append("'" + vesselAfter + "'");
							sb.append("\r\n");
						}
					}
					if (!newViolations.isEmpty()) {
						sb.append(String.format("%s", generateDisplayString(newViolations)));
					}
					
					if (nominalVesselDelta != 0) {
						if (sb.length() > 0) {
							//Place nominal vessel changes explanation on a new line.
							sb.append("\r\n");
						}
					}
					
					return sb.toString();
				}

				return super.getToolTipText(element);
			}

			private final Map<CapacityViolationType, String> nameMap = new HashMap<>();
			{
				nameMap.put(CapacityViolationType.FORCED_COOLDOWN, "Forced cooldown");
				nameMap.put(CapacityViolationType.LOST_HEEL, "Lost heel");
				nameMap.put(CapacityViolationType.MAX_DISCHARGE, "Max discharge");
				nameMap.put(CapacityViolationType.MAX_HEEL, "Max heel");
				nameMap.put(CapacityViolationType.MAX_LOAD, "Max load");
				nameMap.put(CapacityViolationType.MIN_DISCHARGE, "Min discharge");
				nameMap.put(CapacityViolationType.MIN_HEEL, "Min heel");
				nameMap.put(CapacityViolationType.MIN_LOAD, "Min load");
				nameMap.put(CapacityViolationType.VESSEL_CAPACITY, "Vessel capacity");
			}

			private String generateDisplayString(final Set<CapacityViolationType> tmp) {
				final List<String> sorted = tmp.stream() //
						.map(nameMap::get) //
						.sorted() // .
						.collect(Collectors.toList());

				StringBuilder sb = new StringBuilder();
				for (String violation : sorted) {
					if (sb.length() > 0) {
						sb.append("\r\n");
					}
					sb.append("- ");
					sb.append(violation);
					sb.append(" violation");
				}
				return sb.toString();
			}

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setForeground(null);
				cell.setImage(null);
				cell.setFont(null);
				if (element instanceof ChangeSetTableGroup) {
					cell.setFont(boldFont);

					final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
					final DeltaMetrics deltaMetrics = group.getDeltaMetrics();
					
					//Check for nominal vessel changes.
					long nominalVesselCountBefore = 0;
					long nominalVesselCountAfter = 0;
					for (ChangeSetTableRow tr : ((ChangeSetTableGroup) element).getRows()) {
						nominalVesselCountBefore += ChangeSetKPIUtil.getNominalVesselCount(tr, ResultType.Before);
						nominalVesselCountAfter += ChangeSetKPIUtil.getNominalVesselCount(tr, ResultType.After);
					}
					
					final long nominalVesselDelta = nominalVesselCountAfter - nominalVesselCountBefore;
					long netDelta = 0;
					long deltaCapacity = 0;
					if (deltaMetrics != null) {
						deltaCapacity = deltaMetrics.getCapacityDelta();
						netDelta = deltaCapacity + nominalVesselDelta;
					}
					
					if (nominalVesselDelta != 0 || deltaCapacity != 0) {
							if (textualVesselMarkers) {
								cell.setText(
										String.format("%s%s%d", deltaMetrics.getCapacityDelta() < 0 ? "↓" : 
											deltaMetrics.getCapacityDelta() == 0 ? "" : "↑", 
													nominalVesselDelta < 0 ? "↓" : 
														nominalVesselDelta == 0 ? "" : "↑",
													Math.abs(netDelta)));
							} else {
								cell.setText(String.format("%d", Math.abs(netDelta)));
							}
							if (netDelta < 0) {
								cell.setImage(imageGreenArrowDown);
							} else if (netDelta > 0) {
								cell.setImage(imageRedArrowUp);
							}
					}
				}
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					boolean issueChange = isIssueChange(change);
					final long f = ChangeSetKPIUtil.getViolations(change, ResultType.Before);
					final long t = ChangeSetKPIUtil.getViolations(change, ResultType.After);

					//Check for nominal vessel changes.
					final long nominalVesselCountBefore = ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.Before);
					final long nominalVesselCountAfter = ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.After);
					final long nominalVesselDelta = nominalVesselCountAfter - nominalVesselCountBefore;
					
					final long delta = t - f;
					final long netDelta = delta + nominalVesselDelta;
					
					if (issueChange) {
						if (textualVesselMarkers) {
							cell.setText(String.format("%s%s %d", delta < 0 ? "↓" : (delta > 0? "↑" : ""), 
									nominalVesselDelta < 0 ? "↓" : (nominalVesselDelta > 0 ? "↑" : ""), 
											Math.abs(netDelta)));
						} else {
							cell.setText(String.format("%d", Math.abs(netDelta)));
						}
						
						if (netDelta < 0) {
							cell.setImage(imageGreenArrowDown);
						} else if (netDelta > 0) {
							cell.setImage(imageRedArrowUp);
						}
					}

				}
			}
		};
	}

	static boolean isIssueChange(ChangeSetTableRow change) {
		final Set<CapacityViolationType> beforeViolations = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.Before));
		final Set<CapacityViolationType> afterViolations = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.After));
		final int nominalVesselCountBefore = (int)ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.Before);
		final int nominalVesselCountAfter = (int)ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.After);
		return (!beforeViolations.equals(afterViolations)) || (nominalVesselCountBefore != nominalVesselCountAfter);
	}

	static int getNOfAddedIssues(ChangeSetTableRow change) {
		final Set<CapacityViolationType> beforeViolations = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.Before));
		final Set<CapacityViolationType> afterViolations = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.After));
		final int nominalVesselCountBefore = (int)ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.Before);
		final int nominalVesselCountAfter = (int)ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.After);
		afterViolations.removeAll(beforeViolations);
		int addedIssues = afterViolations.size();
		if (nominalVesselCountAfter > nominalVesselCountBefore) {
			addedIssues++;
		}
		return addedIssues;
	}

	static int getNOfResolvedIssues(ChangeSetTableRow change) {
		final Set<CapacityViolationType> beforeViolations = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.Before));
		final Set<CapacityViolationType> afterViolations = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.After));
		final int nominalVesselCountBefore = (int)ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.Before);
		final int nominalVesselCountAfter = (int)ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.After);
		beforeViolations.removeAll(afterViolations);
		int resolvedIssues = beforeViolations.size();
		if (nominalVesselCountAfter < nominalVesselCountBefore) {
			resolvedIssues++;
		}
		return resolvedIssues;
	}
	
	static int getCapacityDelta(ChangeSetTableRow change) {
		final Set<CapacityViolationType> beforeViolations = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.Before));
		final Set<CapacityViolationType> afterViolations = ScheduleModelKPIUtils.getCapacityViolations(ChangeSetKPIUtil.getEventGrouping(change, ResultType.After));
		int capacityDelta = afterViolations.size() - beforeViolations.size();
		return capacityDelta;
	}

	static int getNominalVesselDelta(ChangeSetTableRow change) {
		//Check for nominal vessel changes.
		final int nominalVesselCountBefore = (int)ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.Before);
		final int nominalVesselCountAfter = (int)ChangeSetKPIUtil.getNominalVesselCount(change, ResultType.After);
		final int nominalVesselDelta = nominalVesselCountAfter - nominalVesselCountBefore;
		return nominalVesselDelta;
	}
	
	private CellLabelProvider createCargoOtherPNLDeltaLabelProvider() {
		return createLambdaLabelProvider(true, false, true, false, change -> {
			return ChangeSetKPIUtil.getCargoOtherPNL(change, ResultType.Before) + getExtraValue(change, ResultType.Before, IChangeSetColumnValueExtender::getAdditionalCargoOtherValue);
		}, change -> {
			return ChangeSetKPIUtil.getCargoOtherPNL(change, ResultType.After) + getExtraValue(change, ResultType.After, IChangeSetColumnValueExtender::getAdditionalCargoOtherValue);
		});
	}

	private CellLabelProvider createUpstreamDeltaLabelProvider() {

		return createLambdaLabelProvider(true, false, true, true, change -> {
			return ChangeSetKPIUtil.getUpstreamPNL(change, ResultType.Before) + getExtraValue(change, ResultType.Before, IChangeSetColumnValueExtender::getAdditionalUpstreamValue);
		}, change -> {
			return ChangeSetKPIUtil.getUpstreamPNL(change, ResultType.After) + getExtraValue(change, ResultType.After, IChangeSetColumnValueExtender::getAdditionalUpstreamValue);
		});
	}

	private CellLabelProvider createAdditionalShippingPNLDeltaLabelProvider() {

		return createLambdaLabelProvider(true, true, true, true, change -> {
			return ChangeSetKPIUtil.getAdditionalShippingPNL(change, ResultType.Before) + getExtraValue(change, ResultType.Before, IChangeSetColumnValueExtender::getAdditionalShippingDESValue);
		}, change -> {
			return ChangeSetKPIUtil.getAdditionalShippingPNL(change, ResultType.After) + getExtraValue(change, ResultType.After, IChangeSetColumnValueExtender::getAdditionalShippingDESValue);
		});

	}

	private CellLabelProvider createAdditionalUpsidePNLDeltaLabelProvider() {

		return createLambdaLabelProvider(true, true, true, true, change -> {
			return ChangeSetKPIUtil.getAdditionalUpsidePNL(change, ResultType.Before) + getExtraValue(change, ResultType.Before, IChangeSetColumnValueExtender::getAdditionalUpsideValue);
		}, change -> {
			return ChangeSetKPIUtil.getAdditionalUpsidePNL(change, ResultType.After) + getExtraValue(change, ResultType.After, IChangeSetColumnValueExtender::getAdditionalUpsideValue);
		});
	}

	private CellLabelProvider createShippingDeltaLabelProvider() {
		return createLambdaLabelProvider(true, true, true, false, change -> {
			return ChangeSetKPIUtil.getShipping(change, ResultType.Before) + getExtraValue(change, ResultType.Before, IChangeSetColumnValueExtender::getAdditionalShippingFOBValue);
		}, change -> {
			return ChangeSetKPIUtil.getShipping(change, ResultType.After) + getExtraValue(change, ResultType.After, IChangeSetColumnValueExtender::getAdditionalShippingFOBValue);
		});
	}

	private CellLabelProvider createShippingDaysDeltaLabelProvider(final ShippingCostType shippingCostType) {
		return createLambdaDaysLabelProvider(change -> ChangeSetKPIUtil.getShipping(change, ResultType.Before, shippingCostType),
				change -> ChangeSetKPIUtil.getShipping(change, ResultType.After, shippingCostType));
	}

	private CellLabelProvider createShippingCostDeltaLabelProvider(final ShippingCostType shippingCostType) {
		return createLambdaLabelProvider(true, true, true, false, change -> ChangeSetKPIUtil.getShipping(change, ResultType.Before, shippingCostType),
				change -> ChangeSetKPIUtil.getShipping(change, ResultType.After, shippingCostType));
	}

	private CellLabelProvider createShippingCostDeltaLabelProvider(final ShippingCostType... shippingCostTypes) {
		return createLambdaLabelProvider(true, true, true, false, change -> {
			long sum = 0L;
			for (final ShippingCostType shippingCostType : shippingCostTypes) {
				sum += ChangeSetKPIUtil.getShipping(change, ResultType.Before, shippingCostType);
			}
			return (Number) Long.valueOf(sum);
		}, change -> {
			long sum = 0L;
			for (final ShippingCostType shippingCostType : shippingCostTypes) {
				sum += ChangeSetKPIUtil.getShipping(change, ResultType.After, shippingCostType);
			}
			return (Number) Long.valueOf(sum);
		});
	}

	private CellLabelProvider createCSLabelProvider(final InsertionPlanGrouperAndFilter insertionPlanFilter) {

		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setImage(null);
				cell.setFont(null);
				if (element instanceof ChangeSetTableGroup) {
					// ChangeSetNode changeSetNode = (ChangeSetNode) element;
					final ChangeSetTableGroup changeSet = (ChangeSetTableGroup) element;
					cell.setFont(null);
					if (insertionPlanFilter.isUnexpandedInsertionGroup(changeSet)) {
						cell.setFont(boldFont);
					} else {
						// cell.setFont(boldFont);
					}
					// cell.setFont(boldFont);
					final ChangeSetTableRoot root = (ChangeSetTableRoot) changeSet.eContainer();
					int idx = 0;
					if (root != null) {
						idx = root.getGroups().indexOf(changeSet);
					}

					final String text = changeSetColumnLabelProvider.apply(changeSet, idx);
					cell.setText(text);
					// }
					// if (element instanceof ChangeSetTableGroup) {

				} else if (false && element instanceof ChangeSetTableRow) {
					// DEBUG helper, show P&L sums in label
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final long pnl = ChangeSetKPIUtil.getPNL(change, ResultType.After) - ChangeSetKPIUtil.getPNL(change, ResultType.Before);
					final long purchase = ChangeSetKPIUtil.getPurchaseCost(change, ResultType.After) - ChangeSetKPIUtil.getPurchaseCost(change, ResultType.Before);
					final long sales = ChangeSetKPIUtil.getSalesRevenue(change, ResultType.After) - ChangeSetKPIUtil.getSalesRevenue(change, ResultType.Before);
					final long shipFOB = ChangeSetKPIUtil.getShipping(change, ResultType.After) - ChangeSetKPIUtil.getShipping(change, ResultType.Before);

					// Note: Missing other PNL components.
					final long computedPNL = sales - purchase - shipFOB;
//					cell.setText(String.format("%,d", computedPNL));
					cell.setText(String.format("%,d", pnl - computedPNL));
				}
			}
		};

	}

	private CellLabelProvider createVesselLabelProvider(@NonNull final String name, @NonNull final Integer charterNumber) {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				cell.setImage(null);
				final Object element = cell.getElement();
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) element;
					final String fullName = String.format("%s %d", name, charterNumber);
					final String aVesselName = String.format("%s %d", changeSetRow.getAfterVesselName(), changeSetRow.getAfterVesselCharterNumber());
					final String bVesselName = String.format("%s %d", changeSetRow.getBeforeVesselName(), changeSetRow.getBeforeVesselCharterNumber());
					if (fullName.equals(aVesselName)) {
						cell.setImage(imageClosedCircle);
						if (textualVesselMarkers) {
							cell.setText("●");
						}
					} else if (fullName.equals(bVesselName)) {
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
					final String fullName = String.format("%s %d", name, charterNumber);
					final String aVesselName = String.format("%s (%d)", changeSetRow.getAfterVesselName(), changeSetRow.getAfterVesselCharterNumber());
					final String bVesselName = String.format("%s (%d)", changeSetRow.getBeforeVesselName(), changeSetRow.getBeforeVesselCharterNumber());
					if (fullName.equals(aVesselName)) {
						return name;
					}
					if (fullName.equals(bVesselName)) {
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
							} else {
								cell.setText(String.format("%s      %s", left, right));
							}
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

	private CellLabelProvider createLambdaLabelProvider(final boolean asInt, final boolean asCost, final boolean withColour, final boolean asSigFigs, final Function<ChangeSetTableRow, Number> calcF,
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
				cell.setImage(null);
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
						if (textualVesselMarkers) {
							if (asSigFigs) {
								cell.setText(String.format("%s %,.3G", delta < 0 ? "↓" : "↑", Math.abs(delta)));
							} else {
								cell.setText(String.format("%s %,.3f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
							}
						} else {
							if (asSigFigs) {
								cell.setText(String.format("%,.3G", Math.abs(delta)));
							} else {
								cell.setText(String.format("%,.3f", Math.abs(delta)));
							}

							if (delta < 0) {
								if (withColour) {
									if (asCost) {
										cell.setImage(imageGreenArrowDown);
									} else {
										cell.setImage(imageRedArrowDown);
									}
								} else {
									if (asCost) {
										cell.setImage(imageDarkArrowDown);
									} else {
										cell.setImage(imageDarkArrowDown);
									}
								}
							} else {
								if (withColour) {
									if (asCost) {
										cell.setImage(imageRedArrowUp);
									} else {
										cell.setImage(imageGreenArrowUp);
									}
								} else {
									if (asCost) {
										cell.setImage(imageDarkArrowUp);
									} else {
										cell.setImage(imageDarkArrowUp);
									}
								}
							}
						}
					}
				} else {
					if (Math.abs(delta) > 0.009) {
						if (textualVesselMarkers) {
							if (asSigFigs) {
								cell.setText(String.format("%s %,.2G", delta < 0 ? "↓" : "↑", Math.abs(delta)));
							} else {
								cell.setText(String.format("%s %,.2f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
							}
						} else {
							if (asSigFigs) {
								cell.setText(String.format("%,.2G", Math.abs(delta)));
							} else {
								cell.setText(String.format("%,.2f", Math.abs(delta)));
							}

							if (delta < 0) {
								if (withColour) {
									if (asCost) {
										cell.setImage(imageGreenArrowDown);
									} else {
										cell.setImage(imageRedArrowDown);
									}
								} else {
									if (asCost) {
										cell.setImage(imageDarkArrowDown);
									} else {
										cell.setImage(imageDarkArrowDown);
									}
								}
							} else {
								if (withColour) {
									if (asCost) {
										cell.setImage(imageRedArrowUp);
									} else {
										cell.setImage(imageGreenArrowUp);
									}
								} else {
									if (asCost) {
										cell.setImage(imageDarkArrowUp);
									} else {
										cell.setImage(imageDarkArrowUp);
									}
								}
							}
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
				cell.setImage(null);
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
					if (textualVesselMarkers) {
						cell.setText(String.format("%s %d", delta < 0 ? "↓" : "↑", Math.abs(delta)));
					} else {
						cell.setText(String.format("%d", Math.abs(delta)));
					}

					if (delta < 0) {
						cell.setImage(imageRedArrowDown);
					} else {
						cell.setImage(imageRedArrowUp);
					}
				} else if (originalDelta != 0L) {
					if (textualVesselMarkers) {
						cell.setText(String.format("%s %s", originalDelta < 0 ? "↓" : "↑", "<1"));
					} else {
						cell.setText(String.format("%s", "<1"));
					}
					if (originalDelta < 0) {
						cell.setImage(imageRedArrowDown);
					} else {
						cell.setImage(imageRedArrowUp);
					}
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
				cell.setImage(null);
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

					if (textualVesselMarkers) {
						cell.setText(String.format("%s %,.2f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
					} else {
						cell.setText(String.format("%,.2f", Math.abs(delta)));
					}

					if (delta < 0) {
						cell.setImage(imageDarkArrowDown);
					} else {
						cell.setImage(imageDarkArrowUp);
					}
				}
			}

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetTableRow) {

					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final StringBuilder sb = new StringBuilder();
					sb.append("Prices:\n");
					boolean hasTooltip = false;
					if (isLoad) {
						{
							final SlotAllocation allocation = change.getLhsBefore() != null ? change.getLhsBefore().getLoadAllocation() : null;
							if (allocation != null) {
								final Slot slot = allocation.getSlot();
								if (slot != null) {
									final String expr = slot.getPriceExpression();
									if (expr != null && expr.contains("?")) {
										// return String.format("Break-even price is %,.2f", allocation.getPrice());
									} else {

										hasTooltip = true;
										sb.append(String.format("Before: %,.2f\n", allocation.getPrice()));
									}
								}
							}
						}
						{
							final SlotAllocation allocation = change.getLhsAfter() != null ? change.getLhsAfter().getLoadAllocation() : null;
							if (allocation != null) {
								final Slot slot = allocation.getSlot();
								if (slot != null) {
									final String expr = slot.getPriceExpression();
									if (expr != null && expr.contains("?")) {
										return String.format("Break-even price is %,.2f", allocation.getPrice());
									} else {

										hasTooltip = true;
										sb.append(String.format("After: %,.2f", allocation.getPrice()));
									}
								}
							}
						}
					} else {
						{
							final SlotAllocation allocation = change.getRhsBefore() != null ? change.getRhsBefore().getDischargeAllocation() : null;
							if (allocation != null) {
								final Slot slot = allocation.getSlot();
								if (slot != null) {
									final String expr = slot.getPriceExpression();
									if (expr != null && expr.contains("?")) {
										// return String.format("Break-even price is %,.2f", allocation.getPrice());
									} else {
										hasTooltip = true;
										sb.append(String.format("Before: %,.2f (current sale)\n", allocation.getPrice()));
									}
								}
							}
						}
						{
							final SlotAllocation allocation = change.getLhsBefore() != null ? change.getLhsBefore().getDischargeAllocation() : null;
							if (allocation != null) {
								final Slot slot = allocation.getSlot();
								if (slot != null) {
									final String expr = slot.getPriceExpression();
									if (expr != null && expr.contains("?")) {
										// return String.format("Break-even price is %,.2f", allocation.getPrice());
									} else {
										hasTooltip = true;
										sb.append(String.format("Before: %,.2f (previous sale)\n", allocation.getPrice()));
									}
								}
							}
						}
						{
							final SlotAllocation allocation = change.getRhsAfter() != null ? change.getRhsAfter().getDischargeAllocation() : null;
							if (allocation != null) {
								final Slot slot = allocation.getSlot();
								if (slot != null) {
									final String expr = slot.getPriceExpression();
									if (expr != null && expr.contains("?")) {
										return String.format("Break-even price is %,.2f", allocation.getPrice());
									} else {
										hasTooltip = true;
										sb.append(String.format("After: %,.2f (current sale)", allocation.getPrice()));
									}
								}
							}
						}
					}
					if (hasTooltip) {
						return sb.toString();
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
		if (imageSteadyArrow != null) {
			imageSteadyArrow.dispose();
			imageSteadyArrow = null;
		}
		if (imageGreenArrowUp != null) {
			imageGreenArrowUp.dispose();
			imageGreenArrowUp = null;
		}
		if (imageGreenArrowDown != null) {
			imageGreenArrowDown.dispose();
			imageGreenArrowDown = null;
		}
		if (imageRedArrowUp != null) {
			imageRedArrowUp.dispose();
			imageRedArrowUp = null;
		}
		if (imageRedArrowDown != null) {
			imageRedArrowDown.dispose();
			imageRedArrowDown = null;
		}
		if (imageDarkArrowUp != null) {
			imageDarkArrowUp.dispose();
			imageDarkArrowUp = null;
		}
		if (imageDarkArrowDown != null) {
			imageDarkArrowDown.dispose();
			imageDarkArrowDown = null;
		}
		if (colour_VesselTypeColumn != null) {
			colour_VesselTypeColumn.dispose();
			colour_VesselTypeColumn = null;
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

	public void showAlternativePNLColumn(final boolean showAlternativePNLBase) {
		this.showAlternativePNLBase = showAlternativePNLBase;
		if (altPNLBaseColumn != null) {
			RunnerHelper.asyncExec(() -> altPNLBaseColumn.getColumn().setVisible(this.showAlternativePNLBase));
		}
	}

	public static @NonNull BiFunction<ChangeSetTableGroup, Integer, String> getDefaultLabelProvider() {
		return (changeSet, index) -> {

			// final ChangeSetTableGroup changeSet = node.getTableGroup();
			if (changeSet.getDescription() != null && !changeSet.getDescription().isEmpty()) {
				return changeSet.getDescription();
			} else {
				return String.format("Set %d", index + 1);
			}
		};
	}

	public static @NonNull BiFunction<ChangeSetTableGroup, Integer, String> getMultipleSolutionLabelProvider() {
		return (changeSet, index) -> String.format("Solution %s", index + 1);
	}

	public void showCompareColumns(final boolean showCompareColumns) {
		this.showCompareColumns = showCompareColumns;
		RunnerHelper.asyncExec(() -> {
			latenessColumn.getColumn().setVisible(this.showCompareColumns);
			violationColumn.getColumn().setVisible(this.showCompareColumns);
			column_LoadPrice.getColumn().setVisible(this.showCompareColumns);
			column_LoadVolume.getColumn().setVisible(this.showCompareColumns);
			column_DischargePrice.getColumn().setVisible(this.showCompareColumns);
			column_DischargeVolume.getColumn().setVisible(this.showCompareColumns);
		});
	}

	private long getExtraValue(final ChangeSetTableRow change, final ResultType resultType, final ToLongTriFunction<IChangeSetColumnValueExtender, ChangeSetTableRow, ResultType> f) {

		long sum = 0L;
		if (columnExtenders != null) {
			for (final IChangeSetColumnValueExtender extender : columnExtenders) {
				sum += f.applyAsLong(extender, change, resultType);
			}
		}
		return sum;
	}
}