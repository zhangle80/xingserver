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
import com.xing.http.connector.HttpRequestLine;

import com.xing.http.connector.SocketInputStream;

public class HttpRequest implements HttpServletRequest {
	
	private SocketInputStream inputstream;
	private String requestURI;
	private String queryString;
	private String requestedSessionId;
	/**
	 * sessionID�Ƿ�����URL
	 */
	private boolean requestedSessionURL;
	private String method;
	private String protocol;
	
	public HttpRequest(SocketInputStream inputstream){
		this.inputstream=inputstream;
	}
	
	public void parse() throws IOException, ServletException{
		this.parseRequest();
		this.parseHeaders();
		
	}
	/**
	 * @throws IOException
	 * �÷�����Ҫ�Ĺ����Ǵ��������ж�ȡ��������Ϣ������ص���Ϣ���浽Request��
	 * @throws ServletException 
	 */
	private void parseRequest() throws IOException, ServletException{
		
		HttpRequestLine requestLine=new HttpRequestLine();
		this.inputstream.readRequestLine(requestLine);
		//��ȡ������URI��Э���ַ���
		String method=new String(requestLine.method,0,requestLine.methodEnd);
		String uri=null;//���ÿգ�����readRequestLine���н���ѯ�ַ���Ҳ�Ž���URI�У����ϸ��URI�������ǲ�������ѯ�ַ�����
		String protocol=new String(requestLine.protocol,0,requestLine.protocolEnd);
		//��֤�Ƿ���Ч
		if(method.length()<1){
			System.out.println("Missing HTTP request method");
			throw new ServletException("Missing HTTP request method");
		}else if(requestLine.uriEnd<1){
			System.out.println("Missing HTTP request URI");
			throw new ServletException("Missing HTTP request URI");
		}
		//������ѯ����
		int question= requestLine.indexOf("?");
		if(question>0){
			this.queryString=new String(requestLine.uri,question+1,requestLine.uriEnd-question-1);
			uri=new String(requestLine.uri,0,question);
		}else{
			this.queryString=null;
			uri=new String(requestLine.uri,0,requestLine.uriEnd);
		}
		//�������·��URI���������URI�к���HTTPЭ��Ĳ��֣����磺Http://...
		if(!uri.startsWith("/")){//���������/��ͷ
			int pos=uri.indexOf("://");
			if(pos!=1){
				pos = uri.indexOf("/",pos+3);	//������һ��/,��ʵ����Э�鲿�ֽ����ı�־��ͬʱ��URI���ֿ�ʼ�ı�־
				if(pos==-1){
					uri="";						//������
				}else{
					uri=uri.substring(pos);		//��pos��ʼ���ַ�������ΪURI
				}
			}
		}
		//��URI�н���session ID����
		String match=";jsessionid=";
		int semicolon=uri.indexOf(match);
		if(semicolon>=0){
			String rest=uri.substring(semicolon+match.length());
			int semicolon2=rest.indexOf(";");
			if(semicolon2>=0){
				this.requestedSessionId=rest.substring(0, semicolon2);
				rest=rest.substring(semicolon2);
			}else{
				this.requestedSessionId=rest;
				rest="";
			}
			this.requestedSessionURL=true;
			uri=uri.substring(0, semicolon)+rest;//��URI�н�sessionIDȥ��
		}else{
			this.requestedSessionId=null;
			this.requestedSessionURL=false;
		}
		
		String normalizedUri = normalize(uri);
		this.method=method;
		this.protocol=protocol;
		
		if(normalizedUri!=null){
			this.requestURI=normalizedUri;
		}else{
			this.requestURI=uri;
		}
		
		if(normalizedUri==null){
			throw new ServletException("Invalid URI:"+uri+"'");
		}
	}
	
	/**
	 * ����URI������URI�д��ڵ��쳣�ַ���
	 * ����URI����ȷ�ĸ�ʽ�����쳣���Ը������Ļ���normalize���᷵����ͬ�Ļ��߱��������URI������URI���ܾ����Ļ������������Ϊ�ǷǷ��Ĳ���ͨ���᷵��null
	 * @param uri
	 * @return
	 */
	private String normalize(String uri){
		return uri;
	}
	
	private void parseHeaders(){
		
	}

	@Override
	public boolean authenticate(HttpServletResponse arg0) throws IOException,
			ServletException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getDateHeader(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIntHeader(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return this.method;
	}

	@Override
	public Part getPart(String arg0) throws IOException, IllegalStateException,
			ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Part> getParts() throws IOException,
			IllegalStateException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return this.queryString;
	}

	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestURI() {
		// TODO Auto-generated method stub
		return this.requestURI;
	}

	@Override
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return this.requestedSessionId;
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return this.requestedSessionURL;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return this.requestedSessionURL;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void login(String arg0, String arg1) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout() throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public AsyncContext getAsyncContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getContentLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameter(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParameterValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProtocol() {
		// TODO Auto-generated method stub
		return this.protocol;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

	}

	@Override
	public AsyncContext startAsync() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
