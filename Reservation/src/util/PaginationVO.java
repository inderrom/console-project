package util;

public class PaginationVO {
	private int totalRecord;
	private int totalPage;
	private int currentPage;
	private int screenSize = 10;
	private int blockSize = 5;
	private int startRow;
	private int endRow;
	private int startPage;
	private int endPage;
	
	public String getPagingText() {
		String str = "";
		
		if(startPage > 1) {
			str += "이전"; 
		}
		for(int i = startPage; i <= (endPage < totalPage ? endPage : totalPage); i++) {
			if(i == currentPage) {
				str += " 현재 ";
			}else {
				str += " " + i + " ";
			}
		}
		if(endPage < totalPage) {
			str += "다음";
		}
		str += "\n";
		return str;
	}
	
	public void setTotalRecord(int totalRecord) {
		this.totalPage = totalRecord;
		totalPage = (int)Math.ceil(totalRecord / (double)screenSize);
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		endRow = currentPage * screenSize;
		startRow = endRow - (screenSize - 1);
		endPage = (currentPage + (blockSize - 1)) / blockSize * blockSize;
		startPage = endPage - (blockSize - 1);
	}
}
