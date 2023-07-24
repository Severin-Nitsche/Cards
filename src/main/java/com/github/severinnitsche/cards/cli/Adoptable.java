package com.github.severinnitsche.cards.cli;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to indicate a class is cli-compatible.
 * When registered in the configuration, it will be loaded and eligible to execution.
 * It must provide a method <code>adopt(...)</code> with parameters according to the declared options
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Adoptable {
  String METHOD_NAME = "adopt";
}
