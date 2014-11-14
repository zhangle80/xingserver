package com.xing.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpResponseFacade implements HttpServletResponse {

	private HttpResponse response;
	
	public HttpResponseFacade(HttpResponse response){
		this.response=response;
	}
	@Override
	public void addCookie(Cookie arg0) {
		this.response.addCookie(arg0);
	}

	@Override
	public void addDateHeader(String arg0, long arg1) {
		this.response.addDateHeader(arg0, arg1);
	}

	@Override
	public void addHeader(String arg0, String arg1) {
		this.response.addHeader(arg0, arg1);
	}

	@Override
	public void addIntHeader(String arg0, int arg1) {
		this.response.addIntHeader(arg0, arg1);
	}

	@Override
	public boolean containsHeader(String arg0) {
		return this.response.containsHeader(arg0);
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		return this.response.encodeRedirectURL(arg0);
	}

	@Override
	public String encodeRedirectUrl(String arg0) {
		return this.response.encodeRedirectUrl(arg0);
	}

	@Override
	public String encodeURL(String arg0) {
		return this.response.encodeURL(arg0);
	}

	@Override
	public String encodeUrl(String arg0) {
		return this.response.encodeUrl(arg0);
	}

	@Override
	public String getHeader(String arg0) {
		return this.response.getHeader(arg0);
	}

	@Override
	public Collection<String> getHeaderNames() {
		return this.response.getHeaderNames();
	}

	@Override
	public Collection<String> getHeaders(String arg0) {
		return this.response.getHeaders(arg0);
	}

	@Override
	public int getStatus() {
		return this.response.getStatus();
	}

	@Override
	public void sendError(int arg0) throws IOException {
		this.response.sendError(arg0);
	}

	@Override
	public void sendError(int arg0, String arg1) throws IOException {
		this.response.sendError(arg0, arg1);
	}

	@Override
	public void sendRedirect(String arg0) throws IOException {
		this.response.sendRedirect(arg0);
	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		this.response.setDateHeader(arg0, arg1);
	}

	@Override
	public void setHeader(String arg0, String arg1) {
		this.response.setHeader(arg0, arg1);
	}

	@Override
	public void setIntHeader(String arg0, int arg1) {
		this.response.setIntHeader(arg0, arg1);
	}

	@Override
	public void setStatus(int arg0) {
		this.response.setStatus(arg0);
	}

	@Override
	public void setStatus(int arg0, String arg1) {
		this.response.setStatus(arg0, arg1);
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
