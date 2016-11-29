package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ProfitAndLossResult;
import com.mmxlabs.models.lng.analytics.ResultContainer;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseEvaluator.IMapperClass;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.parseutils.IndexConversion;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class WhatIfEvaluator {

	public static void evaluate(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model) {

		final long targetPNL = model.getBaseCase().getProfitAndLoss();

		final BaseCase baseCase = AnalyticsFactory.eINSTANCE.createBaseCase();
		final List<List<Runnable>> combinations = new LinkedList<>();
		for (final PartialCaseRow r : model.getPartialCase().getPartialCase()) {
			final BaseCaseRow bcr = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
			if (r.getBuyOptions().size() > 1) {

				final List<Runnable> options = new LinkedList<>();
				for (final BuyOption o : r.getBuyOptions()) {
					options.add(() -> bcr.setBuyOption(o));
				}
				combinations.add(options);
			} else if (r.getBuyOptions().size() == 1) {
				bcr.setBuyOption(r.getBuyOptions().get(0));
			}

			if (r.getSellOptions().size() > 1) {
				final List<Runnable> options = new LinkedList<>();
				for (final SellOption o : r.getSellOptions()) {
					options.add(() -> bcr.setSellOption(o));
				}
				combinations.add(options);
			} else if (r.getSellOptions().size() == 1) {
				bcr.setSellOption(r.getSellOptions().get(0));
			}
			if (r.getShipping().size() > 1) {
				final List<Runnable> options = new LinkedList<>();
				for (final ShippingOption o : r.getShipping()) {
					options.add(() -> bcr.setShipping(o));
				}
				combinations.add(options);
			} else if (r.getShipping().size() == 1) {
				bcr.setShipping(r.getShipping().get(0));
			}
			baseCase.getBaseCase().add(bcr);
		}

		final CompoundCommand cmd = new CompoundCommand("Generate results");
		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_SETS, SetCommand.UNSET_VALUE));
		if (combinations.isEmpty()) {
			final ResultSet resultSet = singleEval(scenarioEditingLocation, targetPNL, model, baseCase);
			if (resultSet != null) {
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_SETS, Collections.singletonList(resultSet)));
			}
		} else {
			long a = System.currentTimeMillis();
			final List<BaseCase> tasks = new LinkedList<>();
			recursiveTaskCreator(0, combinations, scenarioEditingLocation, targetPNL, model, baseCase, tasks);
			filterTasks(tasks);
			Collection<ResultSet> results = multiEval(scenarioEditingLocation, targetPNL, model, tasks);
			if (!results.isEmpty()) {
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_SETS, new LinkedList<>(results)));
			}
			long b = System.currentTimeMillis();
			System.out.printf("Eval %d\n", b - a);
		}

		if (!cmd.isEmpty()) {
			scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, model, null);
		}

		if (true) {
			// Debugging / sanity check code. Make sure we haven't accidently pulled in references to data in a temporary scenario.
			// All references to elements (contained or otherwise) should all be in the same eResource
			final Resource expected = model.eResource();
			assert expected != null;
			for (final ResultSet rs : model.getResultSets()) {
				assert rs.eResource() == expected;
				for (final AnalysisResultRow row : rs.getRows()) {
					assert row.eResource() == expected;
					if (row.getBuyOption() != null) {
						assert row.getBuyOption().eResource() == expected;
					}
					if (row.getSellOption() != null) {
						assert row.getSellOption().eResource() == expected;
					}
					if (row.getShipping() != null) {
						assert row.getSellOption().eResource() == expected;
					}
					if (row.getResultDetail() != null) {
						assert row.getResultDetail().eResource() == expected;
					}
					final ResultContainer c = row.getResultDetails();
					if (c != null) {
						assert c.eResource() == expected;
						if (c.getCargoAllocation() != null) {
							assert c.getCargoAllocation().eResource() == expected;

							for (final Event e : c.getEvents()) {
								assert e.eResource() == expected;
								if (e.getSequence() != null) {
									assert e.getSequence().eResource() == expected;
								}
								if (e.getNextEvent() != null) {
									assert e.getNextEvent().eResource() == expected;
								}
								if (e.getPreviousEvent() != null) {
									assert e.getPreviousEvent().eResource() == expected;
								}
							}
							for (final SlotAllocation e : c.getSlotAllocations()) {
								assert e.eResource() == expected;
								final SlotVisit v = e.getSlotVisit();
								if (v != null) {
									assert v.getPort().eResource() == expected;
								}
							}
						}
					}
				}
			}
		}
	}

	private static void filterTasks(List<BaseCase> tasks) {
		Set<BaseCase> duplicates = new HashSet<>();
		for (BaseCase baseCase1 : tasks) {
			DUPLICATE_TEST: for (BaseCase baseCase2 : tasks) {
				if (duplicates.contains(baseCase1)
						|| duplicates.contains(baseCase2)
						|| baseCase1 == baseCase2
						|| baseCase1.getBaseCase().size() != baseCase2.getBaseCase().size()){
					continue;
				}
				for (int i = 0; i < baseCase1.getBaseCase().size(); i++) {
					BaseCaseRow baseCase1Row = baseCase1.getBaseCase().get(i);
					BaseCaseRow baseCase2Row = baseCase2.getBaseCase().get(i);
					if (baseCase1Row.getBuyOption() != baseCase2Row.getBuyOption()
							|| baseCase1Row.getSellOption() != baseCase2Row.getSellOption()
							|| baseCase1Row.getShipping() != baseCase2Row.getShipping()
							) {
						continue DUPLICATE_TEST;
					}
				}
				duplicates.add(baseCase2);
			}			
		}
		tasks.removeAll(duplicates);
	}

	private static ResultSet singleEval(final IScenarioEditingLocation scenarioEditingLocation, final long targetPNL, final OptionAnalysisModel model, final BaseCase baseCase) {

		final ResultSet[] ref = new ResultSet[1];
		BaseCaseEvaluator.generateScenario(scenarioEditingLocation, model, baseCase, (lngScenarioModel, mapper) -> {

			// DEBUG: Pass in the scenario instance
//			final ScenarioInstance parentForFork =  scenarioEditingLocation.getScenarioInstance();
			final ScenarioInstance parentForFork =  null;
			evaluateScenario(lngScenarioModel, parentForFork, model.isUseTargetPNL(), targetPNL);

			if (lngScenarioModel.getScheduleModel().getSchedule() == null) {
				return;
			}
			Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final ResultSet resultSet = AnalyticsFactory.eINSTANCE.createResultSet();
			for (final BaseCaseRow row : baseCase.getBaseCase()) {
				final AnalysisResultRow res = AnalyticsFactory.eINSTANCE.createAnalysisResultRow();
				res.setBuyOption(row.getBuyOption());
				res.setSellOption(row.getSellOption());
				if ((!AnalyticsBuilder.isShipped(row.getBuyOption()) || !AnalyticsBuilder.isShipped(row.getSellOption())) && AnalyticsBuilder.isShipped(row.getShipping())) {
					res.setShipping(null);
				} else {
					res.setShipping(row.getShipping());
				}

				final Triple<SlotAllocation, SlotAllocation, CargoAllocation> t = finder(schedule, row, mapper);
				SlotAllocation loadAllocation = t.getFirst();
				SlotAllocation dischargeAllocation = t.getSecond();
				CargoAllocation cargoAllocation = t.getThird();
				final EcoreUtil.Copier copier = new EcoreUtil.Copier();
				if (loadAllocation != null) {
					copier.copy(loadAllocation.getSlotVisit());
					loadAllocation = (SlotAllocation) copier.copy(loadAllocation);
				}
				if (dischargeAllocation != null) {
					copier.copy(dischargeAllocation.getSlotVisit());
					dischargeAllocation = (SlotAllocation) copier.copy(dischargeAllocation);
				}
				if (cargoAllocation != null) {
					copier.copyAll(cargoAllocation.getEvents());
					cargoAllocation = (CargoAllocation) copier.copy(cargoAllocation);
				}
				copier.copyReferences();

				// Move containership of schedule objects to the container.
				final ResultContainer container = AnalyticsFactory.eINSTANCE.createResultContainer();
				if (cargoAllocation != null) {
					container.setCargoAllocation(cargoAllocation);
					for (final Event e : cargoAllocation.getEvents()) {
						e.setSequence(null);
						container.getEvents().add(e);
					}
				}
				if (loadAllocation != null) {
					container.getSlotAllocations().add(loadAllocation);
					final SlotVisit v = loadAllocation.getSlotVisit();
					if (v != null && !container.getEvents().contains(v)) {
						v.setSequence(null);
						container.getEvents().add(v);
					}
				}
				if (dischargeAllocation != null) {
					container.getSlotAllocations().add(dischargeAllocation);
					final SlotVisit v = dischargeAllocation.getSlotVisit();
					if (v != null && !container.getEvents().contains(v)) {
						v.setSequence(null);
						container.getEvents().add(v);
					}
				}

				if (isBreakEvenRow(loadAllocation)) {
					final BreakEvenResult r = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
					r.setPrice(loadAllocation.getPrice());
					final String priceString = getPriceString(((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getReferenceModel().getPricingModel(),
							((BuyOpportunity) row.getBuyOption()).getPriceExpression(), loadAllocation.getPrice(), YearMonth.from(loadAllocation.getSlotVisit().getStart()));
					r.setPriceString(priceString);
					res.setResultDetail(r);
					if (cargoAllocation != null) {
						r.setCargoPNL((double) cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
					}
				} else if (isBreakEvenRow(dischargeAllocation)) {
					final BreakEvenResult r = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
					r.setPrice(dischargeAllocation.getPrice());
					final String priceString = getPriceString(((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getReferenceModel().getPricingModel(),
							((SellOpportunity) row.getSellOption()).getPriceExpression(), dischargeAllocation.getPrice(), YearMonth.from(dischargeAllocation.getSlotVisit().getStart()));
					r.setPriceString(priceString);
					res.setResultDetail(r);
					if (cargoAllocation != null) {
						r.setCargoPNL((double) cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
					}
				} else {
					final ProfitAndLossResult r = AnalyticsFactory.eINSTANCE.createProfitAndLossResult();
					if (cargoAllocation != null) {
						r.setValue((double) cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
					}
					res.setResultDetail(r);
				}
				if ((loadAllocation == null && row.getBuyOption() != null) || (dischargeAllocation == null && row.getSellOption() != null)) {
					final Pair<OpenSlotAllocation, OpenSlotAllocation> p = finder2(schedule, row, mapper);
					if (p.getFirst() != null) {
						assert loadAllocation == null;
						container.getOpenSlotAllocations().add(p.getFirst());

						final double value = (double) p.getFirst().getGroupProfitAndLoss().getProfitAndLoss();
						if (res.getResultDetail() != null && res.getResultDetail() instanceof ProfitAndLossResult) {
							final ProfitAndLossResult profitAndLossResult = (ProfitAndLossResult) res.getResultDetail();
							profitAndLossResult.setValue(profitAndLossResult.getValue() + value);
						} else {
							final ProfitAndLossResult r = AnalyticsFactory.eINSTANCE.createProfitAndLossResult();
							r.setValue(value);
							res.setResultDetail(r);
						}
					}
					if (p.getSecond() != null) {
						assert dischargeAllocation == null;
						container.getOpenSlotAllocations().add(p.getSecond());

						final double value = (double) p.getSecond().getGroupProfitAndLoss().getProfitAndLoss();
						if (res.getResultDetail() != null && res.getResultDetail() instanceof ProfitAndLossResult) {
							final ProfitAndLossResult profitAndLossResult = (ProfitAndLossResult) res.getResultDetail();
							profitAndLossResult.setValue(profitAndLossResult.getValue() + value);
						} else {
							final ProfitAndLossResult r = AnalyticsFactory.eINSTANCE.createProfitAndLossResult();
							r.setValue(value);
							res.setResultDetail(r);
						}
					}
				}
				// Clear old references
				if (container.getCargoAllocation() != null) {
					fixModelReferences(container.getCargoAllocation(), mapper);

					container.getCargoAllocation().unsetSequence();
					container.getCargoAllocation().unsetInputCargo();
				}
				for (final SlotAllocation slotAllocation : container.getSlotAllocations()) {
					slotAllocation.unsetSlot();
					slotAllocation.unsetSpotMarket();
				}
				for (final OpenSlotAllocation openSlotAllocation : container.getOpenSlotAllocations()) {
					openSlotAllocation.unsetSlot();
				}
				// Check for non-contained events - clear them.
				// Re-map non-contained data elements
				for (final Event e : container.getEvents()) {
					fixModelReferences(e, mapper);

					if (e.getNextEvent() != null) {
						if (!container.getEvents().contains(e.getNextEvent())) {
							e.setNextEvent(null);
						}
					}
					if (e.getPreviousEvent() != null) {
						if (!container.getEvents().contains(e.getPreviousEvent())) {
							e.setPreviousEvent(null);
						}
					}
				}
				res.setResultDetails(container);

				resultSet.getRows().add(res);
			}

			ref[0] = resultSet;
		});
		return ref[0];
	}

	private static Collection<ResultSet> multiEval(final IScenarioEditingLocation scenarioEditingLocation, final long targetPNL, final OptionAnalysisModel model, final List<BaseCase> baseCases) {

		final ConcurrentLinkedQueue<ResultSet> ref = new ConcurrentLinkedQueue<>();
		BaseCaseEvaluator.generateFullScenario(scenarioEditingLocation, model, (lngScenarioModel, shippingMap, mapper) -> {

			// DEBUG: Pass in the scenario instance
			final ScenarioInstance parentForFork = null;// scenarioEditingLocation.getScenarioInstance()
			final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
			userSettings.setBuildActionSets(false);
			userSettings.setGenerateCharterOuts(false);
			userSettings.setShippingOnly(false);
			userSettings.setSimilarityMode(SimilarityMode.OFF);

			ServiceHelper.<IAnalyticsScenarioEvaluator> withService(IAnalyticsScenarioEvaluator.class,
					evaluator -> evaluator.multiEvaluate(lngScenarioModel, userSettings, parentForFork, targetPNL,
							model.isUseTargetPNL() ? IAnalyticsScenarioEvaluator.BreakEvenMode.PORTFOLIO : IAnalyticsScenarioEvaluator.BreakEvenMode.POINT_TO_POINT, baseCases, mapper, shippingMap,
							(baseCase, schedule) -> {

								final ResultSet resultSet = AnalyticsFactory.eINSTANCE.createResultSet();

								for (final BaseCaseRow row : baseCase.getBaseCase()) {
									final AnalysisResultRow res = AnalyticsFactory.eINSTANCE.createAnalysisResultRow();
									res.setBuyOption(row.getBuyOption());
									res.setSellOption(row.getSellOption());
									if ((!AnalyticsBuilder.isShipped(row.getBuyOption()) || !AnalyticsBuilder.isShipped(row.getSellOption())) && AnalyticsBuilder.isShipped(row.getShipping())) {
										res.setShipping(null);
									} else {
										res.setShipping(row.getShipping());
									}

									final Triple<SlotAllocation, SlotAllocation, CargoAllocation> t = finder(schedule, row, mapper);
									SlotAllocation loadAllocation = t.getFirst();
									SlotAllocation dischargeAllocation = t.getSecond();
									CargoAllocation cargoAllocation = t.getThird();
									final EcoreUtil.Copier copier = new EcoreUtil.Copier();
									if (loadAllocation != null) {
										copier.copy(loadAllocation.getSlotVisit());
										loadAllocation = (SlotAllocation) copier.copy(loadAllocation);
									}
									if (dischargeAllocation != null) {
										copier.copy(dischargeAllocation.getSlotVisit());
										dischargeAllocation = (SlotAllocation) copier.copy(dischargeAllocation);
									}
									if (cargoAllocation != null) {
										copier.copyAll(cargoAllocation.getEvents());
										cargoAllocation = (CargoAllocation) copier.copy(cargoAllocation);
									}
									copier.copyReferences();

									// Move containership of schedule objects to the container.
									final ResultContainer container = AnalyticsFactory.eINSTANCE.createResultContainer();
									if (cargoAllocation != null) {
										container.setCargoAllocation(cargoAllocation);
										for (final Event e : cargoAllocation.getEvents()) {
											e.setSequence(null);
											container.getEvents().add(e);
										}
									}
									if (loadAllocation != null) {
										container.getSlotAllocations().add(loadAllocation);
										final SlotVisit v = loadAllocation.getSlotVisit();
										if (v != null && !container.getEvents().contains(v)) {
											v.setSequence(null);
											container.getEvents().add(v);
										}
									}
									if (dischargeAllocation != null) {
										container.getSlotAllocations().add(dischargeAllocation);
										final SlotVisit v = dischargeAllocation.getSlotVisit();
										if (v != null && !container.getEvents().contains(v)) {
											v.setSequence(null);
											container.getEvents().add(v);
										}
									}

									if (isBreakEvenRow(loadAllocation)) {
										final BreakEvenResult r = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
										r.setPrice(loadAllocation.getPrice());
										final String priceString = getPriceString(((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getReferenceModel().getPricingModel(),
												((BuyOpportunity) row.getBuyOption()).getPriceExpression(), loadAllocation.getPrice(), YearMonth.from(loadAllocation.getSlotVisit().getStart()));
										r.setPriceString(priceString);
										res.setResultDetail(r);
										if (cargoAllocation != null) {
											r.setCargoPNL((double) cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
										}
									} else if (isBreakEvenRow(dischargeAllocation)) {
										final BreakEvenResult r = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
										r.setPrice(dischargeAllocation.getPrice());
										final String priceString = getPriceString(((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getReferenceModel().getPricingModel(),
												((SellOpportunity) row.getSellOption()).getPriceExpression(), dischargeAllocation.getPrice(),
												YearMonth.from(dischargeAllocation.getSlotVisit().getStart()));
										r.setPriceString(priceString);
										res.setResultDetail(r);
										if (cargoAllocation != null) {
											r.setCargoPNL((double) cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
										}
									} else {
										final ProfitAndLossResult r = AnalyticsFactory.eINSTANCE.createProfitAndLossResult();
										if (cargoAllocation != null) {
											r.setValue((double) cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());
										}
										res.setResultDetail(r);
									}
									if ((loadAllocation == null && row.getBuyOption() != null) || (dischargeAllocation == null && row.getSellOption() != null)) {
										final Pair<OpenSlotAllocation, OpenSlotAllocation> p = finder2(schedule, row, mapper);
										if (p.getFirst() != null) {
											assert loadAllocation == null;
											container.getOpenSlotAllocations().add(p.getFirst());

											final double value = (double) p.getFirst().getGroupProfitAndLoss().getProfitAndLoss();
											if (res.getResultDetail() != null && res.getResultDetail() instanceof ProfitAndLossResult) {
												final ProfitAndLossResult profitAndLossResult = (ProfitAndLossResult) res.getResultDetail();
												profitAndLossResult.setValue(profitAndLossResult.getValue() + value);
											} else {
												final ProfitAndLossResult r = AnalyticsFactory.eINSTANCE.createProfitAndLossResult();
												r.setValue(value);
												res.setResultDetail(r);
											}
										}
										if (p.getSecond() != null) {
											assert dischargeAllocation == null;
											container.getOpenSlotAllocations().add(p.getSecond());

											final double value = (double) p.getSecond().getGroupProfitAndLoss().getProfitAndLoss();
											if (res.getResultDetail() != null && res.getResultDetail() instanceof ProfitAndLossResult) {
												final ProfitAndLossResult profitAndLossResult = (ProfitAndLossResult) res.getResultDetail();
												profitAndLossResult.setValue(profitAndLossResult.getValue() + value);
											} else {
												final ProfitAndLossResult r = AnalyticsFactory.eINSTANCE.createProfitAndLossResult();
												r.setValue(value);
												res.setResultDetail(r);
											}
										}
									}
									// Clear old references
									if (container.getCargoAllocation() != null) {
										fixModelReferences(container.getCargoAllocation(), mapper);

										container.getCargoAllocation().unsetSequence();
										container.getCargoAllocation().unsetInputCargo();
									}
									for (final SlotAllocation slotAllocation : container.getSlotAllocations()) {
										slotAllocation.unsetSlot();
										slotAllocation.unsetSpotMarket();
									}
									for (final OpenSlotAllocation openSlotAllocation : container.getOpenSlotAllocations()) {
										openSlotAllocation.unsetSlot();
									}
									// Check for non-contained events - clear them.
									// Re-map non-contained data elements
									for (final Event e : container.getEvents()) {
										fixModelReferences(e, mapper);

										if (e.getNextEvent() != null) {
											if (!container.getEvents().contains(e.getNextEvent())) {
												e.setNextEvent(null);
											}
										}
										if (e.getPreviousEvent() != null) {
											if (!container.getEvents().contains(e.getPreviousEvent())) {
												e.setPreviousEvent(null);
											}
										}
									}
									res.setResultDetails(container);

									resultSet.getRows().add(res);
								}
								ref.add(resultSet);
							}));

		});
		return ref;
	}

	/**
	 * Recurse through the object tree ensuring any non-contained references are from the real data model and not one of the copies. Remove references to generated cargo data etc.
	 * 
	 * @param object
	 * @param mapper
	 */
	private static void fixModelReferences(final EObject object, final IMapperClass mapper) {
		final EClass cls = object.eClass();
		for (final EReference ref : cls.getEAllReferences()) {
			if (ref.isContainment()) {
				if (ref.isMany()) {
					final List<EObject> children = (List<EObject>) object.eGet(ref);
					for (final EObject child : children) {
						fixModelReferences(child, mapper);
					}
				} else {
					final EObject child = (EObject) object.eGet(ref);
					if (child != null) {
						fixModelReferences(child, mapper);
					}
				}
			} else {
				if (ref.isMany()) {
					final List<EObject> children = (List<EObject>) object.eGet(ref);
					for (final EObject child : children) {
						if (!(child instanceof Event || child instanceof SlotAllocation)) {
							final EObject replacement = mapper.getOriginal(child);
							if (checkIt(object, ref, child, replacement) && replacement != null) {
								object.eSet(ref, replacement);
							} else {
								object.eUnset(ref);
							}
						}
					}
				} else {
					final EObject child = (EObject) object.eGet(ref);
					if (!(child instanceof Event || child instanceof SlotAllocation)) {
						if (child != null) {
							final EObject replacement = mapper.getOriginal(child);
							if (checkIt(object, ref, child, replacement) && replacement != null) {
								object.eSet(ref, replacement);
							} else {
								object.eUnset(ref);
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Check the child reference and return false if it is something we want to exclude from the results - e.g. elements of the Schedule model.
	 * 
	 * @param object
	 * @param ref
	 * @param child
	 * @param replacement
	 * @return
	 */
	private static boolean checkIt(final EObject object, final EReference ref, final EObject child, final EObject replacement) {
		if (child instanceof Sequence) {
			return false;
		}
		if (child instanceof Slot) {
			return false;
		}
		if (child instanceof Cargo) {
			return false;
		}

		return true;
	}

	private static String getPriceString(@NonNull final PricingModel pricingModel, @NonNull final String expression, final double breakevenPrice, @NonNull final YearMonth date) {
		if (expression.equals("?")) {
			return String.format("%,.3f", breakevenPrice);
		}
		final double rearrangedPrice = IndexConversion.getRearrangedPrice(pricingModel, expression, breakevenPrice, date);
		return expression.replaceFirst(Pattern.quote("?"), String.format("%,.2f", rearrangedPrice));
	}

	private static Triple<SlotAllocation, SlotAllocation, CargoAllocation> finder(final Schedule schedule, final BaseCaseRow baseCaseRow, final IMapperClass mapper) {

		CargoAllocation cargoAllocation = null;
		SlotAllocation loadAllocation = null;
		SlotAllocation dischargeAllocation = null;

		final EList<SlotAllocation> slotAllocations = schedule.getSlotAllocations();

		final LoadSlot loadSlot = mapper.get(baseCaseRow.getBuyOption());
		final DischargeSlot dischargeSlot = mapper.get(baseCaseRow.getSellOption());

		for (final SlotAllocation a : slotAllocations) {
			if (a.getSlot() == loadSlot) {
				loadAllocation = a;
				if (cargoAllocation == null) {
					cargoAllocation = a.getCargoAllocation();
				}
			}
			if (a.getSlot() == dischargeSlot) {
				dischargeAllocation = a;
				if (cargoAllocation == null) {
					cargoAllocation = a.getCargoAllocation();
				}
			}
		}

		return new Triple<>(loadAllocation, dischargeAllocation, cargoAllocation);
	}

	private static Pair<OpenSlotAllocation, OpenSlotAllocation> finder2(Schedule schedule, final BaseCaseRow baseCaseRow, final IMapperClass mapper) {
		OpenSlotAllocation loadAllocation = null;
		OpenSlotAllocation dischargeAllocation = null;

		final LoadSlot loadSlot = mapper.get(baseCaseRow.getBuyOption());
		final DischargeSlot dischargeSlot = mapper.get(baseCaseRow.getSellOption());

		for (final OpenSlotAllocation a : schedule.getOpenSlotAllocations()) {
			if (a.getSlot() == loadSlot) {
				loadAllocation = a;
			}
			if (a.getSlot() == dischargeSlot) {
				dischargeAllocation = a;
			}
		}

		return new Pair<>(loadAllocation, dischargeAllocation);
	}

	private static boolean isBreakEvenRow(final SlotAllocation slotAllocation) {

		if (slotAllocation != null) {
			final Slot slot = slotAllocation.getSlot();
			if (slot != null) {
				final String priceExpression = slot.getPriceExpression();
				if (priceExpression != null) {
					return priceExpression.contains("?");
				}
			}
		}

		return false;
	}

	private static void recursiveTaskCreator(final int listIdx, final List<List<Runnable>> combinations, final IScenarioEditingLocation scenarioEditingLocation, final long targetPNL,
			final OptionAnalysisModel model, final BaseCase baseCase, final List<BaseCase> tasks) {
		if (listIdx == combinations.size()) {
			final BaseCase copy = EcoreUtil.copy(baseCase);
			filterShipping(copy);
			tasks.add(copy);
			return;
		}

		final List<Runnable> options = combinations.get(listIdx);
		for (final Runnable r : options) {
			r.run();
			recursiveTaskCreator(listIdx + 1, combinations, scenarioEditingLocation, targetPNL, model, baseCase, tasks);
		}
	}

	private static void filterShipping(BaseCase copy) {
		for (BaseCaseRow baseCaseRow : copy.getBaseCase()) {
			if (baseCaseRow.getBuyOption() != null
					&& ((baseCaseRow.getBuyOption() instanceof BuyReference && ((BuyReference)baseCaseRow.getBuyOption()).getSlot().isDESPurchase())
					|| (baseCaseRow.getBuyOption() instanceof BuyOpportunity && ((BuyOpportunity)baseCaseRow.getBuyOption()).isDesPurchase()))
							) {
				baseCaseRow.setShipping(null);
			}
		}
	}

	private static void evaluateScenario(final LNGScenarioModel lngScenarioModel, @Nullable final ScenarioInstance parentForFork, final boolean useTargetPNL, final long targetPNL) {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		ServiceHelper.<IAnalyticsScenarioEvaluator> withService(IAnalyticsScenarioEvaluator.class, evaluator -> evaluator.breakEvenEvaluate(lngScenarioModel, userSettings, parentForFork, targetPNL,
				useTargetPNL ? IAnalyticsScenarioEvaluator.BreakEvenMode.PORTFOLIO : IAnalyticsScenarioEvaluator.BreakEvenMode.POINT_TO_POINT));
	}

	public static Predicate<BuyOption> isDESPurchase() {
		return b -> ((b instanceof BuyReference && ((BuyReference) b).getSlot() != null && ((BuyReference) b).getSlot().isDESPurchase() == true)
				|| (b instanceof BuyOpportunity && ((BuyOpportunity) b).isDesPurchase() == true));
	}

	public static Predicate<SellOption> isFOBSale() {
		return s -> ((s instanceof SellReference && ((SellReference) s).getSlot() != null && ((SellReference) s).getSlot().isFOBSale() == true)
				|| (s instanceof SellOpportunity && ((SellOpportunity) s).isFobSale() == true));
	}

	private static Predicate<ShippingOption> isNominated() {
		return s -> s instanceof NominatedShippingOption;
	}

}
