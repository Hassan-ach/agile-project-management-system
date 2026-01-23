package com.ensa.agile.domain.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,
         ElementType.TYPE})
public @interface Loggable {}
