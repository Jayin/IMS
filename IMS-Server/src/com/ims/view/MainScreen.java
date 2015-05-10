package com.ims.view;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.google.zxing.WriterException;
import com.ims.common.Enconding;
import com.ims.common.IPUtils;
import com.ims.service.IMSServer;

public class MainScreen extends Frame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Button btn;
	Image barImage;
	
	final int WIDTH = 300;
	final int HEIGHT = 300;
	final int X = 300;
	final int Y = 300;
	
	boolean ISSTART = false;
	

	public MainScreen() {
		this.setLocation(this.X, this.Y);
		this.setLayout(new FlowLayout());
		initCompont();
		addCompont();
		this.setTitle("Interactive Meeting System");
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
	}
	

	private void addCompont() {
		this.add(btn);
		if(this.WIDTH<=0||this.HEIGHT<=0){
			this.pack();
		}else{
			this.setSize(this.WIDTH, this.HEIGHT);
		}
		this.setVisible(true);
	}

	private void initCompont() {
		btn = new Button("Start");
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					if(!ISSTART){  //如果还没有开启就开启
						btn.setLabel("Stop");
						try {
							barImage = Enconding.createQRCode(IPUtils.getLocalIP()+"@@@"+IMSServer.LONGPORT+"@@@"+IMSServer.SHORTPORT, 200);
							System.out.println(IPUtils.getLocalIP()+"@@@"+IMSServer.LONGPORT+"@@@"+IMSServer.SHORTPORT);
							repaint();
						} catch (WriterException e1) {
							e1.printStackTrace();
						}
						startServer();
						ISSTART = true;
					}else{
						System.out.println("Stop--1");
						btn.setLabel("Start");
						stopServer();
						ISSTART = false;
						System.out.println("Stop--0");
					}
					
				}
			}
		});
	}
	
	private void stopServer(){
		System.out.println("stopServer --1");
		IMSServer.getInstance().stop();
		System.out.println("stopServer --0");
	}
	
	private void startServer(){
		IMSServer.getInstance().start();
		System.out.println("local ip address : " + IPUtils.getLocalIP()+"  local port  " + IMSServer.getInstance().getLongPort());
	}

	public static void main(String[] args) {
		new MainScreen();
	}
	@Override
	public void paint(Graphics g) {
		g.drawImage(barImage, 50, 50, 200, 200, null);
	}

}
