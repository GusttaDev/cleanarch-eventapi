package com.project.cleanarch.eventapi.infraestruct.presentation.constants;

public record ApiConstants() {
    
    public record Headers() {
        public static final String X_APP_ORIGIN = "x-app-origin";
        public static final String CONTENT_NEGOTIATION_V1 = "application/vnd.pags.v1+json";
    }

}