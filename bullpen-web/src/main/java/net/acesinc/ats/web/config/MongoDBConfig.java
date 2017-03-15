/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 *
 * @author andrewserff
 */
@Configuration
@PropertySource({"classpath:${spring.profiles.active}/application.properties"})
@Profile("dev")
public class MongoDBConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "bullpen";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient();
    }
}
