package com.quizz.cal.minio.lib;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinIOAnswer {
    private String fileName;
    private String status;
}
