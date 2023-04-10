package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.AdminDAO;
import util.ScanUtil;
import util.SpaceUtil;
import util.View;

public class AdminService {
	static int num;
	private static AdminService instance = null;
	private AdminService() {}
	public static AdminService getInstance() {
		if(instance == null) instance = new AdminService();
		return instance;
	}
	AdminDAO adminDao = AdminDAO.getInstance();
	Map<Integer,Object> listNum = new HashMap<Integer, Object>(); // 게시글
	Map<Integer,Object> listNum1 = new HashMap<Integer, Object>(); // 댓글
	public int adm_sales() {
		clear();	
		System.out.println("╭─────────────────────────────────────────────────────╮");
	    System.out.println("│ 1. 차량 매출 확인  2. 숙소 매출 확인 3. 총매출 확인 │");
		System.out.println("╰─────────────────────────────────────────────────────╯");
		System.out.print("번호를 입력해주세요 ➤➤ ");

		switch(ScanUtil.nextInt()) {
		case 1: return View.ADMC_SALES;
		case 2: return View.ADMP_SALES;
		case 3: return View.ADMT_SALES;
		default: return View.ADM_SALES;
		}
	}

	public int admc_sales() {
		clear();
		System.out.println("╔═════════════════════ 차량 총 매출 ═══════════════════╗");
		System.out.printf("   %s│%s│%s│%s  │%s"
				, SpaceUtil.format("번호", 6, 0)
				, SpaceUtil.format("차종", 12, 0)
				, SpaceUtil.format("대여일수", 8, 0)
				, SpaceUtil.format("금액", 10, 1)
				, SpaceUtil.format("예약구분", 1, 1)
	);
		System.out.println();
		System.out.println("   ---------------------------------------------------");
		List<Map<String, Object>> salist = adminDao.getc_salist();
//		Map<Integer, Object> listNum = new HashMap<>();
		for(int i = 0; i < salist.size(); i++)  {
			int cmoney = Integer.parseInt(salist.get(i).get("CB_TOTAL").toString());
			String no = String.format("%,3d", cmoney);
			int cstate = Integer.parseInt(salist.get(i).get("CB_STATE").toString());
		
			String s1state = "";
			if(cstate ==0) {
				s1state = "반납완료";
			}else if(cstate==1) {
				s1state = "대여중";
			}else if(cstate==2) {
				s1state = "예약완료";
			}else {
				s1state = "예약취소";
			}
			listNum.put(i, salist.get(i).get("CB_NO"));
			System.out.printf("   %s│%s│%s│%s원│%s"
					 	, SpaceUtil.format(i +1, 6, 0)
						, SpaceUtil.format(salist.get(i).get("C_NAME"), 12, 0)
						, SpaceUtil.format(salist.get(i).get("일수"), 8, 0)
					, SpaceUtil.format(no, 10, 1)
					, SpaceUtil.format(s1state, 4, 1)
		);
			System.out.println();
			System.out.println("   ───────────────────────────────────────────────────");			
		}
		System.out.print("         [매출 총 합계] : ");
		Map<String,Object> param = adminDao.getc_sales();
		
		int tcmoney = Integer.parseInt(param.get("차량매출액").toString());
		String no1 = String.format("%,3d",tcmoney);
		
		System.out.println(no1 + "원"); // 별칭을 설정했으면! 무조건 겟 안에 별칭을 써준다 
		System.out.println("╚══════════════════════════════════════════════════════╝");
		System.out.println("╭───────────────────────────╮");
	    System.out.println("│ 1.뒤로가기 2.관리자 메뉴  │");
		System.out.println("╰───────────────────────────╯");
		System.out.print("번호를 입력해주세요 ➤➤ ");
		switch (ScanUtil.nextInt()) {
		case 1:return View.ADM_SALES;
		case 2: return View.ADMIN;
		default: return View.ADMC_SALES;
		}
	}
	
	public int admp_sales() {
		clear();
		System.out.println("╔═════════════════════ 숙소 총 매출 ═══════════════════╗");
		System.out.printf("   %s│%s│%s│%s  │%s"
				, SpaceUtil.format("번호", 6, 0)
				, SpaceUtil.format("숙소명", 12, 0)
				, SpaceUtil.format("대여일수", 8, 0)
				, SpaceUtil.format("금액", 10, 1)
				, SpaceUtil.format("예약구분", 1, 1)
			);
		System.out.println();
		System.out.println("   ----------------------------------------------------");
		List<Map<String, Object>> pblist = adminDao.getp_salist();
		 Map<Integer, Object> listNum = new HashMap<>();
		 for(int i = 0; i < pblist.size(); i++) {
	         listNum.put(i, pblist.get(i).get("PB_NO"));
			int pmoney = Integer.parseInt(pblist.get(i).get("PB_TOTAL").toString());
			String no2 = String.format("%,3d",pmoney);
			int pstate = Integer.parseInt(pblist.get(i).get("PB_STATE").toString());
			String p1state ="";
			
			if(pstate==0) {
				p1state="반납완료";
			}else if(pstate==1) {
				p1state="대여중";
			}else if(pstate==2) {
				p1state="예약완료";
			}else if(pstate==3) {
				p1state="예약취소";
			}
			
			System.out.printf("   %s│%s│%s│%s원│%s"
					, SpaceUtil.format(i+1, 6, 0)
					, SpaceUtil.format(pblist.get(i).get("P_NAME"), 12, 0)
					, SpaceUtil.format(pblist.get(i).get("일수"), 8, 0)
					, SpaceUtil.format(no2,10, 1)
					, SpaceUtil.format(p1state, 4, 1)
				);
			System.out.println();
			System.out.println("   ────────────────────────────────────────────────────");
		}
		System.out.print("          [매출 총 합계] : ");
		Map<String,Object> param = adminDao.getp_sales();
		
		int pcmoney = Integer.parseInt(param.get("숙소매출액").toString());
		String no3 = String.format("%,3d",pcmoney);
		
		System.out.println(no3 + "원");
		System.out.println("╚══════════════════════════════════════════════════════╝");
		System.out.println("╭───────────────────────────╮");
	    System.out.println("│ 1.뒤로가기 2.관리자 메뉴  │");
		System.out.println("╰───────────────────────────╯");
		System.out.print("번호를 입력해주세요 ➤➤ ");
		switch (ScanUtil.nextInt()) {
		case 1:return View.ADM_SALES;
		case 2: return View.ADMIN;
		default: return View.ADMP_SALES;
		}
	}
	
	public int admt_sales() {
		System.out.println("╔═════ 차량 & 숙소 총 매출액 ═════╗");
		Map<String,Object> param = adminDao.getc_sales();
		Map<String,Object> param1 = adminDao.getp_sales();
		
		int check = Integer.parseInt(param.get("차량매출액").toString());
		String no4 = String.format("%,3d",check);
		int check1 = Integer.parseInt(param1.get("숙소매출액").toString());
		String no5 = String.format("%,3d", check1);
		
		int check2 = (check + check1);
		String no6 = String.format("%,3d", check2);
		
		System.out.println("      차량 총 매출 : " + no4 + "원");
		System.out.println("      숙소 총 매출 : " + no5 + "원");
		System.out.print("          합계 : ");
		System.out.println(no6 + "원");
		System.out.println("╚═════════════════════════════════╝");
		System.out.println("╭───────────────────────────╮");
	    System.out.println("│ 1.뒤로가기 2.관리자 메뉴  │");
		System.out.println("╰───────────────────────────╯");
		System.out.print("번호를 입력해주세요 ➤➤ ");
		switch (ScanUtil.nextInt()) {
		case 1:return View.ADM_SALES;
		case 2: return View.ADMIN;
		default: return View.ADMT_SALES;
		}
	}
	public int adm_bo() {
		clear();
		System.out.println("╭───────────────────────────────────────────╮");
		System.out.println("│ 1.문의 게시판 2.후기 게시판 3.관리자메뉴  │");
		System.out.println("╰───────────────────────────────────────────╯");
		System.out.print("번호를 입력해주세요 ➤➤ ");
	
		switch(ScanUtil.nextInt()) {
		case 1: return View.ADMIQ_BOLIST;
		case 2: return View.ADMRV_BO;
		case 3: return View.ADMIN;
		
		default: return View.ADM_BO;
		}
	}
	
	public int admiq_bolist() {
		clear();
		System.out.println();
		System.out.println("═════════════════════════ 문의 게시판 ═════════════════════════");
		System.out.printf("%s│%s│%s│%s│"
						, SpaceUtil.format("번호", 6, 0)
						, SpaceUtil.format("작성자", 8, 0)
						, SpaceUtil.format("제목", 30, 0)
						, SpaceUtil.format("날짜", 12, 0)
			);
		System.out.println();
		System.out.println("﻿═══════════════════════════════════════════════════════════════");
		
		List<Object> param = new ArrayList<>();
		param.add(1);

		List<Map<String, Object>> iqBolist = adminDao.getiq_bolist(param);
		if(iqBolist.size()==0) {
			System.out.println("등록된 문의글이 없습니다.");
			return View.ADM_BO;
		}
		for(int i = 0; i <iqBolist.size(); i++) {
			listNum.put(i, iqBolist.get(i).get("BO_NO"));
			System.out.printf("%s│%s│%s│%s│"
					, SpaceUtil.format(i+1, 6, 0)
					, SpaceUtil.format(iqBolist.get(i).get("BO_WRITER"), 8, 0)
					, SpaceUtil.format(iqBolist.get(i).get("BO_TITLE"),30, 0)
					, SpaceUtil.dateFormat(iqBolist.get(i).get("BO_DATE"), 12, 1)
		);
			System.out.println();
		}
		System.out.println();
		System.out.println("╭────────────────────────────────────────────╮");
		System.out.println("│ 1.문의글 상세조회 2.문의글 삭제 3.뒤로가기 │ ");
		System.out.println("╰────────────────────────────────────────────╯");
		System.out.print("번호를 입력해주세요 ➤➤ ");
		switch(ScanUtil.nextInt()) {
		case 1: return View.ADMIQ_RESERCH;
		case 2: return View.ADMIQ_DELETE;
		case 3: return View.ADM_BO;
		}
		return View.ADMIQ_BOLIST;
	}
	
	public int admiq_reserch() {
		input:
		while(true) {
			System.out.print("조회하실 번호를 선택해주세요 ➤➤ ");
			num = ScanUtil.nextInt();
			if(num > listNum.size() || num < 1) {
				System.out.println("없는 번호입니다. 다시 선택해주세요! ");
			}else {
				break input;
			}
		}
	
		List<Object> param = new ArrayList<>();
		param.add(Integer.parseInt(listNum.get(num-1).toString()));
		
		List<Map<String,Object>> rsInfo = adminDao.getrs_info(param);
		if(rsInfo == null || rsInfo.size() ==0) {
			System.out.println("없는 번호입니다. !!! ");
			return View.ADMIQ_BOLIST;
		}
		clear();
		System.out.println();
		System.out.println("================================ 문의글 상세보기 ================================");
		System.out.println("제목 : " + rsInfo.get(0).get("BO_TITLE"));
		System.out.println("작성자 : " + rsInfo.get(0).get("BO_WRITER"));
		System.out.printf("작성일자 : %s\n",SpaceUtil.dateFormat(rsInfo.get(0).get("BO_DATE"), 0, 0));
		System.out.println("내용 : " + rsInfo.get(0).get("BO_CONTENT"));
		System.out.println("==================================================================================\r\n" + 
				"");
//		
//		List<Object> param1 = new ArrayList<>();
//		param1.add(Integer.parseInt(listNum.get(num-1).toString()));
		
		List<Map<String, Object>> iqRelist = adminDao.getiq_relist(param);
		if(iqRelist.size()==0) {
			System.out.println("작성된 답변이 없습니다.");
		}else {
		System.out.println("╔═══════════════════════════════════ 문의답변 ═══════════════════════════════════╗");
		for(int i = 0; i < iqRelist.size(); i++) {
	         listNum1.put(i, iqRelist.get(i).get("RE_NO"));
			System.out.println("    ────────────────────────────────────────────────────────────────────────");

			System.out.printf("          %s│%s│%s│%s"
					, SpaceUtil.format(i+1, 6, 0)
					, SpaceUtil.format(iqRelist.get(i).get("RE_WRITER"), 8, 0)
					, SpaceUtil.format(iqRelist.get(i).get("RE_CONTENT"),15, 0)
					, SpaceUtil.dateFormat(iqRelist.get(i).get("RE_DATE"), 15, 0)
		);
			System.out.println();
			System.out.println("    ────────────────────────────────────────────────────────────────────────");
		}
		System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
		}	
		System.out.println();
		System.out.println("╭──────────────────────────────────────────────╮");
		System.out.println("│1.답변 작성 2.답변 수정 3.답변 삭제 4.뒤로가기│");
		System.out.println("╰──────────────────────────────────────────────╯");
		System.out.print("번호를 입력해주세요 ➤➤ ");
		
		switch(ScanUtil.nextInt()) {
		case 1: return View.ADMIQ_ANSWER;
		case 2: return View.ADMIQ_ANUPDATE;
		case 3: return View.ADMIQ_ANDELETE;
		case 4: return View.ADMIQ_BOLIST;
		}
		return View.ADMIQ_BOLIST;
	}
	
	public int admiq_answer() {
		System.out.print("답변 내용을 작성해주세요 ➤➤ ");
		String content = ScanUtil.nextLine();

		List<Object> param = new ArrayList<>();
		param.add(listNum.get(num-1));
		param.add(content);
		
		int iqresult = adminDao.getiq_answer(param);
		if(iqresult >0) {
			System.out.println("답변 작성이 완료되었습니다.");
		}else {
			System.out.println("답변 작성이 실패하였습니다.");
		}
		return View.ADMIQ_BOLIST;
	}
	public int admiq_anupdate() {
		System.out.print("수정할 답변 번호를 입력해주세요 ➤➤ ");
		int number = ScanUtil.nextInt();
		System.out.print("내용을 수정해주세요 ➤➤ ");
		String content = ScanUtil.nextLine();
		
		List<Object> param = new ArrayList<>();
		param.add(content);
		param.add(listNum1.get(number-1));
		
		int iqanupdate = adminDao.getiq_anupdate(param);
		if(iqanupdate >0) {
			System.out.println("답변 수정이 완료되었습니다.");
		}else {
			System.out.println("답변 수정을 실패하였습니다!");
		}
		return View.ADMIQ_BOLIST;
	}
	
	public int admiq_andelete() {
		System.out.println("삭제하실 답변 번호를 입력해주세요 ➤➤");
		int number = ScanUtil.nextInt();
		
		List<Object> param = new ArrayList<>();
		param.add(listNum1.get(number-1));
		System.out.println("해당 답변을 정말 삭제하시겠습니까? (y/n)");
	
		switch((ScanUtil.nextLine().toUpperCase()).trim()) {
		case "Y":
		int iqandelete = adminDao.getiq_andelete(param);
		if(iqandelete >0) {
			System.out.println("답변이 삭제되었습니다.");
			ScanUtil.nextLine();
			return View.ADMIQ_BOLIST;

		}else {
			System.out.println("답변 삭제를 실패하였습니다!");
			return View.ADMIQ_BOLIST;
		}
		case "N":
			System.out.println("이전화면으로 이동하겠습니다.");
			return View.ADMIQ_BOLIST;
		default : 
			System.out.println("잘못입력하셨습니다. 이전 화면으로 이동하겠습니다.");
			return View.ADMIQ_BOLIST;
		}
	}
	
	public int admiq_delete() {
		System.out.println("삭제하실 문의글 번호를 선택해주세요");
		num = ScanUtil.nextInt();
		List<Object> param = new ArrayList<>();
		param.add(listNum.get(num-1));
		System.out.println("해당 답변을 정말 삭제하시겠습니까? (y/n)");
		switch((ScanUtil.nextLine().toUpperCase()).trim()) {
		case "Y":
		int iqdelete = adminDao.getiq_delete(param);
		if(iqdelete >0) {
			System.out.println("해당 문의글이 삭제되었습니다");
			return View.ADMIQ_BOLIST;
		}else {
			System.out.println("삭제를 실패하였습니다!");
			return View.ADMIQ_BOLIST;
		}
		case "N":
			System.out.println("이전화면으로 이동하겠습니다.");
			return View.ADMIQ_BOLIST;
		default : 
			System.out.println("잘못입력하셨습니다. 이전 화면으로 이동하겠습니다.");
			return View.ADMIQ_BOLIST;
		}
	}
	
	public int admrv_bolist() {
		System.out.println("┍━━━━━━━━━━━━━━━━━━━━━━━━━━━»•» 🌸 «•«━┑");
		System.out.println("  1. 차량 후기 2.숙소 후기 3.관리자메뉴");
		System.out.println("﻿┕━»•» 🌸 «•«━━━━━━━━━━━━━━━━━━━━━━━━━━━┙");
		System.out.print("번호를 입력해주세요 ➤➤ ");

		switch(ScanUtil.nextInt()) {
		case 1: return View.ADMRVC_BOLIST;
		case 2: return View.ADMRVP_BOLIST;
		case 3: return View.ADMIN;
	}
		return View.ADMRV_BO;
 }
	
	public int admrvc_bolist() {
		System.out.println();
		System.out.println("═══════════════════════ 차량 후기 게시판 ══════════════════════");
		System.out.printf("%s│%s│%s│%s"
						, SpaceUtil.format("번호", 6, 0)
						, SpaceUtil.format("작성자", 8, 0)
						, SpaceUtil.format("제목", 30, 0)
						, SpaceUtil.format("날짜", 12, 0)
			);
		System.out.println();
		System.out.println("﻿═══════════════════════════════════════════════════════════════");
		
		List<Object> param = new ArrayList<>();
		param.add(2);

		List<Map<String, Object>> rvcBolist = adminDao.getrvc_bolist(param);
		if(rvcBolist.size()==0) {
			System.out.println("등록된 후기글이 없습니다.");
			ScanUtil.nextLine();
			return View.ADM_BO;
		}
		for(int i = 0; i <rvcBolist.size(); i++) {
			listNum.put(i, rvcBolist.get(i).get("BO_NO"));
			System.out.printf("%s│%s│%s│%s│"
					, SpaceUtil.format(i+1, 6, 0)
					, SpaceUtil.format(rvcBolist.get(i).get("BO_WRITER"), 8, 0)
					, SpaceUtil.format(rvcBolist.get(i).get("BO_TITLE"),30, 0)
					, SpaceUtil.dateFormat(rvcBolist.get(i).get("BO_DATE"), 12, 1)
		);
			System.out.println();
		}
		System.out.println();
		System.out.println("╭────────────────────────────────────────────╮");
		System.out.println("│ 1.후기글 상세조회 2.후기글 삭제 3.뒤로가기 │ ");
		System.out.println("╰────────────────────────────────────────────╯");
		System.out.print("번호를 입력해주세요 ➤➤ ");
		switch(ScanUtil.nextInt()) {
		case 1: return View.ADMRVC_RESERCH;
		case 2: return View.ADMRVC_DELETE;
		case 3: return View.ADMRV_BO;
		}
		return View.ADMRVC_BOLIST;
	}
	
	public int admrvc_reserch() {
		input: while (true) {
			System.out.print("조회하실 번호를 선택해주세요 ➤➤ ");
			num = ScanUtil.nextInt();
			if (num > listNum.size() || num < 1) {
				System.out.println("없는 번호입니다. 다시 선택해주세요! ");
			} else {
				break input;
			}
		}
		List<Object> param = new ArrayList<>();
		param.add(Integer.parseInt(listNum.get(num-1).toString()));
		
		List<Map<String,Object>> rscInfo = adminDao.getrsc_info(param);
		if(rscInfo == null) {
			System.out.println("없는 번호입니다.! ");
			return View.ADMRVC_BOLIST;
		}
		System.out.println();
		System.out.println("============================== 차량 후기글 상세보기 ==============================");
		System.out.println("제목 : " + rscInfo.get(0).get("BO_TITLE"));
		System.out.println("작성자 : " + rscInfo.get(0).get("BO_WRITER"));
		System.out.println("작성일자 : " + rscInfo.get(0).get("BO_DATE"));
		System.out.println("별점 : " + rscInfo.get(0).get("BO_STARS"));
		System.out.println("내용 : " + rscInfo.get(0).get("BO_CONTENT"));
		System.out.println("==================================================================================");		
		System.out.println();
		
		List<Map<String, Object>> rvcRelist = adminDao.getrvc_relist(param);
		if(rvcRelist.size()==0) {
			System.out.println("작성된 댓글이 없습니다.");
		}else {
		System.out.println("╔════════════════════════════════ 차량 후기글 댓글 ════════════════════════════╗");
		for(int i = 0; i < rvcRelist.size(); i++) {
	         listNum1.put(i, rvcRelist.get(i).get("RE_NO"));
			System.out.println("    ────────────────────────────────────────────────────────────────────────");
			System.out.printf("   %s│%s│%s│%s"
				, SpaceUtil.format(i+1, 6, 0)
				, SpaceUtil.format(rvcRelist.get(i).get("RE_WRITER"), 8, 0)
				, SpaceUtil.format(rvcRelist.get(i).get("RE_CONTENT"),15, 0)
				, SpaceUtil.dateFormat(rvcRelist.get(i).get("RE_DATE"), 15, 0)
		);
			System.out.println();
			System.out.println("    ────────────────────────────────────────────────────────────────────────");
		}
		System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
		}
		System.out.println();
		System.out.println("╭──────────────────────────────────────────────╮");
		System.out.println("│1.댓글 작성 2.댓글 수정 3.댓글 삭제 4.뒤로가기│");
		System.out.println("╰──────────────────────────────────────────────╯");
		System.out.print("번호를 입력해주세요 ➤➤ ");

		
		switch(ScanUtil.nextInt()) {
		case 1: return View.ADMRVC_ANSWER;
		case 2: return View.ADMRVC_ANUPDATE;
		case 3: return View.ADMRVC_ANDELETE;
		case 4: return View.ADMRVC_BOLIST;
		}
		return View.ADMRVC_BOLIST;
	}
	
	public int admrvc_answer() {

		System.out.println("댓글 내용을 작성해주세요 ➤➤ ");
		String content = ScanUtil.nextLine();

		List<Object> param = new ArrayList<>();
		param.add(listNum.get(num-1));
		param.add(content);
		
		int rvcresult = adminDao.getrvc_answer(param);
		if(rvcresult >0) {
			System.out.println("댓글 작성이 완료되었습니다");
		}else {
			System.out.println("댓글 작성을 실패하였습니다");
		}
		return View.ADMRVC_BOLIST;
	}
	
	public int admrvc_anupdate() {
		System.out.println("수정할 댓글 번호를 입력해주세요");
		int number = ScanUtil.nextInt();
		System.out.println("댓글 내용을 수정해주세요 ➤➤  ");
		String content = ScanUtil.nextLine();
		
		List<Object> param = new ArrayList<>();
		param.add(content);
		param.add(listNum1.get(number-1));
		
		int rvcanupdate = adminDao.getrvc_anupdate(param);
		if(rvcanupdate >0) {
			System.out.println("댓글 수정이 완료되었습니다");
		}else {
			System.out.println("댓글 수정을 실패하였습니다!");
		}
		return View.ADMRVC_BOLIST;
	}
	
	public int admrvc_andelete() {
		System.out.println("삭제하실 댓글 번호를 입력해주세요 ➤➤  ");
		int number = ScanUtil.nextInt();
		
		List<Object> param = new ArrayList<>();
		param.add(listNum1.get(number-1));
		
		System.out.println("해당 댓글을 정말 삭제하시겠습니까? (y/n)");
		switch((ScanUtil.nextLine().toUpperCase()).trim()) {
		case "Y":
		int rvcandelete = adminDao.getrvc_andelete(param);
		if(rvcandelete >0) {
			System.out.println("댓글이 삭제되었습니다");
			return View.ADMRVC_BOLIST;
		}else {
			System.out.println("댓글 삭제를 실패하였습니다!");
			return View.ADMRVC_BOLIST;
		}
		case "N":
			System.out.println("이전화면으로 이동하겠습니다.");
			return View.ADMRVC_BOLIST;
		default : 
			System.out.println("잘못입력하셨습니다. 이전 화면으로 이동하겠습니다.");
			return View.ADMRVC_BOLIST;
		}
	}
	
	public int admrvc_delete() {
		System.out.println("삭제하실 후기글 번호를 선택해주세요 ➤➤ ");
		num = ScanUtil.nextInt();
		List<Object> param = new ArrayList<>();
		param.add(listNum.get(num-1));
		
		System.out.println("해당 후기글을 정말 삭제하시겠습니까? (y/n)");
		switch((ScanUtil.nextLine().toUpperCase()).trim()) {
		case "Y":
		int rvcdelete = adminDao.getrvc_delete(param);
		if(rvcdelete >0) {
			System.out.println("해당 후기글을 삭제하였습니다.");
			return View.ADMRVC_BOLIST;
		}else {
			System.out.println("삭제를 실패하였습니다!");
			return View.ADMRVC_BOLIST;
		}
		case "N":
			System.out.println("이전화면으로 이동하겠습니다.");
			return View.ADMRVC_BOLIST;
		default : 
			System.out.println("잘못입력하셨습니다. 이전 화면으로 이동하겠습니다.");
			return View.ADMRVC_BOLIST;
		}
	}
	
	public int admrvp_bolist() {
		System.out.println();
		System.out.println("═══════════════════════ 숙소 후기 게시판 ══════════════════════");
		System.out.printf("%s│%s│%s│%s│"
						, SpaceUtil.format("번호", 6, 0)
						, SpaceUtil.format("작성자", 8, 0)
						, SpaceUtil.format("제목", 30, 0)
						, SpaceUtil.format("날짜", 15, 0)
			);
		System.out.println();
		System.out.println("﻿═══════════════════════════════════════════════════════════════");
		
		
		List<Object> param = new ArrayList<>();
		param.add(3);

		List<Map<String, Object>> rvpBolist = adminDao.getrvp_bolist(param);
		if(rvpBolist.size()==0) {
			System.out.println("등록된 문의글이 없습니다.");
			ScanUtil.nextLine();
			return View.ADM_BO;
		}
		for(int i = 0; i <rvpBolist.size(); i++) {
			listNum.put(i, rvpBolist.get(i).get("BO_NO"));
			System.out.printf("%s│%s│%s│%s│"
					, SpaceUtil.format(i+1, 6, 0)
					, SpaceUtil.format(rvpBolist.get(i).get("BO_WRITER"), 8, 0)
					, SpaceUtil.format(rvpBolist.get(i).get("BO_TITLE"),30, 0)
					, SpaceUtil.dateFormat(rvpBolist.get(i).get("BO_DATE"), 12, 1)
		);
			System.out.println();
		}
		System.out.println();
		System.out.println("╭────────────────────────────────────────────╮");
		System.out.println("│ 1.후기글 상세조회 2.후기글 삭제 3.뒤로가기 │ ");
		System.out.println("╰────────────────────────────────────────────╯");
		System.out.print("번호를 입력해주세요 ➤➤ ");

		switch(ScanUtil.nextInt()) {
		case 1: return View.ADMRVP_RESERCH;
		case 2: return View.ADMRVP_DELETE;
		case 3: return View.ADMRV_BO;
		}
		return View.ADMRVP_BOLIST;
	}
	public int admrvp_reserch() {
		input: while (true) {
			System.out.print("조회하실 번호를 선택해주세요 ➤➤ ");
			num = ScanUtil.nextInt();
			if (num > listNum.size() || num < 1) {
				System.out.println("없는 번호입니다. 다시 선택해주세요! ");
			} else {
				break input;
			}
		}
		List<Object> param = new ArrayList<>();
		param.add(Integer.parseInt(listNum.get(num-1).toString()));
		List<Map<String,Object>> rspInfo = adminDao.getrsp_info(param);
		if(rspInfo == null) {
			System.out.println("없는 번호입니다!!!! ");
			return View.ADMRVP_BOLIST;

		}
		System.out.println();
		System.out.println("============================== 숙소 후기글 상세보기 ==============================");
		System.out.println("제목 : " + rspInfo.get(0).get("BO_TITLE"));
		System.out.println("작성자 : " + rspInfo.get(0).get("BO_WRITER"));
		System.out.println("작성일자 : " + rspInfo.get(0).get("BO_DATE"));
		System.out.println("별점 : " + rspInfo.get(0).get("BO_STARS"));
		System.out.println("내용 : " + rspInfo.get(0).get("BO_CONTENT"));
		System.out.println("==================================================================================");
		System.out.println();
		
		List<Map<String, Object>> rvpRelist = adminDao.getrvp_relist(param);
		if(rvpRelist.size()==0) {
			System.out.println("작성된 댓글이 없습니다.");
		}else {
		System.out.println("╔════════════════════════════════ 숙소 후기글 댓글 ════════════════════════════╗");
		for(int i = 0; i < rvpRelist.size(); i++) {
	         listNum1.put(i, rvpRelist.get(i).get("RE_NO"));
			System.out.println("    ────────────────────────────────────────────────────────────────────────");
		
			System.out.printf("   %s│%s│%s│%s"
					, SpaceUtil.format(i+1, 6, 0)
					, SpaceUtil.format(rvpRelist.get(i).get("RE_WRITER"), 8, 0)
					, SpaceUtil.format(rvpRelist.get(i).get("RE_CONTENT"),15, 0)
					, SpaceUtil.dateFormat(rvpRelist.get(i).get("RE_DATE"), 15, 0)
		);
			System.out.println();
			System.out.println("    ────────────────────────────────────────────────────────────────────────");
		}
		System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
		}
		System.out.println();
		System.out.println("╭────────────────────────────────────────────────╮");
		System.out.println("│1.댓글 작성 2.댓글 수정 3.댓글 삭제 4.뒤로가기│");
		System.out.println("╰────────────────────────────────────────────────╯");
		System.out.print("번호를 입력해주세요 ➤➤ ");

		
		switch(ScanUtil.nextInt()) {
		case 1: return View.ADMRVP_ANSWER;
		case 2: return View.ADMRVP_ANUPDATE;
		case 3: return View.ADMRVP_ANDELETE;
		case 4: return View.ADMRVP_BOLIST;
		}
		return View.ADMRVP_BOLIST;
	}
	
	public int admrvp_answer() {
		System.out.println("댓글 내용을 작성해주세요 ➤➤ ");
		String content = ScanUtil.nextLine();

		List<Object> param = new ArrayList<>();
		param.add(listNum.get(num-1));
		param.add(content);
		
		int rvpresult = adminDao.getrvp_answer(param);
		if(rvpresult >0) {
			System.out.println("댓글 작성이 완료되었습니다.");
		}else {
			System.out.println("댓글 작성을 실패하였습니다.");
		}
		return View.ADMRVP_BOLIST;
	}
	
	public int admrvp_anupdate() {
		System.out.println("수정할 댓글 번호를 입력해주세요 ➤➤");
		int number = ScanUtil.nextInt();
		System.out.println("내용을 수정해주세요 ➤➤ ");
		String content = ScanUtil.nextLine();
		
		List<Object> param = new ArrayList<>();
		param.add(content);
		param.add(listNum1.get(number-1));
		
		int rvpanupdate = adminDao.getrvp_anupdate(param);
		if(rvpanupdate >0) {
			System.out.println("댓글 수정이 완료되었습니다.");
		}else {
			System.out.println("댓글 수정을 실패하였습니다!");
		}
		return View.ADMRVP_BOLIST;
	}
	public int admrvp_andelete() {
		System.out.println("삭제하실 댓글 번호를 입력해주세요 ➤➤");
		int number = ScanUtil.nextInt();
		
		List<Object> param = new ArrayList<>();
		param.add(listNum1.get(number-1));
		System.out.println("해당 댓글을 정말 삭제하시겠습니까? (y/n)");
		
		switch((ScanUtil.nextLine().toUpperCase()).trim()) {
		case "Y":
		int rvpandelete = adminDao.getrvp_andelete(param);
		if(rvpandelete >0) {
			System.out.println("댓글이 삭제되었습니다.");
			return View.ADMRVP_BOLIST;
		}else {
			System.out.println("댓글 삭제를 실패하였습니다!");
			return View.ADMRVP_BOLIST;
		}
		case "N":
			System.out.println("이전화면으로 이동하겠습니다.");
			return View.ADMRVP_BOLIST;
		default : 
			System.out.println("잘못입력하셨습니다. 이전 화면으로 이동하겠습니다.");
			return View.ADMRVP_BOLIST;
		}
	}
	public int admrvp_delete() {
		System.out.println("삭제하실 후기글 번호를 선택해주세요 ➤➤");
		num = ScanUtil.nextInt();
		List<Object> param = new ArrayList<>();
		param.add(listNum.get(num-1));
		System.out.println("해당 후기글을 정말 삭제하시겠습니까? (y/n)");
		switch((ScanUtil.nextLine().toUpperCase()).trim()) {
		case "Y":
		int rvpdelete = adminDao.getrvp_delete(param);
		if(rvpdelete >0) {
			System.out.println("해당 후기글이 삭제되었습니다.");
			return View.ADMRVP_BOLIST;
		}else {
			System.out.println("삭제를 실패하였습니다!");
			return View.ADMRVP_BOLIST;
		}
		case "N":
			System.out.println("이전화면으로 이동하겠습니다.");
			return View.ADMRVP_BOLIST;
		default : 
			System.out.println("잘못입력하셨습니다. 이전 화면으로 이동하겠습니다.");
			return View.ADMRVP_BOLIST;
		}
	}
	
	public void clear() {
		for (int i = 0; i < 50; i++) {
			System.out.println();
		}
	}
	
}


