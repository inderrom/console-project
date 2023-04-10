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

public class BoardService {
	
	private static BoardService instance = null;
	private BoardService() {}
	public static BoardService getInstance() {
		if(instance == null) instance = new BoardService();
		return instance;
	}
	
	public void space() {
		for(int i = 0; i < 25; i++) {
			System.out.println();
		}
	}
	
	BoardDAO boardDAO = BoardDAO.getInstance();
	
	public int board() {
		space();
		System.out.println("==================== 게시판 선택 ====================");
		System.out.println("    1. 1:1 문의게시판   2. 후기게시판   0.뒤로가기");
		System.out.println("=====================================================");
		System.out.print("어떤 게시판에 접속하시겠습니까? (숫자입력) >>> ");
		switch(ScanUtil.nextInt()) {
		case 1: return View.INQUIRY_BOARD;
		case 2: return View.REVIEW_BOARD;
		case 0: return View.MAIN;
		default :
			System.out.println("잘못입력하셨습니다. 다시 입력해주세요.");
			return View.BOARD;
		}
	}

	public int inquiryBoard() {
		space();
		System.out.println("============================= 1:1 문의게시판 =============================");
		System.out.println("  1.문의글작성   2.문의글조회   3.문의글수정   4.문의글삭제  0.뒤로가기");
		System.out.println("==========================================================================");
		System.out.print("어떤 메뉴를 선택하시겠습니까? (숫자입력) >>> ");
		System.out.println();
		switch(ScanUtil.nextInt()) {
		case 1: return View.INQUIRY_INSERT;
		case 2: return View.INQUIRY_DETAIL;
		case 3: return View.INQUIRY_MODIFY;
		case 4: return View.INQUIRY_DELETE;
		case 0: return View.BOARD;
		default :
			System.out.println("번호를 잘못입력하셨습니다.");
			return View.INQUIRY_BOARD;
		}
	}
	
	public int inquiryInsert() {
		space();
		System.out.println();
		System.out.println("============================ 문의게시판 작성창 ===============================");
		System.out.print("문의글 제목을 입력해주세요 >>> ");
		String title = ScanUtil.nextLine();
		System.out.print("문의글 내용을 입력해주세요 >>> ");
		String content = ScanUtil.nextLine();
		System.out.println("==============================================================================");
		System.out.println();
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		String writer = (String) userInfo.get("MEM_ID");
		
		List<Object> param = new ArrayList<>();
		param.add(writer);
		param.add(title);
		param.add(content);
		
		int result = boardDAO.inqInsert(param);
		if (result > 0) {
			System.out.println("문의글이 등록되었습니다. 빠른 시일 내 답변해드리겠습니다.");
			System.out.println();
			System.out.println();
			System.out.print("이전화면으로 돌아가시려면 아무 숫자나 눌러주세요.");
			int num = ScanUtil.nextInt();
			switch(num) {
			case 1:
				return View.INQUIRY_BOARD;
			default: 
				return View.INQUIRY_BOARD;
			}
		} else {
			System.out.println("문의글이 등록되지 않았습니다. 다시 작성해주세요.");
		}
		return View.INQUIRY_BOARD;
	}	


	public int inquiryDetail() {
		space();
		System.out.println("═════════════════•°• ⚠ •°•═════════════════");
		System.out.println("문의게시판은 작성자 본인만 조회 가능합니다.");
		System.out.println("════════════════════════════════════════════");
		System.out.print("로그인시 비밀번호를 입력해주세요 >>> ");
		String pw = ScanUtil.nextLine();
		System.out.println("─────────────────────────────────────────────");
		System.out.println();
		
		int no = 0;
		
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		List<Object> param = new ArrayList<>();
		param.add((String) userInfo.get("MEM_ID"));
		
		if(pw.equals((String) userInfo.get("MEM_PW"))) {
			List<Map<String, Object>> inqList = boardDAO.getInqList(param);
			if(inqList.size()==0) {
				System.out.println("등록된 문의글이 없습니다.");
				return View.INQUIRY_BOARD;
			}else {
				space();
				int y = 1, h = 0, m = 0;
				h = (y - 1) * 5;
				m = (y * 5);
				int page = 1;
				Map<Integer, Object> listNum = new HashMap<>();
				test:
				while(true) {
					System.out.println("============================ 문의글 조회 ==============================");
					System.out.printf("%sㅣ%sㅣ%s",
							SpaceUtil.format("번호", 7, 0),
							SpaceUtil.format("제목", 35, 0),
							SpaceUtil.format("날짜", 15, 0));
					System.out.println();
					System.out.println("───────────────────────────────────────────────────────────────────────");
					int a = 0;
					
					List<Object> paramm = new ArrayList<>();
					paramm.add((String) userInfo.get("MEM_ID"));
					paramm.add(m);
					paramm.add(h);
//					List<Map<String, Object>> p1 = boardDAO.page1();
					List<Map<String, Object>> pagee = boardDAO.pagee(paramm);
//					Map<Integer, Object> listNum = new HashMap<>();
					if(h < inqList.size()) {
						if(m > inqList.size()) {
							m = inqList.size();
						}
					}
					for(int i = h; i < m; i++) {
						listNum.put(i, pagee.get(a).get("BO_NO"));
						System.out.printf("%sㅣ%sㅣ%s",
								SpaceUtil.format(i+1, 7, 0),
								SpaceUtil.format(pagee.get(a).get("BO_TITLE"), 35, 0),
								SpaceUtil.dateFormat(pagee.get(a++).get("BO_DATE"), 15, 0));	
						System.out.println();
						System.out.println("-----------------------------------------------------------------------");
					}
					System.out.println("=======================================================================");
					System.out.println();
					
					PaginationVO paging = new PaginationVO();
					paging.setCurrentPage(page);
					paging.setTotalRecord(inqList.size());
					String pagingText = paging.getPagingText();
					System.out.println(pagingText);
					System.out.println();
					System.out.println("· · ─────────────── ·𖥸· ─────────────── · ·");
					System.out.println("   상세보기할 번호를 고르시려면 1을,");
					System.out.println("  페이지를 이동하시려면 2를 입력해주세요.");
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
						
						System.out.println();
						List<Object> param1 = new ArrayList<>();
						param1.add(listNum.get(no-1));
						List<Map<String, Object>> inqDetailOne = boardDAO.inqDetailOne(param1);
						space();
						System.out.println("================================ 문의글 상세보기 ================================");
						System.out.println("─────────────────────────────────────────────────────────────────────────────────");
						System.out.printf("%sㅣ%sㅣ%s",
								SpaceUtil.format(no, 7, 0),
								SpaceUtil.format(inqDetailOne.get(0).get("BO_TITLE"), 30, 0),
								SpaceUtil.dateFormat(inqDetailOne.get(0).get("BO_DATE"), 15, 0));
						System.out.println();
						System.out.println("─────────────────────────────────────────────────────────────────────────────────");
						System.out.println("╭─────────────────────────────────────────────────────────────────────╮");
						System.out.printf("   %s",SpaceUtil.format(inqDetailOne.get(0).get("BO_CONTENT"), 50, -1));
						System.out.println();
						System.out.println("╰─────────────────────────────────────────────────────────────────────╯");
						System.out.println("==================================================================================");
						System.out.println();
						System.out.println();
						List<Map<String, Object>> replyList = boardDAO.replyList(param1);
						System.out.println("╔═══════════════════════════════════ 문의답변 ═══════════════════════════════════╗");
						System.out.println("   ────────────────────────────────────────────────────────────────────────");
						if(replyList.size() == 0) {
							System.out.println("  작성된 답변이 없습니다.");
							System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
						} else {
							for(Map<String, Object> row : replyList) {
								System.out.printf("   %sㅣ%s",
										SpaceUtil.format(row.get("RE_CONTENT"), 50, -2),
										SpaceUtil.dateFormat(row.get("RE_DATE"), 15, 0));
								System.out.println();
								System.out.println("   ────────────────────────────────────────────────────────────────────────");
							}
							System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
						}
						System.out.println();
						System.out.print("이전화면으로 돌아가시려면 아무 숫자를 눌러주세요.");
						int num = ScanUtil.nextInt();
						switch(num) {
						case 1: return View.INQUIRY_BOARD;
						default: return View.INQUIRY_BOARD;
						}
					case 2:
						while(true) {
							System.out.print("보고싶은 페이지를 입력해주세요");
							y = ScanUtil.nextInt();
							h = (y - 1) * 5;
							m = (y * 5);
							
							if(h < inqList.size()) {
								if(m > inqList.size()) {
									m = inqList.size();
								}
							}
							
							if(m <= inqList.size() && y > 0) { //원래 6 <
								 page = y;
									break;
							}else if(inqList.size() <= 5 && y == 1){
								if(m > inqList.size()) {
									m = inqList.size();
								}
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
		else {
			System.out.println("비밀번호가 맞지 않습니다.");
			ScanUtil.nextLine();
			return View.INQUIRY_BOARD;
		}
	}
	
	
	
	
	public int inquiryModify() {
		space();
		System.out.println("═════════════════•°• ⚠ •°•═════════════════");
		System.out.println("문의게시판은 작성자 본인만 수정 가능합니다.");
		System.out.println("════════════════════════════════════════════");
		System.out.print("로그인시 비밀번호를 입력해주세요 >>> ");
		String pw = ScanUtil.nextLine();
		
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		List<Object> param = new ArrayList<>();
		param.add((String) userInfo.get("MEM_ID"));
		int no = 0;
		
		if(pw.equals((String) userInfo.get("MEM_PW"))) {
			List<Map<String, Object>> inquiryList = boardDAO.getInqList(param);
			if(inquiryList.size() ==0) {
				System.out.println("═════════•°• ⚠ •°•═════════");
				System.out.println("등록된 문의글이 없습니다.");
				System.out.println("═══════════════════════════");
				return View.INQUIRY_BOARD;
			}else {
				space();
				Map<Integer, Object> listNum = new HashMap<>();
				int y = 1, h = 0, m = 0;
				h = (y - 1) * 5;
				m = (y * 5);
				int page = 1;
				test:
				while(true) {
					System.out.println("======================== 문의글 조회 ===========================");
					System.out.printf("%sㅣ%sㅣ%s",
							SpaceUtil.format("번호", 7, 0),
							SpaceUtil.format("제목", 35, 0),
							SpaceUtil.format("날짜", 15, 0));
					System.out.println();
					System.out.println("────────────────────────────────────────────────────────────────────");
					int a = 0;
					
					List<Object> paramm = new ArrayList<>();
					paramm.add((String) userInfo.get("MEM_ID"));
					paramm.add(m);
					paramm.add(h);
					List<Map<String, Object>> pagee = boardDAO.pagee(paramm);
					
					if(h < inquiryList.size()) {
						if(m > inquiryList.size()) {
							m = inquiryList.size();
						}
					}
					for(int i = h; i < m; i++) {
						listNum.put(i, pagee.get(a).get("BO_NO"));
						System.out.printf("%sㅣ%sㅣ%s",
								SpaceUtil.format(i+1, 7, 0),
								SpaceUtil.format(pagee.get(a).get("BO_TITLE"), 35, 0),
								SpaceUtil.dateFormat(pagee.get(a++).get("BO_DATE"), 15, 0));	
						System.out.println();
						System.out.println("-------------------------------------------------------------------");
					}
					System.out.println("=======================================================================");
					System.out.println();
					PaginationVO paging = new PaginationVO();
					paging.setCurrentPage(page);
					paging.setTotalRecord(inquiryList.size());
					String pagingText = paging.getPagingText();
					System.out.println(pagingText);
					System.out.println();
					System.out.println("· · ─────────────── ·𖥸· ─────────────── · ·");
					System.out.println("      수정할 번호를 고르시려면 1을,");
					System.out.println("  페이지를 이동하시려면 2를 입력해주세요.");
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
						List<Object> param2 = new ArrayList<>();
						param2.add(listNum.get(no-1));
						List<Map<String, Object>> inqReList = boardDAO.inqReList(param2);
						
						if(inqReList.size() >= 1) {
							System.out.println("════════════════════════ •°• ⚠ •°• ═══════════════════════");
							System.out.println("   관리자의 답변이 달렸기 때문에 수정할 수 없습니다.");
							System.out.println("   다른 문의사항이 있으시면 문의게시판에 작성해주세요");
							System.out.println("══════════════════════════════════════════════════════════");
							System.out.println();
							System.out.print("이전화면으로 돌아가시려면 1을 눌러주세요.");
							int num = ScanUtil.nextInt();
							switch(num) {
							case 1: return View.INQUIRY_BOARD;
							default: return View.INQUIRY_BOARD;
							}
						}else {
							System.out.println();
							System.out.print("수정할 제목을 입력해주세요 >>> ");
							String title = ScanUtil.nextLine();
							System.out.println("수정할 내용을 입력해주세요 >>> ");
							String content = ScanUtil.nextLine();
							
							List<Object> param1 = new ArrayList<>();
							param1.add(title);
							param1.add(content);
							param1.add(listNum.get(no-1));
							
							int result = boardDAO.inqModify(param1);
							if (result > 0) {
								System.out.println("문의글이 수정되었습니다.");
							} else {
								System.out.println("문의글이 수정되지 않았습니다.");
							}
						}
						break test;
					case 2:
						while(true) {
							System.out.print("보고싶은 페이지를 입력해주세요");
							y = ScanUtil.nextInt();
							h = (y - 1) * 5;
							m = (y * 5);
							
							if(h < inquiryList.size()) {
								if(m > inquiryList.size()) {
									m = inquiryList.size();
								}
							}
							
							if(m <= inquiryList.size() && y > 0) { //원래 6 <
								 page = y;
									break;
							}else if(inquiryList.size() <= 5 && y == 1){
								page = y;
								break;
							} else {
								System.out.println("없는 페이지입니다.");
							}
						}
						break;
					default:
						System.out.println("잘못입력하셨습니다.");
					}
				}
				}
		} else {
			System.out.println("비밀번호가 맞지 않습니다.");
			return View.INQUIRY_BOARD;
		}
		return View.INQUIRY_BOARD;
	}
	
	
	
	public int inquiryDelete() {
		space();
		System.out.println("═════════════════•°• ⚠ •°•═════════════════");
		System.out.println("문의게시판은 작성자 본인만 삭제 가능합니다.");
		System.out.println("════════════════════════════════════════════");
		System.out.print("비밀번호를 입력해주세요 >>> ");
		String pw = ScanUtil.nextLine();
		
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		List<Object> param = new ArrayList<>();
		param.add((String) userInfo.get("MEM_ID"));
		
		if(pw.equals((String) userInfo.get("MEM_PW"))) {
			List<Map<String, Object>> inquiryList = boardDAO.getInqList(param);
			if(inquiryList.size()==0) {
				System.out.println("═════════•°• ⚠ •°•═════════");
				System.out.println("등록된 문의글이 없습니다.");
				System.out.println("═══════════════════════════");
				return View.INQUIRY_BOARD;
			}else {
				space();
				Map<Integer, Object> listNum = new HashMap<>();
				int y = 1, h = 0, m = 0;
				h = (y - 1) * 5;
				m = (y * 5);
				int page = 1;
				test:
				while(true) {
					System.out.println("======================== 문의글 조회 ===========================");
					System.out.printf("%sㅣ%sㅣ%s",
							SpaceUtil.format("번호", 7, 0),
							SpaceUtil.format("제목", 35, 0),
							SpaceUtil.format("날짜", 15, 0));
					System.out.println();
					System.out.println("────────────────────────────────────────────────────────────────────");
					int a = 0;
					
					List<Object> paramm = new ArrayList<>();
					paramm.add((String) userInfo.get("MEM_ID"));
					paramm.add(m);
					paramm.add(h);
					List<Map<String, Object>> pagee = boardDAO.pagee(paramm);
					
					if(h < inquiryList.size()) {
						if(m > inquiryList.size()) {
							m = inquiryList.size();
						}
					}
					
					for(int i = h; i < m; i++)  {
						listNum.put(i, pagee.get(a).get("BO_NO"));
						System.out.printf("%sㅣ%sㅣ%s",
								SpaceUtil.format(i+1, 7, 0),
								SpaceUtil.format(pagee.get(a).get("BO_TITLE"), 35, 0),
								SpaceUtil.format(pagee.get(a++).get("BO_DATE"), 15, 0));	
						System.out.println();
						System.out.println("-------------------------------------------------------------------");
					}
					System.out.println("=======================================================================");
					System.out.println();
					
					PaginationVO paging = new PaginationVO();
					paging.setCurrentPage(page);
					paging.setTotalRecord(inquiryList.size());
					String pagingText = paging.getPagingText();
					System.out.println(pagingText);
					System.out.println();
					System.out.println("· · ─────────────── ·𖥸· ─────────────── · ·");
					System.out.println("      삭제할 번호를 고르시려면 1을,");
					System.out.println("  페이지를 이동하시려면 2를 입력해주세요.");
					System.out.println("· · ─────────────── ·𖥸· ─────────────── · ·");
					
					switch(ScanUtil.nextInt()) {
					case 1:
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
//						System.out.println(listNum.get(no-1));
						System.out.println("======================== 삭제예정 문의글 ===========================");
						System.out.printf("%sㅣ%sㅣ%s",
								SpaceUtil.format("번호", 7, 0),
								SpaceUtil.format("제목", 35, 0),
								SpaceUtil.format("날짜", 15, 0));
						System.out.println();
						System.out.println("────────────────────────────────────────────────────────────────────");
						List<Map<String, Object>> inqDelList = boardDAO.inqDetailOne(param1);
						System.out.printf("%sㅣ%sㅣ%s",
								SpaceUtil.format(no, 7, 0),
								SpaceUtil.format(inqDelList.get(0).get("BO_TITLE"), 35, 0),
								SpaceUtil.dateFormat(inqDelList.get(0).get("BO_DATE"), 15, 0));
						System.out.println();
						System.out.println("=====================================================================");
						System.out.println();
						System.out.print("이 글을 삭제하시겠습니까? 삭제를 원하시면 1을 눌러주세요.");
						int one = ScanUtil.nextInt();
						switch(one) {
						case 1:
							int result = boardDAO.inqDelete(param1);
							if (result > 0) {
									System.out.println("문의글이 삭제되었습니다.");
									return View.INQUIRY_BOARD;
								}
							else {
								System.out.println("문의글이 삭제되지 않았습니다.");
								return View.INQUIRY_BOARD;
							}
						default:
							System.out.println("삭제 진행이 취소되었습니다.");
							System.out.println("이전화면으로 돌아가겠습니다.");
							ScanUtil.nextLine();
							return View.INQUIRY_BOARD;
						}
					case 2:
						while(true) {
							System.out.print("보고싶은 페이지를 입력해주세요");
							y = ScanUtil.nextInt();
							h = (y - 1) * 5;
							m = (y * 5);
							
							if(h < inquiryList.size()) {
								if(m > inquiryList.size()) {
									m = inquiryList.size();
								}
							}
							
							if(m <= inquiryList.size() && y > 0) { //원래 6 <
								 page = y;
									break;
							}else if(inquiryList.size() <= 5 && y == 1){
								page = y;
								break;
							} else {
								System.out.println("없는 페이지입니다.");
							}
						}
						break;
					default :
						System.out.println("잘못 입력하셨습니다.");
						break;
					}
				}	
				}
		}else {
			System.out.println("비밀번호가 맞지 않습니다.");
		}
		return View.INQUIRY_BOARD;
	}
	
	
	
	
	public int reviewBoard() {
		space();
		System.out.println("================ 후기 게시판 ================");
		System.out.println("         1.차량  2.숙소  0.뒤로가기");
		System.out.println("=============================================");
		System.out.print("목록을 선택해주세요 >>> ");
		switch(ScanUtil.nextInt()) {
		case 1: return View.CAR_REVIEW_BOARD;
		case 2: return View.PEN_REVIEW_BOARD;
		case 0: return View.BOARD;
		default:
			System.out.println("잘못입력하셨습니다.");
			return View.REVIEW_BOARD;
		}
	}	

	}
	
