package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.WhatIfEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.providers.PartialCaseContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;

public class PartialCaseCompoment extends AbstractSandboxComponent {

	private Label generateButton;
	private GridTreeViewer partialCaseViewer;
	private PartialCaseWiringDiagram partialCaseDiagram;

	private Image image_generate;
	private Image image_grey_generate;
	private boolean partialCaseValid = true;

	protected PartialCaseCompoment(@NonNull IScenarioEditingLocation scenarioEditingLocation, Map<Object, IStatus> validationErrors, @NonNull Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
		final ImageDescriptor generate_desc = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "icons/sandbox_generate.gif");
		image_generate = generate_desc.createImage();
		image_grey_generate = ImageDescriptor.createWithFlags(generate_desc, SWT.IMAGE_GRAY).createImage();

	}

	@Override
	public void createControls(Composite parent, boolean expanded, IExpansionListener expansionListener, OptionModellerView optionModellerView) {
		{
			ExpandableComposite expandable = wrapInExpandable(parent, "Options", p -> createPartialCaseViewer(p), expandableCompo -> {

				final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
				final PartialCaseDropTargetListener listener = new PartialCaseDropTargetListener(scenarioEditingLocation, partialCaseViewer);
				inputWants.add(model -> listener.setOptionAnalysisModel(model));
				// Control control = getControl();
				final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_MOVE | DND.DROP_LINK);
				dropTarget.setTransfer(types);
				dropTarget.addDropListener(listener);
			}, false);
			expandable.setLayoutData(GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, 300).grab(false, true).create());

			hookOpenEditor(partialCaseViewer);

			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final PartialCaseDropTargetListener listener = new PartialCaseDropTargetListener(scenarioEditingLocation, partialCaseViewer);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
			partialCaseViewer.addDropSupport(DND.DROP_MOVE | DND.DROP_LINK, types, listener);
			expandable.setExpanded(expanded);

			expandable.addExpansionListener(expansionListener);
		}

		{
			final Composite generateComposite = new Composite(parent, SWT.NONE);
			GridDataFactory.generate(generateComposite, 2, 1);

			generateComposite.setLayout(new GridLayout(1, true));

			generateButton = new Label(generateComposite, SWT.NONE);
			generateButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).create());
			generateButton.setImage(image_grey_generate);
			generateButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDown(final MouseEvent e) {
					OptionAnalysisModel m = modelProvider.get();
					if (partialCaseValid && m != null) {
						BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> WhatIfEvaluator.evaluate(scenarioEditingLocation, m));
					}
				}

				@Override
				public void mouseDoubleClick(final MouseEvent e) {

				}

				@Override
				public void mouseUp(final MouseEvent e) {

				}
			});
			generateButton.addMouseTrackListener(new MouseTrackListener() {

				@Override
				public void mouseHover(final MouseEvent e) {
				}

				@Override
				public void mouseExit(final MouseEvent e) {
					generateButton.setImage(image_grey_generate);
				}

				@Override
				public void mouseEnter(final MouseEvent e) {
					generateButton.setImage(image_generate);
				}
			});
		}
	}

	private Control createPartialCaseViewer(final Composite parent) {
		partialCaseViewer = new GridTreeViewer(parent, SWT.NONE | SWT.WRAP);
		ColumnViewerToolTipSupport.enableFor(partialCaseViewer);

		GridViewerHelper.configureLookAndFeel(partialCaseViewer);
		partialCaseViewer.getGrid().setHeaderVisible(true);
		partialCaseViewer.getGrid().setCellSelectionEnabled(true);

		partialCaseViewer.getGrid().setAutoHeight(true);
		partialCaseViewer.getGrid().setRowHeaderVisible(true);

		createColumn(partialCaseViewer, "Buy", new BuyOptionDescriptionFormatter(), false).getColumn().setWordWrap(true);

		{
			final GridViewerColumn gvc = new GridViewerColumn(partialCaseViewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createWiringColumnLabelProvider());
			this.partialCaseDiagram = new PartialCaseWiringDiagram(partialCaseViewer.getGrid(), gvc);
			// gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		createColumn(partialCaseViewer, "Sell", new SellOptionDescriptionFormatter(), false).getColumn().setWordWrap(true);
		createColumn(partialCaseViewer, "Shipping", new ShippingOptionDescriptionFormatter(), false).getColumn().setWordWrap(true);

		partialCaseViewer.setContentProvider(new PartialCaseContentProvider());

		final MenuManager mgr = new MenuManager();

		final PartialCaseContextMenuManager listener = new PartialCaseContextMenuManager(partialCaseViewer, scenarioEditingLocation, mgr);
		partialCaseViewer.getGrid().addMenuDetectListener(listener);
		inputWants.add(model -> listener.setOptionAnalysisModel(model));

		inputWants.add(model -> partialCaseViewer.setInput(model));
		inputWants.add(model -> partialCaseDiagram.setRoot(model));

		hookDragSource(partialCaseViewer);
		return partialCaseViewer.getGrid();
	}

	@Override
	public void refresh() {
		partialCaseViewer.refresh();
		GridViewerHelper.recalculateRowHeights(partialCaseViewer.getGrid());
	}

	@Override
	public void dispose() {
		if (image_generate != null) {
			image_generate.dispose();
		}

		if (image_grey_generate != null) {
			image_grey_generate.dispose();
		}
		super.dispose();
	}

	public void setPartialCaseValid(boolean valid) {
		partialCaseValid = valid;
		RunnerHelper.asyncExec(() -> {
			if (!generateButton.isDisposed()) {

				if (partialCaseValid) {
					generateButton.setBackground(null);
				} else {
					generateButton.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				}
			}
		});
	}
}
