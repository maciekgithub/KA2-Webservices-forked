package pl.euler.bgs.restapi.core.tracking;

import java.lang.annotation.*;

/**
 * Annotation enables the {@link TrackingAspect} which logs all input and output parameters to the separate log.
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
public @interface Tracked {
    /**
     * @return If {@code true} we will track only errors on that method
     */
    boolean errorsOnly() default false;
}
