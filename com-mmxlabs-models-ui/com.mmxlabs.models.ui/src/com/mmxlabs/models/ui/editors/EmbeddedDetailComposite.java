/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.PairKeyedMap;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DefaultDialogEditingContext;
import com.mmxlabs.models.ui.editors.dialogs.IDialogController;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.IStatusProvider.IStatusChangedListener;
import com.mmxlabs.rcp.common.RunnerHelper;

public class EmbeddedDetailComposite {

	private IScenarioEditingLocation scenarioEditingLocation;

	private EObject input;

	private ObservablesManager observablesManager;
	private EMFDataBindingContext dbc;
	private IDisplayComposite displayComposite;
	private DefaultDialogEditingContext dialogContext;
	private FormToolkit toolkit = new FormToolkit(Display.getDefault());

	private ScrolledComposite mainCompositeScrolled;

	private final IDialogController dialogController = new IDialogController() {

		private final PairKeyedMap<EObject, EStructuralFeature, Boolean> visibilityMap = new PairKeyedMap<>();

		@Override
		public void validate() {
			// Changes should automatically trigger validation externally
		}

		@Override
		public void rebuild(boolean pack) {
			setInput(input);
		}

		@Override
		public void relayout() {
			setInput(input);
		}

		@Override
		public void updateEditorVisibility() {
			// Trigger the recursive UI update
			if (displayComposite != null) {
				displayComposite.checkVisibility(dialogContext);
			}
		}

		@Override
		public void setEditorVisibility(final EObject object, final EStructuralFeature feature, final boolean visible) {
			visibilityMap.put(object, feature, visible);

		}

		@Override
		public boolean getEditorVisibility(final EObject object, final EStructuralFeature feature) {
			if (visibilityMap.contains(object, feature)) {
				return visibilityMap.get(object, feature).booleanValue();
			}
			return true;
		}
	};

	private IStatusChangedListener statusChangedListener;

	public EmbeddedDetailComposite(final Composite parent, IScenarioEditingLocation scenarioEditingLocation) {
		this.scenarioEditingLocation = scenarioEditingLocation;

		statusChangedListener = (provider, status) -> {
			if (displayComposite != null) {
				RunnerHelper.runNowOrAsync(() -> displayComposite.displayValidationStatus(status));
			}
		};

		mainCompositeScrolled = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		mainCompositeScrolled.setExpandVertical(true);
		mainCompositeScrolled.setExpandHorizontal(true);
		
		mainCompositeScrolled.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		mainCompositeScrolled.setLayoutData(GridDataFactory.fillDefaults()//
				.grab(true, true)//
				.create());
		mainCompositeScrolled.setLayout(GridLayoutFactory.fillDefaults()//
				.equalWidth(false) //
				.numColumns(1) //
				.margins(0, 0) //
				.spacing(0, 0) //
				.create());

		setInput(null);
		IStatusProvider statusProvider = scenarioEditingLocation.getStatusProvider();

		if (statusProvider != null) {
			statusProvider.addStatusChangedListener(statusChangedListener);
		}
	}

	private void doCreateFormContent(EObject object) {
		IStatusProvider statusProvider = scenarioEditingLocation.getStatusProvider();
		if (statusProvider != null) {
			statusProvider.removeStatusChangedListener(statusChangedListener);
			statusProvider.addStatusChangedListener(statusChangedListener);
		}
		mainCompositeScrolled.setContent(null);

		if (displayComposite != null) {
			displayComposite.getComposite().dispose();
			displayComposite = null;
		}
		if (object == null) {
			return;
		}

		dialogContext = new DefaultDialogEditingContext(dialogController, scenarioEditingLocation, false, true);

		final EObject selection = object;
		IDisplayCompositeFactory displayCompositeFactory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(object.eClass());
		displayComposite = displayCompositeFactory.createToplevelComposite(mainCompositeScrolled, selection.eClass(), dialogContext, toolkit);
		displayComposite.getComposite().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		mainCompositeScrolled.setContent(displayComposite.getComposite());

		displayComposite.setCommandHandler(scenarioEditingLocation.getDefaultCommandHandler());

		displayComposite.getComposite().setLayoutData(GridDataFactory.fillDefaults() //
				.grab(true, true) //
				.span(5, 1).create());

		final Collection<EObject> range = Collections.emptySet();
		displayComposite.display(dialogContext, scenarioEditingLocation.getRootObject(), object, range, dbc);

		displayComposite.getComposite().setLayoutData(GridDataFactory.fillDefaults() //
				.grab(true, true) //
				.span(5, 1).create());
		// Trigger update of inline editor visibility and UI state update
		dialogController.updateEditorVisibility();

		displayComposite.getComposite().pack();
		if (statusProvider != null) {
			displayComposite.displayValidationStatus(statusProvider.getStatus());
		}

	}

	public void setInput(final @Nullable EObject input) {

		if (observablesManager != null) {
			observablesManager.dispose();
		}
		if (dbc != null) {
			dbc.dispose();
		}
		if (input != null) {
			this.dbc = new EMFDataBindingContext();
			this.observablesManager = new ObservablesManager();

			// This call means we do not need to manually manage our databinding objects lifecycle manually.
			observablesManager.runAndCollect(() -> {
				this.input = input;
				doCreateFormContent(input);
			});
		}
		if (mainCompositeScrolled.getContent() != null) {
			mainCompositeScrolled.setMinSize(mainCompositeScrolled.getContent().computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}

	}

	public void dispose() {

		IStatusProvider statusProvider = scenarioEditingLocation.getStatusProvider();
		if (statusProvider != null) {
			statusProvider.removeStatusChangedListener(statusChangedListener);
		}

		if (observablesManager != null) {
			observablesManager.dispose();
		}
		if (dbc != null) {
			dbc.dispose();
		}
	}

	public EObject getInput() {
		return input;
	}

	public Control getComposite() {
		return mainCompositeScrolled;
	}

}
