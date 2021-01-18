/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.Iterator;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;

public class CargoUtils {
	public static @Nullable LoadSlot getLoadSlot(Cargo cargo) {
		Slot firstElement = CollectionsUtil.getFirstElement(cargo.getSortedSlots());
		LoadSlot load = null;
		if (firstElement != null && firstElement instanceof LoadSlot) {
			load = (LoadSlot) firstElement;
		}
		return load;
	}

	public static @Nullable DischargeSlot getDischargeSlot(Cargo cargo) {
		Slot lastElement = CollectionsUtil.getLastElement(cargo.getSortedSlots());
		DischargeSlot discharge = null;
		if (lastElement != null && lastElement instanceof DischargeSlot) {
			discharge = (DischargeSlot) lastElement;
		}
		return discharge;
	}
	
	/**
	 * Returns an iterable over all the open slots in a particular cargo model, for use in a for loop.
	 * <p>
	 * Example use:
	 * <pre>{@code
	 * for ( Slot<?> slot: CargoUtils.getOpenSlotsIterable(cargoModel) ) {
	 *   ...
	 * }
	 * }</pre>
	 * @param cargoModel
	 * @return An iterable over all the load and discharge slots that do not belong to a Cargo.
	 */
	public static @NonNull Iterable<Slot<?>> getOpenSlotsIterable(CargoModel cargoModel) {
		// annoyingly, Java requires for-loops to be over instances of Iterable, not Iterator
		return new Iterable<Slot<?>>() {

			@Override
			public Iterator<Slot<?>> iterator() {
				return getOpenSlotsIterator(cargoModel);
			}
			
		};
	}
	
	/**
	 * Returns an iterator over all the open slots in a particular cargo model.
	 * <p>
	 * The code is written to provide efficient iteration without copying any data.
	 * 
	 * @param cargoModel
	 * @return An iterator over all the load and discharge slots that do not belong to a Cargo.
	 */
	public static @NonNull Iterator<Slot<?>> getOpenSlotsIterator(CargoModel cargoModel) {
		return new Iterator<Slot<?>>() {
			// we will traverse iterators over the load slots and discharge slots
			private Iterator<?> iterators [] = {
					cargoModel.getLoadSlots().iterator(),
					cargoModel.getDischargeSlots().iterator()				
			};

			// we need to keep track of where we are in our sub-iterator collection 
			private int index = 0;
			
			// we store look-ahead results for hasNext() and next()
			private boolean _hasNext = true;
			private Slot<?> _next = null;

			// initialisation code
			{
				traverse();
			}
			
			/**
			 * Look ahead to find the next open slot, if any, in the cargo model.
			 * Store it as _next, if there is one; otherwise, set _hasNext to false. 
			 */
			private void traverse() {
				do {
					// check if we hit the end of our sub-iterator
					while (!iterators[index].hasNext()) {
						// continue with the next sub-iterator
						index++;
						// unless we have run out of sub-iterators
						if (index >= iterators.length) {
							// then we have no more slots to return
							_hasNext = false;
							return;
						}
					}
					
					_next = (Slot<?>) iterators[index].next();
				}
				// keep clicking forward until we find an open slot
				while (_next == null || _next.getCargo() != null); 
					
			}

			@Override
			public boolean hasNext() {
				// return the cached computation
				return _hasNext;
			}

			@Override
			public Slot<?> next() {
				// store the cached iteration result
				Slot<?> nextResult = _next;
				// cache the next iteration
				traverse();
				// return this iteration's result
				return nextResult;
			}
			
		};

	}
}
