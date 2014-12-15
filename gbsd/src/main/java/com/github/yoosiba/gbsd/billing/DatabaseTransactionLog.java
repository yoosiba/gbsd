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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Production a.k.a. <i>slow</i> implementation of {@link TransactionLog}
 *
 * @author Jakub Siberski
 */
public class DatabaseTransactionLog implements TransactionLog {

    private HashMap<UUID, ChargeResult> charges = new HashMap<>();
    private HashMap<UUID, RuntimeException> exceptions = new HashMap<>();

    public DatabaseTransactionLog() {
    }

    @Override
    public void logChargeResult(UUID transactionID, ChargeResult result) {
        this.pause();
        charges.put(transactionID, result);
        System.out.println(this.getClass().getName() + " saved charge " + result);
    }

    @Override
    public void logConnectException(UUID transactionID, RuntimeException e) {
        this.pause();
        exceptions.put(transactionID, e);
        System.out.println(this.getClass().getName() + " saved exception " + e);
    }

    @Override
    public boolean wasSuccessLogged(UUID transactionID) {
        this.pause();
        return this.charges.containsKey(transactionID) && this.exceptions.containsKey(transactionID) == false;
    }

    private void pause() {
        try {
            java.lang.Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
    }
}
