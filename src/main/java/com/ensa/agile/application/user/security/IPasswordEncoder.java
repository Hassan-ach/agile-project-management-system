package com.ensa.agile.application.user.security;

import com.ensa.agile.domain.global.annotation.Loggable;
@Loggable
public interface IPasswordEncoder {

    public String encode(final CharSequence pass);

    public boolean matches(final CharSequence rawPass, final String encodedPass);
}
