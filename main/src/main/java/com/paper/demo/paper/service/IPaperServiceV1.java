package com.paper.demo.paper.service;

import java.util.List;

import com.paper.demo.paper.domain.PaperDto;
import com.paper.demo.paper.domain.PaperListDto;

public interface IPaperServiceV1 {

	void createPapers(PaperDto.createPaper createPaper);
	void deletePage(Long pageId);
	List<PaperListDto> getPaperList(String accessToken);


}
