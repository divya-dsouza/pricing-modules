package org.divyad.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PRICE")
public class PricingModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "FILEID", referencedColumnName = "FILEID")
    private FileUpload fileId;

    @Column(name = "STOREID")
    @SerializedName(value = "STOREID")
    private Long storeId;

    @Column(name = "SKU")
    @SerializedName(value = "SKU")
    private String sku;

    @Column(name = "PRODUCT")
    @SerializedName(value = "NAME")
    private String product;

    @Column(name = "PRICE")
    @SerializedName(value = "PRICE")
    private Double price;

    @Column(name = "DATE")
    @SerializedName(value = "DATE")
    private Timestamp date;

    @Column(name = "ISACTIVE")
    private Boolean isActive;

    @Column(name = "CREATEDDT")
    private Timestamp createdDt;

    @Column(name = "UPDATEDDT")
    private Timestamp updatedDt;
}
