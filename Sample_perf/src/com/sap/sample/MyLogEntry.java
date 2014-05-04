package com.sap.sample;

import android.app.Activity;

public final class MyLogEntry {
	private long when;
	private int priority;
	private String tag;
	private String message;
	private Throwable ex;

	public MyLogEntry ( final int priority, final long when ) {
		setPriority(priority);
		setWhen(when);
	}
	public MyLogEntry ( final int priority, final String tag, final String message) {
		setPriority(priority);
		setWhen(System.currentTimeMillis());
		setTag(tag);
		setMessage(message);
	}
	public MyLogEntry ( final int priority, final String tag, final String message, final Throwable ex) {
		this(priority, tag, message);
		setThrowable(ex);
	}
	public MyLogEntry( final int priority, final String tag, Throwable tr) {
		this(priority, tag);
		setThrowable(tr);
	}
	public MyLogEntry(final int priority, final String tag) {
		setWhen(System.currentTimeMillis());
		setPriority(priority);
		setTag(tag);
	}
	public void setWhen(long when) {
		this.when = when;
	}
	public long getWhen() {
		return when;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getTag() {
		return tag;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setThrowable(Throwable ex) {
		this.ex = ex;
	}
	public Throwable getThrowable() {
		return ex;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getPriority() {
		return priority;
	}
}

