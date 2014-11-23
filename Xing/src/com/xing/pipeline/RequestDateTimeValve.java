package com.xing.pipeline;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;

import com.xing.http.HttpRequest;
import com.xing.http.HttpResponse;

/**
 * �÷����޾�����;�����ڲ�����ˮ���Ƿ�������
 * @author Leo
 *
 */
public class RequestDateTimeValve implements Valve {

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invoke(HttpRequest request, HttpResponse response,
			ValveContext valveContext) throws ServletException, IOException {
		valveContext.invokeNext(request, response);
		long time=System.currentTimeMillis();
		Date date=new Date(time);
		System.out.println("helle boy,this valve is display time,current time="+date.toString());
	}

}
