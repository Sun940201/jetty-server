
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

/**
 * Created by Administrator on 2016-11-28.
 */
public class Server {
    public static void main(String[] args) throws Exception {

        // 设置log4j配置文件
        PropertyConfigurator.configure("conf/log4j.properties");

        // 设置context属性
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase("src/web");
        org.eclipse.jetty.server.Server jettyServer = new org.eclipse.jetty.server.Server(8888);
        jettyServer.setHandler(context);

        FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");    // allowed origins comma separated
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filterHolder.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        context.addFilter(filterHolder, "/*", null);

        // api接口Servlet
        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/api/*");
        jerseyServlet.setInitOrder(0);
        // 注册jersey的MultiPartFeature， 否则无法上传文件
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
                APIServlet.class.getCanonicalName() + ";org.glassfish.jersey.media.multipart.MultiPartFeature");


        // 静态页面目录
        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
        holderPwd.setInitParameter("dirAllowed", "true");
        context.addServlet(holderPwd, "/");

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jettyServer.stop();
            jettyServer.destroy();
        }

    }
}
