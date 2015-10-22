/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;

public class PromptToolbarEditor extends ControlContribution {

	private final class LimitWidgetHeightListener implements Listener {
		private final Composite pparent;
		private final Button btn;

		private LimitWidgetHeightListener(final Composite pparent, final Button btn) {
			this.pparent = pparent;
			this.btn = btn;
		}

		@Override
		public void handleEvent(final Event arg0) {
			// This should be the toolbar this control set is contained in.
			final Composite toolbarComposite = pparent.getParent();
			// Get the height of the composite.
			int toolbarHeight = toolbarComposite.getSize().y;
			// Fix issue in workbench. On opening the application, the toolbar height is 44 allowing large buttons. However the real hight is only 22 (and this is correct when opening an editor after
			// the application has opened). This will probably cause issues on e.g. Alex's machine when running at high resolution (3840x2160 -- icons are already tiny on this mode).
			if (toolbarHeight > 22) {
				toolbarHeight = 22;
			}
			final Point size = btn.getSize();

			// Clamp to toolbar height.
			if (size.y > toolbarHeight) {
				btn.setSize(size.x, toolbarHeight);
			}
		}
	}

	private EditingDomain editingDomain;
	private LNGScenarioModel rootObject;

	private DateTime periodStartEditor;
	private DateTime periodEndEditor;

	private boolean locked = false;

	private final EContentAdapter adapter = new EContentAdapter() {
		public void notifyChanged(final Notification notification) {
			final Object newValue = notification.getNewValue();
			if (notification.getFeature() == LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_PromptPeriodStart()) {
				if (newValue != null) {

					final LocalDate date = (LocalDate) newValue;
					periodStartEditor.setYear(date.getYear());
					periodStartEditor.setMonth(date.getMonthValue() - 1);
					periodStartEditor.setDay(date.getDayOfMonth());
					periodStartEditor.setEnabled(true);
				}
			} else if (notification.getFeature() == LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_PromptPeriodEnd()) {
				if (newValue != null) {
					final LocalDate date = (LocalDate) newValue;
					periodEndEditor.setYear(date.getYear());
					periodEndEditor.setMonth(date.getMonthValue() - 1);
					periodEndEditor.setDay(date.getDayOfMonth());
					periodEndEditor.setEnabled(true);
				}
			}
		}
	};
	private Button btn90Day;

	public PromptToolbarEditor(final String id, final EditingDomain editingDomain, final LNGScenarioModel rootObject) {
		super(id);
		this.editingDomain = editingDomain;
		this.rootObject = rootObject;
	}

	@Override
	protected Control createControl(final Composite ppparent) {

		final Composite pparent = new Composite(ppparent, SWT.NONE);
		pparent.setLayout(GridLayoutFactory.fillDefaults().numColumns(6).equalWidth(false).spacing(3, 0).margins(0, 0).create());

		final Label lbl = new Label(pparent, SWT.NONE);
		lbl.setText("Prompt:");
		lbl.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());

		final CompoundCommand setDefaultPromptCommand = new CompoundCommand("Set default prompt");

		periodStartEditor = new DateTime(pparent, SWT.DATE | SWT.BORDER | SWT.DROP_DOWN);
		periodStartEditor.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
		{
			final LNGPortfolioModel portfolioModel = rootObject.getPortfolioModel();
			if (portfolioModel.isSetPromptPeriodStart()) {
				final LocalDate date = portfolioModel.getPromptPeriodStart();
				periodStartEditor.setYear(date.getYear());
				periodStartEditor.setMonth(date.getMonthValue() - 1);
				periodStartEditor.setDay(date.getDayOfMonth());
			} else {
				setDefaultPromptCommand
						.append(SetCommand.create(editingDomain, rootObject.getPortfolioModel(), LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_PromptPeriodStart(), new LocalDate()));
			}
		}
		periodStartEditor.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				final LocalDate date = new LocalDate(periodStartEditor.getYear(), periodStartEditor.getMonth() + 1, periodStartEditor.getDay());
				final Command cmd = SetCommand.create(editingDomain, rootObject.getPortfolioModel(), LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_PromptPeriodStart(), date);
				editingDomain.getCommandStack().execute(cmd);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		final Label lbl2 = new Label(pparent, SWT.NONE);
		lbl2.setText(" to ");

		periodEndEditor = new DateTime(pparent, SWT.DATE | SWT.BORDER | SWT.DROP_DOWN);
		periodEndEditor.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
		{
			final LNGPortfolioModel portfolioModel = rootObject.getPortfolioModel();
			if (portfolioModel.isSetPromptPeriodEnd()) {
				final LocalDate date = portfolioModel.getPromptPeriodEnd();
				periodEndEditor.setYear(date.getYear());
				periodEndEditor.setMonth(date.getMonthValue() - 1);
				periodEndEditor.setDay(date.getDayOfMonth());
				// periodEndEnabled.setSelection(true);
			} else {

				setDefaultPromptCommand
						.append(SetCommand.create(editingDomain, rootObject.getPortfolioModel(), LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_PromptPeriodEnd(), new LocalDate().plusDays(90)));
			}
		}
		periodEndEditor.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final LocalDate date = new LocalDate(periodEndEditor.getYear(), periodEndEditor.getMonth() + 1, periodEndEditor.getDay());
				final Command cmd = SetCommand.create(editingDomain, rootObject.getPortfolioModel(), LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_PromptPeriodEnd(), date);
				editingDomain.getCommandStack().execute(cmd);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		btn90Day = new Button(pparent, SWT.PUSH);
		btn90Day.setText("90d");
		btn90Day.setToolTipText("Set prompt period to 90 days from today");

		btn90Day.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final LocalDate date = new LocalDate();
				final CompoundCommand cc = new CompoundCommand("Set prompt");
				cc.append(SetCommand.create(editingDomain, rootObject.getPortfolioModel(), LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_PromptPeriodStart(), date));
				cc.append(SetCommand.create(editingDomain, rootObject.getPortfolioModel(), LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_PromptPeriodEnd(), date.plusDays(90)));
				editingDomain.getCommandStack().execute(cc);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});
		// Limit height to toolbar height.
		btn90Day.addListener(SWT.Resize, new LimitWidgetHeightListener(pparent, btn90Day));

		// Listen to further changes
		rootObject.getPortfolioModel().eAdapters().add(adapter);

		if (!setDefaultPromptCommand.isEmpty()) {
			editingDomain.getCommandStack().execute(setDefaultPromptCommand);
		}

		return pparent;
	}

	@Override
	public void dispose() {
		if (rootObject != null) {
			rootObject.getPortfolioModel().eAdapters().remove(adapter);
			rootObject = null;
		}
		editingDomain = null;
		super.dispose();
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(final boolean locked) {
		this.locked = locked;
		periodStartEditor.setEnabled(!locked);
		periodEndEditor.setEnabled(!locked);
		btn90Day.setEnabled(!locked);
	}
}