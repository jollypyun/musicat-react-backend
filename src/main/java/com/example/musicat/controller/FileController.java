package com.example.musicat.controller;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;

import com.example.musicat.repository.board.FileDao;
import com.example.musicat.util.FileManager;
import com.example.musicat.domain.board.FileVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileController {

	private final FileManager fileManager;
	private final FileDao fileDao;
	
	// 이미지 다운로드
	@ResponseBody
	@GetMapping("/images/{filename}")
	public Resource downLoadImage(@PathVariable String filename) throws MalformedURLException{
		return new UrlResource("file:" + fileManager.getFullPath(filename));
	}

	// 썸네일 출력
	@ResponseBody
	@GetMapping("/thumbnailImages/{filename}")
	public Resource downLoadThumbnailImage(@PathVariable String filename) throws MalformedURLException{
		String thumPath = "thumbnail /thumb" + filename;
		log.info("thumbPath:" +thumPath);
		log.info("GetPufsflasthumbPath:" + fileManager.getFullPath(thumPath));
		return new UrlResource("file:" + fileManager.getFullPath(thumPath));
	}
	
	

	// 첨부파일 다운로드
	@GetMapping("/attach/{fileNo}")
	public ResponseEntity<Resource> downloadAttach(@PathVariable int fileNo) throws MalformedURLException{
		FileVO file = this.fileDao.selectFile(fileNo);
		String systemFileName = file.getSystemFileName();
		String originalFileName = file.getOriginalFileName();
		
		UrlResource resource = new UrlResource("file:" + fileManager.getFullPath(systemFileName));
		
		log.info("originFileName= {}",originalFileName);
		
		String encodedUploadFileName = UriUtils.encode(originalFileName, StandardCharsets.UTF_8);
		String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(resource);
	}
	
}
