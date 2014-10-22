package com.octopod.switchcore.event;

import com.octopod.util.event.v2.EventPriority;

import java.lang.annotation.*;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface EventHandler
{
	com.octopod.util.event.v2.EventPriority priority() default EventPriority.NORMAL;
}
