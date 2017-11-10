package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public class Mapper implements IMapperClass {
	private final EcoreUtil.Copier copier;

	Map<BuyOption, LoadSlot> buyMap_orig = new HashMap<>();
	Map<BuyOption, LoadSlot> buyMap_be = new HashMap<>();
	Map<BuyOption, LoadSlot> buyMap_change = new HashMap<>();
	Map<SellOption, DischargeSlot> sellMap_orig = new HashMap<>();
	Map<SellOption, DischargeSlot> sellMap_be = new HashMap<>();
	Map<SellOption, DischargeSlot> sellMap_change = new HashMap<>();

	Map<Pair<YearMonth, SpotMarket>, DischargeSlot> sellMarketMap_original = new HashMap<>();
	Map<Pair<YearMonth, SpotMarket>, DischargeSlot> sellMarketMap_be = new HashMap<>();
	Map<Pair<YearMonth, SpotMarket>, DischargeSlot> sellMarketMap_change = new HashMap<>();

	Map<Pair<YearMonth, SpotMarket>, LoadSlot> buyMarketMap_original = new HashMap<>();
	Map<Pair<YearMonth, SpotMarket>, LoadSlot> buyMarketMap_be = new HashMap<>();
	Map<Pair<YearMonth, SpotMarket>, LoadSlot> buyMarketMap_change = new HashMap<>();

	public Mapper(final EcoreUtil.Copier copier) {
		this.copier = copier;
	}

	@Override
	public LoadSlot getOriginal(final BuyOption buy) {
		return buyMap_orig.get(buy);
	}

	@Override
	public LoadSlot getBreakEven(final BuyOption buy) {
		return buyMap_be.get(buy);
	}

	@Override
	public LoadSlot getChangable(final BuyOption buy) {
		return buyMap_change.get(buy);
	}

	@Override
	public DischargeSlot getOriginal(final SellOption sell) {
		return sellMap_orig.get(sell);
	}

	@Override
	public DischargeSlot getBreakEven(final SellOption sell) {
		return sellMap_be.get(sell);
	}

	@Override
	public DischargeSlot getChangable(final SellOption sell) {
		return sellMap_change.get(sell);
	}

	@Override
	public void addMapping(final BuyOption buy, final LoadSlot original, final LoadSlot breakeven, final LoadSlot changable) {
		buyMap_orig.put(buy, original);
		buyMap_be.put(buy, breakeven);
		buyMap_change.put(buy, changable);
		if (copier.containsKey(buy)) {
			buyMap_orig.put((BuyOption) copier.get(buy), original);
			buyMap_be.put((BuyOption) copier.get(buy), breakeven);
			buyMap_change.put((BuyOption) copier.get(buy), changable);
		} else {
			for (final Map.Entry<EObject, EObject> e : copier.entrySet()) {
				if (e.getValue() == buy) {
					buyMap_orig.put((BuyOption) e.getKey(), original);
					buyMap_be.put((BuyOption) e.getKey(), breakeven);
					buyMap_change.put((BuyOption) e.getKey(), changable);
				}
			}
		}
	}

	@Override
	public void addMapping(final SellOption sell, final DischargeSlot original, final DischargeSlot breakeven, final DischargeSlot changable) {
		sellMap_orig.put(sell, original);
		sellMap_be.put(sell, breakeven);
		sellMap_change.put(sell, changable);
		if (copier.containsKey(sell)) {
			sellMap_orig.put((SellOption) copier.get(sell), original);
			sellMap_be.put((SellOption) copier.get(sell), breakeven);
			sellMap_change.put((SellOption) copier.get(sell), changable);
		} else {
			for (final Map.Entry<EObject, EObject> e : copier.entrySet()) {
				if (e.getValue() == sell) {
					sellMap_orig.put((SellOption) e.getKey(), original);
					sellMap_be.put((SellOption) e.getKey(), breakeven);
					sellMap_change.put((SellOption) e.getKey(), changable);

				}
			}
		}

	}

	public <T extends EObject> T getOriginal(@NonNull final T copy) {

		for (final Map.Entry<EObject, EObject> e : copier.entrySet()) {
			if (e.getValue() == copy) {
				return (T) e.getKey();
			}
		}

		return (T) null;
	}

	public <T extends EObject> T getCopy(@NonNull final T original) {
		return (T) copier.get(original);
	}

	@Override
	public void addMapping(SpotMarket market, YearMonth date, final LoadSlot original, LoadSlot slot_breakEven, LoadSlot slot_changable) {

		Pair<YearMonth, SpotMarket> key = new Pair<>(date, market);

		buyMarketMap_original.put(key, original);
		buyMarketMap_be.put(key, slot_breakEven);
		buyMarketMap_change.put(key, slot_changable);
		if (copier.containsKey(market)) {
			Pair<YearMonth, SpotMarket> key2 = new Pair<>(date, (SpotMarket) copier.get(market));
			buyMarketMap_original.put(key2, original);
			buyMarketMap_be.put(key2, slot_breakEven);
			buyMarketMap_change.put(key2, slot_changable);
		} else {
			for (final Map.Entry<EObject, EObject> e : copier.entrySet()) {
				if (e.getValue() == market) {
					Pair<YearMonth, SpotMarket> key2 = new Pair<>(date, (SpotMarket) e.getKey());
					buyMarketMap_original.put(key2, original);
					buyMarketMap_be.put(key2, slot_breakEven);
					buyMarketMap_change.put(key2, slot_changable);

				}
			}
		}

	}

	@Override
	public void addMapping(SpotMarket market, YearMonth date, final DischargeSlot original, DischargeSlot slot_breakEven, DischargeSlot slot_changable) {
		Pair<YearMonth, SpotMarket> key = new Pair<>(date, market);

		sellMarketMap_original.put(key, original);
		sellMarketMap_be.put(key, slot_breakEven);
		sellMarketMap_change.put(key, slot_changable);
		if (copier.containsKey(market)) {
			Pair<YearMonth, SpotMarket> key2 = new Pair<>(date, (SpotMarket) copier.get(market));
			sellMarketMap_original.put(key2, original);
			sellMarketMap_be.put(key2, slot_breakEven);
			sellMarketMap_change.put(key2, slot_changable);
		} else {
			for (final Map.Entry<EObject, EObject> e : copier.entrySet()) {
				if (e.getValue() == market) {
					Pair<YearMonth, SpotMarket> key2 = new Pair<>(date, (SpotMarket) e.getKey());
					sellMarketMap_original.put(key2, original);
					sellMarketMap_be.put(key2, slot_breakEven);
					sellMarketMap_change.put(key2, slot_changable);

				}
			}
		}

	}

	@Override
	public DischargeSlot getSalesMarketBreakEven(SpotMarket market, @NonNull YearMonth date) {
		return sellMarketMap_be.get(new Pair<>(date, market));
	}

	@Override
	public DischargeSlot getSalesMarketChangable(SpotMarket market, @NonNull YearMonth date) {
		return sellMarketMap_change.get(new Pair<>(date, market));
	}

	@Override
	public LoadSlot getPurchaseMarketBreakEven(SpotMarket market, @NonNull YearMonth date) {
		return buyMarketMap_be.get(new Pair<>(date, market));
	}

	@Override
	public LoadSlot getPurchaseMarketChangable(SpotMarket market, @NonNull YearMonth date) {
		return buyMarketMap_change.get(new Pair<>(date, market));
	}

	@Override
	public DischargeSlot getSalesMarketOriginal(SpotMarket market, @NonNull YearMonth date) {
		return sellMarketMap_original.get(new Pair<>(date, market));

	}

	@Override
	public LoadSlot getPurchaseMarketOriginal(SpotMarket market, @NonNull YearMonth date) {
		return buyMarketMap_original.get(new Pair<>(date, market));

	}
}
