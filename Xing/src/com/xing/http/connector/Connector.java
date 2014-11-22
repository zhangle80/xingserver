package com.xing.http.connector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xing.container.Container;

/**
 * @author Leo
 * 链接器接口
 *
 */
public interface Connector {

    /**
     * 创建Request对象
     * @return
     */
    public HttpServletRequest createRequest();

    /**
     * 创建Response对象
     * @return
     */
    public HttpServletResponse createResponse();
    
    /**
     * 返回容器
     * @return
     */
    public Container getContainer();

    /**
     * 设置容器
     * @param container
     */
    public void setContainer(Container container);
}
