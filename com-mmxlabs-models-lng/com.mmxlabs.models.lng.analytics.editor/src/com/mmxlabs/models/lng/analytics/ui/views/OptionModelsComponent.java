package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.OptionTreeViewerFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.OptionsTreeViewerContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;

public class OptionModelsComponent extends AbstractSandboxComponent {

	private GridTreeViewer optionsTreeViewer;
	private MenuManager mgr;

	public OptionModelsComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull final Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	public void createControls(final Composite parent, boolean expanded, final IExpansionListener expansionListener, final OptionModellerView optionModellerView) {

		final ExpandableComposite expandable = wrapInExpandable(parent, "Options history", p -> createOptionsTreeViewer(p, optionModellerView), expandableCompo -> {

			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final OptionsTreeViewerDropTargetListener listener = new OptionsTreeViewerDropTargetListener(scenarioEditingLocation, optionsTreeViewer);
			final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_MOVE);
			dropTarget.setTransfer(types);
			dropTarget.addDropListener(listener);
		});

		expandable.addExpansionListener(expansionListener);
		expandable.setExpanded(expanded);

		optionsTreeViewer.getGrid().setCellSelectionEnabled(true);

		{
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final OptionsTreeViewerDropTargetListener listener = new OptionsTreeViewerDropTargetListener(scenarioEditingLocation, optionsTreeViewer);
			optionsTreeViewer.addDropSupport(DND.DROP_MOVE, types, listener);
		}
	}

	private Control createOptionsTreeViewer(final Composite parent, final OptionModellerView optionModellerView) {

		optionsTreeViewer = new GridTreeViewer(parent, SWT.NONE);
		ColumnViewerToolTipSupport.enableFor(optionsTreeViewer);
		GridViewerHelper.configureLookAndFeel(optionsTreeViewer);
		// optionsTreeViewer.getGrid().setHeaderVisible(true);
		optionsTreeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

		CellFormatterLabelProvider labelProvider = new OptionsTreeViewerLabelProvider(new OptionTreeViewerFormatter(optionModellerView), validationErrors, "Option Models", optionModellerView);
		createColumn(optionsTreeViewer, labelProvider, "Option Models", new OptionTreeViewerFormatter(optionModellerView), true);

		mgr = new MenuManager();

		final OptionsTreeViewerContextMenuManager listener = new OptionsTreeViewerContextMenuManager(optionsTreeViewer, scenarioEditingLocation, optionModellerView, mgr);
		// inputWants.add(model -> listener.setOptionAnalysisModel(model));
		optionsTreeViewer.getGrid().addMenuDetectListener(listener);

		optionsTreeViewer.setContentProvider(new OptionsTreeViewerContentProvider());
		optionsTreeViewer.addOpenListener(new OptionsTreeViewerOpenListener(optionModellerView));
		return optionsTreeViewer.getControl();
	}

	@Override
	public @NonNull List<Consumer<OptionAnalysisModel>> getInputWants() {
		return inputWants;
	}

	@Override
	public void refresh() {
		optionsTreeViewer.refresh();
		optionsTreeViewer.expandAll();
	}

	@Override
	public void dispose() {
		mgr.dispose();
		super.dispose();
	}

	public void setInput(Set<@NonNull Object> emptySet) {
		optionsTreeViewer.setInput(emptySet);
	}

}
