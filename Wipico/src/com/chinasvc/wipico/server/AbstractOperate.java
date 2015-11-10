package com.chinasvc.wipico.server;

import java.util.List;

import com.chinasvc.wipico.bean.Device;
import com.chinasvc.wipico.bean.ImageTransform;
import com.chinasvc.wipico.bean.PhoneDevice;

/**
 * 服务器控制接口回调类
 * 
 * 
 * @since 1.0.0
 * */
public class AbstractOperate implements OperateListener {

	@Override
	public void openFile(String filePath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void verification(int type, String devceName, String verification) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openAppByClass(String mPackage, String mClass, int flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openAppByAction(String action) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openHDMI() {
		// TODO Auto-generated method stub

	}

	@Override
	public void openAV() {
		// TODO Auto-generated method stub

	}

	@Override
	public void openLocal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void openUDisk() {
		// TODO Auto-generated method stub

	}

	@Override
	public void openTF() {
		// TODO Auto-generated method stub

	}

	@Override
	public void openSetting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deviceMute() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deviceDisMute() {
		// TODO Auto-generated method stub

	}

	@Override
	public void openOffice(String filePath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDlna() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopDlna() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyboardWrite(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyboardDelete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAudioSeek(Device device, int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopAudio(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playAudio(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pauseAudio(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void muteAudio(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disMuteAudio(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAudioVolume(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void decreaseAudioVolume(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openAudio(Device device, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVideoSeek(Device device, int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopVideo(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playVideo(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pauseVideo(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void muteVideo(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disMuteVideo(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addVideoVolume(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void decreaseVideoVolume(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openVideo(Device device, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openImage(Device device, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopImage(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void turnLeftImage(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void turnRightImage(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoomInImage(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoomOutImage(Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void transformImage(Device device, ImageTransform imageTransform) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeWifi(String ssid, String password, String capabilities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDevice(List<PhoneDevice> listDevices) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openGame(String mPackage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void uninstallGame(String packName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void forgetWifi(String ssid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void installGame(String mPackage, String url) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWifi(String ssid, String password, String capabilities) {
		// TODO Auto-generated method stub
		
	}

}
