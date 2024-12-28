package de.elia.systemclasses.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * This annotation marks that this will be deleted soon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, MODULE, PARAMETER, TYPE})
public @interface ToBeDeleted {

  /**
   * This set the version where this will be deleted
   * @return the version
   */
  String removalVersion() default "unknown";

  /**
   * Set the reason
   * @return the reason
   */
  String reason() default "unknown";

}
