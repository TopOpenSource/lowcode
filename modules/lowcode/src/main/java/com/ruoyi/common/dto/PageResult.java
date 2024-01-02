package com.ruoyi.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
	private PageDto pageDto;
	private List<T> results;

	public PageResult(PageDto pageDto, List<T> results) {
		this.pageDto=pageDto;
		this.results=results;
	}

}
