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

import com.github.yoosiba.gbsd.billing.CreditCard;
import com.github.yoosiba.gbsd.billing.CreditCardProcessor;
import com.github.yoosiba.gbsd.billing.FakeCreditCardProcessor;
import com.github.yoosiba.gbsd.billing.InMemoryTransactionLog;
import com.github.yoosiba.gbsd.billing.PizzaOrder;
import com.github.yoosiba.gbsd.billing.RealBillingService;
import com.github.yoosiba.gbsd.billing.Receipt;
import com.github.yoosiba.gbsd.billing.TransactionLog;
import com.google.inject.Provider;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Manual wiring performed. Allows to peek into internals.
 *
 * @author Jakub Siberski
 */
public class BillingService_ManualWiring_Test {

    private final PizzaOrder order = new PizzaOrder(100);
    private final CreditCard creditCard = new CreditCard("1234", 11, 2010);

    RealBillingService billingService;
    Provider<CreditCardProcessor> processorProvider;
    Provider<TransactionLog> transactionLogProvider;

    @Before
    public void setUp() {

        processorProvider = new Provider<CreditCardProcessor>() {
            CreditCardProcessor processor = new FakeCreditCardProcessor();

            @Override
            public CreditCardProcessor get() {
                return this.processor;
            }
        };

        transactionLogProvider = new Provider<TransactionLog>() {
            TransactionLog transactionLog = new InMemoryTransactionLog();

            @Override
            public TransactionLog get() {
                return this.transactionLog;
            }
        };

        billingService = new RealBillingService(processorProvider, transactionLogProvider);
    }

    /**
     * Test of chargeOrder method, of class BillingService.
     */
    @Test
    public void testChargeOrder() {
        CreditCardProcessor processor = processorProvider.get();
        TransactionLog transactionLog = transactionLogProvider.get();
        Receipt receipt = billingService.chargeOrder(order, creditCard);

        assertTrue(receipt.hasSuccessfulCharge());
        assertEquals(100, receipt.getAmountOfCharge());
        assertEquals(creditCard, processor.getCardOfOnlyCharge());
        assertEquals(100, processor.getAmountOfOnlyCharge());
        assertTrue(transactionLog.wasSuccessLogged(order.getId()));
    }

}
