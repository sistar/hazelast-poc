import static org.testng.Assert.assertEquals;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Stopwatch;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HazelTest {
	private List<Map<Integer, String>> maps = new ArrayList();
	public static final long POW = (long) Math.pow(10, 6);
	private SecureRandom random = new SecureRandom();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@BeforeMethod
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testPut() throws Exception {
		Config cfg = new Config();
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
		Map<Integer, String> mapCustomers = instance.getMap("customers");
		fillMapWithRandomData(mapCustomers);
	}

	private void fillMapWithRandomData(Map<Integer, String> mapCustomers) {
		for (int i = 0; i < POW; i++) {
			String s = new BigInteger(130, random).toString(32);
			if (i % 100000 == 0)
				System.out.printf("size of map: %s \n", mapCustomers.size());
			mapCustomers.put(i, s);
		}
	}

	@Test
	public void testTwoMemberMapSizes() {
		Stopwatch stopwatch = new Stopwatch().start();
		Config cfg = new Config();
		maps.add(oneHazelInstance(cfg));
		fillMapWithRandomData(maps.get(0));
		maps.add(oneHazelInstance(cfg));
		checkSizeOfAllMaps();
		reportElapsedTime(stopwatch,maps);
	}

	private void checkSizeOfAllMaps() {
		for (Map<Integer, String> map : maps) {
			assertEquals(POW, map.size());
		}
	}

	private void reportElapsedTime(Stopwatch stopwatch,List<?> list) {
		stopwatch.stop();
		System.out.printf("map-cluster of size %i time: %s \n",list.size(), stopwatch);
		logger.info("map-cluster of size %i time: %s \n",list.size(), stopwatch);
	}

	@Test
	public void testFourMemberMapSizes() {
		Stopwatch stopwatch = new Stopwatch().start();
		Config cfg = new Config();
		Map map1 = oneHazelInstance(cfg);
		maps.add(oneHazelInstance(cfg));
		fillMapWithRandomData(maps.get(0));
		maps.add(oneHazelInstance(cfg));
		maps.add(oneHazelInstance(cfg));
		maps.add(oneHazelInstance(cfg));
		checkSizeOfAllMaps();
		reportElapsedTime(stopwatch,maps);
	}

	private Map oneHazelInstance(Config cfg) {
		HazelcastInstance h1 = Hazelcast.newHazelcastInstance(cfg);
		return h1.getMap("testmap");
	}

}
