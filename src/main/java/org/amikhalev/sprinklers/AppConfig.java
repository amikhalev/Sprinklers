package org.amikhalev.sprinklers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.amikhalev.sprinklers.adapters.SectionAdapter;
import org.amikhalev.sprinklers.service.Section;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by alex on 4/29/15.
 */
@Configuration
public class AppConfig {
    @Bean
    public static Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Section.class, new SectionAdapter())
                .create();
    }
}
