package com.hyperfresh.mcuniverse.event;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited

/**
 * Marks a method as being an EventHandler.
 * The Method being marked should:
 * 	- return void
 * 	- have one argument
 * 	- have an implementation of Event as the first argument
 *
 * @author octopod
 */
public @interface EventHandler
{
	//HandlerPriority priority() default HandlerPriority.NORMAL;
}
