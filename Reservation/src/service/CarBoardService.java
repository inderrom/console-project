package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.BoardDAO;
import util.PaginationVO;
import util.ScanUtil;
import util.SpaceUtil;
import util.View;

public class CarBoardService {
	
	private static CarBoardService instance = null;
	private CarBoardService() {}
	public static CarBoardService getInstance() {
		if(instance == null) instance = new CarBoardService();
		return instance;
	}
	BoardDAO boardDAO = BoardDAO.getInstance();
	
	public void space() {
		for(int i = 0; i < 25; i++) {
			System.out.println();
		}
	}
		
	
	public int carReviewBoard() {
		space();
		System.out.println("===================== 차량대여 후기 게시판 =====================");
		System.out.println("     1.후기글작성 2.후기글조회 3.본인작성글조회 0.뒤로가기");
		System.out.println("================================================================");
		// 본인작성글 조회
		switch(ScanUtil.nextInt()) {
		case 1: return View.CAR_REVIEW_INSERT;
		case 2: return View.CAR_REVIEW_DETAIL;
		case 3: return View.CAR_BOARD_REFER;
		case 0: return View.REVIEW_BOARD;
		default: 
			System.out.println("잘못입력하셨습니다.");
			return View.CAR_REVIEW_BOARD;
		}
	}
	
	public int carReviewInsert() {
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		List<Object> param = new ArrayList<>();
		param.add((String) userInfo.get("MEM_ID"));
		param.add(0);
		List<Map<String, Object>> carRentList = boardDAO.carRentList(param);
		if(carRentList.size() == 0) {
			space();
			System.out.println("═════════════════════•°• ⚠ •°•═════════════════════");
			System.out.println("    아직 대여하지 않았거나 반납하지 않았습니다.");
			System.out.println("        반납 후 후기를 작성하실 수 있습니다.");
			System.out.println("════════════════════════════════════════════════════");
			System.out.println();
			System.out.print("이전 화면으로 돌아가시려면 아무 숫자나 눌러주세요. >>> ");
			int num = ScanUtil.nextInt();
			switch(num) {
			case 1: return View.CAR_REVIEW_BOARD;
			default: return View.CAR_REVIEW_BOARD;
			}
		}else {
			space();
//			System.out.println("────────────────✧❁✧ "+carRentList.get(0).get("MEM_ID")+"님의 대여목록" +" ✧❁✧────────────────");
//			System.out.printf("%sㅣ%sㅣ%s",
//					SpaceUtil.format("번  호", 5, 0),
//					SpaceUtil.format("차종명", 10, 0),
//					SpaceUtil.format("대여기간", 30, 0));
//			System.out.println();
//			System.out.println("────────────────────────────────────────────────────────────────────");
			Map<Integer, Object> listNum = new HashMap<>();
			int y = 1, h = 0, m = 0;
			h = (y - 1) * 5;
			m = (y * 5);
			int page = 1;
			test:
			while(true) {
				System.out.println("───────────────────✧❁✧ "+carRentList.get(0).get("MEM_ID")+"님의 대여목록" +" ✧❁✧───────────────────");
				System.out.printf("%sㅣ%sㅣ%s",
						SpaceUtil.format("번  호", 5, 0),
						SpaceUtil.format("차종명", 10, 0),
						SpaceUtil.format("대여기간", 30, 0));
				System.out.println();
				System.out.println("────────────────────────────────────────────────────────────────────");
				int a = 0;
				
				List<Object> paramm = new ArrayList<>();
				paramm.add((String) userInfo.get("MEM_ID"));
				paramm.add(0);
				paramm.add(m);
				paramm.add(h);
				List<Map<String, Object>> pagee = boardDAO.pagee1(paramm);
				if(h < carRentList.size()) {
					if(m > carRentList.size()) {
						m = carRentList.size();
					}
				}
				for(int i = h; i < m; i++) {
					listNum.put(i, pagee.get(a).get("CB_NO"));
					System.out.printf("%sㅣ%sㅣ%s ~ %s",
							SpaceUtil.format(i+1, 5, 0),
							SpaceUtil.format(pagee.get(a).get("C_NAME"), 10, 0),
							SpaceUtil.dateFormat(pagee.get(a).get("CB_STARTDATE"), 15, 0),
							SpaceUtil.dateFormat(pagee.get(a++).get("CB_ENDDATE"), 15, 0));
					System.out.println();
					System.out.println("----------------------------------------------------------------");
				}
				System.out.println("────────────────────────────────────────────────────────────────────");
				System.out.println();
				
				PaginationVO paging = new PaginationVO();
				paging.setCurrentPage(page);
				paging.setTotalRecord(carRentList.size());
				String pagingText = paging.getPagingText();
				System.out.println(pagingText);
				System.out.println();
				System.out.println("· · ─────────────── ·𖥸· ─────────────── · ·");
				System.out.println("   후기를 작성할 번호를 고르시려면 1을,");
				System.out.println("  페이지를 이동하시려면 2를 입력해주세요.");
				System.out.println("· · ─────────────── ·𖥸· ─────────────── · ·");
				int no = 0;
				switch(ScanUtil.nextInt()) {
				case 1:
					System.out.println();
					int z  = 0;
					outer:
					while(true) {
						System.out.println("후기를 작성할 문의게시판 번호를 입력해주세요. >>>");
						no = ScanUtil.nextInt();
						for(int i = 0; i <= listNum.size(); i++) {
							if (no == i) {
								z++;
							}
						}if(z == 0) {
							System.out.println("번호를 잘못 입력하셨습니다.");
						} else {
							break outer;
						}
					}
					List<Object> param1 = new ArrayList<>();
					param1.add(listNum.get(no-1));
					List<Map<String, Object>> rNOList = boardDAO.rNOList(param1);
					if(rNOList.size() > 0) {
						System.out.println("※※※ 대여목록당 1개의 후기글만 작성하실 수 있습니다. ※※※");
						System.out.println("         ════════════•°• ⚠ •°•════════════");
						System.out.println("          이미 후기글을 작성하셨습니다.");
						System.out.println("          이전 화면으로 이동하겠습니다.");
						System.out.println("         ═════════════════════════════════");
						return View.CAR_REVIEW_BOARD;
					} else {
						List<Map<String, Object>> carRentRow = boardDAO.carRentRow(param1);
						System.out.print("후기글 제목을 입력해주세요 >>> ");
						String title = ScanUtil.nextLine();
						System.out.print("후기글 내용을 입력해주세요 >>> ");
						String content = ScanUtil.nextLine();
						System.out.print("평점을 1~5점 중에 숫자로 매겨주세요 >>> ");
						int num = ScanUtil.nextInt();
						String star = "";
						for(int i = 0; i < num; i++) {
							star += "★";
						}
						String stars = star;
						List<Object> param2 = new ArrayList<>();
						param2.add((String)userInfo.get("MEM_ID"));
						param2.add(title);
						param2.add(content);
						param2.add(stars);
						param2.add(carRentRow.get(0).get("C_NAME"));
						param2.add(listNum.get(no-1));
						
						int result = boardDAO.carRRInsert(param2);
						if (result > 0) {
							System.out.println("≫ ───────────────────────── ≪•◦ ❈ ◦•≫ ───────────────────────── ≪");
							System.out.println("    후기글이 등록되었습니다. 감사합니다. 다음에도 이용해주세요.");
							System.out.println("≫ ───────────────────────── ≪•◦ ❈ ◦•≫ ───────────────────────── ≪");
							
							List<Object> param4 = new ArrayList<>();
							param4.add(userInfo.get("MEM_ID"));
							List<Map<String, Object>> memMile = boardDAO.memMile(param4);
							System.out.println();
							List<Object> param3 = new ArrayList<>();
							param3.add(Integer.parseInt(memMile.get(0).get("MEM_MILE").toString())+1000);
							param3.add(userInfo.get("MEM_ID"));
							
							int result1 = boardDAO.milePlus(param3);
							System.out.println();
							System.out.println("마일리지 1000점이 적립되었습니다.");
							System.out.println("다음에도 이용해주세요~~ ( ＾◡＾)っ ♡ ");
							System.out.println();
							System.out.println("--<<이전 마일리지 : " + memMile.get(0).get("MEM_MILE") + "점>>--");
							System.out.println("--<<현재 마일리지 : " + (Integer.parseInt(memMile.get(0).get("MEM_MILE").toString())+1000) + "점>>--");
							return View.CAR_REVIEW_BOARD;
						} else {
							System.out.println("후기글이 등록되지 않았습니다. 다시 작성해주세요.");
							return View.CAR_REVIEW_BOARD;
						}
					}
				case 2:
					while(true) {
						System.out.print("보고싶은 페이지를 입력해주세요. >>> ");
						y = ScanUtil.nextInt();
						h = (y - 1) * 5;
						m = (y * 5);
						
						if(h < carRentList.size()) {
							if(m > carRentList.size()) {
								m = carRentList.size();
							}
						}
						
						if(m <= carRentList.size() && y > 0) { //원래 6 <
							 page = y;
								break;
						}else if(carRentList.size() <= 5 && y == 1){
							page = y;
							break;
						} else {
							System.out.println("없는 페이지입니다.");
						}
					}
					break;
				default:
					System.out.println("잘못 입력하셨습니다.");
				}
			}	
		}
	}	
	
	
	
	public int carReviewDtail() {
		while(true) {
		System.out.println("=================== 후기글 조회 ===================");
		System.out.println(" 1.전체후기글 조회 2.차량별후기글 조회 0.뒤로가기");
		System.out.println("===================================================");
		int no = 0;  // 게시글번호
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		Map<Integer, Object> listNum = new HashMap<>();
		
		switch(ScanUtil.nextInt()) {
		case 1: 
			List<Map<String, Object>> carRRTotalList = boardDAO.carRRTotalList();
			int y = 1, h = 0, m = 0;
			h = (y - 1) * 5;
			m = (y * 5);
			int page = 1;
			test:
			while (true) {
				space();
				System.out.println("================================ 전체 후기글 조회목록 ================================");
				System.out.printf("%sㅣ%sㅣ%sㅣ%sㅣ%s",
						SpaceUtil.format("번  호", 5, 0),
						SpaceUtil.format("차종명", 7, 0),
						SpaceUtil.format("제  목", 30, 0),
						SpaceUtil.format("평  점", 7, 0),
						SpaceUtil.format("날  짜", 10, 0));
				System.out.println();
				System.out.println("───────────────────────────────────────────────────────────────────────────────────");
				int a = 0;
				
				List<Object> param = new ArrayList<>();
				param.add(m);
				param.add(h);
				List<Map<String, Object>> pagee = boardDAO.page2(param);
				
				if(h < carRRTotalList.size()) {
					if(m > carRRTotalList.size()) {
						m = carRRTotalList.size();
					}
				}
				
				
				for(int i = h; i < m; i++) {
					listNum.put(i, pagee.get(a).get("BO_NO"));
					System.out.printf("%sㅣ%sㅣ%sㅣ%sㅣ%s",
							SpaceUtil.format(i+1, 5, 0),
							SpaceUtil.format(pagee.get(a).get("BO_RNAME"), 7, 0),
							SpaceUtil.format(pagee.get(a).get("BO_TITLE"), 30, 0),
							SpaceUtil.format(pagee.get(a).get("BO_STARS"), 15, 0),
							SpaceUtil.dateFormat(pagee.get(a++).get("BO_DATE"), 10, 0));
					System.out.println();
					System.out.println("-------------------------------------------------------------------------------");
				}
				System.out.println("=====================================================================================");
				System.out.println();
				
				PaginationVO paging = new PaginationVO();
				paging.setCurrentPage(page);
				paging.setTotalRecord(carRRTotalList.size());
				String pagingText = paging.getPagingText();
				System.out.println();
				System.out.println("· · ─────────────── ·𖥸· ─────────────── · ·");
				System.out.println("       조회할 번호를 고르시려면 1을,");
				System.out.println("   페이지를 이동하시려면 2를 입력해주세요.");
				System.out.println("· · ─────────────── ·𖥸· ─────────────── · ·");
				switch(ScanUtil.nextInt()) {
				
				case 1:
					int z  = 0;
					outer:
					while(true) {
						System.out.println("상세보기할 문의게시판 번호를 입력해주세요. >>>");
						no = ScanUtil.nextInt();
						for(int i = 0; i <= listNum.size(); i++) {
							if (no == i) {
								z++;
							}
						}if(z == 0) {
							System.out.println("번호를 잘못 입력하셨습니다.");
						} else {
							break outer;
						}
					}
					List<Object> param1 = new ArrayList<>();
					param1.add(listNum.get(no-1));
					List<Map<String, Object>> carRRDList = boardDAO.carRRDList(param1);
					System.out.println();
					System.out.println("=============================== 후기글 조회 ===============================");
					System.out.println("╔═════════════════════════════════════════════════════════════════════╗");
					System.out.println("  번  호 : " + no);
					System.out.println("  차종명 : " + carRRDList.get(0).get("BO_RNAME"));
					System.out.println("  작성자 : " + carRRDList.get(0).get("BO_WRITER"));
					System.out.println("  평  점 : " + carRRDList.get(0).get("BO_STARS"));
					System.out.println("  후기글제목 : " + carRRDList.get(0).get("BO_TITLE"));
					System.out.println("  후기글내용 : " + carRRDList.get(0).get("BO_CONTENT"));
					System.out.print("  후기글날짜 : "); 
					System.out.printf(SpaceUtil.dateFormat(carRRDList.get(0).get("BO_DATE"), 15, 0));
					System.out.println();
					System.out.println("╚═════════════════════════════════════════════════════════════════════╝");
					System.out.println("============================================================================");
					no = Integer.parseInt(listNum.get(no-1).toString());
					System.out.println();
					System.out.println();
					System.out.println("=============================== ✐☡ 댓글 ✐☡ ===============================");
					System.out.println("--------------------------------------------------------------------------");
					List<Map<String, Object>> carRRRList = boardDAO.carRRRList(param1);
					if(carRRRList.size()==0) {
						System.out.println("작성된 댓글이 없습니다.");
					}else {
						for(Map<String, Object> row : carRRRList) {
							System.out.printf("%sㅣ%sㅣ%s",
//									SpaceUtil.format(row.get("RE_NO"), 5, 0),
									SpaceUtil.format(row.get("RE_WRITER"), 7, 0),
									SpaceUtil.format(row.get("RE_CONTENT"), 45, 0),
									SpaceUtil.dateFormat(row.get("RE_DATE"), 15, 0));
							System.out.println();
							System.out.println("--------------------------------------------------------------------------");
						}
					}
					System.out.println("===========================================================================");
				System.out.println();
				System.out.println();
				while(true) {
					System.out.print("댓글을 입력하시겠습니까? (y/n)");
					switch(ScanUtil.nextLine()) {
					case "y":
						System.out.print("댓글을 입력해주세요.>>> ");
						String reply = ScanUtil.nextLine();
						List<Object> param2 = new ArrayList<>();
						param2.add(no);
						param2.add((String)userInfo.get("MEM_ID"));
						param2.add(reply);
						int result = boardDAO.reInsert(param2);
						if(result > 0) {
							System.out.println("댓글작성이 완료되었습니다.");
							return View.CAR_REVIEW_BOARD;
						}else {
							System.out.println("댓글작성에 실패하였습니다.");
							return View.CAR_REVIEW_BOARD;
						}
					case "n":
						System.out.println("이전 화면으로 이동하겠습니다.");
						return View.CAR_REVIEW_BOARD;
					default: 
						System.out.println("잘못입력하셨습니다.");
					}	
				}
			
			case 2:
				while(true) {
					System.out.print("보고싶은 페이지를 입력해주세요. >>> ");
					y = ScanUtil.nextInt();
					h = (y - 1) * 5;
					m = (y * 5);
					
					if(h < carRRTotalList.size()) {
						if(m > carRRTotalList.size()) {
							m = carRRTotalList.size();
						}
					}
					
					if(m <= carRRTotalList.size() && y > 0) { //원래 6 <
						 page = y;
							break;
					}else if(carRRTotalList.size() <= 5 && y == 1){
						page = y;
						break;
					} else {
						System.out.println("없는 페이지입니다.");
					}
				}
					break;
			default:
					System.out.println("잘못 입력하셨습니다.");
				}
			}
//			return View.CAR_REVIEW_BOARD;
		
		case 2:
			List<Map<String, Object>> carNameList = boardDAO.carNameList();
			
			System.out.println("======================================================= 차량 후기글 조회 =======================================================");
			for(int i = 0; i < carNameList.size(); i++) {
				System.out.print(carNameList.get(i).get("C_NAME") + "\t");
			}
			System.out.println();
			System.out.println("================================================================================================================================");
			System.out.println();
			System.out.print("조회할 차량명을 입력해주세요. (반드시 차종명을 입력해주세요) >>> ");
			String name = ScanUtil.nextLine();
			List<Object> param = new ArrayList<>();
			param.add(name);
			List<Map<String, Object>> carRRList = boardDAO.carRRList(param);
			
			if(carRRList.size() == 0) {
				System.out.println();
				System.out.println("═════════════════════•°• ⚠ •°•═════════════════════");
				System.out.println("  차종명을 잘못 입력했거나 작성된 후기가 없습니다.");
				System.out.println("═══════════════════════════════════════════════════");
				System.out.println();
				return View.CAR_REVIEW_DETAIL;
			}else {
				space();
				System.out.println("================================ 차량 후기글 조회목록 ================================");
				System.out.printf("%sㅣ%sㅣ%sㅣ%sㅣ%s",
						SpaceUtil.format(" 번  호", 5, 0),
						SpaceUtil.format(" 차종명", 7, 0),
						SpaceUtil.format(" 제  목", 30, 0),
						SpaceUtil.format(" 평  점", 7, 0),
						SpaceUtil.format(" 날  짜", 10, 0));
				System.out.println();
				System.out.println("──────────────────────────────────────────────────────────────────────────────────────");
				Map<Integer, Object> listNum1 = new HashMap<>();
				for(int i = 0; i < carRRList.size(); i++) {
					listNum1.put(i, carRRList.get(i).get("BO_NO"));
					System.out.printf("%sㅣ%sㅣ%sㅣ%sㅣ%s",
							SpaceUtil.format(i+1, 5, 0),
							SpaceUtil.format(carRRList.get(i).get("BO_RNAME"), 7, 0),
							SpaceUtil.format(carRRList.get(i).get("BO_TITLE"), 30, 0),
							SpaceUtil.format(carRRList.get(i).get("BO_STARS"), 7, 0),
							SpaceUtil.dateFormat(carRRList.get(i).get("BO_DATE"), 10, 0));
					System.out.println();
					System.out.println("-------------------------------------------------------------------------------------");
				}
				System.out.println("======================================================================================");
				System.out.println();
				int z  = 0;
				outer:
				while(true) {
					System.out.println("상세보기할 문의게시판 번호를 입력해주세요. >>>");
					no = ScanUtil.nextInt();
					for(int i = 0; i <= listNum1.size(); i++) {
						if (no == i) {
							z++;
						}
					}if(z == 0) {
						System.out.println("번호를 잘못 입력하셨습니다.");
					} else {
						break outer;
					}
				}
				List<Object> param1 = new ArrayList<>();
				param1.add(listNum1.get(no-1));
				List<Map<String, Object>> carRRDList = boardDAO.carRRDList(param1);
				System.out.println();
				System.out.println("=============================== 후기글 조회 ===============================");
				System.out.println("╔═════════════════════════════════════════════════════════════════════╗");
				System.out.println("  번  호 : " + no);
				System.out.println("  차종명 : " + carRRDList.get(0).get("BO_RNAME"));
				System.out.println("  작성자 : " + carRRDList.get(0).get("BO_WRITER"));
				System.out.println("  평  점 : " + carRRDList.get(0).get("BO_STARS"));
				System.out.println("  후기글제목 : " + carRRDList.get(0).get("BO_TITLE"));
				System.out.println("  후기글내용 : " + carRRDList.get(0).get("BO_CONTENT"));
				System.out.print("  후기글날짜 : "); 
				System.out.printf(SpaceUtil.dateFormat(carRRDList.get(0).get("BO_DATE"), 15, 0));
				System.out.println();
				System.out.println("╚═════════════════════════════════════════════════════════════════════╝");
				System.out.println("============================================================================");
				no = Integer.parseInt(listNum1.get(no-1).toString());
				System.out.println();
				System.out.println();
				System.out.println("=============================== ✐☡ 댓글 ✐☡ ===============================");
				List<Map<String, Object>> carRRRList = boardDAO.carRRRList(param1);
				if(carRRRList.size()==0) {
					System.out.println("작성된 댓글이 없습니다.");
				}else {
					for(Map<String, Object> row : carRRRList) {
						System.out.printf("%sㅣ%sㅣ%s",
//								SpaceUtil.format(row.get("RE_NO"), 5, 0),
								SpaceUtil.format(row.get("RE_WRITER"), 7, 0),
								SpaceUtil.format(row.get("RE_CONTENT"), 45, 0),
								SpaceUtil.dateFormat(row.get("RE_DATE"), 15, 0));
						System.out.println();
						System.out.println("--------------------------------------------------------------------------");
					}
				}
				System.out.println("===========================================================================");
			System.out.println();
			System.out.println();
			System.out.print("댓글을 입력하시겠습니까? (y/n)");
			while(true) {
			switch(ScanUtil.nextLine()) {
			case "y":
				System.out.print("댓글을 입력해주세요.");
				String reply = ScanUtil.nextLine();
				List<Object> param2 = new ArrayList<>();
				param2.add(no);
				param2.add((String)userInfo.get("MEM_ID"));
				param2.add(reply);
				int result = boardDAO.reInsert(param2);
				if(result > 0) {
					System.out.println("댓글작성이 완료되었습니다.");
					return View.CAR_REVIEW_BOARD;
				}else {
					System.out.println("댓글작성에 실패하였습니다.");
					return View.CAR_REVIEW_BOARD;
				}
			case "n":
				System.out.println("이전 화면으로 이동하겠습니다.");
				return View.CAR_REVIEW_BOARD;
			default :
				System.out.println("잘못입력하셨습니다.");
//			return View.CAR_REVIEW_BOARD;
			}
	    	}	
		}
		case 0:  return View.CAR_REVIEW_BOARD;
		default: 
			System.out.println("잘못입력하셨습니다.");
		}
	  }
	}
	

	
	public int carBoardRefer() {
		space();
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		System.out.println("====================== "+userInfo.get("MEM_ID") + "님의 작성글/댓글 조회 ======================");
		System.out.println("                  1.작성글 조회 2.댓글조회 0.뒤로가기");
		System.out.println("=========================================================================");
		switch(ScanUtil.nextInt()) {
		case 1: return View.CAR_REVIEW_REFER;
		case 2: return View.CAR_REPLY_REFER;
		case 0: return View.REVIEW_BOARD;
		default:
			System.out.println("잘못 입력하셨습니다.");
			return View.CAR_BOARD_REFER;
					
		}
	}
	
	
	public int carReviewRefer() {
		space();
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		List<Object> param = new ArrayList<>();
		param.add(userInfo.get("MEM_ID"));
		List<Map<String, Object>> carRRList = boardDAO.carRRBMList(param);
		if(carRRList.size() == 0) {
			System.out.println("═════════•°• ⚠ •°•═════════");
			System.out.println("작성하신 후기글이 없습니다.");
			System.out.println("═══════════════════════════");
			
			ScanUtil.nextLine();
			return View.CAR_REVIEW_BOARD;
		} else {
			System.out.println("========================= "+userInfo.get("MEM_ID") + "님의 작성글 조회 =======================");
			System.out.printf("%sㅣ%sㅣ%sㅣ%s",
					SpaceUtil.format("번  호", 7, 0),
					SpaceUtil.format("차종명", 7, 0),
					SpaceUtil.format("제  목", 35, 0),
					SpaceUtil.format("날  짜", 15, 0));
			System.out.println();
			System.out.println("────────────────────────────────────────────────────────────────────");
			Map<Integer, Object> listNum = new HashMap<>();
			for(int i = 0; i < carRRList.size(); i++) {
				listNum.put(i, carRRList.get(i).get("BO_NO"));
				System.out.printf("%sㅣ%sㅣ%sㅣ%s",
						SpaceUtil.format(i+1, 7, 0),
						SpaceUtil.format(carRRList.get(i).get("BO_RNAME"), 7, 0),
						SpaceUtil.format(carRRList.get(i).get("BO_TITLE"), 35, 0),
						SpaceUtil.dateFormat(carRRList.get(i).get("BO_DATE"), 15, 0));	
				System.out.println();
				System.out.println("-------------------------------------------------------------------");
			}
			System.out.println("========================================================================");
			
			int no = 0;
			int z  = 0;
			outer:
			while(true) {
				System.out.println("상세보기할 문의게시판 번호를 입력해주세요. >>>");
				no = ScanUtil.nextInt();
				for(int i = 0; i <= listNum.size(); i++) {
					if (no == i) {
						z++;
					}
				}if(z == 0) {
					System.out.println("번호를 잘못 입력하셨습니다.");
				} else {
					break outer;
				}
			}
			List<Object> param1 = new ArrayList<>();
			param1.add(listNum.get(no-1));
			List<Map<String, Object>> carRReview = boardDAO.carRRDList(param1);
			System.out.println();
			System.out.println("=========================================================================");
			System.out.println("╔═════════════════════════════════════════════════════════════════════╗");
			System.out.println("  번  호 : " +  no);
			System.out.println("  차종명 : " + carRReview.get(0).get("BO_RNAME"));
			System.out.println("  제  목 : " + carRReview.get(0).get("BO_TITLE"));
			System.out.println("  평  점 : " + carRReview.get(0).get("BO_STARS"));
			System.out.println("  내  용 : " + carRReview.get(0).get("BO_CONTENT"));
			System.out.print("  날  짜: ");
			System.out.printf(SpaceUtil.format(carRReview.get(0).get("BO_DATE"), 10, 0));
			System.out.println();
			System.out.println("╚═════════════════════════════════════════════════════════════════════╝");
			System.out.println("=========================================================================");
			System.out.println();
			while(true) {
			System.out.println("원하는 메뉴의 번호를 선택해주세요. >>> ");
			System.out.print("1.수정 2.삭제 0.뒤로가기");
			switch(ScanUtil.nextInt()) {
			case 1:
				System.out.print("수정할 제목을 입력해주세요. >>> ");
				String title = ScanUtil.nextLine();
				System.out.print("수정할 평점을 입력해주세요. >>> ");
				int s = ScanUtil.nextInt();
				String star = "";
				for(int i = 0; i < s; i++) {
					star+="★";
				}
				System.out.println("수정할 내용을 입력해주세요. >>> ");
				String content = ScanUtil.nextLine();
				List<Object> param2 = new ArrayList<>();
				param2.add(title);
				param2.add(star);
				param2.add(content);
				param2.add(listNum.get(no-1));
				int result = boardDAO.carRRModify(param2);
				if(result > 0) {
					System.out.println("수정이 완료되었습니다.");
					return View.CAR_REVIEW_BOARD;
				}else {
					System.out.println("수정을 실패하였습니다.");
					return View.CAR_REVIEW_BOARD;
				}
			case 2:
				//날짜비교
				//보드에 작성된 날짜+30 > 현재날짜 일 때 후기글 삭제하면 1000원 차감 
				List<Object> param3 = new ArrayList<>();
				param3.add(listNum.get(no-1));
				List<Map<String, Object>> torf = boardDAO.torf(param3);
				
				if(torf.get(0).get("V_DATE").equals("A")) {
					System.out.println("≻─────────────────────── ⋆※※※⋆ ────────────────────≺");
					System.out.println("   후기글을 작성한지 30일이 지나지 않았기 때문에");
					System.out.println("      삭제하시면 적립된 마일리지가 취소됩니다.");
					System.out.println("≻──────────────────────────────────────────────────≺");
					System.out.print("조회하신 후기글을 정말 삭제하시겠습니까? (y/n) >>> ");
					while(true) {
						switch(ScanUtil.nextLine()) {
						case "y":
							List<Object> param4 = new ArrayList<>();
							param4.add(userInfo.get("MEM_ID"));
							List<Map<String, Object>> memMile = boardDAO.memMile(param4);
							
							List<Object> param5 = new ArrayList<>();
							param5.add(Integer.parseInt(memMile.get(0).get("MEM_MILE").toString())-1000);
							param5.add(userInfo.get("MEM_ID"));
							
							int result1 = boardDAO.carRRDelete(param1);	
							int result2 = boardDAO.inqReDelete(param1);
							int result3 = boardDAO.milePlus(param5);
							if(result3 > 0) {
								System.out.println();
								System.out.println("해당 후기글이 삭제되었습니다.");
								System.out.println();
								System.out.println("--<<이전 마일리지 : " + memMile.get(0).get("MEM_MILE") + "점>>--");
								System.out.println("--<<현재 마일리지 : " + (Integer.parseInt(memMile.get(0).get("MEM_MILE").toString())-1000) + "점>>--");
								return View.CAR_REVIEW_BOARD;
							}else {
								System.out.println("해당 후기글이 삭제되지 않았습니다.");
								return View.CAR_REVIEW_BOARD;
							}
						case "n":
							System.out.println("삭제를 진행하지 않고 이전화면으로 돌아가겠습니다.");
							return View.CAR_REVIEW_BOARD;
						default:
							System.out.println("잘못입력하셨습니다.");
						}
					}
				} else {
					System.out.println("후기글을 작성한지 30일이 지났기 때문에 삭제하셔도");
					System.out.println("마일리지가 취소되지 않습니다.");
					System.out.print("조회하신 후기글을 정말 삭제하시겠습니까? (y/n)");
					while(true) {
						switch(ScanUtil.nextLine()) {
						case "y":
							int result1 = boardDAO.carRRDelete(param1);	
							int result2 = boardDAO.inqReDelete(param1);
							if(result2 > 0) {
								System.out.println("해당 후기글이 삭제되었습니다.");
								return View.CAR_REVIEW_BOARD;
							}else {
								System.out.println("해당 후기글이 삭제되지 않았습니다.");
								return View.CAR_REVIEW_BOARD;
							}
						case "n":
							System.out.println("삭제를 진행하지 않고 이전화면으로 돌아가겠습니다.");
							return View.CAR_REVIEW_BOARD;
						default:
							System.out.println("잘못입력하셨습니다.");
						}
					}
				}
			case 0:
				return View.CAR_REVIEW_BOARD;
			default:
				System.out.println("잘못입력하셨습니다.");
//				return View.REVIEW_BOARD;
			}
		  }
		}
	}
	
	public int carReplyRefer() {
		space();
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		List<Object> param = new ArrayList<>();
		param.add(userInfo.get("MEM_ID"));
		List<Map<String, Object>> carRRList = boardDAO.carOlnyRRList(param);
		if(carRRList.size() == 0) {
			System.out.println("═════════•°• ⚠ •°•═════════");
			System.out.println("작성한 댓글이 없습니다.");
			System.out.println("═══════════════════════════");
			
			ScanUtil.nextLine();
			return View.CAR_REVIEW_BOARD;
		} else {
			System.out.println("==================== "+userInfo.get("MEM_ID") + "님의 댓글 조회 ====================");
			System.out.printf("%sㅣ%sㅣ%s",
					SpaceUtil.format("번  호", 5, 0),
					SpaceUtil.format("내  용", 35, 0),
					SpaceUtil.format("날  짜", 10, 0));
			System.out.println();
			System.out.println("────────────────────────────────────────────────────────────────────");
			Map<Integer, Object> listNum = new HashMap<>();
			for(int i = 0; i < carRRList.size(); i++) {
				listNum.put(i, carRRList.get(i).get("RE_NO"));
				System.out.printf("%sㅣ%sㅣ%s",
						SpaceUtil.format(i+1, 5, 0),
						SpaceUtil.format(carRRList.get(i).get("RE_CONTENT"), 35, 0),
						SpaceUtil.dateFormat(carRRList.get(i).get("RE_DATE"), 10, 0));	
				System.out.println();
				System.out.println("-------------------------------------------------------------------");
			}
			System.out.println("────────────────────────────────────────────────────────────────────");
			
			int no = 0;
			int z  = 0;
			outer:
			while(true) {
				System.out.println("상세보기할 문의게시판 번호를 입력해주세요. >>>");
				no = ScanUtil.nextInt();
				for(int i = 0; i <= listNum.size(); i++) {
					if (no == i) {
						z++;
					}
				}if(z == 0) {
					System.out.println("번호를 잘못 입력하셨습니다.");
				} else {
					break outer;
				}
			}
			List<Object> param1 = new ArrayList<>();
			param1.add(listNum.get(no-1));
			List<Map<String, Object>> carRRList1 = boardDAO.carReRList(param1);
			System.out.println("========================== 내가 단 댓글의 후기글 ==========================");
			System.out.println("╔═════════════════════════════════════════════════════════════════════╗");
			System.out.println("  번  호 : " + no);
			System.out.println("  차종명 : " + carRRList1.get(0).get("BO_RNAME"));
			System.out.println("  제  목 : " + carRRList1.get(0).get("BO_TITLE"));
			System.out.println("  평  점 : " + carRRList1.get(0).get("BO_STARS"));
			System.out.println("  내  용 : " + carRRList1.get(0).get("BO_CONTENT"));
			System.out.print("  날  짜: ");
			System.out.printf(SpaceUtil.dateFormat(carRRList1.get(0).get("BO_DATE"), 10, 0));
			System.out.println();
			System.out.println("╚═════════════════════════════════════════════════════════════════════╝");
			System.out.println("============================================================================");
			System.out.println();
			System.out.println();
			System.out.println("=============================== ✐☡ 댓글 ✐☡ ===============================");
			List<Map<String, Object>> reply = boardDAO.replyOne(param1);
			System.out.println("╭─────────────────────────────────────────────────────────────────────╮");
			System.out.println("  내용 : " + reply.get(0).get("RE_CONTENT"));
			System.out.print("  날짜 : ");
			System.out.printf(SpaceUtil.dateFormat(reply.get(0).get("RE_DATE"), 10, 0));
			System.out.println();
			System.out.println("╰─────────────────────────────────────────────────────────────────────╯");
			System.out.println("============================================================================");
			System.out.println();
			System.out.println("아래의 목록에서 하나를 선택하여 입력해주세요.");
			System.out.println("1.수정하기 2.삭제하기 0.뒤로가기");
			System.out.println();
			switch(ScanUtil.nextInt()) {
			case 1:
				System.out.print("수정할 내용을 입력해주세요. >>> ");
				String content = ScanUtil.nextLine();
				List<Object> param2 = new ArrayList<>();
				param2.add(content);
				param2.add(listNum.get(no-1));
				int result = boardDAO.modReply(param2);
				if(result > 0) {
					System.out.println("댓글이 수정되었습니다.");
					return View.CAR_REVIEW_BOARD;
				}else {
					System.out.println("댓글 수정에 실패하였습니다.");
					return View.CAR_REVIEW_BOARD;
				}
			case 2:
				System.out.println("해당 댓글을 정말 삭제하시겠습니까? (y/n) >>> ");
				switch(ScanUtil.nextLine()) {
				case "y":
					int result1 = boardDAO.deleteRe(param1);
					if(result1 > 0) {
						System.out.println("해당 댓글이 삭제되었습니다.");
						return View.CAR_REVIEW_BOARD;
					}else {
						System.out.println("댓글 삭제에 실패하였습니다.");
						return View.CAR_REVIEW_BOARD;
					}
				case "n":
					System.out.println("이전화면으로 이동하겠습니다.");
					return View.CAR_REVIEW_BOARD;
				default:
					System.out.println("잘못입력하셨습니다. 이전 화면으로 이동하겠습니다.");
					return View.CAR_REVIEW_BOARD;
				}
			case 0: return View.CAR_REVIEW_BOARD;
			}
			return View.CAR_REVIEW_BOARD;
		}
	}


}
