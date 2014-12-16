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
package com.github.yoosiba.gbsd.billing;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 *
 * @author Jakub Siberski
 */
public class RealBillingService implements BillingService {

    private final Provider<CreditCardProcessor> processorProvider;
    private final Provider<TransactionLog> transactionLogProvider;

    @Inject
    public RealBillingService(Provider<CreditCardProcessor> processorProvider,
            Provider<TransactionLog> transactionLogProvider) {
        this.processorProvider = processorProvider;
        this.transactionLogProvider = transactionLogProvider;
    }

    public Receipt chargeOrder(PizzaOrder order, CreditCard creditCard) {
        CreditCardProcessor processor = processorProvider.get();
        TransactionLog transactionLog = transactionLogProvider.get();

        try {
            ChargeResult result = processor.charge(creditCard, order.getAmount());
            transactionLog.logChargeResult(order.getId(), result);

            return result.wasSuccessful()
                    ? Receipt.forSuccessfulCharge(order.getAmount())
                    : Receipt.forDeclinedCharge(result.getDeclineMessage());
        } catch (RuntimeException e) {

            transactionLog.logConnectException(order.getId(), e);
            return Receipt.forSystemFailure(e.getMessage());
        }
    }
}
