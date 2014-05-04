package com.sap.sample;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import com.sap.mobile.lib.supportability.ISDMLogger;

import android.app.Activity;
import android.text.format.Time;
import android.util.Log;

public final class MyLogger implements ISDMLogger {

	/**
	 * Log level FATAL
	 */
	public static final int FATAL = 8;

	private volatile Vector<MyLogEntry> logData;

	public Enumeration<MyLogEntry> getLogElements() {
		return logData.elements();
	}

	public MyLogger() {
		logData = new Vector<MyLogEntry>();
	}

//	@Override
	public boolean canLog() {
		return logData != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.mobile.lib.ISDMLogger#d(java.lang.String, java.lang.String)
	 */
//	@Override
	public void d(String tag, String msg) {
		if (canLog())
			logData.add(new MyLogEntry(DEBUG, tag, msg));
		Log.d(tag, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.mobile.lib.ISDMLogger#d(java.lang.String, java.lang.String,
	 * java.lang.Throwable)
	 */
//	@Override
	public void d(String tag, String msg, Throwable tr) {
		if (canLog())
			logData.add(new MyLogEntry(DEBUG, tag, msg, tr));
		Log.d(tag, msg, tr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.mobile.lib.ISDMLogger#e(java.lang.String, java.lang.String)
	 */
//	@Override
	public void e(String tag, String msg) {
		if (canLog())
			logData.add(new MyLogEntry(ERROR, tag, msg));
		Log.e(tag, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.mobile.lib.ISDMLogger#e(java.lang.String, java.lang.String,
	 * java.lang.Throwable)
	 */
//	@Override
	public void e(String tag, String msg, Throwable tr) {
		if (canLog())
			logData.add(new MyLogEntry(ERROR, tag, msg, tr));
		Log.e(tag, msg, tr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.mobile.lib.ISDMLogger#w(java.lang.String,
	 * java.lang.Throwable)
	 */
//	@Override
	public void w(String tag, Throwable tr) {
		if (canLog())
			logData.add(new MyLogEntry(ERROR, tag, tr));
		Log.w(tag, tr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.mobile.lib.ISDMLogger#w(java.lang.String, java.lang.String,
	 * java.lang.Throwable)
	 */
	public void w(String tag, String msg, Throwable tr) {
		if (canLog())
			logData.add(new MyLogEntry(ERROR, tag, msg, tr));
		Log.w(tag, msg, tr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.mobile.lib.ISDMLogger#w(java.lang.String, java.lang.String)
	 */
//	@Override
	public void w(String tag, String msg) {
		if (canLog())
			logData.add(new MyLogEntry(WARN, tag, msg));
		Log.w(tag, msg);
	}

//	@Override
	public void i(String tag, String msg) {
		if (canLog())
			logData.add(new MyLogEntry(INFO, tag, msg));
		Log.i(tag, msg);
	}

//	@Override
	public void i(String tag, String msg, Throwable tr) {
		if (canLog())
			logData.add(new MyLogEntry(INFO, tag, msg, tr));
		Log.i(tag, msg, tr);
	}

//	@Override
	public void v(String tag, String msg) {
		if (canLog())
			logData.add(new MyLogEntry(VERBOSE, tag, msg));
		Log.v(tag, msg);
	}

//	@Override
	public void v(String tag, String msg, Throwable tr) {
		if (canLog())
			logData.add(new MyLogEntry(VERBOSE, tag, msg, tr));
		Log.v(tag, msg, tr);
	}

	public void wtf(String tag, Throwable tr) {
		if (canLog())
			logData.add(new MyLogEntry(FATAL, tag, tr));
		Log.wtf(tag, tr);
	}

	public void wtf(String tag, String msg) {
		if (canLog())
			logData.add(new MyLogEntry(FATAL, tag, msg));
		Log.wtf(tag, msg);
	}

	public void wtf(String tag, String msg, Throwable tr) {
		if (canLog())
			logData.add(new MyLogEntry(FATAL, tag, msg, tr));
		Log.i(tag, msg, tr);
	}

	/**
	 * Deletes all entries weaker than WARN priority
	 */
	public void cleanUp() {
		Iterator<MyLogEntry> it = logData.iterator();
		Vector<MyLogEntry> deleted = new Vector<MyLogEntry>();
		while (it.hasNext()) {
			final MyLogEntry entry = it.next();
			if (entry.getPriority() < WARN)
				deleted.add(entry);
		}
		logData.removeAll(deleted);
	}

	public void terminate() {
		logData.clear();
	}
	
	public String getLogsAsString(final int minLogLevel) {
		StringBuilder ret = new StringBuilder();
		Iterator<MyLogEntry> it = logData.iterator();
		while (it.hasNext()) {
			final MyLogEntry entry = it.next();
			if (entry.getPriority() >= minLogLevel) {
				Time t = new Time();
				t.set(entry.getWhen());
				ret.append(t.format("%T"));
				ret.append(" ");
				ret.append(entry.getMessage());
				ret.append("\n");
			}
		}
		if (ret.length() == 0)
			ret.append("-- no log entries --");
		return ret.toString();
	}

	public int getLogLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void p(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void setLogLevel(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public void d(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	public void d(String arg0, String arg1, Throwable arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}

	public void e(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	public void e(String arg0, String arg1, Throwable arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}

	public void i(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	public void i(String arg0, String arg1, Throwable arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}

	public void logFullLocation(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean logsFullLocation() {
		// TODO Auto-generated method stub
		return false;
	}

	public void p(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	public void setHeaderData(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, String arg6, String arg7,
			String arg8) {
		// TODO Auto-generated method stub
		
	}

	public void v(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	public void v(String arg0, String arg1, Throwable arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}

	public void w(String arg0, Throwable arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	public void w(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	public void w(String arg0, String arg1, Throwable arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}

	public void wtf(String arg0, Throwable arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	public void wtf(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	public void wtf(String arg0, String arg1, Throwable arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}
}

