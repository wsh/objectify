package com.googlecode.objectify.impl;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * The new datastore SDK has a neat structure of interfaces and implementations (transaction, datastorereader, etc)
 * but doesn't currently support async operations. We need to shim in a Future-based API so that we can seamlessly
 * support it when it becomes available. We'll remove this parallel hierarchy then.
 */
public interface AsyncDatastoreReaderWriter {
	/** Odd that this is array and the others are iterable, but that's how the underlying API is */
	Future<Map<Key, Entity>> get(final Key... keys);

	<T> QueryResults<T> run(final Query<T> query);

	Future<Void> delete(final Iterable<Key> keys);

	Future<List<Key>> put(final Iterable<? extends FullEntity<?>> entities);

	default Future<Map<Key, Entity>> get(final Collection<Key> keys) {
		return get(keys.toArray(new Key[keys.size()]));
	}

	default Future<List<Key>> put(final FullEntity<?>... entities) {
		return put(Arrays.asList(entities));
	}

}