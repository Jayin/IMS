package com.ims.view;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.ims.entity.RequestMessage;
import com.ims.entity.ResponseMessage;

public class ShortTes { 

	public static void main(String[] args) throws  IOException {
		Socket socket = new Socket("192.168.1.100", 10006);
		OutputStream out = socket.getOutputStream();
		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out));
		
		RequestMessage r = new RequestMessage();
		r.setRequestType(RequestMessage.REQUEST_TYPE_PROBLEM);
		r.setUsername("vector");
		r.setProblemCtn("sdfsdfsdfdsfsd");
		w.write(r.getXML());
		w.flush();
		socket.shutdownOutput();
		
		InputStream in = socket.getInputStream();
		
		ResponseMessage re = ResponseMessage.parse(in);
		System.out.println(re);
		
		socket.close();
		
	}

}
