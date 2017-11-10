package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public interface IMapperClass {

		LoadSlot getOriginal(BuyOption buy);

		LoadSlot getBreakEven(BuyOption buy);

		LoadSlot getChangable(BuyOption buy);

		DischargeSlot getOriginal(SellOption sell);

		DischargeSlot getBreakEven(SellOption sell);

		DischargeSlot getChangable(SellOption sell);

		void addMapping(BuyOption buy, LoadSlot original, LoadSlot breakEven, LoadSlot changable);

		void addMapping(SellOption sell, DischargeSlot original, DischargeSlot breakEven, DischargeSlot changable);

		<T extends EObject> T getCopy(@NonNull T original);

		<T extends EObject> T getOriginal(@NonNull T copy);

		void addMapping(SpotMarket market, YearMonth date, LoadSlot slot_original, LoadSlot slot_breakEven, LoadSlot slot_changable);

		void addMapping(SpotMarket market, YearMonth date, DischargeSlot slot_original, DischargeSlot slot_breakEven, DischargeSlot slot_changable);

		DischargeSlot getSalesMarketBreakEven(SpotMarket market, @NonNull YearMonth from);

		DischargeSlot getSalesMarketChangable(SpotMarket market, @NonNull YearMonth date);

		DischargeSlot getSalesMarketOriginal(SpotMarket market, @NonNull YearMonth date);

		LoadSlot getPurchaseMarketBreakEven(SpotMarket market, @NonNull YearMonth date);

		LoadSlot getPurchaseMarketChangable(SpotMarket market, @NonNull YearMonth date);

		LoadSlot getPurchaseMarketOriginal(SpotMarket market, @NonNull YearMonth date);

	}
