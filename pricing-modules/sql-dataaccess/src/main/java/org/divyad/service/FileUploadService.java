package org.divyad.service;

import lombok.extern.slf4j.Slf4j;
import org.divyad.domain.FileUpload;
import org.divyad.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileUploadService {

    @Autowired
    FileUploadRepository fileUploadRepository;

    public FileUpload addFile(String fileName, String filePath) {
        FileUpload fileUpload = null;
        try {
            fileUpload = FileUpload.builder().withFileName(fileName)
                    .withFilePath(filePath).build();
            fileUploadRepository.save(fileUpload);

        } catch (Exception e){
            log.error("Error in persisting file", e);
        }

        return fileUpload;
    }
}
