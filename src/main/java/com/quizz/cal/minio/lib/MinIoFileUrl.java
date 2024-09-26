package com.quizz.cal.minio.lib;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "MinIoFileUrl")
public class MinIoFileUrl {
    private String url;
    private String status;
}


