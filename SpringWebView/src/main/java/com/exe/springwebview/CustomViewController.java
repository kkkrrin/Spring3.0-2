package com.exe.springwebview;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomViewController {

	@RequestMapping(value = "/simpleCustomView.action")
	public ModelAndView customView() {
		
		//jsp 를 view 로 설정
		ModelAndView mav = new ModelAndView();
		//mav.setViewName("simpleCustomView"); // jsp파일로 가는 경우 
		
		//class 로 view 를 만든다.
		mav.setView(new SimpleCustomView());
		mav.addObject("message","SimpleCustomView Class 이다."); // addobject는 데이터 넣어주는거 
		
		return mav;
		
	}
	
	
	@RequestMapping(value = "/pdfView.action")
	public ModelAndView getPdfView() {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setView(new CustomPdfView());
		mav.addObject("message","PDF FILE");
		
		return mav; 
		
		
	}

	@RequestMapping(value = "/excelView.action")
	public ModelAndView getExcelView() {
		
		ModelAndView mav = new ModelAndView();
		
		mav.setView(new CustomExcelView());
		
		return mav; 
	}
		
	
	//File Upload
	@RequestMapping(value = "/upload.action", method = {RequestMethod.POST, RequestMethod.GET})
	public String upload(MultipartHttpServletRequest request, String str) throws Exception {
		
		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/files"); //수동으로 만든 경로 
		
		MultipartFile file = request.getFile("upload"); 
		
		if(file!=null && file.getSize()>0) { //파일이 넘어옴
			
			try {
				
				FileOutputStream fos= new FileOutputStream(path + "/" + file.getOriginalFilename());
				
				InputStream is = file.getInputStream(); 
			
				
				int data;
				byte[] buffer = new byte[4096];
				
				while((data = is.read(buffer, 0, buffer.length))!=-1) {
					fos.write(buffer,0,data);
				}
				
				
				/*
				 * while(true) {
				 * 
				 * int data = is.read(buffer, 0, buffer.length);
				 * 
				 * if(data == -1) { break; }
				 * 
				 * fos.write(buffer,0,data); }
				 */
				
				is.close();
				fos.close();
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		
		
		return "uploadResult"; //jsp파일로 보냄 

	}
	
	
	//FileDownload
	@RequestMapping(value = "/download.action")
	public ModelAndView download() {
		
		ModelAndView mav = new ModelAndView(); 
		mav.setView(new DownloadView());
		
		return mav; 
		
	}
	
	
	
	
	
	
	
	
}
