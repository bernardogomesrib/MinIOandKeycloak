package com.quizz.cal.minio.lib;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "MinIOFileNameAnswer")
public class MinIOFileNameAnswer {
    private String fileName;
    private String status;
}
