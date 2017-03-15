/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.config;

import java.util.Properties;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 *
 * @author andrewserff
 */
@Configuration
@Profile("cloud")
public class CloudConfig extends AbstractCloudConfig {

    @Bean
    public MongoDbFactory mongoDbFactory() {
        return connectionFactory().mongoDbFactory();
    }
    
    @Bean
   public Properties cloudProperties() {
       return properties();
   }

}
