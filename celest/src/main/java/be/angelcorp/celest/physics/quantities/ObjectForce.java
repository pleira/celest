/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.physics.quantities;

import be.angelcorp.celest.body.CelestialBody;
import be.angelcorp.celest.math.geometry.Vec3;
import be.angelcorp.celest.math.geometry.Vec3$;

/**
 * A generic force that is linked to a specific body. It contains a force on the object and an offset to
 * the center of gravity of that object.
 *
 * @author simon
 */
public class ObjectForce extends Force {

    /**
     * Object where the force acts on
     */
    private CelestialBody object;
    /**
     * Offset with respect to its center of gravity
     */
    private Vec3 offset;

    /**
     * Create a new force on a specific object F: <0,0,0> and R: <0,0,0>
     *
     * @param object Object where the force acts on
     */
    public ObjectForce(CelestialBody object) {
        this(object, Vec3$.MODULE$.zero());
    }

    /**
     * Create a new force on a specific object F: given and R: <0,0,0>
     *
     * @param object Object where the force acts on
     * @param force  Force vector
     */
    public ObjectForce(CelestialBody object, Vec3 force) {
        setObject(object);
        setForce(force);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectForce clone() {
        return new ObjectForce(getObject(), Vec3$.MODULE$.apply(getForce()));
    }

    /**
     * @see ObjectForce#object
     */
    public CelestialBody getObject() {
        return object;
    }

    /**
     * @see ObjectForce#offset
     */
    @Override
    public Vec3 getOffset() {
        return offset;
    }

    /**
     * @see ObjectForce#object
     */
    public void setObject(CelestialBody object) {
        this.object = object;
    }

    /**
     * @see ObjectForce#offset
     */
    public void setOffset(Vec3 offset) {
        this.offset = offset;
    }

    /**
     * Compute the (linear) acceleration of the given force on the object
     *
     * @return Acceleration vector a = F/m
     */
    public Vec3 toAcceleration() {
        // F = m a
        // a = F/m
        return getForce().$div(getObject().mass());
    }
}
