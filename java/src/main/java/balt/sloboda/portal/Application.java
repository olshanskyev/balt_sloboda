package balt.sloboda.portal;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by evolshan on 07.07.2021.
 * main class
 */
@SpringBootApplication(scanBasePackages={"balt.sloboda.portal"})
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
