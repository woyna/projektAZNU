package org.bp.media.clients;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import javax.xml.ws.Service;

@WebServiceClient(name = "OrderTvServiceService",
                  wsdlLocation = "file:/D:/Programowanko/Projects/Projekt%20AZNU/micro-media-ordering/src/main/resources/tv.wsdl",
                  targetNamespace = "http://tv.bp.org/")
public class OrderTvServiceService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://tv.bp.org/", "OrderTvServiceService");
    public final static QName OrderTvServicePort = new QName("http://tv.bp.org/", "OrderTvServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/D:/Programowanko/Projects/Projekt%20AZNU/micro-media-ordering/src/main/resources/tv.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(OrderTvServiceService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/D:/Programowanko/Projects/Projekt%20AZNU/micro-media-ordering/src/main/resources/tv.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public OrderTvServiceService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public OrderTvServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public OrderTvServiceService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public OrderTvServiceService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public OrderTvServiceService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public OrderTvServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns OrderTvService
     */
    @WebEndpoint(name = "OrderTvServicePort")
    public OrderTvService getOrderTvServicePort() {
        return super.getPort(OrderTvServicePort, OrderTvService.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns OrderTvService
     */
    @WebEndpoint(name = "OrderTvServicePort")
    public OrderTvService getOrderTvServicePort(WebServiceFeature... features) {
        return super.getPort(OrderTvServicePort, OrderTvService.class, features);
    }

}
