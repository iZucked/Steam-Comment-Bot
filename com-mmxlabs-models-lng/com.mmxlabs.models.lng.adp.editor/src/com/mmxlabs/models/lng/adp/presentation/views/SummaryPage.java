/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import com.mmxlabs.common.Pair;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.MaxCargoConstraint;
import com.mmxlabs.models.lng.adp.MinCargoConstraint;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;
import com.mmxlabs.models.lng.adp.ProfileConstraint;
import com.mmxlabs.models.lng.adp.ProfileVesselRestriction;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.SubProfileConstraint;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.ecore.SafeAdapterImpl;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class SummaryPage extends ADPComposite {

	private class ADPConstraintsSummaryAdapter extends EContentAdapter {
		@Override
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);
			final Object notifier = notification.getNotifier();
			if (constraintsViewer != null) {
				if (notifier instanceof PeriodDistribution) {
					ViewerHelper.refresh(constraintsViewer, false);
				} else if (notifier instanceof PeriodDistributionProfileConstraint) {
					ViewerHelper.refresh(constraintsViewer, false);
				} else if (notifier instanceof ProfileVesselRestriction) {
					ViewerHelper.refresh(constraintsViewer, false);
				}
			}
		}

		@Override
		protected void setTarget(final EObject target) {
			if (this.target != null && this.target != target) {
				releaseAdapter();
			}
			basicSetTarget(target);
			if (target instanceof ADPModel) {
				final ADPModel localAdpModel = (ADPModel) target;
				addAdapter(localAdpModel);
				for (final PurchaseContractProfile profile : localAdpModel.getPurchaseContractProfiles()) {
					addToContractProfile(profile);
				}
				for (final SalesContractProfile profile : localAdpModel.getSalesContractProfiles()) {
					addToContractProfile(profile);
				}
			}
		}

		private <T extends Slot<U>, U extends Contract> void addToContractProfile(final ContractProfile<T, U> contractProfile) {
			addAdapter(contractProfile);
			for (final ProfileConstraint constraint : contractProfile.getConstraints()) {
				if (constraint instanceof PeriodDistributionProfileConstraint) {
					final PeriodDistributionProfileConstraint pdpc = (PeriodDistributionProfileConstraint) constraint;
					addAdapter(pdpc);
					pdpc.getDistributions().forEach(this::addAdapter);
				} else if (constraint instanceof MaxCargoConstraint || constraint instanceof MinCargoConstraint) {
					addAdapter(constraint);
				}
			}
			for (final SubContractProfile<T, U> subprofile : contractProfile.getSubProfiles()) {
				addAdapter(subprofile);
				for (final SubProfileConstraint spc : subprofile.getConstraints()) {
					if (spc instanceof ProfileVesselRestriction) {
						addAdapter(spc);
					}
				}
			}
		}

		@Override
		protected void unsetTarget(final EObject target) {
			basicUnsetTarget(target);
			if (target instanceof ADPModel) {
				final ADPModel localAdpModel = (ADPModel) target;
				removeAdapter(localAdpModel, false, true);
				for (final PurchaseContractProfile profile : localAdpModel.getPurchaseContractProfiles()) {
					removeAdapter(profile, false, true);
					for (final ProfileConstraint constraint : profile.getConstraints()) {
						if (constraint instanceof PeriodDistributionProfileConstraint) {
							final PeriodDistributionProfileConstraint pdpc = (PeriodDistributionProfileConstraint) constraint;
							removeAdapter(pdpc, false, true);
							for (final PeriodDistribution distribution : pdpc.getDistributions()) {
								removeAdapter(distribution, false, true);
							}
						} else if (constraint instanceof MaxCargoConstraint) {
							final MaxCargoConstraint maxCargoConstraint = (MaxCargoConstraint) constraint;
							removeAdapter(maxCargoConstraint, false, true);
						} else if (constraint instanceof MinCargoConstraint) {
							final MinCargoConstraint minCargoConstraint = (MinCargoConstraint) constraint;
							removeAdapter(minCargoConstraint, false, true);
						}
					}
				}
				for (final SalesContractProfile profile : localAdpModel.getSalesContractProfiles()) {
					removeAdapter(profile, false, true);
					for (final ProfileConstraint constraint : profile.getConstraints()) {
						if (constraint instanceof PeriodDistributionProfileConstraint) {
							final PeriodDistributionProfileConstraint pdpc = (PeriodDistributionProfileConstraint) constraint;
							removeAdapter(pdpc, false, true);
							for (final PeriodDistribution distribution : pdpc.getDistributions()) {
								removeAdapter(distribution, false, true);
							}
						} else if (constraint instanceof MaxCargoConstraint) {
							final MaxCargoConstraint maxCargoConstraint = (MaxCargoConstraint) constraint;
							removeAdapter(maxCargoConstraint, false, true);
						} else if (constraint instanceof MinCargoConstraint) {
							final MinCargoConstraint minCargoConstraint = (MinCargoConstraint) constraint;
							removeAdapter(minCargoConstraint, false, true);
						}
					}
				}
			}
		}

		public void releaseAdapter() {
			if (target != null) {
				unsetTarget(target);
			}
		}
	}

	private ADPEditorData editorData;

	private SashForm mainComposite;
	private ScrolledComposite rhsScrolledComposite;
	private Composite rhsComposite;
	private Runnable releaseAdaptersRunnable = null;

	private GridTableViewer purchasesViewer;

	private GridTableViewer salesViewer;
	private GridTreeViewer constraintsViewer;

	private int constraintsSummaryExpandLevel;

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

	private ADPConstraintsSummaryAdapter adpAdapter;

	public SummaryPage(final Composite parent, final int style, final ADPEditorData editorData) {
		super(parent, style);
		this.editorData = editorData;
		this.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(0, 0).create());

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_ADP_PROFILE_CONSTRAINTS_SUMMARY)) {
			adpAdapter = new ADPConstraintsSummaryAdapter();
		} else {
			adpAdapter = null;
		}

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
			final Group purchaseContractTableGroup = new Group(mainComposite, SWT.NONE);
			purchaseContractTableGroup.setLayout(new GridLayout(1, false));
			purchaseContractTableGroup.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			purchaseContractTableGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
			purchaseContractTableGroup.setText("Purchase Contracts");

			final DetailToolbarManager buttonManager = new DetailToolbarManager(purchaseContractTableGroup, SWT.TOP);

			purchasesViewer = new GridTableViewer(purchaseContractTableGroup);
			purchasesViewer.getGrid().setHeaderVisible(true);
			purchasesViewer.setContentProvider(new ArrayContentProvider() {
				@Override
				public Object[] getElements(final Object inputElement) {
					if (inputElement instanceof ADPModel) {
						final ADPModel adpModel = (ADPModel) inputElement;
						final Set<Contract> contracts = new HashSet<>();
						adpModel.getPurchaseContractProfiles().stream().map(PurchaseContractProfile::getContract).forEach(contracts::add);
						final LinkedList<Object> profiles = new LinkedList<>();
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
						profiles.add(null);
						Object[] profileObjects = profiles.toArray();
						profiles.removeLast();
						profileObjects[profileObjects.length - 1] = profiles;
						return profileObjects;
					}
					return super.getElements(inputElement);
				}
			});

			createColumn(purchasesViewer, "Contract", (profile) -> profile.getContract() == null ? "<unknown>" : profile.getContract().getName());
			createColumn(purchasesViewer, "Generated cargoes", (profile) -> {
				final long generatedCargoes = editorData.getScenarioModel().getCargoModel().getLoadSlots().stream() //
						.filter(s -> profile.getContract() == s.getContract()).count();
				return Long.toString(generatedCargoes);
			});

			createColumn(purchasesViewer, "Max cargoes", (profile) -> {
				final List<LoadSlot> slots = editorData.getScenarioModel().getCargoModel().getLoadSlots();
				final PurchaseContractProfile purchaseProfile = (PurchaseContractProfile) profile;
				final Long maxCargoes = calculateMaxCargoesBound(purchaseProfile, slots);
				return maxCargoes.toString();
			});
		}
		{
			final Group salesContractTableGroup = new Group(mainComposite, SWT.NONE);
			salesContractTableGroup.setLayout(new GridLayout(1, false));
			salesContractTableGroup.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			salesContractTableGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
			salesContractTableGroup.setText("Sales Contracts");

			final DetailToolbarManager buttonManager = new DetailToolbarManager(salesContractTableGroup, SWT.TOP);

			salesViewer = new GridTableViewer(salesContractTableGroup);
			salesViewer.getGrid().setHeaderVisible(true);
			salesViewer.setContentProvider(new ArrayContentProvider() {
				@Override
				public Object[] getElements(final Object inputElement) {
					if (inputElement instanceof ADPModel) {
						final ADPModel adpModel = (ADPModel) inputElement;
						final Set<Contract> contracts = new HashSet<>();
						adpModel.getSalesContractProfiles().stream().map(SalesContractProfile::getContract).forEach(contracts::add);
						final LinkedList<Object> profiles = new LinkedList<>();
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
						profiles.add(null);
						Object[] profileObjects = profiles.toArray();
						profiles.removeLast();
						profileObjects[profileObjects.length - 1] = profiles;
						return profileObjects;
					}
					return super.getElements(inputElement);
				}
			});

			createColumn(salesViewer, "Contract", (profile) -> profile.getContract().getName());
			createColumn(salesViewer, "Generated cargoes", (profile) -> {
				final long generatedCargoes = editorData.getScenarioModel().getCargoModel().getDischargeSlots().stream() //
						.filter(s -> profile.getContract() == s.getContract()).count();
				return Long.toString(generatedCargoes);
			});
			createColumn(salesViewer, "Max cargoes", (profile) -> {
				final List<DischargeSlot> slots = editorData.getScenarioModel().getCargoModel().getDischargeSlots();
				final SalesContractProfile salesProfile = (SalesContractProfile) profile;
				final Long maxCargoes = calculateMaxCargoesBound(salesProfile, slots);
				return maxCargoes.toString();
			});
			buttonManager.getToolbarManager().add(new ActionContributionItem(PackActionFactory.createPackColumnsAction(salesViewer)));
		}
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_ADP_PROFILE_CONSTRAINTS_SUMMARY)) {
			final ScrolledComposite contractConstraintScrolledComposite = new ScrolledComposite(mainComposite, SWT.V_SCROLL);
			contractConstraintScrolledComposite.setLayoutData(GridDataFactory.fillDefaults()//
					.grab(false, true)//
					.align(SWT.FILL, SWT.FILL).create());

			contractConstraintScrolledComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			contractConstraintScrolledComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
			contractConstraintScrolledComposite.setExpandHorizontal(true);
			contractConstraintScrolledComposite.setExpandVertical(true);
			// final Composite contractConstraintComposite = new Composite(contractConstraintScrolledComposite, SWT.NONE);
			final Group contractConstraintsGroup = new Group(contractConstraintScrolledComposite, SWT.NONE);
			contractConstraintScrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			contractConstraintScrolledComposite.setContent(contractConstraintsGroup);

			contractConstraintsGroup.setLayout(new GridLayout(1, false));
			contractConstraintsGroup.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			contractConstraintsGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
			contractConstraintsGroup.setText("Profile Constraints");

			final DetailToolbarManager toolbarManager = new DetailToolbarManager(contractConstraintsGroup, SWT.TOP);

			constraintsViewer = new GridTreeViewer(contractConstraintsGroup, SWT.V_SCROLL | SWT.FILL);
			constraintsViewer.getGrid().setHeaderVisible(true);
			constraintsViewer.setContentProvider(getConstraintsSummaryContentProvider());
			final GridViewerColumn rangeColumn = new GridViewerColumn(constraintsViewer, SWT.NONE);
			rangeColumn.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			rangeColumn.getColumn().setText("Range");
			rangeColumn.getColumn().setTree(true);
			rangeColumn.getColumn().setWidth(200);
			rangeColumn.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					if (element instanceof PurchaseContractProfile) {
						final String contractName = ScenarioElementNameHelper.getName(((PurchaseContractProfile) element).getContract(), "<Not specified>");
						return String.format("%s (Purchase)", contractName);
					} else if (element instanceof SalesContractProfile) {
						final String contractName = ScenarioElementNameHelper.getName(((SalesContractProfile) element).getContract(), "<Not specified>");
						return String.format("%s (Sale)", contractName);
					} else if (element instanceof PeriodDistribution) {
						return ADPModelUtil.getPeriodDistributionRangeString((PeriodDistribution) element);
					} else if (element instanceof ProfileConstraint) {
						return "Range";
					} else if (element instanceof Pair) {
						final Pair<String, ?> pair = (Pair<String, ?>) element;
						return pair.getFirst();
					}
					return "";
				}
			});
			final GridViewerColumn minColumn = new GridViewerColumn(constraintsViewer, SWT.NONE);
			minColumn.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			minColumn.getColumn().setText("Min");
			minColumn.getColumn().setWidth(50);
			minColumn.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					if (element instanceof PeriodDistribution) {
						final PeriodDistribution periodDistribution = (PeriodDistribution) element;
						return Integer.toString(periodDistribution.getMinCargoes());
					} else if (element instanceof MinCargoConstraint) {
						return Integer.toString(((MinCargoConstraint) element).getMinCargoes());
					} else if (element instanceof Vessel) {
						return ScenarioElementNameHelper.getName((Vessel) element, "<Not specified>");
					}
					return "";
				}
			});
			final GridViewerColumn maxColumn = new GridViewerColumn(constraintsViewer, SWT.NONE);
			maxColumn.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			maxColumn.getColumn().setText("Max");
			maxColumn.getColumn().setWidth(50);
			maxColumn.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					if (element instanceof PeriodDistribution) {
						return Integer.toString(((PeriodDistribution) element).getMaxCargoes());
					} else if (element instanceof MaxCargoConstraint) {
						return Integer.toString(((MaxCargoConstraint) element).getMaxCargoes());
					}
					return "";
				}
			});

			Action packAction = new Action("Pack") {
				@Override
				public void run() {

					if (constraintsViewer != null && !constraintsViewer.getControl().isDisposed()) {
						final GridColumn[] columns = constraintsViewer.getGrid().getColumns();
						for (final GridColumn c : columns) {
							if (c.getResizeable()) {
								c.pack();
							}
						}
					}
				}
			};

			packAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Pack, IconMode.Enabled));
			toolbarManager.getToolbarManager().add(packAction);

			final CopyGridToClipboardAction constraintsViewerCopyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(constraintsViewer);
			final ActionContributionItem copyToClipboardAci = new ActionContributionItem(constraintsViewerCopyAction);
			toolbarManager.getToolbarManager().add(copyToClipboardAci);

			constraintsSummaryExpandLevel = 1;
			final Action collapseOneLevel = new Action("Collapse All") {
				@Override
				public void run() {
					constraintsViewer.collapseAll();
					constraintsSummaryExpandLevel = 1;
				}
			};
			final Action expandOneLevel = new Action("Expand one Level") {
				@Override
				public void run() {
					constraintsViewer.expandToLevel(++constraintsSummaryExpandLevel);
				}
			};
			CommonImages.setImageDescriptors(collapseOneLevel, IconPaths.CollapseAll);
			CommonImages.setImageDescriptors(expandOneLevel, IconPaths.ExpandAll);

			toolbarManager.getToolbarManager().add(collapseOneLevel);
			toolbarManager.getToolbarManager().add(expandOneLevel);
			toolbarManager.getToolbarManager().update(true);
			contractConstraintsGroup.layout();
			contractConstraintScrolledComposite.setMinSize(contractConstraintsGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			mainComposite.layout();
		} else {
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

	private <U extends Contract, V extends Slot<U>> Long calculateMaxCargoesBound(ContractProfile<V, U> profile, List<V> slots) {
		final List<ProfileConstraint> constraints = profile.getConstraints();
		final Long maxCargoes;
		if (constraints.isEmpty()) {
			maxCargoes = slots.stream().filter(s -> profile.getContract() == s.getContract()).count();
		} else {
			final Map<YearMonth, Long> variableBounds = new HashMap<>();
			slots.stream() //
					.filter(slot -> profile.getContract() == slot.getContract()) //
					.forEach(slot -> {
						final YearMonth ym = YearMonth.from(slot.getWindowStart());
						final Long currentBound = variableBounds.get(ym);
						if (currentBound == null) {
							variableBounds.put(ym, 1L);
						} else {
							variableBounds.put(ym, currentBound + 1L);
						}
					});
			final Long solverBound = calculateConstrainedProfileBound(variableBounds, constraints);
			if (solverBound != null) {
				maxCargoes = solverBound;
			} else {
				final ADPPeriod adpPeriod = new ADPPeriod(editorData.getAdpModel());
				maxCargoes = calculateNaiveSummaryBound(variableBounds, constraints, adpPeriod);
			}
		}
		return maxCargoes;
	}

	private long calculateNaiveSummaryBound(final Map<YearMonth, Long> variableBounds, final List<ProfileConstraint> constraints, final ADPPeriod adpPeriod) {
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
							Long variableBound = variableBounds.get(ym);
							if (variableBound != null && variableBound > upperbound) {
								variableBounds.put(ym, upperbound);
							}
						} else if (adpPeriod.equalsRange(range) && upperbound < fullCoverBound) {
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
		ViewerHelper.refresh(purchasesViewer, false);
		ViewerHelper.refresh(salesViewer, false);
		ViewerHelper.refresh(constraintsViewer, false);
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
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_ADP_PROFILE_CONSTRAINTS_SUMMARY)) {
				constraintsViewer.setInput(adpModel);
			}
		} else {
			purchasesViewer.setInput(null);
			salesViewer.setInput(null);
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_ADP_PROFILE_CONSTRAINTS_SUMMARY)) {
				constraintsViewer.setInput(null);
			}
		}

		if (scenarioModel != null) {
			final CargoModel cargoModel = scenarioModel.getCargoModel();
			final ADPModel thisAdpModel = scenarioModel.getAdpModel();
			if (thisAdpModel != null && LicenseFeatures.isPermitted(KnownFeatures.FEATURE_ADP_PROFILE_CONSTRAINTS_SUMMARY)) {
				releaseAdaptersRunnable = () -> {
					cargoModel.eAdapters().remove(cargoAdapter);
					adpAdapter.releaseAdapter();
				};
				cargoModel.eAdapters().add(cargoAdapter);
				adpAdapter.setTarget(thisAdpModel);
			} else {
				releaseAdaptersRunnable = () -> cargoModel.eAdapters().remove(cargoAdapter);
				cargoModel.eAdapters().add(cargoAdapter);
			}
		}
	}

	@Override
	public void dispose() {

		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}
		adpAdapter = null;
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
				} else if (element instanceof List) {
					if (name.equals("Contract")) {
						return "Total";
					} else {
						final List<?> profiles = (List<?>) element;
						if (profiles.isEmpty())
							return "0";
						if (name.equals("Generated cargoes")) {
							final Set<Contract> contractsOfInterest = profiles.stream().map(p -> (ContractProfile<?, ?>) p).map(ContractProfile::getContract).collect(Collectors.toSet());
							final ContractProfile<?, ?> firstProfile = (ContractProfile<?, ?>) profiles.get(0);
							final long generatedCargoes;
							if (firstProfile instanceof PurchaseContractProfile) {
								generatedCargoes = editorData.getScenarioModel().getCargoModel().getLoadSlots().stream() //
										.map(LoadSlot::getContract).filter(contractsOfInterest::contains).count();
							} else {
								generatedCargoes = editorData.getScenarioModel().getCargoModel().getDischargeSlots().stream() //
										.map(DischargeSlot::getContract).filter(contractsOfInterest::contains).count();
							}
							return Long.toString(generatedCargoes);
						} else {
							final ContractProfile<?, ?> firstProfile = (ContractProfile<?, ?>) profiles.get(0);
							final Long maxCargoesTotal;
							if (firstProfile instanceof PurchaseContractProfile) {
								final List<LoadSlot> slots = editorData.getScenarioModel().getCargoModel().getLoadSlots();
								maxCargoesTotal = profiles.stream().mapToLong(p -> calculateMaxCargoesBound((PurchaseContractProfile) p, slots)).sum();
							} else {
								final List<DischargeSlot> slots = editorData.getScenarioModel().getCargoModel().getDischargeSlots();
								maxCargoesTotal = profiles.stream().mapToLong(p -> calculateMaxCargoesBound((SalesContractProfile) p, slots)).sum();
							}
							return maxCargoesTotal.toString();
						}
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
								Long variableBound = variableBounds.get(ym);
								if (variableBound != null && variableBounds.get(ym) > upperbound) {
									variableBounds.put(ym, upperbound);
									dateToMPVarMap.get(ym).setUb(upperbound);
								}
							} else {
								final MPConstraint mpConstraint = solver.makeConstraint(0, profileConstraint.getMaxCargoes());
								range.forEach(ym -> {
									MPVariable mpVar = dateToMPVarMap.get(ym);
									if (mpVar != null) {
										mpConstraint.setCoefficient(mpVar, 1);
									}
								});
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

	private class ADPPeriod {
		private final YearMonth adpStart;
		private final YearMonth adpInclusiveEnd;
		private final int monthCount;

		public ADPPeriod(final ADPModel adpModel) {
			this.adpStart = adpModel.getYearStart();
			final YearMonth adpEnd = adpModel.getYearEnd();
			Period p = Period.between(this.adpStart.atDay(1), adpEnd.atDay(1));
			this.monthCount = 12 * p.getYears() + p.getMonths();
			this.adpInclusiveEnd = adpEnd.minusMonths(1);
		}

		public boolean equalsRange(final List<YearMonth> range) {
			if (this.monthCount == range.size()) {
				Iterator<YearMonth> rangeIter = range.iterator();
				if (rangeIter.hasNext()) {
					YearMonth earliestMonth = rangeIter.next();
					YearMonth latestMonth = earliestMonth;
					while (rangeIter.hasNext()) {
						final YearMonth nextMonth = rangeIter.next();
						if (nextMonth.isBefore(earliestMonth)) {
							earliestMonth = nextMonth;
						} else if (nextMonth.isAfter(latestMonth)) {
							latestMonth = nextMonth;
						}
					}
					return this.adpStart.equals(earliestMonth) && this.adpInclusiveEnd.equals(latestMonth);
				}
			}
			return false;
		}
	}

	private ITreeContentProvider getConstraintsSummaryContentProvider() {
		return new ITreeContentProvider() {
			HashMap<Object, Object> reverseParentMap = new HashMap<>();

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof PurchaseContractProfile) {
					final PurchaseContractProfile pcp = (PurchaseContractProfile) element;
					return hasContractConstraints(pcp) || hasVesselConstraints(pcp);
				} else if (element instanceof SalesContractProfile) {
					final SalesContractProfile scp = (SalesContractProfile) element;
					return hasContractConstraints(scp) || hasVesselConstraints(scp);
				} else if (element instanceof Pair) {
					return true;
				}
				return false;
			}

			private <T extends Slot<U>, U extends Contract> boolean hasContractConstraints(final ContractProfile<T, U> contractProfile) {
				return contractProfile.getConstraints().stream().anyMatch(con -> {
					if (con instanceof PeriodDistributionProfileConstraint) {
						final PeriodDistributionProfileConstraint pdpc = (PeriodDistributionProfileConstraint) con;
						return !pdpc.getDistributions().isEmpty();
					}
					return false;
				});
			}

			private <T extends Slot<U>, U extends Contract> boolean hasVesselConstraints(final ContractProfile<T, U> contractProfile) {
				return contractProfile.getSubProfiles().stream().flatMap(scp -> scp.getConstraints().stream()).filter(ProfileVesselRestriction.class::isInstance)
						.map(ProfileVesselRestriction.class::cast).anyMatch(pvr -> !pvr.getVessels().isEmpty());
			}

			@Override
			public Object getParent(Object element) {
				final Object parent = reverseParentMap.get(element);
				return parent;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof ADPModel) {
					final ADPModel adpModel = (ADPModel) inputElement;
					final List<PurchaseContractProfile> purchaseContractProfiles = new ArrayList<>(adpModel.getPurchaseContractProfiles());
					purchaseContractProfiles.sort((profile1, profile2) -> {
						final String contractName1 = ScenarioElementNameHelper.getName(profile1.getContract(), "<Not specified>");
						final String contractName2 = ScenarioElementNameHelper.getName(profile2.getContract(), "<Not specified>");
						return contractName1.compareTo(contractName2);
					});
					final List<SalesContractProfile> salesContractProfiles = new ArrayList<>(adpModel.getSalesContractProfiles());
					salesContractProfiles.sort((profile1, profile2) -> {
						final String contractName1 = ScenarioElementNameHelper.getName(profile1.getContract(), "<Not specified>");
						final String contractName2 = ScenarioElementNameHelper.getName(profile2.getContract(), "<Not specified>");
						return contractName1.compareTo(contractName2);
					});
					final Object[] elements = new Object[purchaseContractProfiles.size() + salesContractProfiles.size()];
					int i = 0;
					final Iterator<PurchaseContractProfile> iterPurchaseProfiles = purchaseContractProfiles.iterator();
					while (iterPurchaseProfiles.hasNext()) {
						elements[i++] = iterPurchaseProfiles.next();
					}
					final Iterator<SalesContractProfile> iterSalesProfiles = salesContractProfiles.iterator();
					while (iterSalesProfiles.hasNext()) {
						elements[i++] = iterSalesProfiles.next();
					}
					assert i == elements.length;
					return elements;
				}
				return new Object[0];
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof PurchaseContractProfile) {
					return localGetChildren((PurchaseContractProfile) parentElement);
				} else if (parentElement instanceof SalesContractProfile) {
					return localGetChildren((SalesContractProfile) parentElement);
				} else if (parentElement instanceof Pair) {
					final Pair<?, List<Object>> pair = (Pair<?, List<Object>>) parentElement;
					return pair.getSecond().toArray();
				}
				return new Object[0];
			}

			private <T extends Slot<U>, U extends Contract> Object[] localGetChildren(final ContractProfile<T, U> contractProfile) {
				final List<Object> constraintGroups = new ArrayList<>();
				final List<Object> profileConstraints = contractProfile.getConstraints().stream().flatMap(constraint -> {
					if (constraint instanceof PeriodDistributionProfileConstraint) {
						return ((PeriodDistributionProfileConstraint) constraint).getDistributions().stream().map(Object.class::cast);
					} else {
						return Stream.of((Object) constraint);
					}
				}).collect(Collectors.toList());
				if (!profileConstraints.isEmpty()) {
					final Pair<String, List<Object>> pair = Pair.of("Contract constraints", profileConstraints);
					profileConstraints.forEach(o -> reverseParentMap.put(o, contractProfile));
					reverseParentMap.put(pair, contractProfile);
					constraintGroups.add(pair);
				}
				final List<Object> vesselConstraints = contractProfile.getSubProfiles().stream().flatMap(scp -> scp.getConstraints().stream()).filter(ProfileVesselRestriction.class::isInstance)
						.map(ProfileVesselRestriction.class::cast).map(ProfileVesselRestriction::getVessels).flatMap(List::stream).collect(Collectors.toList());
				if (!vesselConstraints.isEmpty()) {
					final Pair<String, List<Object>> pair = Pair.of("Vessel restrictions", vesselConstraints);
					vesselConstraints.forEach(o -> reverseParentMap.put(o, contractProfile));
					reverseParentMap.put(pair, contractProfile);
					constraintGroups.add(pair);
				}
				return constraintGroups.toArray();
			}

			@Override
			public void dispose() {
				this.reverseParentMap = null;
			}
		};
	}
}
