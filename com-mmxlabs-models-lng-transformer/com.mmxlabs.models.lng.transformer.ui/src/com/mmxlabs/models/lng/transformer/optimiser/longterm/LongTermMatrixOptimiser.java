package com.mmxlabs.models.lng.transformer.optimiser.longterm;
///**
// * Copyright (C) Minimax Labs Ltd., 2010 - 2018
// * All rights reserved.
// */
//package com.mmxlabs.models.lng.transformer.longterm;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import org.eclipse.jdt.annotation.NonNull;
//
//public class LongTermMatrixOptimiser implements ILongTermMatrixOptimiser {
//	
//	private class Data {
//		int x;
//		int y;
//		long value;
//		public Data(int x, int y, long value) {
//			this.x = x;
//			this.y = y;
//			this.value = value;
//		}
//	}
//
//	@Override
//	public boolean[][] findOptimalPairings(long[][] values, boolean[] optionalLoads, boolean[] optionalDischarges, boolean[][] valid,
//			List<Map<String, List<Integer>>> maxSlotsConstraints, List<Map<String, List<Integer>>> minSlotsConstraints) {
////		boolean[][] allocations = new boolean[values.length][values[0].length];
////		Set<Integer> usedLoads = new HashSet<>();
////		Set<Integer> usedDischarges = new HashSet<>();
////		List<Data> sortedData = getSortedData(values, optionalLoads, optionalDischarges);
////		long totalValue = 0;
////		while (sortedData.size() != 0) {
////			Data best = sortedData.remove(0);
////			if (usedLoads.contains(best.x) || usedDischarges.contains(best.y)) {
////				continue;
////			}
////			if (best.value < 0 && optionalLoads[best.x] == true && optionalDischarges[best.y] == true) {
////				continue;
////			}
////			allocations[best.x][best.y] = true;
////			usedLoads.add(best.x);
////			usedDischarges.add(best.y);
////			System.out.println("best:"+best.value);
////			totalValue += best.value;
////		}
////		System.out.println("fitness:"+totalValue);
////		for (int i = 0; i < allocations.length; i++) {
////			double max = 0;
////			for (int j = 0; j < allocations[i].length; j++) {
////				double allVal = allocations[i][j] == false ? 0 : 1;
////				if (allVal > max) max = allVal;
////			}
////			if (max == 0 && !optionalLoads[i]) {
////				System.out.println("load "+i+" missing");
////			}
////		}
////		for (int j = 0; j < allocations[0].length; j++) {
////			double max = 0;
////			for (int i = 0; i < allocations.length; i++) {
////				double allVal = allocations[i][j] == false ? 0 : 1;
////				if (allVal > max) max = allVal;
////			}
////			if (max == 0 && !optionalDischarges[j]) {
////				System.out.println("discharges "+j+" missing");
////			}
////		}
////
////		return allocations;
//		return null;
//	}
//
//	private List<Data> getSortedData(@NonNull Long[][] values, boolean[] optionalLoads, boolean[] optionalDischarges) {
//		List<Data> data = new LinkedList<>();
//		for (int i = 0; i < values.length; i++) {
//			for (int j = 0; j < values[i].length; j++) {
//				if (values[i][j] != null) {
//				data.add(new Data(i, j, values[i][j] + (optionalLoads[i] == false ? 1*6_000_000 : 0) + (optionalDischarges[j] == false ? 1*6_000_000 : 0)));
//				}
//			}
//		}
//		data.sort((a,b) -> Long.compare(a.value, b.value) * -1);
//		return data;
//	}
//	
//
//}
