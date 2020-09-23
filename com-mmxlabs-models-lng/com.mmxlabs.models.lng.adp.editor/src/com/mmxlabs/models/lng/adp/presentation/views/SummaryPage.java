/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.ProfileConstraint;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.impl.PeriodDistributionProfileConstraintImpl;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ecore.SafeAdapterImpl;

public class SummaryPage extends ADPComposite {

	private ADPEditorData editorData;

	private SashForm mainComposite;
	private ScrolledComposite rhsScrolledComposite;
	private Composite rhsComposite;
	private Runnable releaseAdaptersRunnable = null;

	private GridTableViewer purchasesViewer;

	private GridTableViewer salesViewer;

	/**
	 * Adapter to list to changes in number of slots on the cargo model.
	 */
	private final AdapterImpl cargoAdapter = new SafeAdapterImpl() {
		@Override
		public void safeNotifyChanged(final org.eclipse.emf.common.notify.Notification msg) {
			if (msg.isTouch()) {
				return;
			}
			if (msg.getFeature() == CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS || msg.getFeature() == CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS) {
				RunnerHelper.asyncExec(SummaryPage.this::refresh);
			}
		}
	};

	public SummaryPage(final Composite parent, final int style, final ADPEditorData editorData) {
		super(parent, style);
		this.editorData = editorData;
		this.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(0, 0).create());

		mainComposite = new SashForm(this, SWT.HORIZONTAL);
		mainComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GRAY));
		mainComposite.setSashWidth(5);

		mainComposite.setLayoutData(GridDataFactory.fillDefaults()//
				.grab(true, true)//
				// .align(SWT.CENTER, SWT.TOP)//
				// .span(1, 1) //
				.create());
		mainComposite.setLayout(GridLayoutFactory.fillDefaults()//
				.equalWidth(true) //
				.numColumns(2) //
				.spacing(0, 0) //
				.create());

		{
			purchasesViewer = new GridTableViewer(mainComposite);
			purchasesViewer.getGrid().setHeaderVisible(true);
			purchasesViewer.setContentProvider(new ArrayContentProvider() {
				@Override
				public Object[] getElements(final Object inputElement) {
					if (inputElement instanceof ADPModel) {
						final ADPModel adpModel = (ADPModel) inputElement;
						final Set<Contract> contracts = new HashSet<>();
						adpModel.getPurchaseContractProfiles().stream().map(p -> p.getContract()).forEach(c -> contracts.add(c));
						final List<ContractProfile<?, ?>> profiles = new LinkedList<>();
						profiles.addAll(adpModel.getPurchaseContractProfiles());
						final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(editorData.getScenarioDataProvider());
						for (final PurchaseContract c : commercialModel.getPurchaseContracts()) {
							if (!contracts.contains(c)) {
								final PurchaseContractProfile p = ADPFactory.eINSTANCE.createPurchaseContractProfile();
								p.setContract(c);
								p.setEnabled(false);
								profiles.add(p);
							}
						}
						return profiles.toArray();
					}
					return super.getElements(inputElement);
				}
			});
			
			createColumn(purchasesViewer, "Contract", (profile) -> profile.getContract() == null ? "<unknown>" : profile.getContract().getName());
			createColumn(purchasesViewer, "Num cargoes", (profile) -> Long.toString(editorData.getScenarioModel().getCargoModel().getLoadSlots().stream() //
					.filter(s -> profile.getContract() == s.getContract()).count()));
			
			createColumn(purchasesViewer, "Usable cargoes", (profile) -> {
				final EList<ProfileConstraint>  constraints = profile.getConstraints();
				if (constraints.isEmpty()) {
					return Long.toString(editorData.getScenarioModel().getCargoModel().getLoadSlots().stream() //
							.filter(s-> profile.getContract() == s.getContract()).count());
				} else {
					final ADPModel adpModel = editorData.getAdpModel();
					final Contract contract = profile.getContract();
					final Pair<YearMonth, YearMonth> adpPeriod = ADPModelUtil.getContractProfilePeriod(adpModel, contract);
					final YearMonth start = adpPeriod.getFirst();
					final YearMonth end = adpPeriod.getSecond();
					int months = Months.between(start, end);
					PeriodDistributionProfileConstraintImpl firstConstraint = (PeriodDistributionProfileConstraintImpl) constraints.get(0);
					OptionalInt explicitMaxCargo = firstConstraint.getDistributions().stream() //
							.filter(p -> p.isSetMaxCargoes() && p.getRange().size() == months) //
							.map(p -> new Pair<List<YearMonth>, Integer> (p.getRange().stream().sorted((a,b) -> a.compareTo(b)).collect(Collectors.toCollection(ArrayList::new)), p.getMaxCargoes())) //
							.filter(pair -> pair.getFirst().get(0).equals(start) && pair.getFirst().get(pair.getFirst().size()-1).equals(end.minusMonths(1))) //
							.mapToInt(pair -> pair.getSecond()) //
							.min();
					if (explicitMaxCargo.isPresent()) {
						return Integer.toString(explicitMaxCargo.getAsInt());
					} else {
						return Long.toString(editorData.getScenarioModel().getCargoModel().getLoadSlots().stream() //
								.filter(s-> profile.getContract() == s.getContract()).count());
					}
				}
			});
		}
		{
			salesViewer = new GridTableViewer(mainComposite);
			salesViewer.getGrid().setHeaderVisible(true);
			salesViewer.setContentProvider(new ArrayContentProvider() {
				@Override
				public Object[] getElements(final Object inputElement) {
					if (inputElement instanceof ADPModel) {
						final ADPModel adpModel = (ADPModel) inputElement;
						final Set<Contract> contracts = new HashSet<>();
						adpModel.getSalesContractProfiles().stream().map(p -> p.getContract()).forEach(c -> contracts.add(c));
						final List<ContractProfile<?, ?>> profiles = new LinkedList<>();
						profiles.addAll(adpModel.getSalesContractProfiles());
						final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(editorData.getScenarioDataProvider());
						for (final SalesContract c : commercialModel.getSalesContracts()) {
							if (!contracts.contains(c)) {
								final SalesContractProfile p = ADPFactory.eINSTANCE.createSalesContractProfile();
								p.setContract(c);
								p.setEnabled(false);
								profiles.add(p);
							}
						}
						return profiles.toArray();
					}
					return super.getElements(inputElement);
				}
			});

			createColumn(salesViewer, "Contract", (profile) -> profile.getContract().getName());
			createColumn(salesViewer, "Cargoes", (profile) -> Long.toString(editorData.getScenarioModel().getCargoModel().getDischargeSlots().stream() //
					.filter(s -> profile.getContract() == s.getContract()).count()));
		}
		{

			rhsScrolledComposite = new ScrolledComposite(mainComposite, SWT.V_SCROLL | SWT.V_SCROLL);
			rhsScrolledComposite.setLayoutData(GridDataFactory.fillDefaults()//
					.grab(false, true)//
					.hint(200, SWT.DEFAULT) //
					// .span(1, 1) //
					.align(SWT.FILL, SWT.FILL).create());

			rhsScrolledComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

			rhsScrolledComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
			rhsScrolledComposite.setExpandHorizontal(true);
			rhsScrolledComposite.setExpandVertical(true);
			// lhsScrolledComposite.setMinSize(400, 400);
			rhsComposite = new Composite(rhsScrolledComposite, SWT.NONE);
			// centralComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			// centralComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			rhsScrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			rhsScrolledComposite.setContent(rhsComposite);

			rhsComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
			{
				// Preview Table with generated options

			}
		}

	}

	@Override
	public void refresh() {
		if (purchasesViewer != null) {
			purchasesViewer.refresh();
		}
		if (salesViewer != null) {
			salesViewer.refresh();
		}
	}

	@Override
	public synchronized void updateRootModel(@Nullable final LNGScenarioModel scenarioModel, @Nullable final ADPModel adpModel) {

		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}

		if (adpModel != null) {
			purchasesViewer.setInput(adpModel);
			salesViewer.setInput(adpModel);
		} else {
			purchasesViewer.setInput(null);
			salesViewer.setInput(null);
		}

		if (scenarioModel != null) {
			final CargoModel cargoModel = scenarioModel.getCargoModel();
			releaseAdaptersRunnable = () -> {
				cargoModel.eAdapters().remove(cargoAdapter);
			};
			cargoModel.eAdapters().add(cargoAdapter);
		}
	}

	@Override
	public void dispose() {

		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}

		super.dispose();
	}

	// private void updateDetailPaneInput(final EObject object) {
	// EObject target = null;
	// if (editorData.getAdpModel() != null) {
	// target = editorData.getAdpModel().getSpotMarketsProfile();
	// //
	// previewViewer.setInput(editorData.getAdpModel().getFleetProfile().getVesselAvailabilities());
	// previewViewer.setInput(null);
	// }
	//
	// detailComposite.setInput(target);
	//
	// }

	private void createColumn(final GridTableViewer viewer, final String name, final Function<ContractProfile<?, ?>, String> labelProvider) {
		final GridViewerColumn col = new GridViewerColumn(viewer, SWT.NONE);
		col.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element instanceof ContractProfile<?, ?>) {
					return labelProvider.apply((ContractProfile<?, ?>) element);
				}
				return "";
			}

		});
		col.getColumn().setWidth(100);
		col.getColumn().setText(name);
	}

}
