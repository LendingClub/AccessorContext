package com.lendingclub.unittesting.domainexamples;

import com.lendingclub.unittesting.accessorcontext.MockableAccessorContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WidgetServiceTest {

    MockableAccessorContext context;
    WidgetService widgetService = new WidgetService();

    @Before
    public void setup() {
        context = new MockableAccessorContext();
        context.establish();
    }

    @Test
    public void getStandardizedWidget_returns_standardized_widget() {
        WidgetAccessor mockWidgetAccessor = mock(WidgetAccessor.class);
        context.setWidget(mockWidgetAccessor);
        Widget unstandardizedWidget = new Widget();
        when(mockWidgetAccessor.getWidget()).thenReturn(unstandardizedWidget);
        Widget widget = widgetService.getStandardizedWidget();
        assertTrue(widget.isStandardized());
    }

    @After
    public void afterTest() {
        if (context != null) {
            context.release();
            context = null;
        }
    }
}