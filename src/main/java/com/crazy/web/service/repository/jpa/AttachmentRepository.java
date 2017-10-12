package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.AttachmentFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface AttachmentRepository  extends JpaRepository<AttachmentFile, String>{
	
	public abstract AttachmentFile findByIdAndOrgi(String id, String orgi);

	public abstract List<AttachmentFile> findByDataidAndOrgi(String dataid, String orgi);

	public abstract List<AttachmentFile> findByModelidAndOrgi(String modelid, String orgi);

	public abstract Page<AttachmentFile> findByOrgi(String orgi, Pageable page);
}

