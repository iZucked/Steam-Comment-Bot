package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Iterator;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class BuysDropTargetListener implements DropTargetListener {

	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private OptionAnalysisModel optionAnalysisModel;

	private @NonNull final GridTreeViewer viewer;

	private final LocalMenuHelper menuHelper;

	public BuysDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, @NonNull final GridTreeViewer viewer) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.optionAnalysisModel = optionAnalysisModel;
		this.viewer = viewer;
		menuHelper = new LocalMenuHelper(scenarioEditingLocation.getShell());
		viewer.getControl().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				menuHelper.dispose();
			}
		});
	}

	public BuysDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull final GridTreeViewer viewer) {
		this(scenarioEditingLocation, null, viewer);
	}

	@Override
	public void dropAccept(final DropTargetEvent event) {

	}

	@Override
	public void drop(final DropTargetEvent event) {
		final OptionAnalysisModel optionAnalysisModel = this.optionAnalysisModel;
		if (optionAnalysisModel == null) {
			return;
		}
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (!selection.isEmpty()) {
				final CompoundCommand cmd = new CompoundCommand();
				final Iterator<?> itr = selection.iterator();
				while (itr.hasNext()) {
					final Object o = itr.next();
					if (o instanceof CargoModelRowTransformer.RowData) {
						final CargoModelRowTransformer.RowData rowData = (CargoModelRowTransformer.RowData) o;

						final LoadSlot loadSlot = rowData.getLoadSlot();
						final BuyOption buyRef = AnalyticsBuilder.getOrCreateBuyOption(loadSlot, optionAnalysisModel, scenarioEditingLocation, cmd);
					}
				}
				if (!cmd.isEmpty()) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);
				}

				return;
			}
		}
	}

	@Override
	public void dragOver(final DropTargetEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (!selection.isEmpty()) {
				final Iterator<?> itr = selection.iterator();
				while (itr.hasNext()) {
					final Object o = itr.next();
					if (o instanceof CargoModelRowTransformer.RowData) {
						// Found a valid source in the selection.
						event.operations = DND.DROP_MOVE;
						return;
					}
				}
			}
		}
		event.operations = DND.DROP_NONE;
	}

	@Override
	public void dragOperationChanged(final DropTargetEvent event) {

	}

	@Override
	public void dragLeave(final DropTargetEvent event) {

	}

	@Override
	public void dragEnter(final DropTargetEvent event) {

	}

	public OptionAnalysisModel getOptionAnalysisModel() {
		return optionAnalysisModel;
	}

	public void setOptionAnalysisModel(final OptionAnalysisModel optionAnalysisModel) {
		this.optionAnalysisModel = optionAnalysisModel;
	}
}
