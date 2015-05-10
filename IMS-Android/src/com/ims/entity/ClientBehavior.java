package com.ims.entity;
/**
 * 客户端行为接口
 * @author Jayin Ton
 *
 */
public interface ClientBehavior {
	/**
	 * 下载资料
	 */
	public void downloadData();
    /**
     * 同步屏幕
     */
	public void display();
    /**
     * 用户验证
     * @return
     */
	public boolean identity();
   /**
    * 提问
    * @return
    */
	public boolean ask();
	/**
	 * 等待
	 */
	public void waitting();


}
