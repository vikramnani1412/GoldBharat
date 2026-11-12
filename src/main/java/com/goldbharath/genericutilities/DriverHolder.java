package com.goldbharath.genericutilities;

import org.openqa.selenium.WebDriver;

/** Gives the TestNG listener access to the browser of the running test. */
public class DriverHolder {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static void set(WebDriver driver) { DRIVER.set(driver); }

    public static WebDriver get() { return DRIVER.get(); }

    public static void clear() { DRIVER.remove(); }
}