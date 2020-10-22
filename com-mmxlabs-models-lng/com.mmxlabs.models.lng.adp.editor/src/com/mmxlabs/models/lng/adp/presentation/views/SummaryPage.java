/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
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

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.MaxCargoConstraint;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;
import com.mmxlabs.models.lng.adp.ProfileConstraint;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
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
						adpModel.getPurchaseContractProfiles().stream().map(PurchaseContractProfile::getContract).forEach(contracts::add);
						final List<Object> profiles = new LinkedList<>();
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
						profiles.add(profiles);
						return profiles.toArray();
					}
					return super.getElements(inputElement);
				}
			});
			
			createColumn(purchasesViewer, "Contract", (profile, acc) -> profile.getContract() == null ? "<unknown>" : profile.getContract().getName());
			createColumn(purchasesViewer, "Generated cargoes", (profile, acc) -> {
					long generatedCargoes = editorData.getScenarioModel().getCargoModel().getLoadSlots().stream() //
							.filter(s -> profile.getContract() == s.getContract()).count();
					acc.accumulate(generatedCargoes);
					return Long.toString(generatedCargoes);
				});
			
			createColumn(purchasesViewer, "Max cargoes", (profile, acc) -> {
				final Long maxCargoes;
				final List<ProfileConstraint> constraints = profile.getConstraints();
				if (constraints.isEmpty()) {
					maxCargoes = editorData.getScenarioModel().getCargoModel().getLoadSlots().stream() //
							.filter(s -> profile.getContract() == s.getContract()) //
							.count();
				} else {
					final Map<YearMonth, Long> variableBounds = new HashMap<>();
					editorData.getScenarioModel().getCargoModel().getLoadSlots().stream() //
							.filter(slot -> profile.getContract() == slot.getContract()) //
							.forEach(slot -> {
								final YearMonth ym = YearMonth.from(slot.getWindowStart());
								final Long currentBound = variableBounds.get(ym);
								if (currentBound == null) {
									variableBounds.put(ym, 1L);
								} else {
									variableBounds.put(ym, currentBound+1L);
								}
							});
					Long solverBound = calculateConstrainedProfileBound(variableBounds, constraints);
					maxCargoes = solverBound != null ? solverBound : calculateNaiveSummaryBound(variableBounds, constraints);
				}
				acc.accumulate(maxCargoes);
				return maxCargoes.toString();
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
						adpModel.getSalesContractProfiles().stream().map(SalesContractProfile::getContract).forEach(contracts::add);
						final List<Object> profiles = new LinkedList<>();
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
						profiles.add(profiles);
						return profiles.toArray();
					}
					return super.getElements(inputElement);
				}
			});

			createColumn(salesViewer, "Contract", (profile, acc) -> profile.getContract().getName());
			createColumn(salesViewer, "Generated cargoes", (profile, acc) -> {
				long generatedCargoes = editorData.getScenarioModel().getCargoModel().getDischargeSlots().stream() //
						.filter(s -> profile.getContract() == s.getContract()).count();
				acc.accumulate(generatedCargoes);
				return Long.toString(generatedCargoes);
				});
			createColumn(salesViewer, "Max cargoes", (profile, acc) -> {
				final List<ProfileConstraint> constraints = profile.getConstraints();
				final Long maxCargoes;
				if (constraints.isEmpty()) {
					maxCargoes = editorData.getScenarioModel().getCargoModel().getDischargeSlots().stream() //
							.filter(s -> profile.getContract() == s.getContract()) //
							.count();
				} else {
					final Map<YearMonth, Long> variableBounds = new HashMap<>();
					editorData.getScenarioModel().getCargoModel().getDischargeSlots().stream() //
							.filter(slot -> profile.getContract() == slot.getContract()) //
							.forEach(slot -> {
								final YearMonth ym = YearMonth.from(slot.getWindowStart());
								final Long currentBound = variableBounds.get(ym);
								if (currentBound == null) {
									variableBounds.put(ym, 1L);
								} else {
									variableBounds.put(ym, currentBound+1L);
								}
							});
					final Long solverBound = calculateConstrainedProfileBound(variableBounds, constraints);
					maxCargoes = solverBound != null ? solverBound : calculateNaiveSummaryBound(variableBounds, constraints);
				}
				acc.accumulate(maxCargoes);
				return maxCargoes.toString();
			});
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
	
	private long calculateNaiveSummaryBound(final Map<YearMonth, Long> variableBounds, final List<ProfileConstraint> constraints) {
		final int numVars = variableBounds.size();
		Long fullCoverBound = Long.MAX_VALUE;
		for (final ProfileConstraint constraint : constraints) {
			if (constraint instanceof PeriodDistributionProfileConstraint) {
				PeriodDistributionProfileConstraint profileConstraint = (PeriodDistributionProfileConstraint) constraint;
				for (final PeriodDistribution distribution : profileConstraint.getDistributions()) //
					if (distribution.isSetMaxCargoes()) {
						final List<YearMonth> range = distribution.getRange();
						int rangeSize = range.size();
						final long upperbound = distribution.getMaxCargoes();
						if (rangeSize == 1) {
							final YearMonth ym = range.get(0);
							if (variableBounds.get(ym) > upperbound) {
								variableBounds.put(ym, upperbound);
							}
						} else if (rangeSize == numVars && upperbound < fullCoverBound) {
							fullCoverBound = upperbound;
						}
					}
			} else if (constraint instanceof MaxCargoConstraint) {
				final MaxCargoConstraint maxCargoConstraint = (MaxCargoConstraint) constraint;
				final IntervalType intervalType = maxCargoConstraint.getIntervalType();
				final long maxCargoes = maxCargoConstraint.getMaxCargoes();
				if (intervalType == IntervalType.YEARLY) {
					if (maxCargoes < fullCoverBound) {
						fullCoverBound = maxCargoes;
					}
				} else if (intervalType == IntervalType.MONTHLY) {
					variableBounds.entrySet().stream() //
							.filter(mapEntry -> mapEntry.getValue() > maxCargoes) //
							.forEach(mapEntry -> mapEntry.setValue(maxCargoes));
				}
			}
		}
		Long simpleBound = 0L;
		for (final Long variableBound : variableBounds.values()) {
			simpleBound += variableBound;
		}
		return Long.min(simpleBound, fullCoverBound);
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
			releaseAdaptersRunnable = () -> cargoModel.eAdapters().remove(cargoAdapter);
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

	private void createColumn(final GridTableViewer viewer, final String name, final BiFunction<ContractProfile<?, ?>, LongAccumulator, String> labelProvider) {
		final GridViewerColumn col = new GridViewerColumn(viewer, SWT.NONE);
		final LongAccumulator acc = new LongAccumulator(Long::sum, 0);
		col.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element instanceof ContractProfile<?, ?>) {
					return labelProvider.apply((ContractProfile<?, ?>) element, acc);
				} else if (element instanceof List) {
					if (name.equals("Contract")) {
						return "Total";
					} else {
						return Long.toString(acc.get());
					}
				}
				return "";
			}
		});
		col.getColumn().setWidth(100);
		col.getColumn().setText(name);
	}
	
	private Long calculateConstrainedProfileBound(final Map<YearMonth, Long> variableBounds, final List<ProfileConstraint> constraints) {
		Object solver = createSolver();
		if (solver != null) {
			MPSolver mpSolver = (MPSolver) solver;
			try {
				buildProblem(mpSolver, variableBounds, constraints);
			} catch (Exception | Error e) {
				return null;
			}
			if (mpSolver.numConstraints() == 0) {
				return variableBounds.values().stream().collect(Collectors.summingLong(Long::longValue));
			} else {
				try {
					final MPSolver.ResultStatus resultStatus = mpSolver.solve();
					if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
						final Double bestBound = Math.floor(mpSolver.objective().bestBound());
						return bestBound.longValue();
					} else {
						final Double objVal = mpSolver.objective().value();
						return objVal.longValue();
					}
				} catch (Exception | Error e) {
					// or tools error, skip
				}
			}
		}
		return null;
	}
	
	private void buildProblem(MPSolver solver, Map<YearMonth, Long> variableBounds, final List<ProfileConstraint> constraints) {
		final HashMap<YearMonth, MPVariable> dateToMPVarMap = new HashMap<>();
		MPObjective obj = solver.objective();
		obj.setMaximization();
		variableBounds.forEach((ym, upperbound) -> {
			MPVariable mpVar = solver.makeIntVar(0, upperbound, "");
			dateToMPVarMap.put(ym, mpVar);
			obj.setCoefficient(mpVar, 1);
		});
		
		for (final ProfileConstraint constraint : constraints) {
			if (constraint instanceof PeriodDistributionProfileConstraint) {
				((PeriodDistributionProfileConstraint) constraint).getDistributions().stream() //
						.filter(PeriodDistribution::isSetMaxCargoes) //
						.forEach(profileConstraint -> {
							final List<YearMonth> range = profileConstraint.getRange();
							if (range.size() == 1) {
								final YearMonth ym = range.get(0);
								final long upperbound = profileConstraint.getMaxCargoes();
								if (variableBounds.get(ym) > upperbound) {
									variableBounds.put(ym, upperbound);
									dateToMPVarMap.get(ym).setUb(upperbound);
								}
							} else {
								final MPConstraint mpConstraint = solver.makeConstraint(0, profileConstraint.getMaxCargoes());
								range.forEach(ym -> mpConstraint.setCoefficient(dateToMPVarMap.get(ym), 1));
							}
						});
			} else if (constraint instanceof MaxCargoConstraint) {
				final MaxCargoConstraint maxCargoConstraint = (MaxCargoConstraint) constraint;
				final IntervalType intervalType = maxCargoConstraint.getIntervalType();
				final long maxCargoes = maxCargoConstraint.getMaxCargoes();
				if (intervalType == IntervalType.YEARLY) {
					final MPConstraint mpConstraint = solver.makeConstraint(0, maxCargoes);
					dateToMPVarMap.values().forEach(mpVar -> mpConstraint.setCoefficient(mpVar, 1));
				} else if (intervalType == IntervalType.MONTHLY) {
					variableBounds.entrySet().stream() //
							.filter(mapEntry -> mapEntry.getValue() > maxCargoes) //
							.forEach(mapEntry -> {
								mapEntry.setValue(maxCargoes);
								dateToMPVarMap.get(mapEntry.getKey()).setUb(maxCargoes);
							});
				}
			}
		}
	}
	
	private static Object createSolver() {
		try {
			return new MPSolver("ADPSummaryPageSolver", MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));
		} catch (Exception | Error e) {
			// or tools error, skip
		}
		return null;
	}
}
