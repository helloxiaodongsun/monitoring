package com.pactera.monitoring.core.page;

import com.github.pagehelper.PageInfo;

public class PageInfoSetVal {

	public static void setVal(PageInfo<?> pageDateResult, PageInfo<?> pageDate) {
		pageDateResult.setPageNum(pageDate.getPageNum());
		pageDateResult.setPageSize(pageDate.getPageSize());
		pageDateResult.setSize(pageDate.getSize());
		pageDateResult.setStartRow(pageDate.getStartRow());
		pageDateResult.setEndRow(pageDate.getEndRow());
		pageDateResult.setTotal(pageDate.getTotal());
		pageDateResult.setPages(pageDate.getPages());
		pageDateResult.setPrePage(pageDate.getPrePage());
		pageDateResult.setNextPage(pageDate.getNextPage());
	}

}
