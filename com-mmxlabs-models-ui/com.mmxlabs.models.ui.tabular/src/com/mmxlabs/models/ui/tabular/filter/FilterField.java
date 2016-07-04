/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.filter;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.models.ui.tabular.EObjectTableViewerFilterSupport;

/**
 * Wraps up a text box, actions, events etc for a filter
 * 
 * @author hinton
 * 
 */
public class FilterField implements ModifyListener, DisposeListener, KeyListener, IContentProposalListener2 {
	private final Text text;
	private final ActionContributionItem contribution;
	private final FilterProposalProvider proposals;
	private final GridData layoutData;

	private EObjectTableViewerFilterSupport filterSupport = null;
	private final ContentProposalAdapter adapter;

	public FilterField(final Composite textContainer) {
		text = new Text(textContainer, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.exclude = true;
		text.setLayoutData(layoutData);
		text.setVisible(false);
		proposals = new FilterProposalProvider();

		text.addModifyListener(this);
		text.addDisposeListener(this);
		text.addKeyListener(this);

		adapter = new ContentProposalAdapter(text, new TextContentAdapter(), proposals, null, null);

		adapter.setAutoActivationDelay(700);

		adapter.addContentProposalListener(this);

		contribution = new ActionContributionItem(

		new Action() {
			{
				setChecked(false);
				setText("Filter table");
				try {
					setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.ui.tabular/icons/filter.gif")));
				} catch (final MalformedURLException ex) {
				}
			}

			@Override
			public void run() {
				toggleVisibility();
			}

		}

		);
	}

	// public EObjectTableViewer getViewer() {
	// return viewer;
	// }

	/**
	 */
	public void setFilterSupport(final EObjectTableViewerFilterSupport filterSupport) {
		this.filterSupport = filterSupport;
		proposals.setFilterSupport(filterSupport);
	}

	@Override
	public void widgetDisposed(final DisposeEvent e) {
		if (e.widget == text) {
			text.removeModifyListener(this);
			text.removeKeyListener(this);
		}
	}

	@Override
	public void modifyText(final ModifyEvent e) {
		if (e.widget == text) {
			if (filterSupport != null) {
				filterSupport.setFilterString(text.isVisible() ? text.getText() : "");
			}
		}
	}

	@Override
	public void keyPressed(final KeyEvent e) {
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		if (e.keyCode == SWT.ESC) {
			// I can see no nice way to handle this case other than using a timer; sorry!
			if ((System.currentTimeMillis() - proposalLastClosed) < 200) {
				return;
			}
			if (text.getText().length() > 0) {
				text.setText("");
			} else {
				contribution.getWidget().notifyListeners(SWT.Selection, new Event());
			}
		}
	}

	/**
	 * @return the contribution
	 */
	public ActionContributionItem getContribution() {
		return contribution;
	}

	private long proposalLastClosed;

	@Override
	public void proposalPopupOpened(final ContentProposalAdapter adapter) {
	}

	@Override
	public void proposalPopupClosed(final ContentProposalAdapter adapter) {
		proposalLastClosed = System.currentTimeMillis();
	}

	public void toggleVisibility() {
		text.setVisible(!text.getVisible());
		layoutData.exclude = !layoutData.exclude;
		text.getParent().layout(true);
		if (text.isVisible() == false) {
			if (filterSupport != null) {
				filterSupport.setFilterString("");
			}
		} else {
			text.setFocus();
		}
		contribution.getAction().setChecked(text.isVisible());
	}
}
