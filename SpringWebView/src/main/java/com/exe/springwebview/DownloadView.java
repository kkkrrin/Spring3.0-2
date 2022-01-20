package com.exe.springwebview;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

public class DownloadView extends AbstractView{

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		response.setContentType("application/octet-stream");
		response.addHeader("Content-Disposition", "Attachment;filename=\"inna.jpg\"");
		
		String path= request.getSession().getServletContext().getRealPath("/WEB-INF/files/inna.jpg");
		
		//파일 읽어 
		BufferedInputStream bis= new BufferedInputStream(new FileInputStream(path));
		
		//파일 내보내
		BufferedOutputStream bos= new BufferedOutputStream(response.getOutputStream());
		
		
		  while(true) {
		  
		  int data =bis.read();
		  
		  if(data!=-1) { 
			  bos.write(data);// 데이터 내보내
		  }else { 
			  break; 
			  }
		  }
	
		/*
		int data;

		while((data=bis.read()!=-1)) {
			bos.write(data);
		}
		*/
		
		bis.close();
		bos.close();
	}

	
}
