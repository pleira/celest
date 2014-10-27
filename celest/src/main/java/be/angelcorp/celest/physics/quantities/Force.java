/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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

import be.angelcorp.celest.math.geometry.Vec3;
import be.angelcorp.celest.math.geometry.Vec3$;

public abstract class Force implements Comparable<Force>, Cloneable {

    private Vec3 force;

    public Vec3 add(Force force2) {
        setForce(getForce().$plus(force2.getForce()));
        return getForce();
    }

    public void add(Vec3 term) {
        setForce(getForce().$plus(term));
    }

    @Override
    public int compareTo(Force o) {
        return Double.compare(getForce().normSq(), o.getForce().normSq());
    }

    public Vec3 getForce() {
        return force;
    }

    public abstract Vec3 getOffset();

    public Vec3 mult(float scalar) {
        setForce(getForce().$times(scalar));
        return getForce();
    }

    public void setForce(Vec3 force) {
        this.force = force;

    }

    @Override
    public String toString() {
        return String.format("[Force f:%s]", getForce());
    }

    public Torque toTorque() {
        return toTorque(Vec3$.MODULE$.zero());
    }

    public Torque toTorque(Vec3 newOrigin) {
        Torque t = new Torque();
        Vec3 dR = getOffset().$minus(newOrigin);
        t.setTorque(dR.cross(getForce()));
        return t;
    }
}
