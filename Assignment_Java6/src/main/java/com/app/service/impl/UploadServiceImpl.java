package com.app.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.service.UploadService;

import jakarta.servlet.ServletContext;

@Service
public class UploadServiceImpl implements UploadService{

	@Autowired
	ServletContext app;
	
	@Override
	public File save(MultipartFile file, String folder) {
		File dir = new File(app.getRealPath("/" + folder));
		if(!dir.exists()) {
			dir.mkdirs();
		}
		String originalName = file.getOriginalFilename();
		String baseName = originalName.substring(0, originalName.lastIndexOf("."));
		String extension = originalName.substring(originalName.lastIndexOf("."));

		File savedFile = new File(dir, originalName);
		int count = 1;

		// Nếu file đã tồn tại, tạo tên mới tránh trùng
		while (savedFile.exists()) {
			String newName = baseName + "-" + count + extension;
			savedFile = new File(dir, newName);
			count++;
		}
		try {
			file.transferTo(savedFile);
			System.out.println(savedFile.getAbsolutePath());
			return savedFile;
		}catch(Exception e) {
			throw new RuntimeException("Upload fail: " + e.getMessage(), e);
		}
	}
}
