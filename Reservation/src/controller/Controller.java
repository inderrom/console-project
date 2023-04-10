package controller;

import java.util.HashMap;
import java.util.Map;

import service.AdminService;
import service.BoardService;
import service.CarBoardService;
import service.CarService;
import service.MemberService;
import service.MypageService;
import service.PensionBoardService;
import service.PensionService;
import util.ScanUtil;
import util.View;

public class Controller {
	static public Map<String, Object> sessionStorage = new HashMap<>();
	MemberService memberService = MemberService.getInstance();
	CarService carService = CarService.getInstance();
	PensionService pensionService = PensionService.getInstance();
	MypageService mypageService = MypageService.getInstance();
	AdminService adminservice = AdminService.getInstance();
	BoardService boardService = BoardService.getInstance();
	CarBoardService carBoardService = CarBoardService.getInstance();
	PensionBoardService pensionBoardService = PensionBoardService.getInstance();
	
	public static void main(String[] args) throws InterruptedException {
		new Controller().start();
	}

	private void start() throws InterruptedException {
		sessionStorage.put("login", false);
		sessionStorage.put("loginInfo", null);
		int view = View.HOME;
		while(true) {
			switch(view) {
			case View.HOME: view = home(); break;
			case View.USER_LOGIN: view = memberService.userLogin(); break;
			case View.SIGNUP: view = memberService.signup(); break;
			case View.MAIN: view = main(); break;
			case View.MYPAGE: view = myPage(); break;
			case View.FIND_ID: view = memberService.findId(); break;
			case View.FIND_PW: view = memberService.findPw(); break;
			
			// 내정보
			case View.MY_INFO: view = mypageService.myInfo(); break;
			case View.RES_INFO: view = mypageService.resInfo(); break;				// 예약 정보
			case View.USER_LOGOUT: view = memberService.userLogout(); break;
			case View.UPDATE_INFO: view = mypageService.updateInfo(); break;
			case View.UPDATE_PW: view = mypageService.updatePw(); break;
			case View.UPDATE_PHONE: view = mypageService.updatePN(); break;
			case View.UPDATE_ADDR: view = mypageService.updateAddr(); break;
			case View.UPDATE_LICENSE: view = mypageService.updateLicense(); break;
			case View.PENSHON_CANCLE: view = mypageService.pensionCancel(); break; 	// 숙소 취소
			case View.CAR_CANCLE: view = mypageService.carCancel(); break; 			// 예약 취소
			case View.CARD_MANAGE: view = mypageService.cardManage(); break;				// 카드 추가
			case View.CARD_ADD: view = mypageService.cardAdd(); break;
			case View.CARD_DELETE: view = mypageService.cardDelete(); break;
			
			
			
			// 관리자
			case View.ADMIN: view = admin(); break;
			case View.ADM_BO : view = adminservice.adm_bo(); break;
			
			case View.ADMIQ_BOLIST: view = adminservice.admiq_bolist();break;
			case View.ADMIQ_RESERCH: view = adminservice.admiq_reserch();break;
			case View.ADMIQ_ANSWER: view = adminservice.admiq_answer(); break;
			case View.ADMIQ_ANUPDATE: view = adminservice.admiq_anupdate();break;
			case View.ADMIQ_ANDELETE: view = adminservice.admiq_andelete();break;
			case View.ADMIQ_DELETE: view = adminservice.admiq_delete();break;
			
			case View.ADMRV_BO: view = adminservice.admrv_bolist();break;
			case View.ADMRVC_BOLIST: view = adminservice.admrvc_bolist();break;
			case View.ADMRVC_RESERCH: view = adminservice.admrvc_reserch();break;
			case View.ADMRVC_ANSWER: view = adminservice.admrvc_answer();break;
			case View.ADMRVC_ANUPDATE: view = adminservice.admrvc_anupdate();break;
			case View.ADMRVC_ANDELETE: view = adminservice.admrvc_andelete();break;
			case View.ADMRVC_DELETE: view = adminservice.admrvc_delete();break;
			
			case View.ADMRVP_BOLIST: view = adminservice.admrvp_bolist();break;
			case View.ADMRVP_RESERCH: view = adminservice.admrvp_reserch();break;
			case View.ADMRVP_ANSWER: view = adminservice.admrvp_answer();break;
			case View.ADMRVP_ANUPDATE: view = adminservice.admrvp_anupdate();break;
			case View.ADMRVP_ANDELETE: view = adminservice.admrvp_andelete();break;
			case View.ADMRVP_DELETE: view = adminservice.admrvp_delete();break;

			case View.ADM_SALES: view = adminservice.adm_sales(); break;
			case View.ADMC_SALES: view = adminservice.admc_sales();break;
			//case View.ADMC_SALIST: view = adminservice.admc_salist();break;
			case View.ADMP_SALES: view = adminservice.admp_sales();break;
			case View.ADMT_SALES: view = adminservice.admt_sales();break;
			
			// 게시판
			case View.BOARD: view = boardService.board(); break;
			
			case View.INQUIRY_BOARD: view = boardService.inquiryBoard(); break;
			case View.INQUIRY_INSERT: view = boardService.inquiryInsert(); break;
			case View.INQUIRY_DETAIL: view = boardService.inquiryDetail(); break;
			case View.INQUIRY_MODIFY: view = boardService.inquiryModify(); break;
			case View.INQUIRY_DELETE: view = boardService.inquiryDelete(); break;
			
			case View.REVIEW_BOARD: view = boardService.reviewBoard(); break;
			
			case View.CAR_REVIEW_BOARD: view = carBoardService.carReviewBoard(); break;
			case View.CAR_REVIEW_INSERT: view = carBoardService.carReviewInsert(); break;
			case View.CAR_REVIEW_DETAIL: view = carBoardService.carReviewDtail(); break;
			
			case View.CAR_BOARD_REFER : view = carBoardService.carBoardRefer(); break;
			case View.CAR_REVIEW_REFER : view = carBoardService.carReviewRefer(); break;
			case View.CAR_REPLY_REFER: view = carBoardService.carReplyRefer(); break;
//			case View.CAR_REVIEW_MODIFY: view = carBoardService.carReviewModify(); break;
//			case View.CAR_REVIEW_DELETE: view = boardService.carReviewDelete(); break;
			
			case View.PEN_REVIEW_BOARD: view = pensionBoardService.penReviewBoard(); break;
			case View.PEN_REVIEW_INSERT: view = pensionBoardService.penReviewInsert(); break;
			case View.PEN_REVIEW_DETAIL: view = pensionBoardService.penReviewDtail(); break;
			
			case View.PEN_BOARD_REFER: view = pensionBoardService.penBoardRefer(); break;
			case View.PEN_REVIEW_REFER: view = pensionBoardService.penReviewRefer(); break;
			case View.PEN_REPLY_REFER: view = pensionBoardService.penReplyRefer(); break;
			
			// 차 예약
			case View.CARRE: view = carService.carre(); break;
			case View.RESERVATIONSTART: view=carService.reservationstart(); break;
			case View.LICENSE1: view=carService.license1(); break;
			case View.LICENSE2: view=carService.license2(); break;
			case View.RESERVATION: view=carService.reservation(); break;
			case View.CARDETAIL: view=carService.carDetail(); break;
			case View.INPUTCNO: view=carService.inputCno(); break;
			case View.MILEAGE: view=carService.mileage(); break;
			case View.ALLMILEAGE: view=carService.allmileage(); break;
			case View.PARTITIONMILEAGE: view=carService.partitionmileage(); break;
			case View.PAYMENT: view=carService.payment(); break;
			
			// 숙소 예약
			case View.PENSIONSTATE: view = pensionService.pensionState();
			case View.PENSION_RES: view = pensionService.pensionRes(); break;
			case View.PENSION_SELECT: view = pensionService.pension_select(); break;
			case View.PENSION_PAYMENT: view = pensionService.pension_payment(); break;			
			case View.PENSION_CARD_REGIST: view = pensionService.card_regist(); break;
			case View.PENSION_MILE: view = pensionService.pension_mile(); break;
			default: view = home(); break;
			}
		}
	}

	private int home() {
		clear();
		System.out.println("====================================================");
		System.out.println("              여기갈까? 옆에 탈래?");
		System.out.println("====================================================");
		System.out.println("1.로그인  2.회원가입  3.아이디 찾기  4.비밀번호 찾기");
		System.out.println("====================================================");
		System.out.print("번호 입력 >> ");
		switch(ScanUtil.nextInt()) {
		case 1: return View.USER_LOGIN;
		case 2: return View.SIGNUP;
		case 3: return View.FIND_ID;
		case 4: return View.FIND_PW;
		default: return View.HOME;
		}
	}
	
	private int main() {
		clear();
		System.out.println("==========================================================");
		System.out.println("                 여기갈까? 옆에 탈래? ");
		System.out.println("==========================================================");
		System.out.println("1.차량 예약  2.숙소 예약  3.내정보  4.게시판  5.로그아웃");
		System.out.println("==========================================================");
		System.out.print("번호 입력 >> ");
		int num = ScanUtil.nextInt();
		clear();
		switch(num) {
		case 1: return View.CARRE;
		case 2: return View.PENSIONSTATE;
		case 3: return View.MYPAGE;
		case 4: return View.BOARD;
		case 5: return View.USER_LOGOUT;
		default: return View.MAIN;
		}
		
	}
	
	private int admin() {
		clear();
		System.out.println(" ▰▱▰▱▰▱▰▱▰▱▰▱ 관리자 메뉴 ▰▱▰▱▰▱▰▱▰▱▰▱");
		System.out.println(" 1.게시물 관리 2.매출 관리 3.로그아웃");
		System.out.println(" ▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰▱▰");
		System.out.print(" 번호를 입력해주세요 ➤➤ ");
		switch(ScanUtil.nextInt()) {
		case 1: return View.ADM_BO;
		case 2: return View.ADM_SALES;
		case 3: return View.USER_LOGOUT;
		default: return View.ADMIN;
		}
	}
	
	private int myPage() {
		clear();
		System.out.println("============ 여기갈까? 옆에 탈래? ===========");
		System.out.println("1.상세정보  2.예약 현황  3.카드관리  0.홈으로");
		System.out.println("=============================================");
		System.out.print("번호 입력 >> ");
		int num = ScanUtil.nextInt();		
		clear();
		switch(num) {
		case 1:
			clear();
			return View.MY_INFO; 
		case 2: return View.RES_INFO;
		case 3: return View.CARD_MANAGE;
		case 0: return View.MAIN;
		default: return View.MYPAGE;
		}
	}
	
	public void clear() {
		for (int i = 0; i < 50; i++) {
			System.out.println();
		}
	}
}

	















