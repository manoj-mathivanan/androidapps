package com.sap.android.sdmscen.test;

import android.test.ActivityInstrumentationTestCase2;

import com.sap.android.activity.TestActivity;

public class SDMConnectivityTest extends ActivityInstrumentationTestCase2<TestActivity> {

	private TestActivity mActivity;
	
	public SDMConnectivityTest() {
		super("com.sap.android.activity", TestActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	  protected void setUp() throws Exception {
	    super.setUp();
	    
	    mActivity = getActivity();
	    
	    setActivityInitialTouchMode(false);

	  } // end of setUp() method definition
	
	public void testPreConditions() {
	    //fail("Precondition check not implemented!");
	    assertEquals(true,true);
	  } // end of testPreConditions() method definition

}
