package cat.udl.eps.softarch.hello.config;

import cat.udl.eps.softarch.hello.filter.SimpleCORSFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;

/**
 * Created by http://rhizomik.net/~roberto/
 */
public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/api/*");

        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
        FilterRegistration.Dynamic security = servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy());
        security.addMappingForUrlPatterns(dispatcherTypes, true, "/api/*");

        FilterRegistration.Dynamic cors = servletContext.addFilter("simpleCORSFilter", new SimpleCORSFilter());
        cors.addMappingForUrlPatterns(dispatcherTypes, true, "/api/*");

        FilterRegistration.Dynamic httpMethods = servletContext.addFilter("hiddenHttpMethodFilter", new HiddenHttpMethodFilter());
        httpMethods.addMappingForUrlPatterns(dispatcherTypes, true, "/api/*");


        servletContext.addListener(new ContextLoaderListener(rootContext));
    }
}
