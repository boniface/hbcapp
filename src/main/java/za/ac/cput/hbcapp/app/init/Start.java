package za.ac.cput.hbcapp.app.init;

import com.vaadin.server.VaadinServlet;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import za.ac.cput.hbcapp.app.conf.AppConfig;
import za.ac.cput.hbcapp.app.conf.WebConfig;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by hashcode on 2014/10/21.
 */
@Configuration
@ComponentScan(basePackages = {"za.ac.cput"})
@EnableWebMvc
@Profile("container")
@EnableScheduling
@EnableAsync
public class Start implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        sc.addListener(new ContextLoaderListener(rootContext));
        rootContext.register(AppConfig.class);


        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(WebConfig.class);
        dispatcherContext.scan("zm.hashcode.mshengu");

        // Register Spring security filter
//        FilterRegistration.Dynamic springSecurityFilterChain = sc.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
//        springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");

        ServletRegistration.Dynamic dispatcher = sc.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        dispatcher.setInitParameter("spring.profiles.active", "container");

        ServletRegistration.Dynamic vaadin = sc.addServlet("gwt", VaadinServlet.class);
        vaadin.setLoadOnStartup(1);
//        vaadin.addMapping("/app/*");
//        vaadin.addMapping("/VAADIN/*");
        vaadin.setAsyncSupported(true);
        vaadin.setInitParameter("UI", "za.ac.cput.hbcapp.HbcMainUI");
        vaadin.setInitParameter("widgetset", "za.ac.cput.hbcapp.AppWidgetSet");
    }
}