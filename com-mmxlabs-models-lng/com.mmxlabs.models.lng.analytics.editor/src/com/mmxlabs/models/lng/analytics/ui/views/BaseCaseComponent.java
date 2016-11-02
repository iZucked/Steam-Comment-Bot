package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.providers.BaseCaseContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.impl.NumberInlineEditor;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;

public class BaseCaseComponent extends AbstractSandboxComponent {
	private boolean baseCaseValid = true;

	private GridTreeViewer baseCaseViewer;
	private Label baseCaseProftLabel;
	private Label baseCaseCalculator;

	private Control inputPNL;
	private NumberInlineEditor numberInlineEditor;
	private BaseCaseWiringDiagram baseCaseDiagram;
	private Image image_calculate;
	private Image image_grey_calculate;

	protected BaseCaseComponent(@NonNull IScenarioEditingLocation scenarioEditingLocation, Map<Object, IStatus> validationErrors, @NonNull Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);

		final ImageDescriptor calc_desc = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "icons/sandbox_calc.gif");
		image_calculate = calc_desc.createImage();
		image_grey_calculate = ImageDescriptor.createWithFlags(calc_desc, SWT.IMAGE_GRAY).createImage();
	}

	@Override
	public void createControls(Composite parent, boolean expanded, IExpansionListener expansionListener, OptionModellerView optionModellerView) {
		ExpandableComposite expandable = wrapInExpandable(parent, "Target", p -> createBaseCaseViewer(p), expandableCompo -> {

			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final BaseCaseDropTargetListener listener = new BaseCaseDropTargetListener(scenarioEditingLocation, baseCaseViewer);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
			// Control control = getControl();
			final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_MOVE);
			dropTarget.setTransfer(types);
			dropTarget.addDropListener(listener);
		});

		final Composite c = new Composite(parent, SWT.NONE);
		GridDataFactory.generate(c, 1, 1);
		c.setLayout(new GridLayout(5, false));
		baseCaseProftLabel = new Label(c, SWT.NONE);
		GridDataFactory.generate(baseCaseProftLabel, 1, 1);
		baseCaseProftLabel.setText("Base P&&L: $");
		inputPNL = createInputTargetPNL(c);
		inputPNL.setLayoutData(new GridData(100, SWT.DEFAULT));
		inputWants.add(m -> inputPNL.setEnabled(m != null));
		inputWants.add(m -> inputPNL.redraw());

		baseCaseCalculator = new Label(c, SWT.NONE);
		// baseCaseCalculator.setText("Calc."); --cogs
		baseCaseCalculator.setImage(image_grey_calculate);
		GridDataFactory.generate(baseCaseCalculator, 1, 1);
		baseCaseCalculator.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(final MouseEvent e) {
			}

			@Override
			public void mouseExit(final MouseEvent e) {
				baseCaseCalculator.setImage(image_grey_calculate);
			}

			@Override
			public void mouseEnter(final MouseEvent e) {
				baseCaseCalculator.setImage(image_calculate);
			}
		});

		baseCaseCalculator.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(final MouseEvent e) {
				OptionAnalysisModel m = modelProvider.get();
				if (baseCaseValid && m != null) {
					BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> BaseCaseEvaluator.evaluate(scenarioEditingLocation, m, m.getBaseCase(), true, "Base Case"));
				}
			}

		});
		/*
		 * toggle for target pnl
		 */
		final Composite targetPNLToggle = createUseTargetPNLToggleComposite(c);
		GridDataFactory.generate(targetPNLToggle, 1, 1);

		hookOpenEditor(baseCaseViewer);

		final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		final BaseCaseDropTargetListener listener = new BaseCaseDropTargetListener(scenarioEditingLocation, baseCaseViewer);
		inputWants.add(model -> listener.setOptionAnalysisModel(model));
		baseCaseViewer.addDropSupport(DND.DROP_MOVE, types, listener);
		expandable.setExpanded(expanded);

		expandable.addExpansionListener(expansionListener);
	}

	private Control createBaseCaseViewer(final Composite parent) {
		baseCaseViewer = new GridTreeViewer(parent, SWT.NONE | SWT.SINGLE);
		ColumnViewerToolTipSupport.enableFor(baseCaseViewer);

		GridViewerHelper.configureLookAndFeel(baseCaseViewer);
		baseCaseViewer.getGrid().setHeaderVisible(true);
		baseCaseViewer.getGrid().setRowHeaderVisible(true);

		createColumn(baseCaseViewer, "Buy", new BuyOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
		{
			final GridViewerColumn gvc = new GridViewerColumn(baseCaseViewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(new CellLabelProvider() {

				@Override
				public void update(final ViewerCell cell) {
					// TODO Auto-generated method stub

				}
			});
			this.baseCaseDiagram = new BaseCaseWiringDiagram(baseCaseViewer.getGrid(), gvc);
			// gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		createColumn(baseCaseViewer, "Sell", new SellOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
		createColumn(baseCaseViewer, "Shipping", new ShippingOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);

		baseCaseViewer.getGrid().setCellSelectionEnabled(true);

		baseCaseViewer.setContentProvider(new BaseCaseContentProvider());

		final MenuManager mgr = new MenuManager();

		final BaseCaseContextMenuManager listener = new BaseCaseContextMenuManager(baseCaseViewer, scenarioEditingLocation, mgr);
		inputWants.add(model -> listener.setOptionAnalysisModel(model));
		baseCaseViewer.getGrid().addMenuDetectListener(listener);

		inputWants.add(model -> baseCaseViewer.setInput(model));
		inputWants.add(model -> baseCaseDiagram.setRoot(model));

		return baseCaseViewer.getGrid();
	}

	@Override
	public void refresh() {
		baseCaseViewer.refresh();
	}

	private Control createInputTargetPNL(final Composite composite) {
		numberInlineEditor = new NumberInlineEditor(AnalyticsPackage.Literals.BASE_CASE__PROFIT_AND_LOSS);
		numberInlineEditor.setCommandHandler(scenarioEditingLocation.getDefaultCommandHandler());
		final Control control = numberInlineEditor.createControl(composite, null, new FormToolkit(Display.getDefault()));

		inputWants.add(model -> {
			numberInlineEditor.display(dialogContext, scenarioEditingLocation.getRootObject(), model != null ? model.getBaseCase() : null,
					model != null ? Lists.newArrayList(model.getBaseCase()) : Lists.newArrayList());

		});

		return control;
	}

	private Composite createUseTargetPNLToggleComposite(final Composite composite) {
		final Composite matching = new Composite(composite, SWT.ALL);
		final GridLayout gridLayoutRadiosMatching = new GridLayout(3, false);
		matching.setLayout(gridLayoutRadiosMatching);
		final GridData gdM = new GridData(SWT.LEFT, SWT.BEGINNING, false, false);
		gdM.horizontalSpan = 2;
		matching.setLayoutData(gdM);
		new Label(matching, SWT.NONE).setText("B/E with target P&&L");
		final Button matchingButton = new Button(matching, SWT.CHECK | SWT.LEFT);
		matchingButton.setSelection(false);
		matchingButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				OptionAnalysisModel m = modelProvider.get();
				if (m != null) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
							SetCommand.create(scenarioEditingLocation.getEditingDomain(), m, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL, matchingButton.getSelection()), m,
							AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL);
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});

		inputWants.add(m -> matching.setEnabled(m != null));

		// final Button matchingYesButton = new Button(matching, SWT.RADIO | SWT.LEFT);
		// matchingYesButton.setText("Yes");
		// matchingYesButton.setSelection(false);
		// matchingYesButton.addSelectionListener(new SelectionListener() {
		//
		// @Override
		// public void widgetSelected(final SelectionEvent e) {
		// getDefaultCommandHandler().handleCommand(SetCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL, Boolean.TRUE), model,
		// AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL);
		// }
		//
		// @Override
		// public void widgetDefaultSelected(final SelectionEvent e) {
		// }
		// });

		// FIXME: This control does not respond to e.g. Undo() calls.
		// Need to hook up explicitly to the refresh adapter

		inputWants.add(model -> {
			if (model != null) {
				matchingButton.setSelection(model.isUseTargetPNL());
			}
		});
		return matching;
	}

	@Override
	public void dispose() {
		if (image_calculate != null) {
			image_calculate.dispose();
		}
		if (image_grey_calculate != null) {
			image_grey_calculate.dispose();
		}

		super.dispose();
	}

	public void setBaseCaseValid(boolean valid) {
		baseCaseValid = valid;
		if (!baseCaseCalculator.isDisposed()) {
			if (baseCaseValid) {
				baseCaseCalculator.setBackground(null);
			} else {
				baseCaseCalculator.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			}
		}
	}
}
