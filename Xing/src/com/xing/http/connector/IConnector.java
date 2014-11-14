package com.xing.http.connector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xing.container.IContainer;

/**
 * @author Leo
 * �������ӿ�
 *
 */
public interface IConnector {

    /**
     * ����Request����
     * @return
     */
    public HttpServletRequest createRequest();

    /**
     * ����Response����
     * @return
     */
    public HttpServletResponse createResponse();
    
    /**
     * ��������
     * @return
     */
    public IContainer getContainer();

    /**
     * ��������
     * @param container
     */
    public void setContainer(IContainer container);
}
