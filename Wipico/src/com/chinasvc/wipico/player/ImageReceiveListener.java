package com.chinasvc.wipico.player;

/**
 * 图片控制接收监听器接口
 * 
 * */
public interface ImageReceiveListener {

	/**
	 * 打开图片
	 * 
	 * @param path
	 *                图片路径
	 * */
	public void openImage(String path);

	/**
	 * 停止
	 * */
	public void stop();

	/**
	 * 左转
	 * */
	public void turnLeft();

	/**
	 * 右转
	 * */
	public void turnRight();

	/**
	 * 放大
	 * */
	public void zoomIn();

	/**
	 * 缩小
	 * */
	public void zoomOut();

	/**
	 * 缩放 onScaleEnd
	 * 
	 * @param currentScale
	 *                当前的缩放比例
	 * @param currentMiddleX
	 *                中心点的x坐标
	 * @param currentMiddleY
	 *                中心点的y坐标
	 * */
	public void onScaleEnd(float currentScale, float currentMiddleX, float currentMiddleY);

	/**
	 * 缩放 onScale
	 * 
	 * @param currentScale
	 *                当前的缩放比例
	 * @param currentMiddleX
	 *                中心点的x坐标
	 * @param currentMiddleY
	 *                中心点的y坐标
	 * */
	public void onScale(float currentScale, float currentMiddleX, float currentMiddleY);

	/**
	 * 滚动
	 * 
	 * @param distanceX
	 *                X坐标
	 * @param distanceY
	 *                Y坐标
	 * */
	public void onScroll(float distanceX, float distanceY);

	/**
	 * 双击图片
	 * 
	 * @param motionEventX
	 *                点击的X坐标
	 * @param motionEventY
	 *                点击的Y坐标
	 * */
	public void onDoubleTap(float motionEventX, float motionEventY);

}