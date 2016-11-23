package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.VesselDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.providers.VesselContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;

public class VesselsComponent extends AbstractSandboxComponent {

	private GridTreeViewer vesselViewer;

	public VesselsComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors, @NonNull final Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	public void createControls(final Composite parent, boolean expanded, final IExpansionListener expansionListener, OptionModellerView optionModellerView) {

		final ExpandableComposite expandableVessels = wrapInExpandable(parent, "Vessels", p -> createVesselOptionsViewer(p).getGrid());
		expandableVessels.setExpanded(expanded);
		expandableVessels.addExpansionListener(expansionListener);
	}

	@Override
	public void refresh() {
		vesselViewer.refresh();
	}

	private GridTreeViewer createVesselOptionsViewer(final Composite vesselComposite) {
		vesselViewer = new GridTreeViewer(vesselComposite, SWT.NONE | SWT.MULTI);

		GridViewerHelper.configureLookAndFeel(vesselViewer);
		vesselViewer.getGrid().setHeaderVisible(false);

		// vesselViewer.getGrid().setHeaderVisible(true);

		createColumn(vesselViewer, "Vessels", new VesselDescriptionFormatter(), false);
		vesselViewer.setContentProvider(new VesselContentProvider(scenarioEditingLocation));
		hookDragSource(vesselViewer);
		return vesselViewer;
	}

	public void setInput(IScenarioEditingLocation scenarioEditingLocation) {
		vesselViewer.setInput(scenarioEditingLocation);
	}
}
