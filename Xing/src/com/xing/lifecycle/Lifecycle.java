package com.xing.lifecycle;

/**
 * @author leo
 *	�������ں���,�������״̬�仯,�Լ�֪ͨ������ע��������ִ�ж���
 *	֪ͨ���õ��ǹ۲���ģʽ,�ýӿ��ǹ۲���ģʽ�е�ͳ���߽ӿ�
 */
public interface Lifecycle {
	public static final String START_EVENT = "start";
	public static final String BEFORE_START_EVENT = "before_start";
	public static final String AFTER_START_EVENT = "after_start";
	public static final String STOP_EVENT = "stop";
	public static final String BEFORE_STOP_EVENT = "before_stop";
	public static final String AFTER_STOP_EVENT = "after_stop";
	/**
	 * Lifecycle��ʼ��
	 */
	public void initialize();
	
	/**
	 * Lifecycle��ʼ
	 */
	public void start();
	
	/**
	 * Lifecycle����
	 */
	public void stop();
	
	/**
	 * Lifecycle��ӹ۲���
	 * @param listener
	 */
	public void addLifecycleListener(LifecycleListener listener);
	
	/**
	 * �������й۲���
	 * @return
	 */
	public LifecycleListener[] findLifecycleListeners();
	
	/**
	 * �Ƴ��۲���
	 * @param listener
	 */
	public void removeLifecycleListener(LifecycleListener listener);

}
