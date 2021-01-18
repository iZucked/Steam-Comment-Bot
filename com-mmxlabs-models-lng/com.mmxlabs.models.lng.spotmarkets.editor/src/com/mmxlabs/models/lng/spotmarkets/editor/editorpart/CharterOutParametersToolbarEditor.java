/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.editorpart;

import java.time.LocalDate;
import java.util.Objects;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.ui.date.LocalDateTextFormatter;
import com.mmxlabs.rcp.common.ecore.SafeAdapterImpl;

/**
 * Based on PromptToolbarEditor
 * @author FM
 *
 */
public class CharterOutParametersToolbarEditor extends ControlContribution {


	private EditingDomain editingDomain;
	private CharterOutMarketParameters charterOutMarketParameters;
	
	private LocalDateTextFormatter startDateFormatter = new LocalDateTextFormatter(true);
	private LocalDateTextFormatter endDateFormatter = new LocalDateTextFormatter(true);
	private FormattedText startDateText;
	private FormattedText endDateText;

	private boolean locked = false;

	private final @NonNull AdapterImpl adapter = new SafeAdapterImpl() {
		@Override
		public void safeNotifyChanged(final Notification notification) {
			final Object newValue = notification.getNewValue();
			if(notification.isTouch()) {
				return;
			}
			if (notification.getFeature() == SpotMarketsPackage.eINSTANCE.getCharterOutMarketParameters_CharterOutStartDate()) {
				startDateText.setValue(newValue);
			} else if(notification.getFeature() == SpotMarketsPackage.eINSTANCE.getCharterOutMarketParameters_CharterOutEndDate()) {
				endDateText.setValue(newValue);
			}
		}
	};

	public CharterOutParametersToolbarEditor(final String id, final @NonNull EditingDomain editingDomain, final @NonNull LNGScenarioModel scenarioModel) {
		super(id);
		this.editingDomain = editingDomain;
		final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);
		this.charterOutMarketParameters = spotMarketsModel.getCharterOutMarketParameters();
		if (this.charterOutMarketParameters == null) {
			charterOutMarketParameters = SpotMarketsFactory.eINSTANCE.createCharterOutMarketParameters();
			final Command cmd = SetCommand.create(editingDomain, spotMarketsModel,//
					SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS, charterOutMarketParameters);
			editingDomain.getCommandStack().execute(cmd);
		}
	}

	@Override
	protected Control createControl(final Composite ppparent) {

		int minHeight = 40;
		final Composite pparent = new Composite(ppparent, SWT.NONE) {
			@Override
			protected void checkSubclass() {
			}

			@Override
			public Point getSize() {
				Point p = super.getSize();
				return new Point(p.x, Math.max(minHeight, p.y));
			}

			@Override
			public void setSize(int width, int height) {
				super.setSize(width, Math.max(minHeight, height));
			}

			@Override
			public Point computeSize(int wHint, int hHint) {
				Point p = super.computeSize(wHint, hHint);
				return new Point(p.x, Math.max(minHeight, p.y));
			}

			@Override
			public Point computeSize(int wHint, int hHint, boolean b) {
				Point p = super.computeSize(wHint, hHint, b);
				return new Point(p.x, Math.max(minHeight, p.y));
			};
		};
		pparent.setLayout(GridLayoutFactory.fillDefaults().numColumns(10).equalWidth(false).spacing(3, 0).margins(0, 7).create());
		
		{
			final Label lbl = new Label(pparent, SWT.NONE);
			lbl.setText("Charter out dates. From:");
			lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(1000, -1).create());

			startDateText = createStartDateEditorControl(pparent);
			{
				if (charterOutMarketParameters.getCharterOutStartDate() != null) {
					final LocalDate date = charterOutMarketParameters.getCharterOutStartDate();
					startDateText.setValue(date);
				}
			}

			final Label lbl2 = new Label(pparent, SWT.NONE);
			lbl2.setText(" to ");
			lbl2.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).create());

			endDateText = createEndDateEditorControl(pparent);
			{
				if (charterOutMarketParameters.getCharterOutEndDate() != null) {
					final LocalDate date = charterOutMarketParameters.getCharterOutEndDate();
					endDateText.setValue(date);
				}
			}
		}

		// Listen to further changes
		charterOutMarketParameters.eAdapters().add(adapter);

		return pparent;
	}

	@Override
	public void dispose() {
		if (charterOutMarketParameters != null) {
			charterOutMarketParameters.eAdapters().remove(adapter);
			charterOutMarketParameters = null;
		}
		editingDomain = null;
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

	protected FormattedText createStartDateEditorControl(final Composite parent) {
		startDateText = new FormattedText(parent);

		startDateText.setFormatter(startDateFormatter);

		startDateText.getControl().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (startDateText.isValid()) {
					Object startDate = startDateText.getValue();
					if (!Objects.equals(startDate, charterOutMarketParameters.getCharterOutStartDate())){
						if (startDate == null) {
							startDate = SetCommand.UNSET_VALUE;
						}
						final Command cmd = SetCommand.create(editingDomain, charterOutMarketParameters, 
							SpotMarketsPackage.eINSTANCE.getCharterOutMarketParameters_CharterOutStartDate(), startDate);
						editingDomain.getCommandStack().execute(cmd);
					}
				}
			}
		});
		startDateText.getControl().setLayoutData(GridDataFactory.swtDefaults().minSize(90, -1).hint(90, 20).create());
		return startDateText;
	}
	
	protected FormattedText createEndDateEditorControl(final Composite parent) {
		endDateText = new FormattedText(parent);

		endDateText.setFormatter(endDateFormatter);

		endDateText.getControl().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (endDateText.isValid()) {
					Object endDate = endDateText.getValue();
					if (!Objects.equals(endDate, charterOutMarketParameters.getCharterOutEndDate())){
						if (endDate == null) {
							endDate = SetCommand.UNSET_VALUE;
						}
						final Command cmd = SetCommand.create(editingDomain, charterOutMarketParameters, 
							SpotMarketsPackage.eINSTANCE.getCharterOutMarketParameters_CharterOutEndDate(), endDate);
						editingDomain.getCommandStack().execute(cmd);
					}
				}
			}
		});
		endDateText.getControl().setLayoutData(GridDataFactory.swtDefaults().minSize(90, -1).hint(90, 20).create());
		return endDateText;
	}
}