package org.acme;

import io.quarkus.logging.Log;
import org.acme.annotations.PaymentType;
import org.acme.enums.PaymentMode;
import org.acme.interfaces.PaymentService;
import org.jboss.resteasy.reactive.RestQuery;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("hello")
public class HelloResource {

    @Inject
    HelloService helloService;

    @Inject
    @PaymentType(PaymentMode.PAYPAL)
    PaymentService paypal;

    @Inject
    @PaymentType(PaymentMode.CREDIT_CARD)
    PaymentService credit;

    @GET
    public String hello(@RestQuery String name) {
        credit.process();
        paypal.process();
        return helloService.createHelloMessage(name);
    }
}
