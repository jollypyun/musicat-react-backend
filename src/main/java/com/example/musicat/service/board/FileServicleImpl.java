package com.example.musicat.service.board;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.musicat.domain.board.ArticleVO;
import com.example.musicat.domain.board.FileVO;
import com.example.musicat.repository.board.FileDao;
import com.example.musicat.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("fiileService")
public class FileServicleImpl implements FileService {

	@Autowired
	private FileDao fileDao;
	@Autowired
	private FileManager fileManager;
	@Value("${file.dir}")
	private String dirPath;

	// 파일 업로드
	@Override
	public void uploadFile(ArticleVO article) {
		int articleNo = article.getNo();

		if (article.getAttacheFile() != null) { // 첨부파일이 있다면
			if (fileCheck(this.dirPath, article.getAttacheFile().getSystemFileName())) {
				FileVO attachFile = article.getAttacheFile();
				attachFile.setArticleNo(articleNo);
				attachFile.setFileType(0);
				this.fileDao.insertFile(attachFile); // 첨부파일은 맨 마지막으로 가게해서 저장
			}
		}
		if (article.getFileList() != null) { // 이미지 첨부 파일이 있을 때
			List<FileVO> fileList = article.getFileList();
			for (FileVO file : fileList) {
				if (fileCheck(this.dirPath, file.getSystemFileName())) {
					file.setArticleNo(articleNo);
					file.setFileType(1);
					this.fileDao.insertFile(file); // 이미지 파일들 저장
				}
			}
		}
	}

	// 썸네일 생성
	public void createThumbnail(FileVO thumbFile) throws IOException {
		String thumPath = this.dirPath + "thumbnail";
		if (fileCheck(thumPath, thumbFile.getSystemFileName())) {
			this.fileManager.createThumbnail(thumbFile.getSystemFileName());
		}
	}

	// file 꺼내서 추출, retirveArticle에서 사용됨
	public ArticleVO fileList(ArticleVO article, List<FileVO> files) {
		if (files == null) {
			return article;
		}
		List<FileVO> imageFiles = new ArrayList<FileVO>();
		for (FileVO file : files) {
			if (file.getFileType() == 0) {
				article.setAttacheFile(file);
			}
			imageFiles.add(file); // 이미지 파일은 fileType= 1
		}
		article.setFileList(imageFiles); // 결과 저장
		return article;
	}

	// 중복 검사
	private boolean fileCheck(String path, String systemFileName) {
		File dir = new File(path);
		File files[] = dir.listFiles();
		int pos = path.length();
		for (File file : files) {
			log.info("files:" + file.toString());
			String fileName = file.toString().substring(pos + 1);
			if (fileName.equals(systemFileName)) { // 첨부파일 중복검사
				return false;
			}
			if (fileName.equals("thumb" + systemFileName)) { // 썸네일 중복검사
				return false;
			}
		}
		return true;
	}

	@Override
	public FileVO retrieveThumbFile(FileVO file) {
		List<FileVO> files = this.fileDao.selectThumbFile(file);
		if (files.size() > 0) { // 첨부된 이미지 파일이 있으면 FileVO 리턴
			FileVO thumbnail = files.get(0);
			return thumbnail;
		} else {
			return null;
		}
	}
	
	@Override
	public void removeFile(int fileNo) {
		this.fileDao.deleteFile(fileNo);
	}
	
	@Override
	public void allDelete(int articleNo) {
		this.fileDao.allDelete(articleNo);
		
	}
}
