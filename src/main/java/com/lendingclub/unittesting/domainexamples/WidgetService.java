package com.lendingclub.unittesting.domainexamples;

import com.lendingclub.unittesting.accessorcontext.AccessorContext;

public class WidgetService {

    public Widget getStandardizedWidget() {
        Widget w = AccessorContext.get().widget().getWidget();
        if (w != null) {
            w.setStandardized(true);
        }
        return w;
    }

}