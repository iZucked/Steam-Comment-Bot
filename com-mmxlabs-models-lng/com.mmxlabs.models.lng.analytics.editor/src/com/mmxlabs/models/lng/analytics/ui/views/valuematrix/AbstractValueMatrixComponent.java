package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.SandboxUIHelper;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public abstract class AbstractValueMatrixComponent {

	@NonNull
	protected final IScenarioEditingLocation scenarioEditingLocation;
	protected final Map<Object, IStatus> validationErrors;
	@NonNull
	protected final Supplier<SwapValueMatrixModel> modelProvider;
	@NonNull
	protected final List<Consumer<SwapValueMatrixModel>> inputWants = new LinkedList<>();
	
	protected ValueMatrixModellerView valueMatrixModellerView;

	protected SandboxUIHelper sandboxUIHelper;

	protected AbstractValueMatrixComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull final Supplier<SwapValueMatrixModel> modelProvider) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.validationErrors = validationErrors;
		this.modelProvider = modelProvider;
	}

	protected ExpandableComposite wrapInExpandable(final Composite composite, final String name, final Function<Composite, Control> s, @Nullable final Consumer<ExpandableComposite> customiser,
			final boolean expand) {

		sandboxUIHelper = new SandboxUIHelper(composite);

		final ExpandableComposite expandableCompo;
		if (!expand) {
			expandableCompo = new ExpandableComposite(composite, SWT.NONE, 0);
		} else {
			expandableCompo = new ExpandableComposite(composite, SWT.NONE, ExpandableComposite.TWISTIE);
		}

		expandableCompo.setText(name);
		expandableCompo.setFont(sandboxUIHelper.largeFont);
		expandableCompo.setLayout(new GridLayout(1, false));

		final Control client = s.apply(expandableCompo);
		GridDataFactory.generate(expandableCompo, 2, 2);

		expandableCompo.setClient(client);

		if (customiser != null) {
			customiser.accept(expandableCompo);
		}
		return expandableCompo;
	}
	
	public List<Consumer<SwapValueMatrixModel>> getInputWants() {
		return inputWants;
	}

	public abstract void createControls(final Composite parent, final boolean expanded, final IExpansionListener expansionListener, final ValueMatrixModellerView valueMatrixModellerView);

	public abstract void dispose();
}
