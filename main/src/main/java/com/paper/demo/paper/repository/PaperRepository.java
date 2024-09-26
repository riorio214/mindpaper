package com.paper.demo.paper.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paper.demo.paper.domain.Paper;
import com.paper.demo.paper.domain.PaperDto;
import com.paper.demo.paper.domain.PaperListDto;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {
	//
	@Query("SELECT new com.paper.demo.paper.domain.PaperListDto(p.id,p.content) FROM Paper p WHERE p.author.email = :email AND p.deletedYn <> 'y'")
	List<PaperListDto> getPaperList(@Param("email") String email);

	@Modifying
	@Query("UPDATE Paper p SET p.deletedYn= 'Y' WHERE p.id = :paperId")
	void deletePaper(@Param("paperId")Long paperId);

	@Query("SELECT p.content FROM Paper p WHERE p.author.id = :pageId")
	List<String> findContentsByPageId(@Param("pageId") Long pageId);


}
