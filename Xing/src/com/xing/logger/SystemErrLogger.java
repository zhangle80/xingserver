package com.xing.logger;

public class SystemErrLogger extends LoggerBase {

	protected final String info="xing server logger systemerr/1.0";
	@Override
	public void log(String message) {
		System.err.println(message);
	}

}
