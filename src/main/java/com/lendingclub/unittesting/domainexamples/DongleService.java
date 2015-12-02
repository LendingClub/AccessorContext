package com.lendingclub.unittesting.domainexamples;

import com.lendingclub.unittesting.accessorcontext.AccessorContext;

public class DongleService {

    public Dongle getCustomizedDongle() {
        Dongle dongle = AccessorContext.get().dongle().getById(1L);
        //dongle business logic
        return dongle;
    }
}
