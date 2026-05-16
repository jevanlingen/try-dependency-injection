package com.di;

import com.di.architecture.SetupConfigurer;

class Startup {
    private static boolean ENABLE_EVENTS = false;
    private static boolean ENABLE_EVENT_EXAMPLE_FLOW = false;
    private static boolean ENABLE_SERVER = true;

    void main() {
        SetupConfigurer.configure(ENABLE_EVENTS, ENABLE_EVENT_EXAMPLE_FLOW, ENABLE_SERVER);
    }
}
