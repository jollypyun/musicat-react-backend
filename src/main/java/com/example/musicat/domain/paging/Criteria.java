package com.example.musicat.domain.paging;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Alias("criteria")
public class Criteria {
	private int currentPageNo;
	private int dataPerPage;
	
	public int getPageStart() {
		return (this.currentPageNo - 1) * dataPerPage;
	}
	
	public Criteria() {
		this.currentPageNo = 1;
		this.dataPerPage = 2;
	}
	
	public void setCurrentPageNo(int page) {
		if(page <= 0) {
			this.currentPageNo= 1;
		}
		else {
			this.currentPageNo = page;
		}
	}
	
	public void setDataPerPage(int num) {
		int cnt = this.dataPerPage;
		
		if(num != cnt) {
			this.dataPerPage = cnt;
		}
		else {
			this.dataPerPage = num;
		}
	}
}
