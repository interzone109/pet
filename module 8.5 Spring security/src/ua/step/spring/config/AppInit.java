package ua.step.spring.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    // Этот метод должен содержать конфигурации которые инициализируют Beans
    // для инициализации бинов у нас использовалась аннотация @Bean
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{
                WebAppConfig.class
        };
    }

    // Тут добавляем конфигурацию, в которой инициализируем ViewResolver
    @Override
    protected Class<?>[] getServletConfigClasses() {

        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}