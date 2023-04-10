package util;

public interface View {
	// 싱글톤 생성자가 하나이다. 하나만 만들어서 사용해라
	// 싱글톤 패턴은 '하나'의 인스턴스만 생성하여 사용하는 디자인 패턴이다. 
	// 인스턴스가 필요할 때, 똑같은 인스턴스를 만들지 않고 기존의 인스턴스를 활용하는 것!
	// 일반적으로 실행 중인 임의의 프로세스, 클래스의 현재 생성된 오브젝트
	
	// 아무거나 겹치지 않게 사용
	// 팀원들과 합의 하에 사용
	
	int HOME = 1;				// 홈
	int USER_LOGIN = 2;	    	// 유저 로그인
	int SIGNUP = 3;		    	// 회원가입
	int FIND_ID = 4;			// 아이디 찾기
	int FIND_PW = 5;			// 비밀번호 찾기
	int ADM_MAIN = 6;	    	// 관리자 메인
	int MAIN = 7;		    	// 로그인 후
	int MYPAGE = 8;		    	// 내정보
	int USER_LOGOUT = 9;    	// 로그 아웃
	
	// 내정보
	int MY_INFO = 10;			// 내 상세정보
	int	UPDATE_INFO = 11;   	// 내정보 수정
	int UPDATE_PW = 12;			// 비밀번호 수정
	int UPDATE_PHONE = 13;		// 전화번호 수정
	int UPDATE_ADDR = 14;		// 주소 수정
	int UPDATE_LICENSE = 15;	// 면허증 수정
	int RES_INFO = 16;	    	// 예약 확인
	int PENSHON_CANCLE = 17;	// 숙소 예약 취소
	int CAR_CANCLE = 18; 		// 차량 예약 취소
	int CARD_MANAGE = 19;		// 카드 관리
	int CARD_ADD = 100;			// 카드 추가
	int CARD_DELETE = 101;		// 카드 삭제
	
	// 차 예약
	int CARRE = 20;
	int RESERVATIONSTART = 21;
	int LICENSE1 = 22; // 예약자가 1종일때
	int LICENSE2 = 23; // 예약자가 2종일때
	int RESERVATION = 24; // 예약
	int CARDETAIL = 25; // 차량명 입력 후 차량 상세정보
	int INPUTCNO = 26; // 차량구분 누른 후
	int MILEAGE = 27; // 차량상세정보까지 본 뒤 마일리지 사용여부 나타내는거
	int ALLMILEAGE = 28;// 마일리지 몽땅 결제
	int PARTITIONMILEAGE = 29; // 마일리지 부분 결제
	int PAYMENT = 200; //마일리지로 계산 안하고 그냥 최종 결제
	
	// 게시판
	int BOARD = 30;
	   
	int INQUIRY_BOARD = 31;
	int INQUIRY_INSERT = 32;
	int INQUIRY_DETAIL = 33;
	int INQUIRY_MODIFY = 34;
	int INQUIRY_DELETE = 35;
	
	int REVIEW_BOARD = 36;
	
	int CAR_REVIEW_BOARD = 37;
	int CAR_REVIEW_INSERT = 38;
	int CAR_REVIEW_DETAIL = 39;
	
	int CAR_BOARD_REFER = 300;
	int CAR_REVIEW_REFER = 301;
	int CAR_REPLY_REFER = 302;
	
	int CAR_REVIEW_MODIFY = 303;
//   int CAR_REVIEW_DELETE = 305;

   
	int PEN_REVIEW_BOARD = 304;
	int PEN_REVIEW_INSERT = 305;
	int PEN_REVIEW_DETAIL = 306;
   
	int PEN_BOARD_REFER = 307;
	int PEN_REVIEW_REFER = 308;
	int PEN_REPLY_REFER = 309;
	
	// 관리자
	int ADMIN = 40;
	
	int ADM_BO =41;
	int ADMIQ_BOLIST = 42;
	int ADMIQ_RESERCH =43;
	int ADMIQ_ANSWER = 44;
	int ADMIQ_ANUPDATE = 45;
	int ADMIQ_ANDELETE=46;
	
	int ADMIQ_DELETE = 47;
	
	int ADMRV_BO = 48;
	
	int ADMRVC_BOLIST = 49;
	int ADMRVC_RESERCH = 400;
	int ADMRVC_ANSWER = 401;
	int ADMRVC_ANUPDATE = 402;
	int ADMRVC_ANDELETE=403;
	
	int ADMRVP_BOLIST = 404;
	int ADMRVP_RESERCH = 405;
	int ADMRVP_ANSWER = 406;
	int ADMRVP_ANUPDATE = 407;
	int ADMRVP_ANDELETE = 408;
	
	int ADMRVC_DELETE = 409;
	int ADMRVP_DELETE = 410;
	
	int ADM_SALES=411;
	int ADMC_SALES=412;
	int ADMP_SALES=413;
	int ADMT_SALES=414;
	
	// 숙소예약
	int PENSIONSTATE = 50;
	int PENSION_RES = 51;
	int PENSION_SELECT = 52;
	int PENSION_PAYMENT = 53;
	int PENSION_CARD_REGIST = 54;	// 등록된 카드가 없을 때 카드 추가
	int PENSION_MILE = 55;
	
	
	
}