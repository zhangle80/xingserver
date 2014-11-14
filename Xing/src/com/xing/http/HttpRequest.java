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
	 * sessionID是否来自URL
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
	 * 该方法主要的功能是从输入流中读取请求行信息并将相关的信息保存到Request中
	 * @throws ServletException 
	 */
	private void parseRequest() throws IOException, ServletException{
		
		HttpRequestLine requestLine=new HttpRequestLine();
		this.inputstream.readRequestLine(requestLine);
		//获取方法，URI及协议字符串
		String method=new String(requestLine.method,0,requestLine.methodEnd);
		String uri=null;//先置空，由于readRequestLine类中将查询字符串也放进了URI中，但严格的URI定义中是不包含查询字符串的
		String protocol=new String(requestLine.protocol,0,requestLine.protocolEnd);
		//验证是否有效
		if(method.length()<1){
			System.out.println("Missing HTTP request method");
			throw new ServletException("Missing HTTP request method");
		}else if(requestLine.uriEnd<1){
			System.out.println("Missing HTTP request URI");
			throw new ServletException("Missing HTTP request URI");
		}
		//解析查询参数
		int question= requestLine.indexOf("?");
		if(question>0){
			this.queryString=new String(requestLine.uri,question+1,requestLine.uriEnd-question-1);
			uri=new String(requestLine.uri,0,question);
		}else{
			this.queryString=null;
			uri=new String(requestLine.uri,0,requestLine.uriEnd);
		}
		//处理绝对路径URI的情况，即URI中含有HTTP协议的部分，例如：Http://...
		if(!uri.startsWith("/")){//如果不是以/开头
			int pos=uri.indexOf("://");
			if(pos!=1){
				pos = uri.indexOf("/",pos+3);	//查找下一个/,其实就是协议部分结束的标志，同时是URI部分开始的标志
				if(pos==-1){
					uri="";						//有问题
				}else{
					uri=uri.substring(pos);		//从pos开始的字符串部分为URI
				}
			}
		}
		//从URI中解析session ID部分
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
			uri=uri.substring(0, semicolon)+rest;//从URI中将sessionID去掉
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
	 * 修正URI，纠正URI中存在的异常字符串
	 * 假如URI是正确的格式或者异常可以给纠正的话，normalize将会返回相同的或者被纠正后的URI。假如URI不能纠正的话，它将会给认为是非法的并且通常会返回null
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
