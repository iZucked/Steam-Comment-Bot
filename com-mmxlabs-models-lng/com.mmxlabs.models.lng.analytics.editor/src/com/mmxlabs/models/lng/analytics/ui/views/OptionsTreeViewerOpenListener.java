package com.mmxlabs.models.lng.analytics.ui.views;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreeSelection;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;

public class OptionsTreeViewerOpenListener implements IOpenListener {
	private OptionModellerView optionModellerView;
	
	public OptionsTreeViewerOpenListener(OptionModellerView optionModellerView) {
		this.optionModellerView = optionModellerView;
	}
	
	@Override
	public void open(OpenEvent event) {
		ISelection selection = event.getSelection();
		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			Object firstElement = treeSelection.getFirstElement();
			if (firstElement instanceof OptionAnalysisModel) {
				optionModellerView.setInput((@Nullable OptionAnalysisModel) firstElement);
			}
			
		}
	}

}
