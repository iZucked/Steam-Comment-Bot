package com.mmxlabs.common.recorder;

import java.util.List;

interface ITestClass {

	public void setMessage(String message);

	public String getMessage();

	List<Integer> getList();
	
	void setList(Integer ...  ints);

	void done();
}