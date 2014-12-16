/*
 * The MIT License
 *
 * Copyright 2014 Jakub Siberski.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.yoosiba.gbsd;

import com.github.yoosiba.gbsd.billing.BillingService;
import com.github.yoosiba.gbsd.billing.BillingTestModule;
import com.github.yoosiba.gbsd.billing.CreditCard;
import com.github.yoosiba.gbsd.billing.PizzaOrder;
import com.github.yoosiba.gbsd.billing.Receipt;
import com.google.inject.Guice;
import com.google.inject.Inject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Uses {@link BillingTestModule} to wire objects
 *
 * @author Jakub Siberski
 */
public class BillingService_Injection_Test {

    private final PizzaOrder order = new PizzaOrder(100);
    private final CreditCard creditCard = new CreditCard("1234", 11, 2010);

    @Inject
    BillingService billingService;

    @Before
    public void setUp() {
        Guice.createInjector(new BillingTestModule()).injectMembers(this);
    }

    /**
     * Test of chargeOrder method, of class RealBillingService.
     */
    @Test
    public void testChargeOrder() {
        Receipt receipt = billingService.chargeOrder(order, creditCard);

        assertTrue(receipt.hasSuccessfulCharge());
        assertEquals(100, receipt.getAmountOfCharge());
    }

}
