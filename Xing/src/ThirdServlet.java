import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class ThirdServlet implements Servlet  {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("i am thirdservlet, and i am be destory");
	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("i am thirdservlet, and i am be initing");
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		System.out.println("third service");
		PrintWriter out= arg1.getWriter();
		String info=((HttpServletRequest)arg0).getMethod()+arg0.getProtocol()+((HttpServletRequest)arg0).getRequestURI();
		out.println("request info="+info);
		out.println("Hello. my third service from bin,haha");
	}

}
