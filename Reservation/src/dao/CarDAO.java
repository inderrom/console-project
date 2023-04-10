package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class CarDAO {
	private static CarDAO instance=null;
	private CarDAO() {};
	public static CarDAO getInstance() {
		if(instance==null) instance=new CarDAO();
		
		return instance;
	}
	JDBCUtil jdbc=JDBCUtil.getInstance();
	

	public int carReservation(List<Object> param) {
		String sql="INSERT INTO CAR_BOOKING (CB_NO, C_NO, C_NAME ,MEM_ID,  CB_STARTDATE, CB_ENDDATE, CB_TOTAL , CB_STATE, CB_DATE, C_LICENCE)"
				+ "VALUES "
				+ "(SEQ_CB_NO.NEXTVAL, ? ,(SELECT C_NAME FROM CAR WHERE C_NO = ?) ,? , TO_DATE(?) , TO_DATE(?) , ? ,?,SYSDATE,?) ";
		return jdbc.update(sql, param);
		
	}

	//이거 상세보기
	public List<Map<String, Object>> carList(String startDate, String endDate,String carName) { //이거 씀 상세보기임
		   String sql=
	               " SELECT A.* "
	             + "   FROM CAR A "  
	             + "  WHERE "  
	                  + "   (SELECT COUNT(*) "  
	                  + "      FROM CAR_BOOKING B "  
	                  + "     WHERE (B.CB_STARTDATE <= '" + startDate + "' AND B.CB_ENDDATE >= '"+startDate + "') "
	                  + "       AND (B.CB_STATE = 1 AND B.CB_STATE = 2) "
	                  + "       AND B.C_NAME=A.C_NAME )"
	                  + "		=0 "
	            +"      AND "   
	                  + "   (SELECT COUNT(*) "  
                      + "      FROM CAR_BOOKING B "  
                      + "     WHERE (B.CB_STARTDATE <= '"+endDate+ "' AND B.CB_ENDDATE >= '"+endDate + "')"
                      + "       AND (B.CB_STATE = 1 AND B.CB_STATE = 2)"
                      + "       AND B.C_NAME=A.C_NAME )"
                      + "		=0  "
            + "       	AND "    
	                  + "   (SELECT COUNT(*) "  
	                  + "      FROM CAR_BOOKING B "  
	                  + "     WHERE (B.CB_STARTDATE >= '"+startDate + "'AND B.CB_ENDDATE<= '"+endDate+"')"
	                  + "       AND (B.CB_STATE = 1 AND B.CB_STATE = 2) "
	                  + "       AND B.C_NAME=A.C_NAME )"
	                  + "		=0 "
		            + "AND A.C_NAME='" + carName+"'";
//		String sql=
//		 " SELECT A.* "
//	     +" FROM CAR A "  
//		 + " WHERE "  
//		 + " (SELECT COUNT(*) "  
//		 + " FROM CAR_BOOKING B "  
//         + " WHERE (B.CB_STARTDATE <= '"+inputDate+ "' AND B.CB_ENDDATE >= '"+inputDate + "') AND B.C_NO=A.C_NO)=0 "
//         + "AND"
//         + " (SELECT COUNT(*) "  
//		 + " FROM CAR_BOOKING B "  
//         + " WHERE (B.CB_STARTDATE <= '"+inputDate2+ "' AND B.CB_ENDDATE >= '"+inputDate2 + "') AND B.C_NO=A.C_NO)=0 "
//         + "AND "
//         + " (SELECT COUNT(*) "  
//		 + " FROM CAR_BOOKING B "  
//         + " WHERE (B.CB_STARTDATE >= '"+inputDate + "'AND B.CB_ENDDATE<= '"+inputDate2+"') AND B.C_NO=A.C_NO)=0 "
//         + "AND "
//         + " (SELECT COUNT(*)"
//         + " FROM CAR_BOOKING B"
//         + " WHERE (CB_STATE=0  AND CB_STATE=3) AND B.C_NO=A.C_NO)=0 "
//         + " AND "
//         + "C_NAME='"+carName+"'";
		   
		return jdbc.selectList(sql);
	}
	
	public List<Map<String, Object>> carList2(String inputDate, String inputDate2,String carName,Object object) {
//		String sql=
//				   " SELECT A.* "
//	              +" FROM CAR A "  
//		 + " WHERE "  
//		 + " (SELECT COUNT(*) "  
//		 + " FROM CAR_BOOKING B "  
//         + " WHERE (B.CB_STARTDATE <= '"+inputDate+ "' AND B.CB_ENDDATE >= '"+inputDate + "') AND B.C_NO=A.C_NO AND (B.CB_STATE=1 AND B.CB_STATE=2)=0 "
//         + "AND"
//         + " (SELECT COUNT(*) "  
//		 + " FROM CAR_BOOKING B "  
//         + " WHERE (B.CB_STARTDATE <= '"+inputDate2+ "' AND B.CB_ENDDATE >= '"+inputDate2 + "') AND B.C_NO=A.C_NO AND (B.CB_STATE=1 AND B.CB_STATE=2)=0 "
//         + "AND "
//         + " (SELECT COUNT(*) "  
//		 + " FROM CAR_BOOKING B "  
//         + " WHERE (B.CB_STARTDATE >= '"+inputDate + "'AND B.CB_ENDDATE<= '"+inputDate2+"')AND B.C_NO=A.C_NO AND (B.CB_STATE=1 AND B.CB_STATE=2)=0  AND "
//         		+ "C_NAME='"+carName+"' AND C_LICENCE='"+object+"'";
		
		
		String sql=
				   " SELECT A.* "
	              +" FROM CAR A "  
		 + " WHERE "  
		 + " (SELECT COUNT(*) "  
		 + " FROM CAR_BOOKING B "  
      + " WHERE (B.CB_STARTDATE <= '"+inputDate+ "' AND B.CB_ENDDATE >= '"+inputDate + "') AND B.C_NO=A.C_NO)=0 "
      + "AND"
      + " (SELECT COUNT(*) "  
		 + " FROM CAR_BOOKING B "  
      + " WHERE (B.CB_STARTDATE <= '"+inputDate2+ "' AND B.CB_ENDDATE >= '"+inputDate2 + "') AND B.C_NO=A.C_NO)=0 "
      + "AND "
      + " (SELECT COUNT(*) "  
		 + " FROM CAR_BOOKING B "  
      + " WHERE (B.CB_STARTDATE >= '"+inputDate + "'AND B.CB_ENDDATE<= '"+inputDate2+"')AND B.C_NO=A.C_NO)=0  "
      		+ "AND "
      		+ "C_NAME='"+carName+"' AND C_LICENCE='"+object+"'";

		return jdbc.selectList(sql);
	}
	
	public Map<String, Object> selectCar(List<Object> param3) {
		
		String sql="SELECT * FROM CAR WHERE C_NO=?";
		
		return jdbc.selectOne(sql, param3);
	}
	public Map<String, Object> selectLicence(List<Object> param0) {
		
		String sql="SELECT MEM_LICENSE FROM MEMBER WHERE MEM_ID=?";
		
		return jdbc.selectOne(sql, param0);
	}
	public List<Map<String, Object>> selectLicenceCar(String inputDate, String inputDate2, int mEM_LICENSE) {
		String sql=
				   " SELECT A.* "
	              +" FROM CAR A "  
	              + " WHERE "  
	              + " (SELECT COUNT(*) "  
	              + " FROM CAR_BOOKING B "  
	              + " WHERE (B.CB_STARTDATE <= '"+inputDate+ "' AND B.CB_ENDDATE >= '"+inputDate + "') AND B.C_NO=A.C_NO)=0 "
	              + "AND"
	              + " (SELECT COUNT(*) "  
	              + " FROM CAR_BOOKING B "  
	              + " WHERE (B.CB_STARTDATE <= '"+inputDate2+ "' AND B.CB_ENDDATE >= '"+inputDate2 + "') AND B.C_NO=A.C_NO)=0 "
	              + "AND "
	              + " (SELECT COUNT(*) "  
	              + " FROM CAR_BOOKING B "  
	              + " WHERE (B.CB_STARTDATE >= '"+inputDate + "'AND B.CB_ENDDATE<= '"+inputDate2+"')AND B.C_NO=A.C_NO)=0  "
	              + "AND A.C_LICENCE='"+mEM_LICENSE+"'";
		return jdbc.selectList(sql);
	}
	//1종차량 출력
	public List<Map<String, Object>> carNameList(String inputDate, String inputDate2) { //이거 씀(처음에 예약날 겹친거 걸러주는거)
		String sql=
				   " SELECT DISTINCT(A.C_NAME), A.C_LICENCE, A.C_MEN, A.C_PRICE "
	              +"   FROM CAR A "  
	              +"  WHERE (SELECT COUNT(DISTINCT(B.C_NAME)) "  
	              +"  		   FROM CAR_BOOKING B "  
	              +"  		  WHERE (B.CB_STARTDATE <= '" + inputDate + "' AND B.CB_ENDDATE >= '" + inputDate + "') "
	              +" 	        AND (B.CB_STATE=1 OR B.CB_STATE=2) "
	              +" 	   	    AND B.C_NO=A.C_NO)=0 "
	              +"    AND (SELECT COUNT(DISTINCT(B.C_NAME)) "  
	              +"  		   FROM CAR_BOOKING B "  
	              +" 		  WHERE (B.CB_STARTDATE <= '" + inputDate2 + "' AND B.CB_ENDDATE >= '" + inputDate2 + "') "
	              +" 			AND (B.CB_STATE=1 OR B.CB_STATE=2) "
	              +" 			AND B.C_NO=A.C_NO)=0 "
	              +"    AND (SELECT COUNT(DISTINCT(B.C_NAME)) "  
	              +"  		   FROM CAR_BOOKING B "
	              +"  		  WHERE (B.CB_STARTDATE >= '" + inputDate + "' AND B.CB_ENDDATE <= '" + inputDate2 + "') "
	              +" 			AND (B.CB_STATE=1 OR B.CB_STATE=2) "
	              +" 			AND B.C_NO=A.C_NO )=0";
//		String sql=
//				   " SELECT DISTINCT(A.C_NAME), A.C_LICENCE, A.C_MEN, A.C_PRICE "
//	              +"   FROM CAR A "  
//	              +"  WHERE (SELECT COUNT(DISTINCT(B.C_NAME)) "  
//	              +"  		   FROM CAR_BOOKING B "  
//	              +"  		  WHERE (B.CB_STARTDATE <= '" + inputDate + "' AND B.CB_ENDDATE >= '" + inputDate + "') "
//	              +" 	        AND (B.CB_STATE=1 OR B.CB_STATE=2) "
//	              +" 	   	    AND B.C_NO=A.C_NO)=0 "
//	              +"    AND (SELECT COUNT(DISTINCT(B.C_NAME)) "  
//	              +"  		   FROM CAR_BOOKING B "  
//	              +" 		  WHERE (B.CB_STARTDATE <= '" + inputDate2 + "' AND B.CB_ENDDATE >= '" + inputDate2 + "') "
//	              +" 			AND (B.CB_STATE=1 OR B.CB_STATE=2) "
//	              +" 			AND B.C_NO=A.C_NO)=0 "
//	              +"    AND (SELECT COUNT(DISTINCT(B.C_NAME)) "  
//	              +"  		   FROM CAR_BOOKING B "
//	              +"  		  WHERE (B.CB_STARTDATE >= '" + inputDate + "' AND B.CB_ENDDATE <= '" + inputDate2 + "') "
//	              +" 			AND (B.CB_STATE=1 OR B.CB_STATE=2) "
//	              +" 			AND B.C_NO=A.C_NO )=0";
		return jdbc.selectList(sql);
	}
	
	
	//2종 차량 출력
	public List<Map<String, Object>> carNameList2(String inputDate, String inputDate2,Object object) {
//		String sql=
//				   " SELECT DISTINCT(A.C_NAME), A.C_LICENCE, A.C_MEN, A.C_PRICE "
//	              +" FROM CAR A "  
//	              + " WHERE "  
//	              + " (SELECT COUNT(DISTINCT(B.C_NAME)) "  
//	              + " FROM CAR_BOOKING B "  
//	              + " WHERE (B.CB_STARTDATE <= '"+inputDate+ "' AND B.CB_ENDDATE >= '"+inputDate + "') AND B.C_NO=A.C_NO AND (B.CB_STATE=1 AND B.CB_STATE=2))=0 "
//	              + "AND "
//	              + " (SELECT COUNT(DISTINCT(B.C_NAME)) "  
//	              + " FROM CAR_BOOKING B "  
//	              + " WHERE (B.CB_STARTDATE <= '"+inputDate2+ "' AND B.CB_ENDDATE >= '"+inputDate2 + "') AND B.C_NO=A.C_NO AND (B.CB_STATE=1 AND B.CB_STATE=2))=0 "
//	              + "AND "
//	              + " (SELECT COUNT(DISTINCT(B.C_NAME)) "  
//	              + " FROM CAR_BOOKING B "  
//	              + " WHERE (B.CB_STARTDATE >= '"+inputDate + "'AND B.CB_ENDDATE <= '"+inputDate2+"')AND B.C_NO=A.C_NO AND (B.CB_STATE=1 AND B.CB_STATE=2))=0 "
//	              		+ "AND C_LICENCE='"+object+"'";
		
		
		   String sql=
	               " SELECT DISTINCT(A.C_NAME), A.C_LICENCE, A.C_MEN, A.C_PRICE "
	                + "   FROM CAR A "  
	                + "  WHERE "  
	                    + "   (SELECT COUNT(DISTINCT(B.C_NAME)) "  
	                    + "      FROM CAR_BOOKING B "  
	                    + "     WHERE (B.CB_STARTDATE <= '"+inputDate+ "' AND B.CB_ENDDATE >= '"+inputDate + "') "
	                    + "       AND (B.CB_STATE = 1 OR B.CB_STATE = 2) "
	                    + "      AND B.C_NAME=A.C_NAME)=0 "
	                + "            AND"   
	                          + "   (SELECT COUNT(DISTINCT(B.C_NAME)) "  
	                          + "      FROM CAR_BOOKING B "  
	                          + "     WHERE (B.CB_STARTDATE <= '"+inputDate2+ "' AND B.CB_ENDDATE >= '"+inputDate2 + "')"
	                          + "       AND (B.CB_STATE = 1 OR B.CB_STATE = 2) "
	                          + "      AND B.C_NAME=A.C_NAME)=0 "
	                + "                  AND "    
	                                + "   (SELECT COUNT(DISTINCT(B.C_NAME)) "  
	                                + "      FROM CAR_BOOKING B "  
	                                + "     WHERE (B.CB_STARTDATE >= '"+inputDate + "'AND B.CB_ENDDATE<= '"+inputDate2+"')"
	                                + "       AND (B.CB_STATE = 1 OR B.CB_STATE = 2)"
	                                + "      AND B.C_NAME=A.C_NAME)=0 "
	                                + "AND C_LICENCE='"+object+"'";
		return jdbc.selectList(sql);
	}
	
	
	public List<Map<String, Object>> carNameList3(String inputDate, String inputDate2,String carName, Object object) {
		String sql=
				   " SELECT A.* "
	              +" FROM CAR A "  
		 + " WHERE "  
		 + " (SELECT COUNT(*) "  
		 + " FROM CAR_BOOKING B "  
      + " WHERE (B.CB_STARTDATE <= '"+inputDate+ "' AND B.CB_ENDDATE >= '"+inputDate + "') AND B.C_NO=A.C_NO)=0 "
      + "AND"
      + " (SELECT COUNT(*) "  
		 + " FROM CAR_BOOKING B "  
      + " WHERE (B.CB_STARTDATE <= '"+inputDate2+ "' AND B.CB_ENDDATE >= '"+inputDate2 + "') AND B.C_NO=A.C_NO)=0 "
      + "AND "
      + " (SELECT COUNT(*) "  
		 + " FROM CAR_BOOKING B "  
      + " WHERE (B.CB_STARTDATE >= '"+inputDate + "'AND B.CB_ENDDATE<= '"+inputDate2+"')AND B.C_NO=A.C_NO)=0  AND "
      		+ "C_NAME='"+carName+"' AND C_LICENCE='"+object+"'";
		
		return jdbc.selectList(sql);
	}
	public List<Map<String, Object>> carNameList2(String carName) {
		String sql="SELECT * FROM CAR WHERE C_NAME='"+carName+"'";
		
		return jdbc.selectList(sql);
	}
	public List<Map<String, Object>> reservationCar() {
		String sql="SELECT CB_STARTDATE,CB_ENDDATE, CB_NO FROM CAR_BOOKING WHERE (CB_STATE = 1 OR CB_STATE = 2)";
		
		return jdbc.selectList(sql);
	}
	public int carState(List<Object> param) {
		String sql="UPDATE CAR_BOOKING SET CB_STATE=? WHERE CB_NO=? ";
		
		return jdbc.update(sql, param);
	}
	public int carMileage(List<Object> memMilage) {
		String sql="UPDATE MEMBER SET MEM_MILE=? WHERE MEM_ID=?";
		
		return jdbc.update(sql, memMilage);
	}
	public Map<String, Object> getMemberMile(Object object) {
		String sql="SELECT MEM_MILE FROM MEMBER WHERE MEM_ID='"+object+"'";
		
		return jdbc.selectOne(sql);
	}
	public Map<String, Object> membergetMileage(Object object) {
		String sql="SELECT MEM_MILE FROM MEMBER WHERE MEM_ID='"+object+"'";
		
		return jdbc.selectOne(sql);
	}
	public Map<String, Object> card(Object object) {
		String sql="SELECT MEM_ID FROM CARD WHERE MEM_ID='"+object+"'";
		
		return jdbc.selectOne(sql);
	}
	public int card_insert(List<Object> param) {
		String sql="INSERT INTO CARD (CARD_NO,MEM_ID, CARD_NAME, CARD_NUMBER)"
				 + "VALUES"
				 + "(SEQ_CARD.NEXTVAL,?,?,?)";
		return jdbc.update(sql, param);
	}
	public Map<String, Object> cardSearch(Object object) {
		String sql="SELECT * FROM CARD WHERE CARD_NUMBER='"+object+"'";
		return jdbc.selectOne(sql);
	}
	public List<Map<String, Object>> findCard(List<Object> param) {
		String sql="SELECT CARD_NAME,CARD_NUMBER FROM CARD WHERE MEM_ID=?";
		
		return jdbc.selectList(sql, param);
	}
}
