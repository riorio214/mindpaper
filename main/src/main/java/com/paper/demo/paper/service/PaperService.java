package com.paper.demo.paper.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.paper.demo.paper.domain.Paper;
import com.paper.demo.paper.domain.PaperDto;
import com.paper.demo.paper.domain.PaperListDto;
import com.paper.demo.paper.repository.PageRepository;
import com.paper.demo.paper.repository.PaperRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaperService implements IPaperServiceV1 {

	private final PageRepository pageRepository;
	private final PaperRepository paperRepository;
	private final PageService pageService;
	/**
	 * 사용자가 작성한 페이퍼를 생성하는 메서드
	 * @param createPaper
	 */
	@Override
	@Transactional
	public void createPapers(PaperDto.createPaper createPaper) {
		Paper paper = Paper.builder()
			.deletedYn("N")
			.content(createPaper.getContent())
			.author(pageRepository.findByEmail(pageService.getUserEmail()))
			.build();
		paperRepository.save(paper);
		log.info("페이퍼 생성 완료");
	}
	/**
	 * 사용자가 작성한 페이퍼들을 조회하는 메서드
	 * @param accessToken
	 * @return
	 */
	@Override
	@Transactional
	public List<PaperListDto> getPaperList(String accessToken){
		return paperRepository.getPaperList(pageService.getUserEmail());
	}
	/**
	 * 해당 페이퍼를 삭제하는 메서드
	 * @param paperId
	 */
	@Override
	@Transactional
	public void deletePage(Long paperId){
		if (paperRepository.findById(paperId).isEmpty()) {
			throw new IllegalArgumentException("해당 페이퍼가 존재하지 않습니다.");
		}
		else{
			paperRepository.deletePaper(paperId);
		}
	}
}


