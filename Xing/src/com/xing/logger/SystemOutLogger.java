package com.xing.logger;

public class SystemOutLogger extends LoggerBase {

	protected final String info="xing server logger systemout/1.0";
	@Override
	public void log(String message) {
		System.out.println(message);
	}

}
