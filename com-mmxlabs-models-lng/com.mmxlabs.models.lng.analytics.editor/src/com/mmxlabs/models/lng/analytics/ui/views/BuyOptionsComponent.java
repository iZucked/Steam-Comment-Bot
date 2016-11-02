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
import com.mmxlabs.models.lng.analytics.ui.views.providers.OptionsViewerContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;

public class BuyOptionsComponent extends AbstractSandboxComponent {

	private GridTreeViewer buyOptionsViewer;
	private MenuManager mgr;

	public BuyOptionsComponent(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors, @NonNull Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	public void createControls(Composite parent, boolean expanded, IExpansionListener expansionListener, OptionModellerView optionModellerView) {
		ExpandableComposite expandable = wrapInExpandable(parent, "Buys", p -> createBuyOptionsViewer(p), expandableComposite -> {

			{
				final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
				final BuysDropTargetListener listener = new BuysDropTargetListener(scenarioEditingLocation, buyOptionsViewer);
				inputWants.add(model -> listener.setOptionAnalysisModel(model));
				// Control control = getControl();
				final DropTarget dropTarget = new DropTarget(expandableComposite, DND.DROP_MOVE);
				dropTarget.setTransfer(types);
				dropTarget.addDropListener(listener);
			}

			final Label c = new Label(expandableComposite, SWT.NONE);
			expandableComposite.setTextClient(c);
			c.setImage(image_grey_add);
			c.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).hint(16, 16).grab(true, false).create());
			c.addMouseTrackListener(new MouseTrackListener() {

				@Override
				public void mouseHover(final MouseEvent e) {

				}

				@Override
				public void mouseExit(final MouseEvent e) {
					c.setImage(image_grey_add);
				}

				@Override
				public void mouseEnter(final MouseEvent e) {
					c.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
				}
			});
			c.addMouseListener(OptionMenuHelper.createNewBuyOptionMenuListener(c.getParent(), scenarioEditingLocation, modelProvider));
		});

		expandable.setExpanded(expanded);

		expandable.addExpansionListener(expansionListener);

		buyOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(false, true).hint(SWT.DEFAULT, 400).create());
		hookDragSource(buyOptionsViewer);
	}

	private Control createBuyOptionsViewer(final Composite buyComposite) {

		buyOptionsViewer = new GridTreeViewer(buyComposite, SWT.NONE | SWT.MULTI | SWT.V_SCROLL);
		ColumnViewerToolTipSupport.enableFor(buyOptionsViewer);

		GridViewerHelper.configureLookAndFeel(buyOptionsViewer);
		buyOptionsViewer.getGrid().setHeaderVisible(false);

		createColumn(buyOptionsViewer, "Buy", new BuyOptionDescriptionFormatter(), false);

		buyOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS));
		hookOpenEditor(buyOptionsViewer);
		{
			mgr = new MenuManager();
			final BuyOptionsContextMenuManager listener = new BuyOptionsContextMenuManager(buyOptionsViewer, scenarioEditingLocation, mgr);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
			buyOptionsViewer.getGrid().addMenuDetectListener(listener);
		}
		{
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final BuysDropTargetListener listener = new BuysDropTargetListener(scenarioEditingLocation, buyOptionsViewer);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
			buyOptionsViewer.addDropSupport(DND.DROP_MOVE, types, listener);
		}
		inputWants.add(model -> buyOptionsViewer.setInput(model));

		return buyOptionsViewer.getControl();
	}

	@Override
	public void refresh() {
		buyOptionsViewer.refresh();
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
