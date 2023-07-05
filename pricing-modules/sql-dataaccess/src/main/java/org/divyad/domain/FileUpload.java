package org.divyad.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder", setterPrefix = "with")
@Entity
@Table(name = "FILEUPLOAD")
public class FileUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILEID")
    private Long fileID;

    @Column(name = "FILENAME")
    private String fileName;

    @Column(name = "FILEPATH")
    private String filePath;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "ISACTIVE")
    private Boolean isActive;

    @Column(name = "CreatedDt")
    private Timestamp createdDt;

    @Column(name = "UpdatedDt")
    private Timestamp updatedDt;

}
