package com.paper.demo.paper.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.paper.demo.auth.domain.JsonResponse;
import com.paper.demo.auth.service.SecurityService;
import com.paper.demo.paper.domain.Page;
import com.paper.demo.paper.domain.PaperDto;
import com.paper.demo.paper.domain.PageIdDto;
import com.paper.demo.paper.repository.PageRepository;
import com.paper.demo.paper.repository.PaperRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Service
@Slf4j
@RequiredArgsConstructor
public class PageService implements IPageServiceV1 {
	private final SecurityService securityService;
	private final PageRepository pageRepository;
	private final PaperRepository paperRepository;

	/**
	 * 페이지 타이틀 유효성 검사
	 * @param accessToken
	 * @return
	 */
	public boolean validateTitle(String accessToken) {
		String email = getUserEmail();
		String loginType = getLoginType(accessToken);
		Optional<Page> optionalPage = pageRepository.findByEmailAndLoginType(email, loginType);
		return optionalPage.isPresent();
	}
	/**
	 * 유저 정보 조회
	 * @param accessToken
	 * @return
	 */
	@Override
	public Mono<String> getUserYn(String accessToken) {
		return securityService.validateToken(accessToken)
			.map(responseEntity -> {
				JsonResponse jsonResponse = (JsonResponse)responseEntity.getBody();
				if (jsonResponse != null) {
					if (jsonResponse.getCode() == 200) {
						return "yes";
					}
				}
				return "no";
			});
	}


	/**
	 * 페이지 생성
	 * @param createPage
	 * @param accessToken
	 */
	@Transactional
	@Override
	public void createPage(PaperDto.createPage createPage,String accessToken) {
		if (Boolean.TRUE.equals(getUserYn(accessToken).map(yn -> yn.equals("yes")).block())){
			Page buildPage = Page.builder()
				.email(getUserEmail())
				.title(createPage.getTitle())
				.loginType(getLoginType(accessToken))
				.build();
			pageRepository.save(buildPage);
			log.info("페이지 생성 완료");
		} else {
			throw new RuntimeException("페이지 생성 실패");
		}
	}
	/**
	 * 유저 이메일 조회
	 * @return
	 */
	@Override
	public String getUserEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
			throw new RuntimeException("인증정보가 없거나 잘못된 토큰정보 입니다.");
		}
		return (String) jwt.getClaims().get("email");
	}

	@Override
	public String getLoginType(String accessToken) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
			throw new RuntimeException("인증정보가 없거나 잘못된 토큰정보 입니다.");
		}
		return (String) jwt.getClaims().get("loginType");
	}

	@Override
	public PageIdDto getPageAndPapersByPageId(Long pageId, String accessToken) {
		Page page = pageRepository.findById(pageId)
			.orElseThrow(() -> new RuntimeException("Page not found"));
		List<String> contents = paperRepository.findContentsByPageId(pageId);
		return new PageIdDto(
			page.getEmail(),
			page.getTitle(),
			contents,
			page.getLoginType()
		);
	}

	@Override
	public Long getPageId(){
		String email = getUserEmail();
		System.out.println("pageRepository.findIdByEmail(email) = " + pageRepository.findIdByEmail(email));
		return pageRepository.findIdByEmail(email);
	}

}
