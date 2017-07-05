package com.mmxlabs.models.lng.transformer.longterm;

import java.util.ArrayList;

public class SequenceTest {
	public static void main(String[] args) {
		ArrayList<Integer> a = new ArrayList<>();
		a.add(1);
//		a.add(2);
//		a.add(3);
		a.add(1,9);
		a.forEach(s->System.out.println(s));
	}
}
