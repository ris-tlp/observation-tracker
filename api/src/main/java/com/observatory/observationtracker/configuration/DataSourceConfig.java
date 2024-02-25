package com.observatory.observationtracker.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
public class DataSourceConfig {
    private final AwsConfig awsConfig;
    private final String rdsUri;
    private final String rdsUsername;
    private final String rdsPassword;
    private final String driverClassName;

    public DataSourceConfig(AwsConfig awsConfig) {
        this.awsConfig = awsConfig;
        this.rdsUri = "jdbc:postgresql://" + awsConfig.getRdsUri() + "/" + awsConfig.getRdsName();
        this.rdsUsername = awsConfig.getRdsUsername();
        this.rdsPassword = awsConfig.getRdsPassword();
        this.driverClassName = org.postgresql.Driver.class.getName();
    }

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.url(rdsUri);
        builder.username(rdsUsername);
        builder.password(rdsPassword);
        builder.driverClassName(driverClassName);

        return builder.build();
    }
}
