import static org.springframework.util.Assert.hasLength;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.Hashing;

public class CalculateShardBean {
	private static final Logger LOG = LoggerFactory.getLogger(CalculateShardBean.class);
	public static final String SHARD_VALUE_HEADER_KEY = "shardValue";

	public String process(String body, @Header(Exchange.SLIP_ENDPOINT) String previous) throws Exception {
		if (previous != null) {
			return null;
		} else {
			String browser_id = body;
			return getShardUriForBrowserId(browser_id);
		}
	}

	public static String getShardUriForBrowserId(final String browserId) {
		Integer shard = getShardForBrowserId(browserId);
		String shard_uri = String.format("activemq:shard_%02d", shard);
		LOG.debug("browser_id: {} -> shard_uri: {}", browserId, shard_uri);
		return shard_uri;
	}

	public static Integer getShardForBrowserId(final String browserId) {
		hasLength(browserId, "BrowserId is missing");
		int shard = Math.abs(Hashing.md5().hashString(browserId).asInt()) % 12;
		return shard;
	}

}
