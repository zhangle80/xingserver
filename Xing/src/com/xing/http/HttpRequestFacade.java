package com.xing.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class HttpRequestFacade implements HttpServletRequest {
	private HttpRequest request;
	
	public HttpRequestFacade(HttpRequest request){
		this.request=request;
	}

	@Override
	public boolean authenticate(HttpServletResponse arg0) throws IOException,
			ServletException {
		return this.request.authenticate(arg0);
	}

	@Override
	public String getAuthType() {
		return this.request.getAuthType();
	}

	@Override
	public String getContextPath() {
		return this.request.getContextPath();
	}

	@Override
	public Cookie[] getCookies() {
		return this.request.getCookies();
	}

	@Override
	public long getDateHeader(String arg0) {
		return this.request.getDateHeader(arg0);
	}

	@Override
	public String getHeader(String arg0) {
		return this.request.getHeader(arg0);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return this.getHeaderNames();
	}

	@Override
	public Enumeration<String> getHeaders(String arg0) {
		return this.getHeaders(arg0);
	}

	@Override
	public int getIntHeader(String arg0) {
		return this.getIntHeader(arg0);
	}

	@Override
	public String getMethod() {
		return this.request.getMethod();
	}

	@Override
	public Part getPart(String arg0) throws IOException, IllegalStateException,
			ServletException {
		return this.request.getPart(arg0);
	}

	@Override
	public Collection<Part> getParts() throws IOException,
			IllegalStateException, ServletException {
		return this.request.getParts();
	}

	@Override
	public String getPathInfo() {
		return this.request.getPathInfo();
	}

	@Override
	public String getPathTranslated() {
		return this.request.getPathTranslated();
	}

	@Override
	public String getQueryString() {
		return this.request.getQueryString();
	}

	@Override
	public String getRemoteUser() {
		return this.request.getRemoteUser();
	}

	@Override
	public String getRequestURI() {
		return this.request.getRequestURI();
	}

	@Override
	public StringBuffer getRequestURL() {
		return this.request.getRequestURL();
	}

	@Override
	public String getRequestedSessionId() {
		return this.request.getRequestedSessionId();
	}

	@Override
	public String getServletPath() {
		return this.request.getServletPath();
	}

	@Override
	public HttpSession getSession() {
		return this.request.getSession();
	}

	@Override
	public HttpSession getSession(boolean arg0) {
		return this.request.getSession();
	}

	@Override
	public Principal getUserPrincipal() {
		return this.request.getUserPrincipal();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return this.request.isRequestedSessionIdFromCookie();
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return this.request.isRequestedSessionIdFromURL();
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return this.request.isRequestedSessionIdFromUrl();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return this.request.isRequestedSessionIdValid();
	}

	@Override
	public boolean isUserInRole(String arg0) {
		return this.request.isUserInRole(arg0);
	}

	@Override
	public void login(String arg0, String arg1) throws ServletException {
		this.request.login(arg0, arg1);
	}

	@Override
	public void logout() throws ServletException {
		this.request.logout();
	}

	@Override
	public AsyncContext getAsyncContext() {
		return this.request.getAsyncContext();
	}

	@Override
	public Object getAttribute(String arg0) {
		return this.request.getAttribute(arg0);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return this.request.getAttributeNames();
	}

	@Override
	public String getCharacterEncoding() {
		return this.request.getCharacterEncoding();
	}

	@Override
	public int getContentLength() {
		return this.request.getContentLength();
	}

	@Override
	public String getContentType() {
		return this.request.getContentType();
	}

	@Override
	public DispatcherType getDispatcherType() {
		return this.request.getDispatcherType();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return this.request.getInputStream();
	}

	@Override
	public String getLocalAddr() {
		return this.request.getLocalAddr();
	}

	@Override
	public String getLocalName() {
		return this.request.getLocalName();
	}

	@Override
	public int getLocalPort() {
		return this.request.getLocalPort();
	}

	@Override
	public Locale getLocale() {
		return this.request.getLocale();
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return this.request.getLocales();
	}

	@Override
	public String getParameter(String arg0) {
		return this.request.getParameter(arg0);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.request.getParameterMap();
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return this.request.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String arg0) {
		return this.request.getParameterValues(arg0);
	}

	@Override
	public String getProtocol() {
		return this.request.getProtocol();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return this.request.getReader();
	}

	@Override
	public String getRealPath(String arg0) {
		return this.request.getRealPath(arg0);
	}

	@Override
	public String getRemoteAddr() {
		return this.request.getRemoteAddr();
	}

	@Override
	public String getRemoteHost() {
		return this.request.getRemoteHost();
	}

	@Override
	public int getRemotePort() {
		return this.request.getRemotePort();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return this.request.getRequestDispatcher(arg0);
	}

	@Override
	public String getScheme() {
		return this.request.getScheme();
	}

	@Override
	public String getServerName() {
		return this.request.getServerName();
	}

	@Override
	public int getServerPort() {
		return this.request.getServerPort();
	}

	@Override
	public ServletContext getServletContext() {
		return this.request.getServletContext();
	}

	@Override
	public boolean isAsyncStarted() {
		return this.request.isAsyncStarted();
	}

	@Override
	public boolean isAsyncSupported() {
		return this.request.isAsyncSupported();
	}

	@Override
	public boolean isSecure() {
		return this.request.isSecure();
	}

	@Override
	public void removeAttribute(String arg0) {
		this.request.removeAttribute(arg0);
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		this.request.setAttribute(arg0, arg1);
	}

	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		this.request.setCharacterEncoding(arg0);
	}

	@Override
	public AsyncContext startAsync() {
		return this.request.startAsync();
	}

	@Override
	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) {
		return this.request.startAsync(arg0, arg1);
	}

}
