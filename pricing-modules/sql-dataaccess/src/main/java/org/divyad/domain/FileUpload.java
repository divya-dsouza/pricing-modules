package org.divyad.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "ISACTIVE")
    private Boolean isActive;

    @Column(name = "CreatedDt")
    private Timestamp createdDt;

    @Column(name = "UpdatedDt")
    private Timestamp updatedDt;

}
