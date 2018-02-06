package com.mmxlabs.lngdataserver.lng.importers.pricing;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.mmxlabs.lngdataserver.integration.pricing.CurveType;
import com.mmxlabs.lngdataserver.integration.pricing.IPricingProvider;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;

public class PricingToScenarioCopier {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PricingToScenarioCopier.class);
	
	public static Command getUpdatePricingCommand(@NonNull final EditingDomain editingDomain, IPricingProvider pricingProvider, @NonNull final PricingModel pricingModel) {
		
		final CompoundCommand cc = new CompoundCommand();
		
		// add curves
		updatePrices(pricingProvider, 
				PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES,
				pricingProvider.getCommodityCurves(),
				pricingModel.getCommodityIndices(), 
				PricingPackage.eINSTANCE.getPricingFactory()::createCommodityIndex, 
				pricingModel,
				editingDomain,
				cc,
				false);
		
		updatePrices(pricingProvider, 
				PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES,
				pricingProvider.getBaseFuelCurves(),
				pricingModel.getBaseFuelPrices(), 
				PricingPackage.eINSTANCE.getPricingFactory()::createBaseFuelIndex, 
				pricingModel,
				editingDomain,
				cc,
				false);
		
		updatePrices(pricingProvider, 
				PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES,
				pricingProvider.getCharterCurves(),
				pricingModel.getCharterIndices(), 
				PricingPackage.eINSTANCE.getPricingFactory()::createCharterIndex, 
				pricingModel,
				editingDomain,
				cc,
				true);
		
		updatePrices(pricingProvider, 
				PricingPackage.Literals.PRICING_MODEL__CURRENCY_INDICES,
				pricingProvider.getCurrencyCurves(),
				pricingModel.getCurrencyIndices(), 
				PricingPackage.eINSTANCE.getPricingFactory()::createCurrencyIndex, 
				pricingModel,
				editingDomain,
				cc,
				false);
		return cc;
	}

	private static <I extends NamedIndexContainer<?>> void updatePrices(IPricingProvider pricingProvider, 
			EReference indexReference,
			List<String> availableIndices,
			EList<I> currentIndices, 
			Supplier<I> indexSupplier,
			@NonNull final PricingModel pricingModel,
			EditingDomain editingDomain,
			CompoundCommand cc,
			boolean isInt) {
		
		Set<I> added = new HashSet<>();
		availableIndices.forEach(curve -> {
			I index;
			Optional<I> potential = currentIndices.stream().filter(i -> i.getName().equals(curve)).findFirst();
			if (potential.isPresent()) {
				// reuse existing
				index = potential.get();
			}else {
				index = indexSupplier.get();
				index.setName(curve);
				Index data;
				if(pricingProvider.getCurveType(curve) == CurveType.DataCurve) {
					data = PricingPackage.eINSTANCE.getPricingFactory().createDataIndex();
				}else {
					data = PricingPackage.eINSTANCE.getPricingFactory().createDerivedIndex();
				}
				index.setData(data);
				
				cc.append(AddCommand.create(editingDomain, pricingModel, indexReference, index));
				// check whether it is a data or an expression/derived curve
			}
			added.add(index);
			
			if (pricingProvider.getCurveType(curve) == CurveType.DataCurve) {
				Class<?> value = Integer.class;
				
				DataIndex<Number> dataIndex = PricingPackage.eINSTANCE.getPricingFactory().createDataIndex();
				try {
					pricingProvider.getData(curve).forEach(point -> {
						IndexPoint<Number> indexPoint = PricingPackage.eINSTANCE.getPricingFactory().createIndexPoint();
						indexPoint.setDate(YearMonth.of(point.getFirst().getYear(), point.getFirst().getMonthValue()));
						if(!isInt) {
							indexPoint.setValue(point.getSecond());
						}else {
							indexPoint.setValue(point.getSecond().intValue());
						}
						dataIndex.getPoints().add(indexPoint);
					});
				} catch (IOException e) {
					LOGGER.error("Error getting expression for curve {}", curve);
					throw new RuntimeException(e);
				}
				
				// set data
				cc.append(SetCommand.create(editingDomain, index.getData(), PricingPackage.Literals.DATA_INDEX__POINTS, dataIndex.getPoints()));
			}else { // expression curve
				
				DerivedIndex<Double> derivedIndex = PricingPackage.eINSTANCE.getPricingFactory().createDerivedIndex();
				try {
					derivedIndex.setExpression(pricingProvider.getExpression(curve));
				} catch (IOException e) {
					LOGGER.error("Error getting expression for curve {}", curve);
					throw new RuntimeException(e);
				}
				
				// set data
				cc.append(SetCommand.create(editingDomain, index.getData(), PricingPackage.Literals.DERIVED_INDEX__EXPRESSION, derivedIndex.getExpression()));
			}
			
			
			// currency unit
			cc.append(SetCommand.create(editingDomain, index, PricingPackage.Literals.NAMED_INDEX_CONTAINER__CURRENCY_UNIT, pricingProvider.getCurrencyUnit(curve)));
			//volume unit
			cc.append(SetCommand.create(editingDomain, index, PricingPackage.Literals.NAMED_INDEX_CONTAINER__VOLUME_UNIT, pricingProvider.getVolumeUnit(curve)));
		});
		
		
		// delete indices that are not present anymore
		Collection<I> toDelete = Sets.difference(new HashSet<I>(currentIndices), added);
		if (!toDelete.isEmpty()) {
			cc.append(RemoveCommand.create(editingDomain, pricingModel, indexReference, toDelete));
//			cc.append(RemoveCommand.create(editingDomain, currentIndices, indexReference, toDelete));
		}
	}
}
