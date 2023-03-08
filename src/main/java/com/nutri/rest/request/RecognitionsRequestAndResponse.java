package com.nutri.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecognitionsRequestAndResponse {
    private String awardsOrRecognitions;
    private Long yearOfRecognition;
}
