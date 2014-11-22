package com.xing.http.connector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xing.container.Container;

/**
 * @author Leo
 * �������ӿ�
 *
 */
public interface Connector {

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
    public Container getContainer();

    /**
     * ��������
     * @param container
     */
    public void setContainer(Container container);
}
