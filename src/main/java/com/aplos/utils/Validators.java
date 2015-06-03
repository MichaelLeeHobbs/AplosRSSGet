package com.aplos.utils;

import com.aplos.rssget.Constants;
import org.apache.commons.validator.routines.RegexValidator;

/**
 * Project: aplosrssget
 * Created by Michael Hobbs on 4/5/15.
 * email: Michael.Lee.Hobbs@gmail.com
 */
public class Validators {
    private Validators(){}

    /**
     * Validates a <code>candidate</code> against <code>regexValidation</code> and throws an
     * <code>IllegalArgumentException(illegalArgumentExceptionMsg)</code>
     * @param candidate string to validate
     * @param regexValidation regex to validate candidate against
     * @param illegalArgumentExceptionMsg exception message
     */
    public static void RegexValidator(String candidate, String regexValidation, String illegalArgumentExceptionMsg){
        RegexValidator regexValidator = new RegexValidator(regexValidation);
        if ( ! regexValidator.isValid(candidate) ){
            throw new IllegalArgumentException(illegalArgumentExceptionMsg);
        }
    }

    public static void booleanValidator(boolean actual, boolean expected, Exception e) throws Exception {
        if (actual != expected){
            throw e;
        }
    }

}
