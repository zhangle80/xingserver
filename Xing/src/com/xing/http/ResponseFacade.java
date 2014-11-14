package com.xing.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

public class ResponseFacade implements ServletResponse {
	private Response response;

	public ResponseFacade(Response response){
		this.response=response;
	}

	@Override
	public void flushBuffer() throws IOException {
		this.response.flushBuffer();
	}

	@Override
	public int getBufferSize() {
		return this.response.getBufferSize();
	}

	@Override
	public String getCharacterEncoding() {
		return this.response.getCharacterEncoding();
	}

	@Override
	public String getContentType() {
		return this.response.getContentType();
	}

	@Override
	public Locale getLocale() {
		return this.response.getLocale();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return this.response.getOutputStream();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return this.response.getWriter();
	}

	@Override
	public boolean isCommitted() {
		return this.response.isCommitted();
	}

	@Override
	public void reset() {
		this.response.reset();
	}

	@Override
	public void resetBuffer() {
		this.response.resetBuffer();
	}

	@Override
	public void setBufferSize(int arg0) {
		this.response.setBufferSize(arg0);
	}

	@Override
	public void setCharacterEncoding(String arg0) {
		this.response.setCharacterEncoding(arg0);
	}

	@Override
	public void setContentLength(int arg0) {
		this.response.setContentLength(arg0);
	}

	@Override
	public void setContentType(String arg0) {
		this.response.setContentType(arg0);
	}

	@Override
	public void setLocale(Locale arg0) {
		this.response.setLocale(arg0);
	}

}
