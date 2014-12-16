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

import java.util.HashMap;
import java.util.UUID;

/**
 * Test a.k.a. <i>fast</i> implementation of {@link TransactionLog}
 *
 * @author Jakub Siberski
 */
public class InMemoryTransactionLog implements TransactionLog {

    private HashMap<UUID, ChargeResult> charges = new HashMap<>();
    private HashMap<UUID, RuntimeException> exceptions = new HashMap<>();
   
    @Override
    public void logChargeResult(UUID transactionID, ChargeResult result) {
        charges.put(transactionID, result);
        System.out.println(this.getClass().getName() + " saved charge " + result);
    }

    @Override
    public void logConnectException(UUID transactionID, RuntimeException e) {
        exceptions.put(transactionID, e);
        System.out.println(this.getClass().getName() + " saved exception " + e);
    }

    @Override
    public boolean wasSuccessLogged(UUID transactionID) {
        return this.charges.containsKey(transactionID) && this.exceptions.containsKey(transactionID) == false;
    }

}
