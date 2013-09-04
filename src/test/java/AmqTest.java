import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.testng.AbstractCamelTestNGSpringContextTests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration
@DirtiesContext
public class AmqTest extends AbstractCamelTestNGSpringContextTests {

	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(
			uri = "mock:result", context = "camelContext")
	protected MockEndpoint resultEndpoint;

	@Produce(
			uri = "activemq:incoming", context = "camelContext")
	protected ProducerTemplate start;

	private List<Map<Integer, String>> maps = new ArrayList();
	public static final long POW = (long) Math.pow(10, 6);
	private SecureRandom random = new SecureRandom();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	@DirtiesContext
	public void testShardingFunction() throws Exception {
		Assert.assertEquals(CalculateShardBean.getShardForBrowserId("TEST"), new Integer(4));
		Assert.assertEquals(CalculateShardBean.getShardForBrowserId("TESTx"), new Integer(10));
		Assert.assertEquals(CalculateShardBean.getShardForBrowserId("TESTa"), new Integer(0));
		Assert.assertEquals(CalculateShardBean.getShardForBrowserId("TESTy"), new Integer(11));
	}

	@Test
	@DirtiesContext
	public void testThatMatchingMessageIsRoutedToShard() throws InterruptedException {

		resultEndpoint.expectedMessageCount(4);
		resultEndpoint.setAssertPeriod(100L);

		start.sendBody("TEST");
		start.sendBody("TEST");
		start.sendBody("TEST");
		start.sendBody("TEST");
		resultEndpoint.assertIsSatisfied();
	}

	@Test
    @DirtiesContext
	public void testThatNonMatchingMessageIsNotRoutedToShard() throws InterruptedException {

		resultEndpoint.expectedMessageCount(0);
		resultEndpoint.setAssertPeriod(100L);

		start.sendBody("TEST12");

		resultEndpoint.assertIsSatisfied();
	}

	@Test
    @DirtiesContext
	public void testThatManyMessagesAreRoutedToShard() throws InterruptedException {

		resultEndpoint.expectedMessageCount(10000);
		resultEndpoint.setAssertPeriod(100L);

		for (int i = 0; i < 10000; i++)
			start.sendBody("TEST");

		resultEndpoint.assertIsSatisfied();
	}
}
