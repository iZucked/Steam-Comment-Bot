package com.mmxlabs.scenario.service.ui.editor.actions;

import java.util.Collection;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;
import com.mmxlabs.scenario.service.ui.internal.Activator;
import com.mmxlabs.scenario.service.ui.internal.ScenarioServiceSelectionProvider;

public class PinStateEditorActionDelegate implements IEditorActionDelegate, IActionDelegate2 {
	private ScenarioServiceSelectionProvider selectionProvider;
	private IEditorPart targetEditor;
	private IAction action;
	@Override
	public void run(IAction action) {
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.action = action;
		this.targetEditor = targetEditor;
		updateActionState();
	}

	private void updateActionState() {
		boolean enabled = false;
		boolean toggled = false;
			if (targetEditor != null) {
				IEditorInput input = targetEditor.getEditorInput();
				if (input instanceof IScenarioServiceEditorInput) {
					enabled = true;
					if (selectionProvider.getPinnedInstance() != null) {
					final ScenarioInstance instance = ((IScenarioServiceEditorInput) input)
							.getScenarioInstance();
					if (instance == selectionProvider.getPinnedInstance()) {
						toggled = true;
					}
				}
			}
		}
		if (action != null) {
			action.setEnabled(enabled);
			action.setChecked(toggled);
		}
	}
	
	@Override
	public void init(final IAction action) {
		selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
		selectionProvider.addSelectionChangedListener(new IScenarioServiceSelectionChangedListener() {
			
			@Override
			public void selected(IScenarioServiceSelectionProvider provider, Collection<ScenarioInstance> selected) {
				
			}
			
			@Override
			public void pinned(IScenarioServiceSelectionProvider provider,
					ScenarioInstance oldPin, ScenarioInstance newPin) {
				updateActionState();
			}
			
			@Override
			public void deselected(IScenarioServiceSelectionProvider provider, Collection<ScenarioInstance> deselected) {
				
			}
		});
	}

	@Override
	public void dispose() {
		selectionProvider = null;
	}

	@Override
	public void runWithEvent(IAction action, Event event) {
		if (targetEditor != null) {
			IEditorInput input = targetEditor.getEditorInput();
			if (input instanceof IScenarioServiceEditorInput) {
				final ScenarioInstance instance = ((IScenarioServiceEditorInput) input).getScenarioInstance();
				if (selectionProvider.getPinnedInstance() == instance) {
					selectionProvider.setPinnedInstance(null);
				} else {
					selectionProvider.setPinnedInstance(instance);
				}
			}
		}
	}
}
