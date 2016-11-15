package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.OptionsViewerContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;

public class SellOptionsComponent extends AbstractSandboxComponent {

	private GridTreeViewer sellOptionsViewer;
	private MenuManager mgr;

	public SellOptionsComponent(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors, @NonNull Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	public void createControls(Composite parent, boolean expanded, IExpansionListener expansionListener, OptionModellerView optionModellerView) {
		ExpandableComposite expandable = wrapInExpandable(parent, "Sells", p -> createSellOptionsViewer(p), expandableComposite -> {

			{
				final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
				final SellsDropTargetListener listener = new SellsDropTargetListener(scenarioEditingLocation, sellOptionsViewer);
				inputWants.add(model -> listener.setOptionAnalysisModel(model));
				// Control control = getControl();
				final DropTarget dropTarget = new DropTarget(expandableComposite, DND.DROP_MOVE);
				dropTarget.setTransfer(types);
				dropTarget.addDropListener(listener);
				// expandableComposite.addDropSupport(DND.DROP_MOVE, types, listener);
			}

			final Label addSellButton = new Label(expandableComposite, SWT.NONE);
			addSellButton.setImage(image_grey_add);
			expandableComposite.setTextClient(addSellButton);
			addSellButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).grab(true, false).create());
			addSellButton.addMouseTrackListener(new MouseTrackListener() {

				@Override
				public void mouseHover(final MouseEvent e) {

				}

				@Override
				public void mouseExit(final MouseEvent e) {
					addSellButton.setImage(image_grey_add);
				}

				@Override
				public void mouseEnter(final MouseEvent e) {
					addSellButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
				}
			});
			addSellButton.addMouseListener(OptionMenuHelper.createNewSellOptionMenuListener(addSellButton.getParent(), scenarioEditingLocation, modelProvider));

		});
		sellOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 400).create());

		hookDragSource(sellOptionsViewer);
		expandable.setExpanded(expanded);

		expandable.addExpansionListener(expansionListener);
	}

	private Control createSellOptionsViewer(final Composite sellComposite) {

		sellOptionsViewer = new GridTreeViewer(sellComposite, SWT.NONE | SWT.MULTI);
		ColumnViewerToolTipSupport.enableFor(sellOptionsViewer);

		GridViewerHelper.configureLookAndFeel(sellOptionsViewer);
		sellOptionsViewer.getGrid().setHeaderVisible(false);

		CellFormatterLabelProvider labelProvider = new BuysSellsLabelProvider(new SellOptionDescriptionFormatter(), validationErrors, "Sell");
		createColumn(sellOptionsViewer, labelProvider, "Sell", new SellOptionDescriptionFormatter(), false);

		sellOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS));
		hookOpenEditor(sellOptionsViewer);
		{
			mgr = new MenuManager();
			final SellOptionsContextMenuManager listener = new SellOptionsContextMenuManager(sellOptionsViewer, scenarioEditingLocation, mgr);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
			sellOptionsViewer.getGrid().addMenuDetectListener(listener);
		}
		{
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final SellsDropTargetListener listener = new SellsDropTargetListener(scenarioEditingLocation, sellOptionsViewer);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
			sellOptionsViewer.addDropSupport(DND.DROP_MOVE, types, listener);
		}
		inputWants.add(model -> sellOptionsViewer.setInput(model));
		return sellOptionsViewer.getControl();
	}

	@Override
	public void refresh() {
		sellOptionsViewer.refresh();
	}

	@Override
	public List<Consumer<OptionAnalysisModel>> getInputWants() {
		return inputWants;
	}

	@Override
	public void dispose() {
		mgr.dispose();
		super.dispose();
	}
}
