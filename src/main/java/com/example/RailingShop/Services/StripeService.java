package com.example.RailingShop.Services;


import com.example.RailingShop.Entities.OrderProduct;
import com.example.RailingShop.Enums.Cart;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class StripeService {

    public Session createCheckoutSession(Cart cart) throws StripeException {
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (OrderProduct item : cart.getOrderProducts()) {
            lineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("bgn")
                                            .setUnitAmount(item.getProduct().getPrice().multiply(BigDecimal.valueOf(100)).longValue()) // Stripe expects amount in cents
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(item.getProduct().getName())
                                                            .build())
                                            .build())
                            .setQuantity(Long.valueOf(item.getQuantity()))
                            .build());
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8081/order/success")
                .setCancelUrl("http://localhost:8081/order/cancel")
                .addAllLineItem(lineItems)
                .build();

        return Session.create(params);
    }
}
