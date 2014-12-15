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

/**
 *
 * @author Jakub Siberski
 */
public class App {

    public static void main(String[] args) {
        CreditCardProcessor processor = new PaypalCreditCardProcessor();
        TransactionLog transactionLog = new DatabaseTransactionLog();
        BillingService billingService = new RealBillingService(processor, transactionLog);

        PizzaOrder order = new PizzaOrder(100);
        CreditCard creditCard = new CreditCard("1234", 11, 2010);

        Receipt receipt = billingService.chargeOrder(order, creditCard);

        System.out.println("receipt.hasSuccessfulCharge : " + receipt.hasSuccessfulCharge());
        System.out.println("receipt.getAmountOfCharge : " + receipt.getAmountOfCharge());
        System.out.println("processor.getCardOfOnlyCharge : " + processor.getCardOfOnlyCharge());
        System.out.println("processor.getAmountOfOnlyCharge : " + processor.getAmountOfOnlyCharge());
        System.out.println("transactionLog.wasSuccessLogged : " + transactionLog.wasSuccessLogged());
    }
}
