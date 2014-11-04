package com.octopod.switchcore.event;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
/**
 * @author Octopod - octopodsquad@gmail.com
 *
 * Marks a method as being an EventHandler.
 * The Method being marked should:
 * 	- return void
 * 	- have one argument
 * 	- have an implementation of Event as the first argument
 */
public @interface EventSubscribe
{
	HandlerPriority priority() default HandlerPriority.NORMAL;
}
