/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

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

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.lingo.reports.views.IProvideEditorInputScenario;
import com.mmxlabs.lingo.reports.views.standard.HeadlineReportTransformer.RowData;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
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
		Label, Value
	}

	/**
	 * The ordered list of columns. These are either labels, or values. Label types take a String value and no formatter. Value types take a default Long value representing the largest expected value
	 * which is used to calculate the required column width.
	 */
	public enum ColumnDefinition {
		LABEL_PNL(ColumnType.Label, "P&L", null), VALUE_PNL(ColumnType.Value, 1000000000l, KPIReportTransformer.TYPE_COST), //
		LABEL_TRADING(ColumnType.Label, "Trading", null), VALUE_TRADING(ColumnType.Value, 1000000000l, KPIReportTransformer.TYPE_COST), //
		LABEL_SHIPPING(ColumnType.Label, "Shipping", null), VALUE_SHIPPING(ColumnType.Value, 1000000000l, KPIReportTransformer.TYPE_COST), //
		LABEL_EQUITY(ColumnType.Label, "Equity", null, "features:report-equity-book"), VALUE_EQUITY(ColumnType.Value, 1000000000l, KPIReportTransformer.TYPE_COST, "features:report-equity-book"), //
		LABEL_GCO(ColumnType.Label, "Charter Out (virt)", null, "features:optimisation-charter-out-generation"), VALUE_GCO_DAYS(ColumnType.Value, 2400l, KPIReportTransformer.TYPE_TIME,
				"features:optimisation-charter-out-generation"), VALUE_GCO_REVENUE(ColumnType.Value, 1000000000l, KPIReportTransformer.TYPE_COST, "features:optimisation-charter-out-generation"), //
		LABEL_VIOLATIONS(ColumnType.Label, "Violations", null), VALUE_VIOLATIONS(ColumnType.Value, 100l, ""), //
		LABEL_LATENESS(ColumnType.Label, "Late", null), VALUE_LATENESS(ColumnType.Value, 5200l, KPIReportTransformer.TYPE_TIME); //

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
			final Runnable r = new Runnable() {
				@Override
				public void run() {
					pinnedData = null;
					RowData pPinnedData = null;
					final List<Object> rowElements = new LinkedList<>();
					if (pinned != null) {
						final ScheduleModel other_scheduleModel = pinned.getTypedResult(ScheduleModel.class);
						if (other_scheduleModel != null) {
							final Schedule schedule = other_scheduleModel.getSchedule();
							if (schedule != null) {
								pPinnedData = transformer.transform(schedule, pinned);
							}
						}
					}

					for (final ScenarioResult other : others) {
						final ScheduleModel other_scheduleModel = other.getTypedResult(ScheduleModel.class);
						if (other_scheduleModel != null) {
							final Schedule schedule = other_scheduleModel.getSchedule();
							if (schedule != null) {
								if (pPinnedData != null) {
									rowElements.add(transformer.transform(schedule, other));
								} else {
									if (HeadlineReportView.this.scheduleModel != null && schedule == HeadlineReportView.this.scheduleModel.getSchedule()) {
										rowElements.add(transformer.transform(schedule, other));
									}
								}
							}
						}
					}

					if (rowElements.isEmpty()) {
						if (pPinnedData != null) {
							rowElements.add(pPinnedData);
							pPinnedData = null;
						} else {
							rowElements.add(new RowData("", null, null, null, null, null, null, null, null, null, null, null));
						}
					}

					pinnedData = pPinnedData;
					ViewerHelper.setInput(viewer, true, rowElements);
				}
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
			case VALUE_PNL:
				return d.totalPNL;
			case VALUE_SHIPPING:
				return d.shippingPNL;
			case VALUE_TRADING:
				return d.tradingPNL;
			case VALUE_EQUITY:
				return d.upstreamDownstreamPNL;
			case VALUE_VIOLATIONS:
				return d.capacityViolationCount;
			default:
				break;

			}
			return null;
		}

		@Override
		public String getText(final Object obj) {
			if (obj instanceof RowData) {
				final RowData d = (RowData) obj;
				if (d.dummy) {
					return "";
				}
				final RowData pinD = pinnedData;

				if (columnDefinition.columnType == ColumnType.Label) {
					return columnDefinition.getLabel();
				} else if (columnDefinition.getColumnType() == ColumnType.Value) {
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
				if (d.dummy) {
					return null;
				}
				final RowData pinD = pinnedData;
				int color = SWT.COLOR_DARK_GRAY;
				if (columnDefinition.getColumnType() == ColumnType.Label) {
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
				case VALUE_SHIPPING:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.shippingPNL - pinD.shippingPNL) >= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
					break;
				case VALUE_EQUITY:
					if (pinD == null) {
						color = SWT.COLOR_BLACK;
					} else {
						color = (d.upstreamDownstreamPNL - pinD.upstreamDownstreamPNL) >= 0 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_RED;
					}
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
				default:
					break;
				}
				return Display.getCurrent().getSystemColor(color);
			}
			return null;
		}
	}

	/**
	 * The constructor.
	 */
	public HeadlineReportView() {

	}

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		selectedScenariosService = (SelectedScenariosService) getSite().getService(SelectedScenariosService.class);

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
			if (def.columnType == ColumnType.Label) {
				string = def.getLabel();
			} else {
				string = format(def.getDefaultLong(), def.getFormatType());
			}
			width = getTextWidth(20, string);
			tvc.getColumn().setWidth(width);
			final ViewLabelProvider labelProvider = new ViewLabelProvider(def);
			tvc.setLabelProvider(labelProvider);

		}
		// viewer.setLabelProvider(labelProvider);

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(false);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_Headline");
		//
		partListener = new IPartListener() {
			@Override
			public void partOpened(final IWorkbenchPart part) {

			}

			@Override
			public void partDeactivated(final IWorkbenchPart part) {
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
			// 8 taken from sum margins in org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer
			return Math.max(minWidth, 10 + gc.textExtent(string).x);
		} finally {
			gc.dispose();
		}
	}

	int getTextHeight(final int minHeight, final String string) {
		final GC gc = new GC(viewer.getControl());
		try {
			gc.setFont(boldFont);
			// 3 taken from sum margins in org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer
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
		ScheduleModel scheduleModel = null;
		if (activeEditor != null) {
			final IEditorInput editorInput = activeEditor.getEditorInput();
			if (editorInput instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput ssInput = (IScenarioServiceEditorInput) editorInput;
				final ScenarioInstance scenarioInstance = ssInput.getScenarioInstance();
				if (scenarioInstance != null) {
					if (!scenarioInstance.isLoadFailure()) {
						this.modelReference = scenarioInstance.getReference("HeadlineReportView:1");
						final EObject instance = modelReference.getInstance();
						if (instance instanceof LNGScenarioModel) {
							final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
							scheduleModel = lngScenarioModel.getScheduleModel();
						}
					}
				}
			}
		}
		// this.activeEditor = activeEditor;
		this.scheduleModel = scheduleModel;
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
					ScheduleModel scheduleModel = null;

					if (scenarioResult != null) {
						ScenarioInstance scenarioInstance = scenarioResult.getScenarioInstance();
						if (!scenarioInstance.isLoadFailure()) {
							HeadlineReportView.this.modelReference = scenarioInstance.getReference("HeadlineReportView:2");
							final EObject instance = modelReference.getInstance();
							if (instance instanceof LNGScenarioModel) {
								final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
								scheduleModel = lngScenarioModel.getScheduleModel();
							}
						}
					}
					// this.activeEditor = activeEditor;
					HeadlineReportView.this.scheduleModel = scheduleModel;

				}
			};
		}

		if (IReportContents.class.isAssignableFrom(adapter))

		{

			final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);
			final String contents = util.convert();
			return (T) new IReportContents() {

				@Override
				public String getStringContents() {
					return contents;
				}
			};
		}

		return super.getAdapter(adapter);
	}
}