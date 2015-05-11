package server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class RequestRateChecker implements Runnable {

	private Server server;
	private ConcurrentHashMap<InetAddress, long[]> mappings;

	public RequestRateChecker(Server server) {
		this.server = server;
		mappings =  new ConcurrentHashMap<InetAddress, long[]>();
	}

	@Override
	public void run() {
		try {
			while (true) {
				ArrayList<InetAddress> toRemove = new ArrayList<InetAddress>();
				for (InetAddress key : mappings.keySet()) {
					// last entry was a second ago, remove it
					if (System.currentTimeMillis() - mappings.get(key)[0] > 1000) {
						toRemove.add(key);
					} else if (mappings.get(key)[1] > 20) {
						this.server.addToBlacklist(key);
						toRemove.add(key);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addToMap(InetAddress address) {
		try {
			if (!mappings.containsKey(address)) {
				mappings.put(address, new long[] { System.currentTimeMillis(),1 });
			} else {
				mappings.get(address)[0] = System.currentTimeMillis();
				mappings.get(address)[1]++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
