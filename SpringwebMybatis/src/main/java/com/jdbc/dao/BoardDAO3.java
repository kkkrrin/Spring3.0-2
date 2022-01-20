package  com.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;

import com.jdbc.dto.BoardDTO;

public class BoardDAO3 {
	

	private SqlSessionTemplate sessionTemplate;
	
	public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
		this.sessionTemplate = sessionTemplate;
	
	}
	
	
	//1.num의 최대값
	public int getMaxNum(){
		
		
		int maxNum = 0;
		
		maxNum = sessionTemplate.selectOne("com.boardMapper.maxNum");
		
		return maxNum;
		
	}
	
	//입력
	public void insertData(BoardDTO dto){
		
		sessionTemplate.insert("com.boardMapper.insertData",dto);
		
		
	}
	
	//전체리스트
	public List<BoardDTO> getList(int start, int end,
			String searchKey, String searchValue){
		
		HashMap<String, Object> params= new HashMap<String, Object>();
		
		params.put("start", start);
		params.put("end", end);
		params.put("searchKey", searchKey);
		params.put("searchValue", searchValue);
		
		List<BoardDTO> lists= sessionTemplate.selectList("com.boardMapper.getLists",params);
		
		return lists;
		
	}
	
	//전체 데이터수 구하기
	public int getDataCount(String searchKey,String searchValue){
		
		HashMap<String, Object> params= new HashMap<String, Object>();
		
		params.put("searchKey", searchKey);
		params.put("searchValue", searchValue);
		
		int result= sessionTemplate.selectOne("com.boardMapper.getDataCount",params);
	
		return result;
		
	}
	
	//조회수증가
	public void updateHitCount(int num){
		
		sessionTemplate.update("com.boardMapper.updateHitCount",num);
		
	}
	
	//한명의 데이터 출력
	public BoardDTO getReadData(int num){
		
		BoardDTO dto = sessionTemplate.selectOne("com.boardMapper.getReadData",num);
		
		return dto;
		
	}
	
	//삭제
	public void deleteData(int num){
		
		sessionTemplate.delete("com.boardMapper.deleteData",num);
		
	}
	
	
	//수정
	public void updateData(BoardDTO dto){
		
		sessionTemplate.update("com.boardMapper.updateData",dto);
	
				
	}
	

}


































