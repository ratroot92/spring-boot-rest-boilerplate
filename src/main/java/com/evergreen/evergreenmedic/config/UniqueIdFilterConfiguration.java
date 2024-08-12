package com.evergreen.evergreenmedic.config;


import com.evergreen.evergreenmedic.filters.UniqueIdientifierFilter;
import lombok.Data;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class UniqueIdFilterConfiguration {

    public static final String DEFAULT_HEADER_TOKEN = "correlationId";
    public static final String DEFAULT_MDC_UUID_TOKEN_KEY = "correlationId";

    private String responseHeader = DEFAULT_HEADER_TOKEN;
    private String mdcKey = DEFAULT_MDC_UUID_TOKEN_KEY;
    private String requestHeader = DEFAULT_HEADER_TOKEN;

    @Bean
    public FilterRegistrationBean<UniqueIdientifierFilter> servletRegistrationBean() {
        final FilterRegistrationBean<UniqueIdientifierFilter> registrationBean = new FilterRegistrationBean<>();
        final UniqueIdientifierFilter log4jMDCFilterFilter = new UniqueIdientifierFilter(responseHeader, mdcKey, requestHeader);
        registrationBean.setFilter(log4jMDCFilterFilter);
        registrationBean.setOrder(2);
        return registrationBean;
    }

}