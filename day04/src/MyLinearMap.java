import sun.awt.SunHints;

import java.util.*;

/**
 * Implementation of a Map using a List of entries, so most
 * operations are linear time.
 */
public class MyLinearMap<K, V> implements Map<K, V> {

	private List<Entry> entries = new ArrayList<Entry>();

	public class Entry implements Map.Entry<K, V> {
		private K key;
		private V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}
		@Override
		public V getValue() {
			return value;
		}
		@Override
		public V setValue(V newValue) {
			value = newValue;
			return value;
		}
	}

	@Override
	public void clear() {
		entries.clear();
	}

	@Override
	public boolean containsKey(Object target) {
		return findEntry(target) != null;
	}

	// Returns the entry that contains the target key, or null if there is none.
	private Entry findEntry(Object target) {
		for (int i = 0; i < entries.size(); i++){ //iterate through entries to see if key exists
            if (entries.get(i).getKey() == null || target == null){ //if entry key or target key is null
                if (entries.get(i).getKey() == null && target == null){ //compare them manually
                    return entries.get(i); //because using equals() throws an error
                }
            }
            else if (entries.get(i).getKey().equals(target)){ //if neither key is null, we use equals() to compare them
			    return entries.get(i);
            }
		}
		return null; //	throw new NoSuchElementException;
    }

	// Compares two keys or two values, handling null correctly.
	private boolean equals(Object target, Object obj) {
		if (target == null) {
			return obj == null;
		}
		return target.equals(obj);
	}

	@Override
	public boolean containsValue(Object target) {
		for (Map.Entry<K, V> entry: entries) {
			if (equals(target, entry.getValue())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V get(Object key) {
		Entry gotEntry = findEntry(key); //if there is no entry, findEntry will return null
		if (gotEntry != null) {
		    return gotEntry.getValue();
        }
		return null; //throw new NoSuchElementException();
	}

	@Override
	public boolean isEmpty() {
		return entries.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new HashSet<K>();
		for (Entry entry: entries) {
			set.add(entry.getKey());
		}
		return set;
	}

	@Override
	public V put(K key, V value) {
	    if (containsKey(key) != true) { //if there is no entry with that key
            Entry putEntry = new Entry(key, value); //make new entry,
            entries.add(putEntry); //append to entries list
            return null; //nothing to return since it didn't exist
	    } else { //if entry already exists
	        V objectValue = get(key); //get old value
	        remove(key); //remove it
            Entry putEntry = new Entry(key, value);
            entries.add(putEntry); //put new entry in
            return objectValue; //return old value
        }
    }

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> entry: map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V remove(Object key) {
	    Entry gotEntry = findEntry(key); //get entry so we can remove it (need entry object to remove, not just key)
        if (gotEntry == null) { //if the entry doesn't exist, we return null
            return null; //	throw new NoSuchElementException;
        }
        entries.remove(gotEntry);
        return gotEntry.getValue(); //can't tell diff between value = null and entry = null (doesn't exist)
	}

	@Override
	public int size() {
		return entries.size();
	}

	@Override
	public Collection<V> values() {
		Set<V> set = new HashSet<V>();
		for (Entry entry: entries) {
			set.add(entry.getValue());
		}
		return set;
	}

	public static void main(String[] args) {
		Map<String, Integer> map = new MyLinearMap<String, Integer>();
		map.put("Word1", 1);
		map.put("Word2", 2);
		Integer value = map.get("Word1");
		System.out.println(value);

		for (String key: map.keySet()) {
			System.out.println(key + ", " + map.get(key));
		}
	}

	/**
	 * Returns a reference to `entries`.
	 *
	 * This is not part of the Map interface; it is here to provide the functionality
	 * of `entrySet` in a way that is substantially simpler than the "right" way.
	 *
	 * @return
	 */
	protected Collection<? extends java.util.Map.Entry<K, V>> getEntries() {
		return entries;
	}
}
