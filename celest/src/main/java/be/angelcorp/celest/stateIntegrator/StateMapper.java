package be.angelcorp.celest.stateIntegrator;

import org.apache.commons.math3.linear.ArrayRealVector;

public interface StateMapper<Y> {

    ArrayRealVector mapTo(ArrayRealVector vector, Y state);

    Y extractFrom(ArrayRealVector vector);

}
