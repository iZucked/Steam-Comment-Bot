/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.recorder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class InterfaceRecorderTest {

	@Test
	public void testCreateRecorder() throws Exception {
		class TestClass implements ITestClass {
			private String message;

			private List<Integer> ints;

			public TestClass() {

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.mmxlabs.optimiser.builder.impl.internal.ITestClass#setMessage
			 * (java.lang.String)
			 */
			@Override
			public void setMessage(String message) {
				this.message = message;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.mmxlabs.optimiser.builder.impl.internal.ITestClass#getMessage
			 * ()
			 */
			@Override
			public String getMessage() {
				return message;
			}

			@Override
			public List<Integer> getList() {
				return ints;
			}

			@Override
			public void setList(final Integer... ints) {
				this.ints = new LinkedList<Integer>();
				for (int i : ints) {
					this.ints.add(i);
				}
			}
			@Override
			public void done() {

			}
		}

		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		InterfaceRecorder recorder = new InterfaceRecorder();

		TestClass object = new TestClass();

		ITestClass rec = recorder
				.createRecorder(ITestClass.class, object, outputStream, "done");

		rec.setMessage("hello");

		rec.done();
		
		System.out.println("Msg is: " + object.getMessage());

	//	rec.setList(1 ,2, 4);
		
		
		byte[] byteArray = outputStream.toByteArray();
		System.out.println(outputStream.toString());
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
		TestClass t2 = new TestClass();
		try {
			recorder.replay(t2, inputStream, "done");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(t2.getMessage());
	}

}
