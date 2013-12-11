package gov.nasa.arc.mct.dbpersistence.service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A Step-Behind Cache is always "one step behind" 
 * the ground truth - when a read is requested, it returns 
 * the last version of the value it read immediately, 
 * then asynchronously looks up the next version of the 
 * value.
 * 
 * This is useful when:
 * - You want up-to-date values to be recognized at 
 *   some point, but not within any fixed time period.
 * - You must get a useful return value quickly, if 
 *   possible.
 * - You do not want to spend time polling in the 
 *   background.
 * 
 * @author vwoeltje
 *
 */
public class StepBehindCache<T> {
	private AtomicReference<T> cache = 
			new AtomicReference<T>(null);
	private Lookup<T> lookup;
	private long lastLookup = 0L;
	private long period = 1000L;

	public StepBehindCache (Lookup<T> lookup, long delay) {
		this.lookup = lookup;
		this.period = delay;
	}
	
	public StepBehindCache (Lookup<T> lookup) {
		this.lookup = lookup;
	}
	
	public T get() {
		T cached = cache.get();
		if (cached == null) {
			cached = lookup.lookup();
			cache.set(cached);
		} else {
			long now = System.currentTimeMillis();
			if (lastLookup < now - period) {
				lastLookup = now;
				backgroundLookup();
			}
		}
		return cached;
	}
	
	private void backgroundLookup() {
		new Thread() {
			public void run() {
				cache.set(lookup.lookup());
			}
		}.start();
	}

	public static interface Lookup<S> {
		public S lookup();
	}
}
