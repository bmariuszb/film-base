package zti.filmbase;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**

This aspect class intercepts the execution of the {@link zti.filmbase.JPAResource rateMovie(Integer, String)}

method and performs additional actions before and after the method execution.
*/
@Aspect
public class DatabaseChangeAspect {
    /**

Executes before the {@link zti.filmbase.JPAResource rateMovie(Integer, String)} method is called.
It retrieves the movie ID and rating from the method arguments and prints a message indicating
the creation/update of the movie with the specified rating.
@param joinPoint the join point representing the method execution
*/
    @Before("execution(* zti.filmbase.JPAResource.rateMovie(..))")
    public void beforeRateMovie(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Integer movieId = (Integer) args[0];
        String rating = (String) args[1];
        System.out.println("Create/update movie ID: " + movieId + " a rate: " + rating);
    }

    /**

Executes after the {@link zti.filmbase.JPAResource rateMovie(Integer, String)} method has successfully returned.
It prints a message indicating the successful update of the movie rating.
@param joinPoint the join point representing the method execution
@param result the result returned by the method
*/
    @AfterReturning(pointcut = "execution(* zti.filmbase.JPAResource.rateMovie(..))", returning = "result")
    public void afterRateMovie(JoinPoint joinPoint, Object result) {
        System.out.println("Succesfully update movie rating ");
    }
}
