import com.google.common.base.Stopwatch;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.testng.AbstractCamelTestNGSpringContextTests;
import org.apache.camel.testng.CamelSpringTestSupport;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
@ContextConfiguration
@DirtiesContext
public class AmqTest extends AbstractCamelTestNGSpringContextTests {


    @Autowired
    protected CamelContext camelContext;


    @EndpointInject(uri = "mock:a", context = "camelContext")
    protected MockEndpoint mockA;

    @EndpointInject(uri = "mock:b", context = "camelContext")
    protected MockEndpoint mockB;

    @EndpointInject(uri = "mock:c", context = "camelContext2")
    protected MockEndpoint mockC;

    @Produce(uri = "direct:start", context = "camelContext")
    protected ProducerTemplate start;

    @Produce(uri = "direct:start2", context = "camelContext2")
    protected ProducerTemplate start2;

	private List<Map<Integer, String>> maps = new ArrayList();
	public static final long POW = (long) Math.pow(10, 6);
	private SecureRandom random = new SecureRandom();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());





	@BeforeMethod
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testInitAmq() {

        start.sendBody("activemq:example.A", "TEST");
		assertEquals(true, true);
	}

}
