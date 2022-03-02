/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.strings;

import java.util.HashMap;
import java.util.Map;

public class DamerauLevenshteinDistance implements StringDistance {
	
	private static final int INSERT_COST = 1;
	private static final int DELETE_COST = 1;
	private static final int REPLACE_COST = 1;
	private static final int SWAP_COST = 1;
	
	
	
	/* Copyright (c) 2012 Kevin L. Stern
	 * 
	 * Permission is hereby granted, free of charge, to any person obtaining a copy
	 * of this software and associated documentation files (the "Software"), to deal
	 * in the Software without restriction, including without limitation the rights
	 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	 * copies of the Software, and to permit persons to whom the Software is
	 * furnished to do so, subject to the following conditions:
	 * 
	 * The above copyright notice and this permission notice shall be included in
	 * all copies or substantial portions of the Software.
	 * 
	 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	 * SOFTWARE.
	 */
	
	/**
	 * from: https://github.com/KevinStern/software-and-algorithms/blob/master/src/main/java/blogspot/software_and_algorithms/stern_library/string/DamerauLevenshteinAlgorithm.java
	 */
	public int distance(String source, String target) {
	    if (source.length() == 0) {
	      return target.length() * INSERT_COST;
	    }
	    if (target.length() == 0) {
	      return source.length() * DELETE_COST;
	    }
	    int[][] table = new int[source.length()][target.length()];
	    Map<Character, Integer> sourceIndexByCharacter = new HashMap<>();
	    if (source.charAt(0) != target.charAt(0)) {
	      table[0][0] = Math.min(REPLACE_COST, DELETE_COST + INSERT_COST);
	    }
	    sourceIndexByCharacter.put(source.charAt(0), 0);
	    for (int i = 1; i < source.length(); i++) {
	      int deleteDistance = table[i - 1][0] + DELETE_COST;
	      int insertDistance = (i + 1) * DELETE_COST + INSERT_COST;
	      int matchDistance = i * DELETE_COST
	          + (source.charAt(i) == target.charAt(0) ? 0 : REPLACE_COST);
	      table[i][0] = Math.min(Math.min(deleteDistance, insertDistance),
	                             matchDistance);
	    }
	    for (int j = 1; j < target.length(); j++) {
	      int deleteDistance = (j + 1) * INSERT_COST + DELETE_COST;
	      int insertDistance = table[0][j - 1] + INSERT_COST;
	      int matchDistance = j * INSERT_COST
	          + (source.charAt(0) == target.charAt(j) ? 0 : REPLACE_COST);
	      table[0][j] = Math.min(Math.min(deleteDistance, insertDistance),
	                             matchDistance);
	    }
	    for (int i = 1; i < source.length(); i++) {
	      int maxSourceLetterMatchIndex = source.charAt(i) == target.charAt(0) ? 0
	          : -1;
	      for (int j = 1; j < target.length(); j++) {
	        Integer candidateSwapIndex = sourceIndexByCharacter.get(target
	            .charAt(j));
	        int jSwap = maxSourceLetterMatchIndex;
	        int deleteDistance = table[i - 1][j] + DELETE_COST;
	        int insertDistance = table[i][j - 1] + INSERT_COST;
	        int matchDistance = table[i - 1][j - 1];
	        if (source.charAt(i) != target.charAt(j)) {
	          matchDistance += REPLACE_COST;
	        } else {
	          maxSourceLetterMatchIndex = j;
	        }
	        int swapDistance;
	        if (candidateSwapIndex != null && jSwap != -1) {
	          int iSwap = candidateSwapIndex;
	          int preSwapCost;
	          if (iSwap == 0 && jSwap == 0) {
	            preSwapCost = 0;
	          } else {
	            preSwapCost = table[Math.max(0, iSwap - 1)][Math.max(0, jSwap - 1)];
	          }
	          swapDistance = preSwapCost + (i - iSwap - 1) * DELETE_COST
	              + (j - jSwap - 1) * INSERT_COST + SWAP_COST;
	        } else {
	          swapDistance = Integer.MAX_VALUE;
	        }
	        table[i][j] = Math.min(Math.min(Math
	            .min(deleteDistance, insertDistance), matchDistance), swapDistance);
	      }
	      sourceIndexByCharacter.put(source.charAt(i), i);
	    }
	    return table[source.length() - 1][target.length() - 1];
	  }
}



