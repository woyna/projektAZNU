package org.bp.media.clients;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

@WebServiceClient(name = "OrderInternetServiceService",
                  wsdlLocation = "file:/D:/Programowanko/Projects/Projekt%20AZNU/micro-media-ordering/src/main/resources/internet.wsdl",
                  targetNamespace = "http://internet.bp.org/")
public class OrderInternetServiceService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://internet.bp.org/", "OrderInternetServiceService");
    public final static QName OrderInternetServicePort = new QName("http://internet.bp.org/", "OrderInternetServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/D:/Programowanko/Projects/Projekt%20AZNU/micro-media-ordering/src/main/resources/internet.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(OrderInternetServiceService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/D:/Programowanko/Projects/Projekt%20AZNU/micro-media-ordering/src/main/resources/internet.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public OrderInternetServiceService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public OrderInternetServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public OrderInternetServiceService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public OrderInternetServiceService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public OrderInternetServiceService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public OrderInternetServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns OrderInternetService
     */
    @WebEndpoint(name = "OrderInternetServicePort")
    public OrderInternetService getOrderInternetServicePort() {
        return super.getPort(OrderInternetServicePort, OrderInternetService.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns OrderInternetService
     */
    @WebEndpoint(name = "OrderInternetServicePort")
    public OrderInternetService getOrderInternetServicePort(WebServiceFeature... features) {
        return super.getPort(OrderInternetServicePort, OrderInternetService.class, features);
    }

}
