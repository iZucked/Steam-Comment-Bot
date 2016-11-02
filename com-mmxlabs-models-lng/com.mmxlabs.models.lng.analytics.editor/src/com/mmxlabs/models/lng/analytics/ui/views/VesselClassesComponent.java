package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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
import com.mmxlabs.models.lng.analytics.ui.views.providers.VesselClassContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.VesselContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;

public class VesselClassesComponent extends AbstractSandboxComponent {

	private GridTreeViewer vesselClassViewer;

	public VesselClassesComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull final Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	public void createControls(final Composite parent, boolean expanded, final IExpansionListener expansionListener, OptionModellerView optionModellerView) {

		ExpandableComposite expandableVesselClasses = wrapInExpandable(parent, "Vessel Classes", p -> createVesselClassOptionsViewer(p).getGrid());
		expandableVesselClasses.setExpanded(expanded);
		expandableVesselClasses.addExpansionListener(expansionListener);
	}

	@Override
	public void refresh() {
		vesselClassViewer.refresh();
	}

	private GridTreeViewer createVesselClassOptionsViewer(final Composite vesselComposite) {
		vesselClassViewer = new GridTreeViewer(vesselComposite, SWT.NONE | SWT.MULTI);

		GridViewerHelper.configureLookAndFeel(vesselClassViewer);
		vesselClassViewer.getGrid().setHeaderVisible(false);

		// vesselViewer.getGrid().setHeaderVisible(true);

		createColumn(vesselClassViewer, "Vessel Classes", new VesselDescriptionFormatter(), false);
		vesselClassViewer.setContentProvider(new VesselClassContentProvider(scenarioEditingLocation));
		hookDragSource(vesselClassViewer);
		return vesselClassViewer;
	}

	public void setInput(IScenarioEditingLocation scenarioEditingLocation) {
		vesselClassViewer.setInput(scenarioEditingLocation);
	}
}
