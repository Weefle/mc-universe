package com.octopod.switchcore.event;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface EventHandler
{
	public boolean async() default false;

    public EventPriority priority() default EventPriority.NORMAL;
}
