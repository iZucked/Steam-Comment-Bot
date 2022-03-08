/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.port.ui.editorpart.MultiplePortReferenceManipulator;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
import com.mmxlabs.models.ui.tabular.SimpleCellRenderer;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;

/**
 * A component helper for SuezCanalTariff instances
 *
 * @generated NOT
 */
public class SuezCanalTariffComponentHelper extends DefaultComponentHelper {
	public SuezCanalTariffComponentHelper() {
		super(PricingPackage.Literals.SUEZ_CANAL_TARIFF);

		addEditor(PricingPackage.Literals.SUEZ_CANAL_TARIFF__BANDS, createBandsEditor());
		addEditor(PricingPackage.Literals.SUEZ_CANAL_TARIFF__TUG_BANDS, createTugBandsEditor());
		addEditor(PricingPackage.Literals.SUEZ_CANAL_TARIFF__ROUTE_REBATES, createRouteRebatesEditor());

	}

	/**
	 * Create the editor for the bands feature on SuezCanalTariff
	 *
	 * @generated NOT
	 */
	protected Function<EClass, IInlineEditor> createBandsEditor() {
		return topClass -> {
			final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(true);
			b.withLabel("Pricing bands based on SCNT");
			b.withContentProvider(new ArrayContentProvider());

			b.withComparator(new ViewerComparator() {
				@Override
				public int compare(final Viewer viewer, final Object e1, final Object e2) {

					final SuezCanalTariffBand b1 = (SuezCanalTariffBand) e1;
					final SuezCanalTariffBand b2 = (SuezCanalTariffBand) e2;
					//
					final int v1 = b1 == null ? -1 : (b1.isSetBandEnd() ? b1.getBandEnd() : Integer.MAX_VALUE);
					final int v2 = b2 == null ? 2 : (b2.isSetBandEnd() ? b2.getBandEnd() : Integer.MAX_VALUE);
					//
					return Integer.compare(v1, v2);
				}
			});

			b.buildColumn("", PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__BAND_START).withRenderer(new SimpleCellRenderer() {

				@Override
				public @Nullable String render(final Object element) {
					String label = "";
					if (element instanceof SuezCanalTariffBand band) {
						if (!band.isSetBandStart()) {
							label = String.format("First %,d", band.getBandEnd());
						} else if (!band.isSetBandEnd()) {
							label = String.format("Over %,d", band.getBandStart());
						} else {
							final int diff = band.getBandEnd() - band.getBandStart();
							label = String.format("Next %,d", diff);
						}
					}
					return label;
				}
			}) //
					.withWidth(100)//
					.build();

			b.buildColumn("Laden", PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__LADEN_TARIFF) //
					.withWidth(75) //
					.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__LADEN_TARIFF, ed)) //
					.build();

			b.buildColumn("Ballast", PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__BALLAST_TARIFF) //
					.withWidth(75) //
					.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__BALLAST_TARIFF, ed)) //
					.build();

			return b.build(PricingPackage.Literals.SUEZ_CANAL_TARIFF__BANDS);
		};
	}

	/**
	 * Create the editor for the tugBands feature on SuezCanalTariff
	 *
	 * @generated NOT
	 */
	protected Function<EClass, IInlineEditor> createTugBandsEditor() {
		return topClass -> {
			final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(true);
			b.withLabel("Tugs by vessel capacity");
			b.withContentProvider(new ArrayContentProvider());

			b.withComparator(new ViewerComparator() {
				@Override
				public int compare(final Viewer viewer, final Object e1, final Object e2) {

					final SuezCanalTugBand b1 = (SuezCanalTugBand) e1;
					final SuezCanalTugBand b2 = (SuezCanalTugBand) e2;

					final int v1 = b1 == null ? -1 : (b1.isSetBandEnd() ? b1.getBandEnd() : Integer.MAX_VALUE);
					final int v2 = b2 == null ? 2 : (b2.isSetBandEnd() ? b2.getBandEnd() : Integer.MAX_VALUE);

					return Integer.compare(v1, v2);
				}
			});

			b.buildColumn("", PricingPackage.Literals.SUEZ_CANAL_TUG_BAND__BAND_START).withRenderer(new SimpleCellRenderer() {

				@Override
				public @Nullable String render(final Object element) {
					String label = "";
					if (element instanceof final SuezCanalTugBand band) {
						if (!band.isSetBandStart()) {
							label = String.format("Up to %,d", band.getBandEnd());
						} else if (!band.isSetBandEnd()) {
							label = String.format("Over %,d", band.getBandStart());
						} else {
							final int diff = band.getBandEnd() - band.getBandStart();
							label = String.format("Next %,d", diff);
						}
					}
					return label;
				}
			}) //
					.withWidth(100)//
					.build();

			b.buildColumn("Tugs", PricingPackage.Literals.SUEZ_CANAL_TUG_BAND__TUGS) //
					.withWidth(100) //
					.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(PricingPackage.Literals.SUEZ_CANAL_TUG_BAND__TUGS, ed)) //
					.build();

			return b.build(PricingPackage.Literals.SUEZ_CANAL_TARIFF__TUG_BANDS);
		};
	}

	/**
	 * Create the editor for the routeRebates feature on SuezCanalTariff
	 *
	 * @generated NOT
	 */
	protected Function<EClass, IInlineEditor> createRouteRebatesEditor() {
		return topClass -> {
			final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(true);
			b.withLabel("Rebate between ports");
			b.withContentProvider(new ArrayContentProvider());
			b.withHeightHint(100);
			b.buildColumn("", PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__FROM)
					.withRMMaker((ed, rvp) -> new MultiplePortReferenceManipulator(PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__FROM, ed,
							rvp.getReferenceValueProvider(PricingPackage.eINSTANCE.getSuezCanalRouteRebate(), PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__FROM),
							MMXCorePackage.eINSTANCE.getNamedObject_Name())) //
					.withWidth(150)//
					.build();

			b.buildColumn("", PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO)
					.withRMMaker((ed, rvp) -> new MultiplePortReferenceManipulator(PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO, ed,
							rvp.getReferenceValueProvider(PricingPackage.eINSTANCE.getSuezCanalRouteRebate(), PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO),
							MMXCorePackage.eINSTANCE.getNamedObject_Name())) //
					.withWidth(150)//
					.build();

			b.buildColumn("Rebate %", PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__REBATE) //
					.withWidth(75) //
					.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__REBATE, ed)) //
					.build();

			// Add action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
				final SuezCanalTariff tariff = ScenarioModelUtil.getCostModel((LNGScenarioModel) ch.getModelReference().getInstance()).getSuezCanalTariff();
				final SuezCanalRouteRebate rebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();
				final Command c = AddCommand.create(ch.getEditingDomain(), tariff, PricingPackage.Literals.SUEZ_CANAL_TARIFF__ROUTE_REBATES, rebate);
				ch.handleCommand(c, tariff, PricingPackage.Literals.SUEZ_CANAL_TARIFF__ROUTE_REBATES);

			});
			// Delete action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {
				if (sel instanceof IStructuredSelection ss && !ss.isEmpty()) {
					final SuezCanalTariff tariff = ScenarioModelUtil.getCostModel((LNGScenarioModel) ch.getModelReference().getInstance()).getSuezCanalTariff();
					final Command c = RemoveCommand.create(ch.getEditingDomain(), tariff, PricingPackage.Literals.SUEZ_CANAL_TARIFF__ROUTE_REBATES, ss.toList());
					ch.handleCommand(c, tariff, PricingPackage.Literals.SUEZ_CANAL_TARIFF__ROUTE_REBATES);
				}
			}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

			return b.build(PricingPackage.Literals.SUEZ_CANAL_TARIFF__ROUTE_REBATES);
		};
	}

	@Override
	protected void sortEditors(List<IInlineEditor> editors) {
		// Sub classes can sort the editor list prior to rendering
		List<EStructuralFeature> orderedFeatures = Lists.newArrayList( //
				PricingPackage.Literals.SUEZ_CANAL_TARIFF__TUG_COST, //
				PricingPackage.Literals.SUEZ_CANAL_TARIFF__FIXED_COSTS, //
				PricingPackage.Literals.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR, //
				PricingPackage.Literals.SUEZ_CANAL_TARIFF__SDR_TO_USD //

		);
		// Reverse the list so that we can move the editors to the head of the list
		Collections.reverse(orderedFeatures);
		for (var feature : orderedFeatures) {
			for (var editor : editors) {
				if (editor.getFeature() == feature) {
					editors.remove(editor);
					editors.add(0, editor);
					break;
				}
			}
		}
	}

}