
package org.bp.media.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "internetOrder", propOrder = {
    "household",
    "installmentDate",
    "internetService"
})
public class InternetOrder {

    protected Household household;
    protected Object installmentDate;
    @XmlElement(name = "internetService")
    protected InternetService internetService;

    /**
     * Gets the value of the household property.
     * 
     * @return
     *     possible object is
     *     {@link Household }
     *     
     */
    public Household getHousehold() {
        return household;
    }

    /**
     * Sets the value of the household property.
     * 
     * @param value
     *     allowed object is
     *     {@link Household }
     *     
     */
    public void setHousehold(Household value) {
        this.household = value;
    }

    /**
     * Gets the value of the installmentDate property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getInstallmentDate() {
        return installmentDate;
    }

    /**
     * Sets the value of the installmentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setInstallmentDate(Object value) {
        this.installmentDate = value;
    }

    /**
     * Gets the value of the internetService property.
     * 
     * @return
     *     possible object is
     *     {@link InternetService }
     *     
     */
    public InternetService getInternetService() {
        return internetService;
    }

    /**
     * Sets the value of the internetService property.
     * 
     * @param value
     *     allowed object is
     *     {@link InternetService }
     *     
     */
    public void setInternetService(InternetService value) {
        this.internetService = value;
    }

}
