package org.bp.media.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.bp.tv package. 
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
public class TVObjectFactory {

    private final static QName _TVCancelOrder_QNAME = new QName("http://tv.bp.org/", "TVCancelOrder");
    private final static QName _TVCancelOrderResponse_QNAME = new QName("http://tv.bp.org/", "TVCancelOrderResponse");
    private final static QName _TVGetOrderInfo_QNAME = new QName("http://tv.bp.org/", "TVGetOrderInfo");
    private final static QName _TVGetOrderInfoResponse_QNAME = new QName("http://tv.bp.org/", "TVGetOrderInfoResponse");
    private final static QName _OrderTV_QNAME = new QName("http://tv.bp.org/", "orderTV");
    private final static QName _OrderTVResponse_QNAME = new QName("http://tv.bp.org/", "orderTVResponse");
    private final static QName _Fault_QNAME = new QName("http://tv.bp.org/", "Fault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.bp.tv
     * 
     */
    public TVObjectFactory() {
    }

    /**
     * Create an instance of {@link TVCancelOrder }
     * 
     */
    public TVCancelOrder createTVCancelOrder() {
        return new TVCancelOrder();
    }

    /**
     * Create an instance of {@link TVCancelOrderResponse }
     * 
     */
    public TVCancelOrderResponse createTVCancelOrderResponse() {
        return new TVCancelOrderResponse();
    }

    /**
     * Create an instance of {@link TVGetOrderInfo }
     * 
     */
    public TVGetOrderInfo createTVGetOrderInfo() {
        return new TVGetOrderInfo();
    }

    /**
     * Create an instance of {@link TVGetOrderInfoResponse }
     * 
     */
    public TVGetOrderInfoResponse createTVGetOrderInfoResponse() {
        return new TVGetOrderInfoResponse();
    }

    /**
     * Create an instance of {@link OrderTV }
     * 
     */
    public OrderTV createOrderTV() {
        return new OrderTV();
    }

    /**
     * Create an instance of {@link OrderTVResponse }
     * 
     */
    public OrderTVResponse createOrderTVResponse() {
        return new OrderTVResponse();
    }

    /**
     * Create an instance of {@link Fault }
     * 
     */
    public Fault createFault() {
        return new Fault();
    }

    /**
     * Create an instance of {@link OrderInfo }
     * 
     */
    public OrderInfo createOrderInfo() {
        return new OrderInfo();
    }

    /**
     * Create an instance of {@link TvOrder }
     * 
     */
    public TVOrder createTvOrder() {
        return new TVOrder();
    }

    /**
     * Create an instance of {@link Household }
     * 
     */
    public Household createHousehold() {
        return new Household();
    }

    /**
     * Create an instance of {@link TvService }
     * 
     */
    public TVService createTvService() {
        return new TVService();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TVCancelOrder }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TVCancelOrder }{@code >}
     */
    @XmlElementDecl(namespace = "http://tv.bp.org/", name = "TVCancelOrder")
    public JAXBElement<TVCancelOrder> createTVCancelOrder(TVCancelOrder value) {
        return new JAXBElement<TVCancelOrder>(_TVCancelOrder_QNAME, TVCancelOrder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TVCancelOrderResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TVCancelOrderResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://tv.bp.org/", name = "TVCancelOrderResponse")
    public JAXBElement<TVCancelOrderResponse> createTVCancelOrderResponse(TVCancelOrderResponse value) {
        return new JAXBElement<TVCancelOrderResponse>(_TVCancelOrderResponse_QNAME, TVCancelOrderResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TVGetOrderInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TVGetOrderInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://tv.bp.org/", name = "TVGetOrderInfo")
    public JAXBElement<TVGetOrderInfo> createTVGetOrderInfo(TVGetOrderInfo value) {
        return new JAXBElement<TVGetOrderInfo>(_TVGetOrderInfo_QNAME, TVGetOrderInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TVGetOrderInfoResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TVGetOrderInfoResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://tv.bp.org/", name = "TVGetOrderInfoResponse")
    public JAXBElement<TVGetOrderInfoResponse> createTVGetOrderInfoResponse(TVGetOrderInfoResponse value) {
        return new JAXBElement<TVGetOrderInfoResponse>(_TVGetOrderInfoResponse_QNAME, TVGetOrderInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderTV }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OrderTV }{@code >}
     */
    @XmlElementDecl(namespace = "http://tv.bp.org/", name = "orderTV")
    public JAXBElement<OrderTV> createOrderTV(OrderTV value) {
        return new JAXBElement<OrderTV>(_OrderTV_QNAME, OrderTV.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderTVResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OrderTVResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://tv.bp.org/", name = "orderTVResponse")
    public JAXBElement<OrderTVResponse> createOrderTVResponse(OrderTVResponse value) {
        return new JAXBElement<OrderTVResponse>(_OrderTVResponse_QNAME, OrderTVResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     */
    @XmlElementDecl(namespace = "http://tv.bp.org/", name = "Fault")
    public JAXBElement<Fault> createFault(Fault value) {
        return new JAXBElement<Fault>(_Fault_QNAME, Fault.class, null, value);
    }

}
