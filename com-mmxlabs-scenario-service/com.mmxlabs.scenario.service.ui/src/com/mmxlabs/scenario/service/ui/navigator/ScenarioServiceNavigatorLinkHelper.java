/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ILinkHelper;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.navigator.LinkHelperService;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.progress.UIJob;

/**
 * Pretty much a direct copy of {@link org.eclipse.ui.internal.navigator.actions.LinkEditorAction} to be usable outside of an action context.
 */
@SuppressWarnings("restriction")
public class ScenarioServiceNavigatorLinkHelper implements ISelectionChangedListener, IPropertyListener {

	public static final int LINK_HELPER_DELAY = 120;

	private IPartListener partListener;

	private final CommonNavigator commonNavigator;

	private final CommonViewer commonViewer;

	private final LinkHelperService linkService;

	private boolean ignoreSelectionChanged;
	private boolean ignoreEditorActivation;

	private boolean checked;

	private final UIJob activateEditorJob = new UIJob("Activate Editor") {
		@Override
		public IStatus runInUIThread(final IProgressMonitor monitor) {

			if (!commonViewer.getControl().isDisposed()) {
				final ISelection selection = commonViewer.getSelection();
				if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {

					final IStructuredSelection sSelection = (IStructuredSelection) selection;
					if (sSelection.size() == 1) {
						final ILinkHelper[] helpers = linkService.getLinkHelpersFor(sSelection.getFirstElement());
						if (helpers.length > 0) {
							ignoreEditorActivation = true;
							SafeRunner.run(new SafeRunnable() {
								@Override
								public void run() throws Exception {
									helpers[0].activateEditor(commonNavigator.getSite().getPage(), sSelection);
								}
							});
							ignoreEditorActivation = false;
						}
					}
				}
			}
			return Status.OK_STATUS;
		}
	};

	private final UIJob updateSelectionJob = new UIJob("Update selection") {
		@Override
		public IStatus runInUIThread(final IProgressMonitor monitor) {

			if (!commonNavigator.getCommonViewer().getControl().isDisposed()) {
				SafeRunner.run(new SafeRunnable() {
					@Override
					public void run() throws Exception {
						final IWorkbenchPage page = commonNavigator.getSite().getPage();
						if (page != null) {
							final IEditorPart editor = page.getActiveEditor();
							if (editor != null) {
								final IEditorInput input = editor.getEditorInput();
								final IStructuredSelection newSelection = linkService.getSelectionFor(input);
								if (!newSelection.isEmpty()) {
									ignoreSelectionChanged = true;
									commonNavigator.selectReveal(newSelection);
									ignoreSelectionChanged = false;
								}
							}
						}
					}
				});
			}

			return Status.OK_STATUS;
		}
	};

	/**
	 * Create a LinkEditorAction for the given navigator and viewer.
	 * 
	 * @param aNavigator
	 *            The navigator which defines whether linking is enabled and implements {@link ISetSelectionTarget}.
	 * @param aViewer
	 *            The common viewer instance with a {@link INavigatorContentService}.
	 * @param linkHelperService
	 */
	public ScenarioServiceNavigatorLinkHelper(final CommonNavigator aNavigator, final CommonViewer aViewer, final LinkHelperService linkHelperService) {
		linkService = linkHelperService;
		commonNavigator = aNavigator;
		commonViewer = aViewer;
		init();
	}

	/**
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	protected void init() {
		partListener = new IPartListener() {

			@Override
			public void partActivated(final IWorkbenchPart part) {
				if (part instanceof IEditorPart && !ignoreEditorActivation) {
					updateSelectionJob.schedule(LINK_HELPER_DELAY);
				}
			}

			@Override
			public void partBroughtToTop(final IWorkbenchPart part) {
				if (part instanceof IEditorPart && !ignoreEditorActivation) {
					updateSelectionJob.schedule(LINK_HELPER_DELAY);
				}
			}

			@Override
			public void partClosed(final IWorkbenchPart part) {

			}

			@Override
			public void partDeactivated(final IWorkbenchPart part) {
			}

			@Override
			public void partOpened(final IWorkbenchPart part) {
			}
		};

		updateLinkingEnabled(commonNavigator.isLinkingEnabled());

		commonNavigator.addPropertyListener(this);

		// linkHelperRegistry = new
		// LinkHelperManager(commonViewer.getNavigatorContentService());
	}

	/**
		 * 
		 */
	public void dispose() {
		commonNavigator.removePropertyListener(this);
		if (isChecked()) {
			commonViewer.removePostSelectionChangedListener(this);
			commonNavigator.getSite().getPage().removePartListener(partListener);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionChangedList
	 */
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		if (commonNavigator.isLinkingEnabled() && !ignoreSelectionChanged) {
			activateEditor();
		}
	}

	/**
	 * Update the active editor based on the current selection in the Navigator.
	 */
	protected void activateEditor() {
		final ISelection selection = commonViewer.getSelection();
		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			/*
			 * Create and schedule a UI Job to activate the editor in a valid Display thread
			 */
			activateEditorJob.schedule(LINK_HELPER_DELAY);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object, int)
	 */
	@Override
	public void propertyChanged(final Object aSource, final int aPropertyId) {
		switch (aPropertyId) {
		case CommonNavigator.IS_LINKING_ENABLED_PROPERTY:
			updateLinkingEnabled(((CommonNavigator) aSource).isLinkingEnabled());
		}
	}

	/**
	 * @param toEnableLinking
	 */
	private void updateLinkingEnabled(final boolean toEnableLinking) {
		setChecked(toEnableLinking);

		if (toEnableLinking) {

			updateSelectionJob.schedule(LINK_HELPER_DELAY);

			commonViewer.addPostSelectionChangedListener(this);
			commonNavigator.getSite().getPage().addPartListener(partListener);
		} else {
			commonViewer.removePostSelectionChangedListener(this);
			commonNavigator.getSite().getPage().removePartListener(partListener);
		}
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(final boolean checked) {
		this.checked = checked;
	}

}
