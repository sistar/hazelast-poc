package de.sistar.poc.amq;

import org.apache.camel.builder.RouteBuilder;

public class SlurpFileRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:input_data").to("file:output_data");
    }
}
