package zti.filmbase;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class DatabaseChangeAspect {
    @Before("execution(* zti.filmbase.JPAResource.rateMovie(..))")
    public void beforeRateMovie(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Integer movieId = (Integer) args[0];
        String rating = (String) args[1];
        System.out.println("Create/update movie ID: " + movieId + " a rate: " + rating);
    }

    @AfterReturning(pointcut = "execution(* zti.filmbase.JPAResource.rateMovie(..))", returning = "result")
    public void afterRateMovie(JoinPoint joinPoint, Object result) {
        System.out.println("Succesfully update movie rating ");
    }
}
