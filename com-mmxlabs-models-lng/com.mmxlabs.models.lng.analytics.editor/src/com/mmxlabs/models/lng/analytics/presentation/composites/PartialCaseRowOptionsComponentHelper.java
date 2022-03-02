/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.time.LocalDate;
import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.LocalDateTimeHolder;
import com.mmxlabs.models.lng.analytics.PartialCaseRowOptions;
import com.mmxlabs.models.lng.analytics.ui.editors.FuelChoiceInlineEditorHelper;
import com.mmxlabs.models.lng.analytics.ui.editors.RouteChoiceInlineEditorHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateTimeAttributeManipulator;

/**
 * A component helper for PartialCaseRowOptions instances
 *
 * @generated NOT
 */
public class PartialCaseRowOptionsComponentHelper extends DefaultComponentHelper {

	public PartialCaseRowOptionsComponentHelper() {
		super(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS);

		addEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES,
				topClass -> RouteChoiceInlineEditorHelper.createRouteChoiceEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES));

		addEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__BALLAST_ROUTES,
				topClass -> RouteChoiceInlineEditorHelper.createRouteChoiceEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__BALLAST_ROUTES));

		addEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES,
				topClass -> FuelChoiceInlineEditorHelper.createFuelChoiceEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES));
		addEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICES,
				topClass -> FuelChoiceInlineEditorHelper.createFuelChoiceEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICES));

		addEditor(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES, createDatesEditor(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES, "Dates"));
		addEditor(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES, createDatesEditor(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES, "Dates"));
	}

	protected Function<EClass, IInlineEditor> createDatesEditor(final EStructuralFeature feature, String label) {
		return topClass -> {
			TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(false);
			b.withLabel(label);
			b.withHeightHint(100);
			b.withContentProvider(new ArrayContentProvider());

			b.withComparator(new ViewerComparator() {
				@Override
				public int compare(final Viewer viewer, final Object e1, final Object e2) {

					final LocalDateTimeHolder b1 = (LocalDateTimeHolder) e1;
					final LocalDateTimeHolder b2 = (LocalDateTimeHolder) e2;
					if (b1.getDateTime() == null) {
						return -1;
					}
					if (b2.getDateTime() == null) {
						return 1;
					}

					return b1.getDateTime().compareTo(b2.getDateTime());
				}
			});

			b.buildColumn("", feature).withWidth(100) //
					.withRMMaker((ch, rvp) -> new LocalDateTimeAttributeManipulator(AnalyticsPackage.Literals.LOCAL_DATE_TIME_HOLDER__DATE_TIME, ch)) //
					.build();

			b.withAction("Add", (input, ch, sel) -> {
				PartialCaseRowOptions options = (PartialCaseRowOptions) input;
				LocalDateTimeHolder holder = AnalyticsFactory.eINSTANCE.createLocalDateTimeHolder();
				holder.setDateTime(LocalDate.now().atStartOfDay());
				Command c = AddCommand.create(ch.getEditingDomain(), options, feature, holder);
				ch.handleCommand(c, options, feature);

			});
			b.withAction("Delete", (input, ch, sel) -> {

				if (sel instanceof IStructuredSelection ss && !ss.isEmpty()) {
					PartialCaseRowOptions options = (PartialCaseRowOptions) input;
					Command c = RemoveCommand.create(ch.getEditingDomain(), options, feature, ss.toList());
					ch.handleCommand(c, options, feature);
				}
			}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

			return (b.build(feature));
		};

	}
}