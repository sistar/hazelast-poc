package de.sistar.poc.amq;

import org.apache.camel.spring.Main;

public class FileToAmq {
	public static void main(String[] args) throws Exception {

		Main main = new Main();
		main.setApplicationContextUri("META-INF/spring/camel-context.xml");
		main.enableHangupSupport();
		main.run();
	}
}
