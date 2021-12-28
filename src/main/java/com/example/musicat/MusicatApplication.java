package com.example.musicat;

//import com.example.musicat.repository.board.ArticleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = {"com.example"})
@ComponentScan(basePackages = {"com.example"})
public class MusicatApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // TODO Auto-generated method stub
        return builder.sources(MusicatApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MusicatApplication.class, args);
    }
}
