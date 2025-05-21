package com.johndoe.bajajfinserv.dto;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// @Data
// @AllArgsConstructor
public class SolutionRequest {
    private String finalQuery;

    public SolutionRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }
}
