package com.jdbc.springweb;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jdbc.dao.BoardDAO2;
import com.jdbc.dto.BoardDTO;
import com.jdbc.util.MyUtil;

@Controller
public class BoardController {
	
	@Autowired
	@Qualifier("boardDAO2")
	BoardDAO2 dao; 
	
	@Autowired
	MyUtil myUtil;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
	
		return "index"; 
	
	}

/*
	@RequestMapping(value = "/created.action", method= {RequestMethod.GET, RequestMethod.POST})
	public String created(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		return "bbs/created";
	
	}
	
*/
	
	//위에꺼 mvc로 바꾸기 
	
	@RequestMapping(value = "/created.action")
	public ModelAndView created() {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("bbs/created");
		
		return mav;
		
	}
	
	

	@RequestMapping(value = "/created_ok.action", method= {RequestMethod.GET, RequestMethod.POST})
	public String created_ok(BoardDTO dto, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		int maxNum = dao.getMaxNum(); 
		
	
		dto.setNum(maxNum+1);
		dto.setIpAddr(request.getRemoteAddr());
		
		dao.insertData(dto);
		
		return "redirect:/list.action";
	
	}
	
	
	@RequestMapping(value = "/list.action", method= {RequestMethod.GET, RequestMethod.POST})
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String cp= request.getContextPath(); 


		
		String pageNum = request.getParameter("pageNum");

		int currentPage = 1;

		if(pageNum!=null){

			currentPage = Integer.parseInt(pageNum);

		}

		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");

		if(searchValue != null){

		
			if(request.getMethod().equalsIgnoreCase("GET")){
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}

		}else{

			searchKey = "subject";
			searchValue = "";

		}


		
		int dataCount = dao.getDataCount(searchKey, searchValue);

		
		int numPerPage = 3;

		
		int totalPage = myUtil.getPageCount(numPerPage, dataCount);

	
		if(currentPage > totalPage){

			currentPage = totalPage;

		}

	
		int start =(currentPage-1)*numPerPage+1;
		int end = currentPage*numPerPage;

		List<BoardDTO> lists = dao.getList(start, end, searchKey, searchValue);

		

		String param = "";
		if(!searchValue.equals("")){
			param = "?searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");

		}


		String listUrl = cp + "/list.action" + param;
		String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);

		
		String articleUrl = cp + "/article.action";

		if(param.equals("")){

			articleUrl += "?pageNum=" + currentPage;

		}else{

			articleUrl += param + "&pageNum=" + currentPage;

		}

	
		request.setAttribute("lists", lists);
		request.setAttribute("pageIndexList", pageIndexList);
		request.setAttribute("dataCount", dataCount);
		request.setAttribute("articleUrl", articleUrl);
		
		return "bbs/list";
	}
		
/*	
	@RequestMapping(value = "/article.action", method= {RequestMethod.GET, RequestMethod.POST})
	public String article(HttpServletRequest request, HttpServletResponse response) throws Exception{
*/		
	

	
		@RequestMapping(value = "/article.action", method= {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView article(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		
		String cp= request.getContextPath();
		
		int num= Integer.parseInt(request.getParameter("num"));
		String pageNum=request.getParameter("pageNum");
		String searchKey=request.getParameter("searchKey");
		String searchValue=request.getParameter("searchValue");

		if(searchValue!=null) {
			searchValue=URLDecoder.decode(searchValue,"UTF-8");
		}

		dao.updateHitCount(num); 
		BoardDTO dto=dao.getReadData(num); 

		if(dto==null) {
			
			ModelAndView mav = new ModelAndView(); 
			mav.setViewName("redirect:bbs/list.action");
			
			//return "redirect:/list.action";
		}

		int lineSu= dto.getContent().split("\n").length; 

		dto.setContent(dto.getContent().replace("\n", "<br/>")); 

		String param = "pageNum="+pageNum;

		if(searchValue!=null) {
			param+="&searchKey="+ searchKey;
			param+="&searchValue="+ URLEncoder.encode(searchValue,"UTF-8");
		}
/*
		request.setAttribute("dto", dto);
		request.setAttribute("params", param);
		request.setAttribute("lineSu",lineSu);
		request.setAttribute("pageNum", pageNum);
		
		
		return "bbs/article"; 
*/	
		
		ModelAndView mav = new ModelAndView(); 
		mav.setViewName("bbs/article");
		mav.addObject("dto",dto);
		mav.addObject("params",param);
		mav.addObject("lineSu",lineSu);
		mav.addObject("pageNum",pageNum);
		
		return mav; 
		
		
	}
	
	
		
		@RequestMapping(value = "/updated.action", method= {RequestMethod.GET, RequestMethod.POST})
		public String updated(HttpServletRequest request, HttpServletResponse response) throws Exception{
			
			String cp = request.getContextPath(); 
			
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum=request.getParameter("pageNum");

			String searchKey=request.getParameter("searchKey");
			String searchValue=request.getParameter("searchValue");

			if(searchValue!=null) {
				searchValue=URLDecoder.decode(searchValue,"UTF-8");
			}

			BoardDTO dto= dao.getReadData(num);

			if(dto==null) {
				return "redirect:/list.action";
			}

			String param="pageNum=" +pageNum;

			if(searchValue!=null) {
				param+="&searchKey="+ searchKey + "&searchValue="+URLEncoder.encode(searchValue,"UTF-8");
			}

			request.setAttribute("dto", dto);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("params", param);
			request.setAttribute("searchKey", searchKey);
			request.setAttribute("searchValue", searchValue);

			return "bbs/updated"; 
			
		}
	
	
		@RequestMapping(value = "/updated_ok.action", method= {RequestMethod.GET, RequestMethod.POST})
		public String updated_ok(BoardDTO dto, HttpServletRequest request, HttpServletResponse response) throws Exception{
			
			
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum=request.getParameter("pageNum");

			String searchKey=request.getParameter("searchKey");
			String searchValue=request.getParameter("searchValue");

			dao.updateData(dto);

			String param="pageNum=" +pageNum;

			if(!searchValue.equals("")) {
				param+="&searchKey="+ searchKey + "&searchValue="+URLEncoder.encode(searchValue,"UTF-8");
			}
			
			return "redirect:/list.action?" + param; 
			
		}
		
		
		

		@RequestMapping(value = "/deleted.action", method= {RequestMethod.GET})
		public String deleted(BoardDTO dto, HttpServletRequest request, HttpServletResponse response) throws Exception{
			
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum=request.getParameter("pageNum");

			String searchKey=request.getParameter("searchKey");
			String searchValue=request.getParameter("searchValue");
			
			dao.deleteData(num); 
	
			//���� �� ���� �ִ� �������� ����
			String param="pageNum=" +pageNum;

			if(searchValue!=null&&!searchValue.equals("")) {
				param+="&searchKey="+ searchKey + "&searchValue="+URLEncoder.encode(searchValue,"UTF-8");
			}
			
			
			return "redirect:/list.action?" + param; 
			
		}
		
}
