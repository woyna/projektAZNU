
package org.bp.media.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.bp.internet package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class InternetObjectFactory {

    private final static QName _CancelOrder_QNAME = new QName("http://internet.bp.org/", "cancelOrder");
    private final static QName _CancelOrderResponse_QNAME = new QName("http://internet.bp.org/", "cancelOrderResponse");
    private final static QName _OrderInternet_QNAME = new QName("http://internet.bp.org/", "orderInternet");
    private final static QName _OrderInternetResponse_QNAME = new QName("http://internet.bp.org/", "orderInternetResponse");
    private final static QName _Fault_QNAME = new QName("http://internet.bp.org/", "Fault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.bp.internet
     * 
     */
    public InternetObjectFactory() {
    }

    /**
     * Create an instance of {@link InternetCancelOrder }
     * 
     */
    public InternetCancelOrder createCancelOrder() {
        return new InternetCancelOrder();
    }

    /**
     * Create an instance of {@link InternetCancelOrderResponse }
     * 
     */
    public InternetCancelOrderResponse createCancelOrderResponse() {
        return new InternetCancelOrderResponse();
    }

    /**
     * Create an instance of {@link OrderInternet }
     * 
     */
    public OrderInternet createOrderInternet() {
        return new OrderInternet();
    }

    /**
     * Create an instance of {@link OrderInternetResponse }
     * 
     */
    public OrderInternetResponse createOrderInternetResponse() {
        return new OrderInternetResponse();
    }

    /**
     * Create an instance of {@link Fault }
     * 
     */
    public Fault createFault() {
        return new Fault();
    }

    /**
     * Create an instance of {@link InternetOrder }
     * 
     */
    public InternetOrder createInternetOrder() {
        return new InternetOrder();
    }

    /**
     * Create an instance of {@link Household }
     * 
     */
    public Household createHousehold() {
        return new Household();
    }

    /**
     * Create an instance of {@link InternetService }
     * 
     */
    public InternetService createInternetService() {
        return new InternetService();
    }

    /**
     * Create an instance of {@link OrderInfo }
     * 
     */
    public OrderInfo createOrderInfo() {
        return new OrderInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InternetCancelOrder }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InternetCancelOrder }{@code >}
     */
    @XmlElementDecl(namespace = "http://internet.bp.org/", name = "cancelOrder")
    public JAXBElement<InternetCancelOrder> createCancelOrder(InternetCancelOrder value) {
        return new JAXBElement<InternetCancelOrder>(_CancelOrder_QNAME, InternetCancelOrder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InternetCancelOrderResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InternetCancelOrderResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://internet.bp.org/", name = "cancelOrderResponse")
    public JAXBElement<InternetCancelOrderResponse> createCancelOrderResponse(InternetCancelOrderResponse value) {
        return new JAXBElement<InternetCancelOrderResponse>(_CancelOrderResponse_QNAME, InternetCancelOrderResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderInternet }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OrderInternet }{@code >}
     */
    @XmlElementDecl(namespace = "http://internet.bp.org/", name = "orderInternet")
    public JAXBElement<OrderInternet> createOrderInternet(OrderInternet value) {
        return new JAXBElement<OrderInternet>(_OrderInternet_QNAME, OrderInternet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderInternetResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OrderInternetResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://internet.bp.org/", name = "orderInternetResponse")
    public JAXBElement<OrderInternetResponse> createOrderInternetResponse(OrderInternetResponse value) {
        return new JAXBElement<OrderInternetResponse>(_OrderInternetResponse_QNAME, OrderInternetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     */
    @XmlElementDecl(namespace = "http://internet.bp.org/", name = "Fault")
    public JAXBElement<Fault> createFault(Fault value) {
        return new JAXBElement<Fault>(_Fault_QNAME, Fault.class, null, value);
    }

}
