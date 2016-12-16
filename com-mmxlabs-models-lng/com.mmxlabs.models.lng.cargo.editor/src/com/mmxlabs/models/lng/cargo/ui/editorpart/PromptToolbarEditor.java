/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.time.LocalDate;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.rcp.common.actions.RunnableAction;

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
			// Fix issue in workbench. On opening the application, the toolbar height is 44 allowing large buttons. However the real height is only 22 (and this is correct when opening an editor after
			// the application has opened). This will probably cause issues on e.g. Alex's machine when running at high resolution (3840x2160 -- icons are already tiny on this mode).
			if (toolbarHeight > 23) {
				toolbarHeight = 23;
			}
			final Point size = btn.getSize();

			// Clamp to toolbar height.
			if (size.y > toolbarHeight) {
				btn.setSize(size.x, toolbarHeight);
			}
		}
	}

	private EditingDomain editingDomain;
	private LNGScenarioModel scenarioModel;

	private DateTime periodStartEditor;
	private DateTime periodEndEditor;
	private DateTime scheduleEndEditor;

	private boolean locked = false;

	private final EContentAdapter adapter = new EContentAdapter() {
		public void notifyChanged(final Notification notification) {
			final Object newValue = notification.getNewValue();
			if (notification.getFeature() == LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart()) {
				if (newValue != null) {

					final LocalDate date = (LocalDate) newValue;
					periodStartEditor.setYear(date.getYear());
					periodStartEditor.setMonth(date.getMonthValue() - 1);
					periodStartEditor.setDay(date.getDayOfMonth());
					periodStartEditor.setEnabled(true);
				}
			} else if (notification.getFeature() == LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd()) {
				if (newValue != null) {
					final LocalDate date = (LocalDate) newValue;
					periodEndEditor.setYear(date.getYear());
					periodEndEditor.setMonth(date.getMonthValue() - 1);
					periodEndEditor.setDay(date.getDayOfMonth());
					periodEndEditor.setEnabled(true);
				}
			} else if (notification.getFeature() == LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SchedulingEndDate()) {
				if (newValue != null) {
					lbl3.setText(" Scheduling end: ");

					final LocalDate date = (LocalDate) newValue;
					scheduleEndEditor.setYear(date.getYear());
					scheduleEndEditor.setMonth(date.getMonthValue() - 1);
					scheduleEndEditor.setDay(date.getDayOfMonth());
					scheduleEndEditor.setEnabled(true);

				} else {
					lbl3.setText(" No scheduling end: ");
					scheduleEndEditor.setEnabled(false);

				}
			}
		}
	};
	private Button btn90Day;
	private Label lbl3;

	public PromptToolbarEditor(final String id, final EditingDomain editingDomain, final LNGScenarioModel scenarioModel) {
		super(id);
		this.editingDomain = editingDomain;
		this.scenarioModel = scenarioModel;
	}

	@Override
	protected Control createControl(final Composite ppparent) {

		final Composite pparent = new Composite(ppparent, SWT.NONE);
		pparent.setLayout(GridLayoutFactory.fillDefaults().numColumns(8).equalWidth(false).spacing(3, 0).margins(0, 0).create());

		final Label lbl = new Label(pparent, SWT.NONE);
		lbl.setText("Prompt:");
		lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.TOP).minSize(1000, -1).create());

		final CompoundCommand setDefaultPromptCommand = new CompoundCommand("Set default prompt");

		periodStartEditor = new DateTime(pparent, SWT.DATE | SWT.BORDER | SWT.DROP_DOWN);
		periodStartEditor.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
		{
			if (scenarioModel.isSetPromptPeriodStart()) {
				final LocalDate date = scenarioModel.getPromptPeriodStart();
				periodStartEditor.setYear(date.getYear());
				periodStartEditor.setMonth(date.getMonthValue() - 1);
				periodStartEditor.setDay(date.getDayOfMonth());
			} else {
				setDefaultPromptCommand.append(SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart(), LocalDate.now()));
			}
		}
		periodStartEditor.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				final LocalDate date = LocalDate.of(periodStartEditor.getYear(), periodStartEditor.getMonth() + 1, periodStartEditor.getDay());
				final Command cmd = SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart(), date);
				editingDomain.getCommandStack().execute(cmd);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		final Label lbl2 = new Label(pparent, SWT.NONE);
		lbl2.setText(" to ");
		lbl2.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.TOP). create());

		periodEndEditor = new DateTime(pparent, SWT.DATE | SWT.BORDER | SWT.DROP_DOWN);
//		.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
		periodEndEditor.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.TOP). create());

		{
			if (scenarioModel.isSetPromptPeriodEnd()) {
				final LocalDate date = scenarioModel.getPromptPeriodEnd();
				periodEndEditor.setYear(date.getYear());
				periodEndEditor.setMonth(date.getMonthValue() - 1);
				periodEndEditor.setDay(date.getDayOfMonth());
				// periodEndEnabled.setSelection(true);
			} else {

				setDefaultPromptCommand.append(SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd(), LocalDate.now().plusDays(90)));
			}
		}
		periodEndEditor.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final LocalDate date = LocalDate.of(periodEndEditor.getYear(), periodEndEditor.getMonth() + 1, periodEndEditor.getDay());
				final Command cmd = SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd(), date);
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
				final LocalDate date = LocalDate.now();
				final CompoundCommand cc = new CompoundCommand("Set prompt");
				cc.append(SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart(), date));
				cc.append(SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd(), date.plusDays(90)));
				editingDomain.getCommandStack().execute(cc);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});
		// Limit height to toolbar height.
		btn90Day.addListener(SWT.Resize, new LimitWidgetHeightListener(pparent, btn90Day));

		lbl3 = new Label(pparent, SWT.NONE);
		lbl3.setText(" No Scheduling end ");
		lbl3.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.TOP). create());

		hookScheduleEndDateMenu(lbl3);

		scheduleEndEditor = new DateTime(pparent, SWT.DATE | SWT.BORDER | SWT.DROP_DOWN);
		scheduleEndEditor.setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
		hookScheduleEndDateMenu(scheduleEndEditor);

		{
			if (scenarioModel.isSetSchedulingEndDate()) {
				lbl3.setText(" Scheduling end ");
				final LocalDate date = scenarioModel.getSchedulingEndDate();
				scheduleEndEditor.setYear(date.getYear());
				scheduleEndEditor.setMonth(date.getMonthValue() - 1);
				scheduleEndEditor.setDay(date.getDayOfMonth());
				scheduleEndEditor.setEnabled(true);
				// periodEndEnabled.setSelection(true);
			} else {
				lbl3.setText(" No Scheduling end ");
				scheduleEndEditor.setEnabled(false);
				// setDefaultPromptCommand.append(SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SchedulingEndDate(), LocalDate.now().plusDays(90)));
			}
		}
	 
		scheduleEndEditor.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(final MouseEvent e) {

			}

			@Override
			public void mouseDown(final MouseEvent e) {

			}

			@Override
			public void mouseDoubleClick(final MouseEvent e) {
				if (!scenarioModel.isSetSchedulingEndDate()) {
					final Command cmd = SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SchedulingEndDate(), LocalDate.now().plusMonths(9));
					editingDomain.getCommandStack().execute(cmd);
				}
			}
		});
	 
		scheduleEndEditor.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final LocalDate date = LocalDate.of(scheduleEndEditor.getYear(), scheduleEndEditor.getMonth() + 1, scheduleEndEditor.getDay());
				final Command cmd = SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SchedulingEndDate(), date);
				editingDomain.getCommandStack().execute(cmd);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}

		});

		// Listen to further changes
		scenarioModel.eAdapters().add(adapter);

		if (!setDefaultPromptCommand.isEmpty()) {
			editingDomain.getCommandStack().execute(setDefaultPromptCommand);
		}

		return pparent;
	}

	private void hookScheduleEndDateMenu(final Control control) {
		final MenuManager mgr2 = new MenuManager();
		control.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				mgr2.dispose();
			}
		});
		control.addMenuDetectListener(new MenuDetectListener() {
			Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {
				mgr2.removeAll();
				if (menu == null) {
					menu = mgr2.createContextMenu(control);
					control.setMenu(menu);
				}
				if (scenarioModel.isSetSchedulingEndDate()) {
					mgr2.add(new RunnableAction("Remove schedule end date", () -> {
						final Command cmd = SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SchedulingEndDate(), SetCommand.UNSET_VALUE);
						editingDomain.getCommandStack().execute(cmd);
					}));
					// mgr.getMenu().setVisible(true);

				} else {
					mgr2.add(new RunnableAction("Set schedule end date", () -> {
						final Command cmd = SetCommand.create(editingDomain, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SchedulingEndDate(), LocalDate.now().plusMonths(6));
						editingDomain.getCommandStack().execute(cmd);
					}));
				}
				menu.setVisible(true);
				mgr2.setVisible(true);
			}
		});
	}

	@Override
	public void dispose() {
		if (scenarioModel != null) {
			scenarioModel.eAdapters().remove(adapter);
			scenarioModel = null;
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
		if (scenarioModel != null) {
			scheduleEndEditor.setEnabled(!locked && scenarioModel.isSetSchedulingEndDate());
		}
		btn90Day.setEnabled(!locked);
	}
}