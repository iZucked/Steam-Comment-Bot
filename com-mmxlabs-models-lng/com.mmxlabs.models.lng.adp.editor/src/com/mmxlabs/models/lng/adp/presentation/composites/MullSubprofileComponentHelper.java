/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.AllowedArrivalTimeRecord;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleTimeOfDayAttributeManipulator;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class MullSubprofileComponentHelper extends DefaultComponentHelper {

	public MullSubprofileComponentHelper() {
		super(ADPPackage.Literals.MULL_SUBPROFILE);

		addDefaultReadonlyEditor(ADPPackage.Literals.MULL_SUBPROFILE__INVENTORY);
		addEditor(ADPPackage.Literals.MULL_SUBPROFILE__ALLOWED_ARRIVAL_TIMES, createAllowedArrivalTimesEditor());
	}

	/**
	 * 
	 * @generated NOT
	 */
	protected Function<EClass, IInlineEditor> createAllowedArrivalTimesEditor() {
		return topClass -> {
			final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(true);
			b.withLabel("Allowed arrival times");
			b.withContentProvider(new ArrayContentProvider());
			b.withHeightHint(200);

			b.buildColumn("Period Start", ADPPackage.Literals.ALLOWED_ARRIVAL_TIME_RECORD__PERIOD_START) //
					.withWidth(120) //
					.withRMMaker((ed, rvp) -> new LocalDateAttributeManipulator(ADPPackage.Literals.ALLOWED_ARRIVAL_TIME_RECORD__PERIOD_START, ed)) //
					.build();

			b.buildColumn("Allowed Times", ADPPackage.Literals.ALLOWED_ARRIVAL_TIME_RECORD__ALLOWED_TIMES) //
					.withWidth(120) //
					.withRMMaker((ed, rvp) -> new MultipleTimeOfDayAttributeManipulator(ADPPackage.Literals.ALLOWED_ARRIVAL_TIME_RECORD__ALLOWED_TIMES, ed)) //
					.build();

			b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
				if (input instanceof @NonNull final MullSubprofile mullSubprofile) {
					final AllowedArrivalTimeRecord allowedArrivalTimeRecord = ADPFactory.eINSTANCE.createAllowedArrivalTimeRecord();
					final Command c = AddCommand.create(ch.getEditingDomain(), mullSubprofile, ADPPackage.Literals.MULL_SUBPROFILE__ALLOWED_ARRIVAL_TIMES, allowedArrivalTimeRecord);
					ch.handleCommand(c, allowedArrivalTimeRecord, ADPPackage.Literals.MULL_SUBPROFILE__ALLOWED_ARRIVAL_TIMES);
				}
			});
			// Delete action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {
				if (sel instanceof final IStructuredSelection ss && !ss.isEmpty()) {
//					final Command c = DeleteCommand.create(ch.getEditingDomain(), ss.toList());
					final Command c = RemoveCommand.create(ch.getEditingDomain(), input, ADPPackage.Literals.MULL_SUBPROFILE__ALLOWED_ARRIVAL_TIMES, ss.toList());
					ch.handleCommand(c, (MullSubprofile) input, ADPPackage.Literals.MULL_SUBPROFILE__ALLOWED_ARRIVAL_TIMES);
				}
			}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

			return b.build(ADPPackage.Literals.MULL_SUBPROFILE__ALLOWED_ARRIVAL_TIMES);
		};
	}

}
