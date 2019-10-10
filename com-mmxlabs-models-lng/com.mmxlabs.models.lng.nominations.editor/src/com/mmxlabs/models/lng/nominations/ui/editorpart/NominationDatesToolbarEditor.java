/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.editorpart;

import java.time.LocalDate;
import java.util.Objects;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.nominations.NominationsFactory;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.NominationsParameters;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.date.LocalDateTextFormatter;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

/**
 * Displays a start + end date formatted text box on the tool bar, so that the start date and end date used to generate nominations
 * can be set by the user. 
 */
public class NominationDatesToolbarEditor extends ControlContribution {
	
	private interface DateGetter {
		LocalDate getDate();
	}

	private final EditingDomain editingDomain;
	
	private final IScenarioEditingLocation jointModelEditor;
	
	private final NominationsParameters nominationsParameters;
	
	private final LocalDateTextFormatter startDateFormatter = new LocalDateTextFormatter(true);
	private final LocalDateTextFormatter endDateFormatter = new LocalDateTextFormatter(true);
	private FormattedText startDateText;
	private FormattedText endDateText;
	
	private boolean locked = false;
	
	private final @NonNull AdapterImpl adapter = new AdapterImpl() {
		@Override
		public void notifyChanged(final Notification notification) {
			final Object newValue = notification.getNewValue();
			if(notification.isTouch()) {
				return;
			}
			if (notification.getFeature() == NominationsPackage.eINSTANCE.getNominationsParameters_StartDate()) {
				startDateText.setValue(newValue);
			} else if(notification.getFeature() == NominationsPackage.eINSTANCE.getNominationsParameters_EndDate()) {
				endDateText.setValue(newValue);
			}
		}
	};	
	
	public NominationDatesToolbarEditor(final String id, final @NonNull EditingDomain editingDomain, IScenarioEditingLocation jointModelEditor) {
		super(id);
		this.editingDomain = editingDomain;
		this.jointModelEditor = jointModelEditor;
		final NominationsModel nominationsModel = ScenarioModelUtil.getNominationsModel(jointModelEditor.getScenarioDataProvider());
		this.nominationsParameters = nominationsModel.getNominationParameters(); 
	}

	@Override
	protected Control createControl(final Composite parent) {
		final int minHeight = 40;
		final Composite pparent = new Composite(parent, SWT.NONE) {
			@Override
			protected void checkSubclass() {
			}

			@Override
			public Point getSize() {
				final Point p = super.getSize();
				return new Point(p.x, Math.max(minHeight, p.y));
			}

			@Override
			public void setSize(int width, int height) {
				super.setSize(width, Math.max(minHeight, height));
			}

			@Override
			public Point computeSize(int wHint, int hHint) {
				final Point p = super.computeSize(wHint, hHint);
				return new Point(p.x, Math.max(minHeight, p.y));
			}

			@Override
			public Point computeSize(int wHint, int hHint, boolean b) {
				final Point p = super.computeSize(wHint, hHint, b);
				return new Point(p.x, Math.max(minHeight, p.y));
			}
		};
		pparent.setLayout(GridLayoutFactory.fillDefaults().numColumns(10).equalWidth(false).spacing(3, 0).margins(0, 7).create());

		final Label lbl = new Label(pparent, SWT.NONE);
		lbl.setText("From:");
		lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(1000, -1).create());

		startDateText = createDateEditorControl(pparent, startDateFormatter, nominationsParameters::getStartDate, NominationsPackage.eINSTANCE.getNominationsParameters_StartDate());
		if (nominationsParameters.getStartDate() != null) {
			final LocalDate date = nominationsParameters.getStartDate();
			startDateText.setValue(date);
		}

		final Label lbl2 = new Label(pparent, SWT.NONE);
		lbl2.setText(" To: ");
		lbl2.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).create());

		endDateText = createDateEditorControl(pparent, endDateFormatter, nominationsParameters::getEndDate, NominationsPackage.eINSTANCE.getNominationsParameters_EndDate());
		if (nominationsParameters.getEndDate() != null) {
			final LocalDate date = nominationsParameters.getEndDate();
			endDateText.setValue(date);
		}

		//Listen to further changes
		nominationsParameters.eAdapters().add(adapter);

		//Make sure dispose gets called.
		startDateText.getControl().addDisposeListener(e -> this.dispose());
		
		return pparent;
	}

	@Override
	public void dispose() {
		if (nominationsParameters != null) {
			nominationsParameters.eAdapters().remove(adapter);
		}
		super.dispose();
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(final boolean locked) {
		this.locked = locked;
		startDateText.getControl().setEnabled(!locked);
		endDateText.getControl().setEnabled(!locked);
	}
	
	//TODO Find a better date picker control or fix the current one when entering the first character of the year and when deleting characters of the year.
	private FormattedText createDateEditorControl(final Composite parent, LocalDateTextFormatter dateFormatter, DateGetter dateGetter, EAttribute initValueEAttribute) {
		final FormattedText dateText = new FormattedText(parent);
		dateText.setFormatter(dateFormatter);
		dateText.getControl().addModifyListener(e -> {
			if (dateText.isValid()) {
				Object date = dateText.getValue();
				final LocalDate prevDateValue = dateGetter.getDate();
				if (!Objects.equals(date, prevDateValue)){
					if (date == null) {
						date = SetCommand.UNSET_VALUE;
					}
					final Command cmd = SetCommand.create(editingDomain, nominationsParameters, 
						initValueEAttribute, date);
					editingDomain.getCommandStack().execute(cmd);
				}
			}
		});
		dateText.getControl().setLayoutData(GridDataFactory.swtDefaults().minSize(90, -1).hint(90, 20).create());
		return dateText;		
	}

	private LocalDate getDate(FormattedText ft) {
		return (LocalDate)ft.getValue();
	}
	
	public LocalDate getStartDate() {
		return getDate(startDateText);
	}
	
	public LocalDate getEndDate() {
		return getDate(endDateText);
	}
}