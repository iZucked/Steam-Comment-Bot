package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.providers.ShippingOptionsContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class ShippingOptionsComponent extends AbstractSandboxComponent {

	private GridTreeViewer shippingOptionsViewer;
	private MenuManager mgr;

	public ShippingOptionsComponent(@NonNull IScenarioEditingLocation scenarioEditingLocation, Map<Object, IStatus> validationErrors, @NonNull Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	public void createControls(Composite parent, boolean expanded, IExpansionListener expansionListener, OptionModellerView optionModellerView) {
		ExpandableComposite expandableShipping = wrapInExpandable(parent, "Shipping", p -> createShippingOptionsViewer(p).getGrid(), expandableCompo -> {

			{
				final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
				final ShippingOptionsDropTargetListener listener = new ShippingOptionsDropTargetListener(scenarioEditingLocation, shippingOptionsViewer);
				inputWants.add(model -> listener.setOptionAnalysisModel(model));
				// Control control = getControl();
				final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_MOVE);
				dropTarget.setTransfer(types);
				dropTarget.addDropListener(listener);
			}

			final Label addShipping = new Label(expandableCompo, SWT.NONE);
			expandableCompo.setTextClient(addShipping);
			addShipping.setImage(image_grey_add);

			addShipping.setLayoutData(GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.BOTTOM).grab(true, false).create());
			addShipping.addMouseTrackListener(new MouseTrackListener() {

				@Override
				public void mouseHover(final MouseEvent e) {

				}

				@Override
				public void mouseExit(final MouseEvent e) {
					addShipping.setImage(image_grey_add);
				}

				@Override
				public void mouseEnter(final MouseEvent e) {
					addShipping.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
				}
			});

			addShipping.addMouseListener(new MouseListener() {

				LocalMenuHelper helper = new LocalMenuHelper(addShipping.getParent());
				{
					helper.addAction(new RunnableAction("Nominated vessel", () -> {
						final NominatedShippingOption opt = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();

						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								AddCommand.create(scenarioEditingLocation.getEditingDomain(), modelProvider.get(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
								modelProvider.get(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
					}));
					helper.addAction(new RunnableAction("Round trip vessel", () -> {
						final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();

						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								AddCommand.create(scenarioEditingLocation.getEditingDomain(), modelProvider.get(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
								modelProvider.get(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
					}));
					helper.addAction(new RunnableAction("Fleet vessel", () -> {
						final FleetShippingOption opt = AnalyticsFactory.eINSTANCE.createFleetShippingOption();
						AnalyticsBuilder.setDefaultEntity(scenarioEditingLocation, opt);
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
								AddCommand.create(scenarioEditingLocation.getEditingDomain(), modelProvider.get(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
								modelProvider.get(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

						DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
					}));
				}

				@Override
				public void mouseDoubleClick(final MouseEvent e) {

				}

				@Override
				public void mouseDown(final MouseEvent e) {
					if (modelProvider.get() != null) {
						helper.open();
					}

				}

				@Override
				public void mouseUp(final MouseEvent e) {

				}
			});
		}, false);

		expandableShipping.addExpansionListener(expansionListener);
		expandableShipping.setExpanded(expanded);

		// Failed attempt to set a minimum size on the table
		shippingOptionsViewer.getGrid().setLayoutData(GridDataFactory.swtDefaults().hint(SWT.DEFAULT, 150).create());

	}

	private GridTreeViewer createShippingOptionsViewer(final Composite vesselComposite) {

		shippingOptionsViewer = new GridTreeViewer(vesselComposite, SWT.NONE | SWT.MULTI);

		GridViewerHelper.configureLookAndFeel(shippingOptionsViewer);

		shippingOptionsViewer.getGrid().setHeaderVisible(false);

		createColumn(shippingOptionsViewer, "Templates", new ShippingOptionDescriptionFormatter(), false);
		shippingOptionsViewer.setContentProvider(new ShippingOptionsContentProvider(scenarioEditingLocation));
		hookOpenEditor(shippingOptionsViewer);

		{
			final Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final ShippingOptionsDropTargetListener listener = new ShippingOptionsDropTargetListener(scenarioEditingLocation, shippingOptionsViewer);
			shippingOptionsViewer.addDropSupport(DND.DROP_MOVE, transferTypes, listener);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
		}

		mgr = new MenuManager();
		final ShippingOptionsContextMenuManager listener = new ShippingOptionsContextMenuManager(shippingOptionsViewer, scenarioEditingLocation, mgr);
		shippingOptionsViewer.getGrid().addMenuDetectListener(listener);

		hookDragSource(shippingOptionsViewer);

		inputWants.add(model -> shippingOptionsViewer.setInput(model));
		inputWants.add(model -> listener.setOptionAnalysisModel(model)); 

		return shippingOptionsViewer;
	}

	@Override
	public void refresh() {
		shippingOptionsViewer.refresh();
	}

	@Override
	public void dispose() {
		mgr.dispose();
		super.dispose();
	}

}
