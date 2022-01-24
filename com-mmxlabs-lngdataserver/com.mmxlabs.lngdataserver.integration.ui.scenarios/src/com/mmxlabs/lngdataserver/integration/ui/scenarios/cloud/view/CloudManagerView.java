/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.view;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationConstants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationDataResultRecord;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationDataService;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationDataService.IUpdateListener;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.ResultStatus;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.analytics.ui.views.SandboxScenario;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.PieChartRenderer;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class CloudManagerView extends ViewPart {

	//
//	/**
//	 * The ID of the view as specified by the extension.
//	 */
//	public static final String ID = "com.mmxlabs.jobcontroller.views.JobManager";
//
	private GridTableViewer viewer;
	private Action packAction;
//	private Action stopAction;
	private Action removeAction;

	private IUpdateListener listener;

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new GridTableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		GridViewerHelper.configureLookAndFeel(viewer);

		{
			final GridViewerColumn tvc0 = new GridViewerColumn(viewer, SWT.None);
			tvc0.getColumn().setText("Name");
			tvc0.getColumn().setWidth(200);
			tvc0.setLabelProvider(createLP(CloudOptimisationDataResultRecord::getOriginalName));
			GridViewerHelper.configureLookAndFeel(tvc0);
		}
		{
			final GridViewerColumn tvc3 = new GridViewerColumn(viewer, SWT.None);
			tvc3.getColumn().setText("Status");
			tvc3.getColumn().setWidth(60);
			tvc3.setLabelProvider(createLP(rec -> {
				final ResultStatus status = rec.getStatus();
				if (status != null) {
					if (status.isRunning()) {
						return String.format("%.0f%%", rec.getStatus().getProgress());
					}
					if (status.isFailed()) {
						return status.getReason();
					}
					String msg = status.getStatus();
					// Mapping to a more user friendly string
					if ("complete".equals(msg)) {
						return "Done";
					}
					return msg;
				}
				return null;
			}, rec -> {
				final ResultStatus status = rec.getStatus();
				if (status != null) {
					if (status.isSubmitted()) {
						return PieChartRenderer.renderPie(0.0);
					}
					if (status.isRunning()) {
						return PieChartRenderer.renderPie(rec.getStatus().getProgress() / 100.0);
					}
				}
				return null;
			}));
			GridViewerHelper.configureLookAndFeel(tvc3);
		}
		{
			final GridViewerColumn tvc1 = new GridViewerColumn(viewer, SWT.None);
			tvc1.getColumn().setText("Started");
			tvc1.getColumn().setWidth(60);
			tvc1.setLabelProvider(createLP(rec -> {
				if (LocalDate.now().equals(LocalDate.ofInstant(rec.getCreationDate(), ZoneId.systemDefault()))) {
					return rec.getCreationDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
				}

				return rec.getCreationDate().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd MMM"));
			}));
			GridViewerHelper.configureLookAndFeel(tvc1);
		}
//		{
//			final GridViewerColumn tvc0 = new GridViewerColumn(viewer, SWT.None);
//			tvc0.getColumn().setText("Job ID");
//			tvc0.getColumn().setWidth(200);
//			tvc0.setLabelProvider(createLP(CloudOptimisationDataResultRecord::getJobid));
//			GridViewerHelper.configureLookAndFeel(tvc0);
//		}
		{
			final GridViewerColumn tvc0 = new GridViewerColumn(viewer, SWT.None);
			tvc0.getColumn().setText("Type");
			tvc0.getColumn().setWidth(200);
			tvc0.setLabelProvider(createLP(r -> {
				String lbl = r.getType();
				switch (r.getType()) {
				case "OPTIMISATION" -> lbl = "Optimisation";
				case "OPTIONISER" -> lbl = "Optioniser";
				case "SANDBOX" -> lbl = "Sandbox";
				}
				if (r.getSubType() != null) {
					lbl = lbl + ": " + r.getSubType();
				}
				return lbl;
			}));
			GridViewerHelper.configureLookAndFeel(tvc0);
		}

		if (CloudOptimisationConstants.RUN_LOCAL_BENCHMARK) {
			{
				final GridViewerColumn tvc3 = new GridViewerColumn(viewer, SWT.None);
				tvc3.getColumn().setText("Local Runtime");
				tvc3.getColumn().setHeaderTooltip("Runtime in seconds");
				tvc3.getColumn().setWidth(60);
				tvc3.setLabelProvider(createLP(rec -> {
					final ResultStatus status = rec.getStatus();
					if (status != null) {
						return String.format("%,d", rec.getLocalRuntime() / 1000);
					}
					return null;
				}));
				GridViewerHelper.configureLookAndFeel(tvc3);
			}
			{
				final GridViewerColumn tvc3 = new GridViewerColumn(viewer, SWT.None);
				tvc3.getColumn().setText("Cloud Runtime");
				tvc3.getColumn().setHeaderTooltip("Runtime in seconds from time submitted to reported complete");
				tvc3.getColumn().setWidth(60);
				tvc3.setLabelProvider(createLP(rec -> {
					final ResultStatus status = rec.getStatus();
					if (status != null) {
						return String.format("%,d", rec.getCloudRuntime() / 1000);
					}
					return null;
				}));
				GridViewerHelper.configureLookAndFeel(tvc3);
			}
		}

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());

		listener = (cRecord) -> {
			if (cRecord != null) {
				ViewerHelper.refresh(viewer, cRecord, false);
			} else {
				final List<CloudOptimisationDataResultRecord> content = new LinkedList<>(CloudOptimisationDataService.INSTANCE.getRecords());
				// Sort newest first
				Collections.sort(content, (a, b) -> b.getCreationDate().compareTo(a.getCreationDate()));
				ViewerHelper.setInputThen(viewer, false, content, () -> updateActionEnablement(viewer.getSelection()));
			}
		};
		CloudOptimisationDataService.INSTANCE.addListener(listener);
// 

//		viewer.setLabelProvider(new ITableLabelProviderImplementation());
		// Create the help context id for the viewer's control
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.jobmanager.viewer");

		makeActions();
		hookContextMenu();
		contributeToActionBars();
//
		viewer.addSelectionChangedListener(event -> {

			final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

			updateActionEnablement(selection);
		});

		listener.updated(null);

		viewer.addOpenListener(event -> {
			if (event.getSelection() instanceof IStructuredSelection sel) {
				if (sel.size() == 1) {
					CloudOptimisationDataResultRecord cRecord = (CloudOptimisationDataResultRecord) sel.getFirstElement();

					// Look up the original scenario.
					final ScenarioInstance instanceRef = cRecord.getScenarioInstance();

					if (instanceRef != null) {

						final ScenarioModelRecord mr = SSDataManager.Instance.getModelRecord(instanceRef);
						try (IScenarioDataProvider sdp = mr.aquireScenarioDataProvider("DownloadTask:patchScenario")) {
							final AnalyticsModel am = ScenarioModelUtil.getAnalyticsModel(sdp);
							if (cRecord.getComponentUUID() != null) {
								for (OptionAnalysisModel sandbox : am.getOptionModels()) {
									if (Objects.equals(cRecord.getComponentUUID(), sandbox.getUuid())) {
										new SandboxScenario(instanceRef, sandbox).openAndShowResult();
										break;
									}
								}
							} else {
								for (AbstractSolutionSet solution : am.getOptimisations()) {
									if (Objects.equals(cRecord.getResultUUID(), solution.getUuid())) {
										new AnalyticsSolution(instanceRef, solution, solution.getName()).open();
										break;
									}
								}
							}

						}

					}
				}
			}
		});
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				CloudManagerView.this.fillContextMenu(manager);
			}
		});
		final Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

//
	private void fillLocalPullDown(final IMenuManager manager) {
//		manager.add(stopAction);
//		manager.add(removeAction);
	}

//
	private void fillContextMenu(final IMenuManager manager) {
//		manager.add(stopAction);
//		manager.add(removeAction);
		manager.add(new RunnableAction("Copy optimisation ID to clipboard", () -> {

			if (viewer.getSelection() instanceof IStructuredSelection ss) {
				StringBuilder sb = new StringBuilder();
				for (var obj : ss) {
					if (obj instanceof CloudOptimisationDataResultRecord r) {
						sb.append(r.getJobid());
						if (ss.size() > 1) {
							sb.append("\n");
						}
					}
				}

				// Create a new clipboard instance
				final Display display = Display.getDefault();
				final Clipboard cb = new Clipboard(display);
				try {
					// Create the text transfer and set the contents
					final TextTransfer textTransfer = TextTransfer.getInstance();
					cb.setContents(new Object[] { sb.toString() }, new Transfer[] { textTransfer });
				} finally {
					// Clean up our local resources - system clipboard now has the data
					cb.dispose();
				}
			}
		}));
//
//		// Other plug-ins can contribute there actions here
//		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(packAction);
//		manager.add(stopAction);
		manager.add(removeAction);
	}

	private void makeActions() {

		packAction = PackActionFactory.createPackColumnsAction(viewer);

//		stopAction = new Action() {
//			@Override
//			public void run() {
//
//			}
//		};
//		stopAction.setText("Abort");
//		stopAction.setToolTipText("Abort Job");
//		CommonImages.setImageDescriptors(stopAction, IconPaths.Terminate);

		removeAction = new Action() {
			@Override
			public void run() {
				final ISelection selection = viewer.getSelection();
				if (selection instanceof final IStructuredSelection ss) {
					final List<String> jobs = new LinkedList<>();
					for (final Object obj : ss) {
						if (obj instanceof final CloudOptimisationDataResultRecord cRecord) {
							jobs.add(cRecord.getJobid());
						}
					}
					if (!jobs.isEmpty()) {
						CloudOptimisationDataService.INSTANCE.delete(jobs);
					}
				}

			}
		};
		removeAction.setText("Remove");
		removeAction.setToolTipText("Abort and remove Job from view");
		CommonImages.setImageDescriptors(removeAction, IconPaths.Delete);
	}

//	/**
//	 * Passing the focus request to the viewer's control.
//	 */
	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	private void updateActionEnablement(final ISelection selection) {
//		stopAction.setEnabled(false);
		removeAction.setEnabled(false);
		if (!selection.isEmpty()) {
			if (selection instanceof final IStructuredSelection ss) {
				final boolean stopEnabled = false;
				for (final Object obj : ss) {
					if (obj instanceof final CloudOptimisationDataResultRecord cRecord) {
						if (cRecord.getStatus() != null && cRecord.getStatus().isRunning()) {
							// No upstream API yet
//								stopEnabled = true;
						}
					}
				}
//				stopAction.setEnabled(stopEnabled);
				removeAction.setEnabled(!ss.isEmpty());
			}

		}
	}

	@Override
	public void dispose() {

		CloudOptimisationDataService.INSTANCE.removeListener(listener);

		super.dispose();
	}

	private CellLabelProvider createLP(final Function<CloudOptimisationDataResultRecord, String> func) {
		return new CellLabelProvider() {
			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				if (element instanceof final CloudOptimisationDataResultRecord rec) {
					cell.setText(func.apply(rec));
				} else {
					cell.setText(null);
				}

			}
		};
	}

	private CellLabelProvider createLP(final Function<CloudOptimisationDataResultRecord, String> func, final Function<CloudOptimisationDataResultRecord, Image> imgFunc) {
		return new CellLabelProvider() {
			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				if (element instanceof final CloudOptimisationDataResultRecord rec) {
					cell.setText(func.apply(rec));
					cell.setImage(imgFunc.apply(rec));
				} else {
					cell.setText(null);
					cell.setImage(null);
				}

			}
		};
	}

}