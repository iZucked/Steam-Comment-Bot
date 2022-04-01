/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.views;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.ui.editorpart.HolidayCalendarsViewerPane;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.rcp.common.ecore.SafeAdapterImpl;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class HolidayCalendarsView extends ScenarioTableViewerView<HolidayCalendarsViewerPane> {
	public static final String ID = "com.mmxlabs.models.lng.pricing.editor.HolidayCalendarsView";

	private Button editBtn;
	private Button deleteBtn;

	private ComboViewer calendarSelectionViewer;
	private PricingModel pricingModel;
	private String lastSelectedCalendar;

	private final Adapter calendarListener = new SafeAdapterImpl() {

		@Override
		public void safeNotifyChanged(final Notification msg) {
			if (msg.isTouch()) {
				return;
			}

			if (msg.getFeature() == PricingPackage.Literals.PRICING_MODEL__HOLIDAY_CALENDARS) {
				if (calendarSelectionViewer != null) {

					final List<HolidayCalendar> models = pricingModel.getHolidayCalendars().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors.toList());
					calendarSelectionViewer.setInput(models);
					HolidayCalendar selectedCalendar = null;
					if (!models.isEmpty()) {
						if (lastSelectedCalendar != null) {
							for (final HolidayCalendar cal : pricingModel.getHolidayCalendars()) {
								if (lastSelectedCalendar.equals(cal.getName())) {
									selectedCalendar = cal;
									break;
								}
							}
						}
						if (selectedCalendar == null) {
							selectedCalendar = pricingModel.getHolidayCalendars().get(0);
						}
						lastSelectedCalendar = selectedCalendar.getName();
						calendarSelectionViewer.setSelection(new StructuredSelection(selectedCalendar));
					}
				}
			}
		}
	};

	@Override
	protected HolidayCalendarsViewerPane createViewerPane() {
		return new HolidayCalendarsViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final HolidayCalendarsViewerPane pane) {

		final EditingDomain domain = getEditingDomain();
		if (domain != null) {

			if (this.pricingModel == null) {
				pricingModel = ScenarioModelUtil.getPricingModel(getScenarioDataProvider());
				pricingModel.eAdapters().add(calendarListener);
			} else {
				pricingModel.eAdapters().add(calendarListener);
			}
			final List<HolidayCalendar> holidays = pricingModel.getHolidayCalendars();
			calendarSelectionViewer.setContentProvider(new ArrayContentProvider());
			calendarSelectionViewer.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(final Object element) {
					return ((HolidayCalendar) element).getName();
				}
			});
			calendarSelectionViewer.setInput(holidays);

			pane.init(Arrays.asList(PricingPackage.eINSTANCE.getHolidayCalendar_Entries()), getAdapterFactory(), getModelReference());
			pane.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			calendarSelectionViewer.addSelectionChangedListener(event -> {
				final ISelection selection = calendarSelectionViewer.getSelection();
				if (selection.isEmpty()) {
					deleteBtn.setEnabled(false);
					editBtn.setEnabled(false);
					pane.setInput(null);
				} else {
					final HolidayCalendar hc = (HolidayCalendar) calendarSelectionViewer.getStructuredSelection().getFirstElement();
					pane.setInput(hc);

					deleteBtn.setEnabled(true);
					editBtn.setEnabled(true);
				}
			});

			if (!holidays.isEmpty()) {
				calendarSelectionViewer.setSelection(new StructuredSelection(holidays.get(0)));

			}
		}
	}

	@Override
	protected Composite wrapChildComposite(final Composite composite) {

		final Composite sectionParent = new Composite(composite, SWT.NONE);
		sectionParent.setLayout(GridLayoutFactory.fillDefaults().equalWidth(true).create());
		sectionParent.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		final Composite selector = new Composite(sectionParent, SWT.NONE);
		selector.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).grab(true, false).create());

		final Label label = new Label(selector, SWT.NONE);
		label.setText("Calendar: ");

		calendarSelectionViewer = new ComboViewer(selector);
		calendarSelectionViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().hint(70, SWT.DEFAULT).create());

		{
			editBtn = new Button(selector, SWT.PUSH);
			final Image imgEnabled = CommonImages.getImageDescriptor(IconPaths.Edit, IconMode.Enabled).createImage();
			editBtn.setImage(imgEnabled);
			editBtn.addDisposeListener(e -> imgEnabled.dispose());

			editBtn.setEnabled(false);

			editBtn.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					final IStructuredSelection selection = calendarSelectionViewer.getStructuredSelection();
					DetailCompositeDialogUtil.editSelection(HolidayCalendarsView.this, selection);
					calendarSelectionViewer.refresh();
				}
			});
		}

		{
			deleteBtn = new Button(selector, SWT.PUSH);

			final Image imgEnabled = CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled).createImage();
			deleteBtn.setImage(imgEnabled);
			deleteBtn.addDisposeListener(e -> imgEnabled.dispose());

			deleteBtn.setEnabled(false);

			deleteBtn.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {

					if (calendarSelectionViewer == null) {
						return;
					}
					if (calendarSelectionViewer.getStructuredSelection() == null || calendarSelectionViewer.getStructuredSelection().isEmpty()) {
						return;
					}
					final IStructuredSelection selection = calendarSelectionViewer.getStructuredSelection();
					final Object obj = selection.getFirstElement();
					if (!(obj instanceof HolidayCalendar)) {
						return;
					}

					final HolidayCalendar hc = (HolidayCalendar) obj;
					getModelReference().executeWithTryLock(true, 200, () -> {

						final CompoundCommand cmd = new CompoundCommand("Remove calendar");
						cmd.append(RemoveCommand.create(getEditingDomain(), pricingModel, PricingPackage.eINSTANCE.getPricingModel_HolidayCalendars(), hc));

						final CommandStack commandStack = getModelReference().getCommandStack();
						commandStack.execute(cmd);
					});
					final List<HolidayCalendar> models = pricingModel.getHolidayCalendars().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors.toList());
					calendarSelectionViewer.setInput(models);

					if (!models.isEmpty()) {
						// Pick first model.
						final var input = models.get(0);
						calendarSelectionViewer.setSelection(new StructuredSelection(input));
					} else {
						calendarSelectionViewer.setSelection(new StructuredSelection());
					}
				}
			});
		}

		final Button btn_new = new Button(selector, SWT.PUSH);

		final Image addImg = CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled).createImage();
		btn_new.setImage(addImg);
		btn_new.addDisposeListener(e -> addImg.dispose());

		btn_new.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				final HolidayCalendar pc = PricingFactory.eINSTANCE.createHolidayCalendar();
				getModelReference().executeWithTryLock(true, 200, () -> {

					final CompoundCommand cmd = new CompoundCommand("New calendar");
					cmd.append(AddCommand.create(getEditingDomain(), pricingModel, PricingPackage.eINSTANCE.getPricingModel_HolidayCalendars(), pc));

					final CommandStack commandStack = getModelReference().getCommandStack();
					commandStack.execute(cmd);
					DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(HolidayCalendarsView.this, pc, commandStack.getMostRecentCommand());
				});
				final List<HolidayCalendar> models = pricingModel.getHolidayCalendars().stream().filter(i -> i.getName() != null && !i.getName().isEmpty()).collect(Collectors.toList());
				calendarSelectionViewer.setInput(models);
				calendarSelectionViewer.setSelection(new StructuredSelection(pc));

			}
		});

		selector.setLayout(new GridLayout(5, false));

		return sectionParent;
	}

	@Override
	protected void cleanUpInstance(final ScenarioInstance instance) {
		if (calendarSelectionViewer != null) {
			calendarSelectionViewer = null;
		}
		if (pricingModel != null) {
			pricingModel.eAdapters().remove(calendarListener);
			pricingModel = null;
		}
		lastSelectedCalendar = "";
	}

	@Override
	public void dispose() {
		cleanUpInstance(null);
		super.dispose();
	}
}
