package com.paper.demo.paper.service;

import java.util.List;

import com.paper.demo.paper.domain.Page;
import com.paper.demo.paper.domain.PaperDto;
import com.paper.demo.paper.domain.PageIdDto;

import reactor.core.publisher.Mono;

public interface IPageServiceV1 {
	Mono<String> getUserYn(String accessToken);
	String getUserEmail();
	void createPage(PaperDto.createPage createPage,String accessToken);
	boolean validateTitle(String accessToken);

	String getLoginType(String accessToken);

	PageIdDto getPageAndPapersByPageId(Long pageId , String accessToken);
	Long getPageId();
}
