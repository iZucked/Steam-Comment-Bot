/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.headline;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.lingo.reports.views.IProvideEditorInputScenario;
import com.mmxlabs.lingo.reports.views.headline.HeadlineReportTransformer.RowData;
import com.mmxlabs.lingo.reports.views.standard.KPIReportTransformer;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToJSONUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResult;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * The "Headline" Report.
 * 
 */
public class HeadlineReportView extends ViewPart {

	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.HorizontalKPIReportView";

	private SelectedScenariosService selectedScenariosService;

	private GridTableViewer viewer;

	private ScheduleModel scheduleModel;

	private IPartListener partListener;

	private IEditorPart currentActiveEditor;

	private ModelReference modelReference;
	private Font boldFont;

	/**
	 * The type of column, a Textual "Label" or numerical "Value"
	 * 
	 */
	public enum ColumnType {
		LABEL, VALUE
	}

	/**
	 * The ordered list of columns. These are either labels, or values. Label types take a String value and no formatter. Value types take a default Long value representing the largest expected value
	 * which is used to calculate the required column width.
	 */
	public enum ColumnDefinition {
		LABEL_PNL(ColumnType.LABEL, "P&L", null), //
		VALUE_PNL(ColumnType.VALUE, 1000000000l, KPIReportTransformer.TYPE_COST), //
		//
		LABEL_PAPER(ColumnType.LABEL, "Paper", null, KnownFeatures.FEATURE_PAPER_DEALS), //
		VALUE_PAPER(ColumnType.VALUE, 1000000000l, KPIReportTransformer.TYPE_COST, KnownFeatures.FEATURE_PAPER_DEALS), //
		//
		LABEL_TRADING(ColumnType.LABEL, "Trading", null, KnownFeatures.FEATURE_SHOW_TRADING_SHIPPING_SPLIT), //
		VALUE_TRADING(ColumnType.VALUE, 1000000000l, KPIReportTransformer.TYPE_COST, KnownFeatures.FEATURE_SHOW_TRADING_SHIPPING_SPLIT), //
		//
		LABEL_SHIPPING(ColumnType.LABEL, "Shipping", null, KnownFeatures.FEATURE_SHOW_TRADING_SHIPPING_SPLIT), //
		VALUE_SHIPPING(ColumnType.VALUE, 1000000000l, KPIReportTransformer.TYPE_COST, KnownFeatures.FEATURE_SHOW_TRADING_SHIPPING_SPLIT), //
		//
		LABEL_UPSIDE(ColumnType.LABEL, "Upside", null, "features:report-headline-upside"), VALUE_UPSIDE(ColumnType.VALUE, 1000000000l, KPIReportTransformer.TYPE_COST,
				"features:report-headline-upside"), //
		//
		LABEL_SALES_REVENUE(ColumnType.LABEL, "Revenue", null, "features:headline-sales-revenue"), VALUE_SALES_REVENUE(ColumnType.VALUE, 1000000000l, KPIReportTransformer.TYPE_COST,
				"features:headline-sales-revenue"), //
		//
		LABEL_EQUITY(ColumnType.LABEL, "Equity", null, "features:report-equity-book"), VALUE_EQUITY(ColumnType.VALUE, 1000000000l, KPIReportTransformer.TYPE_COST, "features:report-equity-book"), //
		//
		LABEL_IDLE_DAYS(ColumnType.LABEL, "Idle", null, "features:headline-idle-days"), VALUE_IDLE_DAYS(ColumnType.VALUE, 24000l, KPIReportTransformer.TYPE_TIME, "features:headline-idle-days"), //
		//
		LABEL_CHARTER_LENGTH_DAYS(ColumnType.LABEL, "Charter Length", null, "features:headline-charter-length"), VALUE_CHARTER_LENGTH_DAYS(ColumnType.VALUE, 24000l, KPIReportTransformer.TYPE_TIME,
				"features:headline-charter-length"), //
		//
		LABEL_GCO(ColumnType.LABEL, "Charter Out (virt)", null, KnownFeatures.FEATURE_OPTIMISATION_CHARTER_OUT_GENERATION), VALUE_GCO_DAYS(ColumnType.VALUE, 2400l, KPIReportTransformer.TYPE_TIME,
				KnownFeatures.FEATURE_OPTIMISATION_CHARTER_OUT_GENERATION), VALUE_GCO_REVENUE(ColumnType.VALUE, 1000000000l, KPIReportTransformer.TYPE_COST,
						KnownFeatures.FEATURE_OPTIMISATION_CHARTER_OUT_GENERATION), //
		//
		LABEL_PURCHASE_COST(ColumnType.LABEL, "P. Cost", null, "features:headline-purchase-cost"), VALUE_PURCHASE_COST(ColumnType.VALUE, 1000000000l, KPIReportTransformer.TYPE_COST,
				"features:headline-purchase-cost"), //
		//
		LABEL_LATENESS(ColumnType.LABEL, "Late", null), VALUE_LATENESS(ColumnType.VALUE, 5200l, KPIReportTransformer.TYPE_TIME), //
		//
		LABEL_VIOLATIONS(ColumnType.LABEL, "Issues", null), VALUE_VIOLATIONS(ColumnType.VALUE, 100l, ""); //

		private final ColumnType columnType;
		private final Object labelOrDefaultLong;
		private final String formatType;
		private final String feature;

		protected ColumnType getColumnType() {
			return columnType;
		}

		protected String getLabel() {
			return (String) labelOrDefaultLong;
		}

		protected long getDefaultLong() {
			return ((Number) labelOrDefaultLong).longValue();
		}

		protected String getFormatType() {
			return formatType;
		}

		private ColumnDefinition(final ColumnType columnType, final Object labelOrDefaultLong, final String formatType) {
			this(columnType, labelOrDefaultLong, formatType, null);

		}

		private ColumnDefinition(final ColumnType columnType, final Object labelOrDefaultLong, final String formatType, final String feature) {
			this.columnType = columnType;
			this.labelOrDefaultLong = labelOrDefaultLong;
			this.feature = feature;
			assert labelOrDefaultLong instanceof String || labelOrDefaultLong instanceof Number;
			this.formatType = formatType;

		}

		/**
		 * If not null, returns the license feature required to display this data
		 * 
		 * @return
		 */
		@Nullable
		public String getFeature() {
			return feature;
		}
	}

	@Nullable
	private RowData pinnedData = null;

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		private final HeadlineReportTransformer transformer = new HeadlineReportTransformer();

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others, final boolean block) {
			final Runnable r = () -> {
				pinnedData = null;
				RowData pPinnedData = null;
				final List<Object> rowElements = new LinkedList<>();
				if (pinned != null) {
					final ScheduleModel otherScheduleModel = pinned.getTypedResult(ScheduleModel.class);
					if (otherScheduleModel != null) {
						final Schedule schedule = otherScheduleModel.getSchedule();
						if (schedule != null) {
							pPinnedData = transformer.transform(schedule, pinned);
						}
					}
				}

				for (final ScenarioResult other : others) {
					if (other != null) {
						boolean showingOptiResult = false;
						if (other.getResultRoot() != null && other.getResultRoot().eContainer() instanceof SolutionOption) {
							showingOptiResult = true;
						}
						final ScheduleModel otherScheduleModel = other.getTypedResult(ScheduleModel.class);
						if (otherScheduleModel != null) {
							final Schedule schedule = otherScheduleModel.getSchedule();
							if (schedule != null) {
								if (pPinnedData != null) {
									rowElements.add(transformer.transform(schedule, other));
								} else {
									if ((HeadlineReportView.this.scheduleModel != null && schedule == HeadlineReportView.this.scheduleModel.getSchedule()) || (others.size() == 1 && pinned == null)) {
										rowElements.add(transformer.transform(schedule, other));
									}
								}
							}
						}
						if (showingOptiResult) {
							break;
						}
					}
				}

				if (rowElements.isEmpty()) {
					if (pPinnedData != null) {
						rowElements.add(pPinnedData);
						pPinnedData = null;
					} else {
						rowElements.add(new RowData());
					}
				}

				pinnedData = pPinnedData;
				ViewerHelper.setInput(viewer, true, rowElements);
			};
			RunnerHelper.exec(r, block);
		}
	};

	class ViewLabelProvider extends ColumnLabelProvider implements ILabelProvider, IFontProvider, IColorProvider {

		private final ColumnDefinition columnDefinition;

		public ViewLabelProvider(final ColumnDefinition columnDefinition) {
			this.columnDefinition = columnDefinition;

		}

		/**
		 * Get Mapping between a {@link ColumnDefinition} value type and a {@link RowData} field.
		 * 
		 * @param d
		 * @param columnDefinition
		 * @return
		 */
		Long getValue(final RowData d, final ColumnDefinition columnDefinition) {
			if (d == null) {
				return null;
			}
			switch (columnDefinition) {
			case VALUE_GCO_DAYS:
				return d.gcoTime;
			case VALUE_GCO_REVENUE:
				return d.gcoRevenue;
			case VALUE_LATENESS:
				return d.latenessExcludingFlex;
			case VALUE_IDLE_DAYS:
				return d.idleTime;
			case VALUE_CHARTER_LENGTH_DAYS:
				return d.charterLength;
			case VALUE_PNL:
				return d.totalPNL;
			case VALUE_SHIPPING:
				return d.shippingPNL;
			case VALUE_TRADING:
				return d.tradingPNL;
			case VALUE_PAPER:
				return d.paperPNL;
			case VALUE_UPSIDE:
				return d.upside;
			case VALUE_EQUITY:
				return d.upstreamDownstreamPNL;
			case VALUE_VIOLATIONS:
				return d.capacityViolationCount;
			case VALUE_PURCHASE_COST:
				return d.purchaseCost;
			case VALUE_SALES_REVENUE:
				return d.salesRevenue;
			default:
				break;

			}
			return null;
		}

		@Override
		public String getText(final Object obj) {
			if (obj instanceof RowData) {
				final RowData d = (RowData) obj;
				final RowData pinD = pinnedData;

				if (columnDefinition.columnType == ColumnType.LABEL) {
					return columnDefinition.getLabel();
				} else if (columnDefinition.getColumnType() == ColumnType.VALUE) {
					final Long dValue = getValue(d, columnDefinition);
					final Long pinValue = getValue(pinD, columnDefinition);
					final Long rtn = (dValue != null ? dValue - (pinD != null ? pinValue : 0) : null);

					String suffix = "";
					if (columnDefinition == ColumnDefinition.VALUE_LATENESS) {

						if (pinD == null) {
							if (d.latenessExcludingFlex != null && d.latenessIncludingFlex != null) {
								if (d.latenessExcludingFlex - d.latenessIncludingFlex != 0) {
									suffix = " *";
								}
							}
						} else {
							long a = 0;
							if (d.latenessExcludingFlex != null && d.latenessIncludingFlex != null) {
								a = d.latenessExcludingFlex - d.latenessIncludingFlex;
							}
							long b = 0;
							if (pinD.latenessExcludingFlex != null && pinD.latenessIncludingFlex != null) {
								b = pinD.latenessExcludingFlex - pinD.latenessIncludingFlex;
							}
							if (a != b) {
								suffix = " *";
							}
						}
					}

					return format(rtn, columnDefinition.getFormatType()) + suffix;
				}
			}
			return "";
		}

		@Override
		public Font getFont(final Object element) {
			return boldFont;
		}

		@Override
		public Color getForeground(final Object element) {

			if (element instanceof RowData) {
				final RowData d = (RowData) element;
				final RowData pinD = pinnedData;
				int color = SWT.COLOR_DARK_GRAY;
				if (columnDefinition.getColumnType() == ColumnType.LABEL) {
					return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
				}
				switch (columnDefinition) {
				case VALUE_PNL:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.totalPNL - pinD.totalPNL) >= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
					break;
				case VALUE_TRADING:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.tradingPNL - pinD.tradingPNL) >= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
					break;
				case VALUE_PAPER:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.paperPNL - pinD.paperPNL) >= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
					break;
				case VALUE_SHIPPING:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.shippingPNL - pinD.shippingPNL) >= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
					break;
				case VALUE_UPSIDE:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.upside - pinD.upside) <= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
					break;
				case VALUE_EQUITY:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.upstreamDownstreamPNL - pinD.upstreamDownstreamPNL) >= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
					break;
				case VALUE_IDLE_DAYS:
					color = SWT.COLOR_BLACK;
					break;
				case VALUE_CHARTER_LENGTH_DAYS:
					color = SWT.COLOR_BLACK;
					break;
				case VALUE_GCO_DAYS:
				case VALUE_GCO_REVENUE:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.gcoTime - pinD.gcoTime) >= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
					break;
				case VALUE_VIOLATIONS:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.capacityViolationCount - pinD.capacityViolationCount) > 0 ? SWT.COLOR_RED : SWT.COLOR_DARK_GREEN;
					}
					break;
				case VALUE_LATENESS:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.latenessExcludingFlex - pinD.latenessExcludingFlex) > 0 ? SWT.COLOR_RED : SWT.COLOR_DARK_GREEN;
					}
					break;
				case VALUE_PURCHASE_COST:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.purchaseCost - pinD.purchaseCost) <= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
					break;
				case VALUE_SALES_REVENUE:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.salesRevenue - pinD.salesRevenue) >= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
					break;
				default:
					break;
				}
				return Display.getCurrent().getSystemColor(color);
			}
			return null;
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		selectedScenariosService = getSite().getService(SelectedScenariosService.class);

		{
			final Font systemFont = Display.getDefault().getSystemFont();
			// Clone the font data
			final FontData fd = new FontData(systemFont.getFontData()[0].toString());
			// Set the bold bit.
			fd.setStyle(fd.getStyle() | SWT.BOLD);
			boldFont = new Font(Display.getDefault(), fd);
		}

		viewer = new GridTableViewer(parent, SWT.FULL_SELECTION | SWT.H_SCROLL);
		GridViewerHelper.configureLookAndFeel(viewer);

		viewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		viewer.setContentProvider(new ArrayContentProvider());

		viewer.setInput(getViewSite());

		// For some reason we've ended up with a small row height.
		// This appears to fix it, but no idea why the problem occurs in first place.
		// SG: 2013-04-17
		viewer.setAutoPreferredHeight(true);

		for (int i = 0; i < ColumnDefinition.values().length; ++i) {
			final ColumnDefinition def = ColumnDefinition.values()[i];

			@Nullable
			final String feature = def.getFeature();
			if (feature != null && !LicenseFeatures.isPermitted(feature)) {
				continue;
			}

			final GridViewerColumn tvc = new GridViewerColumn(viewer, SWT.NONE);
			tvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			int width;
			String string;
			if (def.columnType == ColumnType.LABEL) {
				string = def.getLabel();
			} else {
				string = format(def.getDefaultLong(), def.getFormatType());
			}
			width = getTextWidth(20, string);
			tvc.getColumn().setWidth(width);
			final ViewLabelProvider labelProvider = new ViewLabelProvider(def);
			tvc.setLabelProvider(labelProvider);

		}
		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(false);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_Headline");
		//
		partListener = new IPartListener() {
			@Override
			public void partOpened(final IWorkbenchPart part) {
				// Nothing to do
			}

			@Override
			public void partDeactivated(final IWorkbenchPart part) {
				// Nothing to do
			}

			@Override
			public void partClosed(final IWorkbenchPart part) {

				if (currentActiveEditor == part) {
					try {
						activeEditorChange(null);
					} catch (final Exception e) {
						// Ignore
					}
				}
				selectedScenariosService.triggerListener(selectedScenariosServiceListener, true);
			}

			@Override
			public void partBroughtToTop(final IWorkbenchPart part) {
				if (part instanceof IEditorPart) {
					// Active editor changed
					try {
						activeEditorChange((IEditorPart) part);
					} catch (final Exception e) {
						// Ignore
					}
				}
				selectedScenariosService.triggerListener(selectedScenariosServiceListener, true);
			}

			@Override
			public void partActivated(final IWorkbenchPart part) {
				if (part instanceof IEditorPart) {
					// Active editor changed
					try {
						activeEditorChange((IEditorPart) part);
					} catch (final Exception e) {
						// Ignore
					}
				}
				selectedScenariosService.triggerListener(selectedScenariosServiceListener, true);
			}

		};
		getSite().getPage().addPartListener(partListener);
		// Set initial active editor
		try {
			activeEditorChange(getSite().getPage().getActiveEditor());
		} catch (final Throwable t) {
			// Ignore these errors
		}

		selectedScenariosService.addListener(selectedScenariosServiceListener);
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
	}

	int getTextWidth(final int minWidth, final String string) {
		final GC gc = new GC(viewer.getControl());
		try {
			gc.setFont(boldFont);
			// 8 taken from sum margins in
			// org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer
			return Math.max(minWidth, 10 + gc.textExtent(string).x);
		} finally {
			gc.dispose();
		}
	}

	int getTextHeight(final int minHeight, final String string) {
		final GC gc = new GC(viewer.getControl());
		try {
			gc.setFont(boldFont);
			// 3 taken from sum margins in
			// org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer
			return Math.max(minHeight, 3 + gc.textExtent(string).y);
		} finally {
			gc.dispose();
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	@Override
	public void dispose() {
		if (boldFont != null) {
			boldFont.dispose();
			boldFont = null;
		}

		if (modelReference != null) {
			modelReference.close();
			modelReference = null;
		}
		selectedScenariosService.removeListener(selectedScenariosServiceListener);

		getSite().getPage().removePartListener(partListener);

		super.dispose();
	}

	private void activeEditorChange(final IEditorPart activeEditor) {
		if (this.modelReference != null) {
			this.modelReference.close();
			this.modelReference = null;
		}
		this.currentActiveEditor = activeEditor;
		ScheduleModel currentScheduleModel = null;
		if (activeEditor != null) {
			final IEditorInput editorInput = activeEditor.getEditorInput();
			if (editorInput instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput ssInput = (IScenarioServiceEditorInput) editorInput;
				final ScenarioInstance scenarioInstance = ssInput.getScenarioInstance();
				if (scenarioInstance != null) {
					@NonNull
					ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
					if (!modelRecord.isLoadFailure()) {
						this.modelReference = modelRecord.aquireReference("HeadlineReportView:1");
						final EObject instance = modelReference.getInstance();
						if (instance instanceof LNGScenarioModel) {
							final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
							currentScheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
						}
					}
				}
			}
		}
		this.scheduleModel = currentScheduleModel;
	}

	private String format(final Long value, final String type) {
		if (value == null)
			return "";
		if (KPIReportTransformer.TYPE_TIME.equals(type)) {
			final long days = value / 24;
			final long hours = value % 24;
			return "" + days + "d, " + hours + "h";
		} else if (KPIReportTransformer.TYPE_COST.equals(type)) {
			return String.format("$%,d", value);
		} else {
			return String.format("%,d", value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IProvideEditorInputScenario.class.isAssignableFrom(adapter)) {
			return (T) new IProvideEditorInputScenario() {
				@Override
				public void provideScenarioInstance(ScenarioResult scenarioResult) {

					if (HeadlineReportView.this.modelReference != null) {
						HeadlineReportView.this.modelReference.close();
						HeadlineReportView.this.modelReference = null;
					}
					HeadlineReportView.this.currentActiveEditor = null;
					ScheduleModel currentScheduleModel = null;

					if (scenarioResult != null) {
						final ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();

						if (!modelRecord.isLoadFailure()) {
							HeadlineReportView.this.modelReference = modelRecord.aquireReference("HeadlineReportView:2");
							final EObject instance = modelReference.getInstance();
							if (instance instanceof LNGScenarioModel) {
								final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
								currentScheduleModel = lngScenarioModel.getScheduleModel();
							}
						}
					}
					HeadlineReportView.this.scheduleModel = currentScheduleModel;

				}
			};
		}

		if (IReportContents.class.isAssignableFrom(adapter)) {

			final CopyGridToJSONUtil jsonUtil = new CopyGridToJSONUtil(viewer.getGrid(), true);
			final String jsonContents = jsonUtil.convert();
			return (T) new IReportContents() {
				@Override
				public String getJSONContents() {
					return jsonContents;
				}
			};
		}

		return super.getAdapter(adapter);
	}
}