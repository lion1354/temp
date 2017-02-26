package com.popular.common.id;

public class IdGen {
	static final long idepo = System.currentTimeMillis() - 3600 * 1000L;

	public static long genId() {
		IdWorker idWorker = new IdWorker(idepo);
		return idWorker.getId();
	}
}
