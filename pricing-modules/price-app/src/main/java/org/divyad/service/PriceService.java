package org.divyad.service;

import com.google.gson.Gson;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.divyad.domain.FileUpload;
import org.divyad.domain.PricingModel;
import org.divyad.dto.PricingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@Slf4j
public class PriceService {

    @Autowired
    PriceModelService priceModelService;

    @Autowired
    FileUploadService fileUploadService;

    public static final String CSV_DELIMITER_REGEXP = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
    public static final List<String> csvHeader = Arrays.asList("STOREID", "SKU", "NAME", "PRICE", "DATE");
    public static final String FILE_PATH = "C:\\PricingFiles";

    public String upload(MultipartFile file, String userName) {
        String uploadResponse = null;
        try {
            List<String> data = new ArrayList<>();
            Boolean isValidMetadata = false;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String readLine;
                int lines = 0;
                while ((readLine = reader.readLine()) != null) {
                    if (lines == 0) {
                        isValidMetadata = checkMetadata(readLine, isValidMetadata);
                        if (!isValidMetadata) {
                            uploadResponse = "File is not valid.";
                            break;
                        }
                        lines++;
                        continue;
                    }
                    String[] ar = readLine.split(CSV_DELIMITER_REGEXP);
                    Set<String> dataSet = Arrays.stream(ar).collect(Collectors.toSet());
                    if (dataSet.isEmpty())
                        continue;

                    data.add(readLine);
                    lines++;
                }
            } catch (Exception e) {
                log.error("Exception in reading multipart file", e);
            }
            if (isNull(uploadResponse))
                uploadResponse = convertToPricingModel(data, uploadResponse, file, userName);


        } catch (Exception e) {
            log.error("Exception in upload of PriceService", e);
        }

        return uploadResponse;
    }

    private Boolean checkMetadata(String readLine, Boolean isValidMetadata) {
        String[] columns = (!readLine.trim().isEmpty()) ? readLine.split(",") : new String[0];

        List<String> dataFields = (columns.length > 0) ? Arrays.asList(columns) : new ArrayList<>();
        dataFields.removeAll(Collections.singleton(""));


        if (dataFields != null)
            isValidMetadata = compareHeaders(isValidMetadata, dataFields, csvHeader);

        return isValidMetadata;
    }

    private boolean compareHeaders(Boolean
                                           isPerMetadata, List<String> dataFields, List<String> validMetaData) {
        if (validMetaData != null && validMetaData.size() == dataFields.size()) {
            for (int i = 0; i < validMetaData.size(); i++) {
                if (validMetaData.get(i).equals(dataFields.get(i))) {
                    isPerMetadata = true;
                } else {
                    log.info("Meta data Failed at dataFields : {}, validMetaData :{}",
                            dataFields.get(i), validMetaData.get(i));
                    isPerMetadata = false;
                    break;
                }
            }
        }
        return isPerMetadata;
    }

    private String convertToPricingModel(List<String> data, String uploadResponse, MultipartFile file,
                                         String userName) {
        FileUpload fileUpload = null;
        String filePath = FILE_PATH + File.separator + file.getOriginalFilename();
        try {

            file.transferTo(new File(filePath));
            fileUpload = fileUploadService.addFile(file.getOriginalFilename(), filePath, userName);

            log.info("FileID : {} received", fileUpload);
        } catch (IOException e) {
            log.error("Error in storing fileid details", e);
        }

        List<PricingDto> pricingDtos = new ArrayList<>();
        Gson gson = new Gson();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        AtomicInteger index = new AtomicInteger(1);
        List<PricingModel> pricingModels = new ArrayList<>();

        List<String> errors = new ArrayList<>();

        FileUpload finalFileUpload = fileUpload;

        data.stream().forEach(priceRow -> {

            List<String> rowData = Arrays.asList(priceRow.split(CSV_DELIMITER_REGEXP, -1));

            String row = IntStream.range(0, csvHeader.size()).mapToObj(i -> {
                String a = "\"" + csvHeader.get(i) + "\":\"" + rowData.get(i).replace("\"", "") + "\"";
                return a;
            }).collect(Collectors.joining(","));

            StringBuffer sb = new StringBuffer();
            sb.append("{");
            sb.append(row);
            sb.append("}");

            PricingDto pricingDto = gson.fromJson(sb.toString(), PricingDto.class);
            pricingDtos.add(pricingDto);

            if (!validator.validate(pricingDto).isEmpty())
                errors.add(structureValidationErrors(validator.validate(pricingDto), index.get()));
            else {
                String priceJson = gson.toJson(pricingDto).replaceAll("\"\"", "null");
                PricingModel pricingModel = gson.fromJson(priceJson, PricingModel.class);
                pricingModel.setFileId(finalFileUpload);
                pricingModel.setIsActive(true);
                pricingModel.setCreatedDt(new Timestamp(System.currentTimeMillis()));
                pricingModels.add(pricingModel);
            }
            index.getAndIncrement();
        });

        if (nonNull(pricingModels) && !pricingModels.isEmpty())
            priceModelService.dumpPricingInfo(pricingModels);

        if (nonNull(errors) && !errors.isEmpty())
            uploadResponse = String.join("\n", errors);
        else
            uploadResponse = "File Uploaded Successfully";

        return uploadResponse;
    }

    private String structureValidationErrors(Set<ConstraintViolation<Object>> violations, Integer rowNo) {

        String uploadResponse = "Row : " + rowNo + "\n" +
                violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n"));

        return uploadResponse;
    }

    public List<PricingModel> find(String field, String value) {
        return priceModelService.find(field, value);
    }

}
