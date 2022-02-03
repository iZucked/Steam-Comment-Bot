/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

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
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.tabular.SimpleCellRenderer;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;

/**
 * A component helper for SuezCanalTariff instances
 *
 * @generated
 */
public class SuezCanalTariffComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SuezCanalTariffComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SuezCanalTariffComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}

	/**
	 * add editors to a composite, using SuezCanalTariff as the supertype
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.SUEZ_CANAL_TARIFF);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_bandsEditor(detailComposite, topClass);
		add_tugBandsEditor(detailComposite, topClass);
		add_routeRebatesEditor(detailComposite, topClass);
		add_tugCostEditor(detailComposite, topClass);
		add_fixedCostsEditor(detailComposite, topClass);
		add_discountFactorEditor(detailComposite, topClass);
		add_sdrToUSDEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the bands feature on SuezCanalTariff
	 *
	 * @generated NOT
	 */
	protected void add_bandsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {

		TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
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
			public @Nullable String render(Object element) {
				String label = "";
				if (element instanceof SuezCanalTariffBand) {
					final SuezCanalTariffBand band = (SuezCanalTariffBand) element;
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

		detailComposite.addInlineEditor(b.build(PricingPackage.Literals.SUEZ_CANAL_TARIFF__BANDS));
	}

	/**
	 * Create the editor for the tugBands feature on SuezCanalTariff
	 *
	 * @generated NOT
	 */
	protected void add_tugBandsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
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
			public @Nullable String render(Object element) {
				String label = "";
				if (element instanceof SuezCanalTugBand) {
					final SuezCanalTugBand band = (SuezCanalTugBand) element;
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

		detailComposite.addInlineEditor(b.build(PricingPackage.Literals.SUEZ_CANAL_TARIFF__TUG_BANDS));
	}

	/**
	 * Create the editor for the routeRebates feature on SuezCanalTariff
	 *
	 * @generated NOT
	 */
	protected void add_routeRebatesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {

		TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
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

		b.buildColumn("", PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO).withRMMaker((ed, rvp) -> new MultiplePortReferenceManipulator(PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO, ed,
				rvp.getReferenceValueProvider(PricingPackage.eINSTANCE.getSuezCanalRouteRebate(), PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO), MMXCorePackage.eINSTANCE.getNamedObject_Name())) //
				.withWidth(150)//
				.build();

		b.buildColumn("Rebate %", PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__REBATE) //
				.withWidth(75) //
				.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__REBATE, ed)) //
				.build();

		b.withAction("Add", (input, ch, sel) -> {
			SuezCanalTariff tariff = ScenarioModelUtil.getCostModel((LNGScenarioModel) ch.getModelReference().getInstance()).getSuezCanalTariff();
			SuezCanalRouteRebate rebate = PricingFactory.eINSTANCE.createSuezCanalRouteRebate();
			Command c = AddCommand.create(ch.getEditingDomain(), tariff, PricingPackage.Literals.SUEZ_CANAL_TARIFF__ROUTE_REBATES, rebate);
			ch.handleCommand(c, tariff, PricingPackage.Literals.SUEZ_CANAL_TARIFF__ROUTE_REBATES);

		});
		b.withAction("Delete", (input, ch, sel) -> {

			if (sel instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) sel;
				if (!ss.isEmpty()) {
					SuezCanalTariff tariff = ScenarioModelUtil.getCostModel((LNGScenarioModel) ch.getModelReference().getInstance()).getSuezCanalTariff();
					Command c = RemoveCommand.create(ch.getEditingDomain(), tariff, PricingPackage.Literals.SUEZ_CANAL_TARIFF__ROUTE_REBATES, ss.toList());
					ch.handleCommand(c, tariff, PricingPackage.Literals.SUEZ_CANAL_TARIFF__ROUTE_REBATES);
				}
			}
		}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

		detailComposite.addInlineEditor(b.build(PricingPackage.Literals.SUEZ_CANAL_TARIFF__ROUTE_REBATES));

	}

	/**
	 * Create the editor for the tugCost feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_tugCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__TUG_COST));
	}

	/**
	 * Create the editor for the fixedCosts feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_fixedCostsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__FIXED_COSTS));
	}

	/**
	 * Create the editor for the discountFactor feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_discountFactorEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR));
	}

	/**
	 * Create the editor for the sdrToUSD feature on SuezCanalTariff
	 *
	 * @generated
	 */
	protected void add_sdrToUSDEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF__SDR_TO_USD));
	}
}